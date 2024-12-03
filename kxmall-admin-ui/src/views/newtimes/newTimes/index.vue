<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="storageId">
        <el-select v-model="queryParams.storageId" placeholder="请选择前置仓" clearable>
          <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
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
          v-hasPermi="['newtimes:newTimes:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="newTimesList" @selection-change="handleSelectionChange">
      <el-table-column label="仓库名称" align="center" prop="storageName">
        <template slot-scope="scope">
          {{ storageMap[scope.row.storageId] }}
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column label="编辑人" align="center" prop="createBy" />
      <el-table-column label="更新人" align="center" prop="updateBy" />
      <el-table-column label="时报状态" align="center" prop="isStop">
        <template slot-scope="{row}">
          <div>
            <el-tag v-if="row.isStop === 1">开启</el-tag>
            <el-tag v-else type="info">关闭</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="编辑时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            style="color: #409EFF;"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['newtimes:newTimes:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            style="color: #F56C6C;"
            @click="handleDelete(scope.row)"
            v-hasPermi="['newtimes:newTimes:remove']"
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

    <!-- 添加或修改新鲜时报对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="仓库" prop="storageId">
          <el-select v-model="form.storageId" placeholder="请选择前置仓" clearable>
            <el-option v-for="item in storages" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="时报状态" prop="isStop">
          <el-radio v-model="form.isStop" :label="1">开启</el-radio>
          <el-radio v-model="form.isStop" :label="0">关闭</el-radio>
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
import { listNewTimes, getNewTimes, delNewTimes, addNewTimes, updateNewTimes } from "@/api/newtimes/newTimes";
import {listAllStorage} from "@/api/storage/storage";

export default {
  name: "NewTimes",
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
      // 新鲜时报表格数据
      newTimesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        storageId: undefined,
        content: undefined,
        isStop: undefined,
      },
      // 表单参数
      form: {},
      // 仓库map
      storageMap: {},
      storages: [],
      // 表单校验
      rules: {
        storageId: [
          { required: true, message: "仓库不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ],
        isStop: [
          { required: true, message: "时报状态0，没暂停；1，暂停不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.listAllStorage()
    this.getList();
  },
  methods: {
    listAllStorage() {
      listAllStorage().then(({ data }) => {
        this.storages = data
        this.storages.forEach(storage => {
          this.storageMap[storage.id] = storage.name
        })
      })
    },
    /** 查询新鲜时报列表 */
    getList() {
      this.loading = true;
      listNewTimes(this.queryParams).then(response => {
        this.newTimesList = response.rows;
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
        storageId: undefined,
        content: undefined,
        createBy: undefined,
        updateBy: undefined,
        isStop: 1,
        updateTime: undefined,
        createTime: undefined
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
      this.title = "添加新鲜时报";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getNewTimes(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改新鲜时报";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateNewTimes(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addNewTimes(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除新鲜时报编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delNewTimes(ids);
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
      this.download('newtimes/newTimes/export', {
        ...this.queryParams
      }, `newTimes_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
