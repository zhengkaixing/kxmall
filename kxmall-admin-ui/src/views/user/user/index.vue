<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="" prop="value">
        <el-input v-model="queryParams.value" placeholder="输入搜索内容" clearable />
      </el-form-item>
      <el-form-item label="" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="类型">
          <el-option v-for="item in userTypes" :key="item.value" :value="item.value" :label="item.label" />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="userType">
        <el-select v-model="queryParams.userType" clearable placeholder="会员来源">
          <el-option v-for="item in userSources" :key="item.value" :value="item.value" :label="item.label" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="userList">
      <el-table-column type="index" label="序号" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="会员昵称" header-align="center" />
      <el-table-column label="会员头像" align="center">
        <template slot-scope="{row}">
          <el-link :href="row.avatar" target="_blank" :underline="false">
            <el-image :src="row.avatar" title="点击打开" class="el-avatar" />
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号码" align="center" />
      <el-table-column prop="nowMoney" label="会员余额" header-align="center" align="right" />
      <el-table-column prop="brokeragePrice" label="佣金金额" header-align="center" align="right" />
      <el-table-column prop="integral" label="会员积分" header-align="center" />
      <el-table-column prop="createTime" label="创建日期" width="170" align="center" />
      <el-table-column label="状态" align="center">
        <template slot-scope="{row,$index}">
          <div @click="switchStatus($index)">
            <el-tag v-if="row.status == 1" style="cursor: pointer" :type="''">正常</el-tag>
            <el-tag v-else style="cursor: pointer" :type="'info'">禁用</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="会员来源" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.userType == 'wechat'">公众号</el-tag>
          <el-tag v-else-if="row.userType == 'routine'">小程序</el-tag>
          <el-tag v-else>H5</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="spreadUid" label="推荐人" header-align="center" />
      <el-table-column prop="payCount" label="购买次数" header-align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{$index}">
          <el-dropdown
            v-hasPermi="['user:user:edit']"
            trigger="click"
            @command="command => onOpenDialog($index, command)"
          >
            <el-button type="primary" size="small">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="spread">
                查看下级
              </el-dropdown-item>
              <el-dropdown-item command="user">
                修改会员
              </el-dropdown-item>
              <el-dropdown-item command="money">
                修改余额
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form v-if="dialogMode.current !== 'spread'" ref="form" :model="form" label-width="75px">
        <template v-if="dialogMode.current === 'money'">
          <el-form-item label="会员昵称">
            <el-input v-model="form.nickname" :disabled="true" />
          </el-form-item>
          <el-form-item label="修改余额">
            <el-radio v-model="form.ptype" :label="1">增加</el-radio>
            <el-radio v-model="form.ptype" :label="2">减少</el-radio>
          </el-form-item>
          <el-form-item label="会员余额">
            <el-input v-model="form.money" />
          </el-form-item>
        </template>
        <template v-else-if="dialogMode.current === 'user'">
          <el-form-item label="会员昵称">
            <el-input v-model="form.nickname" :disabled="true" />
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input v-model="form.realName" />
          </el-form-item>
          <el-form-item label="会员备注">
            <el-input v-model="form.mark" />
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="会员积分">
            <el-input v-model="form.integral" />
          </el-form-item>
          <el-form-item label="商户管理">
            <el-radio v-model="form.adminid" :label="1">开启</el-radio>
            <el-radio v-model="form.adminid" :label="0">关闭</el-radio>
          </el-form-item>
        </template>
      </el-form>
      <el-tabs v-else v-model="activeTab" type="border-card" @tab-click="onTabClick">
        <el-tab-pane label="一级" name="1" />
        <el-tab-pane label="二级" name="2" />
        <el-table :data="form.list" border>
          <el-table-column prop="nickname" label="姓名" width="180" header-align="center" />
          <el-table-column label="头像" align="center">
            <template slot-scope="{row}">
              <el-link :href="row.avatar" target="_blank" :underline="false">
                <el-image :src="row.avatar" title="点击打开" class="el-avatar" />
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="加入时间" width="170" align="center" />
        </el-table>
      </el-tabs>
      <div v-if="dialogMode.current !== 'spread'" slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, addUser, updateUser, onStatus, spread, money } from '@/api/user/user'

export default {
  name: 'User',
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 会员表格数据
      userList: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: '',
        value: '',
        userType: ''
      },
      // 表单参数
      form: {},
      userTypes: [
        { value: 'nickname', label: '会员昵称' },
        { value: 'phone', label: '手机号码' }
      ],
      userSources: [
        { value: 'routine', label: '小程序' },
        { value: 'wechat', label: '公众号' },
        { value: 'H5', label: 'H5' }
      ],
      dialogMode: {
        list: {
          spread: {
            title: '查看分销下级'
          },
          money: {
            title: '修改会员余额'
          },
          user: {
            title: '编辑会员'
          }
        },
        current: 'user'
      },
      activeTab: '1'
    }
  },
  computed: {
    // 弹出层标题
    title() {
      const { form, dialogMode } = this
      const { list, current } = dialogMode
      let { title } = list[current]
      if (current === 'spread') {
        const { nickname } = form
        title = `查看${nickname}的分销下级`
      }
      return title
    }
  },
  created() {
    this.getList()
  },
  methods: {
    onStatus(id, status) {
      onStatus(id, { status }).then(({ data }) => {
        this.$message({
          message: '操作成功',
          type: 'success',
          duration: 1000,
          onClose: () => {
            this.getList()
          }
        })
      })
    },
    money(form) {
      return new Promise(resolve => {
        money({ ...form }).then(() => {
          resolve()
        })
      })
    },
    spread() {
      return new Promise(resolve => {
        spread({ uid: this.form.uid, grade: parseInt(this.activeTab) }).then(({ data }) => {
          this.form.list = data
          resolve()
        })
      })
    },
    /** 查询会员列表 */
    getList() {
      this.loading = true
      listUser(this.queryParams).then(({ rows, total }) => {
        this.userList = rows
        this.total = total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    async reset(index, command) {
      this.$_currentSubmitMethod = updateUser
      if (!command) {
        this.form = {}
      } else {
        const user = this.userList[index]
        const { nickname = '', uid } = user
        const forms = {
          user: {
            ...user
          },
          money: {
            nickname, ptype: 1, money: 0, uid
          },
          spread: {
            nickname,
            list: [],
            uid
          }
        }
        this.form = forms[command]
        if (command === 'spread') {
          await this.spread()
        }
        if (command === 'money') {
          this.$_currentSubmitMethod = money
        }
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.uid != null) {
            this.$_currentSubmitMethod(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        }
      })
    },
    switchStatus(index) {
      const { uid, status } = this.userList[index]
      let text = '禁用'; let next = 0
      if (status === 0) {
        text = '启用'
        next = 1
      }
      this.$confirm(`确定进行${text}操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.onStatus(uid, next)
        })
    },
    onOpenDialog(index, command) {
      this.dialogMode.current = command
      this.reset(index, command)
      this.open = true
    },
    onTabClick() {
      this.spread()
    }
  }
}
</script>
