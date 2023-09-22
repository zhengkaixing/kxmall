<template>
	<view style="background-color: #fff;">
		<view class="padding-lr" style="padding-bottom: 22rpx;padding-top: 24rpx;background-color: #fff;" v-if="iste">
			<view @click="naviageToPage('/pages/product/search')" style="height: 64rpx;border: 1px solid #C4C4CC;" class="bg-white round flex align-center">
				<view class="flex align-center padding-left">
					<text class="yticon icon-sousuo" style="margin-right: 10rpx;"></text><text class="lem-text-gray"
					style="font-size: 26rpx;">新鲜，搜一下就涞了</text>
				</view>
			</view>
		</view>
		<view class="flex justify-center">
			<scroll-view scroll-with-animation scroll-x :scroll-into-view="curentTabView"
			class="bg-white"
			style="width:90%;height: 100rpx;overflow-x: scroll;" v-if="iste">
				<view class="flex  align-start">
					<view v-for="item in slist" :key="item.id"
					@click="sTabTap(item)" :id="'id'+item.id"
					:class="{actived: item.id == currentIded}"
					class="flex align-center justify-center margin-left-sm"
					style="padding: 20rpx 0; box-sizing: border-box;width: auto;flex-shrink: 0;"
					>
						<view style="font-size: 28rpx;line-height: 40rpx;"
						>{{item.title}}</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<view style="padding: 40rpx 30rpx;" >
			<image src="https://s1.ax1x.com/2020/04/07/G2usUI.png" mode="aspectFill"
			style="width: 690rpx;height: 170rpx;border-radius: 12rpx;"></image>
		</view>
		<view class="" style="background-color: #fff;padding: 8px 20px 0; ">
			<view v-for="(item,index) in tlist" :key="index"
			@click="navToDetail(item.productId)"
			class=" padding-tb-sm padding-lr-xs flex"
			style="width: 100%;height: 180rpx;border-bottom: solid #E6E6E6 1rpx;">
				<image :src="JSON.parse(item.img)[0].url" mode="aspectFit" class="round"
				style="width: 144rpx;height: 144rpx;margin-right: 50rpx;"></image>
				<view style="padding-top: 4rpx;padding-bottom: 2rpx;flex-grow: 1;">
					<view class="text-black padding-bottom-xs text-cut"
					style="font-size: 30rpx;line-height: 42rpx;width: 300rpx;"
					>{{item.title}}</view>
					<!-- <view class="text-gray text-cut"
					style="font-size: 24rpx;line-height: 34rpx;width: 300rpx;"
					>{{item.description}}</view> -->
					<view class="flex justify-between" style="width: 100%;padding-top: 12rpx;">
						<view style="flex-grow: 1;" class="flex align-center">
							<view class="text-black padding-right-sm"
							style="font-size: 30rpx;line-height: 42rpx;"
							>￥{{item.price}}</view>

							<view class="text-black padding-right-sm"
							style="font-size: 30rpx;line-height: 42rpx;text-decoration:line-through;color: #8D8E99;  "
							>￥{{item.originalPrice}}</view>
						</view>
						<uni-number-box
							class="number-box"
							:min="0"
							:value="item.num"
							:isMin="item.num===0"
							:index="index"
							@eventChange="numberChange"
							v-if="item.num!==0"
						></uni-number-box>
						<image @click.stop="addCart(index)" src="/static/add.png" mode="aspectFit"
						style="width: 44rpx;height: 44rpx;" v-if="item.num==0"></image>
					</view>
				</view>
			</view>
		</view>
		<view style="position: relative;height: 100rpx;"></view>
		<view class="bg-white flex align-end"
		style="position: fixed;bottom: 0;width: 750rpx;height: 98rpx;padding-left: 38rpx;">
			<view @click="navCart" class="flex align-end" style="width: 346rpx;height: 98rpx;padding-bottom: 16rpx;">
				<view style="position: relative;">
					<image src="/static/tab-icon/tab3_2.png" mode="aspectFit"
					style="width: 56rpx;height: 52rpx;" ></image>
					<view v-if="cartNum+cartNums" class="badge round"
					>{{cartNum+cartNums}}</view>
				</view>
				<view style="font-size: 28rpx;line-height: 40rpx;color: #8D8E99;padding-left: 40rpx;">
					已选{{cartNum+cartNums}}件
				</view>
			</view>
			<view @click="navToCart"  style="font-size: 28rpx;line-height: 40rpx;width: 404rpx;height: 98rpx;"
			 class="flex align-center justify-center bg-green ">
				去购物车
			</view>
		</view>
	</view>
</template>

