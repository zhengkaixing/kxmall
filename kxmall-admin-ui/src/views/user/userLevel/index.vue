<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['user:userLevel:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="userLevelList">
      <el-table-column type="index" label="序号" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="等级图标" align="center">
        <template slot-scope="{row}">
          <el-link :href="row.icon[0]?row.icon[0].url:''" target="_blank" :underline="false">
            <el-image :src="row.icon[0]?row.icon[0].url:''" title="点击打开" class="el-avatar" />
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="等级名称" align="center" />
      <el-table-column prop="grade" label="会员等级" align="center" />
      <el-table-column prop="discount" label="享受折扣" header-align="center" align="right" />
      <el-table-column prop="validDate" label="有效时间" align="center" />
      <el-table-column label="是否永久" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.isForever === 1" style="cursor: pointer" :type="''">是</el-tag>
          <el-tag v-else style="cursor: pointer" :type="'info'">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否显示" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.isShow === 1" style="cursor: pointer" :type="''">是</el-tag>
          <el-tag v-else style="cursor: pointer" :type="'info'">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button
            v-hasPermi="['user:userLevel:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['user:userLevel:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(row)"
          >删除</el-button>
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

    <!-- 添加或修改会员等级对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="等级名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="是否永久">
          <el-radio-group v-model="form.isForever">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="有效时间(天)">
          <el-input-number v-model="form.validDate" />
        </el-form-item>
        <el-form-item label="会员等级">
          <el-input-number v-model="form.grade" />
        </el-form-item>
        <el-form-item label="享受折扣">
          <el-input-number v-model="form.discount" />
        </el-form-item>
        <el-form-item label="会员背景">
          <imageUpload v-model="form.image" :limit="1" value-type="json" />
        </el-form-item>
        <el-form-item label="会员图标">
          <imageUpload v-model="form.icon" :limit="1" value-type="json" />
        </el-form-item>
        <el-form-item label="是否显示">
          <el-radio-group v-model="form.isShow">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="form.explain" rows="3" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUserLevel, getUserLevel, delUserLevel, addUserLevel, updateUserLevel } from '@/api/user/userLevel'

export default {
  name: 'UserLevel',
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 会员等级表格数据
      userLevelList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单参数
      form: {
        image: [],
        icon: []
      },
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询会员等级列表 */
    getList() {
      this.loading = true
      this.userLevelList = []
      listUserLevel(this.queryParams).then(response => {
        this.userLevelList = response.rows
        this.total = response.total
      }).finally(() => {
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        name: undefined,
        isForever: 1,
        validDate: undefined,
        grade: undefined,
        discount: undefined,
        image: [],
        icon: [],
        isShow: 1,
        explain: undefined
      }
      this.resetForm('form')
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加会员等级'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true
      this.reset()
      const id = row.id || this.ids
      getUserLevel(id).then(response => {
        this.loading = false
        this.form = response.data
        // this.form.image = this.form.image
        // this.form.icon = this.form.icon
        this.open = true
        this.title = '修改会员等级'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          const { icon, image } = this.form
          const params = { ...this.form }
          if (typeof image === 'string') {
            params.image = JSON.parse(image)
          }
          if (typeof icon === 'string') {
            params.icon = JSON.parse(icon)
          }
          if (this.form.id != null) {
            updateUserLevel(params).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addUserLevel(params).then(response => {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true
        return delUserLevel(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>
