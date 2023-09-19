<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="recommendType">
        <el-select v-model="queryParams.recommendType" clearable placeholder="请选择推荐类型">
          <el-option
            v-for="dict in dict.type.recommend_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
        <el-button
          v-hasPermi="['recommend:recommend:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['recommend:recommend:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['recommend:recommend:edit']"
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
          v-hasPermi="['recommend:recommend:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['recommend:recommend:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row> -->

    <el-table v-loading="loading" :data="recommendList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="序号" width="80" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column v-if="true" label="推荐ID" align="center" prop="id" />
      <el-table-column label="推荐商品" align="center" prop="productName" />
      <el-table-column label="推荐类型" align="center" prop="recommendType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.recommend_type" :value="scope.row.recommendType" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding" width="120">
        <template slot-scope="scope">
          <!-- <el-button
            v-hasPermi="['recommend:recommend:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button> -->
          <el-button
            v-hasPermi="['recommend:recommend:remove']"
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改推荐管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="recommendType" label="推荐类型">
          <el-select v-model="form.recommendType" clearable placeholder="请选择推荐类型">
            <el-option
              v-for="dict in dict.type.recommend_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="productIds" label="推荐商品">
          <el-cascader
            v-model="form.productIds"
            :options="productsTree"
            :props="props"
            collapse-tags
            clearable
            filterable
            placeholder="请选择推荐商品"
          />
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
import { listRecommend, /** getRecommend,*/ delRecommend, addRecommendBatch /** updateRecommend*/ } from '@/api/recommend/recommend'
import { getProductBigTree } from '@/api/product/storeProduct.js'

export default {
  name: 'Recommend',
  dicts: ['recommend_type'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // // 选中数组
      // ids: [],
      // // 非单个禁用
      // single: true,
      // // 非多个禁用
      // multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 推荐管理表格数据
      recommendList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        recommendType: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // id: [
        //   { required: true, message: '不能为空', trigger: 'blur' }
        // ],
        // spuId: [
        //   { required: true, message: '商品id不能为空', trigger: 'blur' }
        // ],
        recommendType: [
          { required: true, message: '推荐类型不能为空', trigger: 'blur' }
        ],
        productIds: [
          { required: true, message: '推荐商品不能为空', trigger: 'blur' }
        ]
      },
      props: {
        multiple: true,
        value: 'id'
      },
      productsTree: []
    }
  },
  created() {
    this.getList()
    this.getProductBigTree()
  },
  methods: {
    getProductBigTree() {
      getProductBigTree().then(({ data }) => {
        this.productsTree = data
      })
    },
    /** 查询推荐管理列表 */
    getList() {
      this.loading = true
      listRecommend(this.queryParams).then(response => {
        this.recommendList = response.rows
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
        id: undefined,
        recommendType: undefined,
        productIds: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm('queryForm')
    //   this.handleQuery()
    // },
    // // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.ids = selection.map(item => item.id)
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    // },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '创建'
    },
    /** 修改按钮操作 */
    // handleUpdate(row) {
    //   this.loading = true
    //   this.reset()
    //   const id = row.id || this.ids
    //   getRecommend(id).then(response => {
    //     this.loading = false
    //     this.form = response.data
    //     this.open = true
    //     this.title = '修改推荐管理'
    //   })
    // },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.id != null) {
            // updateRecommend(this.form).then(response => {
            //   this.$modal.msgSuccess('修改成功')
            //   this.open = false
            //   this.getList()
            // }).finally(() => {
            //   this.buttonLoading = false
            // })
          } else {
            addRecommendBatch({ ...this.form, productIds: this.getProductIds(this.form.productIds) }).then(response => {
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
    getProductIds(ids) {
      const arr = []
      for (let i = 0; i < ids.length; i++) {
        const els = ids[i]
        arr.push(els[els.length - 1])
      }
      console.log(arr)
      return arr
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true
        return delRecommend(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    }
    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('recommend/recommend/export', {
    //     ...this.queryParams
    //   }, `recommend_${new Date().getTime()}.xlsx`)
    // }
  }
}
</script>
