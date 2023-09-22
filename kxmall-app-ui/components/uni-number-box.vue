<template>
	<view class="uni-numbox">
		<view class="uni-numbox-minus" 
			@click.stop="_calcValue('subtract')"
		>
			<text class="yticon icon--jianhao" :class="minDisabled?'uni-numbox-disabled': ''" ></text>
		</view>
		<input 
			class="uni-numbox-value" 
			type="number" 
			:disabled="true"
			v-model="inputValue" 
			@focus.stop="_onfocus"
			@blur.stop="_onBlur"
			@click.stop="_onClick"
		>
		<view 
			class="uni-numbox-plus" 
			@click.stop="_calcValue('add')"
		>
			<text class="yticon icon-jia2" :class="maxDisabled?'uni-numbox-disabled': ''" ></text>
		</view>
		<!-- {{forbid}} -->
	</view>
</template>
<script>
	export default {
		name: 'uni-number-box',
		props: {
			isMax: {
				type: Boolean,
				default: false
			},
			isMin: {
				type: Boolean,
				default: false
			},
			index: {
				type: Number,
				default: 0
			},
			forbid: {
				type: Number,
				default: 0
			},
			value: {
				type: Number,
				default: 0
			},
			min: {
				type: Number,
				default: -Infinity
			},
			max: {
				type: Number,
				default: Infinity
			},
			step: {
				type: Number,
				default: 1
			},
			disabled: {
				type: Boolean
			}
		},
		data() {
			return {
				inputValue: this.value,
				minDisabled: false,
				maxDisabled: false
			}
		},
		created(){
			this.maxDisabled = this.isMax;
			this.minDisabled = this.isMin;
			console.log(this.forbid)
		},
		computed: {
				
		},
		watch: {
			inputValue(number) {
				const data = {
					number: number,
					index: this.index
				}
				this.$emit('eventChange', data);
			},
			value(number){
				this.inputValue = number;
			}
		},
		methods: {
			_onfocus(){},
			_onClick(){},
			_calcValue(type) {
				if(this.forbid !== 0){
					uni.showToast({
						title:"活动商品仅能购买一件",
						icon:"none"
					})
					return
				}
				const scale = this._getDecimalScale();
				let value = this.inputValue * scale;
				let newValue = 0;
				let step = this.step * scale;
				if(type === 'subtract'){
					newValue = value - step;
					if (newValue <= this.min){
						this.minDisabled = true;
					}
					if(newValue < this.min){
						newValue = this.min
					}
					if(newValue < this.max && this.maxDisabled === true){
						this.maxDisabled = false;
					}
				}else if(type === 'add'){
					newValue = value + step;
					if (newValue >= this.max){
						this.maxDisabled = true;
					}
					if(newValue > this.max){
						newValue = this.max
					}
					if(newValue > this.min && this.minDisabled === true){
						this.minDisabled = false;
					}
				}
				if(newValue === value){
					return;
				}
				this.inputValue = newValue / scale; 
			},
			_getDecimalScale() {
				let scale = 1;
				// 浮点型
				if (~~this.step !== this.step) {
					scale = Math.pow(10, (this.step + '').split('.')[1].length);
				}
				return scale;
			},
			_onBlur(event) {
				let value = event.detail.value;
				if (!value) {
					this.inputValue = 0;
					return
				}
				value = +value;
				if (value > this.max) {
					value = this.max;
				} else if (value < this.min) {
					value = this.min
				}

				this.inputValue = value
			}
		}
	}
</script>
<style>
	.uni-numbox {
		/* position:absolute; */
		/* left: 30upx; */
		bottom: 0;
		display: flex;
		justify-content: flex-start;
		align-items: center;
		height: 46rpx;
		/* width:230upx; */
		/* height: 70upx; */
		/* background:#f5f5f5; */
	}

	.uni-numbox-minus,
	.uni-numbox-plus {
		margin: 0;
		/* background-color: #f5f5f5; */
		width: 70upx;
		/* height: 100%; */
		height: 46rpx;
		/* padding: 0 10rpx; */
		/* line-height: 70upx; */
		text-align: center;
		position: relative;
	}
	.uni-numbox-minus .yticon,
	.uni-numbox-plus .yticon{
		font-size: 36upx;
		color: #555;
	}

	.uni-numbox-minus {
		border-right: none;
		border-top-left-radius: 22upx;
		border-bottom-left-radius: 22upx;
		border: #EEEAEA solid 2rpx;
	}

	.uni-numbox-plus {
		border-left: none;
		border-top-right-radius: 22upx;
		border-bottom-right-radius: 22upx;
		border: #EEEAEA solid 2rpx;
	}

	.uni-numbox-value {
		position: relative;
		/* background-color: #f5f5f5; */
		width: 90upx;
		color: #1B1C33;
		border-top: #EEEAEA solid 2rpx;
		border-bottom: #EEEAEA solid 2rpx;
		/* height: 50upx; */
		height: 46rpx;
		text-align: center;
		padding: 0;
		font-size: 32upx;
		line-height: 44rpx;
	}

	.uni-numbox-disabled.yticon {
		color: #d6d6d6;
	}
</style>
