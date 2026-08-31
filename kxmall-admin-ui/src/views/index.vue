<template>
  <div class="dashboard-wrap">
    <div class="dash-hero">
      <div>
        <h2>数据概览</h2>
        <p>实时掌握用户增长与销售表现1</p>
      </div>
    </div>

    <div class="kpi-grid">
      <div v-for="item in userKpiList" :key="item.key" class="kpi-card" :class="item.theme">
        <div class="kpi-card__icon">
          <svg-icon :icon-class="item.icon" class-name="kpi-icon" />
        </div>
        <div class="kpi-card__body">
          <div class="kpi-card__label">{{ item.label }}</div>
          <div class="kpi-card__value">
            <count-to :start-val="0" :end-val="item.value" :duration="1600" :separator="','" />
          </div>
        </div>
      </div>
    </div>

    <el-card class="dash-card" shadow="never">
      <template #header>
        <div class="card-head">
          <div class="card-head__title">今日用户数</div>
          <div class="card-head__actions">
            <el-button link type="primary" icon="RefreshRight" @click="todyUserFilter">刷新</el-button>
            <el-button link type="primary" @click="todyUserMore">{{ todyUserShowMoreText }}</el-button>
          </div>
        </div>
      </template>
      <el-table
        :data="countUserData"
        stripe
        class="dash-table"
        header-cell-class-name="dash-table-header"
      >
        <el-table-column prop="statementDate" label="时间" align="center" min-width="160" />
        <el-table-column prop="totalUser" align="center" label="总用户数" min-width="120" />
        <el-table-column prop="newUser" align="center" label="新注册用户数" min-width="130" />
        <el-table-column prop="onlineUser" align="center" label="在线用户数" min-width="120" />
        <el-table-column prop="orderUser" align="center" label="下单用户数" min-width="120" />
        <el-table-column prop="firstOrderUser" align="center" label="首单用户数" min-width="120" />
      </el-table>
    </el-card>

    <el-card class="dash-card" shadow="never">
      <template #header>
        <div class="card-head">
          <div class="card-head__title">
            今日销售排行
            <el-select
              v-model="saleStorageId"
              placeholder="全部仓库"
              class="card-head__select"
              clearable
              filterable
              @change="todySalesFilter"
            >
              <el-option
                v-for="item in storageLists"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </div>
          <div class="card-head__actions">
            <el-button link type="primary" icon="RefreshRight" @click="todySalesFilter('')">刷新</el-button>
            <el-button link type="primary" @click="salesStatementMore">{{ todySortShowMoreText }}</el-button>
          </div>
        </div>
      </template>
      <el-table
        :data="salesStatementData"
        stripe
        class="dash-table"
        header-cell-class-name="dash-table-header"
      >
        <el-table-column prop="categoryTitle" label="类目" align="center" min-width="140" />
        <el-table-column align="center" label="总销售额/销量" min-width="160">
          <template #default="scope">
            <div class="amount-cell">
              <span class="money-symbol">¥</span>
              <span class="amount-main">{{ scope.row.totalSalesVolume }}</span>
              <span class="amount-split">/</span>
              <span class="amount-sub">{{ scope.row.totalSales }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-for="rank in 5"
          :key="rank"
          align="center"
          :label="'TOP' + rank"
          min-width="150"
        >
          <template #default="scope">
            <div class="top-cell">
              <el-popover trigger="hover" placement="top">
                {{ topItem(scope.row, rank).title }}
                <template #reference>
                  <div class="top-cell__name">
                    <span class="rank-badge" :class="'rank-' + rank">{{ rank }}</span>
                    <span class="top-cell__title">{{ topItem(scope.row, rank).title }}</span>
                  </div>
                </template>
              </el-popover>
              <el-popover trigger="hover" placement="top">
                销售额:{{ topItem(scope.row, rank).totalSalesVolume }}  销售单数:{{ topItem(scope.row, rank).totalSales }}
                <template #reference>
                  <div class="amount-cell amount-cell--sub">
                    <span class="money-symbol">¥</span>
                    <span class="amount-main">{{ topItem(scope.row, rank).totalSalesVolume }}</span>
                    <span class="amount-split">/</span>
                    <span class="amount-sub">{{ topItem(scope.row, rank).totalSales }}</span>
                  </div>
                </template>
              </el-popover>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="dash-card dash-card--last" shadow="never">
      <template #header>
        <div class="card-head">
          <div class="card-head__title">
            今日销售情况
            <el-select
              v-model="saleInfoStorageId"
              placeholder="全部仓库"
              class="card-head__select"
              clearable
              filterable
              @change="todySalesInfoFilter"
            >
              <el-option
                v-for="item in storageLists"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </div>
          <div class="card-head__actions">
            <el-button link type="primary" icon="RefreshRight" @click="todySalesInfoFilter('')">刷新</el-button>
          </div>
        </div>
      </template>
      <div class="sales-info">
        <div class="sales-info__metrics">
          <div class="metric-card metric-card--blue">
            <div class="metric-card__label">今日销售额</div>
            <div class="metric-card__value">
              <span class="money-symbol">¥</span>
              <count-to :start-val="0" :end-val="toNumber(todaySales.totalSalesVolume)" :duration="1600" :separator="','" :decimals="2" />
            </div>
            <div class="metric-card__compare" :class="salesVolumeDiff.cls">
              昨日 ¥{{ yesterdaySales.totalSalesVolume || 0 }}
              <span v-if="salesVolumeDiff.text">{{ salesVolumeDiff.text }}</span>
            </div>
          </div>
          <div class="metric-card metric-card--green">
            <div class="metric-card__label">今日销售单数</div>
            <div class="metric-card__value">
              <count-to :start-val="0" :end-val="toNumber(todaySales.totalSales)" :duration="1600" :separator="','" />
            </div>
            <div class="metric-card__compare" :class="salesCountDiff.cls">
              昨日 {{ yesterdaySales.totalSales || 0 }}
              <span v-if="salesCountDiff.text">{{ salesCountDiff.text }}</span>
            </div>
          </div>
        </div>
        <div id="dayTable" class="sales-info__chart" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { countUser, getSalesStatement, getTodayAndYesterdaySales, getSalesByHour, storageList } from '@/api/dashboard/dashboard'
import * as echarts from 'echarts'
import { CountTo } from 'vue3-count-to'

export default {
  name: 'Index',
  components: {
    CountTo
  },
  data() {
    return {
      storageLists: [],
      storageId: '',
      saleStorageId: '',
      saleInfoStorageId: '',
      countUserData: [],
      countUserOriginData: [],
      todayAndYesterdaySalesData: [{}, {}],
      salesStatementData: [],
      salesStatementOriginData: [],
      salesByHourData: [],
      todyUserShowMore: 0,
      todyUserShowMoreText: '显示更多',
      todySortShowMore: 0,
      todySortShowMoreText: '显示更多',
      value: '',
      dayChart: null,
      dayTableData: {
        color: ['#1890ff', '#34bfa3'],
        title: {
          text: ''
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255,255,255,0.96)',
          borderColor: '#ebeef5',
          textStyle: { color: '#303133' }
        },
        legend: {
          data: ['销售额', '销售单数'],
          right: 16,
          top: 4
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '4%',
          top: 48,
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
          axisLine: { lineStyle: { color: '#dcdfe6' } },
          axisLabel: { color: '#909399' },
          data: ['02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00']
        },
        yAxis: [{
          name: '销售额',
          type: 'value',
          nameTextStyle: { color: '#909399' },
          axisLabel: { color: '#909399' },
          splitLine: { lineStyle: { type: 'dashed', color: '#ebeef5' } }
        }, {
          name: '销售单数',
          type: 'value',
          nameTextStyle: { color: '#909399' },
          axisLabel: { color: '#909399' },
          splitLine: { show: false }
        }],
        series: [
          {
            name: '销售额',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            areaStyle: {
              color: 'rgba(24,144,255,0.12)'
            },
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '销售单数',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            yAxisIndex: 1,
            data: [3, 6, 7, 8, 9, 3, 5]
          }
        ]
      }
    }
  },
  computed: {
    todayUserStat() {
      return this.countUserOriginData[0] || this.countUserData[0] || {}
    },
    userKpiList() {
      const stat = this.todayUserStat
      return [
        { key: 'totalUser', label: '总用户数', icon: 'peoples', theme: 'theme-cyan', value: this.toNumber(stat.totalUser) },
        { key: 'newUser', label: '新注册用户', icon: 'user', theme: 'theme-blue', value: this.toNumber(stat.newUser) },
        { key: 'onlineUser', label: '在线用户', icon: 'visits', theme: 'theme-green', value: this.toNumber(stat.onlineUser) },
        { key: 'orderUser', label: '下单用户', icon: 'money', theme: 'theme-red', value: this.toNumber(stat.orderUser) },
        { key: 'firstOrderUser', label: '首单用户', icon: 'star', theme: 'theme-orange', value: this.toNumber(stat.firstOrderUser) }
      ]
    },
    todaySales() {
      return this.todayAndYesterdaySalesData[0] || {}
    },
    yesterdaySales() {
      return this.todayAndYesterdaySalesData[1] || {}
    },
    salesVolumeDiff() {
      return this.diffText(this.todaySales.totalSalesVolume, this.yesterdaySales.totalSalesVolume)
    },
    salesCountDiff() {
      return this.diffText(this.todaySales.totalSales, this.yesterdaySales.totalSales)
    }
  },
  created() {
    this.getStorageList()
    this.countUser()
    this.getTodayAndYesterdaySales(this.storageId)
    this.getSalesStatement(this.storageId)
    this.getSalesByHour(this.storageId)
  },
  mounted() {
    window.addEventListener('resize', this.handleChartResize)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.handleChartResize)
    if (this.dayChart) {
      this.dayChart.dispose()
      this.dayChart = null
    }
  },
  methods: {
    toNumber(val) {
      const num = Number(val)
      return Number.isFinite(num) ? num : 0
    },
    topItem(row, rank) {
      const list = (row && row.salesTopDTOs) || []
      return list[rank - 1] || { title: '无', totalSales: 0, totalSalesVolume: 0 }
    },
    diffText(today, yesterday) {
      const t = this.toNumber(today)
      const y = this.toNumber(yesterday)
      if (y === 0) {
        if (t === 0) return { text: '', cls: '' }
        return { text: '↑ 新增', cls: 'is-up' }
      }
      const percent = ((t - y) / y) * 100
      if (percent > 0) {
        return { text: `↑ ${percent.toFixed(1)}%`, cls: 'is-up' }
      }
      if (percent < 0) {
        return { text: `↓ ${Math.abs(percent).toFixed(1)}%`, cls: 'is-down' }
      }
      return { text: '持平', cls: '' }
    },
    handleChartResize() {
      this.dayChart && this.dayChart.resize()
    },
    renderDayChart() {
      const el = document.getElementById('dayTable')
      if (!el) return
      if (!this.dayChart) {
        this.dayChart = echarts.init(el)
      }
      this.dayTableData.series[0].areaStyle = {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(24,144,255,0.28)' },
          { offset: 1, color: 'rgba(24,144,255,0.02)' }
        ])
      }
      this.dayChart.setOption(this.dayTableData)
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
      this.$nextTick(() => {
        this.renderDayChart()
      })
    },
    todyUserMore() {
      if (this.countUserData.length === this.countUserOriginData.length && !this.todyUserShowMore) {
        this.$notify({
          title: '提示',
          message: '没有更多数据了',
          type: 'success'
        })
        return false
      }
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

<style lang="scss" scoped>
.dashboard-wrap {
  min-height: calc(100vh - 84px);
  padding: 20px 24px 28px;
  background: #f5f7fa;
}

.dash-hero {
  margin-bottom: 18px;

  h2 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: #1f2d3d;
    letter-spacing: 0.5px;
  }

  p {
    margin: 6px 0 0;
    font-size: 13px;
    color: #909399;
  }
}

