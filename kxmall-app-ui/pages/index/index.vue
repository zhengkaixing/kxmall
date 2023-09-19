<template>
	<view style="overflow-x: hidden;">
		<view>
			<view id="top" style="background-image: url(http://qiniuoss.nauzone.cn/%E9%A1%B6%E9%83%A8.png);background-repeat: no-repeat; background-size: cover; width: 750rpx;position: fixed;top: 0;z-index: 999;">
				<view class="status-bar"></view>
				<view class="flex align-center justify-center text-white" style="font-size: 36rpx;font-weight: 500; position: absolute;width: 750rpx;"
				 :style="'top:'+navbarTop+'px;height:'+navHeight+'px;'">
					kxmall
				</view>
				<view v-if="appear" :style="'margin-top:'+MT+'px;'" class="flex justify-between align-center padding-lr ">
					<view @click="chooseLocation" class="flex align-center" style="padding-left: 6rpx;">
						<image src="../../static/index/dingweiweizhi@3x.png" mode="aspectFit" style="width: 30rpx;height: 36rpx;"></image>
						<view style="font-size: 36rpx;font-weight: 500;max-width: 450rpx;" class="padding-lr-xs text-white text-cut">{{district}}</view>
						<image src="../../static/index/arrow_down.png" mode="aspectFit" style="width: 18rpx;height: 10rpx;"></image>
					</view>
					<image src="../../static/index/saoma@3x.png" mode="aspectFit" style="width: 36rpx;height: 36rpx;margin-right: 6rpx;"></image>
				</view>
				<view v-if="appear" class="padding-lr" style="padding-bottom: 22rpx;padding-top: 24rpx;">
					<view @click="naviageToPage('/pages/product/search')" style="height: 64rpx;" class="bg-white round flex align-center">
						<view class="flex align-center padding-left">
							<text class="yticon icon-sousuo" style="margin-right: 10rpx;"></text><text class="lem-text-gray" style="font-size: 26rpx;">新鲜，搜一下就涞了</text>
						</view>
					</view>
				</view>
				<view v-else class="padding-lr flex align-center" :style="'margin-top:'+MT2+'px;'" style="padding-bottom: 22rpx;padding-top: 24rpx;">
					<view @click="chooseLocation" class="flex align-center padding-right-sm">
						<image src="../../static/index/dingweiweizhi@3x.png" mode="aspectFit" style="width: 25rpx;height: 30rpx;"></image>
						<view style="font-size: 30rpx;font-weight: 500;max-width: 150rpx;" class="padding-lr-xs text-cut text-white">{{district}}</view>
						<image src="../../static/index/arrow_down.png" mode="aspectFit" style="width: 18rpx;height: 10rpx;"></image>
					</view>
					<view @click="naviageToPage('/pages/product/search')" style="height: 64rpx;flex-grow: 1;flex-shrink: 0;" class="bg-white round flex align-center">
						<view class="flex align-center padding-left">
							<text class="yticon icon-sousuo" style="margin-right: 10rpx;"></text><text class="lem-text-gray" style="font-size: 26rpx;">新鲜，搜一下就涞了</text>
						</view>
					</view>
					<image src="../../static/index/saoma@3x.png" mode="aspectFit" style="width: 36rpx;height: 36rpx;margin-left: 10rpx;"></image>
				</view>
			</view>
		</view>
		<view :style="'height: '+topHeight+'px;'" style="width: 100%;display: flex; position: relative;"></view>
		<view v-if="storage" id="sw" style="z-index: 999;background-image: url(http://qiniuoss.nauzone.cn/%E5%BA%95%E9%83%A8.png);background-repeat: no-repeat; background-size: 750rpx 200rpx;"
		 class="padding-top-sm">
			<swiper autoplay="true" interval="2000" duration="400" class="carousel" circular @change="swiperChange"
			 previous-margin="27rpx" next-margin="27rpx">
				<swiper-item v-for="(item, index) in carouselList" :key="index" class="carousel-item" @click="naviageToPage(item.url)">
					<image :src="JSON.parse(item.imgUrl)[0].url" />
				</swiper-item>
			</swiper>
		</view>
		<view v-if="storage" id="bar" class="padding bg-white margin-bottom-sm" style="display: flex;align-items: center;justify-content: space-between;">
			<view class="flex align-center">
				<image src="../../static/index/waimai1.png" mode="aspectFit" style="width: 36rpx;height: 30rpx;"></image>
				<text class="margin-left-sm lem-text-gray" style="font-size: 24rpx;">免费配送</text>
			</view>
			<view class="flex align-center">
				<image src="../../static/index/wuliu2.png" mode="aspectFit" style="width: 36rpx;height: 30rpx;"></image>
				<text class="margin-left-sm lem-text-gray" style="font-size: 24rpx;;">晚到必赔</text>
			</view>
			<view class="flex align-center">
				<image src="../../static/index/star2.png" mode="aspectFit" style="width: 36rpx;height: 30rpx;"></image>
				<text class="margin-left-sm lem-text-gray" style="font-size: 24rpx;;">专业检测</text>
			</view>
			<view class="flex align-center">
				<image src="../../static/index/time1.png" mode="aspectFit" style="width: 36rpx;height: 30rpx;"></image>
				<text class="margin-left-sm lem-text-gray" style="font-size: 24rpx;">30分钟达</text>
			</view>
		</view>
		<view v-if="storage" class="bg-white padding-lr">
			<image @click="naviageToPage(indexBanner.url)" src="https://s1.ax1x.com/2020/04/07/G2urVA.png"
			 mode="aspectFit" style="width: 690rpx;height:174rpx ;padding-top: 8rpx;"></image>
			<view class="cate-section">
				<view v-for="(item, index) in categoryButtomList" :key="index" @click="naviageToPage(item.url+('&id='+item.id),item.title)"
				 class="cate-item">
					<image :src="JSON.parse(item.imgUrl)[0].url"></image>
					<text>{{item.title}}</text>
				</view>
			</view>
		</view>
		<view v-if="storage" class="margin-tb-sm flex align-center bg-white flex padding-top-xs padding-bottom-sm" style="width: 1000px;">
			<image src="../../static/index/title.png" mode="aspectFit" class="bg-white"
			style="width: 184rpx;height: 43rpx;z-index: 99;padding:0 30rpx;"></image>
			<view style="width: 650px;color: #999999;font: 26rpx;"
			class="padding-left-sm margin-left-sm newtimes text-df">
				{{newTimesContent}}
			</view>
		</view>

		<view v-if="cheapRecommend.length > 0 && storage" @click="naviageToPage('/pages/parity/parity?title=今日特价')" style="padding-left: 30rpx;padding-right: 26rpx;">

			<image src="../../static/index/bought.png" mode="aspectFit" style="width: 694rpx;height: 94rpx;"></image>
		</view>
		<scroll-view scroll-x="true" style="width: 750rpx;">
			<view v-if="cheapRecommend.length > 0  && storage" class="flex padding-left-sm padding-top-sm">
				<view v-for="(item,index) in cheapRecommend" :key="index" class="margin-lr-xs bg-white flex align-center justify-center flex-direction"
				 style="width: 200rpx;height: 340rpx;padding: 5rpx;border: 8rpx;" @click="navToDetailPage(item.productId)">
					<image style="width: 186rpx;height: 172rpx" :src="JSON.parse(item.img)[0].url" mode="aspectFit"></image>
					<view style="padding-top: 28rpx;">
						<view class="text-2-cut" style="width: 182rpx;height: 68rpx;font-size: 24rpx;color: #2D4454;">
							{{item.title}}</view>
						<view style="padding-top: 6rpx;padding-bottom: 14rpx;" class="flex align-center justify-between">
							<view style="width: 114rpx;height: 42rpx;line-height: 42rpx;font-size: 24rpx;color:#F62929;">￥{{item.price}}</view>
							<image @click.stop="addCart(item)" style="width: 48rpx;height: 48rpx;" src="../../static/index/cart.png"
							 mode="aspectFit" class="round"></image>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		<view v-if="salesTop.length > 0 && storage" class="bg-white padding-lr padding-tb-sm margin-top-sm"
		@click="naviageToPage('/pages/parity/parity?title=热卖推荐')">
			<image src="../../static/index/command.png" mode="aspectFit" style="width: 690rpx;height: 210rpx;"></image>
		</view>
		<view style="padding: 20rpx 12rpx 20rpx 30rpx;">
			<view v-if="salesTop.length > 0  && storage" class="flex flex-wrap">
				<view v-for="(item,index) in salesTop" :key="index" class="margin-bottom-sm bg-white flex align-center justify-center flex-direction"
				 style="width: 335rpx;height: 520rpx;padding: 10rpx;margin-right: 18rpx;border-radius: 8rpx;"
				 @click="navToDetailPage(item.productId)">
					<image style="width: 280rpx;height: 280rpx;margin: 10rpx;" :src="item.spuImg" mode="aspectFit"></image>
					<view style="padding-top: 28rpx;">
						<view class="text-cut" style="width: 294rpx;height: 40rpx;font-size: 28rpx;font-weight: Medium; color: #2D4454;">{{item.spuTitle}}</view>
						<view class="text-cut margin-tb-xs" style="width: 294rpx;height: 40rpx;font-size: 28rpx;color: #999999;">{{item.spuTitle}}</view>
						<view style="padding-top: 6rpx;" class="flex align-center justify-between">
							<view style="width: 114rpx;height: 42rpx;line-height: 42rpx;font-size: 24rpx;color:#F62929;">￥{{item.spuPrice}}</view>
							<view style="color: #B0B0B0;font-size: 24rpx;">{{item.spuUnit}}/份</view>

							<image @click.stop="addCart(item)" style="width: 48rpx;height: 48rpx;" src="../../static/index/cart.png"
							 mode="aspectFit" class="round"></image>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view v-if="!storage" style="padding-top: 180rpx;padding-bottom: 180rpx;">
			<missing :buttonName="'换个地址试试吧~'" :handlerName="'buttonClick'" @buttonClick="chooseLocation" :imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%204%20%E6%8B%B7%E8%B4%9D@3x.png'"
			 :desc="'当前地区不在配送范围哦'"></missing>
		</view>
		<view class="Bigmask" :class="ismask?'opAC':'op'" @click="nomask"></view>
		<view class="maskBox" :class="ismask?'scAC':'sc'">
			<image src="../../static/Invitation/close.png" mode="" class="maskClose" @click.stop="nomask"></image>
			<image :src="t5.imgUrl" mode="" class="mask" @click="naviageToPage('/pages/Invitation/Invitation')"></image>
			<!-- Invitation  inviteNewpeople-->
		</view>
	</view>
