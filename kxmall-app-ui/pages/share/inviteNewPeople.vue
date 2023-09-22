<template>
	<view class="Invitation">
		<image src="https://s1.ax1x.com/2020/04/07/G2u0DH.png" mode="" style="width: 100%;height: 340px"></image>
		<view class="btn" @click="naviageToPage('/pages/parity/parity?type=6&id=49&title=掌柜推荐')">
			<!--  -->
			前往购买
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				p: ''
			};
		},
		methods: {
			naviageToPage(page) {
				if(this.$store.state.userInfo.accessToken){
					uni.showModal({
						title:"提示",
						content:"您已领取过新人优惠",
						confirmText:"继续分享",
						cancelText:"继续买菜",
						success: (res) => {
							if(res.confirm){
								uni.navigateTo({
									url:"../Invitation/Invitation"
								})
							}else{
								uni.switchTab({
									url:"../index/index"
								})
							}
						}
					})
				}else{
					uni.navigateTo({
						url:"../public/login"
					})
				}
			}
		},
		onLoad(options) {
			if (options.p) {
				console.log(options.p)
				this.$store.commit('setStorageCode', options.p)
			}
			wx.hideHomeButton()
		}
	}
</script>

<style lang="scss">
	.Invitation {
		background-color: #F4F6FA;
		min-height: 100vh;

		.btn {
			margin: 110px auto 0;
			width: 77%;
			height: 45px;
			background-color: #EF570D;
			text-align: center;
			line-height: 45px;
			color: #fff;
			border-radius: 45rpx;

		}
	}
</style>
