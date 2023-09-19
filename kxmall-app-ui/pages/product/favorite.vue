<template>
	<view class="container">
		<!-- 空白页 -->
		<!-- <empty v-if="loadingType === 'nomore' && favoriteList.length === 0"></empty> -->
		<view style="padding: 150rpx 0rpx;"
		v-if="loadingType === 'nomore' && favoriteList.length === 0">
			<missing
			v-if="loadingType === 'nomore' && favoriteList.length === 0"
			:imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%203%20%E6%8B%B7%E8%B4%9D@3x.png'"
			:desc="'暂时没有订单哦！'"></missing>
		</view>
		<view class="favorite-list">
			<block v-for="(item, index) in favoriteList" :key="index">
				<view class="favorite-item" :class="{'b-b': index!==favoriteList.length-1}">
					<view class="image-wrapper" style="width: 180rpx;height: 180rpx;">
						<image :src="JSON.parse(item.img)[0].url" :class="[item.loaded]" mode="aspectFill"
						lazy-load @load="onImageLoad('favoriteList', index)"
						 @error="onImageError('favoriteList', index)"
						 style="width: 180rpx;height: 180rpx;"></image>
					</view>
					<view class="item-right">
						<text class="clamp title">{{item.productName}}</text>
						<text class="attr text-cut">{{item.description}}</text>
						 <text class="attr">累计销售{{item.sales}}件</text>

						<text class="price"><text v-if="item.otPrice > (isVip?item.vipPrice:item.price)" style="text-decoration:line-through;color: #909399;margin-right: 20rpx;">¥{{item.otPrice}}</text>
							¥{{(isVip?(item.vipPrice + ' [VIP]'):item.price)}}</text>

					</view>
					<text class="del-btn yticon icon-fork" @click="deleteFavorite(item)"></text>
				</view>
			</block>
		</view>


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
				favoriteList: [],
				pageNo: 1,
				loadingType: 'more',
				isVip: false
			};
		},
		onShow() {
			this.isVip = this.$api.isVip()
		},
		onLoad(options) {
			this.loadData()
		},
		//下拉刷新
		onPullDownRefresh() {
			this.loadData('refresh');
		},
		//加载更多
		onReachBottom() {
			this.loadData();
		},
		methods: {
			//获取收藏列表
			loadData(type) {
				const that = this
				if (type === 'refresh') {
					that.pageNo = 1
					that.favoriteList = []
					that.loadingType = 'more'
				}
				this.pageSize = 10
				if (that.loadingType === 'more') {
					that.loadingType = 'loading'
					that.$api.request('get', 'user/userCollect/getCollectAll', {
						pageNum: that.pageNo,
                        pageSize: this.pageSize
					}).then(res => {
						that.pageNo = that.pageNo + 1
						that.loadingType = that.pageNo < res.total/ this.pageSize + (res.total%this.pageSize==0?0:1) ? 'more' : 'nomore'
                        res.rows.forEach(item => {
							that.favoriteList.push(item);
							if (type === 'refresh') {
								uni.stopPullDownRefresh();
							}
						})
					})
				}
			},
			deleteFavorite(item) {
				const that = this
				that.$api.request('post','user/userCollect/deleteCollect', {
					productId : item.productId
				}).then(res => {
					that.loadData('refresh')
				})
			},
			//监听image加载完成
			onImageLoad(key, index) {
				this.$set(this[key][index], 'loaded', 'loaded');
			},
			//监听image加载失败
			onImageError(key, index) {
				this[key][index].image = '/static/errorImage.jpg';
			},
		},
	}
</script>

<style lang="scss">
	.container {
		padding-bottom: 134upx;

		/* 空白页 */
		.empty {
			position: fixed;
			left: 0;
			top: 0;
			width: 100%;
			height: 100vh;
			padding-bottom: 100upx;
			display: flex;
			justify-content: center;
			flex-direction: column;
			align-items: center;
			background: #fff;

			image {
				width: 240upx;
				height: 160upx;
				margin-bottom: 30upx;
			}

			.empty-tips {
				display: flex;
				font-size: $font-sm+2upx;
				color: $font-color-disabled;

				.navigator {
					color: $uni-color-primary;
					margin-left: 16upx;
				}
			}
		}
	}

	/* 收藏列表项 */
	.favorite-item {
		display: flex;
		position: relative;
		padding: 30upx 40upx;

		.image-wrapper {
			width: 230upx;
			height: 230upx;
			flex-shrink: 0;
			position: relative;

			image {
				border-radius: 8upx;
			}
		}

		.checkbox {
			position: absolute;
			left: -16upx;
			top: -16upx;
			z-index: 8;
			font-size: 44upx;
			line-height: 1;
			padding: 4upx;
			color: $font-color-disabled;
			background: #fff;
			border-radius: 50px;
		}

		.item-right {
			display: flex;
			flex-direction: column;
			flex: 1;
			overflow: hidden;
			position: relative;
			padding-left: 30upx;

			.title,
			.price {
				font-size: $font-base + 2upx;
				color: $font-color-dark;
				height: 40upx;
				line-height: 40upx;
			}

			.attr {
				font-size: $font-sm + 2upx;
				color: $font-color-light;
				height: 50upx;
				line-height: 50upx;
			}

			.price {
				height: 50upx;
				line-height: 50upx;
			}
		}
	}

</style>
