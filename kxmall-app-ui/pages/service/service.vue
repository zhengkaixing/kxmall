<template>
	<view>
		<view class="bg-white margin-tb-sm padding flex align-center">
			<image src="../../static/service.png" mode="aspectFit"
			class="service"></image>
			<view class="content flex justify-between flex-direction">
				<view class="lem-text-black title">在线客服</view>
				<view class="phone text-df">121421532</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style>
	page{
		background: #f1f1f1;
	}
	
	.title{
		font-size: 32rpx;
		line-height: 38rpx;
	}
	
	.service{
		width: 77rpx;
		height: 77rpx;
		border-radius: 5000rpx;
	}
	
	.content{
		height: 77rpx;
		margin-left: 32rpx;
	}
	
	.phone{
		color:#2AAB34;
	}

</style>
