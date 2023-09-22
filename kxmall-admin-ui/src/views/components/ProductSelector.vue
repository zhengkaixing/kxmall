<template>
  <div>
    <div class="list">
      <ul v-for="(item,index) in list" :key="item.id" class="el-upload-list el-upload-list--picture-card">
        <li tabindex="0" class="el-upload-list__item is-ready">
          <div>
            <img :src="item.image|getStringOSSURL" class="el-upload-list__item-thumbnail">
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="onDelete(index)">
                <i class="el-icon-delete" />
              </span>
            </span>
          </div>
        </li>
      </ul>
      <div tabindex="0" class="el-upload el-upload--picture-card" @click="onChoose">
        <i class="el-icon-plus" />
      </div>
    </div>

    <el-dialog title="商品列表" :visible.sync="open" append-to-body>
      <el-form ref="queryForm" :model="form" size="small" :inline="true" label-width="68px">
        <el-form-item label="" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="table" v-loading="loading" :data="productList" row-key="id" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" align="center">
          <template slot-scope="{$index}">
            {{ (form.pageNum - 1) * form.pageSize + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="商品图片" align="center">
          <template slot-scope="{row}">
            <el-link :href="row.image|getStringOSSURL" target="_blank" :underline="false">
              <el-image :src="row.image|getStringOSSURL" title="点击打开" class="el-avatar" />
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="storeName" label="商品名称" header-align="center" align="left" />
        <el-table-column prop="storeCategory.cateName" label="分类名称" header-align="center" align="left" />
        <el-table-column prop="price" label="商品价格" header-align="center" align="right" />
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="form.pageNum"
        :limit.sync="form.pageSize"
        @pagination="getList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listStoreProduct } from '@/api/product/storeProduct'
import getStringOSSURL from '@/mixin/getStringOSSURL'

export default {
  name: 'ProductSelector',
  mixins: [getStringOSSURL],
  props: {
    value: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      list: [],
      open: false,
      total: 0,
      form: {
        storeName: '',
        pageNum: 1,
        pageSize: 10
      },
      loading: false,
      productList: [],
      selectedList: []
    }
  },
  watch: {
    value: {
      handler(val) {
        this.list = val ? val.slice(0) : []
      },
      immediate: true,
      deep: true
    },
    open(val) {
      if (val) {
        this.getList()
      } else {
        this.form.pageNum = 1
        this.form.storeName = ''
      }
    }
  },
  methods: {
    getList() {
      this.loading = true
      listStoreProduct(this.form).then(response => {
        this.productList = response.rows
        this.total = response.total
        this.loading = false
        this.$nextTick(() => {
          this.setSelectedRows()
        })
      })
    },
    setSelectedRows() {
      const { list, productList } = this
      list.forEach(({ id }) => {
        for (let i = 0; i < productList.length; i++) {
          if (productList[i].id === id) {
            this.$refs.table.toggleRowSelection(productList[i], true)
            break
          }
        }
      })
    },
    handleSelectionChange(selection) {
      this.selectedList = selection
    },
    handleQuery() {
      this.form.pageNum = 1
      this.getList()
    },
    onDelete(index) {
      this.$refs.table.toggleRowSelection(this.list[index], false)
      this.list.splice(index, 1)
    },
    onChoose() {
      this.open = true
    },
    submitForm() {
      const { selectedList } = this
      const list = this.list.slice(0)
      const listIds = list.map(_ => _.id)
      for (let i = 0; i < selectedList.length; i++) {
        const { id } = selectedList[i]
        if (listIds.indexOf(id) < 0) {
          list.push(selectedList[i])
        }
      }
      this.$emit('input', list)
      this.open = false
    },
    cancel() {
      this.open = false
    }
  }
}
</script>
