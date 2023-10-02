<template>
	<view >
		<view id="top"
		style="background-image: url(https://kxmalls.oss-cn-hangzhou.aliyuncs.com/bg/top.png);background-repeat: no-repeat; background-size: cover; width: 750rpx;position: fixed;top: 0;z-index: 999;">
			<view class="status-bar"></view>
			<view class="flex align-center justify-center text-white"
			style="font-size: 36rpx;font-weight: 500; position: absolute;width: 750rpx;"
			:style="'top:'+navbarTop+'px;height:'+navHeight+'px;'"
			 >
				商品品类
			</view>
			<view :style="'margin-top:'+MT2+'px;'"
			 class="padding-lr" style="padding-bottom: 22rpx;padding-top: 24rpx;" >
				<view  @click="naviageToPage('/pages/product/search')" style="height: 64rpx;" class="bg-white round flex align-center">
					<view class="flex align-center padding-left">
						<text class="yticon icon-sousuo" style="margin-right: 10rpx;"></text><text class="lem-text-gray"
						style="font-size: 26rpx;">新鲜，搜一下就涞了</text>
					</view>
				</view>
			</view>
		</view>
		<view :style="'height: '+topHeight+'px;'"
		style="width: 750rpx;display: flex; position: relative;"></view>
		<view v-if="storage" class="bg-white">
			<view style="position: fixed;left:0;"
			:style="'top:'+topHeight+'px;'">
				<scroll-view scroll-y  class="left-aside" :style="'height:'+scollHeight+'px;'">
					<view v-for="item in flist" :key="item.id"
					class="f-item " :class="{active: item.id === currentId}" @click="tabtap(item)">
						{{item.cateName}}
					</view>
				</scroll-view>
			</view>
			<view @click="showCover" class="flex align-center justify-center"
			:style="'top:'+topHeight+'px;'"
			style="background-color: rgba(255,255,255,0.9);margin: 24rpx 0;box-sizing: content-box; position: fixed; right: 0;z-index: 999;height: 50rpx;width: 70rpx;padding-right: 10rpx;">
				<image v-if="!cover" src="../../static/category/arrow_down.png" mode="aspectFit"
				style="width: 22rpx;height: 14rpx;"></image>
				<image v-if="cover" src="../../static/category/arrow_up.png" mode="aspectFit"
				style="width: 22rpx;height: 14rpx;"></image>
			</view>
			<scroll-view scroll-with-animation scroll-x :scroll-into-view="curentTabView"
			class="bg-white"
			style="width: 600rpx;right:0;position: fixed; height: 100rpx;overflow-x: scroll;"
			:style="'top:'+topHeight+'px;'">
				<view class="flex  align-start padding-left-sm">
					<view v-for="item in slist" :key="item.id"
					@click="sTabTap(item)" :id="'id'+item.id"
					:class="{actived: item.id === currentIded}"
					class="flex align-center justify-center margin-left-sm"
					style="padding: 20rpx 0; box-sizing: border-box;width: auto;flex-shrink: 0;"
					>
						<view style="font-size: 28rpx;line-height: 40rpx;"
						>{{item.cateName}}</view>
					</view>
					<view
					class="flex align-center justify-center margin-left-sm"
					style="padding: 20rpx 0; box-sizing: border-box;width: auto;flex-shrink: 0;width: 70rpx;"
					>
						<view style="font-size: 28rpx;line-height: 40rpx;"
						></view>
					</view>
				</view>
			</scroll-view>
			<view v-if="cover" style="z-index: 9999; position: fixed;right: 0;width: 600rpx;margin-top: 90rpx;background-color: rgba(255,255,255,0.9);"
			:style="'top:'+topHeight+'px;height:'+scollHeight+'px;'">
				<view class="bg-white flex flex-wrap padding-bottom padding-top-sm solid-bottom padding-left-xs">
					<view v-for="item in slist" :key="item.id" @click="sTabTap(item)"
					class="lem-btn round margin-bottom-sm margin-lr-xs" style="width: 28%;"
					:class="{'line-green': item.id === currentIded}">{{item.cateName}}</view>
				</view>
			</view>
			<view :style="'top:'+topHeight+'px;'"
			 style="padding-left: 140rpx;padding-top: 90rpx;">
				<image src="https://s1.ax1x.com/2020/04/07/G2uavD.png" mode="aspectFit"
				style="margin: 30rpx;width: 550rpx;height: 160rpx;border-radius: 15rpx;"></image>
				<view style="padding:0rpx 20rpx 30rpx 20rpx;">
					{{sTitle}}
				</view>
				<view v-for="(item,index) in tlist" :key="index"
				@click="navToDetail(item.id)"
				class="margin-lr padding-tb-sm padding-lr-xs flex"
				style="width: 558rpx;height: 180rpx;border-bottom: solid #E6E6E6 1rpx;">
					<image :src="JSON.parse(item.image)[0].url" mode="aspectFill" class="round"
					style="width: 144rpx;height: 144rpx;margin-right: 50rpx;"></image>
					<view style="padding-top: 4rpx;padding-bottom: 2rpx;flex-grow: 1;">
						<view class="text-black padding-bottom-xs text-cut"
						style="font-size: 30rpx;line-height: 42rpx;width: 300rpx;"
						>{{item.storeName}}</view>
						<view class="text-gray text-cut"
						style="font-size: 24rpx;line-height: 34rpx;width: 300rpx;"
						>{{item.description}}</view>
						<view class="flex justify-between" style="width: 100%;padding-top: 12rpx;">
							<view style="flex-grow: 1;" class="flex align-center">
								<view class="text-black padding-right-sm"
								style="font-size: 30rpx;line-height: 42rpx;"
								>￥{{item.kxStockVo.price}}</view>
								<!-- <view style="border: solid #FC6620 2rpx;color: #FC6620;border-radius: 4rpx;width: 80rpx;height: 30rpx;font-size: 20rpx;line-height: auto;text-align: center;">
									专享价
								</view> -->
								<view style="color: #AEAEAE;font-size: 24rpx;line-height: 44rpx;padding: 0 10rpx;text-decoration: line-through;">
									 ￥{{item.otPrice}}
								</view>
							</view>
              <view v-if="item.kxStockVo.stock===0" style="float: right;color: #AEAEAE;font-size: 24rpx;line-height: 44rpx;padding: 0 10rpx;">
                即将到货
              </view>
              <uni-number-box
                  class="number-box"
                  :min="0"
                  :value="item.num"
                  :isMin="item.num==0"
                  :index="index"
                  @eventChange="numberChange"
                  v-if="item.num!=0"
              ></uni-number-box>
              <image  @click.stop="addCart(index)" src="/static/add.png" mode="aspectFit"
                      style="width: 44rpx;height: 44rpx;" v-if="item.num==0 && item.kxStockVo.stock"></image>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view v-else style="padding-top: 180rpx;padding-bottom: 180rpx;">
			<missing :buttonName="'换个地址试试吧~'" :handlerName="'buttonClick'"
			@buttonClick="chooseLocation"
			:imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%204%20%E6%8B%B7%E8%B4%9D@3x.png'" :desc="'当前地区不在配送范围哦'"></missing>
		</view>
		<view v-show="false">{{cartNums}}</view>
	</view>
