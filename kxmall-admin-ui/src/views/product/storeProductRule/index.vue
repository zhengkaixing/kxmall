<template>
  <div class="app-container">
    <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规格名称" prop="ruleName">
        <el-input v-model="queryParams.ruleName" placeholder="请输入规格名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['product:storeProductRule:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['product:storeProductRule:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['product:storeProductRule:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['product:storeProductRule:export']">导出</el-button>
      </el-col> -->
      <right-toolbar
        :show-search.sync="showSearch"
        :search="false"
        :columns="columns"
        @queryTable="getList"
        @columns-change="onColumnChange"
      />
    </el-row>

    <el-table v-loading="loading" :data="storeProductRuleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        v-for="col in filteredColumns"
        :key="col.prop"
        :label="col.label"
        header-align="center"
        :align="col.align || 'center'"
        :prop="col.prop"
        :width="col.width"
      >
        <template slot-scope="{row,column,$index}">
          <!-- <div v-html="col.render(row, column, $index)"  v-if="col.render"></div> -->
          <CellRender
            v-if="col.render"
            :row="row"
            :column="column"
            :col-config="col"
            :index="$index"
            :render="col.render"
          />
          <template v-else>
            {{ row[col.prop] }}
          </template>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['product:storeProductRule:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['product:storeProductRule:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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

    <!-- 添加或修改商品规格对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" inline size="mini">
        <el-form-item label="规格名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规格名称" />
        </el-form-item>
        <el-row v-for="(item, index) in form.ruleValue" :key="index" style="margin-top: 15px;padding:0 20px">
          <el-col :span="24">
            <div style="margin-bottom: 15px;">
              <el-tag closable type="info" size="medium" @close="onDelValue(index)">
                {{ item.value }}
              </el-tag>
            </div>
            <div>
              <el-tag
                v-for="(detail, i) in item.detail"
                :key="i"
                closable
                style="margin-right: 8px;margin-bottom: 8px;"
                size="medium"
                @close="onDelValueDetail(index, i)"
              >
                {{ detail }}
              </el-tag>
              <el-form-item label="">
                <el-input v-model="valueDetails[index]" placeholder="请输入属性名">
                  <el-button slot="append" @click="onAddValueDetail(index)">添加</el-button>
                </el-input>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px;">
          <el-col :span="24">
            <el-form-item label="规格">
              <el-input v-model="detailAdd.name" placeholder="请输入规格" />
            </el-form-item>
            <el-form-item label="规格值">
              <el-input v-model="detailAdd.value" placeholder="请输入规格值" />
            </el-form-item>
            <el-form-item label="">
              <el-button type="primary" @click="onAddValue">添加新规格</el-button>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStoreProductRule, getStoreProductRule, delStoreProductRule, addStoreProductRule, updateStoreProductRule } from '@/api/product/storeProductRule'
import tableColumnSelect from '@/mixin/tableColumnSelect'

export default {
  name: 'StoreProductRule',
  mixins: [tableColumnSelect],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商品规格表格数据
      storeProductRuleList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleName: undefined,
        ruleValue: undefined
      },
      columns: [
        // {
        //   label:'id',
        //   prop:'id',
        //   width:80
        // },
        {
          label: '规格名称',
          prop: 'ruleName',
          align: 'left',
          width: 300
        },
        {
          label: '规格值',
          align: 'left',
          prop: 'ruleValue',
          render(h, props) {
            const { ruleValue } = props.row
            return (
              <div>
                {
                  ruleValue.map(item => {
                    return (
                      <div>{item.value}：{item.detail.join('，')}</div>
                    )
                  })
                }
              </div>
            )
          }
        },
        {
          label: '创建时间',
          prop: 'createTime',
          width: 200
        }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleName: [
          { required: true, message: '规格名称不能为空', trigger: 'blur' }
        ]
      },
      valueDetails: [],
      detailAdd: {
        name: '',
        value: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询商品规格列表 */
    getList() {
      this.loading = true
      listStoreProductRule(this.queryParams).then(response => {
        this.storeProductRuleList = response.rows
        this.total = response.total
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
        ruleName: '',
        ruleValue: []
      }
      this.valueDetails = []
      this.detailAdd = {
        name: '',
        value: ''
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加商品规格'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true
      this.reset()
      const id = row.id || this.ids
      getStoreProductRule(id).then(response => {
        this.loading = false
        this.form = response.data
        this.open = true
        this.title = '修改商品规格'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.ruleValue.length === 0) {
            return this.$message.warning('请至少添加一条商品规格！')
          }
          this.buttonLoading = true
          if (this.form.id != null) {
            updateStoreProductRule(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addStoreProductRule(this.form).then(response => {
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
        return delStoreProductRule(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('product/storeProductRule/export', {
        ...this.queryParams
      }, `storeProductRule_${new Date().getTime()}.xlsx`)
    },
    onDelValue(index) {
      this.form.ruleValue.splice(index, 1)
    },
    onDelValueDetail(index, i) {
      this.form.ruleValue[index].detail.splice(i, 1)
    },
    onAddValueDetail(index) {
      const { valueDetails } = this
      const value = valueDetails[index]?.trim()
      if (!value) {
        this.$message.warning('请输入属性名')
        return
      }
      this.form.ruleValue[index].detail.push(value)
      valueDetails.splice(index, 1, '')
    },
    onAddValue() {
      const { detailAdd } = this
      const { name, value } = detailAdd
      if (name.trim() === '' || value.trim() === '') {
        this.$message.warning('请输入规格和规格值')
        return
      }
      this.form.ruleValue.push({ value: name, detail: [value] })
      this.detailAdd = { name: '', value: '' }
    }
  }
}
</script>