<script>
	import uniNumberBox from '@/components/uni-number-box.vue'
	export default {
		data() {
			return {
				slist: [{
					id: 2,
					title: '平价菜场',
				}, {
					id: 3,
					title: '鲜肉蛋',
				}, {
					id:4,
					title: '新鲜水果',
				},
				{
					id:5,
					title: '活鱼河虾',
				}],
				currentIded: 32,
				sTitle: '',
				tlist:[],
				cartNum:0,
				cartNums:0,
				recommendType:2,
				enumsList:[],
				curentTabView:0,
				iste:true,
				banner:{
					imgUrl:''
				},
				total:0,
				pageNo:1
			}
		},
		components: {
			uniNumberBox
		},
		onLoad(option) {
			console.log(option)
			if(option.title!='今日特价' && option.title != '热卖推荐'){
				this.recommendType=option.type
				this.currentIded=option.id
				this.curentTabView='id'+option.id
				// this.getRecommendByStorage()
				this.slist = uni.getStorageSync('categoryButtomList');
				this.getRecommendTypeEnums()
			}else if(option.title == '今日特价'){
				this.iste=false
				this.recommendType=1
			}else if(option.title == '热卖推荐'){
				this.iste=false
				this.recommendType=11
			}
			this.countCart()
			this.getRecommendByStorage('refresh')
			uni.setNavigationBarTitle({
			    title: option.title
			});
			this.$api.request('get','carousel/app/getCarouselActive',{
				adType:10
			}).then(res=>{
				this.banner = res.data[0]
			})
		},
		onShow() {

		},
		onReachBottom() {
			if(this.tlist.length >= this.total){
				return
			}
			this.getRecommendByStorage()
		},
		watch:{

		},
		methods: {
			naviageToPage(page,title) {
				const that = this
				if(title){
					console.log(title,'jinlai')
					 title?title:''
					 uni.navigateTo({
					 	url: page
					 })
				}else{
					console.log(title)
					uni.navigateTo({
						url: page
					})
				}
				// console.log(that.categoryButtomList[index].title)

			},
			getRecommendTypeEnums(){
				this.$api.request('get','recommend/app/getRecommendTypeEnums').then(res=>{
					console.log(res )
					this.enumsList=res.data

				}).catch(err=>{
					this.$api.msg('请求失败，请稍后再试')
				})
			},
			getRecommendByStorage(loadMore = ''){
				console.log(this.recommendType,'this.recommendType')
				if (loadMore == 'refresh') {
					this.pageNo = 1
				}
				this.$api.request('get','storage/position/getRecommendByStorage',{
					storageId: this.$store.state.storageId,
					recommendType:this.recommendType,
					pageNo:this.pageNo
				}).then(res=>{
					console.log(res )
					this.pageNo++
					let tempList = res.data.rows
					if (loadMore == 'refresh') {
						this.tlist = [];
					}
					this.tlist = this.tlist.concat(tempList)
					this.total = res.total
					for (let i = 0; i < this.tlist.length; i++) {
						this.tlist[i].num=0
					}
				}).catch(err=>{
					this.$api.msg('请求失败，请稍后再试')
				})
			},
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
			navToDetail(id){
				uni.navigateTo({
					url:`../product/detail?id=${id}`
				})
			},
			navToCart(id){
				// console.log()
				uni.switchTab({
					url:`../cart/cart`
				})
			},
			cartNumFn(){
				let cartNum=0
				this.tlist.forEach((item,i)=>{
					cartNum+=item.num
				})
				this.cartNums=cartNum
			},

			addCart(index){
				this.tlist[index].num++
				this.cartNumFn()
				const that = this
				that.$api.request('get', 'cart/app/addCartItem', {
					productId: this.tlist[index].productId,
					num: 1,
					activityId:this.tlist[index].activityId,
					couponId:this.tlist[index].couponId
				}).then(res => {
					that.$api.msg('添加购物车成功')
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
				// console.log(data)
				this.tlist[data.index].num=data.number
				this.cartNumFn()

				that.$api.request('get', 'cart/app/addCartItem', {
					productId: this.tlist[data.index].productId,
					activityId:this.tlist[data.index].activityId,
					couponId:this.tlist[data.index].couponId,
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
				// this.addCart(data.index)
			},
			//二级分类点击
			sTabTap(item) {
				console.log(this.enumsList)
				for (let i = 0; i < this.enumsList.length; i++) {
					if(this.enumsList[i].dictLabel==item.title){
						this.recommendType=this.enumsList[i].dictValue
						console.log(this.recommendType,this.enumsList[i].dictValue)
					}
				}
				this.curentTabView='id'+item.id
				this.currentIded = item.id
				this.sTitle = item.title //二级分类不用显示这个东西

				this.getRecommendByStorage('refresh')
			},
		}
	}
</script>

<style lang='scss'>
	/* page,
	.content {
		background-color: #fff;


	} */
	.actived {
		font-size: 28rpx;
		color: #2AAC34;
		position: relative;
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
	.actived:after {
		display: block;
		content: '';
		width: 54rpx;
		height: 8rpx;
		background-color: #2AAC34;
		position: absolute;
		bottom: -10rpx;
		left: 50%;
		transform: translateX(-50%);
		border-radius: 2px;
	}
	.number-box{
		width: 73px;
	}
</style>
