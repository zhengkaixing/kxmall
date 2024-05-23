<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
      <el-form-item label="微信小程序AppID">
        <el-input v-model="form.mini_appid" style="width: 370px;" />
        <p style="color: red">微信开放平台审核通过的应用APPID（请登录open.weixin.qq.com查看，注意与公众号的APPID不同）</p>
      </el-form-item>
      <el-form-item label="小程序密钥">
        <el-input v-model="form.mini_appSecret" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="商户id">
        <el-input v-model="form.wxpay_mchId" style="width: 370px;" type="password" />
      </el-form-item>
      <el-form-item label="商户密钥">
        <el-input v-model="form.wxpay_mchKey" style="width: 370px;" type="password" />
      </el-form-item>
      <el-form-item label="微信回调url">
        <el-input v-model="form.wxpay_notify_url" style="width: 370px;" />
      </el-form-item>
      <el-form-item label="微信证书">
        <file-upload v-model="form.wxpay_keyPath" style="width: 500px;" />
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
        category: 'wxMini',
        mini_appid: '',
        mini_appSecret: '',
        wxpay_mchId: '',
        wxpay_mchKey: '',
        wxpay_keyPath: '',
        wxpay_notify_url: ''
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
