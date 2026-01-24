<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true"label-width="68px">

      <el-form-item>
        <el-date-picker
          v-model="queryParams.selectedMonth"
          type="month"
          value-format="yyyy-MM"
          placeholder="选择月份"
        ></el-date-picker>
      </el-form-item>

      <el-form-item prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择前置仓" clearable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="fetchReport">搜索</el-button>
      </el-form-item>
    </el-form>


    <el-table :data="salesReport" style="width: 100%">
      <el-table-column align="center" label="日期" prop="orderDate" width="150"></el-table-column>
      <el-table-column align="center" label="仓库" prop="storageName" width="150"></el-table-column>
      <el-table-column align="center" label="销售量" prop="totalSalesVolume"></el-table-column>
      <el-table-column align="center" label="销售额" prop="totalSalesAmount"></el-table-column>
      <el-table-column align="center" label="优惠券费用" prop="totalCouponAmount"></el-table-column>
      <el-table-column align="center" label="实收金额" prop="actualPaymentAmount"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import {warehouseAnalysis} from "@/api/finance/finance";
import {listAllStorage} from "@/api/storage/storage";

export default {
  data() {
    return {
      // 查询参数
      queryParams: {
        selectedMonth: null,
        storageId: null
      },
      salesReport: [],
      storages: [],
    };
  },
  created() {
    this.listAllStorage()
    this.fetchReport();
  },
  methods: {
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    fetchReport() {
      if(!this.queryParams.storageId){
        this.queryParams.storageId =''
      }
      console.log(this.storages)
      warehouseAnalysis(this.queryParams.selectedMonth,this.queryParams.storageId).then(res => {
        this.salesReport = res;
      });
    }
  }
};
</script>
