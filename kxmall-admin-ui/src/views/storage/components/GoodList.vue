<template>
  <el-dialog title="添加商品" :visible.sync="open" append-to-body>
    <el-form ref="form" :model="form" inline>
      <el-form-item prop="categoryId">
        <el-cascader
          v-model="form.categoryId"
          :options="categories"
          :props="{ checkStrictly: true ,value:'id'}"
          placeholder="请选择类目"
          clearable
          filterable
        />
      </el-form-item>
      <el-form-item prop="keyword">
        <el-input
          v-model="form.keyword"
          clearable
          placeholder="请输入商品条码/商品ID/商品名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="onQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="stockList" height="500" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商品条码" align="left" header-align="center" prop="barCode" />
      <el-table-column label="商品名称" align="left" header-align="center" prop="productName" />
      <el-table-column label="商品规格" align="left" header-align="center" prop="productAttrName" />
      <el-table-column label="操作" align="center" class-name="small-padding" width="80">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            @click="onAdd(scope.row)"
          >添加</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="form.pageNum"
      :limit.sync="form.pageSize"
      @pagination="getList"
    />
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onOk">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { listStock } from '@/api/storage/stock.js'
import { listStoreCategoryTree } from '@/api/product/storeCategory'

export default {
  name: 'GoodList',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    storageId: {
      type: [Number, String],
      default: ''
    },
    selectedData: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      loading: true,
      // // 选中数组
      selection: [],
      total: 0,
      form: {
        pageNum: 1,
        pageSize: 10,
        categoryId: [],
        keyword: ''
      },
      categories: [],
      stockList: []
    }
  },
  computed: {
    open: {
      get() {
        return this.visible
      },
      set() {
        this.close()
      }
    },
    selectedIds() {
      return this.selectedData.map(el => el.productId)
    }
  },
  watch: {
    visible: {
      handler(val) {
        if (val) {
          this.reset()
          this.getList()
        }
      }
    }
  },
  created() {
    this.listStoreCategoryTree()
  },
  methods: {
    reset() {
      this.resetForm('form')
    },
    getList() {
      this.stockList = []
      this.loading = true
      const { form, selectedIds } = this
      let { categoryId } = form
      if (categoryId.length > 1) {
        categoryId = categoryId[length - 1] || null
      }
      listStock({ storageId: this.storageId, ...this.form, categoryId, notIds: selectedIds }).then(response => {
        this.stockList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    listStoreCategoryTree() {
      listStoreCategoryTree().then(({ data }) => {
        this.categories = data.content
      })
    },
    onQuery() {
      this.form.pageNum = 1
      this.getList()
    },
    handleSelectionChange(selection) {
      this.selection = selection
    },
    onSelectCategory(value) {
      this.form.categoryId = value[value.length - 1]
    },
    getSelection() {
      return this.selection
    },
    onAdd(row) {
      this.$notify({
        title: '提示',
        message: row.productName + ' 添加成功'
      })
      this.$emit('add', row)
      this.$nextTick(() => {
        this.getList()
      })
    },
    close() {
      this.$emit('update:visible', false)
    },
    onOk() {
      this.$emit('add', this.selection)
      this.close()
    },
    cancel() {
      this.close()
    }
  }
}
</script>