.kpi-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.kpi-card {
  display: flex;
  align-items: center;
  gap: 14px;
  min-height: 96px;
  padding: 18px 16px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  }

  &__icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 52px;
    height: 52px;
    border-radius: 12px;
    flex-shrink: 0;
  }

  &__label {
    font-size: 13px;
    color: #909399;
    line-height: 1.2;
  }

  &__value {
    margin-top: 8px;
    font-size: 22px;
    font-weight: 600;
    color: #303133;
    letter-spacing: 0.3px;
  }
}

.kpi-icon {
  font-size: 26px;
}

.theme-cyan {
  .kpi-card__icon {
    color: #40c9c6;
    background: rgba(64, 201, 198, 0.12);
  }
}

.theme-blue {
  .kpi-card__icon {
    color: #36a3f7;
    background: rgba(54, 163, 247, 0.12);
  }
}

.theme-green {
  .kpi-card__icon {
    color: #34bfa3;
    background: rgba(52, 191, 163, 0.12);
  }
}

.theme-red {
  .kpi-card__icon {
    color: #f4516c;
    background: rgba(244, 81, 108, 0.12);
  }
}

.theme-orange {
  .kpi-card__icon {
    color: #f5a623;
    background: rgba(245, 166, 35, 0.12);
  }
}

