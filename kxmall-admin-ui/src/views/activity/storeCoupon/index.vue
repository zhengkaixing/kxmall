<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['coupon:storeCoupon:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="storeCouponList">
      <el-table-column type="index" label="序号" align="center" width="80">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="title" label="优惠券名称" header-align="center" />
      <el-table-column prop="type" label="优惠券类型" align="center">
        <template slot-scope="{row}">
          <div>
            <el-tag v-if="row.type === 1">商品券</el-tag>
            <el-tag v-else type="info">普通券</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="couponPrice" label="优惠券面值" header-align="center" align="right" />
      <el-table-column prop="useMinPrice" label="优惠券最低消费" header-align="center" align="right" />
      <el-table-column label="优惠券有效期限" align="center">
        <template slot-scope="{row}">
          <span>{{ row.couponTime }}天</span>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="100" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template slot-scope="{row}">
          <div>
            <el-tag v-if="row.status === 1">开启</el-tag>
            <el-tag v-else type="info">关闭</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="{row}">
          <el-button
            v-hasPermi="['coupon:storeCoupon:edit']"
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleUpdate(row,'issue')"
          >发布</el-button>
          <el-button
            v-hasPermi="['coupon:storeCoupon:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(row,'update')"
          >修改</el-button>
          <el-button
            v-hasPermi="['coupon:storeCoupon:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改优惠券对话框 -->
    <el-dialog :title="title" :visible.sync="open" :width="width" append-to-body>
      <el-form v-if="dialogMode.current==='issue'" ref="form" :model="form" size="small" label-width="100px">
        <el-form-item label="优惠券ID" prop="id">
          <el-input v-model="form.id" :disabled="true" />
          <el-input v-model="form.type" type="hidden" />
        </el-form-item>
        <el-form-item label="优惠券名称" prop="title">
          <el-input v-model="form.title" :disabled="true" />
        </el-form-item>
        <el-form-item label="领取开启时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择日期时间"
          />
        </el-form-item>
        <el-form-item label="券领结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择日期时间"
          />
        </el-form-item>
        <el-form-item label="发布数量" prop="totalCount">
          <el-input v-model="form.totalCount" />
        </el-form-item>
        <el-form-item label="是否不限量" prop="isPermanent">
          <el-radio-group v-model="form.isPermanent">
            <el-radio :label="1">不限量</el-radio>
            <el-radio :label="0">限量</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">开启</el-radio>
            <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-form v-else ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="优惠券类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :label="0">通用券</el-radio>
            <el-radio :label="1">商品券</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.type == 1" label="选择商品" prop="product">
          <product-selector v-model="form.product" />
        </el-form-item>
        <el-form-item label="优惠券名称" prop="title" style="width:349px">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="优惠券面值" prop="couponPrice">
          <el-input-number v-model="form.couponPrice" />
        </el-form-item>
        <el-form-item label="优惠券最低消费" prop="useMinPrice">
          <el-input-number v-model="form.useMinPrice" />
        </el-form-item>
        <el-form-item label="优惠券有效期限(天)" prop="couponTime">
          <el-input-number v-model="form.couponTime" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="form.status" :label="1">开启</el-radio>
          <el-radio v-model="form.status" :label="0">关闭</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStoreCoupon, getStoreCoupon, delStoreCoupon, addStoreCoupon, updateStoreCoupon } from '@/api/activity/storeCoupon'
import ProductSelector from '@/views/components/ProductSelector.vue'

export default {
  name: 'StoreCoupon',
  components: {
    ProductSelector
  },
  data() {
    const validateProduct = (rule, value, callback) => {
      if (this.form.type === 1 && value.length === 0) {
        callback(new Error('请选择商品'))
      } else {
        callback()
      }
    }
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 优惠券表格数据
      storeCouponList: [],
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: '优惠券名称不能为空', trigger: 'blur' }
        ],
        product: [
          { validator: validateProduct, trigger: 'blur' }
        ]
      },
      dialogMode: {
        list: {
          add: {
            title: '添加优惠券',
            width: '520px'
          },
          update: {
            title: '修改优惠券',
            width: '520px'
          },
          issue: {
            title: '发布优惠券',
            width: '500px'
          }
        },
        current: 'add'
      }
    }
  },
  computed: {
    title() {
      const { list, current } = this.dialogMode
      return list[current].title
    },
    width() {
      const { list, current } = this.dialogMode
      return list[current].width
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询优惠券列表 */
    getList() {
      this.loading = true
      listStoreCoupon(this.queryParams).then(response => {
        this.storeCouponList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        type: 0,
        title: undefined,
        productId: undefined,
        product: [],
        couponPrice: 0,
        useMinPrice: 0,
        couponTime: 1,
        sort: 0,
        status: 1
      }
      if (this.dialogMode.current === 'issue') {
        this.form = {
          id: '',
          type: '',
          title: '',
          startTime: '',
          endTime: '',
          totalCount: 0,
          isPermanent: 1,
          status: 1
        }
      }
      this.resetForm('form')
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.dialogMode.current = 'add'
      this.reset()
      this.open = true
    },
    /** 修改按钮操作 */
    handleUpdate(row, mode) {
      this.loading = true
      this.dialogMode.current = mode
      this.reset()
      const id = row.id || this.ids
      getStoreCoupon(id).then(({ data }) => {
        this.loading = false
        if (mode === 'update') {
          this.form = data
        } else {
          for (const key in this.form) {
            if (key in data) {
              this.form[key] = data[key]
            }
          }
        }
        this.open = true
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const { dialogMode, form } = this
          const { current } = dialogMode
          form.productId = form.product.map(_ => _.id).toString()
          this.buttonLoading = true
          if (form.id != null) {
            updateStoreCoupon(form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else if (current === 'add') {
            addStoreCoupon(form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {}
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true
        return delStoreCoupon(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>
