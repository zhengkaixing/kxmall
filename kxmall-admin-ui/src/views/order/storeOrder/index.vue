<template>
  <div class="app-container order-page">
    <div class="page-panel">
      <el-tabs v-model="queryParams.orderStatus" class="page-tabs" @tab-change="onOrderStatusChange">
        <el-tab-pane v-for="item in status" :key="item.name" :label="item.label" :name="item.name" />
      </el-tabs>
      <el-form ref="queryForm" class="query-form" :model="queryParams" :inline="true" label-width="80px">
      <el-form-item label="前置仓" prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择前置仓" clearable filterable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单号" prop="orderId">
        <el-input v-model="queryParams.orderId" clearable placeholder="请输入订单号" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          type="datetimerange"
          value-format="YYYY-MM-DD HH:mm:ss"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          range-separator="至"
          unlink-panels
        />
      </el-form-item>
      <el-form-item class="query-form__actions" label-width="0">
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
      </el-form>

      <el-table
      v-loading="loading"
      :data="storeOrderList"
      stripe
      class="list-table"
      header-cell-class-name="list-table-header"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column type="index" label="序号" align="center">
        <template #default="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        width="200"
        prop="orderId"
        align="center"
        label="订单号"
      />
      <el-table-column
        v-if="queryParams.orderStatus==='32'"
        prop="exceptionReason"
        align="center"
        label="异常原因"
      />
      <el-table-column
        v-if="queryParams.orderStatus==='80'"
        prop="exceptionReason"
        align="center"
        label="付款状态"
      />
      <el-table-column
        v-if="queryParams.orderStatus!=='14'&&queryParams.orderStatus!=='16'&&queryParams.orderStatus!=='10'"
        align="center"
        label="配送员"
      >
        <template #default="{row}">
          <span>{{ row.riderName? row.riderName : '商家自配' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="left"
        width="100"
        prop="realName"
        label="姓名"
      />
      <el-table-column
        header-align="center"
        align="left"
        width="110"
        prop="userPhone"
        label="电话"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        header-align="center"
        align="center"
        width="90"
        prop="address"
        label="订单类型"
      >
        <template #default="{row}">
          <el-tag :type="row.shippingType==0 ? 'error' : 'success'">{{ row.shippingType==1?'配送':'自提' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        header-align="center"
        align="left"
        min-width="200"
        prop="userAddress"
        label="地址"
      />
      <el-table-column
        align="center"
        label="配送费"
      >
        <template #default="scope">
          <span>{{ scope.row.freightPrice == 0?'免配送':scope.row.freightPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="订单金额"
      >
        <template #default="scope">
          <span>{{ scope.row.originalTotalPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="实付金额"
      >
        <template #default="scope">
          <span>{{ scope.row.payPrice?scope.row.payPrice:'' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="200"
        align="center"
        label="要求送达时间"
      >
        <template #default="scope">
          <span>{{ scope.row.predictTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="phone"
        width="160"
        align="center"
        label="下单时间"
      >
        <template #default="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        width="160"
        label="支付时间"
        prop="payTime"
      >
        <template #default="scope">
          <span>{{ scope.row.payTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="280"
        align="center"
        label="付款交易号"
      >
        <template #default="scope">
          <span>{{ scope.row.payId }}</span>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        width="160"
        label="创建时间"
      >
        <template #default="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        width="160"
        label="修改时间"
      >
        <template #default="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        align="center"
        label="操作"
        width="300"
      >
        <template #default="{row}">
          <el-button
            type="primary"
            size="small"
            @click="viewDetail(row.id)"
          >详情</el-button>
          <el-button
            v-if="row.status===14"
            type="primary"
            size="small"
            @click="handleOperation(row.id,'startStocking')"
          >开始配货</el-button>
          <el-button
            v-if="row.status===16"
            type="primary"
            size="small"
            @click="handleOperation(row.id,'completeAllocation')"
          >完成配货</el-button>
          <!--<el-button
            v-if="(row.status===20||row.status===32||row.status===30) && row.orderType===0"
            type="primary"
            size="small"
            @click="allot(row.storeId,row.orderNo,row.postId)"
          >{{ row.postId ? '重新配送' : '配送' }}</el-button>-->
          <el-button
            v-if="row.status===20 && row.shippingType===1"
            type="primary"
            size="small"
            @click="handleOperation(row.id,'merchantDistribution')"
          >商家自配</el-button>
          <el-button
            v-if="row.status===30"
            type="primary"
            size="small"
            @click="handleOperation(row.id,'completeDelivery')"
          >完成配送</el-button>
        </template>
      </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 订单详情 -->
    <el-dialog title="订单详情" v-model="open" width="920px" append-to-body class="order-detail-dialog">
      <div class="order-detail">
        <section class="detail-section">
          <div class="detail-section__title">订单信息</div>
          <div class="detail-grid">
            <div class="detail-item">
              <span class="detail-item__label">订单编号</span>
              <span class="detail-item__value">{{ form.orderId }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">订单状态</span>
              <span class="detail-item__value">
                <el-tag :type="getStatusType(form.status)" size="small">{{ getStatus(form.status) }}</el-tag>
              </span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">下单时间</span>
              <span class="detail-item__value">{{ form.createTime }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">要求送达</span>
              <span class="detail-item__value">{{ form.predictTime }}</span>
            </div>
            <div class="detail-item detail-item--block">
              <span class="detail-item__label">备注</span>
              <span class="detail-item__value">{{ form.remark || '—' }}</span>
            </div>
          </div>
        </section>

        <section class="detail-section">
          <div class="detail-section__title">客户信息</div>
          <div class="detail-grid">
            <div class="detail-item">
              <span class="detail-item__label">客户姓名</span>
              <span class="detail-item__value">{{ form.realName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">客户电话</span>
              <span class="detail-item__value">{{ form.userPhone }}</span>
            </div>
            <div class="detail-item detail-item--block">
              <span class="detail-item__label">客户地址</span>
              <span class="detail-item__value">{{ form.userAddress }}</span>
            </div>
          </div>
        </section>

        <section class="detail-section">
          <div class="detail-section__title">支付信息</div>
          <div class="detail-grid">
            <div class="detail-item">
              <span class="detail-item__label">支付状态</span>
              <span class="detail-item__value">{{ getStatus(form.status) }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">支付方式</span>
              <span class="detail-item__value">{{ form.payTypeName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">支付金额</span>
              <span class="detail-item__value detail-item__value--money">¥{{ form.payPrice }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-item__label">支付时间</span>
              <span class="detail-item__value">{{ form.payTime }}</span>
            </div>
            <div class="detail-item detail-item--block">
              <span class="detail-item__label">交易号</span>
              <span class="detail-item__value">{{ form.payId || '—' }}</span>
            </div>
          </div>
        </section>

        <section class="detail-section">
          <div class="detail-section__title">商品信息</div>
          <el-table
            :data="form.productList"
            stripe
            class="list-table"
            header-cell-class-name="list-table-header"
          >
            <el-table-column align="center" label="序号" type="index" width="56" />
            <el-table-column align="center" label="条码" prop="barCode" min-width="110" />
            <el-table-column align="center" label="商品编码" prop="productId" min-width="100" :show-overflow-tooltip="true" />
            <el-table-column header-align="center" align="left" label="商品名称" prop="productTitle" min-width="140" :show-overflow-tooltip="true" />
            <el-table-column align="center" label="数量" prop="num" width="70" />
            <el-table-column align="center" label="单位" prop="unitName" width="70" />
            <el-table-column header-align="center" align="right" label="原价" width="90">
              <template #default="{row}">
                <span>{{ row.otPrice }}</span>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="right" label="售价" width="90">
              <template #default="{row}">
                <span>{{ row.price }}</span>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="right" label="小计" width="110">
              <template #default="{row}">
                <span>{{ Number(row.price * row.num).toFixed(2) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </section>

        <div class="detail-summary">
          <div class="summary-row">
            <span>运费</span>
            <span>{{ form.freightPrice == 0 ? '免配送' : '¥' + form.freightPrice }}</span>
          </div>
          <div class="summary-row">
            <span>优惠</span>
            <span>¥{{ (Number(form.originalTotalPrice || 0) - Number(form.payPrice || 0) + Number(form.freightPrice || 0)).toFixed(2) }}</span>
          </div>
          <div class="summary-row">
            <span>商品金额</span>
            <span>¥{{ form.originalTotalPrice }}</span>
          </div>
          <div class="summary-row summary-row--total">
            <span>总金额</span>
            <span>¥{{ form.payPrice }}</span>
          </div>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { multiply } from '@/utils/math'
import { listStoreOrder /** delStoreOrder, addStoreOrder, updateStoreOrder*/, getStoreOrder, startStocking, completeAllocation, merchantDistribution, completeDelivery } from '@/api/order/storeOrder'
import { listAllStorage } from '@/api/storage/storage'

export default {
  name: 'StoreOrder',
  components: {
    // PrintDialog
  },
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: false,
      // 选中数组
      // ids: [],
      // // 非单个禁用
      // single: true,
      // // 非多个禁用
      // multiple: true,
      // 总条数
      total: 0,
      // 订单表格数据
      storeOrderList: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderStatus: '14',
        storageId: '',
        orderId: '',
        // type: '',
        // orderType: '0',
        createTime: []
      },
      // 表单参数
      form: {},
      // 表单校验
      status: [
        { label: '待配货', name: '14' },
        { label: '配货中', name: '16' },
        { label: '待配送', name: '20' },
        { label: '配送中', name: '30' },
        { label: '已完成', name: '40,50' },
        { label: '配送异常', name: '32' },
        { label: '超时订单', name: '34' },
        { label: '待支付', name: '10' },
        { label: '已取消', name: '80' },
        { label: '全部订单', name: 'all' }
      ],
      storages: []
    }
  },
  computed: {
    // title() {
    //   const { modes, currentMode } = this.dialog
    //   return modes[currentMode]?.title || ''
    // }
  },
  created() {
    this.getList()
    this.listAllStorage()
  },
  methods: {
    multiply,
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    /** 查询订单列表 */
    getList() {
      this.loading = true
      this.storeOrderList = []
      const params = { ...this.queryParams }
      if(params.createTime){
        params.startTime = params.createTime[0]
        params.endTime = params.createTime[1]
      }
      delete params.createTime
      listStoreOrder(params).then(response => {
        this.storeOrderList = response.rows
        this.total = response.total
      }).finally(() => {
        this.loading = false
      })
    },
    // // 取消按钮
    // cancel() {
    //   this.open = false
    //   this.reset()
    // },
    // // 表单重置
    // reset(row) {
    //   this.form = JSON.parse(JSON.stringify(row)) || {}
    //   this.resetForm('form')
    // },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams.storageId = ''
      this.queryParams.orderId = ''
      this.queryParams.createTime = []
      this.handleQuery()
    },
    onOrderStatusChange() {
      this.handleQuery()
    },
    viewDetail(id) {
      getStoreOrder(id).then(({ data }) => {
        this.form = data
        this.open = true
      })
    },
    getStatus(s) {
      const status = {
        14: '待配货',
        16: '配货中',
        20: '待配送',
        30: '配送中',
        50: '已完成',
        40: '已完成',
        32: '配送异常',
        34: '超时订单',
        10: '待支付',
        80: '已取消',
        90: '已取消(系统)'
      }
      return status[s]
    },
    getStatusType(s) {
      const types = {
        14: 'warning',
        16: 'warning',
        20: '',
        30: '',
        40: 'success',
        50: 'success',
        32: 'danger',
        34: 'danger',
        10: 'info',
        80: 'info',
        90: 'info'
      }
      return types[s] || 'info'
    },
    handleOperation(id, operation) {
      const operations = { startStocking, completeAllocation, merchantDistribution, completeDelivery }
      operations[operation]({ id }).then(() => {
        this.$message({
          message: '操作成功',
          type: 'success'
        })
        this.handleQuery()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.order-page {
  min-height: calc(100vh - 84px);
  background: #f5f7fa;
  padding: 16px 20px 24px;
}

.order-overview {
  float: right;
  text-align: right;
}

.order-detail {
  max-height: 70vh;
  overflow: auto;
  padding-right: 4px;
}

.detail-section {
  margin-bottom: 16px;

  &__title {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 10px;
    font-size: 14px;
    font-weight: 600;
    color: #303133;

    &::before {
      content: '';
      width: 3px;
      height: 14px;
      border-radius: 2px;
      background: var(--el-color-primary, #1890ff);
    }
  }
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px 24px;
  padding: 12px 16px;
  background: #fafbfc;
  border: 1px solid #f0f2f5;
  border-radius: 8px;
}

.detail-item {
  display: flex;
  gap: 10px;
  min-width: 0;
  font-size: 13px;
  line-height: 1.6;

  &--block {
    grid-column: 1 / -1;
  }

  &__label {
    flex-shrink: 0;
    width: 64px;
    color: #909399;
  }

  &__value {
    min-width: 0;
    color: #303133;
    word-break: break-all;

    &--money {
      font-weight: 600;
      color: #f56c6c;
    }
  }
}

.detail-summary {
  margin-top: 4px;
  padding: 12px 16px;
  background: #fafbfc;
  border: 1px solid #f0f2f5;
  border-radius: 8px;
}

.summary-row {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 24px;
  font-size: 13px;
  color: #606266;
  line-height: 2;

  span:first-child {
    color: #909399;
  }

  &--total {
    margin-top: 4px;
    padding-top: 8px;
    border-top: 1px dashed #ebeef5;
    font-size: 15px;
    font-weight: 600;
    color: #303133;

    span:last-child {
      color: #f56c6c;
    }
  }
}
</style>
