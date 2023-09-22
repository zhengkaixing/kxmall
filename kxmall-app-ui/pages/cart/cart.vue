<template>
<view>
	<view v-if="storage">
		<view  class="container">
			<!-- 空白页 -->
			<view v-if="!hasLogin || empty===true" >
				<view v-if="hasLogin" style="padding-top: 180rpx;padding-bottom: 180rpx;width: 750rpx;">
					<missing :buttonName="'去添加商品'" :handlerName="'buttonClick'"
					@buttonClick="toCategory"
					:imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%201%20%E6%8B%B7%E8%B4%9D%403x.png'"
					:desc="'购物车空空如也，您不添加吗？'"></missing>
				</view>
				<view v-else style="padding-top: 180rpx;padding-bottom: 180rpx;width: 750rpx;">
					<missing :buttonName="'去登录'" :handlerName="'buttonClick'"
					@buttonClick="navToLogin"
					:imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%201%20%E6%8B%B7%E8%B4%9D%403x.png'"
					:desc="'购物车空空如也，您不添加吗？'"></missing>
				</view>
			</view>
			<view v-else>
				<!-- 列表 -->
				<view class="cart-list bg-white padding-lr">
					<block v-for="(item, index) in cartList" :key="item.id">
						<view :class="[index+1 < cartList.length?'solid-bottom':'']" class="padding-tb  flex align-center">
							<view v-if="item.stockless" style="color: #CFCFCF;font-size: 28rpx;line-height: 40rpx;flex-shrink: 0;">失效</view>
							<image v-else @click="check('item', index)"
							:src="item.checked ?'/static/cart/selected.png':'/static/cart/select.png'"
							mode="aspectFill"
							style="width: 40rpx;height: 40rpx;flex-shrink: 0;"></image>
							<view class="" style="padding-left: 52rpx;flex-shrink: 0;">
								<image :src="(item.productAttrImg?JSON.parse(item.productAttrImg)[0].url:JSON.parse(item.productImg)[0].url)"
								mode="aspectFill" lazy-load
								@load="onImageLoad(item)"
								class="round"
								@error="onImageError('cartList', index)"
								style="width: 130rpx;height: 130rpx;"></image>
							</view>
							<view class="flex justify-between flex-direction"
							style="padding-left: 48rpx;height: 180rpx;flex-grow: 1;">
								<view class="flex justify-between align-start">
									<view style="width: 400rpx;" class="lem-text-black lem-text-xxl text-2-cut">{{item.productName}}</view>
									<image src="../../static/cart/delete.png" mode="aspectFill"
									@click="deleteCartItem(index)"
									style="width: 32rpx;height: 32rpx;margin-top: 15rpx;"></image>
								</view>
								<view class="flex justify-between align-end" >
									<view class="flex align-center">
										<view style="font-weight: 500;color: #FC6620;"
										class=" lem-text-xxl padding-right-sm"> ￥ {{item.price}}</view>
										<view style="color: #AEAEAE;font-size: 24rpx;line-height: 44rpx;padding: 0 10rpx;text-decoration: line-through;">
											 ￥{{item.otPrice}}
										</view>
									</view>
									<uni-number-box
										class="number-box"
										:min="1"
										:max="item.stock"
										:value="item.cartNum"
										:isMin="item.cartNum===0"
										:index="index"
										:forbid="item.activityId"
										@eventChange="numberChange"
									></uni-number-box>
								</view>
							</view>
						</view>
					</block>
				</view>
				<view v-if="stockLessList.length > 0" class="cart-list  padding-right butouming" style="border-left: #CFCFCF solid 30rpx;">
					<view class="flex align-center justify-center" style="padding: 40rpx 0 6rpx 0;">
						<view style="font-size: 28rpx;line-height: 40rpx;color: #666666;"
						>以下商品因库存不足已失效</view>
					</view>
					<block class="bg-white" v-for="(item, index) in stockLessList" :key="item.id">
						<view  class="padding-tb solid-bottom flex align-center">
							<view class="" style="color: #CFCFCF;font-size: 28rpx;line-height: 40rpx;flex-shrink: 0;">失效</view>
							<view class="" style="padding-left: 52rpx;flex-shrink: 0;">
								<image :src="(item.skuImg?item.skuImg:item.spuImg)"
								mode="aspectFit" lazy-load
								@load="onImageLoad(item)"
								class="round"
								@error="onImageError('cartList', index)"
								style="width: 130rpx;height: 130rpx;"></image>
							</view>
							<view class="flex justify-between flex-direction"
							style="padding-left: 48rpx;height: 180rpx;flex-grow: 1;">
								<view class="lem-text-black lem-text-xxl text-2-cut">{{item.title}}</view>
								<view class="flex justify-between align-end" >
									<view style="font-weight: 500;color: #FC6620"
									class="lem-text-xxl"> ￥ {{item.price}}</view>
									<uni-number-box
										class="number-box"
										:min="1"
										:value="item.cartNum"
										:isMin="item.cartNum===1"
										:index="index"
										@eventChange="numberChange"
									></uni-number-box>
								</view>
							</view>
						</view>
					</block>
					<view class="flex align-center justify-center"
					style="padding: 30rpx 0;">
						<button @click="clearStockLess" class="lem-btn line-gray round">清空已失效商品</button>
					</view>
				</view>
        <view class="bg-white flex justify-between align-center padding-tb-xs padding-lr submit-class">
					<view @click="cancelAllAdd" class="flex align-center">
						<image
						:src="allChecked ?'/static/cart/selected.png':'/static/cart/select.png'"
						mode="aspectFit"
						style="width: 40rpx;height: 40rpx;flex-shrink: 0;"></image>
						<view class="lem-text-grey"
						style="padding-left: 16rpx;font-size: 26rpx;line-height: 36rpx;">全选</view>
					</view>
					<view style="padding-left: 54rpx;">
						<view class="flex align-center">
							<view class="lem-text-grey lem-text-lg">总计</view>
							<view class="padding-left-xs lem-text-title"
							style="color: #FC6620;font-weight: 500;">￥ {{total}}</view>
						</view>
						<view style="font-size: 20rpx;line-height: 36rpx;" class="lem-text-gray"
						>已优惠：{{(yuanjia-total)}}元</view>
					</view>
					<button @click="createOrder" style="width: 240rpx;height: 84rpx;font-weight: 500;"
					class="lem-btn round bg-green lem-text-xl ">提交订单</button>
				</view>
			</view>
		</view>

	</view>
	<view v-else style="padding-top: 180rpx;padding-bottom: 180rpx;width: 750rpx;">
		<missing :buttonName="'换个地址试试吧~'" :handlerName="'buttonClick'"
		@buttonClick="chooseLocation"
		:imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%204%20%E6%8B%B7%E8%B4%9D@3x.png'"
		:desc="'当前地区不在配送范围哦'"></missing>
	</view>
