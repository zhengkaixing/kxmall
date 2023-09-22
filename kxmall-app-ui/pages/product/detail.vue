<template>
	<view class="container">
		<view class="carousel">
			<swiper indicator-active-color="#2AAC34" indicator-dots circular=true duration="400">
				<swiper-item  class="swiper-item" v-for="(item,index) in JSON.parse(goods.sliderImage)" :key="item.ossId">
					<view class="image-wrapper">
						<image :src="item.url" class="loaded" mode="aspectFit"></image>
					</view>
				</swiper-item>
			</swiper>
		</view>

		<view class="introduce-section">
			<view class="text-2-cut " style="font-size: 36rpx;font-weight: bold; line-height: 50rpx;color: #333333;width: 686rpx;"
			>{{goods.storeName}}</view>

			<view class="flex align-start justify-between padding-top-sm">
				<view class="text-cut" style="width: 580rpx;font-size: 28rpx;line-height: 40rpx;color: #8D8E99;">
					{{goods.description}}
				</view>
				<view class="flex">
          <view @click="toFavorite" style="text-align: center;">
            <image :src="goods.collect?'../../static/product/collect-active.png':'../../static/product/collect.png'" mode="aspectFit"
                   style="width: 42rpx;height: 42rpx;"></image>
            <view style="font-size: 20rpx;line-height: 28rpx;padding-top: 10rpx;">收藏</view>
          </view>
					<view @click="shareShow = 1" style="text-align: center;padding-left: 40rpx;padding-right: 6rpx;">
						<image src="../../static/product/share.png" mode="aspectFit"
						style="width: 42rpx;height: 42rpx;"></image>
						<view style="font-size: 20rpx;line-height: 28rpx;padding-top: 10rpx;">分享</view>
					</view>
				</view>
			</view>
			<view class="flex align-center justify-between ">
				<view class="">
					<view class="flex align-center">
						<view class="text-green" style="font-size: 40rpx;line-height: 56rpx;font-weight: 600;">
							￥{{goods.kxStockVo.price}}
						</view>
						<view class="bg-gradual-orange margin-left-sm" v-if="zhekou >= 0"
						style="border-radius: 4rpx;padding: 0 8rpx;line-height: 34rpx;font-size: 24rpx;">
							{{zhekou}}折
						</view>
					</view>
					<view style="font-size: 28rpx;color: #8D8E99;line-height: 40rpx;text-decoration: line-through;padding: 10rpx;">
						￥{{goods.otPrice}}/{{goods.unitName}}
					</view>
				</view>
				<view style="font-size: 28rpx;color: #8D8E99;line-height: 40rpx;">
					{{goods.kxStockVo.sales}}人购买
				</view>
			</view>
		</view>

		<!-- 团购分享 -->
		<button v-if="goods.groupShop" class="share-section" open-type="share">
			<view class="share-icon">
				<text class="yticon icon-xingxing"></text>
				团
			</view>
			<text class="tit">{{goods.groupShop.minimumNumber}}人成团，已有{{goods.groupShop.alreadyBuyNumber}}人参团</text>
			<text class="yticon icon-bangzhu1"></text>
			<view class="share-btn">
				告诉TA
				<text class="yticon icon-you"></text>
			</view>
		</button>

		<view class="c-list" style="padding: 0 34rpx 0 30rpx;">
			<!-- <view style="justify-content: space-between;" class="c-row b-b" @click="toggleSpec">
				<view class="flex align-center">
					<view class="tit">购买类型</view>
					<view style="font-size: 28rpx;line-height: 40rpx;color: #FE9F43;">
						{{selectedSku.title?selectedSku.title:'请选择'}}
					</view>
				</view>
				<text class="yticon icon-you"></text>
			</view> -->
			<view v-if="couponList.length > 0" @click="toggleMask('show')" class="c-row b-b">
				<text class="tit">优惠券</text>
				<text class="con t-r" style="font-size: 28rpx;line-height: 40rpx;color: #FE9F43;">领取优惠券</text>
				<text class="yticon icon-you"></text>
			</view>
			<view class="c-row b-b" style="align-items: flex-start;">
				<text class="tit">配送费用</text>
        <view class="con-list">
          全场免运费
        </view>
        <!-- <view v-if="goods.freightTemplate && goods.freightTemplate.freightTemplateDO.defaultFreePrice == 0" class="con-list">
          全场免运费
        </view>
        <view v-else class="con-list">
          <text>单笔购买满¥{{goods.freightTemplate.freightTemplateDO.defaultFreePrice / 100.0}}元免邮费</text>
          <text v-if="goods.freightTemplate.freightTemplateDO.defaultContinueMoney > 0">每增加{{goods.freightTemplate.freightTemplateDO.defaultFirstNum}}件，增加运费¥{{goods.freightTemplate.freightTemplateDO.defaultContinueMoney / 100.0}}元</text>
          <text v-if="goods.freightTemplate.freightTemplateCarriageDOList.length > 0">TODO 特殊情况说明页面</text>
        </view> -->
			</view>
			<!-- <view class="c-row b-b">
				<text class="tit">规格</text>
				<text class="con t-r ">{{goods.unit}}</text>
			</view> -->
			<view v-for="(item, index) in goods.attributeList" :key="index" class="c-row b-b">
				<text class="tit">{{item.attribute}}</text>
				<view class="bz-list con">
					<text>{{item.value}}</text>
				</view>
			</view>
		</view>

		<!-- 评价 -->
		<view v-if="goods.appraisePage > 0" class="eva-section">
			<view class="e-header">
				<text class="tit">评价</text>
				<text>({{goods.appraisePage.length}})</text>
				<text @click="navAllAppraise" class="tip">全部评论</text>
				<text class="yticon icon-you"></text>
			</view>
			<view class="eva-box">
				<image class="portrait" :src="goods.appraisePage[0].userAvatarUrl ? goods.appraisePage[0].userAvatarUrl : '/static/user/touxiang-@2x.png'"
				 mode="aspectFill"></image>
				<view class="right">
					<text class="name">{{goods.appraisePage[0].userNickName?goods.appraisePage[0].userNickName:('用户' + goods.appraisePage[0].userId)}}</text>
					<text class="con">{{goods.appraisePage[0].content}}</text>
					<view class="bot">
						<text class="attr">购买类型：{{goods.appraisePage[0].skuTitle}}</text>
						<text class="time">{{timeFormat(goods.appraisePage[0].gmtCreate)}}</text>
					</view>
				</view>
			</view>
		</view>


		<view class="detail-desc padding-lr">
			<!-- <view class="d-header">
				<text>图文详情</text>
			</view> -->
			<view class="text-black padding-tb-lg " style="line-height: 50rpx;font-size: 36rpx;font-weight: bold;">
				商品详情
			</view>
			<!-- <u-parse className="rich-img" :content="goods.detail"></u-parse> -->
			<rich-text style="font-size: 28rpx;" :nodes="goods.detail"></rich-text>
		</view>
		<view class="bg-white flex align-end"
		style="position: fixed;bottom: 0;width: 750rpx;height: 98rpx;padding-left: 38rpx;">
			<view @click="navCart" class="flex align-end" style="width: 346rpx;height: 98rpx;padding-bottom: 16rpx;">
				<view style="position: relative;">
					<image src="../../static/tab-icon/tab3_2.png" mode="aspectFit"
					style="width: 56rpx;height: 52rpx;" ></image>
					<view v-if="cartNum" class="badge round"
					>{{cartNum}}</view>
				</view>
				<view style="font-size: 28rpx;line-height: 40rpx;color: #8D8E99;padding-left: 40rpx;">
					已选{{cartNum}}件
				</view>
			</view>
			<view @click="addCart" style="font-size: 28rpx;line-height: 40rpx;width: 404rpx;height: 98rpx;"
			 class="flex align-center justify-center bg-green ">
				加入购物车
			</view>
		</view>

		<view class="mask" :class="maskState===0 ? 'none' : maskState===1 ? 'show' : ''" @click="toggleMask">
			<view class="mask-content" @click.stop.prevent="stopPrevent">
				<view @click="obtainCoupon(index)" class="coupon-item" v-for="(item,index) in couponList" :key="index">
					<view class="con">
						<view class="left">
							<text class="title">{{item.title}}</text>
							<text v-if="item.gmtEnd" class="time">在{{item.gmtEnd}}前有效。 可领{{item.limit}}张，已领{{item.nowCount}}张</text>
							<text v-if="!item.gmtEnd" class="time">在领取后{{item.days}}天内有效。可领{{item.limit}}张，已领{{item.nowCount}}张</text>
						</view>
						<view class="right">
							<text class="price">{{item.discount / 100.0}}</text>
							<text>满{{item.min / 100.0}}可用</text>
						</view>
						<view class="circle l"></view>
						<view class="circle r"></view>
					</view>
					<text class="tips">{{item.categoryTitle?'限' + item.categoryTitle + '可用': '全品类可用'}}</text>
				</view>
			</view>
		</view>
		<!-- 分享弹窗 -->
		<view class="mask" :class="shareShow===0 ? 'none' : shareShow===1 ? 'show' : ''" @click="toggleMask1">
			<view class="mask-content bg-white" style="z-index: 999999;" @click.stop.prevent="stopPrevent">
				<view class="flex align-center justify-center" style="padding: 60rpx 0 50rpx 0;">
					<image src="../../static/line.png" mode="aspectFit"
					style="width: 100rpx;height: 2rpx;"></image>
					<view style="padding: 0 12rpx 0 16rpx;font-size: 30rpx;line-height: 42rpx;color: #333333;">分享至</view>
					<image src="../../static/line.png" mode="aspectFit"
					style="width: 100rpx;height: 2rpx;"></image>
				</view>
				<view class="solid-bottom flex flex-wrap" style="padding: 0 0 50rpx 40rpx;margin: 0 44rpx 0 42rpx;">
					<view @click="share('miniwechat')" style="text-align: center;">
						<button open-type="share" class="round" style="width: 88rpx;height: 88rpx;padding: 0;">
						<image class="round" src="../../static/login/weixindefuben@3x.png" mode="aspectFit"
						style="width: 88rpx;height: 88rpx;"></image>
						</button>
						<view style="font-size: 24rpx;line-height: 34rpx;color: #999999;padding-top: 26rpx;">微信</view>
					</view>
				</view>
				<view @click="toggleMask1" class="flex justify-center align-center"
				style="padding: 34rpx 0 24rpx;font-size: 30rpx;line-height: 42rpx;color: #333333;">
					取消
				</view>
			</view>
		</view>


		<!-- 规格-模态层弹窗 -->
		<view class="popup spec" :class="specClass" @touchmove.stop.prevent="stopPrevent">
			<!-- 遮罩层 -->
			<view class="mask"></view>
			<view class="layer attr-content" @click.stop="stopPrevent">
				<view class="a-t">
					<image v-if="goods.img" :src="(selectedSku.img?selectedSku.img:goods.img)"></image>
					<view class="right">
						<text class="price">¥{{isVip ? (selectedSku.vipPrice  + ' [VIP]') : selectedSku.price}}</text>
						<text class="stock">库存：{{selectedSku.stock}}件</text>
						<view class="selected">
							已选：
							<text>
								{{selectedSku.title}}
							</text>
						</view>
					</view>
				</view>
				<view class="attr-list">
					<!-- <text>规格</text> -->
					<view class="item-list">
						<text v-for="(skuItem, skuIndex) in goods.productList" :key="skuIndex" class="tit" :class="{selected: skuIndex === selectedSkuIndex}"
						 @click="selectSpec(skuItem, skuIndex)">
							{{skuItem.title}}
						</text>
					</view>
				</view>
				<button class="btn" @click="toggleSpec">完成</button>
			</view>
		</view>
	</view>
