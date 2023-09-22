<template>
	<view>
		<view class="state">
			<view class="state-text" v-if="orderDetail.status !== 10" :style="state == 5 || state == 8?'color:#1B1C33;font-size:20px':''">
				{{stateText}}
			</view>
			<view class="state-text" v-if="state === 0">
				{{'等待支付 剩余'+ num(minutes)}}:{{num(seconds)}}
			</view>
			<view class="state-timeText">
				{{state == 0?'逾期未支付将会自动取消':''}}
				{{state == 1 || state == 2?'预约送达时间':''}}
				{{state == 3 || state == 4?'订单已完成':''}}
				{{state == 5?'订单已取消':''}}
				{{state == 8?'订单已取消':''}}
				<!-- {{orderDetail.status==80&&orderDetail.status==90?'订单已取消':orderDetail.status==10&&orderDetail.status==12?'逾期未支付将会自动取消':orderDetail.status==40&&orderDetail.status==50?'订单已送达':orderDetail.status==70?'订单已取消':'预约送达时间 '}} -->
			</view>
			<view class="state-time">
				{{state == 1 || state == 2?orderDetail.predictTime:''}}
				<!-- {{state == 3 || state == 4?'订单已完成':''}} -->
				{{state == 3 || state == 4 || state == 5 || state ==8? timeFormat(orderDetail.updateTime) :''}}
			</view>
			<view @click="refundOrder" class="state-btn state-btn-prohibit" v-if="state == 1" style="margin: 12px auto;">
				申请退款
			</view>
			<view @click="appraiseOrder" class="state-btn state-btn-prohibit" v-if="state == 3" style="margin: 12px auto;">
				立即评价
			</view>
			<view v-if="orderDetail.status === 10" style="display: flex;justify-content: center;">
				<view open-type="contact" class="state-btn state-btn-prohibit" style="width: 69px;" @click="naviageToPage('pages/service/service')">
					<button open-type="contact" class="contact-btn">客服</button>
				</view>
				<view @click="cancelOrder" class="state-btn state-btn-prohibit">
					取消订单
				</view>
				<view @click="confirm" class="state-btn state-btn-ac" >
					立即付款
				</view>
			</view>
		</view>

		<view class="goods">

			<view class="goods-one" v-for="(item, index) in orderDetail.productList" :key="index">
				<image class="goods-img" :src="JSON.parse(item.img)[0].url"></image>
				<view class="goods-text">
					<view class="goods-tag">
						<view class="goods-name">{{item.productTitle}}</view>
						<view class="goods-price">￥{{item.price}}</view>

					</view>
					<view class="goods-number">×{{item.num}}</view>
				</view>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp" style="color: #333333;">配送费</text>
				<text class="cell-tip">{{orderDetail.freightPrice}}</text>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp" style="color: #333333;">合计</text>
				<text class="cell-tip">￥{{orderDetail.payPrice}}</text>
			</view>
		</view>


		<!-- 配送信息 -->
		<view class="yt-list">
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp yt-list-title">配送信息</text>

			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">联系地址</text>
				<text class="cell-tip">{{orderDetail.city}} {{orderDetail.county}} {{orderDetail.userAddress}}</text>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">联系人 </text>
				<text class="cell-tip ">{{orderDetail.realName}}</text>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">期望时间 </text>
				<text class="cell-tip ">{{orderDetail.predictTime}}</text>
			</view>

		</view>
		<!-- 支付信息 -->
		<view class="yt-list">
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp yt-list-title">支付信息</text>

			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">订单号</text>
				<text class="cell-tip">{{orderDetail.orderId}}</text>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">备注信息 </text>
				<text class="cell-tip ">{{orderDetail.remark}}</text>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">支付方式 </text>
				<text class="cell-tip ">{{orderInformation.paymentMethod}}</text>
			</view>
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">下单时间 </text>
				<text class="cell-tip ">{{timeFormat(orderDetail.createTime)}}</text>
			</view>
			<view v-if="orderDetail.gmtPay"  class="yt-list-cell b-b">
				<text class="cell-tit clamp">支付时间 </text>
				<text class="cell-tip ">{{timeFormat(orderDetail.payTime)}}</text>
			</view>

		</view>

		<!-- 底部 -->
		<!-- <view class="footer">
			<view class="price-content">
				<text>实付款</text>
				<text class="price-tip">￥</text>
				<text class="price">{{orderDetail.actualPrice | priceFormat}}</text>
			</view>
			<text class="submit" @click="submit">提交订单</text>
		</view> -->

	</view>
