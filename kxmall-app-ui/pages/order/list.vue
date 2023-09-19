<template>
	<view class="content">
		<view class="navbar">
			<view v-for="(item, index) in navList" :key="index" class="nav-item" :class="{current: tabCurrentIndex === index}"
			 @click="tabClick(index)">
				{{item.text}}
			</view>
		</view>

		<swiper :current="tabCurrentIndex" class="swiper-box" duration="300" @change="changeTab">
			<swiper-item class="tab-content" v-for="(tabItem,tabIndex) in navList" :key="tabIndex">
				<scroll-view class="list-scroll-content" scroll-y @scrolltolower="loadData">
					<!-- 空白页 -->
					<!-- <empty v-if="tabItem.loaded === true && tabItem.orderList.length === 0"></empty> -->

					<!-- 订单列表 -->
					<view v-for="(item,index) in tabItem.orderList" :key="index" class="order-item">
						<navigator :url="'/pages/order/detail?orderid=' + item.id">
							<view class="i-top b-b">
								<text class="time">{{item.createTime | dateFormat}}</text>
								<text class="state">{{item.stateText}}</text>
							</view>

							<scroll-view v-if="item.productList.length > 1" class="goods-box" scroll-x>
								<view v-for="(skuItem, skuIndex) in item.productList" :key="skuIndex" class="goods-item">
									<image class="goods-img" :src="JSON.parse(skuItem.img)[0].url" mode="aspectFill"></image>
								</view>
							</scroll-view>
							<view v-if="item.productList.length === 1" class="goods-box-single" v-for="(skuItem, skuIndex) in item.productList" :key="skuIndex">
								<image class="goods-img" :src="JSON.parse(skuItem.img)[0].url" mode="aspectFill"></image>
								<view class="right">
									<text class="title clamp">{{skuItem.productTitle}}</text>
									<text class="attr-box">{{skuItem.productTitle}} x {{skuItem.num}}</text>
									<text class="price">{{skuItem.price}}<text class="text-gray text-sm">/{{skuItem.unitName}}</text></text>
								</view>
							</view>

							<view class="price-box">
								共
								<text class="num">{{item.skuCount}}</text>
								件商品 实付款
								<text class="price">{{item.payPrice}}</text>
							</view>
						</navigator>
						<view class="action-box b-t" v-if="item.status == 10">
							<button :disabled="submiting" class="action-btn" @click="cancelOrder(item)">取消订单</button>
							<button class="action-btn recom" @click="payOrder(item)">立即支付</button>
						</view>
						<view class="action-box b-t" v-if="item.state == 1">
							<button :disabled="submiting" class="action-btn" @click="refundOrder(item)">申请退款</button>
						</view>
						<!-- <view class="action-box b-t" v-if="item.status == 30">
							<button :disabled="submiting" class="action-btn" @click="refundOrder(item)">申请退款</button>
							<button :disabled="submiting" class="action-btn" @click="showShipTrace(item)">查看物流</button>
							<button :disabled="submiting" class="action-btn recom" @click="confirmOrder(item)">确认收货</button>
						</view> -->
						<view class="action-box b-t" v-if="item.status == 40">
							<view >
								<button :disabled="submiting" class="action-btn recom" @click="appraiseOrder(item)">立即评价</button>
							</view>
						</view>
					</view>
					<view style="padding: 150rpx 0rpx;"
					v-if="tabItem.loaded === true && tabItem.orderList.length === 0">
						<missing
						v-if="tabItem.loaded === true && tabItem.orderList.length === 0"
						:imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%203%20%E6%8B%B7%E8%B4%9D@3x.png'"
						:desc="'暂时没有订单哦！'"></missing>
					</view>
					<uni-load-more v-else :status="tabItem.loadingType"></uni-load-more>

				</scroll-view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import missing from '@/components/missing.vue'
	// import empty from "@/components/empty";
	export default {

		components: {
			uniLoadMore,missing
			// empty
		},
		data() {
			return {
				statusMap: {
					10: '未付款',
					12: '正在拼团',
					20: '待出库',
					30: '待收货',
					40: '待评价',
					50: '已完成',
					60: '退款中',
					70: '已退款',
					80: '已取消',
					90: '已取消(系统)'
				},
				submiting: false,
				tabCurrentIndex: 0,
				navList: [{
						state: "",
						text: '全部',
						loadingType: 'more',
						pageNo: 1,
						orderList: []
					},
					{
						state: "10",
						text: '待付款',
						loadingType: 'more',
						pageNo: 1,
						orderList: []
					},
					{
						state: "14,16,20,30",
						text: '待收货',
						loadingType: 'more',
						pageNo: 1,
						orderList: []
					},
					{
						state: "40,50",
						text: '待评价',
						loadingType: 'more',
						pageNo: 1,
						orderList: []
					}
				],
			};
		},
		onLoad(options) {
			/**
			 * 修复app端点击除全部订单外的按钮进入时不加载数据的问题
			 * 替换onLoad下代码即可
			 */
			this.tabCurrentIndex = 0
			for (let i = 0; i < this.navList.length; i++) {
				if (i === parseInt(options.state)) {
					this.tabCurrentIndex = i
				}
			}
			// #ifndef MP
			this.loadData()
			// #endif
			// #ifdef MP
			if (options.state == 0) {
				this.loadData()
			}
			// #endif

		},

		methods: {
			//获取订单列表
			loadData(source) {
				const that = this
				//这里是将订单挂载到tab列表下
				let index = that.tabCurrentIndex;
				let navItem = that.navList[index];
				let state = navItem.state;

				if (source === 'tabChange' && navItem.loaded === true) {
					//tab切换只有第一次需要加载数据
					return;
				}
				if (navItem.loadingType === 'loading') {
					//防止重复加载
					return;
				}

				navItem.loadingType = 'loading';

				let orderList = that.$api.request('get', 'order/app/getOrderPage', {
					pageNo: navItem.pageNo,
					status: navItem.state
				}).then(res => {
					navItem.loadingType = navItem.pageNo < res.total/ navItem.pageNo + (res.total%navItem.pageNo==0?0:1) ? 'more' : 'noMore'
                    navItem.pageNo = navItem.pageNo + 1
                    res.rows.forEach(item => {
						if(item.status === 10){
							item.state = 0
							item.stateText = '待支付'
						}
						if(item.status === 14 || item.status === 16 || item.status === 20){
							item.state = 1
							item.stateText = '备货中'
						}
						if(item.status === 30){
							item.state = 2
							item.stateText = '配送中'
						}
						if(item.status === 40){
							item.state = 3
							item.stateText = '待评价'
						}
						if(item.status === 50){
							item.state = 4
							item.stateText = '已完成'
						}
						if(item.status === 80 || item.status === 90){
							item.state = 5
							item.stateText = '已取消'
						}
						if(item.status === 32){
							item.state = 6
							item.stateText = '配送异常'
						}
						if(item.status === 60){
							item.state = 7
							item.stateText = '退款中'
						}
						if(item.status === 70){
							item.state = 8
							item.stateText = '已退款'
						}
						navItem.orderList.push(item);
						item.skuCount = 0
						item.productList.forEach(skuItem => {
							item.skuCount += skuItem.num
						})
					})
					//loaded新字段用于表示数据加载完毕，如果为空可以显示空白页
					that.$set(navItem, 'loaded', true);
				})
			},
			//刷新数据
			refresh(){
				let index = this.tabCurrentIndex;
				let navItem = this.navList[index];
				this.$set(navItem, 'loaded', false);
				this.$set(navItem, 'pageNo', 1);
				navItem.orderList = []
				this.loadData()
			},
			//swiper 切换
			changeTab(e) {
				this.tabCurrentIndex = e.target.current;
				this.loadData('tabChange');
			},
			//顶部tab点击
			tabClick(index) {
				this.tabCurrentIndex = index;
			},
			payOrder(item) {
				// #ifdef APP-PLUS
				this.$api.msg('演示环境不支持支付')
				return
				// #endif
				uni.showLoading({})
				const that = this
				that.$api.request('get', 'order/app/wxPrepay', {
					orderId : item.orderId
				}, failres => {
					that.submiting = false
					that.$api.msg(failres.msg)
				}).then(prepayRes => {
					uni.hideLoading()
					that.submiting = false
					//#ifdef MP-WEIXIN
					const payParam = {
						appId: prepayRes.data.appId,
						nonceStr: prepayRes.data.nonceStr,
						package: prepayRes.data.packageValue,
						timeStamp: prepayRes.data.timeStamp,
						signType: prepayRes.data.signType,
						paySign: prepayRes.data.paySign,
					}
					//#endif
					//#ifdef APP-PLUS
					const payParam = {
						appid: prepayRes.data.appId,
						noncestr: prepayRes.data.nonceStr,
						package: prepayRes.data.packageValue,
						partnerid: prepayRes.data.partnerId,
						prepayid: prepayRes.data.prepayId,
						timestamp: parseInt(prepayRes.data.timeStamp),
						sign: prepayRes.data.sign,
						signType: 'MD5'
					}
					//#endif
					//#ifdef MP-WEIXIN || APP-PLUS
					uni.requestPayment({
						provider: 'wxpay',
						//#ifdef MP-WEIXIN
						...payParam,
						//#endif
						//#ifdef APP-PLUS
						orderInfo: payParam,
						//#endif
						success: function(res) {
							uni.redirectTo({
								url: '/pages/pay/success'
							})
						},
						fail: function(res) {
							// console.log("支付过程失败");
							// that.$api.msg(JSON.stringify(res))
						},
						complete: function(res) {
							console.log("支付过程结束")
						}
					});
					//#endif
					//#ifdef H5
					that.$jweixin.chooseWXPay({
						nonceStr: prepayRes.data.nonceStr,
						timestamp: prepayRes.data.timeStamp,
						package: prepayRes.data.packageValue,
						signType: prepayRes.data.signType,
						paySign: prepayRes.data.paySign,
						success: (e) => {
							//支付成功
							uni.redirectTo({
								url: '/pages/pay/success'
							})
						},
						fail: function(res) {
							console.log("支付过程失败");
							that.$api.msg(JSON.stringify(res))
						},
						complete: function(res) {
							console.log("支付过程结束")
						}
					})
					//#endif
				})
			},
			//取消订单
			cancelOrder(item) {
				const that = this
				uni.showModal({
					title: '取消？',
					content: '您确定要取消此订单吗？',
					success : (e) => {
						if (e.confirm) {
							that.submiting = true
							that.$api.request('get', 'order/app/cancel', {
								orderId: item.orderId
							}, failres => {
								that.submiting = false
								that.$api.msg(failres.msg)
							}).then(res => {
								that.submiting = false
								item.status = 80
								// this.refresh()
							})
						}
					}
				})

			},
			//订单退款
			refundOrder(item) {
				const that = this
				uni.showModal({
					title: '退款？',
					content: '您确定要退款吗？',
					success : (e) => {
						if (e.confirm) {
							that.submiting = true
							that.$api.request('get', 'order/app/refund', {
								orderId: item.orderId
							}, failres => {
								that.submiting = false
								that.$api.msg(failres.msg)
							}).then(res => {
								that.submiting = false
								item.status = 60
								this.refresh()
							})
						}
					}
				})
			},
			//确认订单
			confirmOrder(item) {
				const that = this
				uni.showModal({
					title: '收货？',
					content: '您确定要确认收货吗？',
					success : (e) => {
						if (e.confirm) {
							that.submiting = true
							that.$api.request('get', 'order/app/confirm', {
								orderId: item.orderId
							}, failres => {
								that.submiting = false
								that.$api.msg(failres.msg)
							}).then(res => {
								that.submiting = false
								item.status = 40
							})
						}
					}
				})
			},
			showShipTrace(item) {
				uni.navigateTo({
					url: "/pages/order/trace?orderno=" + item.orderId
				})
			},
			//评价订单
			appraiseOrder(item) {
				uni.navigateTo({
					url: '/pages/order/appraise?orderid=' + item.id
				})
			}
		}
	}
