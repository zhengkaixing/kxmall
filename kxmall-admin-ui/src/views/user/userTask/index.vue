<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <el-form class="query-form" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
        <el-form-item label="任务名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入任务名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="等级ID" prop="levelId">
          <el-input
            v-model="queryParams.levelId"
            placeholder="请输入等级ID"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item class="query-form__actions" label-width="0">
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!--表格渲染-->
    <el-table v-loading="loading" :data="userTaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="等级名称" align="center">
        <template #default="scope">
          <el-tag :color="getLevelColor(scope.row.levelId)">
            {{ scope.row.levalName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="name" />
      <el-table-column label="任务类型" align="center" prop="taskType" />
      <el-table-column label="限定数" align="center" prop="number" />
      <el-table-column label="排序" align="center" prop="sort" />
      <el-table-column label="是否显示" align="center" prop="isShow">
        <template #default="scope">
          <el-tag :type="scope.row.isShow === 1 ? 'success' : 'info'">
            {{ scope.row.isShow === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            size="small"
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['user:userTask:edit']"
          >修改</el-button>
          <el-button
            size="small"
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['user:userTask:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页组件-->
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户任务对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="等级名称" prop="levalName">
          <el-input v-model="form.levalName" placeholder="请输入等级名称" disabled />
        </el-form-item>
        <el-form-item label="任务名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="form.taskType" placeholder="请选择任务类型" style="width: 100%">
            <el-option label="积分任务" value="SatisfactionIntegral" />
            <el-option label="消费金额" value="ConsumptionAmount" />
            <el-option label="累计签到" value="CumulativeAttendance" />
          </el-select>
        </el-form-item>
        <el-form-item label="限定数" prop="number">
          <el-input-number v-model="form.number" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否显示" prop="isShow">
          <el-radio-group v-model="form.isShow">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="任务说明" prop="illustrate">
          <el-input v-model="form.illustrate" type="textarea" placeholder="请输入任务说明" />
        </el-form-item>
      </el-form>
      <template #footer><div class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div></template>
    </el-dialog>
  </div>
</template>

<script>
import { listUserTask, getUserTask, delUserTask, addUserTask, updateUserTask } from "@/api/user/userTask";

export default {
  name: "UserTask",
  data() {
    return {
      buttonLoading: false,
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      userTaskList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        levelId: undefined,
        taskType: undefined,
        isShow: undefined
      },
      form: {
        id: undefined,
        name: '',
        realName: '',
        taskType: '',
        number: 0,
        levelId: 0,
        sort: 0,
        isShow: 1,
        isMust: 0,
        illustrate: '',
        levalName: ''
      },
      rules: {
        name: [
          { required: true, message: "任务名称不能为空", trigger: "blur" }
        ],
        taskType: [
          { required: true, message: "任务类型不能为空", trigger: "change" }
        ],
        number: [
          { required: true, message: "限定数不能为空", trigger: "blur" }
        ],
        levelId: [
          { required: true, message: "等级ID不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getLevelColor(levelId) {
      const colors = [
        '#FFA39E',
        '#FFD591',
        '#FFF7AD',
        '#D3F261',
        '#91D5FF',
        '#B37FEB',
        '#FF85C0',
        '#5CDBD3',
      ];
      return colors[levelId % colors.length];
    },
    getList() {
      this.loading = true;
      listUserTask(this.queryParams).then(response => {
        this.userTaskList = response.rows;
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
        name: '',
        realName: '',
        taskType: '',
        number: 0,
        levelId: 0,
        sort: 0,
        isShow: 1,
        isMust: 0,
        illustrate: '',
        levalName: ''
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
      this.title = "添加用户任务";
    },
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getUserTask(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改用户任务";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateUserTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserTask(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户任务编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delUserTask(ids);
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
      this.download('user/userTask/export', {
        ...this.queryParams
      }, `userTask_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped>
</style>
