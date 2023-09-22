<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择出库仓库" clearable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="outStockNumbers">
        <el-input
          v-model="queryParams.outStockNumbers"
          placeholder="请输入出库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item prop="states">
        <el-select v-model="queryParams.states" placeholder="请选择出库状态" clearable>
          <el-option
            v-for="dict in dict.type.out_stock_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="出库人" prop="ingoingPerson">
        <el-input
          v-model="queryParams.ingoingPerson"
          placeholder="请输入出库人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item prop="outgoingTime">
        <el-date-picker
          v-model="queryParams.outgoingTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择出库时间"
        />
      </el-form-item>
      <!-- <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="queryParams.remarks"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="outgoingDay">
        <el-input
          v-model="queryParams.outgoingDay"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          v-hasPermi="['storage:goodsOutStock:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >创建</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:goodsInStock:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:goodsInStock:edit']"
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
          v-hasPermi="['storage:goodsInStock:remove']"
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
          v-hasPermi="['storage:goodsInStock:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row> -->

    <el-table v-loading="loading" :data="goodsOutStockList">
      <el-table-column label="序号" width="80" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="left" header-align="center" prop="storageName" />
      <el-table-column label="出库单号" align="center" prop="outStockNumbers" />
      <el-table-column label="出库状态" align="center" prop="states">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.out_stock_status" :value="scope.row.states" />
        </template>
      </el-table-column>
      <el-table-column label="出库人" align="center" prop="outgoingPerson" />
      <el-table-column label="出库时间" align="center" prop="outgoingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.outgoingTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding" width="300">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['storage:goodsOutStock:edit']"
            size="mini"
            type="primary"
            @click="handleView(scope.row)"
          >详情</el-button>
          <template v-if="scope.row.states===0">
            <el-button
              v-hasPermi="['storage:goodsOutStock:edit']"
              size="mini"
              type="primary"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              v-hasPermi="['storage:goodsOutStock:edit']"
              size="mini"
              type="primary"
              @click="handleStockIn(scope.row)"
            >出库</el-button>
            <el-button
              v-hasPermi="['storage:goodsOutStock:remove']"
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
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

    <!-- 添加或修改商品出库对话框 -->

    <stock-list :id="id" :title="title" :visible.sync="open" type="out" :mode="mode" @ok="onOk" />
  </div>
</template>

<script>
import { listGoodsOutStock, /** getGoodsOutStock,*/ delGoodsOutStock, /** addGoodsOutStock, updateGoodsOutStock */updateOutOfStock } from '@/api/storage/goodsOutStock'
import { listAllStorage } from '@/api/storage/storage'
import StockList from '../components/StockList.vue'

export default {
  name: 'GoodsOutStock',
  dicts: ['out_stock_status'],
  components: {
    StockList
  },
  data() {
    return {
      // 按钮loading
      // buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      // ids: [],
      // // 非单个禁用
      // single: true,
      // // 非多个禁用
      // multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商品出库表格数据
      goodsOutStockList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        storageId: undefined,
        outStockNumbers: undefined,
        states: undefined,
        outgoingTime: undefined
      },
      storages: [],
      mode: null,
      id: ''
      // // 表单参数
      // form: {},
      // // 表单校验
      // rules: {
      //   id: [
      //     { required: true, message: '出库id不能为空', trigger: 'blur' }
      //   ],
      //   storageId: [
      //     { required: true, message: '仓库id不能为空', trigger: 'blur' }
      //   ],
      //   outStockNumbers: [
      //     { required: true, message: '出库单号不能为空', trigger: 'blur' }
      //   ],
      //   states: [
      //     { required: true, message: '0:待出库;1:已出库；不能为空', trigger: 'blur' }
      //   ],
      //   outgoingPerson: [
      //     { required: true, message: '出库人不能为空', trigger: 'blur' }
      //   ],
      //   outgoingTime: [
      //     { required: true, message: '出库时间不能为空', trigger: 'blur' }
      //   ],
      //   remarks: [
      //     { required: true, message: '备注不能为空', trigger: 'blur' }
      //   ],
      //   outgoingDay: [
      //     { required: true, message: '不能为空', trigger: 'blur' }
      //   ]
      // }
    }
  },
  created() {
    this.getList()
    this.listAllStorage()
  },
  methods: {
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    /** 查询商品出库列表 */
    getList() {
      this.loading = true
      listGoodsOutStock(this.queryParams).then(response => {
        this.goodsOutStockList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // // 取消按钮
    // cancel() {
    //   this.open = false
    //   this.reset()
    // },
    // 表单重置
    reset() {
      // this.form = {
      //   id: undefined,
      //   storageId: undefined,
      //   outStockNumbers: undefined,
      //   states: undefined,
      //   outgoingPerson: undefined,
      //   outgoingTime: undefined,
      //   remarks: undefined,
      //   outgoingDay: undefined,
      //   createBy: undefined,
      //   updateBy: undefined,
      //   createTime: undefined,
      //   updateTime: undefined
      // }
      // this.resetForm('form')
      this.id = ''
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
    // // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.ids = selection.map(item => item.id)
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    // },
    /** 新增按钮操作 */
    handleAdd() {
      this.mode = 'add'
      this.reset()
      this.open = true
      this.title = '创建出库单'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.mode = 'update'
      // this.loading = true
      this.reset()
      // const id = row.id || this.ids
      this.id = row.id
      this.open = true
      this.title = '编辑出库单'
      // getGoodsOutStock(id).then(response => {
      //   this.loading = false
      //   this.form = response.data
      //   this.open = true
      //   this.title = '修改商品出库'
      // })
    },
    // /** 提交按钮 */
    // submitForm() {
    //   this.$refs['form'].validate(valid => {
    //     if (valid) {
    //       this.buttonLoading = true
    //       if (this.form.id != null) {
    //         updateGoodsOutStock(this.form).then(response => {
    //           this.$modal.msgSuccess('修改成功')
    //           this.open = false
    //           this.getList()
    //         }).finally(() => {
    //           this.buttonLoading = false
    //         })
    //       } else {
    //         addGoodsOutStock(this.form).then(response => {
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
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true
        return delGoodsOutStock(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('storage/goodsOutStock/export', {
    //     ...this.queryParams
    //   }, `goodsOutStock_${new Date().getTime()}.xlsx`)
    // }
    onOk() {
      if (this.mode !== 'view') {
        this.getList()
      }
    },
    handleStockIn(row) {
      this.$modal.confirm('确定出库吗？').then(() => {
        this.loading = true
        return updateOutOfStock({ ...row })
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('出库成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    handleView(row) {
      this.mode = 'view'
      this.reset()
      this.id = row.id
      this.open = true
      this.title = '出库单详情'
    }
  }
}
</script>
