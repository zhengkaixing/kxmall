<template>
	<view >
		<!-- #ifdef MP-WEIXIN -->
		<view style="background-image: url(http://qiniuoss.nauzone.cn/%E7%BC%96%E7%BB%84%202@3x.png);background-repeat: no-repeat;background-size: 750rpx 260rpx;position: absolute;width: 750rpx;height: 260rpx;top: 0;z-index: -1;">

		</view>
		<!-- #endif -->

		<view class="flex align-center justify-center flex-direction" style="z-index: 1;">
			<!-- #ifdef MP-WEIXIN -->
			<image src="../../static/login/logoo.png" mode="aspectFit" class=" radius"
			style="width: 266rpx;height: 266rpx;margin-top: 148rpx;"></image>
			<!-- #endif -->
			<!-- #ifdef APP-PLUS -->
			<image src="../../static/login/logoo.png" mode="aspectFit" class="margin-top radius"
			style="width: 266rpx;height: 266rpx;"></image>
			<!-- #endif -->
			<!-- <image src="../../static/login/laileme-text.png" mode="aspectFit"
			style="width: 130rpx;height: 40rpx;margin-top: 32rpx;"></image> -->
			<!-- 兼容小程序端使用手机号登陆 -->
			<view v-if="mobileLogin" class="flex align-center justify-center flex-direction">
				<view class="padding-tb-sm flex align-center"
				style="margin-left: 66rpx;margin-right: 66rpx;width: 616rpx; border-bottom: #eeeeee solid 2rpx;margin-top: 56rpx;">
					<image src="../../static/login/mobile.png" mode="aspectFit" style="margin-left: 26rpx;margin-right: 34rpx;width: 36rpx;height: 40rpx;"></image>
					<view style="font-size: 30rpx;color: #bbbbbb;">
						<input v-model="phone" type="number" class="text-black lem-text-lg"
						placeholder-style="font-size: 30rpx;color: #bbbbbb;"
						placeholder="请输入手机号码" />
					</view>
				</view>
				<view class="padding-tb flex align-center justify-between"
				style="margin-left: 66rpx;margin-right: 66rpx;width: 616rpx; border-bottom: #eeeeee solid 2rpx;">
					<view class="flex align-center">
						<image src="../../static/login/code.png" mode="aspectFit" style="margin-left: 26rpx;margin-right: 34rpx;width: 36rpx;height: 40rpx;"></image>
						<view style="font-size: 30rpx;color: #bbbbbb;">
							<input type="number" v-model="verifyCode" class="text-black lem-text-lg"
							placeholder-style="font-size: 30rpx;color: #bbbbbb;"
							placeholder="请输入验证码" />
						</view>
					</view>
					<button :disabled="sendDisabled" @click="doGetVerify"
					:class="[sendDisabled?'bg-gradual-greend':'bg-gradual-green']"
					class="lem-btn round">{{sendText}}</button>
				</view>
				<button class="round "  @click="loginRegis"
				:class="[phone.length >= 11 && verifyCode.length>= 6?'bg-gradual-green':'bg-gradual-greend']"
				style="height: 96rpx;width: 640rpx;margin-top: 72rpx;line-height: 96rpx;">登录</button>
        <view class="lem-text-df" style="margin-top: 30rpx;color: #bbbbbb;line-height: 44rpx;font-size: 28rpx;width: 600rpx;">
          <label class="radio">
            <radio-group @change="checkOne" style="float: left">
              <radio :value="1" color="#8dc63f" :checked='checkType == 1' />
            </radio-group>
          </label>
          勾选即代表确认 <text style="color: #1F7AFF;">《注册协议》</text> <text style="color: #1F7AFF;">《隐私协议》</text>
				</view>
				<view class="flex align-center" style="margin-top: 72rpx;">
					<image src="../../static/line.png" mode="aspectFill" style="height: 4rpx;width: 244rpx;"></image>
					<view class="lem-text-xl" style="color: #848582;margin-left: 22rpx;margin-right: 22rpx;">快速登录</view>
					<image src="../../static/line.png" mode="aspectFill" style="height: 4rpx;width: 244rpx;"></image>
				</view>
				<view class="flex align-center justify-center" style="margin-top: 50rpx;">
					<view @click="mobileLogin = false" class="flex align-center justify-center flex-direction">
						<image class="round" src="../../static/login/weixindefuben@3x.png" mode="aspectFit"
						style="width: 84rpx;height: 84rpx;margin-bottom: 28rpx;"></image>
						<view class="lem-text-xl" style="color: #848582;">微信</view>
					</view>
				</view>
			</view>
			<!-- #ifdef MP-WEIXIN -->
			<view v-else class="flex align-center justify-center flex-direction">
				<button v-if="!puserInfo.phone" open-type="getPhoneNumber"
				 @getphonenumber="miniWechatLogin" class="round bg-gradual-green"
				style="height: 96rpx;width: 640rpx;margin-top: 174rpx;line-height: 96rpx; font-size: 32rpx;font-weight: 600;"
				>一键登录</button>
				<button v-else @click="fakeLogin" class="round bg-gradual-green"
				style="height: 96rpx;width: 640rpx;margin-top: 174rpx;line-height: 96rpx; font-size: 32rpx;font-weight: 600;"
				>一键登录</button>
				<view @click="mobileLogin = true" class="lem-text-xl" style="margin-top: 60rpx;color: #bbbbbb;line-height: 30rpx;font-weight: 600;">
					手机验证码、注册登录
				</view>
        <view class="lem-text-df" style="margin-top: 30rpx;color: #bbbbbb;line-height: 22px;font-size: 28rpx;width: 600rpx;">
          <label class="radio">
            <radio-group @change="checkOne" style="float: left">
              <radio :value="1" color="#8dc63f" :checked='checkType == 1' />
            </radio-group>
          </label>
          勾选即代表确认 <text style="color: #1F7AFF;">《注册协议》</text> <text style="color: #1F7AFF;">《隐私协议》</text>
				</view>
			</view>

			<!-- #endif -->
		</view>
	</view>
