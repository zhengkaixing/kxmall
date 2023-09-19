<template>
	<view class="InvitationList">
		<view class="listBox" v-for="(item,i) in list" :key="i">
			<view class="index">
				{{i+1}}
			</view>
			<image :src="item.userDO.avatarUrl" mode="" class="img"></image>
			<view class="" class="name">
				{{item.userDO.nickname}}
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: []
			};
		},
		methods: {
			listin() {
				let that=this
				that.$api.request('get','invitationList',  failres => {
					that.logining = false
					that.$api.msg(failres.msg)
				}).then(res => {
					this.list=res.data

				})
			}
		},
		mounted() {
			this.listin()
		}
	}
</script>

<style lang="scss" scoped>
	.InvitationList{
		min-height: 100vh;
		background-color: #F6F5FA;
		width: 100%;
		color: #1B1C33;
		font-size: 32rpx;
		.listBox{
			display: flex;
			align-items: center;
			padding-left: 15px;
			background-color: #fff;
			width: 100%;
			border-top: 1px solid #F6F5FA;
			padding: 14rpx 30rpx;
			.index{

			}
			.img{
				width: 35px;
				height: 35px;
				border-radius: 50%;
				margin: 0 47px 0 37px;
			}
			.name{

			}
		}
	}
</style>
