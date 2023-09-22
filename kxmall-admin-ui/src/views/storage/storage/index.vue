<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入仓库名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择仓库状态" clearable>
          <el-option
            v-for="dict in dict.type.storage_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="operatingState">
        <el-select v-model="queryParams.operatingState" placeholder="请选择营业状态" clearable>
          <el-option
            v-for="dict in dict.type.operating_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="region">
        <region-selector v-model="queryParams.region" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
        <el-button
          v-hasPermi="['storage:storage:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
         <el-button
          v-hasPermi="['storage:storage:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:storage:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:storage:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['storage:storage:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row> -->

    <el-table v-loading="loading" :data="storageList">
      <!-- <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="true" label="主键" align="center" prop="id" /> -->
      <el-table-column label="序号" width="80" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="仓库名称" header-align="center" align="left" prop="name" />
      <el-table-column label="仓库状态" header-align="center" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.storage_status" :value="scope.row.state" />
        </template>
      </el-table-column>
      <el-table-column label="营业状态" header-align="center" align="center" prop="operatingState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.operating_state" :value="scope.row.operatingState" />
        </template>
      </el-table-column>
      <el-table-column label="负责人" header-align="center" align="center" prop="leaderName" />
      <el-table-column label="联系电话" header-align="center" align="center" prop="phone" />
      <el-table-column label="地址" header-align="center" align="left" prop="address" />
      <el-table-column label="营业时间" header-align="center" align="center">
        <template slot-scope="{row}">
          {{ row.businessStartTime+'~'+row.businessStopTime }}
        </template>
      </el-table-column>
      <el-table-column label="配送时间" header-align="center" align="center">
        <template slot-scope="{row}">
          {{ row.deliveryStartTime+'~'+row.deliveryStopTime }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="150" />
      <el-table-column label="修改时间" align="center" prop="updateTime" width="150" />
      <!-- <el-table-column label="省" align="center" prop="province" />
      <el-table-column label="市" align="center" prop="city" />
      <el-table-column label="区" align="center" prop="county" />
      <el-table-column label="详细地址" align="center" prop="address" />
      <el-table-column label="区域编码" align="center" prop="adcode" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="仓库管理名称" align="center" prop="leaderName" />
      <el-table-column label="营业起始时间" align="center" prop="businessStartTime" />
      <el-table-column label="配送起始时间" align="center" prop="deliveryStartTime" />
      <el-table-column label="营业结束时间" align="center" prop="businessStopTime" />
      <el-table-column label="配送结束时间" align="center" prop="deliveryStopTime" />
      <el-table-column label="配送范围" align="center" prop="deliveryRadius" />
      <el-table-column label="是否自动分配订单【0：非自动 1：自动】" align="center" prop="automatic" />
      <el-table-column label="状态 " align="center" prop="printSwitch" />
      <el-table-column label="Ukey" align="center" prop="printUkey" />
      <el-table-column label="SN" align="center" prop="printSn" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding" width="300"><!-- fixed-width -->
        <template slot-scope="{row}">
          <el-button
            v-hasPermi="['storage:storage:edit']"
            size="mini"
            type="primary"
            @click="handleView(row)"
          >详情</el-button>
          <el-button
            v-show="row.state===1"
            v-hasPermi="['storage:storage:edit']"
            size="mini"
            type="primary"
            @click="handleUpdate(row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['storage:storage:edit']"
            size="mini"
            :type="row.state===1?'danger':'success'"
            @click="updateState(row)"
          >{{ row.state===1?'禁用':'启用' }}</el-button>
          <el-button
            v-show="row.state===1"
            v-hasPermi="['storage:storage:edit']"
            size="mini"
            :type="row.operatingState===1?'warning':'success'"
            @click="updateBusinessState(row)"
          >{{ row.operatingState===1?'休息':'营业' }}</el-button>
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

    <!-- 添加或修改仓库管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" :width="dialogMode==='view'?'500px':'1200px'" append-to-body>
      <template v-if="dialogMode==='view'">
        <el-form :model="form" label-width="120px">
          <el-form-item label="前置仓名称">
            <span>{{ form.name }}</span>
          </el-form-item>
          <el-form-item label="前置仓状态">
            <span>{{ form.state===1?'正常':'禁用' }}</span>
          </el-form-item>
          <el-form-item label="营业状态">
            <span>{{ form.operatingState===1? '营业中':'休息中' }}</span>
          </el-form-item>
          <el-form-item label="营业时间">
            <span>{{ form.businessStartTime+'~'+form.businessStopTime }}</span>
          </el-form-item>
          <el-form-item label="配送时间">
            <span>{{ form.deliveryStartTime+'~'+form.deliveryStopTime }}</span>
          </el-form-item>
          <el-form-item label="负责人">
            <span>{{ form.leaderName }}</span>
          </el-form-item>
          <el-form-item label="联系电话">
            <span>{{ form.phone }}</span>
          </el-form-item>
          <el-form-item label="配送范围">
            <span>{{ form.deliveryRadius+'公里' }}</span>
          </el-form-item>
          <el-form-item label="地址">
            <span>{{ form.address }}</span>
          </el-form-item>
          <el-form-item label="经纬度">
            <span>{{ form.longitude+','+form.latitude }}</span>
          </el-form-item>
        </el-form>
      </template>
      <template v-else>
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="前置仓名称">
            <el-input v-model="form.name" placeholder="请输入仓库名称" clearable style="width:500px" />
          </el-form-item>
          <el-form-item label="前置仓状态">
            <el-radio-group v-model="form.state">
              <el-radio-button label="1">
                正常
              </el-radio-button>
              <el-radio-button label="0">
                禁用
              </el-radio-button>
            </el-radio-group>
            <b class="formInfo">订单自动分配配送员</b>
            <el-radio-group v-model="form.automatic">
              <el-radio-button label="1">
                是
              </el-radio-button>
              <el-radio-button label="0">
                否
              </el-radio-button>
            </el-radio-group>

          </el-form-item>
          <el-form-item label="营业状态">
            <el-radio-group v-model="form.operatingState">
              <el-radio-button label="1">
                营业中
              </el-radio-button>
              <el-radio-button label="0">
                休息中
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="营业时间">
            <el-time-select
              v-model="form.businessStartTime"
              :picker-options="{
                start: '00:00',
                step: '1:00',
                end: '23:00',
              }"
              placeholder="起始时间"
            />
            <el-time-select
              v-model="form.businessStopTime"
              :picker-options="{
                start: '00:00',
                step: '1:00',
                end: '23:00',
                minTime: form.businessStartTime
              }"
              placeholder="结束时间"
            />
          </el-form-item>
          <el-form-item label="配送时间">
            <el-time-select
              v-model="form.deliveryStartTime"
              :picker-options="{
                start: '00:00',
                step: '01:00',
                end: '23:00',
              }"
              placeholder="起始时间"
            />
            <el-time-select
              v-model="form.deliveryStopTime"
              :picker-options="{
                start: '00:00',
                step: '01:00',
                end: '23:00',
                minTime: form.deliveryStartTime
              }"
              placeholder="结束时间"
            />
          </el-form-item>
          <el-form-item label="负责人">
            <el-input v-model="form.leaderName" placeholder="请输入负责人姓名" clearable style="width:500px" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.phone" placeholder="请输入负责人手机号" clearable style="width:500px" />
          </el-form-item>
          <el-form-item label="仓库地址信息：" />
          <el-form-item label="配送范围">
            <el-input v-model="form.deliveryRadius" clearable style="width:500px">
              <template slot="append">公里</template>
            </el-input>
          </el-form-item>
          <el-form-item label="地址">
            <region-selector ref="regionSelector" v-model="form.region" style="display: inline-block;" />
            <el-input v-model="form.address" placeholder="详细地址" clearable style="width:450px" />
          </el-form-item>
          <el-form-item label="经纬度">
            <el-input v-model="form.litude" style="width:500px" clearable />
            <el-button type="primary" style="width:120px" @click="parse">解析</el-button>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
        <el-dialog v-model="modalMap" :visible.sync="modalMap" title="上传经纬度" append-to-body width="60%">
          <iframe
            id="mapPage"
            :src="keyUrl"
            width="100%"
            height="600px"
            frameborder="0"
          />
        </el-dialog>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listStorage, getStorage, addStorage, updateStorage, updateStateToNomral, updateStateToAbort, updateBusinessStateToRest, updateBusinessStateToOpen } from '@/api/storage/storage'