</template>

<script>
	import {
		mapMutations
	} from 'vuex';

	export default {
		data() {
			return {
        checkType: 0,
				loginType: '',
				phone: '',
				password: '',
				logining: false,
				sessionKey:'',
				sendText: '获取验证码',
				sendDisabled: false,
				verifyCode:'',
				puserInfo:{},
				mobileLogin:false
			}
		},
		onShow() {
			this.$api.logout()
		},
		onLoad(options) {
			// #ifndef MP-WEIXIN
			this.mobileLogin = true
			// #endif

			// #ifdef MP-WEIXIN
			uni.showLoading({

			})
			uni.login({
				provider: 'weixin',
				success: (wxres => {
					this.logining = false
					this.$api.request('get', 'miniLogin', {
                        loginType: 1,
                        code: wxres.code
					}, failres => {
						this.$api.msg(failres.msg)
						uni.hideLoading()
					}).then(res => {
						uni.hideLoading()
						console.log(res)
						this.puserInfo = res.data
						// this.$api.setUserInfo(res.data)
						this.sessionKey = res.data.sessionKey
					})
				})
			})
			// #endif

		},
		methods: {
			...mapMutations(['login']),
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			chooseLoginType(type) {
				this.loginType = type
			},
			navBack() {
				uni.navigateBack();
			},
			toRegist() {
				uni.redirectTo({
					url: '/pages/public/register'
				})
			},
      checkOne(e){
        this.checkType = e.detail.value
      },
			doGetVerify() {
				const that = this
				if (!that.phone || that.phone.length != 11) {
					uni.showToast({
						title:'请输入正确手机号！',
						icon:'none'
					})
					return
				}
                that.$api.request('get', 'captchaSms', {
                    phonenumber: that.phone,
				}).then(res => {
					//debugger
					that.sendDisabled = true
					let sec = 60
					let interval = setInterval(() => {
						sec--;
						that.sendText = sec + 's后重发'
						if (sec <= 0) {
							that.sendDisabled = false
							that.sendText = "获取验证码"
							clearInterval(interval)
						}
					}, 1000)
				})

			},
			async toLogin() {
				const that = this
				if (that.phone.length !== 11) {
					that.$api.msg('请输入11位中国手机号')
				} else if (that.password.length < 8) {
					that.$api.msg('密码至少8位')
        } else if (that.checkType === 0) {
          that.$api.msg('请勾选注册协议与隐私协议')
				} else {
					that.logining = true;
					//#ifdef MP-WEIXIN
					//若是小程序平台，则获取到openId。整个过程是静默完成的
					uni.login({
						provider: 'weixin',
						success: (wxres => {
							that.$api.request('get', '/login', {
								phone: that.phone,
								password: that.password,
								loginType: 1,
								raw: JSON.stringify(wxres)
							}, failres => {
								that.logining = false
								uni.showToast({
									title: failres.msg,
									icon: "none"
								});
							}).then(res => {
								that.logining = false
								that.$store.commit('login', res.data)
								uni.setStorageSync('userInfo', res.data)
								if (that.$api.prePage().lodaData) {
									that.$api.prePage().loadData()
								}
								uni.navigateBack()
							})
						})
					})
					//#endif
					//#ifdef APP-PLUS || H5
					//若是App登录，则不需要保存OpenId。可直接登录
					that.$api.request('get', 'login', {
						phone: that.phone,
						password: that.password,
					}, failres => {
						that.logining = false
						uni.showToast({
							title: failres.msg,
							icon: "none"
						});
					}).then(res => {
						that.logining = false
						that.$store.commit('login', res.data)
						uni.setStorageSync('userInfo', res.data)
						if (that.$api.prePage().lodaData) {
							that.$api.prePage().loadData()
						}
						uni.navigateBack()
					})
					//#endif
				}
			},
			loginRegis(){
				const that = this
				if (that.phone.length !== 11) {
					that.$api.msg('请输入11位中国手机号')
					return
				}
				if (!that.verifyCode) {
					that.$api.msg('请输入验证码')
					return
				}
        if (that.checkType === 0) {
          that.$api.msg('请勾选注册协议与隐私协议')
          return
        }
				that.logining = true;
				uni.showLoading({

				})
				var param = {
					phone: that.phone,
					verifyCode: that.verifyCode,
				}

				if(that.$store.state.InvitationCode){
					param.p = that.$store.state.InvitationCode
				}
				that.$api.request('get', 'phoneLogin', {
					phone: that.phone,
					verifyCode: that.verifyCode,
					openId:that.puserInfo.openId,
				}, failres => {
					uni.hideLoading()
					that.logining = false
					uni.showToast({
						title: failres.msg,
						icon: "none"
					});
				}).then(res => {
          //console.log(res)
          uni.hideLoading()
          that.logining = false
          // uni.navigateBack({
          // 	delta:1
          // })
          that.$store.commit('login', res.data)
          uni.setStorageSync('userInfo', res.data)
          that.$api.setUserInfo(res.data)
          if (that.$api.prePage().lodaData) {
            that.$api.prePage().loadData()
          }
          uni.switchTab({
            url:"../index/index"
          })
        })
			},
			fakeLogin(){
        const that = this
        if (that.checkType === 0) {
          that.$api.msg('请勾选注册协议与隐私协议')
          return
        }
        uni.showLoading({})
				//老用户，活动提醒
				setTimeout(function() {
					that.$api.setUserInfo(that.puserInfo)
					that.$store.commit('login', that.puserInfo)
					uni.setStorageSync('userInfo', that.puserInfo)
					uni.hideLoading()
                    uni.navigateBack({
                        delta:1
                    })
				}, 800);
			},
			//授权手机号登录
			miniWechatLogin(e) {
                const that = this
                if (that.checkType === 0) {
                    that.$api.msg('请勾选注册协议与隐私协议')
                    return
                }
				uni.showLoading({})
				if(!this.sessionKey || !this.puserInfo.openId){
					console.log('第三方登录异常')
					console.log('sessionKEY：'+this.sessionKey)
					console.log('openid:'+this.puserInfo.openId)
					this.$api.msg('登录异常，请重新点击授权登录')
					return
					uni.showLoading({})
					uni.login({
						provider: 'weixin',
						success: (wxres => {
							this.logining = false
							this.$api.request('get', 'miniLogin', {
								loginType: 1,
								raw: JSON.stringify(wxres)
							}, failres => {
								this.$api.msg(failres.msg)
								uni.hideLoading()
							}).then(res => {
								uni.hideLoading()
								console.log(res)
								this.$api.msg('登录异常，请重新点击授权登录')
								this.puserInfo = res.data
								// this.$api.setUserInfo(res.data)
								this.sessionKey = res.data.sessionKey
							})
						})
					})
				}
				console.log(e.detail.errMsg)
				console.log(e.detail.iv)
				console.log(this.puserInfo)
				var param = {
					encryptedData: e.detail.encryptedData,
					iv:e.detail.iv,
					session_key:that.sessionKey,
					openId:that.puserInfo.openId,
					loginType:1
				}
				that.$api.request('get', 'authPhone', param, failres => {
					that.$api.msg(failres.msg)
					uni.hideLoading()
				}).then(res => {
					if(res.code === 200){
						uni.setStorageSync('userInfo', res.data)
						that.$store.commit('login', res.data)
						uni.hideLoading()
                        uni.navigateBack({
                            delta:1
                        })
					}
				})
			},
			wechatLogin() {
				const that = this
				that.logining = true
				let loginType = 2
				uni.showLoading({
					title: '正在同步消息'
				})
				uni.login({
					provider: 'weixin',
					success: (wxres => {
						that.$api.request('get', 'appLogin', {
							loginType: loginType,
							raw: JSON.stringify(wxres)
						}, failres => {
							that.$api.msg(failres.msg)
							uni.hideLoading()
						}).then(res => {
							that.logining = false
							uni.getUserInfo({
								lang: 'zh_CN',
								success: (e) => {
									uni.setStorageSync('userInfo', res.data)
									that.$store.commit('login', res.data)
									e.userInfo.nickname = e.userInfo.nickName
									that.$api.request('post', 'user/app/updateUser', e.userInfo).then(syncRes => {
										//同步过后
										res.data.nickname = e.userInfo.nickName
										res.data.avatarUrl = e.userInfo.avatarUrl
										res.data.gender = e.userInfo.gender
										uni.setStorageSync('userInfo', res.data)
										that.$store.commit('login', res.data)
									})
								},
								complete: (e) => {
									if (that.$api.prePage().lodaData) {
										that.$api.prePage().loadData()
									}
									uni.hideLoading()
									uni.navigateBack()
								}
							})
						})
					})
				})
			},
			wechatH5Login() {
        const that = this
        const href = window.location.href
        const page = that.$api.prePage()
        let prePath = '/pages/index/index'
        window.location = 'https://open.weixin.qq.com/connect/oauth2/authorize?' +
            'appid=' + that.$api.defConfig().h5Appid + '&redirect_uri=' + escape(href) + '&response_type=code&scope=snsapi_userinfo&state=' + escape(prePath) + '#wechat_redirect'
      }		},

	}