</template>

<script>
	import uParse from '@/components/u-parse/u-parse.vue';
	import {mapState} from 'vuex'
	export default {
		components: {
			uParse
		},
		data() {
			return {
				goods: {
					freightTemplate: undefined,
					skuList: [],
					categoryList: [],
					appraisePage: undefined
				},
				isVip: false,
				specClass: 'none',
				specSelected: [],
				selectedSku: {},
				selectedSkuIndex: -1,
				toggleCallback: undefined,
				maskState: 0, //优惠券面板显示状态
				couponList: [],
				submiting: false,
				cartNum:0,
				shareShow:0,
				zhekou:-1
			};
		},
		onShow() {
			this.isVip = this.$api.isVip()
		},
		computed:{
			...mapState(['storageId'])
		},
		onLoad(options) {
			console.log(options)
			const that = this
			uni.showLoading({
				title: '正在加载'
			})
			that.$api.request('get', 'product/app/getGoodsByStorage', {
				productId: options.id,
				storageId:this.storageId
			}, failres => {
				that.$api.msg(failres.msg)
				uni.hideLoading()
			}).then(res => {
				that.goods = res.data
				that.zhekou = ((res.data.kxStockVo.price/res.data.otPrice)*10).toFixed(1)
				// that.zhekou = ((30/90)*10).toFixed(1)
				if (that.goods.groupShop) {
					//若存在团购信息，将价格更新到团购价格
					that.goods.price = that.goods.groupShop.minPrice
					that.goods.vipPrice = that.goods.groupShop.minPrice
					//更新各个SKU的价格
					for (let i = 0; i < that.goods.productList.length; i++) {
						for (let j = 0; j < that.goods.groupShop.groupShopSkuList.length; j++) {
							if (that.goods.productList[i].id === that.goods.groupShop.groupShopSkuList[j].skuId) {
								that.goods.productList[i].price = that.goods.groupShop.groupShopSkuList[j].skuGroupShopPrice
								that.goods.productList[i].vipPrice = that.goods.groupShop.groupShopSkuList[j].skuGroupShopPrice
							}
						}
					}
				}
				uni.hideLoading()
			})
			//3.5接口改造中 暂时屏蔽
			// that.$api.request('coupon', 'getObtainableCoupon').then(res => {
			// 	that.couponList = res.data
			// })
			this.countCart()
		},
		onShareAppMessage() {
			return {
				title: (this.goods.groupShop ? '立即拼团-' : '好货分享-') + this.goods.title,
				imageUrl: this.goods.img,
				path: '/pages/product/detail?id=' + this.goods.id + (this.goods.groupShop ? '&gid=' + this.goods.groupShop.id : '')
			}
		},
		methods: {
			//统计购物车数量
			countCart(){
				this.$api.request('get','cart/app/countCart',{
					storageId:this.$store.state.storageId
				}).then(res=>{
					this.cartNum = res.data
				}).catch(err=>{
					this.$api.msg('请求失败，请稍后再试')
				})
			},
			//切换到购物车页面
			navCart(){
				uni.switchTab({
					url:"../cart/cart"
				})
			},
			//分享{
			share(type){
				console.log(type)
				if(type == 'miniwechat'){
					uni.showShareMenu({
						title: (this.goods.groupShop ? '立即拼团-' : '好货分享-') + this.goods.title,
						imageUrl: this.goods.img,
						path: '/pages/product/detail?id=' + this.goods.id + (this.goods.groupShop ? '&gid=' + this.goods.groupShop.id : '')
					})
				}
			},
			toggleMask(type) {
				let timer = type === 'show' ? 10 : 300;
				let state = type === 'show' ? 1 : 0;
				this.maskState = 2;
				setTimeout(() => {
					this.maskState = state;
				}, timer)
			},
			toggleMask1(type) {
				let timer = type === 'show' ? 10 : 300;
				let state = type === 'show' ? 1 : 0;
				this.shareShow = 2;
				setTimeout(() => {
					this.shareShow = state;
				}, timer)
			},
			//领取优惠券
			obtainCoupon(index) {
				const that = this
				that.$api.request('get', '/obtainCoupon', {
					couponId: that.couponList[index].id
				}).then(res => {
					that.$api.msg('领取成功')
					that.couponList[index].nowCount++
					that.toggleMask()
				})
			},
			//规格弹窗开关
			toggleSpec(e) {
				if (this.specClass === 'show') {
					this.specClass = 'hide';

					setTimeout(() => {
						this.specClass = 'none';
						if (this.toggleCallback) {
							this.toggleCallback()
							this.toggleCallback = undefined
						}
					}, 150);
				} else if (this.specClass === 'none') {
					this.specClass = 'show';
					if (!this.selectedSku.title) {
						this.selectedSku = this.goods.productList[0]
						this.selectedSkuIndex = 0
					}
				}
			},
			//选择规格
			selectSpec(skuItem, skuIndex) {
				this.selectedSku = skuItem
				this.selectedSkuIndex = skuIndex
			},
			//加入购物车
			addCart(e) {
				//不再按规格
				const that = this
				that.$api.request('get', 'cart/app/addCartItem', {
					productId: that.goods.kxStockVo.productId,
					// activityId:that.goods.skuDto.activityId,
					// couponId:that.goods.skuDto.couponId,
					num: 1
				}).then(res => {
					if (that.goods.groupShop) {
						that.$api.msg('从购物车结算不会参加团购')
					} else {
						that.$api.msg('添加购物车成功')
						this.cartNum ++
						that.$store.commit('addCart',this.cartNum)
						uni.setTabBarBadge({
							index:2,
							text:this.cartNum+''
						})
					}

				})
				return
				if (!that.selectedSku.id) {
					that.specClass = 'none'
					that.toggleSpec()
					that.toggleCallback = that.addCart
				} else {
					//添加到购车车
					that.$api.request('get', 'cart/app/addCartItem', {
						productId: that.selectedSku.productId,
						num: 1
					}).then(res => {
						if (that.goods.groupShop) {
							that.$api.msg('从购物车结算不会参加团购')
						} else {
							that.$api.msg('添加购物车成功')
						}

					})
				}
			},
			//收藏
			toFavorite() {
				const that = this
				if (that.goods.collect) {
					//取消收藏
					that.goods.collect = false
					this.$api.request('post', 'user/userCollect/deleteCollect', {
						productId: that.goods.id
					}).then(res => {
						this.$api.msg('已取消收藏')
					})
				} else {
					//添加收藏
					that.goods.collect = true
					this.$api.request('post', 'user/userCollect/addCollect', {
						productId: that.goods.id
					}).then(res=>{
						this.$api.msg('已收藏')
					})
				}
			},
			buy() {
				const that = this
				if (!that.selectedSku.id) {
					that.specClass = 'none'
					that.toggleSpec()
					that.toggleCallback = that.buy
				} else {
					let skuItem = {
						productId: that.selectedSku.id,
						num: 1,
						title: that.goods.title,
						otPrice: that.selectedSku.otPrice,
						price: that.selectedSku.price,
						vipPrice: that.selectedSku.vipPrice,
						skuTitle: that.selectedSku.title,
						spuImg: that.goods.img,
						skuImg: that.selectedSku.img,
						stock: that.selectedSku.stock,
						productId: that.goods.id,
						categoryId: that.goods.categoryId,
						cateIdList: that.goods.categoryIds
					}
					if (that.goods.groupShop) {
						skuItem['groupShopId'] = that.goods.groupShop.id
					}
					let skuList = [1]
					skuList[0] = skuItem
					that.$api.globalData.productList = skuList
					uni.navigateTo({
						url: `/pages/order/create?takeway=buy`
					})
				}
			},
			//查看所有评价
			navAllAppraise() {
				uni.navigateTo({
					url: `/pages/product/appraise?spuid=${this.goods.id}&firstpage=${JSON.stringify(this.goods.appraisePage)}`
				})
			},
			timeFormat(time){
				var date = new Date(parseInt(time));
				var month = date.getMonth()+1>9?date.getMonth()+1:'0'+parseInt(date.getMonth()+1)
				var day = date.getDate()>9?date.getDate():'0'+date.getDate()
				return date.getFullYear()+'-'+month+'-'+day
			},
			stopPrevent() {}
		},

	}
