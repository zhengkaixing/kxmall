<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户uid" prop="uid">
        <el-input
          v-model="queryParams.uid"
          placeholder="请输入用户uid"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['user:userSign:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userSignList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="true"/>
      <el-table-column label="用户" align="center" prop="nickname" />
      <el-table-column label="签到说明" align="center" prop="title" />
      <el-table-column label="获得积分" align="center" prop="number" />
      <el-table-column label="剩余积分" align="center" prop="balance" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改签到记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户uid" prop="uid">
          <el-input v-model="form.uid" placeholder="请输入用户uid" />
        </el-form-item>
        <el-form-item label="签到说明" prop="title">
          <el-input v-model="form.title" placeholder="请输入签到说明" />
        </el-form-item>
        <el-form-item label="获得积分" prop="number">
          <el-input v-model="form.number" placeholder="请输入获得积分" />
        </el-form-item>
        <el-form-item label="剩余积分" prop="balance">
          <el-input v-model="form.balance" placeholder="请输入剩余积分" />
        </el-form-item>
        <el-form-item label="" prop="isDel">
          <el-input v-model="form.isDel" placeholder="请输入" />
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
import { listUserSign, getUserSign, delUserSign, addUserSign, updateUserSign } from "@/api/user/userSign";

export default {
  name: "UserSign",
  data() {
    return {
      buttonLoading: false,
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      userSignList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uid: undefined,
        title: undefined,
        number: undefined,
        balance: undefined,
        isDel: undefined,
      },
      form: {},
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        uid: [
          { required: true, message: "用户uid不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "签到说明不能为空", trigger: "blur" }
        ],
        number: [
          { required: true, message: "获得积分不能为空", trigger: "blur" }
        ],
        balance: [
          { required: true, message: "剩余积分不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "添加时间不能为空", trigger: "blur" }
        ],
        isDel: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listUserSign(this.queryParams).then(response => {
        this.userSignList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: undefined,
        uid: undefined,
        title: undefined,
        number: undefined,
        balance: undefined,
        createTime: undefined,
        updateTime: undefined,
        isDel: undefined,
        createBy: undefined,
        updateBy: undefined
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加签到记录";
    },
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getUserSign(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改签到记录";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateUserSign(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserSign(this.form).then(response => {
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
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除签到记录编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delUserSign(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    handleExport() {
      this.download('user/userSign/export', {
        ...this.queryParams
      }, `userSign_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
