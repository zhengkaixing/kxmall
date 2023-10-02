<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择前置仓" clearable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="categoryId">
        <el-cascader
          :options="categories"
          :props="{ checkStrictly: true ,value:'id'}"
          placeholder="请选择类目"
          clearable
          filterable
          @change="onSelectCategory"
        />
      </el-form-item>
      <el-form-item prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入商品条码/商品ID/商品名称"
        />
      </el-form-item>
      <el-form-item prop="type" style="width:100px">
        <el-select v-model="queryParams.type" clearable placeholder="请选择销售状态">
          <el-option
            v-for="dict in dict.type.warning_num"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="minNum" style="width:110px">
        <el-input
          v-model="queryParams.minNum"
          clearable
          placeholder="请输入最小值"
        />
      </el-form-item>
      <el-form-item style="color:#d2d3d7">
        —
      </el-form-item>
      <el-form-item prop="maxNum" style="width:110px">
        <el-input
          v-model="queryParams.maxNum"
          clearable
          placeholder="请输入最大值"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
      </el-form-item>
      <el-form-item prop="showType">
        <el-checkbox v-model="queryParams.showType" :true-label="1" :false-label="0" @change="handleQuery">只显示库存预警</el-checkbox>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:stock:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:stock:edit']"
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
          v-hasPermi="['storage:stock:remove']"
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
          v-hasPermi="['storage:stock:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row> -->

    <el-table v-loading="loading" :data="stockList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="序号" width="80" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="前置仓" align="left" header-align="center" prop="storageName" />
      <el-table-column label="商品类目" align="left" header-align="center" prop="categoryName" />
      <el-table-column label="商品条码" align="left" header-align="center" prop="barCode" />
      <el-table-column label="商品ID" align="left" header-align="center" prop="productId" />
      <el-table-column label="商品名称" align="left" header-align="center" prop="productName" />
      <!-- <el-table-column v-if="true" label="主键" align="center" prop="id" /> -->
      <el-table-column label="商品规格" align="left" header-align="center" prop="productAttrName" />
      <el-table-column label="现货量" align="center" header-align="center">
        <template slot-scope="{row}">
          {{ (Number(row.frezzStock)+Number(row.stock)).toFixed(0) }}
        </template>
      </el-table-column>
      <el-table-column label="可售量" align="center" header-align="center" prop="stock">
        <template slot-scope="{row}">
          <div :class="{'stock--warning': row.stock < row.warningNum}">{{ row.stock }}</div>
        </template>
      </el-table-column>
      <el-table-column label="锁定量" align="center" header-align="center" prop="frezzStock" />
      <el-table-column label="预警量" align="center" header-align="center" prop="warningNum">
        <template slot-scope="{row}">
          <el-button type="text" @click="updateWarningNum(row)">{{ row.warningNum }}</el-button>
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

    <el-dialog
      :visible.sync="open"
      title="预警量设置"
      width="25%"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        inline
      >
        <el-form-item prop="warningNum" label="预警值">
          <el-input v-model="form.warningNum" clearable />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改前置仓商品对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品id" prop="spuId">
          <el-input v-model="form.spuId" placeholder="请输入商品id" />
        </el-form-item>
        <el-form-item label="商品规格id" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入商品规格id" />
        </el-form-item>
        <el-form-item label="前置仓id" prop="storageId">
          <el-input v-model="form.storageId" placeholder="请输入前置仓id" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="form.stock" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="销售量" prop="sales">
          <el-input v-model="form.sales" placeholder="请输入销售量" />
        </el-form-item>
        <el-form-item label="冻结库存" prop="frezzStock">
          <el-input v-model="form.frezzStock" placeholder="请输入冻结库存" />
        </el-form-item>
        <el-form-item label="当前售价" prop="price">
          <el-input v-model="form.price" placeholder="请输入当前售价" />
        </el-form-item>
        <el-form-item label="预警数量" prop="warningNum">
          <el-input v-model="form.warningNum" placeholder="请输入预警数量" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { warningList, warningUpdate /** getStock delStock,*/ /** addStock, updateStockupdatePrice, updateStatus */ } from '@/api/storage/stock'
import { listAllStorage } from '@/api/storage/storage'
import { listStoreCategoryTree } from '@/api/product/storeCategory'
// import NP from 'number-precision'

