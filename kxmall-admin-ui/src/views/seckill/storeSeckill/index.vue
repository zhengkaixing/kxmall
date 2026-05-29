<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择前置仓" clearable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="活动标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入活动标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['seckill:storeSeckill:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="storeSeckillList" @selection-change="handleSelectionChange">
      <el-table-column v-if="true" label="id" align="center" prop="id" />
      <el-table-column label="商品id" align="center" prop="productId" />
      <el-table-column label="推荐图" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="JSON.parse(scope.row.image)[0].url" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="center">
        <template slot-scope="{row}">
          {{ storages.filter(el=>el.id===row.storageId)[0].name }}
        </template>
      </el-table-column>
      <el-table-column label="活动标题" align="center" prop="title" />
      <el-table-column label="秒杀价格" align="center" prop="price" />
      <el-table-column label="原价" align="center" prop="otPrice" />
      <el-table-column label="库存" align="center" prop="stock" />
      <el-table-column label="销量" align="center" prop="sales" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="stopTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stopTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品状态" align="center">
        <template slot-scope="{row}">
          {{ row.status===1?'启用':'不启用' }}
        </template>
      </el-table-column>
      <el-table-column label="限购" align="center" prop="num" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['seckill:storeSeckill:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['seckill:storeSeckill:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
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

    <!-- 添加或修改商品秒杀对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="仓库" prop="storageId">
          <el-select v-model="form.storageId" placeholder="请选择仓库" clearable :disabled="isUpdateMode" @change="showEditStorageChange">
            <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="秒杀商品" prop="productId">
          <el-cascader
            v-model="goodsOption"
            :options="goodsOptions"
            placeholder="秒杀商品"
            filterable
            clearable
            @change="chooseGoods"
          />
        </el-form-item>
        <el-form-item label="秒杀标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题" />
        </el-form-item>
        <el-form-item label="秒杀简介" prop="info">
          <el-input v-model="form.info" placeholder="请输入简介" />
        </el-form-item>

        <el-form-item label="单位名" prop="unitName">
          <el-input v-model="form.unitName" placeholder="请输入单位名" />
        </el-form-item>

        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            clearable
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="stopTime">
          <el-date-picker
            v-model="form.stopTime"
            clearable
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间"
          />
        </el-form-item>

        <el-form-item label="秒杀时间范围" prop="seckillStartTime">
          <el-time-select
            :key="form.seckillStartTime"
            v-model="form.seckillStartTime"
            :picker-options="{
              start: '00:00',
              step: '1:00',
              end: '23:00',
            }"
            placeholder="起始时间"
          />
          <el-time-select
            :key="form.seckillStopTime"
            v-model="form.seckillStopTime"
            :picker-options="{
              start: '00:00',
              step: '1:00',
              end: '23:00',
              minTime: form.seckillStartTime
            }"
            placeholder="结束时间"
          />
        </el-form-item>

        <el-form-item label="商品封面图" prop="image">
          <imageUpload v-model="form.image" :limit="1" value-type="json" />
        </el-form-item>

        <el-form-item label="商品轮播图" prop="sliderImage">
          <imageUpload v-model="form.sliderImage" :limit="4" value-type="json" />
        </el-form-item>

        <el-form-item label="活动状态" props="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">不开启</el-radio>
            <el-radio :label="1">开启</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="限购" prop="num">
          <el-input-number v-model="form.num" placeholder="限购" :min="1" />
        </el-form-item>

        <el-form-item v-if="form.specType === 0">
          <el-table :data="attr" size="small">
            <el-table-column label="图片" align="center">
              <template slot-scope="{row}">
                <imageUpload
                  v-model="row.pic"
                  :limit="1"
                  value-type="json"
                  :is-show-tip="false"
                  class="table-image-upload"
                />
              </template>
            </el-table-column>
            <el-table-column label="售价" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.price" type="text" />
              </template>
            </el-table-column>
            <el-table-column label="成本价" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.cost" type="text" />
              </template>
            </el-table-column>
            <el-table-column label="原价" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.otPrice" type="text" />
              </template>
            </el-table-column>
            <el-table-column label="库存" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.stock" type="text" maxlength="7" />
              </template>
            </el-table-column>
            <el-table-column label="销量" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.sales" type="text" maxlength="7" />
              </template>
            </el-table-column>

          </el-table>
        </el-form-item>

        <el-row>
          <el-col :span="24">
            <el-form-item label="商品详情" prop="description">
              <editor v-model="form.description" :min-height="192" />
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
import { listStoreSeckill, getStoreSeckill, delStoreSeckill, addStoreSeckill, updateStoreSeckill } from '@/api/seckill/storeSeckill'
import { listAllStorage } from '@/api/storage/storage'
import { detailGoodsByStorageId, productTreeByStorageId } from '@/api/product/storeProduct'

