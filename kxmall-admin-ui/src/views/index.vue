<template>
  <div class="dashboard-wrap">
    <el-card>
      <div slot="header">
        <b>今日用户数</b>
        <el-button style="float: right; padding: 3px 0" type="text" @click="todyUserMore">{{ todyUserShowMoreText }}</el-button>
        <el-button style="float: right; padding: 3px 10px" type="text" @click="todyUserFilter">刷新</el-button>
      </div>
      <el-table
        :data="countUserData"
        style="width: 100%">
        <el-table-column
          prop="statementDate"
          label="时间"
          align="center"
          width="180">
        </el-table-column>
        <el-table-column
          prop="totalUser"
          align="center"
          label="总用户数">
        </el-table-column>
        <el-table-column
          prop="newUser"
          align="center"
          label="新注册用户数">
        </el-table-column>
        <el-table-column
          prop="onlineUser"
          align="center"
          label="在线用户数">
        </el-table-column>
        <el-table-column
          prop="orderUser"
          align="center"
          label="下单用户数">
        </el-table-column>
        <el-table-column
          prop="firstOrderUser"
          align="center"
          label="首单用户数">
        </el-table-column>
      </el-table>
    </el-card>
    <el-card style="margin-top:16px;">
      <div slot="header">
        <b>今日销售排行</b>
        <el-select v-model="saleStorageId" placeholder="请选择" style="margin-left:16px;" clearable filterable @change="todySalesFilter">
          <el-option
            v-for="item in storageLists"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <el-button style="float: right; padding: 3px 0" type="text" @click="salesStatementMore">{{ todySortShowMoreText }}</el-button>
        <el-button style="float: right; padding: 3px 10px" type="text" @click="todySalesFilter('')">刷新</el-button>
      </div>
      <el-table
        :data="salesStatementData"
        style="width: 100%">
        <el-table-column
          prop="categoryTitle"
          label="类目"
          align="center"
          width="180">
        </el-table-column>
        <el-table-column
          align="center"
          label="总销售额/销量">
          <template slot-scope="scope">
            <div class="name-wrapper-number">
              <span class="moneySymbol">¥</span>
              <span>{{ scope.row.totalSalesVolume }}</span>
              <span> / </span>
              <span>{{ scope.row.totalSales }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="TOP1">
          <template slot-scope="scope">
            <el-popover trigger="hover" placemename-wrappernt="top">
              {{ scope.row.salesTopDTOs[0].title }}
              <div slot="reference" class="name-wrapper salesStatementName">
                {{ scope.row.salesTopDTOs[0].title }}
              </div>
            </el-popover>
            <el-popover trigger="hover" placement="top">
              销售额:{{ scope.row.salesTopDTOs[0].totalSalesVolume }}  销售单数:{{ scope.row.salesTopDTOs[0].totalSales }}
              <div slot="reference" class="name-wrapper name-wrapper-number">
                <span class="moneySymbol">¥</span>
                <span>{{ scope.row.salesTopDTOs[0].totalSalesVolume }}</span>
                <span> / </span>
                <span>{{ scope.row.salesTopDTOs[0].totalSales }}</span>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="TOP2">
          <template slot-scope="scope">
            <el-popover trigger="hover" placemename-wrappernt="top">
              {{ scope.row.salesTopDTOs[1].title }}
              <div slot="reference" class="name-wrapper salesStatementName">
                {{ scope.row.salesTopDTOs[1].title }}
              </div>
            </el-popover>
            <el-popover trigger="hover" placement="top">
              销售额:{{ scope.row.salesTopDTOs[1].totalSalesVolume }}  销售单数:{{ scope.row.salesTopDTOs[1].totalSales }}
              <div slot="reference" class="name-wrapper name-wrapper-number">
                <span class="moneySymbol">¥</span>
                <span>{{ scope.row.salesTopDTOs[1].totalSalesVolume }}</span>
                <span> / </span>
                <span>{{ scope.row.salesTopDTOs[1].totalSales }}</span>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="TOP3">
          <template slot-scope="scope">
            <el-popover trigger="hover" placemename-wrappernt="top">
              {{ scope.row.salesTopDTOs[2].title }}
              <div slot="reference" class="name-wrapper salesStatementName">
                {{ scope.row.salesTopDTOs[2].title }}
              </div>
            </el-popover>
            <el-popover trigger="hover" placement="top">
              销售额:{{ scope.row.salesTopDTOs[2].totalSalesVolume }}  销售单数:{{ scope.row.salesTopDTOs[2].totalSales }}
              <div slot="reference" class="name-wrapper name-wrapper-number">
                <span class="moneySymbol">¥</span>
                <span>{{ scope.row.salesTopDTOs[2].totalSalesVolume }}</span>
                <span> / </span>
                <span>{{ scope.row.salesTopDTOs[2].totalSales }}</span>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="TOP4">
          <template slot-scope="scope">
            <el-popover trigger="hover" placemename-wrappernt="top">
              {{ scope.row.salesTopDTOs[3].title }}
              <div slot="reference" class="name-wrapper salesStatementName">
                {{ scope.row.salesTopDTOs[3].title }}
              </div>
            </el-popover>
            <el-popover trigger="hover" placement="top">
              销售额:{{ scope.row.salesTopDTOs[3].totalSalesVolume }}  销售单数:{{ scope.row.salesTopDTOs[3].totalSales }}
              <div slot="reference" class="name-wrapper name-wrapper-number">
                <span class="moneySymbol">¥</span>
                <span>{{ scope.row.salesTopDTOs[3].totalSalesVolume }}</span>
                <span> / </span>
                <span>{{ scope.row.salesTopDTOs[3].totalSales }}</span>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="TOP5">
          <template slot-scope="scope">
            <el-popover trigger="hover" placemename-wrappernt="top">
              {{ scope.row.salesTopDTOs[4].title }}
              <div slot="reference" class="name-wrapper salesStatementName">
                {{ scope.row.salesTopDTOs[4].title }}
              </div>
            </el-popover>
            <el-popover trigger="hover" placement="top">
              销售额:{{ scope.row.salesTopDTOs[4].totalSalesVolume }}  销售单数:{{ scope.row.salesTopDTOs[4].totalSales }}
              <div slot="reference" class="name-wrapper name-wrapper-number">
                <span class="moneySymbol">¥</span>
                <span>{{ scope.row.salesTopDTOs[4].totalSalesVolume }}</span>
                <span> / </span>
                <span>{{ scope.row.salesTopDTOs[4].totalSales }}</span>
              </div>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-card style="margin-top:16px;">
      <div slot="header">
        <b>今日销售情况</b>
        <el-select v-model="saleInfoStorageId" placeholder="请选择" style="margin-left:16px;" clearable filterable @change="todySalesInfoFilter">
          <el-option
            v-for="item in storageLists"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <el-button style="float: right; padding: 3px 10px" type="text" @click="todySalesInfoFilter('')">刷新</el-button>
      </div>
      <el-row>
        <el-col :span="6"><div style="height:350px;">
          <div class="item">
            <div>今日销售额</div>
            <div class="blue">￥{{ todayAndYesterdaySalesData[0].totalSalesVolume }}</div>
            <div>昨日:￥{{ todayAndYesterdaySalesData[1].totalSalesVolume }}</div>
          </div>
          <div class="item">
            <div>今日销售单数</div>
            <div class="green">{{ todayAndYesterdaySalesData[0].totalSales }}</div>
            <div>昨日:{{ todayAndYesterdaySalesData[1].totalSales }}</div>
          </div>
        </div></el-col>
        <el-col :span="18"><div id="dayTable" style="height:350px;"></div></el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { countUser, getSalesStatement, getTodayAndYesterdaySales, getSalesByHour, storageList } from '@/api/dashboard/dashboard'
var echarts = require('echarts')
import store from '@/store'
export default {
  data() {
    return {
      storageLists: [], // 仓库列表
      storageId: '', // 仓库id
      saleStorageId: '', // 销售排行卡片对应的仓库
      saleInfoStorageId: '', // 今日销售情况的对应仓库Id
      countUserData: [], // 用户数量统计数据(根据是否显示更多 判断条数)
      countUserOriginData: [], // 用户数量统计数据
      todayAndYesterdaySalesData: [{}, {}], // 近两日销售情况
      salesStatementData: [], // 销售情况报表数据(根据是否显示更多 判断条数)
      salesStatementOriginData: [], // 销售情况报表数据
      salesByHourData: [], // 按小时统计的报表
      todyUserShowMore: 0, // 今日用户数 是否显示更多
      todyUserShowMoreText: '显示更多',
      todySortShowMore: 0, // 今日排行 是否显示更多
      todySortShowMoreText: '显示更多',
      value: '',
      dayTableData: {
        title: {
          text: ''
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['销售额', '销售单数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00']
        },
        yAxis: [{
          name: '销售额',
          type: 'value'
          // min: 90,
          // max: 240,
          // splitNumber: 10,
          // interval: 15
        }, {
          name: '销售单数',
          type: 'value'
          // min: 0,
          // max: 10,
          // splitNumber: 10,
          // interval: 1
        }],
        series: [
          {
            name: '销售额',
            type: 'line',
            // itemStyle: { normal: { label: { show: true }}},
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '销售单数',
            type: 'line',
            yAxisIndex: 1,
            // itemStyle: { normal: { label: { show: true }}},
            data: [3, 6, 7, 8, 9, 3, 5]
          }
        ]
      }
    }
  },
  created() {
    this.getStorageList()
    this.countUser()
    this.getTodayAndYesterdaySales(this.storageId)
    this.getSalesStatement(this.storageId)
    this.getSalesByHour(this.storageId)
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    async getStorageList() {
      const lists = await storageList()
      this.storageLists = lists.data
    },
    async countUser(data) {
      const lists = await countUser(data)
      this.countUserOriginData = lists.data
      if (this.todyUserShowMore) {
        this.countUserData = lists.data
      } else {
        this.countUserData = lists.data.slice(0, 2)
      }
    },
    async getTodayAndYesterdaySales(data) {
      const lists = await getTodayAndYesterdaySales(data)
      this.todayAndYesterdaySalesData = lists.data
    },
    salesStatementDataCheck(data) {
      // 销售报表返回的数据 top排行可能为空 为空时设置为空对象
      data.forEach(element => {
        for (var i = 0; i < 5; i++) {
          if (!element.salesTopDTOs[i]) {
            element.salesTopDTOs[i] = {
              'skuId': '',
              'spuId': '',
              'title': '无',
              'totalSales': 0,
              'totalSalesVolume': 0
            }
          }
        }
      })
    },
    async getSalesStatement(data) {
      const lists = await getSalesStatement(data)
      this.salesStatementDataCheck(lists.data)
      this.salesStatementOriginData = lists.data
      if (!this.todySortShowMore) {
        this.salesStatementData = lists.data.slice(0, 5)
      } else {
        this.salesStatementData = lists.data
      }
    },
    salesByHourDataFn(data) {
      // 根据返回的小时统计信息 设置折线图的数据
      const xArr = ['02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00', '24:00']
      this.dayTableData.xAxis.data = xArr.slice(0, data.length)
      const totalSales = []
      const totalSalesVolume = []
      data.forEach(item => {
        totalSales.push(item.totalSales)
        totalSalesVolume.push(item.totalSalesVolume)
      })
      this.dayTableData.series[0].data = totalSalesVolume
      this.dayTableData.series[1].data = totalSales
    },
    async getSalesByHour(data) {
      const lists = await getSalesByHour(data)
      this.salesByHourData = lists.data
      this.salesByHourDataFn(lists.data)
      const dayTable = echarts.init(document.getElementById('dayTable'))
      dayTable.setOption(this.dayTableData)
    },
    todyUserMore() {
      // 如果没有更多数据了 就提示没有更多了
      if (this.countUserData.length === this.countUserOriginData.length && !this.todyUserShowMore) {
        this.$notify({
          title: '提示',
          message: '没有更多数据了',
          type: 'success'
        })
        return false
      }
      // 今日用户数显示更多
      if (!this.todyUserShowMore) {
        this.todyUserShowMoreText = '收起'
        this.todyUserShowMore = 1
        this.countUserData = Object.assign([], this.countUserOriginData)
      } else {
        this.todyUserShowMoreText = '显示更多'
        this.todyUserShowMore = 0
        this.countUserData = this.countUserData.slice(0, 2)
      }
    },
    salesStatementMore() {
      // 如果没有更多数据了 就提示没有更多了
      if (this.salesStatementData.length === this.salesStatementOriginData.length && !this.todySortShowMore) {
        this.$notify({
          title: '提示',
          message: '没有更多数据了',
          type: 'success'
        })
        return false
      }
      if (!this.todySortShowMore) {
        this.todySortShowMoreText = '收起'
        this.todySortShowMore = 1
        this.salesStatementData = Object.assign([], this.salesStatementOriginData)
      } else {
        this.todySortShowMoreText = '显示更多'
        this.todySortShowMore = 0
        this.salesStatementData = this.salesStatementData.slice(0, 5)
      }
    },
    todySalesFilter(val) {
      this.getSalesStatement(val)
    },
    todySalesInfoFilter(val) {
      this.getTodayAndYesterdaySales(val)
      this.getSalesByHour(val)
    },
    todyUserFilter() {
      this.countUser()
    }

  }
}
</script>

<style   scoped>
.dashboard-wrap{
  padding:20px;
}
  .item{
    margin-top:60px;
  }
  .item>div{
    text-align:center;
    margin:12px 0px;
  }
  .item>div:nth-child(1){
    font-size:16px;
    color:#333;
  }
  .item>div:nth-child(3){
    color:#aaa;
  }
  .blue{
    color:#268FF9;
    font-size:20px;
  }
  .green{
    color:#75B530;
    font-size:20px;
  }
  .salesStatementName{
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .name-wrapper-number>span:nth-child(1){
    font-size:12px;
  }
  .name-wrapper-number>span:nth-child(2){
    font-size:16px;
  }
  .name-wrapper-number>span:nth-child(4){
    font-size:16px;
  }
</style>
