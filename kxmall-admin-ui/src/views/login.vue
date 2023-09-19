<template>
  <div class="login-form-layout">
    <div class="login-form-left">
      <img src="@/assets/images/login/loginLogoLeft.png" alt="" />
    </div>
    <div class="login-form-right">
      <div class="login-form-container">
        <el-form
          class="login-form"
          autoComplete="on"
          :model="loginForm"
          :rules="loginRules"
          ref="loginForm"
          label-position="left"
        >
          <div style="text-align: center">
            <img
              class="logo-right"
              src="@/assets/images/login/loginRightLogo.png"
              alt=""
            />
          </div>
          <el-form-item prop="username">
            <el-input
              name="username"
              type="text"
              v-model="loginForm.username"
              autoComplete="on"
              placeholder="请输入用户名"
            >
              <span slot="prefix">
                <svg-icon icon-class="user" class="color-main"></svg-icon>
              </span>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              name="password"
              :type="pwdType"
              @keyup.enter.native="handleLogin"
              v-model="loginForm.password"
              autoComplete="on"
              placeholder="请输入密码"
            >
              <span slot="prefix">
                <svg-icon icon-class="password" class="color-main"></svg-icon>
              </span>
              <span slot="suffix" @click="showPwd">
                <svg-icon icon-class="eye" class="color-main"></svg-icon>
              </span>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-col :span="15">
              <el-input
                v-model="loginForm.code"
                placeholder="输入验证码"
                auto-complete="off"
                clearable
                @keyup.enter.native="handleLogin"
              >
                <span slot="prefix">
                  <svg-icon
                    slot="prefix"
                    icon-class="email"
                    style="margin-top: 6px"
                  />
                </span>
              </el-input>
            </el-col>
            <el-col :span="6" style="margin-left: 10px; margin-top: 2px">
              <img :src="codeUrl" @click="getCode" width="130" height="35"/>
            </el-col>
          </el-form-item>
          <el-form-item style="margin-bottom: 20px">
            <el-button
              style="width: 100%"
              type="primary"
              :loading="loading"
              @click.native.prevent="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>

<!--          <el-form-item style="margin-bottom: 10px">-->
<!--            <el-button style="width: 100%" type="primary"  @click.native.prevent="handleStore">-->
<!--              商家入驻-->
<!--            </el-button>-->
<!--          </el-form-item>-->

        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      pwdType: 'password',
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCookie();
    this.getCode();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.data.captchaEnabled === undefined ? true : res.data.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.data.img;
          this.loginForm.uuid = res.data.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    showPwd () {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.login-form-layout {
  display: flex;
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;

  .login-form-left {
    width: 60%;
    height: 100vh;
    background-color: #409eff;
    float: left;

    img {
      width: 60%;
      margin-left: 20%;
      margin-top: 15%;
    }
  }

  .login-form-right {
    height: 100vh;
    width: 40%;
    float: left;

    .logo-right {
      width: 40%;
      margin-top: 10vh;
    }

    .login-form-container {
      width: 50%;
      margin: auto;
      height: 30vh;
      margin-top: 20vh;

      .login-form {
        width: 100%;
        height: 20vh;
      }
    }
  }
}
</style>