export default {
  name: 'Stock',
  dicts: ['warning_num'],
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
      // // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 前置仓商品表格数据
      stockList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        storageId: undefined,
        categoryId: undefined,
        type: undefined,
        name: undefined,
        minNum: undefined,
        maxNum: undefined,
        showType: 0
      },
      // 表单参数
      form: {
        warningNum: 0
      },
      // 表单校验
      rules: {
        warningNum: { required: true, message: '请输入预警量', trigger: 'blur' }
      },
      storages: [],
      categories: []
      // editMode: {}
    }
  },
  created() {
    this.getList()
    this.listAllStorage()
    this.listStoreCategoryTree()
  },
  methods: {
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    listStoreCategoryTree() {
      listStoreCategoryTree().then(({ data }) => {
        this.categories = data.content
      })
    },
    /** 查询前置仓商品列表 */
    getList() {
      this.loading = true
      // this.editMode = {}
      warningList(this.queryParams).then(response => {
        this.stockList = response.rows
        this.total = response.total
        // response.rows.forEach(({ id }) => {
        //   this.$set(this.editMode, id, false)
        // })
      }).finally(() => {
        this.loading = false
      })
    },
    // 取消按钮
    // cancel() {
    //   this.open = false
    //   this.reset()
    // },
    // 表单重置
    // reset() {
    //   this.form = {
    //     id: undefined,
    //     spuId: undefined,
    //     productId: undefined,
    //     storageId: undefined,
    //     status: undefined,
    //     stock: undefined,
    //     sales: undefined,
    //     frezzStock: undefined,
    //     price: undefined,
    //     warningNum: undefined,
    //     createBy: undefined,
    //     updateBy: undefined,
    //     createTime: undefined,
    //     updateTime: undefined
    //   }
    //   this.resetForm('form')
    // },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm('queryForm')
    //   this.handleQuery()
    // },
    // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.ids = selection.map(item => item.id)
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    // },
    /** 新增按钮操作 */
    // handleAdd() {
    //   this.reset()
    //   this.open = true
    //   this.title = '添加前置仓商品'
    // },
    /** 修改按钮操作 */
    // handleUpdate(row) {
    //   this.loading = true
    //   this.reset()
    //   const id = row.id || this.ids
    //   getStock(id).then(response => {
    //     this.loading = false
    //     this.form = response.data
    //     this.open = true
    //     this.title = '修改前置仓商品'
    //   })
    // },
    /** 提交按钮 */
    // submitForm() {
    //   this.$refs['form'].validate(valid => {
    //     if (valid) {
    //       this.buttonLoading = true
    //       if (this.form.id != null) {
    //         updateStock(this.form).then(response => {
    //           this.$modal.msgSuccess('修改成功')
    //           this.open = false
    //           this.getList()
    //         }).finally(() => {
    //           this.buttonLoading = false
    //         })
    //       } else {
    //         addStock(this.form).then(response => {
    //           this.$modal.msgSuccess('新增成功')
    //           this.open = false
    //           this.getList()
    //         }).finally(() => {
    //           this.buttonLoading = false
    //         })
    //       }
    //     }
    //   })
    // },
    /** 删除按钮操作 */
    // handleDelete(row) {
    //   const ids = row.id || this.ids
    //   this.$modal.confirm('是否确认删除？').then(() => {
    //     this.loading = true
    //     return delStock(ids)
    //   }).then(() => {
    //     this.loading = false
    //     this.getList()
    //     this.$modal.msgSuccess('删除成功')
    //   }).catch(() => {
    //   }).finally(() => {
    //     this.loading = false
    //   })
    // },
    /** 导出按钮操作 */
    // handleExport() {
    //   this.download('storage/stock/export', {
    //     ...this.queryParams
    //   }, `stock_${new Date().getTime()}.xlsx`)
    // }
    onSelectCategory(value) {
      this.queryParams.categoryId = value[value.length - 1]
    },
    // getDiscount(row) {
    //   const { price, originalPrice } = row
    //   if (Number(originalPrice) === 0) {
    //     return '0.00'
    //   }
    //   return (NP.divide(NP.times(price, 10), originalPrice)).toFixed(2)
    // },
    // updatePrice(row) {
    //   const { id, price } = row
    //   updatePrice({ id, price }).then(() => {
    //     this.editMode[id] = false
    //   })
    // },
    // updateStatus(row, status) {
    //   this.$modal.confirm(status === 1 ? '是否将此商品上架' : '是否将此商品下架').then(() => {
    //     this.loading = true
    //     return updateStatus({ id: row.id, status })
    //   }).then(() => {
    //     this.loading = false
    //     this.getList()
    //   }).catch(() => {
    //   }).finally(() => {
    //     this.loading = false
    //   })
    // }
    warningUpdate(num) {
      const { storageId, productId, productAttrId } = this.$_currentRow
      return warningUpdate({ storageId, productId, productAttrId, num })
    },
    updateWarningNum(row) {
      this.$_currentRow = row
      this.form.warningNum = row.warningNum
      this.open = true
    },
    onSubmit() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.warningUpdate(this.form.warningNum).then(() => {
            this.open = false
            this.getList()
          })
        }
      })
    }
  }
}
</script>
<style>

.stock--warning {
  background: #f00;
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center
}
</style>