</template>

<script>
	import missing from "@/components/missing.vue"
	import uniNumberBox from '@/components/uni-number-box.vue'
	export default {
		data() {
			return {
				sizeCalcState: false,
				tabScrollTop: 0,
				currentId: 1,
				flist: [],
				slist: [],
				sTitle:'',
				tlist: [],
				rawData: [],
				storage:true,
				currentIded:0,
				//自适应头部
				navbarTop:26,
				navHeight:32,
				navBottom:58,
				MT:38,
				MT2:20,
				statusHeight:20,
				topHeight:156,
				scollHeight:500,
				count:1,
				pageNo:1,
                pageSize:10,
				cover:false,
				curentTabView:'',
				cartNums:'',
				loaded:false,
				banner:{
					imgUrl:''
				}
			}
		},
		components:{missing, uniNumberBox},
		onReachBottom() {
			if(this.pageNo >= this.count){
				console.log('到底了')
				//到底了
				return
			}
			this.pageNo ++
			this.$api.request('get', 'storage/position/getGoodsPageByStorage',{
				storageId :this.$store.state.storageId,
				categoryId:this.currentId,
                pageNo:this.pageNo,
                pageSize:this.pageSize
			}).then( res => {
				uni.hideLoading()
				this.tlist = this.tlist.concat(res.data.rows)
				this.count = res.data.total / this.pageSize + (res.data.total % this.pageSize == 0 ? 0 : 1)
			})
		},
		onReady() {
			const res = uni.getSystemInfoSync();
			console.log(res.statusBarHeight);
			this.statusHeight = res.statusBarHeight


			// #ifdef MP-WEIXIN
			console.log('状态栏高度')
			console.log(this.statusHeight+'px')
			this.navbarTop = wx.getMenuButtonBoundingClientRect().top
			this.navHeight = wx.getMenuButtonBoundingClientRect().height
			console.log('标题栏高度')
			console.log(this.navHeight+'px')
			this.navBottom = wx.getMenuButtonBoundingClientRect().bottom
			console.log('底部坐标')
			console.log(this.navBottom+'px')
			this.MT = this.navBottom
			console.log('定位栏外边框高度')
			console.log(this.MT+'px')
			// #endif
			this.MT2 = this.MT - 20
			// #ifdef APP-PLUS
			this.navbarTop = this.statusHeight
			this.navHeight = 44
			// this.navBottom =  this.navHeight + 20
			// this.MT = this.navBottom
			this.navBottom =  this.navHeight
			this.MT2 = this.navBottom
			// #endif

			const that = this
			const query = uni.createSelectorQuery().in(this);
			query.select('#top').boundingClientRect(data => {
			  console.log("得到布局位置信息" + JSON.stringify(data));
			  console.log("节点离页面顶部的距离为" + data.top);
			  that.topHeight = data.height + that.statusHeight - 2
			  that.scollHeight = res.screenHeight - 50 - that.topHeight
			  // #ifdef APP-PLUS
			  that.topHeight = data.height + (that.statusHeight/2)
			  // that.topHeight = 146
			  that.scollHeight = res.screenHeight - that.topHeight
			  // #endif
			  console.log('可使用高度为：')
			  console.log(that.scollHeight)
			}).exec();
		},
		onLoad(){
			// console.log(this.$store.state.storageId)
			this.loadData();
			this.$api.request('get','carousel/app/getCarouselActive',{
				adType:7
			}).then(res=>{
				this.banner = res.data[0]
			})
		},
		onShow() {
			this.$store.state.storageId ? this.storage = true : this.storage = false
			//如果用户已登录，获取购物车数量
			if(this.$store.state.userInfo.accessToken){
				this.countTabNum()
			}
			if(this.loaded){
				this.loadCartData()
			}
		},
		methods: {
			loadData(){
				const that = this
				this.$api.request('get', 'product/app/categoryList').then( res => {
					that.rawData = res.data
					that.flist = res.data
					that.currentId = res.data[0].id
					that.slist[0] = {
                        cateName:'全部',
						id:that.currentId
					}
					that.slist = that.slist.concat(res.data[0].children)
					that.sTitle = that.slist[1].cateName
					that.sTabTap(that.slist[0])
					console.log(that.slist)
				})

			},
			//一级分类点击
			tabtap(item){
				this.currentId = item.id;
				this.slist = []
				this.slist[0] = {
                    cateName:'全部',
					id:this.currentId
				}
				if(item.childrenList){
                    this.slist = this.slist.concat(item.childrenList)
                    this.sTitle = this.slist[1].cateName
                }
				console.log(this.slist)
				this.sTabTap(this.slist[0])
				this.tabScrollTop = this.tabScrollTop === 0 ? 1 : 0
			},
			//二级分类点击
			sTabTap(item){
				uni.showLoading({

				})
				this.cover ? this.cover = false:'';
				this.currentIded = item.id
				this.sTitle = item.cateName //二级分类不用显示这个东西
				this.curentTabView = 'id'+item.id
				this.$api.request('get', 'storage/position/getGoodsPageByStorage',{
					storageId :this.$store.state.storageId,
					categoryId:item.id,
                    pageSize:this.pageSize
				}).then( res => {
					uni.hideLoading()
					if(!this.loaded){
						this.loaded = true
					}
					this.tlist = res.data.rows
					for (var i = 0; i < this.tlist.length; i++) {
						this.tlist[i].num = 0
					}
					this.loadCartData()
					console.log(this.tlist)
					this.count = res.data.total / this.pageSize + (res.data.total % this.pageSize == 0 ? 0 : 1)
				})
			},
			navToList(tid){
				uni.navigateTo({
					url: `/pages/product/list?tid=${tid}`
				})
			},
			navToDetail(id){
				// console.log()
				uni.navigateTo({
					url:`../product/detail?id=${id}`
				})
			},
			naviageToPage(page) {
				uni.navigateTo({
					url: page
				})
			},
			showCover(){
				this.cover ? this.cover = false : this.cover = true
			},
			cartNumFn(){
				let cartNum=0
				this.tlist.forEach((item,i)=>{
					cartNum+=item.num
				})
				this.cartNums=cartNum
			},
			addCart(index){
				const that = this
				that.$api.request('get', 'cart/app/addCartItem', {
					productId: that.tlist[index].id,
					// activityId:that.tlist[index].skuDto.activityId,
					// couponId:that.tlist[index].skuDto.couponId,
					num: 1
				}).then(res => {
					that.$api.msg('添加购物车成功')
					this.tlist[index].num++
					this.cartNumFn()
					this.tlist[index].cartId = res.data.id
					console.log(that.tlist)
					var cartNum = this.$store.state.cartNum+1
					that.$store.commit('addCart',cartNum)
					uni.setTabBarBadge({
						index:2,
						text:cartNum+''
					})
				})
			},
			//数量
			numberChange(data){
				const that = this
				if(data.number == 0){
					this.deleteCartItem(data.index)
					return
				}
				that.$api.request('get','cart/app/updateCartItemNum', {
					cartId: this.tlist[data.index].cartId,
					num: data.number
				}).then(res => {
					that.$api.msg('添加购物车成功')
					this.tlist[data.index].num=data.number
					this.cartNumFn()
					this.countTabNum()
				})
			},
			loadCartData(){
				const that = this
				that.$api.request('get', 'cart/app/getCartList',{
					storageId:this.$store.state.storageId
				}).then(res => {
					//遍历查询当前展示的商品中是否有数量
					var cartIds = []
					var goodsIds = []
					for (var i = 0; i < res.data.length; i++) {
						cartIds.push(res.data[i].productId)
						for (var j = 0; j < that.tlist.length; j++) {
							if(that.tlist[j].id === res.data[i].productId){
								that.tlist[j].num = res.data[i].cartNum
								that.tlist[j].cartId = res.data[i].id
								console.log(that.tlist[j])
							}
						}
					}
					for (var j = 0; j < that.tlist.length; j++) {
						if(!cartIds.includes(that.tlist[j].id)){
							that.tlist[j].num = 0
							that.tlist[j].cartId = 0
						}
					}
					//触发视图更新
					that.cartNums+=1
					console.log(that.tlist)
				})
			},
			//删除
			deleteCartItem(index){
				const that = this
				that.$api.request('get', 'cart/app/removeCartItem', {
					cartId: that.tlist[index].cartId
				}).then(res => {
					// that.tlist.splice(index, 1);
					// that.calcTotal();
					that.tlist[index].num = 0
					that.cartNumFn()
					this.countTabNum()
					//uni.hideLoading();
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
								this.storage ? this.storage = false : this.storage = true
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
	page,
	.content {
		min-height: 50%;
		background-color: #f8f8f8;
	}

	.number-box{
		width: 73px;
	}

	.actived{
		font-size: 28rpx;
		color:#2AAC34;
		border-bottom:#2AAC34 solid 8rpx;
	}

	.line-orange::after, .lines-orange::after {
	  border-color: #FC6620;
	}

	.solid-bottom::after {
	  border-bottom: 1rpx solid rgba(0, 0, 0, 0.1);
	}

	.content {
		display: flex;
	}
	.left-aside {
		flex-shrink: 0;
		width: 150upx;
		height: 100%;
		background-color: #f8f8f8;
	}
	.f-item {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 100upx;
		font-size: 28upx;
		color: $font-color-base;
		position: relative;
		&.active{
			color: $base-color;
			background: #fff;
			&:before{
				content: '';
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				height: 30upx;
				width: 8upx;
				background-color: $base-color;
				opacity: .8;
			}
		}
	}

	.right-aside{
		flex: 1;
		overflow: hidden;
		padding-left: 20upx;
	}
	.s-item{
		display: flex;
		align-items: center;
		height: 70upx;
		padding-top: 8upx;
		font-size: 28upx;
		color: $font-color-dark;
	}
	.t-list{
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		background: #fff;
		padding-top: 12upx;
		&:after{
			content: '';
			flex: 99;
			height: 0;
		}
	}
	.t-item{
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		width: 176upx;
		font-size: 26upx;
		color: #666;
		padding-bottom: 20upx;

		image{
			width: 140upx;
			height: 140upx;
		}
	}
</style>
