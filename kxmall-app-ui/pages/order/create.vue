<template>
	<view>
		<navigator url="/pages/address/list?source=1"  class="address">
			<view class="address-head">订单配送至</view>
			<view v-if="addressData.phone" class="address-main">
				<image src="../../static/order/address.png" class="address-left"></image>
				<view class="address-text">{{addressData.province}} {{addressData.city}} {{addressData.county}}
					{{addressData.address}}</view>
				<image src="../../static/order/right.png" class="address-right"></image>
			</view>
			<view class="padding-tb-sm" v-else>
				请选择配送地址
			</view>
			<!-- <view class="address-distance">距离您732米</view> -->
		</navigator>
		<image class="a-bg" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAu4AAAAFCAYAAAAaAWmiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6Rjk3RjkzMjM2NzMxMTFFOUI4RkU4OEZGMDcxQzgzOEYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6Rjk3RjkzMjQ2NzMxMTFFOUI4RkU4OEZGMDcxQzgzOEYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGOTdGOTMyMTY3MzExMUU5QjhGRTg4RkYwNzFDODM4RiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGOTdGOTMyMjY3MzExMUU5QjhGRTg4RkYwNzFDODM4RiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PrEOZlQAAAiuSURBVHjazJp7bFvV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"></image>
		<view class="goods">
			<view  class="goods-head flex">
				<view class="goods-head-text">送达时间</view>
				<view @click="selectTime" style="text-align: right;" class="goods-head-time text-cut padding-right-sm">{{orderReqeust.predictTime}}</view>
				<image src="../../static/order/time-right.png" class="goods-head-right"></image>
			</view>
			<view class="goods-one" v-if="orderReqeust.productList.length ==1">
				<image class="goods-img"
				:src="(orderReqeust.productList[0].productAttrImg?JSON.parse(orderReqeust.productList[0].productAttrImg)[0].url:JSON.parse(orderReqeust.productList[0].productImg)[0].url)"></image>
				<view class="goods-text">
					<view class="goods-name">{{orderReqeust.productList[0].productAttrName}}</view>
					<view class="goods-tag">
						<view class="goods-price">￥{{orderReqeust.productList[0].price}}</view>
						<view class="goods-yprice">￥{{orderReqeust.productList[0].otPrice}}</view>
						<view class="goods-number">×{{orderReqeust.productList[0].cartNum}}</view>
					</view>
				</view>
			</view>
			<view class="goods-one" v-else>
				<view class="scroll_box">
					<scroll-view  scroll-x="true">
						<block  v-for="(item, index) in orderReqeust.productList" :key="index">
							<image class="goods-more-img" :src="(item.productAttrImg?JSON.parse(item.productAttrImg)[0].url:JSON.parse(item.productImg)[0].url)"></image>
						</block>
					</scroll-view>
				</view>
				<view class="goods-more-number">共{{orderReqeust.totalNumber}}件</view>
				<image src="../../static/order/time-right.png" class="goods-more-right"></image>
			</view>
		</view>
		<view class="distribution">
			<view class="distribution-list distribution-list-bottom">
				<view class="distribution-text">配送费</view>
				<view class="distribution-price">0元</view>
			</view>
			<view class="distribution-list">
				<view class="distribution-text">备注</view>
				<view class="distribution-price"><input type="text" v-model="orderReqeust.mono" placeholder="请输入备注信息"></view>
			</view>
		</view>
		<view class="distribution">
			<view class="distribution-list distribution-list-bottom">
				<view class="distribution-text pay-text">支付方式</view>
			</view>
			<view class="distribution-list distribution-list-bottom">
				<view class="distribution-text">微信</view>
				<view class="distribution-price">
					<image src="../../static/order/xuanzhong.png"></image>
				</view>
			</view>
			<!-- <view class="distribution-list">
				<view class="distribution-text">支付宝</view>
				<view class="distribution-price">
					<image src="../../static/order/xuanzhong-no.png"></image>
				</view>
			</view> -->
		</view>
		<!-- 底部 -->
		<view class="footer">
			<view class="price">
				<view class="price-content"><text>总计</text><text class="price-content-money">￥{{(orderReqeust.totalPrice - (orderReqeust.coupon?orderReqeust.coupon.discount:0) + orderReqeust.freightPrice)}}</text></view>
				<view class="price-preferential"><text>已优惠：</text><text>{{(orderReqeust.totalotPrice - orderReqeust.totalPrice)}}元</text></view>
			</view>
			<view class="submit" @click="submit">
				去付款
			</view>
		</view>

		<uni-popup ref="popup" type="bottom" :animation="true" style="z-index: 9999;">
			<view class="flex align-center justify-between" style="padding: 34rpx 30rpx;background-color: #F2F2F2;">
				<view style="font-size: 32rpx;line-height: 38rpx;color: #1B1C33;">选择送达时间</view>
				<view @click="checkTime" style="font-size: 32rpx;line-height: 38rpx;color: #2AAB34;">确定</view>
			</view>
			<view class="flex justify-between">
				<view style="width: 347rpx;height: 500rpx;background-color: #F2F2F2;">
					<view v-for="(date,index) in dayList" :key="index" @click="chooseDay(index)"
					:class="[index == dayIndex?'bg-white':'']"
					style="padding: 25rpx 84rpx 25rpx 59rpx;font-size: 32rpx;line-height: 38rpx;color: #1B1C33;">
						{{date}}
					</view>
				</view>
				<view class="bg-white" style="width: 404rpx;height: 500rpx;padding-left: 52rpx;position: relative;">
					<scroll-view scroll-y="true" style="width: 404rpx;height: 500rpx;">
						<view v-for="(time,index) in timeList[dayIndex]" :key="index" @click="chooseTime(index)"
						:class="[index == timeIndex?'text-green':'']"
						class="solid-bottom" style="padding: 25rpx 0;font-size: 32rpx;line-height: 38rpx;">
							<text v-if="index == 0 && dayIndex == 0 && dayList.length >= 3">{{time}}</text>
							<text v-else>{{time[0]+'-'+time[1]}}</text>
						</view>
					</scroll-view>
					<view style="position: absolute;bottom: 0;width: 404rpx;height: 53rpx;background-color: rgba(255,255,255,0.8);">
					</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	import uniPopup from "@/components/uni-popup/uni-popup.vue"
	export default {
		data() {
			return {
				orderReqeust: {
					productList: [],
					totalotPrice: 0,
					totalPrice: 0, //商品折扣（仅算VIP和限时打折）后总价
					totalNumber:0,//商品件数
					coupon: undefined,
					couponId: 0,
					mono: '',
					takeWay: '',
					freightPrice: 0,
					addressId: undefined,
					predictTime:''
				},
				skuCategoryPriceMap: {},
				maskState: 0, //优惠券面板显示状态
				couponList: [],
				isVip: false,
				addressData: {
					consignee: '',
					phone: '',
					province: '',
					city: '',
					county: '',
					address: '',
					defaultAddress: false,
				},
				submiting: false,
				dayList:[],//配送时间
				dayIndex:0,//选中配送时间
				timeList:[],
				timeIndex:0,
				now:0,//当前小时
				nowTime:0,//当前分钟
				dateObj:{
					today:0,
					tomorrow:1,
					afterTomo:2
				}
			}
		},
		components: {uniPopup},
		onShow() {
			this.isVip = this.$api.isVip()
		},
		onLoad(option) {
			//商品数据
			this.isVip = this.$api.isVip()
			const that = this
			if (option.takeway) {
				that.orderReqeust.takeWay = option.takeway
			}
			that.orderReqeust.productList = that.$api.globalData.productList;
			let totalotPrice = 0
			let totalPrice = 0
			let totalNumber = 0
			let skuCategoryPriceMap = {}
			that.orderReqeust.productList.forEach(item => {
				totalotPrice += item.otPrice * item.cartNum
				totalNumber += item.cartNum
				totalPrice += that.isVip ? (item.vipPrice * item.num) : (item.price * item.cartNum)
				//构建category价格Map
				item.cateIdList.forEach(catItem => {
					if (skuCategoryPriceMap[catItem]) {
						skuCategoryPriceMap[catItem] += that.isVip ? (item.vipPrice * item.cartNum) : (item.price * item.cartNum)
					} else {
						skuCategoryPriceMap[catItem] = that.isVip ? (item.vipPrice) : (item.price)
					}
				})
			})
			that.skuCategoryPriceMap = skuCategoryPriceMap
			that.orderReqeust.totalotPrice = totalotPrice
			that.orderReqeust.totalNumber = totalNumber
			that.orderReqeust.totalPrice = totalPrice
			that.$api.request('get', 'address/app/getDefAddress').then(res => {
				if(res.data){
					that.addressData = res.data
				}
				that.calcFreightPrice()
			})

			if (that.orderReqeust.productList.length === 1 && that.orderReqeust.productList[0].groupShopId) {
				//若是团购商品，则携带上团购信息
				that.orderReqeust.groupShopId = that.orderReqeust.productList[0].groupShopId
			}
			this.calcTime()
		},
		methods: {
			calcFreightPrice() {
				const that = this
				if (that.addressData) {
					that.orderReqeust.addressId = that.addressData.id
				}
				that.orderReqeust.freightPrice = 0
			},
			numberChange(data) {
				this.number = data.number;
			},
			submit() {
				const that = this
				if (that.submiting) {
					return
				}
				if(!this.addressData.id){
					this.$api.msg('请选择配送地址')
					return
				}
				uni.showLoading({})
				that.submiting = true
				if (that.addressData.id) {
					that.orderReqeust.addressId = that.addressData.id
				}
				that.orderReqeust.storageId = that.$store.state.storageId
                that.orderReqeust.channel = uni.getSystemInfoSync().platform
				that.$api.request('post', 'order/app/takeOrder',
					JSON.stringify(that.orderReqeust)
				, failres => {
					that.submiting = false
					that.$api.msg(failres.msg)
				}).then(res => {
					//提交订单成功后，无需再让用户提交订单
					// that.submiting = false
					// uni.redirectTo({
					// 	url: '/pages/pay/pay?orderno=' + res.data + '&price=' + that.orderReqeust.totalPrice
					// })
					that.confirm(res.data)
				})

			},
			selectCoupon(couponItem) {
				this.orderReqeust.couponId = couponItem.id
				this.orderReqeust.coupon = couponItem
				this.maskState = 0
				this.calcFreightPrice()
			},
			stopPrevent() {},
			//打开选择时间的弹窗
			selectTime(){
				this.$refs.popup.open()
			},
			//根据当前仓库营业时段及当前时间计算可供选择的送达时间
			calcTime(){
				var deliveryStart = this.$store.state.storageObj.deliveryStartTime.split(':')
				var deliveryStop = this.$store.state.storageObj.deliveryStopTime.split(':')
				var startHour = parseInt(deliveryStart[0])
				var startMin = parseInt(deliveryStart[1])
				var stopHour = parseInt(deliveryStop[0])
				var stopMin = parseInt(deliveryStop[1])
				var date = new Date()
				var month = date.getMonth()+1>9?date.getMonth()+1:'0'+parseInt(date.getMonth()+1)
				var day = date.getDate()>9?date.getDate():'0'+date.getDate()
				var tomorrow = date.getDate()+1>9?date.getDate()+1:'0'+parseInt(date.getDate()+1)
				var afterTomo = date.getDate()+2>9?date.getDate()+2:'0'+parseInt(date.getDate()+2)

				this.dateObj.today = day
				this.dateObj.tomorrow = tomorrow
				this.dateObj.afterTomo = afterTomo
				this.dateObj.month = month

				this.dayList[0] = '今天'+month+'月'+day+'日'
				this.dayList[1] = '明天'+month+'月'+tomorrow+'日'
				this.dayList[2] = '后天'+month+'月'+afterTomo+'日'
				this.timeList[0] = ['尽快送达'],
				this.timeList[1] = []
				for (var i = 0; i < stopHour - startHour; i++) {
					var time = startHour+i>9?parseInt(startHour+i):'0'+parseInt(startHour+i)
					this.timeList[1].push([time+':00',time+':30'])
					this.timeList[1].push([time+':30',parseInt(parseInt(time)+1)+':00'])
				}
				if(startMin >= 30){//半点后配送，删除第一条记录
					this.timeList[1].splice(0,1)
				}
				//预计送达时间字符
				this.orderReqeust.predictTime = this.dayList[1]+this.timeList[1][0][0]+'-'+this.timeList[1][0][1]
				this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.tomorrow+' '+this.timeList[1][0][1]).getTime()
				this.timeList[2] = []
				for (var i = 0; i < stopHour - startHour; i++) {
					var time = startHour+i>9?parseInt(startHour+i):'0'+parseInt(startHour+i)
					this.timeList[2].push([time+':00',time+':30'])
					this.timeList[2].push([time+':30',parseInt(parseInt(time)+1)+':00'])
				}
				if(startMin >= 30){//半点后配送，删除第一条记录
					this.timeList[2].splice(0,1)
				}
				var now = date.getHours()
				if(now < startHour){
					now = startHour
				}
				var nowTime = date.getMinutes()
				this.nowTime = nowTime
				this.now = now
				if(now >= stopHour){
					this.dayList.splice(0,1)
					this.timeList.splice(0,1)
				}else{
					this.timeList[0] = ['尽快送达']
					for (var i = 0; i < stopHour - now; i++) {
						var time = now+i>9?now+i:'0'+parseInt(now+i)
						this.timeList[0].push([time+':00',time+':30'])
						this.timeList[0].push([time+':30',parseInt(now+i+1)+':00'])
					}
					if(nowTime > 30){
						this.timeList[0].splice(1,2)
						var timeMine = nowTime - 30 > 9 ?parseInt(nowTime - 30):'0'+parseInt(nowTime - 30)
						this.orderReqeust.predictTime = '尽快送达（预计'+parseInt(now+1)+':'+ timeMine+'送达)'
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+parseInt(now+1)+':'+ timeMine).getTime()
					}else if(nowTime < 29){
						this.orderReqeust.predictTime = '尽快送达（预计'+now+':'+ parseInt(nowTime+30)+'送达)'
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+now+':'+ parseInt(nowTime+30)).getTime()
					}else if(nowTime == 30){
						this.timeList[0].splice(1,1)
						this.orderReqeust.predictTime = '尽快送达（预计'+parseInt(now+1)+':00送达)'
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+parseInt(now+1)+':00').getTime()
					}
				}
			},
			chooseDay(index){
				this.dayIndex = index
			},
			chooseTime(index){
				this.timeIndex = index
			},
			checkTime(){
				//当天尽快送达
				if(this.dayIndex == 0 && this.timeIndex == 0 && this.dayList.length >= 3){
					if(this.nowTime > 30){
						var timeMine = this.nowTime - 30 > 9 ?parseInt(this.nowTime - 30):'0'+parseInt(this.nowTime - 30)
						this.orderReqeust.predictTime = '尽快送达（预计'+parseInt(this.now+1)+':'+ timeMine+'送达)'
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+parseInt(this.now+1)+':'+ timeMine).getTime()
					}else if(this.nowTime < 29){
						this.orderReqeust.predictTime = '尽快送达（预计'+this.now+':'+ parseInt(this.nowTime+30)+'送达)'
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+this.now+':'+ parseInt(this.nowTime+30)).getTime()
					}else if(this.nowTime == 30){
						this.orderReqeust.predictTime = '尽快送达（预计'+parseInt(this.now+1)+':00送达)'
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+parseInt(this.now+1)+':00').getTime()
					}
					this.$refs.popup.close()
					console.log(this.orderReqeust)
					return
				}
				//当天送达
				if(this.dayIndex == 0 && this.dayList.length >= 3){
					this.orderReqeust.predictTime = this.timeList[0][this.timeIndex][0]+'-'+this.timeList[0][this.timeIndex][1]
					console.log('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+this.timeList[this.dayIndex][this.timeIndex][1])
					this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.today+' '+this.timeList[this.dayIndex][this.timeIndex][1]).getTime()
					this.$refs.popup.close()
					console.log(this.orderReqeust)
					return
				}
				//明、后天送达
				this.orderReqeust.predictTime = this.dayList[this.dayIndex]+this.timeList[this.dayIndex][this.timeIndex][0]+'-'+this.timeList[this.dayIndex][this.timeIndex][1]
				if(this.dayList.length >= 3){
					if(this.dayIndex == 1){//明天
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.tomorrow+' '+this.timeList[this.dayIndex][this.timeIndex][1]).getTime()
					}else if(this.dayIndex == 2){//后天
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.afterTomo+' '+this.timeList[this.dayIndex][this.timeIndex][1]).getTime()
					}
				}else if(this.dayList.length >= 2){
					if(this.dayIndex == 0){//明天
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.tomorrow+' '+this.timeList[this.dayIndex][this.timeIndex][1]).getTime()
					}else if(this.dayIndex == 1){//后天
						this.orderReqeust.gmtPredict = new Date('2020-'+this.dateObj.month+'-'+this.dateObj.afterTomo+' '+this.timeList[this.dayIndex][this.timeIndex][1]).getTime()
					}
				}
				this.$refs.popup.close()
				console.log(this.orderReqeust)
			},
			//确认支付
			confirm(orderNo,totalPrice) {
        // ============微信支付代码 prod 开始============
				// // #ifdef APP-PLUS
				// this.$api.msg('演示环境不支持支付')
				// return
				// // #endif
				// const that = this
				// that.$api.request('post', 'order/app/wxPrepay', {
				// 	orderNo : orderNo
				// }, failres => {
				// 	that.submiting = false
				// 	that.$api.msg(failres.msg)
				// }).then(prepayRes => {
				// 	uni.hideLoading()
				// 	that.submiting = false
				// 	//#ifdef MP-WEIXIN
				// 	const payParam = {
				// 		appId: prepayRes.data.appId,
				// 		nonceStr: prepayRes.data.nonceStr,
				// 		package: prepayRes.data.packageValue,
				// 		timeStamp: prepayRes.data.timeStamp,
				// 		signType: prepayRes.data.signType,
				// 		paySign: prepayRes.data.paySign,
				// 	}
				// 	//#endif
				// 	//#ifdef APP-PLUS
				// 	const payParam = {
				// 		appid: prepayRes.data.appId,
				// 		noncestr: prepayRes.data.nonceStr,
				// 		package: prepayRes.data.packageValue,
				// 		partnerid: prepayRes.data.partnerId,
				// 		prepayid: prepayRes.data.prepayId,
				// 		timestamp: parseInt(prepayRes.data.timeStamp),
				// 		sign: prepayRes.data.sign,
				// 		signType: 'MD5'
				// 	}
				// 	//#endif
				// 	//#ifdef MP-WEIXIN || APP-PLUS
				// 	uni.requestPayment({
				// 		provider: 'wxpay',
				// 		//#ifdef MP-WEIXIN
				// 		...payParam,
				// 		//#endif
				// 		//#ifdef APP-PLUS
				// 		orderInfo: payParam,
				// 		//#endif
				// 		success: function(res) {
				// 			uni.redirectTo({
				// 				url: '/pages/pay/success'
				// 			})
				// 		},
				// 		fail: function(res) {
				// 			// console.log("支付过程失败");
				// 			// that.$api.msg(JSON.stringify(res))
				// 		},
				// 		complete: function(res) {
				// 			console.log("支付过程结束")
				// 		}
				// 	});
				// 	//#endif
				// 	//#ifdef H5
				// 	uni.redirectTo({
				// 		url: '/pages/pay/success'
				// 	})
				// 	// that.$jweixin.chooseWXPay({
				// 	// 	nonceStr: prepayRes.data.nonceStr,
				// 	// 	timestamp: prepayRes.data.timeStamp,
				// 	// 	package: prepayRes.data.packageValue,
				// 	// 	signType: prepayRes.data.signType,
				// 	// 	paySign: prepayRes.data.paySign,
				// 	// 	success: (e) => {
				// 	// 		//支付成功
				// 	// 		uni.redirectTo({
				// 	// 			url: '/pages/pay/success'
				// 	// 		})
				// 	// 	},
				// 	// 	fail: function(res) {
				// 	// 		console.log("支付过程失败");
				// 	// 		that.$api.msg(JSON.stringify(res))
				// 	// 	},
				// 	// 	complete: function(res) {
				// 	// 		console.log("支付过程结束")
				// 	// 	}
				// 	// })
				// 	//#endif
				// })
        //============微信支付代码 prod 结束============
        //============微信支付代码 dev 开始============
				uni.request({
					url: this.$api.defConfig().baseUrl + '/cb/wxpay',
					data: {
						outTradeNo: orderNo,
						transactionId:'test',
						totalFee:totalPrice
					},
					method: 'POST',
					header: {
						'Content-Type': 'application/json; charset=UTF-8'
					},
					success: (res) => {
						//代码回调-上线后注释代码-结束
						uni.redirectTo({
							url: '/pages/pay/success'
						})
					}
				});
        //============微信支付代码 dev 结束============
			}
		}
	}
