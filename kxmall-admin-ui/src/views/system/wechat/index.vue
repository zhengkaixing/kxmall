<template>
  <div class="wechat-page" v-loading="pageLoading">
    <div class="wechat-hero">
      <div class="wechat-hero__icon">
        <el-icon :size="28"><ChatDotRound /></el-icon>
      </div>
      <div class="wechat-hero__text">
        <h2>微信小程序配置</h2>
        <p>配置小程序身份与微信支付参数，保存后立即生效</p>
      </div>
    </div>

    <el-form
      ref="form"
      class="wechat-form"
      :model="form"
      :rules="rules"
      label-width="120px"
      label-position="top"
    >
      <el-card class="wechat-card" shadow="never">
        <template #header>
          <div class="card-head">
            <div class="card-head__title">小程序基础信息</div>
            <el-tag type="success" effect="plain" round>wxMini</el-tag>
          </div>
        </template>

        <el-row :gutter="24">
          <el-col :xs="24" :md="12">
            <el-form-item label="小程序 AppID" prop="mini_appid">
              <el-input
                v-model="form.mini_appid"
                placeholder="请输入微信小程序 AppID"
                clearable
                maxlength="64"
              >
                <template #prefix>
                  <el-icon><Key /></el-icon>
                </template>
              </el-input>
              <div class="field-tip">
                请到
                <el-link type="primary" href="https://open.weixin.qq.com" target="_blank">open.weixin.qq.com</el-link>
                查看审核通过的应用 AppID，注意与公众号 AppID 不同
              </div>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="小程序密钥" prop="mini_appSecret">
              <el-input
                v-model="form.mini_appSecret"
                placeholder="请输入小程序 AppSecret"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
              <div class="field-tip">用于调用微信开放接口，请妥善保管，不要泄露</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="wechat-card" shadow="never">
        <template #header>
          <div class="card-head">
            <div class="card-head__title">微信支付</div>
            <el-tag type="warning" effect="plain" round>wxPay</el-tag>
          </div>
        </template>

        <el-row :gutter="24">
          <el-col :xs="24" :md="12">
            <el-form-item label="商户号" prop="wxpay_mchId">
              <el-input
                v-model="form.wxpay_mchId"
                placeholder="请输入微信支付商户号"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Wallet /></el-icon>
                </template>
              </el-input>
              <div class="field-tip">微信支付商户平台中的商户 ID（mchId）</div>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="商户密钥" prop="wxpay_mchKey">
              <el-input
                v-model="form.wxpay_mchKey"
                placeholder="请输入商户 API 密钥"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
              <div class="field-tip">商户平台「API 安全」中设置的 API 密钥</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="支付回调地址" prop="wxpay_notify_url">
              <el-input
                v-model="form.wxpay_notify_url"
                placeholder="例如 https://your-domain.com/callback/wxPay"
                clearable
              >
                <template #prefix>
                  <el-icon><Link /></el-icon>
                </template>
              </el-input>
              <div class="field-tip">必须为公网可访问的 HTTPS 地址，用于接收微信支付结果通知</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="商户证书" prop="wxpay_keyPath">
              <file-upload
                v-model="form.wxpay_keyPath"
                :limit="1"
                :file-size="2"
                :file-type="['p12', 'pem']"
              />
              <div class="field-tip">退款等接口需要证书，请上传商户平台下载的 apiclient_cert.p12</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <div class="wechat-actions">
        <el-button @click="loadConfig">重置</el-button>
        <el-button type="primary" :loading="submitLoading" @click="doSubmit">保存配置</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { saveConfig, getCategoryConfig } from '@/api/system/config'
import fileUpload from '@/components/FileUpload'

export default {
  components: { fileUpload },
  data() {
    return {
      pageLoading: false,
      submitLoading: false,
      form: {
        category: 'wxMini',
        mini_appid: '',
        mini_appSecret: '',
        wxpay_mchId: '',
        wxpay_mchKey: '',
        wxpay_keyPath: '',
        wxpay_notify_url: ''
      },
      rules: {}
    }
  },
  created() {
    this.loadConfig()
  },
  methods: {
    loadConfig() {
      this.pageLoading = true
      getCategoryConfig(this.form.category).then(rese => {
        const that = this
        rese.data.map(function (key) {
          const keyName = key.configKey
          const newValue = key.configValue
          if (keyName in that.form) {
            that.form[keyName] = newValue
          }
        })
      }).finally(() => {
        this.pageLoading = false
      })
    },
    doSubmit() {
      this.submitLoading = true
      saveConfig(this.form).then(() => {
        this.$notify.success({
          title: '成功',
          message: '设置成功'
        })
      }).catch(err => {
        console.log(err.msg)
      }).finally(() => {
        this.submitLoading = false
      })
    }
  }
}
</script>

<style scoped lang="scss">
.wechat-page {
  max-width: 1080px;
  padding: 16px 20px 32px;
}

.wechat-hero {
  display: flex;
  align-items: center;
  gap: 16px;
  min-height: 88px;
  margin-bottom: 16px;
  padding: 20px 22px;
  border-radius: 12px;
  background: linear-gradient(135deg, #07c160 0%, #1aad19 58%, #2aae67 100%);
  color: #fff;
  box-shadow: 0 8px 24px rgba(7, 193, 96, 0.18);

  &__icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 52px;
    height: 52px;
    border-radius: 14px;
    background: rgba(255, 255, 255, 0.16);
    flex-shrink: 0;
  }

  &__text {
    min-width: 0;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 650;
      letter-spacing: 0.3px;
    }

    p {
      margin: 6px 0 0;
      font-size: 13px;
      opacity: 0.88;
    }
  }
}

.wechat-card {
  margin-bottom: 16px;
  border: none;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);

  :deep(.el-card__header) {
    padding: 14px 20px;
    border-bottom: 1px solid #f0f2f5;
  }

  :deep(.el-card__body) {
    padding: 20px 20px 8px;
  }
}

.card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;

  &__title {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;

    &::before {
      content: '';
      width: 3px;
      height: 16px;
      border-radius: 2px;
      background: #07c160;
    }
  }
}

.field-tip {
  margin-top: 6px;
  line-height: 1.5;
  font-size: 12px;
  color: #909399;
}

.wechat-form {
  :deep(.el-form-item) {
    margin-bottom: 18px;
  }

  :deep(.el-form-item__label) {
    font-weight: 550;
    color: #303133;
    padding-bottom: 6px;
  }

  :deep(.el-input__wrapper) {
    border-radius: 8px;
  }
}

.wechat-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 4px 4px 0;

  .el-button {
    min-width: 96px;
  }
}

@media (max-width: 768px) {
  .wechat-page {
    padding: 12px;
  }

  .wechat-hero {
    padding: 16px;
  }

  .wechat-actions {
    justify-content: stretch;

    .el-button {
      flex: 1;
    }
  }
}
</style>
