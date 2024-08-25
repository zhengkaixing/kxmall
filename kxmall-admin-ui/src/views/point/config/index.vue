<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
      <el-form-item label="积分抵用比例">
        <el-input v-model="form.integral_ratio" style="width: 370px;" />
        <p style="color: red">积分抵用比例(1积分抵多少金额(元))</p>
      </el-form-item>
      <el-form-item label="满多少可以抵扣">
        <el-input v-model="form.integral_full" style="width: 370px;" />
        <p style="color: red">消费必须满一定额度才可使用,0代表无限制</p>
      </el-form-item>
      <el-form-item label="单次最大抵扣积分">
        <el-input v-model="form.integral_max" style="width: 370px;" />
        <p style="color: red">限制一次只能使用多少积分,0代表无限制</p>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="doSubmit">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {saveConfig , getCategoryConfig} from '@/api/system/config'
export default {
  data() {
    return {
      delLoading: false,
      form: {
        category: 'point',
        integral_ratio: '',
        integral_full: 0,
        integral_max: 0
      },
      rules: {
      }
    }
  },
  created() {
    getCategoryConfig(this.form.category).then(rese => {
      const that = this
      rese.data.map(function (key, value) {
        const keyName = key.configKey
        const newValue = key.configValue
        if (keyName in that.form) {
          that.form[keyName] = newValue
        }
      })
    })
  },
  methods: {
    doSubmit() {
      saveConfig(this.form).then(res => {
        this.$notify.success({
          title: '成功',
          message: '设置成功'
        })
      }).catch(err => {
        console.log(err.msg)
      })
    }
  }
}
</script>

<style scoped>

</style>