</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import uniNumberBox from '@/components/uni-number-box.vue'
	import missing from "@/components/missing.vue"
	export default {
		components: {
			uniNumberBox,missing
		},
		data() {
			return {
				totalItems: 0, //总数量
				total: 0, //总价格
				allChecked: false, //全选状态  true|false
				empty: false, //空白页现实  true|false
				cartList: [],
				storage:true,
				loadedItemIds: new Set(),
				yuanjia:0,//商品原价
				stockLessList:[],
				stockLessIds:''
			};
		},
		onLoad(){

		},
		onShow() {
			this.$store.state.storageId ? this.storage = true : this.storage = false
			this.loadData();
			//如果用户已登录，获取购物车数量
			if(this.$store.state.userInfo.accessToken){
				this.countTabNum()
			}
		},
		watch:{
			//显示空白页
			cartList(e){
				if(e.length == 0){
					this.total = 0
					this.yuanjia = 0
					this.empty = true
				}
			}
		},
		computed:{
			...mapState(['hasLogin'])
		},
		methods: {
			//请求数据
			async loadData(){
				const that = this
				that.$api.request('get', 'cart/app/getCartList',{
					storageId:this.$store.state.storageId
				}).then(res => {
					//修改为for i循环，以找出库存不足的商品索引并去掉，存入库存不足商品组
					// var stockless = [];//库存不足商品索引
					var lessIds = []
					for (var i = 0; i < res.data.length; i++) {
						if(res.data[i].stock <= 0){
							// stockless.push(i)
							res.data[i].stockless = true
							lessIds.push(res.data[i].id)
						}else{
							res.data[i].checked = true
						}
					}
					// console.log('__________________________________')
					// console.log(stockless)
					// for (var i = 0; i < stockless.length; i++) {
					// 	that.stockLessList.push(res.data[stockless[i]])
					// 	res.data.splice(stockless[i],1)
					// }
					// stockless.forEach(item => {
					// 	that.stockLessList.push(res.data[item])
					// })

					that.cartList = res.data
					if(that.cartList){
						this.empty = false
					}
					that.calcTotal();  //计算总价
					this.stockLessIds = lessIds.join(',')
				})
			},
			countTabNum(){
				this.$api.request('get','cart/app/countCart',{
					storageId:this.$store.state.storageId
				}).then(res=>{
					if(res.data > 0){
						uni.setTabBarBadge({
							index:2,
							text:res.data+''
						})
					}else if(res.data <= 0){
						uni.removeTabBarBadge({
							index:2
						})
					}
					this.$store.commit('addCart',res.data)
				}).catch(err=>{
					// this.$api.msg('请求失败，请稍后再试')
				})
			},
			//监听image加载完成
			onImageLoad(item) {
				this.loadedItemIds.add(item.id)
				this.$forceUpdate()
			},
			//监听image加载失败
			onImageError(key, index) {
				this[key][index].skuImg = '/static/errorImage.jpg';
			},
			navToLogin(){
				uni.navigateTo({
					url: '/pages/public/login'
				})
			},
			 //选中状态处理
			check(type, index){
				if(type === 'item'){
					this.cartList[index].checked = !this.cartList[index].checked;
				}else{
					const checked = !this.allChecked
					const list = this.cartList;
					list.forEach(item=>{
						item.checked = checked;
					})
					this.allChecked = checked;
				}
				this.calcTotal(type);
			},
			//数量
			numberChange(data){
				const that = this
				if(data.number > that.cartList[data.index].stock){
					this.$api.msg('库存不足')
					return
				}
				if(that.cartList[data.index].activityId && that.cartList[data.index].activityId !== 0 ){
					this.$api.msg('抢购商品只允许购买一件')
					that.cartList[data.index].cartNum = 1
					return
				}
				if(that.cartList[data.index].couponId && that.cartList[data.index].couponId !== 0){
					this.$api.msg('抢购商品只允许购买一件')
					that.cartList[data.index].cartNum = 1
					return
				}
				if(data.number > that.cartList[data.index].cartNum){
					var cartNum = this.$store.state.cartNum+1
					that.$store.commit('addCart',cartNum)
					uni.setTabBarBadge({
						index:2,
						text:cartNum+''
					})
				}else if(data.number < that.cartList[data.index].cartNum){
					var cartNum = this.$store.state.cartNum-1
					that.$store.commit('addCart',cartNum)
					uni.setTabBarBadge({
						index:2,
						text:cartNum+''
					})
				}
				if(data.number == 0){
					this.deleteCartItem(data.index)
					return
				}

				that.$api.request('get','cart/app/updateCartItemNum', {
					cartId: that.cartList[data.index].id,
					num: data.number
				}, failres => {
					uni.showToast({
						title: failres.msg,
						icon: 'none'
					});
					that.cartList[data.index].cartNum = that.cartList[data.index].cartNum
				}).then(res => {
					that.cartList[data.index].cartNum = data.number;
					that.calcTotal();
				})
			},
			//取消全选
			cancelAllAdd(){
				const checked = !this.allChecked
				const list = this.cartList;
				list.forEach(item=>{
					item.checked = checked;
				})
				this.allChecked = checked;
				this.calcTotal();
			},
			//删除
			deleteCartItem(index){
				const that = this
				that.$api.request('get', 'cart/app/removeCartItem', {
					cartId: that.cartList[index].id
				}).then(res => {
					that.cartList.splice(index, 1);
					that.calcTotal();
					this.countTabNum()
					//uni.hideLoading();
				})
			},
			//清空
			clearCart(){
				const that = this
				uni.showModal({
					content: '清空购物车？',
					success: (e)=>{
						if(e.confirm){
							that.$api.request('get','removeCartAll').then(res => {
								that.cartList = []
							})
						}
					}
				})
			},
			//清空失效商品
			clearStockLess(){
				that.$api.request('get', 'removeCartItemBatch', {
					cartIdList: this.stockLessList
				}).then(res => {
					this.stockLessIds = ''
					this.stockLessList = []
					// that.calcTotal();
					this.countTabNum()
					//uni.hideLoading();
				})
			},
			//计算总价
			calcTotal(){
				const that = this
				let list = that.cartList;
				if(list.length === 0){
					// that.empty = true;
					return;
				}
				let total = 0;
				let totalItems = 0;
				that.yuanjia = 0
				let checked = true;
				list.forEach(item=>{
					console.log(item)
					if(item.checked === true){
						totalItems += item.cartNum
						total += (that.isVip ? item.vipPrice : item.price) * item.cartNum;
						that.yuanjia += Number((item.otPrice*item.cartNum).toFixed(2))
					}else if(checked === true){
						checked = false;
					}
				})
				console.log('原价'+that.yuanjia)
				this.allChecked = checked;
				this.total = Number(total.toFixed(2));
				this.totalItems = totalItems
			},
			//创建订单
			createOrder(){
				//滤除未被选择的item
				let selectedItems = []
				this.cartList.forEach(item => {
					if (item.checked) {
						selectedItems.push(item)
					}
				})
				if (selectedItems.length === 0) {
					this.$api.msg('您没有选中任何商品')
					return
				}
				this.$api.globalData.productList = selectedItems
				uni.navigateTo({
					url: `/pages/order/create?takeway=cart`
				})
			},
			toCategory(){
				uni.switchTab({
					url:"../category/category"
				})
			},
			//配送外区域选择区域
			chooseLocation(){
				uni.chooseLocation({
				    success: (res1)=> {
						console.log(res1)
						this.district = res1.name
						uni.showLoading({
							title:"加载中..."
						})
						this.$api.request('get','storage/position/getRecentlyStorage',{
							lng:res1.longitude,
							lat:res1.latitude
						},failres => {
							uni.hideLoading()
								this.logining = false
								this.$api.msg(failres.msg)
								this.$store.commit('setStorage',22)
								this.loadData(22)
								if(!22){
									this.storage ? this.storage = false : this.storage = true
								}else{
									this.loadRecommand('refresh')
								}
						}).then(res=>{
							uni.hideLoading()
							console.log(res)
							// res.data.id = 11
							this.$store.commit('setStorage',res.data.id)
							this.newTop = []
							this.cheapRecommend = []
							this.salesTop = []
							this.loadData(res.data.id)
							if(!res.data.id){
								this.storage = false
							}else{
								this.storage ? this.storage = false : this.storage = true
								// this.loadRecommand('refresh')
							}
						})
				    }
				});
			}
		}
	}