</template>

<script>
	export default {
		filters: {
			priceFormat(price) {
				return price / 100.0
			},
			dateFormat(time) {
				return 'temp'
				//return formatDate(new Date(time),'yyyy-MM-dd HH:mm')
			}
		},
		data() {
			return {
				timeing:'2020-03-01 13:45',
				minutes:0,
				seconds:0,
				Total: 0,
				distributionFee: 123, //配送费
				// 配送信息
				DisInformation: {
					address: '广东省深证市中山大道1号星汇维港购物',
					contacts: '17671467812',
					expectedTime: '9月21日 13:20'
				},
				// 支付信息
				orderInformation: {
					orderNumber: '4567 8123 4567 890',
					information: '',
					paymentMethod: '微信支付',
					orderTime: '2018-04-28 13:58',
					paymentTime: '2018-04-28 13:58'
				},
				orderId: '',
				stateText:'',//状态文字说明
				orderDetail1: {
					skuList: [{
							img: 'https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3529759643,1765872423&fm=26&gp=0.jpg',
							name: '芥蓝',
							price: 10,
							number: '2'
						},
						{
							img: 'https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3529759643,1765872423&fm=26&gp=0.jpg',
							name: '芥蓝',
							price: 10,
							number: '2'
						}
					],
					totalotPrice: 0,
					totalPrice: 0, //商品折扣（仅算VIP和限时打折）后总价
					coupon: undefined,
					mono: '',
					orderDetail: [],
				},
				orderDetail: {
					skuList: [],
					totalotPrice: 0,
					totalPrice: 0, //商品折扣（仅算VIP和限时打折）后总价
					coupon: undefined,
					mono: '',
					status:20
				},
				statusMap: {
					10: '未付款',
					12: '等待团购活动结束',
					14: '待配货',
					16: '配货中',
					20: '待出库',
					30: '待收货',
					32: '配送异常',
					40: '待评价',
					50: '已完成',
					60: '退款中',
					70: '已退款',
					80: '已取消',
					90: '已取消(系统)'
				},
				state: 4
			}
		},
		watch: {
			second: {
				handler(newVal) {
					this.num(newVal)

				}
			},
			minute: {
				handler(newVal) {
					this.num(newVal)
				}
			},
		},
		onLoad(option) {
			this.orderId = option.orderid
			console.log(this.orderId )
			this.loadData()
		},
		methods: {
			naviageToPage(page, title) {
				const that = this
				this.ismask = false
				if (title) {
					console.log(title, 'jinlai')
					title ? title : ''
					uni.navigateTo({
						url: page + '&title=' + title
					})
				} else {
					console.log(title)
					uni.navigateTo({
						url: page
					})
				}
				// console.log(that.categoryButtomList[index].title)

			},
			//申请退款
			refundOrder(){
				const that = this
				uni.showModal({
					title: '取消？',
					content: '您确定要申请退款并取消此订单吗？',
					success : (e) => {
						if (e.confirm) {
							// that.submiting = true
							that.$api.request('get', 'order/app/refund', {
								orderNo: this.orderDetail.orderNo
							}, failres => {
								// that.submiting = false
								that.$api.msg(failres.msg)
							}).then(res => {
								that.loadData()
								// that.submiting = false
								// item.status = 80
							})
						}
					}
				})
			},
			//评价订单
			appraiseOrder() {
				uni.navigateTo({
					url: '/pages/order/appraise?orderid=' + this.orderDetail.id
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
							// that.submiting = true
							that.$api.request('get', 'order/app/cancel', {
								orderNo: this.orderDetail.orderNo
							}, failres => {
								// that.submiting = false
								that.$api.msg(failres.msg)
							}).then(res => {
								that.loadData()
								// that.submiting = false
								// item.status = 80
							})
						}
					}
				})

			},
			// 倒计时
			num(n) {
				return n < 10 ? '0' + n : '' + n
			},
			timer() {
				var _this = this
				var time = setInterval(function() {

					if (_this.seconds === 0 && _this.minutes !== 0) {
						_this.seconds = 59

						_this.minutes -= 1
					} else if (_this.minutes === 0 && _this.seconds === 0) {
						_this.seconds = 0
						clearInterval(time)
						_this.loadData()

					} else {
						_this.seconds -= 1
					}
				}, 1000)
			},
			sum() {
				var x = 0;
				for (var i = 0; i < this.orderDetail1.productList.length; ++i) {
					x += this.orderDetail1.productList[i].price;
				}
				console.log(x)
				this.Total = x
			},
			countDown(){
				let date=this.orderDetail.createTime.getTime() //现在的时间戳
				let datenew=new Date().getTime() //现在的时间戳
				// that.orderDetail.gmtCreate
				let date1=date+(60000*15)  //十五分钟后的时间戳
				let date3=date1-datenew //下单时间
				console.log(datenew,date)
				var leave1=date3%(24*3600*1000)    //计算天数后剩余的毫秒数
				var hours=Math.floor(leave1/(3600*1000))
				//计算相差分钟数
				var leave2=leave1%(3600*1000)        //计算小时数后剩余的毫秒数
				var minutes=Math.floor(leave2/(60*1000))
				//计算相差秒数
				var leave3=leave2%(60*1000)      //计算分钟数后剩余的毫秒数
				var seconds=Math.round(leave3/1000)
				this.minutes=minutes
				this.seconds=seconds
				console.log(minutes+" 分钟"+seconds+" 秒")
			},
			//获取订单详情
			loadData() {
				const that = this
				that.$api.request('get', 'order/app/getOrderDetail', {
					orderId: that.orderId
				}).then(res => {
					that.orderDetail = res.data

					if(that.orderDetail.status === 10){
						this.state = 0
						this.stateText = '待支付'
						this.timer()
						this.countDown()
					}
					if(res.data.status === 14 || res.data.status === 16 || res.data.status === 20){
						this.state = 1
						this.stateText = '备货中'
					}
					if(res.data.status === 30){
						this.state = 2
						this.stateText = '配送中'
					}
					if(res.data.status === 40){
						this.state = 3
						this.stateText = '已完成'
					}
					if(res.data.status === 50){
						this.state = 4
						this.stateText = '已完成'
					}
					if(res.data.status === 80 || res.data.status === 90){
						this.state = 5
						this.stateText = '已取消'
					}
					if(res.data.status === 32){
						this.state = 6
						this.stateText = '配送异常'
					}
					if(res.data.status === 60){
						this.state = 7
						this.stateText = '退款中'
					}
					if(res.data.status === 70){
						this.state = 8
						this.stateText = '已退款'
					}
					this.sum()
				})

			},
			timeFormat(time){
				var date = new Date(time);
				var month = date.getMonth()+1>9?date.getMonth()+1:'0'+parseInt(date.getMonth()+1)
				var day = date.getDate()>9?date.getDate():'0'+date.getDate()
				return date.getFullYear()+'-'+month+'-'+day+' '+date.getHours()+':'+date.getMinutes()
			},
			//确认支付
			confirm() {
				// #ifdef APP-PLUS
				this.$api.msg('演示环境不支持支付')
				return
				// #endif
				uni.showLoading({})
				const that = this
				that.$api.request('post', 'order/app/wxPrepay', {
					orderNo : this.orderDetail.orderNo
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
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: $page-color-base;
		padding-bottom: 100upx;
	}

	.contact-btn{
		border: none;
		background-color: inherit;
		font-size: inherit;
		line-height: inherit;
		color: inherit;
		height: inherit;
		width: inherit;
	}

	.contact-btn::after{
		border: none;
	}

	.state {
		text-align: center;
		padding: 17px 0 12px;
		background-color: #fff;
		width: 100%;

		.state-text {
			font-size: 40upx;
			color: #2AAC34;

		}

		.state-timeText {
			color: #717171;
			font-size: 24upx;
			margin: 6px 0 2px;
		}

		.state-time {
			color: #1B1C33;
			font-size: 28upx;
		}

		.state-btn {
			width: 204upx;
			height: 68upx;
			line-height: 66upx;
			border-radius: 100px;
			margin: 0 auto;
			border: 1px solid #ddd;
			margin-top: 24upx;
			font-size: 15px;
			margin: 24upx 0 0 24upx;

		}

		.state-btn-prohibit {
			border-color: #C4C4CC;
			color: #C4C4CC;
		}
		.state-btn-ac {
			border-color: #2AAC34;
			color: #2AAC34;
			background-color: #2AAC34;
			color: #fff;
		}


	}

	.goods {
		margin-top: 20upx;
		background-color: #FFFFFF;
		overflow: hidden;

		.goods-one {
			padding: 40upx 0 30upx 0;
			margin: 0 30upx;
			height: 190upx;
			border-bottom: 1px solid #F1F1F1;
		}

		.goods-img {
			margin-top: 2upx;
			width: 118upx;
			height: 110upx;
			float: left;
		}

		.goods-text {
			margin-top: 6upx;
			margin-left: 32upx;
			width: 538upx;
			height: 110upx;
			float: left;
		}

		.goods-name {
			width: 500upx;
			line-height: 44upx;
			overflow: hidden;
			color: #333333;
			font-size: 32upx;
		}

		.goods-tag {
			margin-top: 10upx;
			display: flex;
			justify-content: space-between;
			align-items: cenetr;
		}

		.goods-price {
			float: left;
			color: #333333;
			font-size: 32upx;
			line-height: 44upx;
		}

		.goods-yprice {
			margin-left: 16upx;
			float: left;
			color: #AEAEAE;
			font-size: 24upx;
			line-height: 44upx;
			text-decoration: line-through;
		}

		.goods-number {
			float: left;
			color: #333333;
			font-size: 32upx;
		}
	}

	.address-section {
		padding: 30upx 0;
		background: #fff;
		position: relative;

		.order-content {
			display: flex;
			align-items: center;
		}

		.icon-shouhuodizhi {
			flex-shrink: 0;
			display: flex;
			align-items: center;
			justify-content: center;
			width: 90upx;
			color: #888;
			font-size: 44upx;
		}

		.cen {
			display: flex;
			flex-direction: column;
			flex: 1;
			font-size: 28upx;
			color: $font-color-dark;
		}

		.name {
			font-size: 34upx;
			margin-right: 24upx;
		}

		.address {
			margin-top: 16upx;
			margin-right: 20upx;
			color: $font-color-light;
		}

		.icon-you {
			font-size: 32upx;
			color: $font-color-light;
			margin-right: 30upx;
		}

		.a-bg {
			position: absolute;
			left: 0;
			bottom: 0;
			display: block;
			width: 100%;
			height: 5upx;
		}
	}

	.goods-section {
		margin-top: 16upx;
		background: #fff;
		padding-bottom: 1px;

		.g-header {
			display: flex;
			align-items: center;
			height: 84upx;
			padding: 0 30upx;
			position: relative;
		}

		.logo {
			display: block;
			width: 50upx;
			height: 50upx;
			border-radius: 100px;
		}

		.name {
			font-size: 30upx;
			color: $font-color-base;
			margin-left: 24upx;
		}

		.g-item {
			display: flex;
			margin: 20upx 30upx;

			image {
				flex-shrink: 0;
				display: block;
				width: 140upx;
				height: 140upx;
				border-radius: 4upx;
			}

			.right {
				flex: 1;
				padding-left: 24upx;
				overflow: hidden;
			}

			.title {
				font-size: 30upx;
				color: $font-color-dark;
			}

			.spec {
				font-size: 26upx;
				color: $font-color-light;
			}

			.price-box {
				display: flex;
				align-items: center;
				font-size: 32upx;
				color: $font-color-dark;
				padding-top: 10upx;

				.price {
					margin-bottom: 4upx;
				}

				.number {
					font-size: 26upx;
					color: $font-color-base;
					margin-left: 20upx;
				}
			}

			.step-box {
				position: relative;
			}
		}
	}

	.yt-list {
		margin-top: 16upx;
		background: #fff;

	}

	.yt-list-cell-bor {
		border-bottom: 1px solid #F1F1F1;
	}

	.yt-list-cell {
		display: flex;
		align-items: center;
		padding: 10upx 30upx 10upx 40upx;
		line-height: 70upx;
		position: relative;

		&.cell-hover {
			background: #fafafa;
		}

		&.b-b:after {
			left: 30upx;
		}

		.cell-icon {
			height: 32upx;
			width: 32upx;
			font-size: 22upx;
			color: #fff;
			text-align: center;
			line-height: 32upx;
			background: #f85e52;
			border-radius: 4upx;
			margin-right: 12upx;

			&.hb {
				background: #ffaa0e;
			}

			&.lpk {
				background: #3ab54a;
			}

		}

		.cell-more {
			align-self: center;
			font-size: 24upx;
			color: $font-color-light;
			margin-left: 8upx;
			margin-right: -10upx;
		}

		.cell-tit {
			flex: 1;
			font-size: 26upx;
			color: $font-color-light;
			margin-right: 10upx;
		}

		.cell-tip {
			font-size: 26upx;
			color: $font-color-dark;

			&.disabled {
				color: $font-color-light;
			}

			&.active {
				color: $base-color;
			}

			&.red {
				color: $base-color;
			}
		}

		&.desc-cell {
			.cell-tit {
				max-width: 90upx;
			}
		}

		.desc {
			flex: 1;
			font-size: $font-base;
			color: $font-color-dark;
		}

		.yt-list-title {

			font-size: 16px;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 500;
			color: rgba(51, 51, 51, 1);
		}
	}

	/* 支付列表 */
	.pay-list {
		padding-left: 40upx;
		margin-top: 16upx;
		background: #fff;

		.pay-item {
			display: flex;
			align-items: center;
			padding-right: 20upx;
			line-height: 1;
			height: 110upx;
			position: relative;
		}

		.icon-weixinzhifu {
			width: 80upx;
			font-size: 40upx;
			color: #6BCC03;
		}

		.icon-alipay {
			width: 80upx;
			font-size: 40upx;
			color: #06B4FD;
		}

		.icon-xuanzhong2 {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 60upx;
			height: 60upx;
			font-size: 40upx;
			color: $base-color;
		}

		.tit {
			font-size: 32upx;
			color: $font-color-dark;
			flex: 1;
		}
	}

	.footer {
		position: fixed;
		left: 0;
		bottom: 0;
		z-index: 995;
		display: flex;
		align-items: center;
		width: 100%;
		height: 90upx;
		justify-content: space-between;
		font-size: 30upx;
		background-color: #fff;
		z-index: 998;
		color: $font-color-base;
		box-shadow: 0 -1px 5px rgba(0, 0, 0, .1);

		.price-content {
			padding-left: 30upx;
		}

		.price-tip {
			color: $base-color;
			margin-left: 8upx;
		}

		.price {
			font-size: 36upx;
			color: $base-color;
		}

		.submit {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 280upx;
			height: 100%;
			color: #fff;
			font-size: 32upx;
			background-color: $base-color;
		}
	}

	/* 优惠券面板 */
	.mask {
		display: flex;
		align-items: flex-end;
		position: fixed;
		left: 0;
		top: var(--window-top);
		bottom: 0;
		width: 100%;
		background: rgba(0, 0, 0, 0);
		z-index: 9995;
		transition: .3s;

		.mask-content {
			width: 100%;
			min-height: 30vh;
			max-height: 70vh;
			background: #f3f3f3;
			transform: translateY(100%);
			transition: .3s;
			overflow-y: scroll;
		}

		&.none {
			display: none;
		}

		&.show {
			background: rgba(0, 0, 0, .4);

			.mask-content {
				transform: translateY(0);
			}
		}
	}

	/* 优惠券列表 */
	.coupon-item {
		display: flex;
		flex-direction: column;
		margin: 20upx 24upx;
		background: #fff;

		.con {
			display: flex;
			align-items: center;
			position: relative;
			height: 120upx;
			padding: 0 30upx;

			&:after {
				position: absolute;
				left: 0;
				bottom: 0;
				content: '';
				width: 100%;
				height: 0;
				border-bottom: 1px dashed #f3f3f3;
				transform: scaleY(50%);
			}
		}

		.left {
			display: flex;
			flex-direction: column;
			justify-content: center;
			flex: 1;
			overflow: hidden;
			height: 100upx;
		}

		.title {
			font-size: 32upx;
			color: $font-color-dark;
			margin-bottom: 10upx;
		}

		.time {
			font-size: 24upx;
			color: $font-color-light;
		}

		.right {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			font-size: 26upx;
			color: $font-color-base;
			height: 100upx;
		}

		.price {
			font-size: 44upx;
			color: $base-color;

			&:before {
				content: '￥';
				font-size: 34upx;
			}
		}

		.tips {
			font-size: 24upx;
			color: $font-color-light;
			line-height: 60upx;
			padding-left: 30upx;
		}

		.circle {
			position: absolute;
			left: -6upx;
			bottom: -10upx;
			z-index: 10;
			width: 20upx;
			height: 20upx;
			background: #f3f3f3;
			border-radius: 100px;

			&.r {
				left: auto;
				right: -6upx;
			}
		}
	}
</style>
