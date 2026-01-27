<template>
	<view class="content">
		<view class="goods-list">
			<view v-for="(item, index) in goodsList" :key="index" class="goods-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="JSON.parse(item.image)[0].url" mode="aspectFill"></image>
				</view>
				<text class="title clamp">{{item.storeName}}</text>
				<view class="price-box">
					<view class="">
						<text style="font-size: 36rpx;" class="price">{{item.kxStockVo.price}}</text>
						<text class="text-gray text-df" style="font-size: 24rpx;">/{{item.unitName}}</text>
					</view>
					<text>{{item.kxStockVo.sales?item.kxStockVo.sales:0}}人已购买</text>
				</view>
			</view>
		</view>
		<uni-load-more :status="loadingType"></uni-load-more>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				loadingType: 'more', //加载更多状态
				goodsList: [],
				pageNo: 1,
				isVip: false
			};
		},
		onShow() {
			this.isVip = this.$api.isVip()
		},
		onLoad(options) {
			this.loadData();
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
			//加载商品 ，带下拉刷新和上滑加载
			async loadData(type = 'add', loading) {
				//没有更多直接返回
				if (type === 'add') {
					if (this.loadingType === 'nomore') {
						return;
					}
					this.loadingType = 'loading';
				} else {
					this.loadingType = 'more'
				}

				if (type === 'refresh') {
					this.pageNo = 1
				}
				this.$api.request('get', 'storage/position/getGoodsPageByStorage', {
					pageNo: this.pageNo,
					storageId: this.$store.state.storageId
				}).then(res => {
					let tempList = res.data.rows
					if (type === 'refresh') {
						this.goodsList = [];
					}
					this.goodsList = this.goodsList.concat(tempList);
					this.pageNo = res.data.pageNo + 1
					this.loadingType = res.data.totalPageNo > res.data.pageNo ? 'more' : 'nomore';
					if (type === 'refresh') {
						if (loading == 1) {
							uni.hideLoading()
						} else {
							uni.stopPullDownRefresh();
						}
					}
				})
			},
			//详情
			navToDetailPage(item) {
				let id = item.id;
				uni.navigateTo({
					url: `/pages/product/detail?id=${id}`
				})
			}
		},
	}
</script>

<style lang="scss">
	page,
	.content {
		background: $page-color-base;
	}

	.content {
		padding-top: 20upx;
	}

	/* 商品列表 */
	.goods-list {
		display: flex;
		flex-wrap: wrap;
		padding: 0 30upx;
		background: #fff;

		.goods-item {
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

		.price-box {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding-right: 10upx;
			font-size: 24upx;
			color: $font-color-light;
		}

		.price {
			font-size: $font-lg;
			color: $uni-color-primary;
			line-height: 1;

			&:before {
				content: '￥';
				font-size: 26upx;
			}
		}
	}
</style>