</script>

<style lang='scss'>
	page {
		background: #fff;
	}

	.bg-gradual-greend {
	  background-image: linear-gradient(45deg,rgba(57,181,74,0.5),rgba(141,198,63,0.5));
	  /* background-color: #ffffff; */
	  color: #fff;
	}

	.container {
		padding-top: 115px;
		position: relative;
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		background: #fff;
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 40upx;
	}

	.back-btn {
		position: absolute;
		left: 40upx;
		z-index: 9999;
		padding-top: var(--status-bar-height);
		top: 40upx;
		font-size: 40upx;
		color: $font-color-dark;
	}

	.left-top-sign {
		font-size: 120upx;
		color: $page-color-base;
		position: relative;
		left: -16upx;
	}

	.right-top-sign {
		position: absolute;
		top: 80upx;
		right: -30upx;
		z-index: 95;

		&:before,
		&:after {
			display: block;
			content: "";
			width: 400upx;
			height: 80upx;
			background: #b4f3e2;
		}

		&:before {
			transform: rotate(50deg);
			border-radius: 0 50px 0 0;
		}

		&:after {
			position: absolute;
			right: -198upx;
			top: 0;
			transform: rotate(-50deg);
			border-radius: 50px 0 0 0;
			/* background: pink; */
		}
	}

	.left-bottom-sign {
		position: absolute;
		left: -270upx;
		bottom: -320upx;
		border: 100upx solid #d0d1fd;
		border-radius: 50%;
		padding: 180upx;
	}

	.welcome {
		position: relative;
		left: 50upx;
		top: -90upx;
		font-size: 46upx;
		color: #555;
		text-shadow: 1px 0px 1px rgba(0, 0, 0, .3);
	}

	.input-content {
		padding: 0 60upx;
	}

	.input-item {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: center;
		padding: 0 30upx;
		background: $page-color-light;
		height: 120upx;
		border-radius: 4px;
		margin-bottom: 50upx;

		&:last-child {
			margin-bottom: 0;
		}

		.tit {
			height: 50upx;
			line-height: 56upx;
			font-size: $font-sm+2upx;
			color: $font-color-base;
		}

		input {
			height: 60upx;
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			width: 100%;
		}
	}

	.confirm-btn {
		width: 630upx;
		height: 76upx;
		line-height: 76upx;
		border-radius: 50px;
		margin-top: 70upx;
		background: $uni-color-primary;
		color: #fff;
		font-size: $font-lg;

		&:after {
			border-radius: 100px;
		}
	}

	.forget-section {
		font-size: $font-sm+2upx;
		color: $font-color-spec;
		text-align: center;
		margin-top: 40upx;
	}

	.register-section {
		position: absolute;
		left: 0;
		bottom: 50upx;
		width: 100%;
		font-size: $font-sm+2upx;
		color: $font-color-base;
		text-align: center;

		text {
			color: $font-color-spec;
			margin-left: 10upx;
		}
	}
</style>
