<template>
  <div class="app-container">
    <el-tabs v-model="queryParams.orderStatus" @tab-click="onOrderStatusChange">
      <el-tab-pane v-for="item in status" :key="item.name" :label="item.label" :name="item.name" />
    </el-tabs>
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择前置仓" clearable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="orderId">
        <el-input v-model="queryParams.orderId" clearable placeholder="输入订单号" @keyup.enter.native="toQuery" />
      </el-form-item>
      <el-form-item prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          start-placeholder="创建开始时间"
          end-placeholder="创建结束时间"
          range-separator="至"
          unlink-panels
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="storeOrderList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column type="index" label="序号" align="center">
        <template slot-scope="{$index}">
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
        <template slot-scope="{row}">
          <span>{{ row.riderName? row.riderName : '商家自配' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="left"
        width="100"
        prop="realName"
        label="姓名"
      />
      <el-table-column
        align="left"
        width="110"
        prop="userPhone"
        label="电话"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="left"
        width="80"
        prop="address"
        label="订单类型"
      >
        <template slot-scope="{row}">
          <el-tag :type="row.shippingType==0 ? 'error' : 'success'">{{ row.shippingType==1?'配送':'自提' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="left"
        width="200"
        prop="userAddress"
        label="地址"
      />
      <el-table-column
        align="center"
        label="配送费"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.freightPrice == 0?'免配送':scope.row.freightPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="订单金额"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.originalTotalPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="实付金额"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.payPrice?scope.row.payPrice:'' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="200"
        align="center"
        label="要求送达时间"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.predictTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="phone"
        width="160"
        align="center"
        label="下单时间"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        width="160"
        label="支付时间"
        prop="payTime"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.payTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        width="280"
        align="center"
        label="付款交易号"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.payId }}</span>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        width="160"
        label="创建时间"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        width="160"
        label="修改时间"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        align="center"
        label="操作"
        width="300"
      >
        <template slot-scope="{row}">
          <el-button
            type="primary"
            size="mini"
            @click="viewDetail(row.id)"
          >详情</el-button>
          <el-button
            v-if="row.status===14"
            type="primary"
            size="mini"
            @click="handleOperation(row.id,'startStocking')"
          >开始配货</el-button>
          <el-button
            v-if="row.status===16"
            type="primary"
            size="mini"
            @click="handleOperation(row.id,'completeAllocation')"
          >完成配货</el-button>
          <!--<el-button
            v-if="(row.status===20||row.status===32||row.status===30) && row.orderType===0"
            type="primary"
            size="mini"
            @click="allot(row.storeId,row.orderNo,row.postId)"
          >{{ row.postId ? '重新配送' : '配送' }}</el-button>-->
          <el-button
            v-if="row.status===20 && row.shippingType===1"
            type="primary"
            size="mini"
            @click="handleOperation(row.id,'merchantDistribution')"
          >商家自配</el-button>
          <el-button
            v-if="row.status===30"
            type="primary"
            size="mini"
            @click="handleOperation(row.id,'completeDelivery')"
          >完成配送</el-button>
          <!--<el-button
            v-if="(row.status===20||row.status===32||row.status===30) && row.orderType===1"
            type="success"
            size="mini"
            @click="completeRider(row.storeId,row.orderNo)"
          >完成取货</el-button> -->
        </template>
      </el-table-column>
      <!-- <el-table-column prop="orderId" width="200" label="订单号" header-align="center">
        <template slot-scope="{row}">
          <div>{{ row.orderId }}</div>
          <div>{{ row.pinkName }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="realName" label="用户昵称" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.realName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="cartInfoList" width="400" label="商品信息" header-align="center">
        <template slot-scope="{row}">
          <div v-for="(item, index) in row.cartInfoList" :key="index">
            <span v-if="item.cartInfoMap.productInfo.attrInfo">
              <img style="width: 30px;height: 30px;margin:0;" :src="item.cartInfoMap.productInfo.attrInfo.image">
            </span>
            <span v-else>
              <img style="width: 30px;height: 30px;margin:0;" :src="item.cartInfoMap.productInfo.image">
            </span>
            <span>
              {{ item.cartInfoMap.productInfo.storeName }}
              <span v-if="item.cartInfoMap.productInfo.attrInfo">&nbsp;{{ item.cartInfoMap.productInfo.attrInfo.sku
              }}</span>
            </span>
            <span> | ￥{{ multiply(item.cartInfoMap.truePrice, item.cartInfoMap.cartNum) }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="payPrice" label="实际支付" header-align="center" align="right" />
      <el-table-column prop="payIntegral" label="消费积分" align="center" />
      <el-table-column prop="payTypeName" label="支付状态" align="center" />
      <el-table-column prop="statusName" label="订单状态" header-align="center">
        <template slot-scope="{row}">
          <span v-html="row.statusName" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" width="160" label="创建时间" align="center" :formatter="formatDate" />
      <el-table-column label="操作" align="center" class-name="small-padding" fixed="right" width="200">
        <template slot-scope="{row}">
          <el-button size="mini" type="primary" style="margin-right: 5px;" @click="viewDetail(row.id)">
            订单详情
          </el-button>
          <el-dropdown size="mini" split-button type="success" trigger="click" @command="c => onCommand(c, row)">
            操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="remark">
                订单备注
              </el-dropdown-item>
              <el-dropdown-item v-if="row._status == 2" command="deliver">
                去发货
              </el-dropdown-item>
              <el-dropdown-item v-if="row._status == 4" command="express">
                修改快递
              </el-dropdown-item>
              <el-dropdown-item v-if="row._status == 3" command="refund">
                立刻退款
              </el-dropdown-item>
              <el-dropdown-item v-if="row._status == 1" v-hasPermi="['order:storeOrder:edit']" command="update">
                修改
              </el-dropdown-item>
              <el-dropdown-item v-if="row._status == 1" v-hasPermi="['order:storeOrder:remove']" command="delete">
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改订单对话框 -->
    <el-dialog title="订单详情" :visible.sync="open" width="800px" append-to-body>
      <el-form
        :data="form"
        label-position="left"
      >
        <el-form-item label="订单编号">
          <span>{{ form.orderId }}</span>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-tag>{{ getStatus(form.status) }}</el-tag>
        </el-form-item>
        <el-form-item label="下单时间">
          <span>{{ form.createTime }}</span>
        </el-form-item>
        <el-form-item label="要求送达时间">
          <span>{{ form.predictTime }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <span>{{ form.remark }}</span>
        </el-form-item>
        <el-form-item label="客户姓名">
          <span>{{ form.realName }}</span>
        </el-form-item>
        <el-form-item label="客户电话">
          <span>{{ form.userPhone }}</span>
        </el-form-item>
        <el-form-item label="客户地址">
          <span>{{ form.userAddress }}</span>
        </el-form-item>
        <el-form-item label="支付状态">
          <span>{{ getStatus(form.status) }}</span>
        </el-form-item>
        <el-form-item label="支付方式">
          <span>{{ form.payTypeName }}</span>
        </el-form-item>
        <el-form-item label="支付金额">
          <span>{{ form.payPrice }}</span>
        </el-form-item>
        <el-form-item label="交易号">
          <span>{{ form.payId }}</span>
        </el-form-item>
        <el-form-item label="支付时间">
          <span>{{ form.payTime }}</span>
        </el-form-item>
        <el-form-item label="商品信息">
          <el-table
            :data="form.productList"
            size="small"
            border
            fit
            highlight-current-row
          >
            <el-table-column
              align="center"
              label="序号"
              type="index"
            />
            <el-table-column
              align="center"
              label="条码"
              prop="barCode"
            />
              <!-- <span>一级类>二级类</span> -->
            </el-table-column>
            <el-table-column
              align="center"
              label="商品编码"
              prop="productId"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              align="center"
              label="商品名称"
              prop="productTitle"
            />
            <el-table-column
              align="center"
              label="数量"
              prop="num"
            />
            <el-table-column
              align="center"
              label="单位"
              prop="unitName"
            />
            <el-table-column
              align="center"
              label="原价"
            >
              <template slot-scope="{row}">
                <span>{{ row.otPrice  }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="售价"
            >
              <template slot-scope="{row}">
                <span>{{ row.price  }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="总价（单价*数量）"
              width="150"
            >
              <template slot-scope="{row}">
                <span>{{ row.price * row.num  }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="运费">
          <span>{{ form.freightPrice }}</span>
        </el-form-item>
        <el-form-item label="优惠">
          <span>{{ (form.originalTotalPrice-form.payPrice + form.freightPrice) }}</span>
        </el-form-item>
        <el-form-item label="商品金额">
          <span>{{ form.originalTotalPrice }}</span>
        </el-form-item>
        <el-form-item label="总金额">
          <span>{{ form.payPrice }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- <print-dialog :visible.sync="printDialog.open" :list="printDialog.list" /> -->
  </div>
</template>

<script>
// import dayjs from 'dayjs'
import { multiply } from '@/utils/math'
import { listStoreOrder /** delStoreOrder, addStoreOrder, updateStoreOrder*/, getStoreOrder, startStocking, completeAllocation, merchantDistribution, completeDelivery } from '@/api/order/storeOrder'
import { listAllStorage } from '@/api/storage/storage'
// import PrintDialog from './PrintDialog'

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
      // rules: {
      // },
      // types: [
      //   { value: 'orderId', label: '订单号' },
      //   { value: 'realName', label: '用户姓名' },
      //   { value: 'userPhone', label: '用户电话' }
      // ],
      // orderTypes: [
      //   { value: '0', label: '所有订单' },
      //   { value: '1', label: '普通订单' },
      //   { value: '2', label: '拼团订单' },
      //   { value: '3', label: '秒杀订单' },
      //   { value: '4', label: '砍价订单' },
      //   { value: '5', label: '核销订单' },
      //   { value: '6', label: '积分订单' }
      // ],
      // printDialog: {
      //   open: false,
      //   list: []
      // },
      // overview: {},
      // dialog: {
      //   modes: {
      //     remark: {
      //       title: '修改订单'
      //     },
      //     deliver: {
      //       title: '去发货'
      //     },
      //     express: {
      //       title: '修改快递'
      //     },
      //     refund: {
      //       title: '退款'
      //     },
      //     update: {
      //       title: '修改订单'
      //     }
      //   },
      //   currentMode: ''
      // },
      // express: [],
      status: [
        { label: '待配货', name: '14' },
        { label: '配货中', name: '16' },
        { label: '待配送', name: '20' },
        { label: '配送中', name: '30' },
        { label: '已完成', name: '50' },
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
      params.startTime = params.createTime[0]
      params.endTime = params.createTime[1]
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
    /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm('queryForm')
    //   this.handleQuery()
    // },
    // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.ids = selection.map(item => item)
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    // },
    /** 提交按钮 */
    // submitForm() {
    //   this.$refs['form'].validate(valid => {
    //     if (valid) {
    //       this.buttonLoading = true
    //       if (this.form.id != null) {
    //         updateStoreOrder(this.form).then(response => {
    //           this.$modal.msgSuccess('修改成功')
    //           this.open = false
    //           this.getList()
    //         }).finally(() => {
    //           this.buttonLoading = false
    //         })
    //       } else {
    //         addStoreOrder(this.form).then(response => {
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
    //   this.$modal.confirm('是否确认删除订单编号为"' + ids + '"的数据项？').then(() => {
    //     this.loading = true
    //     return delStoreOrder(ids)
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
    //     handleExport(command) {
    //       /** page: 0
    // size: 10000
    // sort: id,desc
    // orderStatus: -9
    // orderType: 0
    // createTime:
    // listContent: ["1627920685013663744","1627930721404321792","1627864121783353344"] */
    //       if (command === '0') {
    //         this.download('order/storeOrder/export', {}, `storeOrder_${new Date().getTime()}.xlsx`)
    //       } else if (command === '1') {
    //         const listContent = this.ids.map(item => item.id)
    //         this.download('order/storeOrder/export', { listContent: JSON.stringify(listContent) }, `storeOrder_${new Date().getTime()}.xlsx`)
    //       }
    //     },
    onOrderStatusChange() {
      this.handleQuery()
    },
    // handlePrint() {
    //   this.printDialog.list = this.ids
    //   this.printDialog.open = true
    // },
    // formatDate(row) {
    //   return dayjs(row.createTime).format('YYYY-MM-DD HH:mm:ss')
    // },
    // onCommand(c, row) {
    //   if (c === 'delete') {
    //     this.handleDelete(row)
    //   } else {
    //     this.dialog.currentMode = c
    //     this.reset(row)
    //     this.open = true
    //   }
    // },
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
.order-overview {
  float: right;
  text-align: right;
}
</style>