</script>

<style lang="scss">
	page,
	.content {
		background: $page-color-base;
		height: 100%;
	}

	.swiper-box {
		height: calc(100% - 40px);
	}

	.list-scroll-content {
		height: 100%;
	}

	.navbar {
		display: flex;
		height: 40px;
		padding: 0 5px;
		background: #fff;
		box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
		position: relative;
		z-index: 10;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			color: $font-color-dark;
			position: relative;

			&.current {
				color: $base-color;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					border-bottom: 2px solid $base-color;
				}
			}
		}
	}

	.uni-swiper-item {
		height: auto;
	}

	.order-item {
		display: flex;
		flex-direction: column;
		padding-left: 30upx;
		background: #fff;
		margin-top: 16upx;

		.i-top {
			display: flex;
			align-items: center;
			height: 80upx;
			padding-right: 30upx;
			font-size: $font-base;
			color: $font-color-dark;
			position: relative;

			.time {
				flex: 1;
			}

			.state {
				color: $base-color;
			}

			.del-btn {
				padding: 10upx 0 10upx 36upx;
				font-size: $font-lg;
				color: $font-color-light;
				position: relative;

				&:after {
					content: '';
					width: 0;
					height: 30upx;
					border-left: 1px solid $border-color-dark;
					position: absolute;
					left: 20upx;
					top: 50%;
					transform: translateY(-50%);
				}
			}
		}

		/* 多条商品 */
		.goods-box {
			height: 160upx;
			padding: 20upx 0;
			white-space: nowrap;

			.goods-item {
				width: 120upx;
				height: 120upx;
				display: inline-block;
				margin-right: 24upx;
			}

			.goods-img {
				display: block;
				width: 100%;
				height: 100%;
			}
		}

		/* 单条商品 */
		.goods-box-single {
			display: flex;
			padding: 20upx 0;

			.goods-img {
				display: block;
				width: 120upx;
				height: 120upx;
			}

			.right {
				flex: 1;
				display: flex;
				flex-direction: column;
				padding: 0 30upx 0 24upx;
				overflow: hidden;

				.title {
					font-size: $font-base + 2upx;
					color: $font-color-dark;
					line-height: 1;
				}

				.attr-box {
					font-size: $font-sm + 2upx;
					color: $font-color-light;
					padding: 10upx 12upx;
				}

				.price {
					font-size: $font-base + 2upx;
					color: $font-color-dark;

					&:before {
						content: '￥';
						font-size: $font-sm;
						margin: 0 2upx 0 8upx;
					}
				}
			}
		}

		.price-box {
			display: flex;
			justify-content: flex-end;
			align-items: baseline;
			padding: 20upx 30upx;
			font-size: $font-sm + 2upx;
			color: $font-color-light;

			.num {
				margin: 0 8upx;
				color: $font-color-dark;
			}

			.price {
				font-size: $font-lg;
				color: $font-color-dark;

				&:before {
					content: '￥';
					font-size: $font-sm;
					margin: 0 2upx 0 8upx;
				}
			}
		}

		.action-box {
			display: flex;
			justify-content: flex-end;
			align-items: center;
			height: 100upx;
			position: relative;
			padding-right: 30upx;
		}

		.action-btn {
			width: 160upx;
			height: 60upx;
			margin: 0;
			margin-left: 24upx;
			padding: 0;
			text-align: center;
			line-height: 60upx;
			font-size: $font-sm + 2upx;
			color: $font-color-dark;
			background: #fff;
			border-radius: 100px;

			&:after {
				border-radius: 100px;
			}

			&.recom {
				// background: #fff9f9;
				color: $base-color;

				// &:after {
				// 	border-color: #f7bcc8;
				// }
			}
		}
	}


	/* load-more */
	.uni-load-more {
		display: flex;
		flex-direction: row;
		height: 80upx;
		align-items: center;
		justify-content: center
	}

	.uni-load-more__text {
		font-size: 28upx;
		color: #999
	}

	.uni-load-more__img {
		height: 24px;
		width: 24px;
		margin-right: 10px
	}

	.uni-load-more__img>view {
		position: absolute
	}

	.uni-load-more__img>view view {
		width: 6px;
		height: 2px;
		border-top-left-radius: 1px;
		border-bottom-left-radius: 1px;
		background: #999;
		position: absolute;
		opacity: .2;
		transform-origin: 50%;
		animation: load 1.56s ease infinite
	}

	.uni-load-more__img>view view:nth-child(1) {
		transform: rotate(90deg);
		top: 2px;
		left: 9px
	}

	.uni-load-more__img>view view:nth-child(2) {
		transform: rotate(180deg);
		top: 11px;
		right: 0
	}

	.uni-load-more__img>view view:nth-child(3) {
		transform: rotate(270deg);
		bottom: 2px;
		left: 9px
	}

	.uni-load-more__img>view view:nth-child(4) {
		top: 11px;
		left: 0
	}

	.load1,
	.load2,
	.load3 {
		height: 24px;
		width: 24px
	}

	.load2 {
		transform: rotate(30deg)
	}

	.load3 {
		transform: rotate(60deg)
	}

	.load1 view:nth-child(1) {
		animation-delay: 0s
	}

	.load2 view:nth-child(1) {
		animation-delay: .13s
	}

	.load3 view:nth-child(1) {
		animation-delay: .26s
	}

	.load1 view:nth-child(2) {
		animation-delay: .39s
	}

	.load2 view:nth-child(2) {
		animation-delay: .52s
	}

	.load3 view:nth-child(2) {
		animation-delay: .65s
	}

	.load1 view:nth-child(3) {
		animation-delay: .78s
	}

	.load2 view:nth-child(3) {
		animation-delay: .91s
	}

	.load3 view:nth-child(3) {
		animation-delay: 1.04s
	}

	.load1 view:nth-child(4) {
		animation-delay: 1.17s
	}

	.load2 view:nth-child(4) {
		animation-delay: 1.3s
	}

	.load3 view:nth-child(4) {
		animation-delay: 1.43s
	}

	@-webkit-keyframes load {
		0% {
			opacity: 1
		}

		100% {
			opacity: .2
		}
	}
</style>
