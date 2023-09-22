<template>
  <el-dialog :visible.sync="open" width="770px" append-to-body :before-close="onClose">
    <template slot="title">
      <el-button v-print="'#content'" type="primary" size="mini" icon="el-icon-printer">打印</el-button>
    </template>
    <div id="content">
      <div v-for="item in list" :key="item.orderId">
        <h1 class="header">订货单</h1>
        <el-row class="info">
          <el-col :span="8">{{ item.orderId }}</el-col>
          <el-col :span="8">下单日期 : {{ item.addTime }}</el-col>
          <el-col :span="8">客户名称 : {{ item.realName }}</el-col>
        </el-row>
        <el-row class="info">
          <el-col :span="8">联系人 : {{ item.userDTO.account }}</el-col>
          <el-col :span="8">联系电话 : {{ item.userPhone }}</el-col>
          <el-col :span="8">收货地址 : {{ item.userAddress }}</el-col>
        </el-row>

        <el-table border show-summary :data="item.cartInfoList" size="small" :summary-method="getSummaries">
          <el-table-column type="index" label="序号" width="50" align="center" />

          <el-table-column prop="cartInfoMap.productInfo.productId" label="商品编号" align="center" />

          <el-table-column prop="cartInfoMap.productInfo.storeName" label="商品名称" header-align="center" />

          <el-table-column prop="cartInfoMap.productInfo.unitName" label="商品规格" align="center" />

          <el-table-column prop="cartInfoMap.productInfo.unitName" label="单位" align="center" />

          <el-table-column prop="cartInfoMap.productInfo.price" label="单价" header-align="center" align="right" />

          <el-table-column prop="cartInfoMap.cartNum" label="数量" align="center" />

          <el-table-column label="小计(元)" header-align="center" align="right" prop="sum">
            <template slot-scope="{row}">
              <span>{{ multiply(row.cartInfoMap.productInfo.price, row.cartInfoMap.cartNum) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="cartInfoMap.mark" label="备注" header-align="center" />

        </el-table>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import { add, multiply } from '@/utils/math'
import print from 'vue-print-nb'

export default {
  name: 'OrderPrintDialog',
  directives: {
    print
  },
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
.header {
  font-weight: bolder;
  text-align: center;
}

.info {
  font-size: 12px;
  margin-bottom: 12px;
}

::v-deep .el-dialog__body {
  height: 700px;
  overflow: auto;
}
</style>