</script>


<style lang='scss'>
	page {
		background: $page-color-base;
		padding-bottom: 160upx;
	}

	.badge{
		background-color: #FF473C;
		width: 32rpx;
		height: 32rpx;
		color: #fff;
		font-size: 22rpx;line-height: 32rpx;text-align: center;
		position: absolute;
		left: 42rpx;
		bottom: 32rpx;
	}

	.bg-gradual-orange {
	  background-image: linear-gradient(180deg, #FE9F43, #FC6620);
	  color: #fff;
	}

	.icon-you {
		font-size: $font-base + 2upx;
		color: #888;
	}

	.carousel {
		height: 414rpx;
		position: relative;

		swiper {
			height: 100%;
		}

		.image-wrapper {
			width: 100%;
			height: 100%;
		}

		.swiper-item {
			display: flex;
			justify-content: center;
			align-content: center;
			height: 750upx;
			overflow: hidden;

			image {
				width: 100%;
				height: 100%;
			}
		}

	}

	/* 标题简介 */
	.introduce-section {
		background: #fff;
		padding: 32rpx 34rpx 0 30rpx;

		.title {
			font-size: 32upx;
			color: $font-color-dark;
			height: 50upx;
			line-height: 50upx;
		}

		.price-box {
			display: flex;
			align-items: baseline;
			height: 64upx;
			padding: 10upx 0;
			font-size: 26upx;
			color: $uni-color-primary;
		}

		.price {
			font-size: $font-lg + 2upx;
		}

		.m-price {
			margin: 0 12upx;
			color: $font-color-light;
			text-decoration: line-through;
		}

		.coupon-tip {
			align-items: center;
			padding: 4upx 10upx;
			background: $uni-color-primary;
			font-size: $font-sm;
			color: #fff;
			border-radius: 6upx;
			line-height: 1;
			transform: translateY(-4upx);
		}

		.bot-row {
			display: flex;
			align-items: center;
			height: 50upx;
			font-size: $font-sm;
			color: $font-color-light;

			text {
				flex: 1;
			}
		}
	}

	/* 分享 */
	.share-section {
		display: flex;
		align-items: center;
		color: $font-color-base;
		background: linear-gradient(left, #fdf5f6, #fbebf6);
		padding: 12upx 30upx;

		.share-icon {
			display: flex;
			align-items: center;
			width: 70upx;
			height: 30upx;
			line-height: 1;
			border: 1px solid $uni-color-primary;
			border-radius: 4upx;
			position: relative;
			overflow: hidden;
			font-size: 22upx;
			color: $uni-color-primary;

			&:after {
				content: '';
				width: 50upx;
				height: 50upx;
				border-radius: 50%;
				left: -20upx;
				top: -12upx;
				position: absolute;
				background: $uni-color-primary;
			}
		}

		.icon-xingxing {
			position: relative;
			z-index: 1;
			font-size: 24upx;
			margin-left: 2upx;
			margin-right: 10upx;
			color: #fff;
			line-height: 1;
		}

		.tit {
			font-size: 28rpx;
			line-height: 40rpx;
			/* margin-left: 10upx; */
			color: #8D8E99;
			font-weight: bold;
		}

		.icon-bangzhu1 {
			padding: 10upx;
			font-size: 30upx;
			line-height: 1;
		}

		.share-btn {
			flex: 1;
			text-align: right;
			font-size: $font-sm;
			color: $uni-color-primary;
		}

		.icon-you {
			font-size: $font-sm;
			margin-left: 4upx;
			color: $uni-color-primary;
		}
	}

	.c-list {
		font-size: $font-sm + 2upx;
		color: $font-color-base;
		background: #fff;

		.c-row {
			display: flex;
			align-items: center;
			padding: 28upx 6rpx 34rpx 8rpx;
			position: relative;
		}

		.tit {
			width: 140upx;
			font-size: 28rpx;
			line-height: 40rpx;
			/* margin-left: 10upx; */
			color: #8D8E99;
			font-weight: 550;
		}

		.con {
			flex: 1;
			color: $font-color-dark;

			.selected-text {
				margin-right: 10upx;
			}
		}

		.bz-list {
			height: 40upx;
			font-size: $font-sm+2upx;
			color: $font-color-dark;

			text {
				display: inline-block;
				margin-right: 30upx;
			}
		}

		.con-list {
			flex: 1;
			display: flex;
			flex-direction: column;
			color: $font-color-dark;
			line-height: 40upx;
		}

		.red {
			color: $uni-color-primary;
		}
	}

	/* 评价 */
	.eva-section {
		display: flex;
		flex-direction: column;
		padding: 20upx 30upx;
		background: #fff;
		margin-top: 16upx;

		.e-header {
			display: flex;
			align-items: center;
			height: 70upx;
			font-size: $font-sm + 2upx;
			color: $font-color-light;

			.tit {
				font-size: $font-base + 2upx;
				color: $font-color-dark;
				margin-right: 4upx;
			}

			.tip {
				flex: 1;
				text-align: right;
			}

			.icon-you {
				margin-left: 10upx;
			}
		}
	}

	.eva-box {
		display: flex;
		padding: 20upx 0;

		.portrait {
			flex-shrink: 0;
			width: 80upx;
			height: 80upx;
			border-radius: 100px;
		}

		.right {
			flex: 1;
			display: flex;
			flex-direction: column;
			font-size: $font-base;
			color: $font-color-base;
			padding-left: 26upx;

			.con {
				font-size: $font-base;
				color: $font-color-dark;
				padding: 20upx 0;
			}

			.bot {
				display: flex;
				justify-content: space-between;
				font-size: $font-sm;
				color: $font-color-light;
			}
		}
	}

	/*  详情 */
	.detail-desc {
		background: #fff;
		margin-top: 16upx;
		width: 750upx;

		.d-header {
			display: flex;
			justify-content: center;
			align-items: center;
			height: 80upx;
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			position: relative;

			text {
				padding: 0 20upx;
				background: #fff;
				position: relative;
				z-index: 1;
			}

			&:after {
				position: absolute;
				left: 50%;
				top: 50%;
				transform: translateX(-50%);
				width: 300upx;
				height: 0;
				content: '';
				border-bottom: 1px solid #ccc;
			}
		}
	}

	/* 规格选择弹窗 */
	.attr-content {
		padding: 10upx 30upx;

		.a-t {
			display: flex;

			image {
				width: 170upx;
				height: 170upx;
				flex-shrink: 0;
				margin-top: -40upx;
				border-radius: 8upx;
				;
			}

			.right {
				display: flex;
				flex-direction: column;
				padding-left: 24upx;
				font-size: $font-sm + 2upx;
				color: $font-color-base;
				line-height: 42upx;

				.price {
					font-size: $font-lg;
					color: $uni-color-primary;
					margin-bottom: 10upx;
				}

				.selected-text {
					margin-right: 10upx;
				}
			}
		}

		.attr-list {
			display: flex;
			flex-direction: column;
			font-size: $font-base + 2upx;
			color: $font-color-base;
			padding-top: 30upx;
			padding-left: 10upx;
		}

		.item-list {
			padding: 20upx 0 0;
			display: flex;
			flex-wrap: wrap;

			text {
				display: flex;
				align-items: center;
				justify-content: center;
				background: #eee;
				margin-right: 20upx;
				margin-bottom: 20upx;
				border-radius: 100upx;
				min-width: 60upx;
				height: 60upx;
				padding: 0 20upx;
				font-size: $font-base;
				color: $font-color-dark;
			}

			.selected {
				background: #fbebee;
				color: $uni-color-primary;
			}
		}
	}

	/*  弹出层 */
	.popup {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		z-index: 99;

		&.show {
			display: block;

			.mask {
				animation: showPopup 0.2s linear both;
			}

			.layer {
				animation: showLayer 0.2s linear both;
			}
		}

		&.hide {
			.mask {
				animation: hidePopup 0.2s linear both;
			}

			.layer {
				animation: hideLayer 0.2s linear both;
			}
		}

		&.none {
			display: none;
		}

		.mask {
			position: fixed;
			top: 0;
			width: 100%;
			height: 60%;
			z-index: 1;
			background-color: rgba(0, 0, 0, 0.4);
		}

		.layer {
			position: fixed;
			z-index: 99;
			bottom: 0;
			width: 100%;
			min-height: 40vh;
			border-radius: 10upx 10upx 0 0;
			background-color: #fff;

			.btn {
				height: 66upx;
				line-height: 66upx;
				border-radius: 100upx;
				background: $uni-color-primary;
				font-size: $font-base + 2upx;
				color: #fff;
				margin: 30upx auto 20upx;
			}
		}

		@keyframes showPopup {
			0% {
				opacity: 0;
			}

			100% {
				opacity: 1;
			}
		}

		@keyframes hidePopup {
			0% {
				opacity: 1;
			}

			100% {
				opacity: 0;
			}
		}

		@keyframes showLayer {
			0% {
				transform: translateY(120%);
			}

			100% {
				transform: translateY(0%);
			}
		}

		@keyframes hideLayer {
			0% {
				transform: translateY(0);
			}

			100% {
				transform: translateY(120%);
			}
		}
	}

	/* 底部操作菜单 */
	.page-bottom {
		position: fixed;
		left: 30upx;
		bottom: 30upx;
		z-index: 95;
		display: flex;
		justify-content: center;
		align-items: center;
		width: 690upx;
		height: 100upx;
		background: rgba(255, 255, 255, .9);
		box-shadow: 0 0 20upx 0 rgba(0, 0, 0, .5);
		border-radius: 16upx;

		.p-b-btn {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			font-size: $font-sm;
			color: $font-color-base;
			width: 96upx;
			height: 80upx;

			.yticon {
				font-size: 40upx;
				line-height: 48upx;
				color: $font-color-light;
			}

			&.active,
			&.active .yticon {
				color: $uni-color-primary;
			}

			.icon-fenxiang2 {
				font-size: 42upx;
				transform: translateY(-2upx);
			}

			.icon-shoucang {
				font-size: 46upx;
			}
		}

		.action-btn-group {
			display: flex;
			height: 76upx;
			border-radius: 100px;
			overflow: hidden;
			box-shadow: 0 20upx 40upx -16upx #fa436a;
			box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
			background: linear-gradient(to right, #ffac30, #fa436a, #F56C6C);
			margin-left: 20upx;
			position: relative;

			&:after {
				content: '';
				position: absolute;
				top: 50%;
				right: 50%;
				transform: translateY(-50%);
				height: 28upx;
				width: 0;
				border-right: 1px solid rgba(255, 255, 255, .5);
			}

			.action-btn {
				display: flex;
				align-items: center;
				justify-content: center;
				width: 180upx;
				height: 100%;
				font-size: $font-base;
				padding: 0;
				border-radius: 0;
				background: transparent;
			}
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

	.rich-img {
		width: 100%;
		height: auto;
		margin: 0;
		padding: 0;
		line-height: 0px;
	}


	button::after {
		border: none;
	}

</style>
