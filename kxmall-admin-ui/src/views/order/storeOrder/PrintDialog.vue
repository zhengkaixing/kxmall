<template>
  <el-dialog
    v-model="open"
    width="860px"
    append-to-body
    class="print-dialog"
    :before-close="onClose"
  >
    <template #header>
      <div class="print-dialog__header">
        <span class="print-dialog__title">订货单预览</span>
        <el-button v-print="'#content'" type="primary" icon="Printer">打印</el-button>
      </div>
    </template>
    <div class="print-dialog__body">
      <div id="content" class="print-content">
        <div v-for="item in list" :key="item.orderId" class="order-sheet">
          <h1 class="order-sheet__title">订货单</h1>
          <div class="order-sheet__meta">
            <div class="meta-item">
              <span class="meta-item__label">订单号</span>
              <span class="meta-item__value">{{ item.orderId }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__label">下单日期</span>
              <span class="meta-item__value">{{ item.addTime }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__label">客户名称</span>
              <span class="meta-item__value">{{ item.realName }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__label">联系人</span>
              <span class="meta-item__value">{{ item.userDTO && item.userDTO.account }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__label">联系电话</span>
              <span class="meta-item__value">{{ item.userPhone }}</span>
            </div>
            <div class="meta-item meta-item--block">
              <span class="meta-item__label">收货地址</span>
              <span class="meta-item__value">{{ item.userAddress }}</span>
            </div>
          </div>

          <el-table
            border
            show-summary
            :data="item.cartInfoList"
            class="list-table order-sheet__table"
            header-cell-class-name="list-table-header"
            :summary-method="getSummaries"
          >
            <el-table-column type="index" label="序号" width="56" align="center" />
            <el-table-column prop="cartInfoMap.productInfo.productId" label="商品编号" align="center" min-width="100" />
            <el-table-column prop="cartInfoMap.productInfo.storeName" label="商品名称" header-align="center" min-width="140" />
            <el-table-column prop="cartInfoMap.productInfo.unitName" label="商品规格" align="center" min-width="90" />
            <el-table-column prop="cartInfoMap.productInfo.unitName" label="单位" align="center" width="70" />
            <el-table-column prop="cartInfoMap.productInfo.price" label="单价" header-align="center" align="right" width="90" />
            <el-table-column prop="cartInfoMap.cartNum" label="数量" align="center" width="70" />
            <el-table-column label="小计(元)" header-align="center" align="right" prop="sum" width="100">
              <template #default="{row}">
                <span>{{ multiply(row.cartInfoMap.productInfo.price, row.cartInfoMap.cartNum) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="cartInfoMap.mark" label="备注" header-align="center" min-width="100" />
          </el-table>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import { add, multiply } from '@/utils/math'

export default {
  name: 'OrderPrintDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    list: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      open: false
    }
  },
  watch: {
    visible(val) {
      this.open = val
    }
  },
  methods: {
    multiply,
    onClose(done) {
      this.$emit('update:visible', false)
      done()
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        } else if (index === 7) {
          const values = data.map(item => multiply(item.cartInfoMap.productInfo.price, item.cartInfoMap.cartNum))
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return add(prev, curr)
            } else {
              return prev
            }
          }, 0)
        } else if (index === 6) {
          const values = data.map(item => item.cartInfoMap.cartNum)
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return add(prev, curr)
            } else {
              return prev
            }
          }, 0)
        }
      })

      return sums
    }
  }
}
</script>
<style lang="scss" scoped>
.print-dialog__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-right: 28px;
}

.print-dialog__title {
  display: flex;
  align-items: center;
  gap: 8px;
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

.print-dialog__body {
  max-height: 70vh;
  overflow: auto;
  padding: 4px 2px 8px;
}

.print-content {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 8px;
}

.order-sheet {
  background: #fff;
  padding: 24px 28px 20px;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);

  & + & {
    margin-top: 16px;
    page-break-before: always;
  }
}

.order-sheet__title {
  margin: 0 0 20px;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 6px;
  text-align: center;
  color: #1f2d3d;
}

.order-sheet__meta {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px 16px;
  margin-bottom: 16px;
  padding: 12px 14px;
  background: #fafbfc;
  border: 1px solid #f0f2f5;
  border-radius: 8px;
}

.meta-item {
  min-width: 0;
  display: flex;
  gap: 8px;
  font-size: 13px;
  line-height: 1.5;

  &--block {
    grid-column: 1 / -1;
  }
}

.meta-item__label {
  flex-shrink: 0;
  color: #909399;
}

.meta-item__value {
  min-width: 0;
  color: #303133;
  word-break: break-all;
}

.order-sheet__table {
  width: 100%;
}

@media print {
  .print-content {
    background: #fff;
    padding: 0;
  }

  .order-sheet {
    box-shadow: none;
    border-radius: 0;
    padding: 0;
  }

  .order-sheet__meta {
    background: #fff;
  }
}
</style>