.dash-card {
  margin-bottom: 16px;
  border: none;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);

  &--last {
    margin-bottom: 0;
  }

  :deep(.el-card__header) {
    padding: 14px 20px;
    border-bottom: 1px solid #f0f2f5;
  }

  :deep(.el-card__body) {
    padding: 12px 16px 16px;
  }
}

.card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  min-height: 32px;

  &__title {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 12px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;

    &::before {
      content: '';
      width: 3px;
      height: 16px;
      border-radius: 2px;
      background: var(--el-color-primary, #1890ff);
    }
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 4px;
    flex-shrink: 0;
  }

  &__select {
    width: 180px;
  }
}

.dash-table {
  width: 100%;

  :deep(.dash-table-header) {
    background: #fafbfc;
    color: #606266;
    font-weight: 600;
  }

  :deep(.el-table__cell) {
    padding: 10px 0;
  }
}

.amount-cell {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 2px;
  color: #303133;

  &--sub {
    margin-top: 4px;
    color: #606266;
  }
}

.money-symbol {
  font-size: 12px;
  color: #909399;
}

.amount-main,
.amount-sub {
  font-size: 15px;
  font-weight: 600;
}

.amount-split {
  margin: 0 2px;
  color: #c0c4cc;
}

.top-cell {
  min-width: 0;
  padding: 0 8px;
}

