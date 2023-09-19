<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="cateName">
        <el-input v-model="queryParams.cateName" placeholder="输入分类名称搜索" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['product:storeCategory:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['product:storeCategory:edit']"
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
          v-hasPermi="['product:storeCategory:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleExport"
                v-hasPermi="['product:storeCategory:export']"
              >导出</el-button>
            </el-col> -->
      <right-toolbar
        :show-search.sync="showSearch"
        :columns="columns"
        @queryTable="getList"
        @columns-change="onColumnChange"
      />
    </el-row>

    <el-table v-loading="loading" :data="storeCategoryList" row-key="id" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        v-for="col in filteredColumns"
        :key="col.prop"
        :label="col.label"
        header-align="center"
        :align="col.align || 'center'"
        :prop="col.prop"
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
            v-hasPermi="['product:storeCategory:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['product:storeCategory:remove']"
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

    <!-- 添加或修改商品分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="cateName">
          <el-input v-model="form.cateName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类图片" prop="pic">
          <imageUpload v-model="form.pic" :limit="1" value-type="json" />
        </el-form-item>
        <el-form-item label="状态" prop="isShow">
          <el-radio-group v-model="form.isShow">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="上级分类" prop="pid">
          <TreeSelect v-model="form.pid" :options="options" placeholder="选择上级分类" no-options-text="没有数据" />
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
import { getStoreCategory, delStoreCategory, addStoreCategory, updateStoreCategory, listStoreCategoryTree } from '@/api/product/storeCategory'
import tableColumnSelect from '@/mixin/tableColumnSelect'

export default {
  name: 'StoreCategory',
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
      // 商品分类表格数据
      storeCategoryList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cateName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cateName: [
          { required: true, message: '分类名称不能为空', trigger: 'blur' }
        ]
      },
      columns: [
        {
          label: '名称',
          prop: 'cateName',
          align: 'left'
        },
        {
          label: '状态',
          prop: 'isShow',
          render(h, props) {
            const { row } = props
            let text = '显示'
            let type = null
            if (row.isShow === 0) {
              text = '隐藏'
              type = 'info'
            }
            return (
              <el-tag type={type}>{text}</el-tag>
            )
          }
        },
        {
          label: '排序',
          prop: 'sort'
        }
      ],
      options: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询商品分类列表 */
    getList() {
      this.loading = true
      listStoreCategoryTree(this.queryParams).then(({ data }) => {
        const { content, totalElements } = data
        this.storeCategoryList = content
        this.total = totalElements
        this.loading = false
      })
    },
    listStoreCategoryTree() {
      return new Promise((resolve, reject) => {
        listStoreCategoryTree().then(({ data }) => {
          const { content } = data
          this.$set(this.options[0], 'children', content || [])
          resolve()
        }, err => reject(err))
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
        pic: undefined,
        pid: 0,
        cateName: undefined,
        sort: 1,
        isShow: 1
      }
      this.options = [].concat([
        { id: 0, label: '顶级类目' }
      ])
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
    async handleAdd() {
      this.reset()
      await this.listStoreCategoryTree()
      this.open = true
      this.title = '添加商品分类'
    },
    /** 修改按钮操作 */
    async handleUpdate(row) {
      this.loading = true
      this.reset()
      const id = row.id || this.ids
      await this.listStoreCategoryTree()
      getStoreCategory(id).then(response => {
        this.loading = false
        this.form = response.data
        this.open = true
        this.title = '修改商品分类'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.id != null) {
            updateStoreCategory(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addStoreCategory(this.form).then(response => {
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
        return delStoreCategory(ids)
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
      this.download('product/storeCategory/export', {
        ...this.queryParams
      }, `storeCategory_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