import { getProvinceAll } from '@/api/region/region'
import { querBasic } from '@/api/basic'
import RegionSelector from '@/components/RegionSelector'

export default {
  name: 'Storage',
  dicts: ['operating_state', 'storage_status'],
  components: {
    RegionSelector
  },
  data() {
    return {
      // address: '',
      province: [],
      city: [],
      county: [],
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      // ids: [],
      // // 非单个禁用
      // single: true,
      // // 非多个禁用
      // multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 仓库管理表格数据
      storageList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        state: undefined,
        operatingState: undefined,
        region: undefined
      },
      // 表单参数
      form: {},
      rules: {},
      dialogMode: 'view',
      mapKey: '',
      keyUrl: '',
      modalMap: false,
      flagClick: false
    }
  },
  async created() {
    this.getList()
    await querBasic({ key: 'mapKey' }).then(res => {
      this.mapKey = res.msg
      this.keyUrl = `https://apis.map.qq.com/tools/locpicker?type=1&key=${this.mapKey}&referer=myapp`
    })
  },
  mounted() {
    window.addEventListener('message', function(event) {
      // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
      var loc = event.data
      if (loc && loc.module === 'locationPicker') { // 防止其他应用也会向该页面post信息，需判断module是否为'locationPicker'
        window.parent.selectAdderss(loc)
      }
    }, false)
    window.selectAdderss = this.selectAdderss
  },
  methods: {
    /** 查询仓库管理列表 */
    getList() {
      this.loading = true
      const params = { ...this.queryParams, ...this.queryParams.region }
      delete params.region
      listStorage(params).then(response => {
        this.storageList = response.rows
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
        id: undefined,
        name: undefined,
        province: undefined,
        city: undefined,
        county: undefined,
        address: undefined,
        adcode: undefined,
        remark: undefined,
        state: undefined,
        longitude: undefined,
        latitude: undefined,
        phone: undefined,
        leaderName: undefined,
        operatingState: undefined,
        businessStartTime: undefined,
        deliveryStartTime: undefined,
        businessStopTime: undefined,
        deliveryStopTime: undefined,
        deliveryRadius: undefined,
        automatic: undefined,
        printSwitch: undefined,
        printAcount: undefined,
        printUkey: undefined,
        printSn: undefined,
        createBy: undefined,
        updateBy: undefined,
        createTime: undefined,
        updateTime: undefined,
        region: undefined,
        litude: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm('queryForm')
    //   this.handleQuery()
    // },
    // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.ids = selection.map(item => item.id)
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    // },
    /** 新增按钮操作 */
    async handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加仓库'
      this.dialogMode = 'add'
      this.province = await getProvinceAll()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true
      this.reset()
      const id = row.id
      this.dialogMode = 'update'
      getStorage(id).then(response => {
        this.loading = false
        this.form = response.data
        const { province, city, county } = this.form
        this.form.region = { province, city, county }
        this.form.litude = this.form.longitude + ',' + this.form.latitude
        this.open = true
        this.title = '编辑仓库'
      })
    },
    handleView(row) {
      this.loading = true
      this.reset()
      const id = row.id
      this.dialogMode = 'view'
      getStorage(id).then(response => {
        this.loading = false
        this.form = response.data
        this.open = true
        this.title = '仓库详情'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const params = { ...this.form, ...this.form.region }
          delete params.region
          this.buttonLoading = true
          if (this.form.id != null) {
            updateStorage(params).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addStorage(params).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        }
      })
    },
    // /** 删除按钮操作 */
    // handleDelete(row) {
    //   const ids = row.id || this.ids
    //   this.$modal.confirm('是否确认删除仓库管理编号为"' + ids + '"的数据项？').then(() => {
    //     this.loading = true
    //     return delStorage(ids)
    //   }).then(() => {
    //     this.loading = false
    //     this.getList()
    //     this.$modal.msgSuccess('删除成功')
    //   }).catch(() => {
    //   }).finally(() => {
    //     this.loading = false
    //   })
    // },
    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('storage/storage/export', {
    //     ...this.queryParams
    //   }, `storage_${new Date().getTime()}.xlsx`)
    // },
    selectAdderss(data) {
      if (this.flagClick) {
        this.form.litude = data.latlng.lng + ',' + data.latlng.lat
        this.modalMap = false
        this.flagClick = false
        this.form.latitude = data.latlng.lat
        this.form.longitude = data.latlng.lng
      }
    },
    onSearch(address) {
      this.flagClick = true
      if (!(address && address.length !== 0)) {
        this.$message.warning('请选择地址')
        return
      }
      if (!(this.form.address && this.form.address !== '')) {
        this.$message.warning('请输入详细地址')
        return
      }
      if (!this.mapKey) {
        this.$notify.error({
          title: '失败',
          message: '获取地址的key为空，无法获取经纬度！'
        })
        return
      }

      this.$jsonp('https://apis.map.qq.com/ws/geocoder/v1', {
        key: this.mapKey,
        address: address,
        output: 'jsonp'
      }).then(res => {
        if (res.status !== 0) {
          this.$message.warning(res.message)
          return
        }
        const addressStr = res.result.location.lng + ',' + res.result.location.lat
        this.keyUrl += `&coord= ` + addressStr
        this.modalMap = true
        this.form.latitude = res.result.location.lat
        this.form.longitude = res.result.location.lng
      })
    },
    parse() {
      if (!this.form.region.province) {
        this.$message.warning('请选择地址')
        return
      }
      if (!this.form.region.city) {
        this.$message.warning('请选择地址')
        return
      }
      if (!this.form.region.county) {
        this.$message.warning('请选择地址')
        return
      }
      const province = this.$refs.regionSelector.province.filter((item) => item.value === this.form.region.province)[0].label
      const city = this.$refs.regionSelector.city.filter((item) => item.value === this.form.region.city)[0].label
      const county = this.$refs.regionSelector.county.filter((item) => item.value === this.form.region.county)[0].label
      const add = this.form.address
      const address = province + city + county + add
      this.onSearch(address)
    },
    updateState(row) {
      const { state, id } = row
      this.$confirm(state === 0 ? '是否启用此仓库' : '是否禁用此仓库', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const api = state === 0 ? updateStateToNomral : updateStateToAbort
        api({ id }).then(res => {
          this.getList()
        })
      }).catch((e) => {

      })
    },
    updateBusinessState(row) {
      const { operatingState, id } = row
      this.$confirm(operatingState === 0 ? '是否开启此仓库的营业' : '是否关闭此仓库的营业', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const api = operatingState === 0 ? updateBusinessStateToOpen : updateBusinessStateToRest
        api({ id }).then(res => {
          this.getList()
        })
      }).catch((e) => {

      })
    }
  }
}
</script>
<style>
.main {
  padding: 40px;
}
.formInfo{
  padding-left:30px;
  margin-left: 30px;
  padding-right:10px;
  font-size: 14px;
  color: #606266;
}
</style>
