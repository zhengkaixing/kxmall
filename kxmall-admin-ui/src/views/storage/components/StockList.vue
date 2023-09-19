<template>
  <el-dialog :title="title" :visible.sync="show" append-to-body>

    <el-form ref="form" :model="form" inline :disabled="isViewMode" :rules="rules">
      <el-form-item :label="text+'仓库'" prop="storageId">
        <el-select v-model="form.storageId" :placeholder="`请选择${text}仓库`" clearable :disabled="isUpdateMode" @change="onStorageChange">
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input v-model="form.remarks" placeholder="请输入内容" />
      </el-form-item>
      <br>
      <el-form-item>
        <el-button type="primary" @click="onAdd">添加</el-button>
        <el-button :disabled="multiple" @click="onDelete">删除</el-button>
      </el-form-item>
    </el-form>

    <el-table
      :data="list"
      border
      height="500"
      style="margin-top:15px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-if="mode!=='view'"
        type="selection"
        align="center"
        width="55"
      />
      <el-table-column
        prop="categoryName"
        align="center"
        width="120"
        label="商品类目"
      />
      <el-table-column
        align="center"
        prop="barCode"
        label="商品条码"
      />
      <el-table-column
        align="center"
        prop="productName"
        label="商品名称"
      />
      <el-table-column
        align="center"
        prop="productAttrName"
        label="商品规格"
      />
      <el-table-column
        align="center"
        prop="stock"
        :label="text+'前可用量'"
      />
      <el-table-column
        align="center"
        :prop="type+'StockNum'"
        :label="text+'数量'"
      >
        <template slot-scope="{row}">
          <div v-if="isViewMode">
            <span>{{ row[type+'StockNum' ] }}</span>
          </div>
          <div v-else>
            <el-input v-model="row[type+'StockNum']" clearable placeholder="请输入内容" />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="!isViewMode"
        fixed="right"
        align="center"
        label="操作"
        width="80"
      >
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <good-list ref="goodList" :storage-id="form.storageId" :visible.sync="open" :selected-data="list" @add="onAddGood" />
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onOk">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>

</template>
<script>
import { listAllStorage } from '@/api/storage/storage'
import { addGoodsInStock, getGoodsInStock, updateGoodsInStock } from '@/api/storage/goodsInStock'
import { addGoodsOutStock, getGoodsOutStock, updateGoodsOutStock } from '@/api/storage/goodsOutStock'
import GoodList from './GoodList.vue'
export default {
  name: 'StockList',
  components: {
    GoodList
  },
  props: {
    id: {
      type: [String, Number],
      default: ''
    },
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'in',
      validator(value) {
        return ['in', 'out'].indexOf(value) > -1
      }
    },
    mode: {
      type: String,
      default: 'add',
      validator(value) {
        return ['add', 'view', 'update'].indexOf(value) > -1
      }
    }
  },
  data() {
    return {
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      form: { },
      storages: [],
      list: [],
      open: false,
      rules: {
        storageId: [
          { required: true, message: '请选择仓库' }
        ]
      }
    }
  },
  computed: {
    text() {
      return this.type === 'in' ? '入库' : '出库'
    },
    isGoodsIn() {
      return this.type === 'in'
    },
    isViewMode() {
      return this.mode === 'view'
    },
    isAddMode() {
      return this.mode === 'add'
    },
    isUpdateMode() {
      return this.mode === 'update'
    },
    show: {
      get() {
        return this.visible
      },
      set() {
        this.cancel()
      }
    }
  },
  watch: {
    visible: {
      handler(val) {
        if (val) {
          this.reset()
          this.listAllStorage()
          if (this.id) {
            this.getGoods()
          }
        }
      }
    }
  },
  methods: {
    reset() {
      this.list = []
      this.form = {
        storageId: '',
        remarks: ''
      }
    },
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
      })
    },
    async getGoods() {
      const { type } = this
      let res
      if (this.isGoodsIn) {
        res = await this.getGoodsInStock()
      } else {
        res = await this.getGoodsOutStock()
      }
      const { data } = res
      const { id, remarks, storageId } = data
      this.form = { id, storageId, remarks, [type + 'StockNumbers']: data[type + 'StockNumbers'] }
      this.list = data[type + 'StockProductVoList']
    },
    getGoodsInStock() {
      return getGoodsInStock(this.id)
    },
    getGoodsOutStock() {
      return getGoodsOutStock(this.id)
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    onAdd() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.open = true
        }
      })
    },
    onAddGood(good) {
      this.list = this.list.concat(good)
    },
    close() {
      this.$emit('update:visible', false)
    },
    cancel() {
      this.close()
    },
    onDelete(row) {
      let selection = this.ids
      if (row.id) {
        selection = [row.id]
      }
      this.list = this.list.filter(el => selection.indexOf(el.id) < 0)
    },
    async onOk() {
      if (this.isGoodsIn) {
        if (this.isAddMode) {
          await addGoodsInStock({ inStockProductVoList: this.list.map(el => {
            const data = { ...el }
            delete data.id
            return data
          }), ...this.form })
        }
        if (this.isUpdateMode) {
          await updateGoodsInStock({ inStockProductVoList: this.list, ...this.form })
        }
      } else {
        if (this.isAddMode) {
          await addGoodsOutStock({ outStockProductVoList: this.list.map(el => {
            const data = { ...el }
            delete data.id
            return data
          }), ...this.form })
        }
        if (this.isUpdateMode) {
          await updateGoodsOutStock({ outStockProductVoList: this.list, ...this.form })
        }
      }
      this.$emit('ok')
      this.close()
    },
    onStorageChange() {
      this.list = []
    }
  }
}
</script>
