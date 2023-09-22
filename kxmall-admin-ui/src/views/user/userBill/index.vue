<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="" prop="nickname">
        <el-input v-model="queryParams.nickname" clearable placeholder="输入用户昵称" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="" prop="category">
        <el-select v-model="queryParams.category" clearable placeholder="明细种类">
          <el-option v-for="item in categories" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="明细类型">
          <el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="inOuttype">
        <el-select v-model="queryParams.inOuttype" clearable placeholder="进出账">
          <el-option v-for="item in inOuts" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="shibai">
        <el-select v-model="queryParams.shibai" clearable placeholder="账单标题">
          <el-option v-for="item in shibais" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="dateRange">
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          unlink-panels
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="userBillList">
      <el-table-column type="index" label="序号" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="用户昵称" header-align="center" />
      <el-table-column prop="title" label="账单标题" header-align="center" />
      <el-table-column label="明细种类" align="center">
        <template slot-scope="{row}">
          <span v-if="row.category == 'now_money'">余额</span>
          <span v-else-if="row.category == 'integral'">积分</span>
          <span v-else>未知</span>
        </template>
      </el-table-column>
      <el-table-column label="明细数字" header-align="center" align="right">
        <template slot-scope="{row}">
          <span v-if="row.pm == 1">+</span>
          <span v-else>-</span>
          <span>{{ row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { listUserBill } from '@/api/user/userBill'

export default {
  name: 'UserBill',
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 用户账单表格数据
      userBillList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickname: undefined,
        category: undefined,
        type: undefined,
        inOuttype: undefined,
        shibai: undefined,
        dateRange: []
      },
      categories: [
        { value: 'now_money', label: '余额' },
        { value: 'integral', label: '积分' }
      ],
      types: [
        { value: 'brokerage', label: '佣金' },
        { value: 'sign', label: '签到' }
      ],
      inOuts: [
        { value: 0, label: '支出' },
        { value: 1, label: '获得' }
      ],
      shibais: [
        { value: '获得推广佣金', label: '获得推广佣金' },
        { value: '系统增加余额', label: '增加余额' },
        { value: '系统减少余额', label: '减少余额' },
        { value: '佣金提现', label: '佣金提现' },
        { value: '购买商品', label: '购买商品' },
        { value: '商品退款', label: '商品退款' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户账单列表 */
    getList() {
      this.loading = true
      const { queryParams } = this
      const { dateRange } = queryParams
      const params = { ...queryParams }
      delete params.dateRange
      params.startTime = dateRange[0]
      params.endTime = dateRange[1]
      listUserBill(params).then(response => {
        this.userBillList = response.rows
        this.total = response.total
        this.loading = false
      })
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
    }
  }
}
</script>