.top-cell__name {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  min-width: 0;
}

.top-cell__title {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 110px;
  color: #303133;
}

.rank-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  font-size: 11px;
  font-weight: 700;
  color: #fff;
  background: #c0c4cc;
  flex-shrink: 0;
}

.rank-1 { background: #f5a623; }
.rank-2 { background: #8c9bb5; }
.rank-3 { background: #c97a4a; }

.sales-info {
  display: flex;
  gap: 16px;
  min-height: 340px;
}

.sales-info__metrics {
  width: 240px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sales-info__chart {
  flex: 1;
  min-width: 0;
  height: 340px;
}

.metric-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px 22px;
  border-radius: 12px;
  color: #fff;

  &__label {
    font-size: 13px;
    opacity: 0.88;
  }

  &__value {
    margin-top: 10px;
    font-size: 28px;
    font-weight: 700;
    letter-spacing: 0.4px;
    line-height: 1.2;

    .money-symbol {
      margin-right: 2px;
      font-size: 16px;
      color: rgba(255, 255, 255, 0.85);
    }
  }

  &__compare {
    margin-top: 10px;
    font-size: 12px;
    opacity: 0.9;

    span {
      margin-left: 8px;
      font-weight: 600;
    }

    &.is-up span {
      color: #fff;
    }

    &.is-down span {
      color: #ffe8e8;
    }
  }
}

.metric-card--blue {
  background: linear-gradient(135deg, #36a3f7 0%, #1890ff 100%);
}

.metric-card--green {
  background: linear-gradient(135deg, #40c9c6 0%, #34bfa3 100%);
}

@media (max-width: 1400px) {
  .kpi-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 992px) {
  .sales-info {
    flex-direction: column;
  }

  .sales-info__metrics {
    width: 100%;
    flex-direction: row;
  }

  .sales-info__chart {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .dashboard-wrap {
    padding: 16px;
  }

  .kpi-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .card-head {
    flex-wrap: wrap;
  }

  .sales-info__metrics {
    flex-direction: column;
  }
}
</style>
