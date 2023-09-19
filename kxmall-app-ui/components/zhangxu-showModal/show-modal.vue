<template>
	<div class="_showModal" v-show="show">
		<div class="_shade"></div>
		<div class="_modalBox" @click="closeModal" @touchmove.stop.prevent="">
			<div class="_modal">
				<slot name="title">
					<div class="title" v-show="title">{{title}}</div>
				</slot>
				<slot name="content">
					<div class="content">
						{{content}}
					</div>
				</slot>
				<slot name="btn">
					<div class="btnbox">
						<div class="cancel btn" v-show="showCancel" :style="cancelColor" @click.stop="clickBtn('cancel')">{{cancelText}}</div>
						<div class="confirm btn" :style="confirmColor" @click.stop="clickBtn('confirm')">{{confirmText}}</div>
					</div>
				</slot>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	computed: {
		show(){
			return this.$modalStore.state.show;
		},
		title(){
			return this.$modalStore.state.title;
		},
		content(){
			return this.$modalStore.state.content;
		},
		showCancel(){
			return this.$modalStore.state.showCancel;
		},
		cancelText(){
			return this.$modalStore.state.cancelText;
		},
		cancelColor(){
			return "color:"+this.$modalStore.state.cancelColor;
		},
		confirmText(){
			return this.$modalStore.state.confirmText;
		},
		confirmColor(){
			return "color:"+this.$modalStore.state.confirmColor;
		}
	},
	methods:{
		closeModal(){
			this.$modalStore.commit('hideModal')
		},
		clickBtn(res){
			this.$modalStore.commit('hideModal')
			this.$modalStore.commit('success',res)
		}
	},
	beforeDestroy(){
		this.$modalStore.commit('hideModal')
	}
};
</script>

<style lang="scss" scoped>
	._showModal{
		position: fixed;
		top: 0;
		left:0;
		width: 100vw;
		height: 100vh;
		z-index:10000;
		._shade{
			width: 100%;
			height: 100%;
			position: absolute;
			top: 0;
			left: 0;
			background: #000;
			opacity: .6;
			z-index:11000;
		}
		._modalBox{
			width: 100%;
			height: 100%;
			position: absolute;
			top: 0;
			left: 0;
			z-index:12000;
			display: flex;
			justify-content: center;
			align-items: center;
			._modal{
				flex: none;
				width:70%;
				// min-height:200upx;
				background: #fff;
				border-radius: 10upx;
				.title{
					height: 40upx;
					line-height: 40upx;
					text-align: center;
					font-size: 26upx;
					font-weight: bold;
					padding: 15upx 0 0;
					// border-bottom: 1upx solid #e1e1e1;
				}
				.content{
					padding:30upx;
					font-size: 24upx;
					color: #666;
					line-height: 35upx;
					text-align: center;
				}
				.btnbox{
					display: flex;
					.btn{
						font-size: 30upx;
						text-align: center;
						flex: auto;
						line-height: 80upx;
						border-top: 1upx solid #e1e1e1;
						border-right: 1upx solid #e1e1e1;
					}
					.btn:last-child{
						border-right:none;
					}
					.cancel{
						
					}
				}
			}
		}
		
	}
</style>