export default {
  name: 'StoreSeckill',
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
      // 商品秒杀表格数据
      storeSeckillList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      goodsOptions: [],
      goodsOption: undefined,
      // 查询参数
      queryParams: {
        storageId: undefined,
        pageNum: 1,
        pageSize: 10,
        productId: undefined,
        image: undefined,
        images: undefined,
        title: undefined,
        info: undefined,
        price: undefined,
        cost: undefined,
        otPrice: undefined,
        giveIntegral: undefined,
        sort: undefined,
        stock: undefined,
        sales: undefined,
        unitName: undefined,
        postage: undefined,
        description: undefined,
        startTime: undefined,
        stopTime: undefined,
        status: undefined,
        isPostage: undefined,
        isHot: undefined,
        num: undefined,
        isShow: undefined,
        timeId: undefined,
        specType: undefined,
        tempId: undefined,
        isDel: undefined
      },
      // 表单参数
      form: {},
      attr: [],
      storages: [],
      isUpdateMode: false,
      // 表单校验
      rules: {
        title: [
          { required: true, message: '活动标题不能为空', trigger: 'blur' }
        ],
        storageId: [
          { required: true, message: '请选择仓库', trigger: 'blur' }
        ],
        productId: [
          { required: true, message: '请选择商品', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'blur' }
        ],
        stopTime: [
          { required: true, message: '请选择结束时间', trigger: 'blur' }
        ],
        seckillStartTime: [
          { required: true, message: '请选择秒杀时间范围', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入价格', trigger: 'blur' }
        ],
        stock: [
          { required: true, message: '请输入库存', trigger: 'blur' }
        ],
        num: [
          { required: true, message: '请输入限购数量', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.listAllStorage()
    this.getList()
  },
  methods: {
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    showEditStorageChange(val) {
      // 仓库切换时 清空列表
      this.goodsOption = null
      this.refreshGoodsOptions(val)
      this.resetOther()
    },
    showEditStorageClick() {
      if (this.goodsOption && this.goodsOption.length > 0) {
        this.$message({
          message: '改变仓库将会清空已选商品',
          type: 'warning'
        })
      }
    },
    refreshGoodsOptions(val) {
      productTreeByStorageId(val).then(response => {
        this.goodsOptions = response.data
      })
    },
    /** 查询商品秒杀列表 */
    getList() {
      this.loading = true
      listStoreSeckill(this.queryParams).then(response => {
        this.storeSeckillList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    async chooseGoods(e) {
      const that = this
      // 1. 验证是否是商品
      if (e !== undefined) {
        const tag = e[e.length - 1]
        if (this.goodsOption === undefined || this.goodsOption === null || this.goodsOption.length <= 2) {
          this.$notify.error({
            title: '提示',
            message: '请选择商品'
          })
          return false
        }
        // 2. 向后台根据商品ID获取商品信息
        this.form.productId = tag.substring(2)
        that.form.skuList = []
        await detailGoodsByStorageId(this.form.productId, this.form.storageId).then(response => {
          const kxStockVo = response.data.kxStockVo
          this.form.title = response.data.storeName
          this.form.info = response.data.storeInfo
          this.form.unitName = response.data.unitName

          this.form.image = response.data.image
          this.form.images = response.data.sliderImage

          this.form.description = response.data.description
          this.attr[0].pic = response.data.image
          this.attr[0].price = kxStockVo.price
          this.attr[0].cost = response.data.cost
          this.attr[0].otPrice = response.data.otPrice
          this.attr[0].stock = kxStockVo.stock
          this.attr[0].sales = kxStockVo.sales
        }).catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.msg + ',请重新选择'
          })
        })
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        productId: undefined,
        image: undefined,
        images: undefined,
        title: undefined,
        info: undefined,
        price: undefined,
        cost: undefined,
        otPrice: undefined,
        giveIntegral: undefined,
        sort: undefined,
        stock: undefined,
        sales: undefined,
        unitName: undefined,
        postage: undefined,
        description: undefined,
        startTime: undefined,
        stopTime: undefined,
        status: 0,
        isPostage: undefined,
        isHot: undefined,
        num: 1,
        isShow: undefined,
        timeId: undefined,
        specType: 0,
        tempId: undefined,
        seckillStartTime: undefined,
        seckillStopTime: undefined
      }
      this.attr = [
        {
          imageArr: [],
          pic: '',
          price: 1,
          cost: 0,
          otPrice: 0,
          stock: 1,
          seckillStock: 0,
          seckillSrice: 0,
          pinkStock: 0,
          pinkPrice: 0,
          barCode: '',
          weight: 0,
          volume: 0,
          brokerage: 0,
          brokerageTwo: 0,
          integral: 0
        }
      ]
      this.resetForm('form')
    },
    // 表单重置
    resetOther() {
      this.form.productId = undefined
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
      this.isUpdateMode = false
      this.title = '添加商品秒杀'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.isUpdateMode = true
      this.loading = true
      this.reset()
      const id = row.id || this.ids
      getStoreSeckill(id).then(response => {
        this.loading = false
        this.form = response.data
        this.attr[0].pic = response.data.image
        this.attr[0].price = response.data.price
        this.attr[0].cost = response.data.cost
        this.attr[0].otPrice = response.data.otPrice
        this.attr[0].stock = response.data.stock
        this.attr[0].sales = response.data.sales
        this.open = true
        this.title = '修改商品秒杀'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true

          this.form.price = this.attr[0].price
          this.form.cost = this.attr[0].cost
          this.form.otPrice = this.attr[0].otPrice
          this.form.stock = this.attr[0].stock
          this.form.sales = this.attr[0].sales

          if (this.form.id != null) {
            updateStoreSeckill(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addStoreSeckill(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除商品秒杀编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true
        return delStoreSeckill(ids)
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
      this.download('seckill/storeSeckill/export', {
        ...this.queryParams
      }, `storeSeckill_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
