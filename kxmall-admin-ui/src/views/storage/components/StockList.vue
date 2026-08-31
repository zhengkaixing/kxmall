<template>
  <el-dialog :title="title" v-model="dialogVisible" append-to-body width="900px">

    <el-form ref="form" class="query-form stock-form" :model="form" inline :disabled="isViewMode" :rules="rules" label-width="80px">
      <el-form-item :label="text+'仓库'" prop="storageId">
        <el-select v-model="form.storageId" :placeholder="`请选择${text}仓库`" clearable filterable :disabled="isUpdateMode" @change="onStorageChange">
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remarks" class="query-form__wide">
        <el-input v-model="form.remarks" placeholder="请输入备注" clearable />
      </el-form-item>
    </el-form>

    <div class="stock-toolbar">
      <el-button type="primary" icon="Plus" :disabled="isViewMode" @click="onAdd">添加</el-button>
      <el-button icon="Delete" :disabled="isViewMode || multiple" @click="onDelete">删除</el-button>
    </div>

    <el-table
      :data="list"
      stripe
      class="list-table"
      header-cell-class-name="list-table-header"
      height="500"
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
        <template #default="scope">
          <template v-if="scope && scope.row">
            <div v-if="isViewMode">
              <span>{{ scope.row[type + 'StockNum'] }}</span>
            </div>
            <div v-else>
              <el-input v-model="scope.row[type + 'StockNum']" clearable placeholder="请输入内容" />
            </div>
          </template>
        </template>
      </el-table-column>
      <el-table-column
        v-if="!isViewMode"
        fixed="right"
        align="center"
        label="操作"
        width="80"
      >
        <template #default="scope">
          <el-button v-if="scope && scope.row" type="primary" size="small" @click="onDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <good-list ref="goodList" :storage-id="form.storageId" v-model="open" :selected-data="list" @add="onAddGood" />
    <template #footer><div class="dialog-footer">
      <el-button type="primary" @click="onOk">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div></template>
  </el-dialog>

</template>
<script>
import { listAllStorage } from '@/api/storage/storage'
import { addGoodsInStock, getGoodsInStock, updateGoodsInStock } from '@/api/storage/goodsInStock'
import { addGoodsOutStock, getGoodsOutStock, updateGoodsOutStock } from '@/api/storage/goodsOutStock'
import GoodList from './GoodList.vue'
export default {
  name: 'StockList',
  inheritAttrs: false,
  components: {
    GoodList
  },
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    visible: {
      type: Boolean,
      default: false
    },
    id: {
      type: [String, Number],
      default: ''
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
        return !value || ['add', 'view', 'update'].indexOf(value) > -1
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
    dialogVisible: {
      get() {
        return this.modelValue || this.visible
      },
      set(val) {
        this.$emit('update:modelValue', val)
        this.$emit('update:visible', val)
      }
    },
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
    }
  },
  watch: {
    dialogVisible: {
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
  created() {
    this.listAllStorage()
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
      listAllStorage().then(res => {
        this.storages = res.data || []
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
      this.dialogVisible = false
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
<style lang="scss" scoped>
.stock-form {
  margin-bottom: 4px;
}

.stock-toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0 16px;
}
</style>
