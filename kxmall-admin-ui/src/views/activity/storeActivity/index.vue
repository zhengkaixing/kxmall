<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="活动标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入活动标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动海报" prop="imgUrl">
        <el-input
          v-model="queryParams.imgUrl"
          placeholder="请输入活动海报"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="海报链接" prop="outUrl">
        <el-input
          v-model="queryParams.outUrl"
          placeholder="请输入海报链接"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动详情" prop="description">
        <el-input
          v-model="queryParams.description"
          placeholder="请输入活动详情"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['activity:storeActivity:add']"
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
          v-hasPermi="['activity:storeActivity:edit']"
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
          v-hasPermi="['activity:storeActivity:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['activity:storeActivity:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storeActivityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="活动id" align="center" prop="id" v-if="true"/>
      <el-table-column label="活动标题" align="center" prop="title" />
      <el-table-column label="活动海报" align="center" prop="imgUrl">
        <template slot-scope="{ row }">
          <div v-if="row.imgUrl && row.imgUrl.length > 0">
            <el-image :src="JSON.parse(row.imgUrl)[0].url" />
          </div>
          <div v-else>
            <!-- 你可以选择显示一个默认图片或留空 -->
            <span>无海报</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="海报链接" align="center" prop="outUrl" />
      <el-table-column label="活动详情" align="center" prop="description" />
      <el-table-column label="状态(是否开启)" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['activity:storeActivity:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['activity:storeActivity:remove']"
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
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题" />
        </el-form-item>
        <el-form-item label="活动海报" prop="imgUrl">
          <image-upload v-model="form.imgUrl" :limit="1" value-type="json" />
        </el-form-item>
        <el-form-item label="海报链接" prop="outUrl">
          <el-input v-model="form.outUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="活动详情" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
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
import { listStoreActivity, getStoreActivity, delStoreActivity, addStoreActivity, updateStoreActivity } from "@/api/activity/storeActivity";

export default {
  name: "StoreActivity",
  data() {
    return {
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
      storeActivityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        imgUrl: undefined,
        outUrl: undefined,
        description: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // id: [
        //   { required: true, message: "活动id不能为空", trigger: "blur" }
        // ],
        title: [
          { required: true, message: "活动标题不能为空", trigger: "blur" }
        ],
        // imgUrl: [
        //   { required: true, message: "活动海报不能为空", trigger: "blur" }
        // ],
        // outUrl: [
        //   { required: true, message: "海报链接不能为空", trigger: "blur" }
        // ],
        // description: [
        //   { required: true, message: "活动详情不能为空", trigger: "blur" }
        // ],
        status: [
          { required: true, message: "状态(是否开启)不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询活动商品列表 */
    getList() {
      this.loading = true;
      listStoreActivity(this.queryParams).then(response => {
        this.storeActivityList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        title: undefined,
        imgUrl: undefined,
        outUrl: undefined,
        description: undefined,
        status: undefined,
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
      getStoreActivity(id).then(response => {
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
            updateStoreActivity(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addStoreActivity(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除活动商品编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delStoreActivity(ids);
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
      this.download('activity/storeActivity/export', {
        ...this.queryParams
      }, `storeActivity_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