</template>

<script>
	import missing from "@/components/missing.vue"
	export default {
		data() {
			return {
				titleNViewBackground: '',
				swiperCurrent: 0,
				swiperLength: 0,
				carouselList: [],
				windowSpuList: [],
				cheapRecommend: [], //特价接口
				categoryPickList: [],
				categoryButtomList: [],
				salesTop: [],
				newTop: [],
				banner: undefined,
				isVip: false,
				district: '定位中...',
				navbarTop: 26,
				navHeight: 32,
				navBottom: 58,
				MT: 38,
				MT2: 20,
				statusHeight: 20,
				pageNo: 1,
				storage: true,
				topHeight: 156,
				appear: true,
				loaded: false, //是否已加载过
				total: 10, //推荐商品总数
				t5: {},
				ismask: false,
				newTimesContent:'',
				indexBanner:{
					imgUrl:''
				}
			};
		},
		onShow() {
      let addresses = uni.getStorageSync('addresses');
      if(addresses){
        this.district = addresses.addressesName;
      }else{
        this.district = '定位中...';
      }
			this.isVip = this.$api.isVip()
			if (this.loaded) {
				this.$store.state.storageId ? this.storage = true : this.storage = false
				if (this.$store.state.storageId && this.categoryButtomList.length == 0) {
					this.loadData(this.$store.state.storageId)
				}
			}
			//如果用户已登录，获取购物车数量
			if(this.$store.state.userInfo.accessToken && this.$store.state.storageId != 0){
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
					this.$api.msg('请求失败，请稍后再试')
				})
			}
		},
		components: {
			missing
		},
		async onLoad(options) {
			//#ifdef H5
			//H5进入，有可能是回调进来的
			if (options.code && options.state) {
				const that = this
				that.logining = true
				that.$api.request('get', 'h5Login', {
					loginType: 3,
					raw: options.code
				}, failres => {
					that.logining = false
					that.$api.msg(failres.msg)
				}).then(res => {
					//登录成功，重定向到指定目标
					that.logining = false
					that.$store.commit('login', res.data)
					uni.setStorageSync('userInfo', res.data)
					//重定向到
					//不能重定向到tabbar页面
					if (options.state === '/pages/cart/cart' || options.state === '/pages/user/user' ||
						options.state === '/pages/index/index' || options.state === '/pages/category/category') {
						uni.switchTab({
							url: options.state
						})
					} else {
						uni.redirectTo({
							url: options.state
						})
					}

				})
			}
			//#endif
			this.$api.request('get','carousel/app/getCarouselActive',{
				adType:6
			}).then(res=>{
				this.indexBanner = res.data[0]
			})
      // if(options && options.type){
      //   let addresses = uni.getStorageSync('addresses');
      //   if(addresses){
      //     this.district = addresses.addressesName;
      //     res1.longitude =addresses.lng
      //     res1.latitude =addresses.lat
      //     res1.errMsg ='getLocation:ok'
      //   }
      // }else {
      //   var [err, res1] = await uni.getLocation({
      //   type: 'wgs84'
      //    })
      //
      // }
			var res1={}
			res1.errMsg ='getLocation:ok'
			res1.longitude ='11.22'
			res1.latitude ='11.22'
			if (res1.errMsg === 'getLocation:ok') {
				uni.showLoading({
					title: "加载中..."
				})
				this.$api.request('get','storage/position/getRecentlyStorage', {
					lng: res1.longitude,
					lat: res1.latitude
				}, failres => {
					uni.hideLoading()
					this.logining = false
					this.loaded = true
					this.$api.msg(failres.msg)
					this.loadRecommand('refresh')
				}).then(res => {
					uni.hideLoading()
					console.log(res)
					res.data.id = res.data.id
					this.loaded = true
					this.$store.commit('setStorage', res.data.id)
					this.loadData(res.data.id)
					if (!res.data.id) {
						this.storage = false
					}
				})
			}
			var address = await this.$getLocation()
			if (address.streetNumber.street.length > 0) {
				this.district = address.streetNumber.street
			} else {
				this.district = address.township
			}
			if (address.pois.length > 0 && address.pois[0].name.length < 8) {
				this.district = address.pois[0].name
			}
			//获取前置仓数据
			console.log(address)
		},
		onPageScroll(e) {
			e.scrollTop >= 100 ? this.appear = false : this.appear = true
		},
		onReachBottom() {
			if (this.salesTop.length % 10 == 0 && this.salesTop.length > 0) {
				this.loadRecommand()
			}
		},
		onReady() {
			const res = uni.getSystemInfoSync();
			console.log(res.statusBarHeight);
			this.statusHeight = res.statusBarHeight

			// #ifdef MP-WEIXIN
			console.log('状态栏高度')
			console.log(this.statusHeight + 'px')
			this.navbarTop = wx.getMenuButtonBoundingClientRect().top
			this.navHeight = wx.getMenuButtonBoundingClientRect().height
			console.log('标题栏高度')
			console.log(this.navHeight + 'px')
			this.navBottom = wx.getMenuButtonBoundingClientRect().bottom
			console.log('底部坐标')
			console.log(this.navBottom + 'px')
			this.MT = this.navBottom

			console.log('定位栏外边框高度')
			console.log(this.MT + 'px')
			// #endif
			// #ifdef APP-PLUS
			this.navbarTop = this.statusHeight
			this.navHeight = 44
			this.navBottom = this.navHeight + 20
			this.MT = this.navBottom
			// #endif
      // #ifdef H5
      this.navbarTop = 5;
      // #endif
			this.MT2 = this.MT - 20
			const that = this
			const query = uni.createSelectorQuery().in(this);
			query.select('#top').boundingClientRect(data => {
				console.log("得到布局位置信息" + JSON.stringify(data));
				console.log("节点离页面顶部的距离为" + data.top);
				that.topHeight = data.height + that.statusHeight - 5
				// #ifdef APP-PLUS
				that.topHeight = data.height + 20
				console.log('占位元素高度为' + that.topHeight)
				// #endif
			}).exec();
		},
		methods: {
			async loadData(id) {
				const that = this

				if (!id) {
					id = 0
				}
				console.log(id)
				uni.showLoading({
					title: "加载中..."
				})
				that.$api.request('get', 'storage/position/getIndexDataByStorage', {
					storageId: id
				}, failres => {
					that.$api.msg(failres.msg)
					uni.hideLoading()
				}).then(res => {
					let data = res.data
					//橱窗
					that.windowSpuList = data.cheapRecommend
					//轮播
					data.carouseList.t1.forEach(item => {
						if (!item.color) {
							item.color = 'rgb(205, 215, 218)'
						}
					})
					that.carouselList = data.carouseList.t1
					that.swiperLength = data.carouseList.t1.length
					that.titleNViewBackground = data.carouseList.t1[0].color
					if (data.cheapRecommend) {
						that.cheapRecommend = data.cheapRecommend
					}

					//弹窗
					// if (data.carouseList.t5.length > 0) {
					// 	that.t5 = data.carouseList.t5[0]
					// 	this.ismask = true
					// }

					this.newTimesContent = data.newTimesContent


					//分类精选
					if (data.carouseList.t2) {
						that.categoryPickList = data.carouseList.t2
					}
					//横幅
					if (data.carouseList.t3 && data.carouseList.t3.length > 0) {
						that.banner = data.carouseList.t3[0]
					}
					//热销
					if (data.salesTop) {
						that.salesTop = data.salesTop
						this.pageNo++
					}
					//分类5Buttom
					if (data.carouseList.t4) {
						that.categoryButtomList = data.carouseList.t4

						uni.setStorageSync('categoryButtomList', that.categoryButtomList);
					}
					uni.hideLoading()
				})
			},
			nomask() {
				this.ismask = false
			},
			loadRecommand(type) {
				console.log(type)
				if (type === 'refresh') {
					this.pageNo = 1
				}
				const that = this
				that.$api.request('get', 'storage/position/getGoodsPageByStorage', {
					storageId: this.$store.state.storageId,
					pageNo: this.pageNo
				}, failres => {
					that.$api.msg(failres.msg)
				}).then(res => {
					this.pageNo++
					let tempList = res.data.rows
					if (type === 'refresh') {
						that.salesTop = [];
					}
					that.salesTop = that.salesTop.concat(tempList)
				})
			},
			//轮播图切换修改背景色
			swiperChange(e) {
				const index = e.detail.current;
				this.swiperCurrent = index;
				this.titleNViewBackground = this.carouselList[index].color;
			},
			//详情页
			navToDetailPage(id) {
				uni.navigateTo({
					url: `/pages/product/detail?id=${id}`
				})
			},
			navToWindowSuggestSpu(index) {
				const that = this
				uni.navigateTo({
					url: '/pages/product/detail?id=' + that.windowSpuList[index].productId
				})
			},

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
					console.log(page)
					uni.navigateTo({
						url: page
					})
				}
				// console.log(that.categoryButtomList[index].title)

			},
			//选择地址
			chooseLocation() {
				uni.chooseLocation({
					success: (res1) => {
						console.log(res1)
						if (res1.name === "") {
							return
						}
						this.district = res1.name
						uni.showLoading({
							title: "加载中..."
						})
            let addressesInfo = {};
            addressesInfo.lng = res1.longitude;
            addressesInfo.lat = res1.latitude;
            addressesInfo.addressesName = res1.name;
            uni.setStorageSync('addresses', addressesInfo);
						this.$api.request('get','storage/position/getRecentlyStorage', {
							lng: res1.longitude,
							lat: res1.latitude
						}, failres => {
							uni.hideLoading()
						}).then(res => {
							uni.hideLoading()
							console.log(res)
							this.$store.commit('setStorage', res.data.id)
							this.newTop = []
							this.cheapRecommend = []
							this.salesTop = []
							this.loadData(res.data.id)
							if (!res.data.id) {
								this.storage = false
							} else {
								this.storage = true
								// this.loadRecommand('refresh')
							}
						})
					}
				});
			},
			addCart(item) {
				const that = this
				that.$api.request('get', 'cart/app/addCartItem', {
					productId: item.productId,
					activityId:item.activityId,
					couponId:item.couponId,
					num: 1
				}).then(res => {
					that.$api.msg('添加购物车成功')
					var cartNum = this.$store.state.cartNum+1
					that.$store.commit('addCart',cartNum)
					uni.setTabBarBadge({
						index:2,
						text:cartNum+''
					})
				})
			}
		},
		// #ifndef MP
		// 标题栏input搜索框点击
		onNavigationBarSearchInputClicked: async function(e) {
			uni.navigateTo({
				url: '/pages/product/search'
			})
		},
		//点击导航栏 buttons 时触发
		// onNavigationBarButtonTap(e) {
		// 	const index = e.index;
		// 	if (index === 0) {
		// 		this.$api.msg('点击了扫描');
		// 	} else if (index === 1) {
		// 		// #ifdef APP-PLUS
		// 		const pages = getCurrentPages();
		// 		const page = pages[pages.length - 1];
		// 		const currentWebview = page.$getAppWebview();
		// 		currentWebview.hideTitleNViewButtonRedDot({
		// 			index
		// 		});
		// 		// #endif
		// 		uni.navigateTo({
		// 			url: '/pages/notice/notice'
		// 		})
		// 	}
		// }
		// #endif
	}