</script>

<style lang='scss'>
	.number-box{
		width: 146rpx;
		height: 46rpx;
	}
  .submit-class {
    position: fixed;
    /* #ifdef H5 */
    bottom: 50px;
    /* #endif */
    /* #ifdef MP-WEIXIN */
    bottom: 0px;
    /* #endif */
    width: 750rpx;
    z-index: 99999;
  }
	.butouming{
		opacity:0.5;
	}

	.container{
		padding-bottom: 134upx;
		/* 空白页 */
		.empty{
			position:fixed;
			left: 0;
			top:0;
			width: 100%;
			height: 100vh;
			padding-bottom:100upx;
			display:flex;
			justify-content: center;
			flex-direction: column;
			align-items:center;
			background: #fff;
			image{
				width: 240upx;
				height: 160upx;
				margin-bottom:30upx;
			}
			.empty-tips{
				display:flex;
				font-size: $font-sm+2upx;
				color: $font-color-disabled;
				.navigator{
					color: $uni-color-primary;
					margin-left: 16upx;
				}
			}
		}
	}
	/* 购物车列表项 */
	.cart-item{
		display:flex;
		position:relative;
		padding:30upx 40upx;
		.image-wrapper{
			width: 230upx;
			height: 230upx;
			flex-shrink: 0;
			position:relative;
			image{
				border-radius:8upx;
			}
		}
		.checkbox{
			position:absolute;
			left:-16upx;
			top: -16upx;
			z-index: 8;
			font-size: 44upx;
			line-height: 1;
			padding: 4upx;
			color: $font-color-disabled;
			background:#fff;
			border-radius: 50px;
		}
		.item-right{
			display:flex;
			flex-direction: column;
			flex: 1;
			overflow: hidden;
			position:relative;
			padding-left: 30upx;
			.title,.price{
				font-size:$font-base + 2upx;
				color: $font-color-dark;
				height: 40upx;
				line-height: 40upx;
			}
			.attr{
				font-size: $font-sm + 2upx;
				color: $font-color-light;
				height: 50upx;
				line-height: 50upx;
			}
			.price{
				height: 50upx;
				line-height:50upx;
			}
		}
		.del-btn{
			padding:4upx 10upx;
			font-size:34upx;
			height: 50upx;
			color: $font-color-light;
		}
	}
	/* 底部栏 */
	.action-section{
		/* #ifdef H5 */
		margin-bottom:100upx;
		/* #endif */
		position:fixed;
		left: 30upx;
		bottom:30upx;
		z-index: 95;
		display: flex;
		align-items: center;
		width: 690upx;
		height: 100upx;
		padding: 0 30upx;
		background: rgba(255,255,255,.9);
		box-shadow: 0 0 20upx 0 rgba(0,0,0,.5);
		border-radius: 16upx;
		.checkbox{
			height:52upx;
			position:relative;
			image{
				width: 52upx;
				height: 100%;
				position:relative;
				z-index: 5;
			}
		}
		.clear-btn{
			position:absolute;
			left: 26upx;
			top: 0;
			z-index: 4;
			width: 0;
			height: 52upx;
			line-height: 52upx;
			padding-left: 38upx;
			font-size: $font-base;
			color: #fff;
			background: $font-color-disabled;
			border-radius:0 50px 50px 0;
			opacity: 0;
			transition: .2s;
			&.show{
				opacity: 1;
				width: 120upx;
			}
		}
		.total-box{
			flex: 1;
			display:flex;
			flex-direction: column;
			text-align:right;
			padding-right: 40upx;
			.price{
				font-size: $font-lg;
				color: $font-color-dark;
			}
			.coupon{
				font-size: $font-sm;
				color: $font-color-light;
				text{
					color: $font-color-dark;
				}
			}
		}
		.confirm-btn{
			padding: 0 38upx;
			margin: 0;
			border-radius: 100px;
			height: 76upx;
			line-height: 76upx;
			font-size: $font-base + 2upx;
			background: $uni-color-primary;
			box-shadow: 1px 2px 5px rgba(217, 60, 93, 0.72)
		}
	}
	/* 复选框选中状态 */
	.action-section .checkbox.checked,
	.cart-item .checkbox.checked{
		color: $uni-color-primary;
	}
</style>
