<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item>
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions"
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

    <div class="product-card-list">
      <el-card 
        v-for="(item, index) in productSummaryList" 
        :key="index" 
        :class="['product-card', {'stock-warning-card': isStockLow(item)}]"
        shadow="hover"
      >
        <div class="card-header">
          <h3 class="product-title">{{ item.productTitle }}</h3>
          <span class="product-spec" v-if="item.productAttrTitle">{{ item.productAttrTitle }}</span>
        </div>
        <div class="card-content">
          <div class="info-item">
            <span class="label">购买人数：</span>
            <span class="value highlight">{{ item.buyerCount }}人</span>
          </div>
          <div class="info-item">
            <span class="label">总数量：</span>
            <span class="value">{{ item.totalNum }}{{ item.unitName }}</span>
          </div>
          <div class="info-item">
            <span class="label">总金额：</span>
            <span class="value">¥{{ item.totalAmount }}</span>
          </div>
          <div class="info-item">
            <span class="label">当前库存：</span>
            <span class="value" :class="{'stock-low': isStockLow(item)}">
              {{ item.stock || 0 }}{{ item.unitName }}
            </span>
          </div>
          <div class="info-item stock-suggestion">
            <span class="label">建议备货量：</span>
            <span class="value stock-value">{{ item.totalNum }}{{ item.unitName }}</span>
          </div>
          <div v-if="isStockLow(item)" class="stock-warning">
            <el-alert
              :title="getStockWarningText(item)"
              type="warning"
              :closable="false"
              show-icon
            ></el-alert>
          </div>
        </div>
      </el-card>
      <div v-if="productSummaryList.length === 0" class="empty-state">
        <el-empty description="暂无数据"></el-empty>
      </div>
    </div>
  </div>
</template>

<script>
import { productSummary } from "@/api/finance/finance";
import { listAllStorage } from "@/api/storage/storage";

export default {
  name: 'ProductSummary',
  data() {
    return {
      // 查询参数
      queryParams: {
        dateRange: null,
        storageId: null
      },
      productSummaryList: [],
      storages: [],
      pickerOptions: {
        shortcuts: [{
          text: '昨天',
          onClick(picker) {
            const yesterday = new Date();
            yesterday.setTime(yesterday.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [yesterday, yesterday]);
          }
        }, {
          text: '过去7天',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '过去30天',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 29);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '本月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setDate(1);
            picker.$emit('pick', [start, end]);
          }
        }]
      }
    };
  },
  created() {
    this.listAllStorage()
    // 默认选择过去7天
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
    this.queryParams.dateRange = [
      this.formatDate(start),
      this.formatDate(end)
    ];
    this.fetchReport();
  },
  methods: {
    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    fetchReport() {
      let startDate = null;
      let endDate = null;
      if (this.queryParams.dateRange && this.queryParams.dateRange.length === 2) {
        startDate = this.queryParams.dateRange[0];
        endDate = this.queryParams.dateRange[1];
      }
      productSummary(startDate, endDate, this.queryParams.storageId).then(({ data }) => {
        this.productSummaryList = data;
      });
    },
    isStockLow(item) {
      if (!item.stock && item.stock !== 0) {
        return false;
      }
      return item.stock < item.totalNum;
    },
    getStockWarningText(item) {
      const needStock = item.totalNum - (item.stock || 0);
      const days = this.getDateRangeDays();
      return `库存不足！当前库存${item.stock || 0}${item.unitName}，建议备货${item.totalNum}${item.unitName}，需要补货${needStock}${item.unitName}（基于过去${days}天的销售数据）`;
    },
    getDateRangeDays() {
      if (!this.queryParams.dateRange || this.queryParams.dateRange.length !== 2) {
        return 7;
      }
      const start = new Date(this.queryParams.dateRange[0]);
      const end = new Date(this.queryParams.dateRange[1]);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      return diffDays;
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.product-card-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.product-card {
  transition: all 0.3s;
  border-radius: 8px;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-card.stock-warning-card {
  border: 2px solid #E6A23C;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.product-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  flex: 1;
}

.product-spec {
  font-size: 12px;
  color: #909399;
  background: #f4f4f5;
  padding: 4px 8px;
  border-radius: 4px;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.info-item .label {
  color: #606266;
}

.info-item .value {
  color: #303133;
  font-weight: 500;
}

.info-item .value.highlight {
  color: #409EFF;
  font-weight: 600;
}

.stock-suggestion {
  margin-top: 8px;
  padding-top: 12px;
  border-top: 2px dashed #e4e7ed;
}

.stock-suggestion .label {
  font-weight: 600;
  color: #303133;
}

.stock-value {
  color: #409EFF;
  font-size: 18px;
  font-weight: bold;
}

.stock-low {
  color: #E6A23C;
  font-weight: 600;
}

.stock-warning {
  margin-top: 12px;
}

.stock-warning .el-alert {
  padding: 8px 12px;
}

.empty-state {
  grid-column: 1 / -1;
  padding: 40px 0;
}

@media (max-width: 768px) {
  .product-card-list {
    grid-template-columns: 1fr;
  }
}
</style>