</script>

<style lang="scss">
	page {
		background: #f5f5f5;
	}

	.m-t {
		margin-top: 16upx;
	}

	.Bigmask {
		width: 100%;
		height: 100vh;
		background-color: rgba(0, 0, 0, .3);
		position: fixed;
		top: 0;
		left: 0;
		z-index: 1000;
		transition: all .5s,
	}

	.maskBox {
		width: 77%;
		height: 52%;
		position: fixed;
		top: 50%;
		left: 50%;
		z-index: 1001;
		transform: translateX(-50%) translateY(-50%) scale(1, 1);
		transition: all .5s ease;

		.maskClose {
			position: absolute;
			right: 15px;
			top: 15px;
			width: 26px;
			height: 26px;
		}

		.mask {
			width: 100%;
			height: 100%;
		}
	}

	.op {
		opacity: 0;
		visibility: hidden;
	}

	.opAc {
		visibility: visible;
		opacity: 1;
	}

	.sc {
		opacity: 0;
		transform: translateX(-50%) translateY(-50%) scale(0, 0);
		transition: all .5s ease;
	}

	.scAc {
		opacity: 1;
		transform: translateX(-50%) translateY(-50%) scale(1, 1);
		transition: all .5s ease;
	}

	/* 新鲜时报 */
	.newtimes{
		overflow: hidden;
		white-space: nowrap;
		box-sizing: border-box;
		animation: marquee 15s linear infinite;
	}

	@keyframes marquee {
		0% {
		    -webkit-transform: translateX(184rpx);
		  }

		100% {
		    -webkit-transform: translateX(-100%);
		}
	}

	/* 头部 轮播图 */
	.carousel-section {
		position: relative;
		padding-top: 10px;

		.titleNview-placing {
			height: var(--status-bar-height);
			padding-top: 44px;
			box-sizing: content-box;
		}

		.titleNview-background {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 426upx;
			transition: .4s;
		}
	}

	.carousel {
		width: 100%;
		height: 280upx;

		.carousel-item {
			width: 100%;
			height: 100%;
			padding: 0 11upx;
			overflow: hidden;
		}

		image {
			width: 100%;
			height: 100%;
			border-radius: 12upx;
		}
	}

	.swiper-dots {
		display: flex;
		position: absolute;
		left: 60upx;
		bottom: 15upx;
		width: 72upx;
		height: 36upx;
		background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAABkCAYAAADDhn8LAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTMyIDc5LjE1OTI4NCwgMjAxNi8wNC8xOS0xMzoxMzo0MCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTk4MzlBNjE0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTk4MzlBNjA0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0E3RUNERkE0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6Q0E3RUNERkI0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4Gh5BPAAACTUlEQVR42uzcQW7jQAwFUdN306l1uWwNww5kqdsmm6/2MwtVCp8CosQtP9vg/2+/gY+DRAMBgqnjIp2PaCxCLLldpPARRIiFj1yBbMV+cHZh9PURRLQNhY8kgWyL/WDtwujjI8hoE8rKLqb5CDJaRMJHokC6yKgSCR9JAukmokIknCQJpLOIrJFwMsBJELFcKHwM9BFkLBMKFxNcBCHlQ+FhoocgpVwwnv0Xn30QBJGMC0QcaBVJiAMiec/dcwKuL4j1QMsVCXFAJE4s4NQA3K/8Y6DzO4g40P7UcmIBJxbEesCKWBDg8wWxHrAiFgT4fEGsB/CwIhYE+AeBAAdPLOcV8HRmWRDAiQVcO7GcV8CLM8uCAE4sQCDAlHcQ7x+ABQEEAggEEAggEEAggEAAgQACASAQQCCAQACBAAIBBAIIBBAIIBBAIABe4e9iAe/xd7EAJxYgEGDeO4j3EODp/cOCAE4sYMyJ5cwCHs4rCwI4sYBxJ5YzC84rCwKcXxArAuthQYDzC2JF0H49LAhwYUGsCFqvx5EF2T07dMaJBetx4cRyaqFtHJ8EIhK0i8OJBQxcECuCVutxJhCRoE0cZwMRyRcFefa/ffZBVPogePihhyCnbBhcfMFFEFM+DD4m+ghSlgmDkwlOgpAl4+BkkJMgZdk4+EgaSCcpVX7bmY9kgXQQU+1TgE0c+QJZUUz1b2T4SBbIKmJW+3iMj2SBVBWz+leVfCQLpIqYbp8b85EskIxyfIOfK5Sf+wiCRJEsllQ+oqEkQfBxmD8BBgA5hVjXyrBNUQAAAABJRU5ErkJggg==);
		background-size: 100% 100%;

		.num {
			width: 36upx;
			height: 36upx;
			border-radius: 50px;
			font-size: 24upx;
			color: #fff;
			text-align: center;
			line-height: 36upx;
		}

		.sign {
			position: absolute;
			top: 0;
			left: 50%;
			line-height: 36upx;
			font-size: 12upx;
			color: #fff;
			transform: translateX(-50%);
		}
	}

	/* 分类 */
	.cate-section {
		display: flex;
		//justify-content: space-around;
		align-items: center;
		flex-wrap: wrap;
		padding-top: 34rpx;
		// padding: 30upx 25upx;
		background: #fff;

		.cate-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 138rpx;
			padding-bottom: 24rpx;
			font-size: $font-sm + 2upx;
			color: $font-color-dark;
		}

		/* 原图标颜色太深,不想改图了,所以加了透明度 */
		image {
			width: 86upx;
			height: 86upx;
			margin-bottom: 24upx;
			border-radius: 8rpx;
			opacity: .7;
			// box-shadow: 4upx 4upx 20upx rgba(250, 67, 106, 0.3);
		}
	}

	.ad-1 {
		width: 100%;
		height: 210upx;
		padding: 10upx 0;
		background: #fff;

		image {
			width: 100%;
			height: 100%;
		}
	}

	/* 秒杀专区 */
	.seckill-section {
		padding: 4upx 30upx 24upx;
		background: #fff;

		.s-header {
			display: flex;
			align-items: center;
			height: 92upx;
			line-height: 1;

			.s-img {
				width: 140upx;
				height: 30upx;
			}

			.tip {
				font-size: $font-base;
				color: $font-color-light;
				margin: 0 20upx 0 40upx;
			}

			.timer {
				display: inline-block;
				width: 40upx;
				height: 36upx;
				text-align: center;
				line-height: 36upx;
				margin-right: 14upx;
				font-size: $font-sm+2upx;
				color: #fff;
				border-radius: 2px;
				background: rgba(0, 0, 0, .8);
			}

			.icon-you {
				font-size: $font-lg;
				color: $font-color-light;
				flex: 1;
				text-align: right;
			}
		}

		.floor-list {
			white-space: nowrap;
		}

		.scoll-wrapper {
			display: flex;
			align-items: flex-start;
		}

		.floor-item {
			width: 150upx;
			margin-right: 20upx;
			font-size: $font-sm+2upx;
			color: $font-color-dark;
			line-height: 1.8;

			image {
				width: 150upx;
				height: 150upx;
				border-radius: 6upx;
			}

			.price {
				color: $uni-color-primary;
			}
		}
	}

	.f-header {
		display: flex;
		align-items: center;
		height: 100upx;
		padding: 6upx 30upx 8upx;
		background: #fff;

		image {
			flex-shrink: 0;
			width: 80upx;
			height: 80upx;
			margin-right: 20upx;
		}

		.tit-box {
			flex: 1;
			display: flex;
		}

		.tit {
			font-size: $font-lg +2upx;
			color: #font-color-dark;
			line-height: 1.3;
		}

		.tit2 {
			font-size: $font-sm;
			color: $font-color-light;
		}

		.icon-you {
			font-size: $font-lg +2upx;
			color: $font-color-light;
		}
	}

	/* 团购楼层 */
	.group-section {
		background: #fff;

		.g-swiper {
			height: 550upx;
			padding-bottom: 30upx;
		}

		.g-swiper-item {
			width: 100%;
			padding: 0 30upx;
			display: flex;
		}

		image {
			width: 100%;
			height: 360upx;
			border-radius: 4px;
		}

		.g-item {
			display: flex;
			flex-direction: column;
			overflow: hidden;
			width: 200rpx;
		}

		.left {
			flex: 1.2;
			margin-right: 24upx;

			.t-box {
				padding-top: 20upx;
			}
		}

		.right {
			flex: 0.8;
			flex-direction: column-reverse;

			.t-box {
				padding-bottom: 20upx;
			}
		}

		.t-box {
			height: 160upx;
			font-size: $font-base+2upx;
			color: $font-color-dark;
			line-height: 1.6;
		}

		.price {
			color: $uni-color-primary;
		}

		.m-price {
			font-size: $font-sm+2upx;
			text-decoration: line-through;
			color: $font-color-light;
			margin-left: 8upx;
		}

		.pro-box {
			display: flex;
			align-items: center;
			margin-top: 10upx;
			font-size: $font-sm;
			color: $font-base;
			padding-right: 10upx;
		}

		.progress-box {
			flex: 1;
			border-radius: 10px;
			overflow: hidden;
			margin-right: 8upx;
		}
	}

	/* 分类推荐楼层 */
	.hot-floor {
		width: 100%;
		overflow: hidden;
		margin-bottom: 20upx;

		.floor-img-box {
			width: 100%;
			height: 320upx;
			position: relative;

			&:after {
				content: '';
				position: absolute;
				left: 0;
				top: 0;
				width: 100%;
				height: 100%;
				background: linear-gradient(rgba(255, 255, 255, .06) 30%, #f8f8f8);
			}
		}

		.floor-img {
			width: 100%;
			height: 100%;
		}

		.floor-list {
			white-space: nowrap;
			padding: 20upx;
			padding-right: 50upx;
			border-radius: 6upx;
			margin-top: -140upx;
			margin-left: 30upx;
			background: #fff;
			box-shadow: 1px 1px 5px rgba(0, 0, 0, .2);
			position: relative;
			z-index: 1;
		}

		.scoll-wrapper {
			display: flex;
			align-items: flex-start;
		}

		.floor-item {
			width: 180upx;
			margin-right: 20upx;
			font-size: $font-sm+2upx;
			color: $font-color-dark;
			line-height: 1.8;

			image {
				width: 180upx;
				height: 180upx;
				border-radius: 6upx;
			}

			.price {
				color: $uni-color-primary;
			}
		}

		.more {
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			flex-shrink: 0;
			width: 180upx;
			height: 180upx;
			border-radius: 6upx;
			background: #f3f3f3;
			font-size: $font-base;
			color: $font-color-light;

			text:first-child {
				margin-bottom: 4upx;
			}
		}
	}

	/* 猜你喜欢 */
	.guess-section {
		display: flex;
		flex-wrap: wrap;
		padding: 0 30upx;
		background: #fff;

		.guess-item {
			display: flex;
			flex-direction: column;
			width: 48%;
			padding-bottom: 40upx;

			&:nth-child(2n+1) {
				margin-right: 4%;
			}
		}

		.image-wrapper {
			width: 100%;
			height: 330upx;
			border-radius: 3px;
			overflow: hidden;

			image {
				width: 100%;
				height: 100%;
				opacity: 1;
			}
		}

		.title {
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 80upx;
		}

		.price {
			font-size: $font-lg;
			color: $uni-color-primary;
			line-height: 1;
		}
	}

	.bg-gradual-green {
		background-image: linear-gradient(180deg, #86CE2C, #6db617);
		color: #ffffff;
	}
</style>
