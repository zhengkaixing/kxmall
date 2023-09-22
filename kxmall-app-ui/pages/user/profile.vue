<template>
	<view>
		<!-- <view class="container">
			<view class="list-cell b-b m-t" @click="inputShowModal('nickname')" hover-class="cell-hover" :hover-stay-time="50">
				<text class="cell-tit">修改昵称</text>
				<text class="cell-more yticon icon-you"></text>
			</view>
			<view class="list-cell b-b" @click="genderShowModal" hover-class="cell-hover" :hover-stay-time="50">
				<text class="cell-tit">修改性别</text>
				<text class="cell-more yticon icon-you"></text>
			</view>

			<neil-modal :show="inputShow" @close="cancel" title="编辑" @cancel="cancel" @confirm="confirm">
				<input v-model="inputContent" style="margin:20upx" placeholder="请输入..." />
			</neil-modal>

			<neil-modal :show="genderShow" @close="cancel" title="选择性别" @cancel="cancel" @confirm="confirmGender">
				<view>
					<radio-group style="text-align:center" @change="genderRadioChange">
						<label v-for="(item, index) in genders" :key="item.value">
							<radio :value="item.value + ''" :checked="index === gender" style="margin:10upx" />{{item.name}}
						</label>
					</radio-group>
				</view>
			</neil-modal>
		</view> -->
		<view style="height: 120rpx;"
		class="solid-top bg-white flex justify-between align-center margin-bottom-sm padding-lr padding-tb-s">
			<view class="title text-black">头像</view>
			<image @click="chooseImg" :src="userInfo.avatarUrl ? userInfo.avatarUrl :'../../static/user/touxiang-@2x.png'"
			class="round" mode="aspectFill"
			style="width: 90rpx;height: 90rpx;"></image>
		</view>
		<view style="height: 100rpx;"
		class="solid-bottom bg-white flex justify-between align-center padding-lr padding-tb-s">
			<view class="title text-black">昵称</view>
			<view class="flex align-center">
				<!-- <view class="nick-name">
					{{ userInfo.nickname ?userInfo.nickname : '未设置昵称' }}
				</view> -->
				<input @blur="changeName" @confirm="changeName"
				v-model="userInfo.nickname" placeholder="请输入昵称" class="text-df" style="text-align: right;" />
				<!-- <image src="../../static/icon/arrow_right.png" mode="aspectFit"
				class="icon-right"></image> -->
			</view>
		</view>
		<view style="height: 100rpx;"
		class="solid-bottom bg-white flex justify-between align-center padding-lr padding-tb-s">
			<view class="title text-black">性别</view>
			<view class="flex align-center">
				<view @click="changeGender(1)" class="flex align-center">
					<image
					:src="userInfo.gender == 1 ?'/static/cart/selected.png':'/static/cart/select.png'"
					mode="aspectFill"
					style="width: 40rpx;height: 40rpx;"></image>
					<view style="font-size: 28rpx;line-height: 38rpx;color: #5E5E66;padding-left: 20rpx;">男</view>
				</view>
				<view @click="changeGender(2)" class="flex align-center" style="padding-left: 80rpx;">
					<image
					 :src="userInfo.gender == 2 ?'/static/cart/selected.png':'/static/cart/select.png'"
					 mode="aspectFit"
					style="width: 40rpx;height: 40rpx;"></image>
					<view style="font-size: 28rpx;line-height: 38rpx;color: #5E5E66;padding-left: 20rpx;">女</view>
				</view>
			</view>
		</view>
		<view style="height: 100rpx;"
		class="solid-bottom bg-white flex justify-between align-center padding-lr padding-tb-s">
			<view class="title text-black">生日</view>
			<view class="flex align-center">
				<picker mode="date" @change="bindDateChange">
					<view class="lem-text-grey content">{{date}}</view>
				</picker>
				<!-- <view class="lem-text-grey content">2000年10月09日</view> -->
				<image src="../../static/icon/arrow_right.png" mode="aspectFit"
				class="icon-right"></image>
			</view>
		</view>
		<view style="height: 100rpx;"
		class=" bg-white flex justify-between align-center padding-lr padding-tb-s">
			<view class="title text-black">手机号</view>
			<view class="lem-text-grey content" style="padding-right: 34rpx;">{{phone}}</view>
		</view>
		<show-modal></show-modal>
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex';
	export default {

		data() {
			return {
				date:'选择您的出生日期',
				phone:''
			};
		},
		computed: {
			...mapState(['userInfo']),
		},
		onLoad() {
			if(this.userInfo.birthday){
				var date = new Date(this.userInfo.birthday)
				var month = date.getMonth() >= 9 ? date.getMonth()+1 : '0'+parseInt(date.getMonth()+1)
				var day = date.getDate() >= 9 ? date.getDate() : '0'+parseInt(date.getDate())
				this.date = date.getFullYear()+'-'+month+'-'+day
			}
			this.phone = this.userInfo.phone[0]+this.userInfo.phone[1]+this.userInfo.phone[2]+'****'+this.userInfo.phone[7]+this.userInfo.phone[8]+this.userInfo.phone[9]+this.userInfo.phone[10]
		},
		methods: {
			chooseImg(){
				this.$api.uploadImg(1,(res)=>{
					console.log(res)
					this.userInfo.avatarUrl = res
					var info = this.userInfo
					var tempObj = Object.assign({},this.userInfo,info)
					this.userInfo = this.login(tempObj)
					this.syncUser()
					//uni.setStorageSync('userInfo',this.userInfo)
				})
			},
			changeGender(index){
				this.userInfo.gender = index
				this.syncUser()
			},
			changeName(){
				this.syncUser()
			},
			syncUser(){
				this.$api.request('post', 'user/app/updateUser', this.userInfo).then(syncRes => {
					//同步过后
					uni.setStorageSync('userInfo', this.userInfo)
				})
			},
			 bindDateChange: function(e) {
				 console.log(e)
				this.userInfo.birthday = new Date(e.target.value).getTime()
				console.log(this.userInfo.birthday)
				this.date = e.target.value
				this.syncUser()
			},
			...mapMutations(['login'])
		}
	}
</script>

<style lang="scss">
	page {
		background: $page-color-base;
	}

	.solid-top{
		border-top: #F2F2F2 solid 2rpx;
	}

	.solid-bottom{
		border-bottom: #F2F2F2 solid 2rpx;
	}

	.padding-tb-s{
		padding: 15rpx 30rpx;
	}

	.title{
		font-size: 32rpx;
		line-height: 38rpx;
	}

	.content{
		font-size: 28rpx;
		line-height: 38rpx;
	}

	.nick-name{
		color: #2AAB34;
		font-size: 28rpx;
		line-height: 38rpx;
	}

	.icon-right{
		width: 14rpx;
		height: 27rpx;
		margin-left: 18rpx;
	}
</style>
