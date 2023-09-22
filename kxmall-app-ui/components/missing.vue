<template name="Mssing">
	<view  class="app">
	    <view class="mssing_top">
			<image class="mssing_top_img" :src="imgUrl"></image>
		</view>
		<view class="mssing_desc">
			{{desc}}
		</view>
		<button v-if="handlerName != ''" @click="handler"
		class="lem-btn round line-green" style="font-size: 28rpx;margin-top: 57rpx;">{{buttonName}}</button>
	</view>
</template>
<script>
export default {
	name: "missing",
	//属性
	props: {
		imgUrl: {
			type: String,//属性类型
			default: ""
		},
		desc:{
			type:String,
			default:""
		},
		handlerName:{
			type:String,
			default:""
		},
		buttonName:{
			type:String,
			default:""
		}
	},
	//组件生命周期
	created:function(e){
	
	},
	methods: {
		handler(){
			this.$emit(this.handlerName,1)
		}
	}
}
</script>
<style scoped>
 page{
	 background-color: #F6F5FA;
 }
 
 .app{
	 width: 100%;
	 display: flex;
	 justify-content: center;
	 align-items: center;
	 flex-direction: column;
 }
 
 .line-green::after {
   border-color: #2AAC34;
 }
 .mssing_top{
	 width: 419rpx;
	 height: 253rpx;
 }
 .mssing_top_img{
	 width: 419rpx;
	 height: 253rpx;
 }
 .mssing_desc{
	 padding-top: 106rpx;
	 font-size: 28rpx;
	 color: #8D8E99;
 }
</style>