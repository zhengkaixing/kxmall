<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品id" prop="productId">
        <el-select v-model="queryParams.activityId" clearable placeholder="请选择推荐类型">
          <el-option
            v-for="dict in activityTypeList"
            :key="dict.id"
            :label="dict.title"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>

<!--      <el-form-item label="商品id" prop="productId">-->
<!--        <el-input-->
<!--          v-model="queryParams.productId"-->
<!--          placeholder="请输入商品id"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="活动id" prop="activityId">-->
<!--        <el-input-->
<!--          v-model="queryParams.activityId"-->
<!--          placeholder="请输入活动id"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['activity:storeActivityProduct:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['activity:storeActivityProduct:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['activity:storeActivityProduct:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['activity:storeActivityProduct:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storeActivityProductList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" v-if="true"/>
<!--      <el-table-column label="商品id" align="center" prop="productId" />-->
      <el-table-column label="活动商品" align="center" prop="productName" />
<!--      <el-table-column label="活动id" align="center" prop="activityId" />-->
      <el-table-column label="所属活动" align="center" prop="activityTitle" />
      <el-table-column label="市场价" align="center" prop="originalPrice" />
      <el-table-column label="售价" align="center" prop="price" />
<!--      <el-table-column label="活动价" align="center" prop="activityPrice" />-->
      <el-table-column label="活动价" align="center">
        <template slot-scope="{row}">
          <el-input v-if="editMode[row.id]" v-model="row.activityPrice" clearable @blur="updatePrice(row)" />
          <el-button v-else type="text" @click="editMode[row.id]=true">
            {{ row.activityPrice }}
          </el-button>
        </template>
      </el-table-column>
      <!--      <el-table-column label="所属活动" align="center" prop="activityTitle">-->
<!--        <template slot-scope="scope">-->
<!--          {{ activityTypeMap[scope.row.id] }}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['activity:storeActivityProduct:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['activity:storeActivityProduct:remove']"
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

    <!-- 添加或修改活动商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="商品id" prop="productId">-->
<!--          <el-input v-model="form.productId" placeholder="请输入商品id" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="活动id" prop="activityId">-->
<!--          <el-input v-model="form.activityId" placeholder="请输入活动id" />-->
<!--        </el-form-item>-->
        <el-form-item prop="activityId" label="所属活动">
          <el-select v-model="form.activityId" clearable placeholder="请选择活动">
            <el-option
              v-for="dict in activityTypeList"
              :key="dict.id"
              :label="dict.title"
              :value="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="productIds" label="活动商品">
          <el-cascader
            v-model="form.productIds"
            :options="productsTree"
            :props="props"
            collapse-tags
            clearable
            filterable
            placeholder="请选择活动商品"
          />
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
import {
  listStoreActivityProduct,
  getStoreActivityProduct,
  delStoreActivityProduct,
  // addStoreActivityProduct,
  updateStoreActivityProduct,
  listActivityType,
  addProductBatch,
  updatePrice
} from '@/api/activity/storeActivityProduct'
import { getProductBigTree } from '@/api/product/storeProduct'

export default {
  name: "StoreActivityProduct",
  data() {
    return {
      props: {
        multiple: true,
        value: 'id'
      },
      productsTree: [],
      activityTypeList: [],

      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 活动商品表格数据
      storeActivityProductList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productId: undefined,
        activityId: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // id: [
        //   { required: true, message: "不能为空", trigger: "blur" }
        // ],
        productIds: [
          { required: true, message: "活动商品不能为空", trigger: "blur" }
        ],
        activityIds: [
          { required: true, message: "活动不能为空", trigger: "blur" }
        ],
      },
      editMode: {}

    };
  },
  created() {
    this.getList()

    this.getActivityTitleList()
    this.getProductBigTree()
  },
  methods: {
    getProductBigTree() {
      getProductBigTree().then(({ data }) => {
        this.productsTree = data
      })
    },
    /** 查询推荐类型列表 */
    getActivityTitleList() {
      let that = this
      that.loading = true
      listActivityType().then(response => {
        that.activityTypeList = response.data

        // that.activityTypeList.forEach(item => {
        //   that.activityTypeMap[item.id] = item.title
        //   // debugger
        // })
        that.loading = false
      })
    },
    getProductIds(ids) {
      const arr = []
      for (let i = 0; i < ids.length; i++) {
        const els = ids[i]
        arr.push(els[els.length - 1])
      }
      console.log(arr)
      return arr
    },
    updatePrice(row) {
      const { id, activityPrice } = row
      updatePrice({ id, activityPrice }).then(() => {
        this.editMode[id] = false
      })
    },



    /** 查询活动商品列表 */
    getList() {
      this.editMode = {}

      this.loading = true;
      listStoreActivityProduct(this.queryParams).then(response => {
        this.storeActivityProductList = response.rows;
        this.total = response.total;
        this.loading = false;

        response.rows.forEach(({ id }) => {
          this.$set(this.editMode, id, false)
        })
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        productId: undefined,
        activityId: undefined,
        activityPrice: undefined,
        createBy: undefined,
        updateBy: undefined,
        createTime: undefined,
        updateTime: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加活动商品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getStoreActivityProduct(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改活动商品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateStoreActivityProduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            // addStoreActivityProduct(this.form).then(response => {
            //   this.$modal.msgSuccess("新增成功");
            //   this.open = false;
            //   this.getList();
            // }).finally(() => {
            //   this.buttonLoading = false;
            // });
            addProductBatch({ ...this.form, productIds: this.getProductIds(this.form.productIds) }).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除活动商品编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delStoreActivityProduct(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('activity/storeActivityProduct/export', {
        ...this.queryParams
      }, `storeActivityProduct_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
