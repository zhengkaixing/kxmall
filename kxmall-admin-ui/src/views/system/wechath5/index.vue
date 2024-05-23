<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
      <el-form-item label="微信公众号AppID">
        <el-input v-model="form.h5_appid" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="微信公众号密钥">
        <el-input v-model="form.h5_appSecret" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="微信公众号token">
        <el-input v-model="form.h5_token" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="EncodingAESKey">
        <el-input v-model="form.h5_aesKey" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="回调地址示例">
        <el-input v-model="form.h5_callback" style="width: 370px;" :disabled="true"/>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="doSubmit">提交</el-button>
      </el-form-item>

    </el-form>

  </div>
</template>

<script>

import {saveConfig , getCategoryConfig} from '@/api/system/config'
import fileUpload from '@/components/FileUpload'
export default {
  components: { fileUpload },
  data() {
    return {
      delLoading: false,
      form: {
        category: 'wxH5',
        h5_appid: '',
        h5_appSecret: '',
        h5_token: '',
        h5_aesKey: '',
        h5_callback: 'http://你的H5端域名/上下文/wechat/serve'
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