</script>

<style  lang="scss">
	page {
		background: #F4F4F4;
		padding-bottom: 100upx;
	}
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
	.address {
		padding: 36upx 30upx;
		background-color: #FFFFFF;
		overflow: hidden;
	}

	.address-head {
		color: #999999;
		font-size: 28upx;
		font-weight: 400;
	}

	.address-main {
		margin-top: 24upx;
	}

	.address-left {
		margin: 9upx 0;
		width: 24upx;
		height: 32upx;
		float: left;
	}

	.address-text {
		margin-left: 14upx;
		float: left;
		font-size: 36upx;
		color: #333333;
		font-weight: 500;
	}

	.address-right {
		margin: 11upx 20upx 11upx 0;
		width: 28upx;
		height: 28upx;
		float: right;
	}

	.address-distance {
		clear: both;
		margin-top: 10upx;
		color: #333333;
		font-size: 30upx;
	}

	.a-bg {
		display: block;
		width: 100%;
		height: 5upx;
	}

	.goods {
		margin-top: 20upx;
		background-color: #FFFFFF;
		overflow: hidden;
	}

	.goods-head {
		margin: 0 30upx;
		height: 100upx;
		padding: 30upx 0;
		border-bottom: 2upx solid #F1F1F1;
	}

	.goods-head-text {
		width: 294upx;
		float: left;
		color: #333333;
		font-size: 28upx;
	}

	.goods-head-time {
		width: 490upx;
		float: left;
		color: #2AAC34;
		font-size: 28upx;
	}

	.goods-head-right {
		margin-top: 8upx;
		width: 16upx;
		height: 24upx;
		float: left;
	}

	.goods-one {
		padding: 40upx 0 30upx 0;
		margin: 0 30upx;
		height: 190upx;
	}
	.scroll_box{
		width: 544upx;
		height: 110upx;
		float: left;
		overflow:hidden;
	}
	.scroll_box scroll-view{
	  height: 110upx;
	  width: 544upx;
	  white-space: nowrap;
	}
	.goods-img {
		margin-top: 2upx;
		width: 118upx;
		height: 110upx;
		float: left;
	}
	.goods-more-img{
		margin-top: 2upx;
		width: 118upx;
		height: 110upx;
		display: inline-block;
		margin-right: 36upx;
	}
	.goods-more-number{
		width: 126upx;
		height: 110upx;
		line-height: 110upx;
		text-align: center;
		color: #1B1C33;
		font-size: 28upx;
		float: left;
	}
	.goods-more-right{
		margin-top: 43.5upx;
		width: 15upx;
		height: 23upx;
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
		float: right;
		color: #333333;
		font-size: 32upx;
	}

	.distribution {
		padding: 0 30upx;
		margin-top: 20upx;
		background-color: #FFFFFF;
		overflow: hidden;
	}

	.distribution-list {
		height: 100upx;
		line-height: 100upx;
		font-size: 28upx;
		clear: both;

	}

	.distribution-list-bottom {
		border-bottom: 2upx solid #F1F1F1;
	}

	.distribution-text {
		width: 200upx;
		float: left;
		color: #333333;
	}

	.distribution-price {
		width: 490upx;
		float: left;
		text-align: right;
		color: #666666;
	}

	.distribution-price input {
		width: 490upx;
		height: 100upx;
		line-height: 100upx;
		overflow: hidden;
		text-align: right;
	}

	.pay-text {
		color: #333333;
		font-size: 32upx;
	}

	.distribution-price image {
		width: 40upx;
		height: 40upx;
		float: right;
		margin-top: 30upx;
	}

	.footer {
		position: fixed;
		left: 0;
		bottom: 0;
		z-index: 99;
		height: 98upx;
		width: 100%;
		background-color: #FFFFFF;
	}
	.price{
		margin-left: 62upx;
		width: 448upx;
		float: left;
	}
	.price-content{
		height: 50upx;
		line-height: 50upx;
		margin-top: 10upx;
		color: #666666;
		font-size: 26upx;
	}
	.price-content-money{
		color: #2AAC34;
		font-size: 36upx;
	}
	.price-preferential{
		height: 36upx;
		line-height: 36upx;
		color: #AEAEAE;
		font-size: 20upx;
	}
	.submit{
		width: 240upx;
		float: left;
		line-height: 98upx;
		color: #FFFFFF;
		background-color: #2AAC34;
		text-align: center;
	}
</style>
