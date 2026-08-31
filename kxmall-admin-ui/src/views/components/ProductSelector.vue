<template>
  <div>
    <div class="list">
      <ul v-for="(item,index) in list" :key="item.id" class="el-upload-list el-upload-list--picture-card">
        <li tabindex="0" class="el-upload-list__item is-ready">
          <div>
            <img :src="getStringOSSURL(item.image)" class="el-upload-list__item-thumbnail">
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="onDelete(index)">
                <el-icon  ><Delete /></el-icon>
              </span>
            </span>
          </div>
        </li>
      </ul>
      <div tabindex="0" class="el-upload el-upload--picture-card" @click="onChoose">
        <el-icon  ><Plus /></el-icon>
      </div>
    </div>

    <el-dialog title="商品列表" v-model="open" append-to-body>
      <el-form ref="queryForm" :model="form" size="small" :inline="true" label-width="68px">
        <el-form-item label="" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" size="small" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="table" v-loading="loading" :data="productList" row-key="id" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" align="center">
          <template #default="{$index}">
            {{ (form.pageNum - 1) * form.pageSize + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="商品图片" align="center">
          <template #default="{row}">
            <el-link :href="getStringOSSURL(row.image)" target="_blank" :underline="false">
              <el-image :src="getStringOSSURL(row.image)" title="点击打开" class="el-avatar" />
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
        v-model:page="form.pageNum"
        v-model:limit="form.pageSize"
        @pagination="getList"
      />
      <template #footer><div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div></template>
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
    modelValue: {
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
    modelValue: {
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
      this.$emit('update:modelValue', list)
      this.open = false
    },
    cancel() {
      this.open = false
    }
  }
}
</script>
