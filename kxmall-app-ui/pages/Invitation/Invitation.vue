<template>
	<view class="Invitation">
		<image src="https://s1.ax1x.com/2020/04/07/G2uBbd.png" mode="" style="width: 100%;height: 340px"></image>
		<view class="flex center" style="margin: -20px 0 0px;">
			<view class="flex column">
				<button open-type="share" class="round" style="position: relative;">
					<image src="https://lailemefresh.oss-cn-shanghai.aliyuncs.com/imgs/2YNSJW4NwsEf2ZD.png" mode="" style="width: 76px;height: 76px;"></image>
					<text style="position: absolute;top: 50px;left:0;width: 100%;font-size: 12px;">分享微信好友</text>
				</button>
			</view>
			<view class="" style="width: 32px;height: 0;">

			</view>
			<view class="flex column">
				<button  @tap="shareFc()" class="round" style="position: relative;">
					<image src="https://lailemefresh.oss-cn-shanghai.aliyuncs.com/imgs/AdRP3QjijAimkQZ.png" mode="" style="width: 76px;height: 76px;"></image>
					<text style="position: absolute;top: 50px;left:0;width: 100%;font-size: 12px;">分享朋友圈</text>
				</button>
			</view>


		</view>
		<view class="btnBox flex">
			<view class="box">
				<view class="title">
					我的邀请
				</view>
				<view class="content">
					<span class="number">{{invite_num}}</span>
					<span>人</span>
				</view>
				<view class="btn" @click="naviageToPage('/pages/InvitationList/InvitationList')">
					点击查看
				</view>
			</view>
			<view class="box">
				<view class="title">
					我的抢购券
				</view>
				<view class="content" style="color: #EE4F08;">
					<span class="number">{{coupen_num}}</span>
					<span>张</span>
				</view>
				<view class="btn" style="background-color: #EE4F08;" @click="naviageToPage('/pages/parity/PanicbuyingLIst')">
					前往抢购
				</view>
			</view>
		</view>
		<view class="listBig" style="width: 100%;">
			<image src="https://lailemefresh.oss-cn-shanghai.aliyuncs.com/imgs/5riYrpyWDsj5Bek.png" mode="" style="width: 100%;height: 160px;"></image>
			<view class="listBox">

				<view class="list" v-for="(item,i) in list" :key="i">
					<view class="img" style="width: 227rpx;height: 227rpx;">
						<image :src="item.img" mode="" style="width: 100%;height: 100%;"></image>
					</view>
					<view class="right">
						<view class="" style="font-weight: Bold;margin-top: 12px;">
							{{item.title}}
						</view>
						<view class="flex" style="justify-content: space-between;margin: 7.5px;align-items: flex-end;">
							<view class="" style="color: #999999;text-decoration: line-through;">
								市场价:{{item.otPrice}}
							</view>
							<view class="" style="font-size: 30px;color: #EE4F08;">
								{{item.kxStockVo.price}}
							</view>

						</view>
						<!-- 	<view class="btn">
							立即领券
						</view> -->
					</view>
				</view>
			</view>
		</view>
		<view class="flex_row_c_c modalView" :class="qrShow?'show':''" @tap="hideQr()">
			<view class="flex_column">
				<view class="backgroundColor-white padding1vh border_radius_10px">
					<image :src="poster.finalPath" mode="widthFix" class="posterImage"></image>
				</view>
				<view class="flex_row marginTop2vh">
					<button type="primary" size="mini" @tap.prevent.stop="saveImage()">保存图片</button>
					<!-- <button type="primary" size="mini" @tap.prevent.stop="share()">分享图片</button> -->
				</view>
			</view>
		</view>
		<!-- <button type="primary" @tap="shareFc()">生成海报</button> -->
		<view class="hideCanvasView">
			<canvas class="hideCanvas" canvas-id="default_PosterCanvasId" :style="{width: (poster.width||0) + 'px', height: (poster.height||0) + 'px'}"></canvas>
		</view>
	</view>
</template>

<script>
	import _app from '@/util/QS-SharePoster/app.js';
	import {
		getSharePoster
	} from '@/util/QS-SharePoster/QS-SharePoster.js';
	export default {
		data() {
			return {
				url: '',
				invite_num: 0,
				coupen_num: 0,
				list: [],
				p: '',
				poster: {},
				qrShow: false,
				canvasId: 'default_PosterCanvasId'
			};
		},
		mounted() {
			let that = this
			that.$api.request('get', 'inviteBuyingInfo', failres => {
				that.logining = false
				that.$api.msg(failres.msg)
			}).then(res => {
				console.log(res)
				that.p = res.data.p
				that.coupen_num = res.data.coupen_num
				that.invite_num = res.data.invite_num
			})
			that.$api.request('get', 'activityGoodsList', {
				activityType: 2,
				storageId: this.$store.state.storageId
			}, failres => {
				that.logining = false
				that.$api.msg(failres.msg)
			}).then(res => {
				console.log(res)
				that.list = res.data
			})
		},
		onLoad() {
		},

		onShareAppMessage() {
			return {
				title: '0.1限时大抢购',
				// imageUrl: this.goods.img,
				path: '/pages/share/inviteNewPeople?p=' + this.p,
			}
		},
		methods: {
			naviageToPage(page, title) {
				const that = this
				if (title) {
					console.log(title, 'jinlai')
					title ? title : ''
					uni.navigateTo({
						url: page + '&title=' + title
					})
				} else {
					console.log(page)
					uni.navigateTo({
						url: page
					})
				}
				// console.log(that.categoryButtomList[index].title)

			},
			share(type) {

			},
			async shareFc() {
				try {
					console.log('准备生成:' + new Date())
					const d = await getSharePoster({
						_this: this, //若在组件中使用 必传
						type: 'testShareType',
						formData: {
							//访问接口获取背景图携带自定义数据

						},
						posterCanvasId: this.canvasId,	//canvasId
						delayTimeScale: 20, //延时系数
						/* background: {
							width: 1080,
							height: 1920,
							backgroundColor: '#666'
						}, */
						drawArray: ({
							bgObj,
							type,
							bgScale
						}) => {
							const dx = bgObj.width * 0.3;
							const fontSize = bgObj.width * 0.045;
							const lineHeight = bgObj.height * 0.04;
							//可直接return数组，也可以return一个promise对象, 但最终resolve一个数组, 这样就可以方便实现后台可控绘制海报
							return new Promise((rs, rj) => {
								rs([{
										type: 'custom',
										setDraw(Context) {
											Context.setFillStyle('black');
											Context.setGlobalAlpha(0.3);
											Context.fillRect(0, bgObj.height - bgObj.height * 0.2, bgObj.width, bgObj.height * 0.2);
											Context.setGlobalAlpha(1);
										}
									},
									{
										type: 'image',
										url: 'https://lailemefresh.oss-cn-shanghai.aliyuncs.com/imgs/iWWHE7rtFYTnG8t.jpg',
										alpha: .3,
										dx,
										dy: bgObj.height - bgObj.width * 0.25,
										infoCallBack(imageInfo) {
											let scale = bgObj.width * 0.2 / imageInfo.height;
											return {
												circleSet: {
													x: imageInfo.width * scale / 2,
													y: bgObj.width * 0.2 / 2,
													r: bgObj.width * 0.2 / 2
												}, // 圆形图片 , 若circleSet与roundRectSet一同设置 优先circleSet设置
												dWidth: imageInfo.width * scale, // 因为设置了圆形图片 所以要乘以2
												dHeight: bgObj.width * 0.2,
												/* roundRectSet: { // 圆角矩形
													r: imageInfo.width * .1
												} */
											}
										}
									},
									{
										type: 'text',
										fontStyle: 'italic',
										text: '取舍',
										size: fontSize,
										color: 'white',
										alpha: .5,
										textAlign: 'left',
										textBaseline: 'middle',
										infoCallBack(textLength) {
											_app.log('index页面的text的infocallback ，textlength:' + textLength);
											return {
												dx: bgObj.width - textLength - fontSize,
												dy: bgObj.height - lineHeight * 3
											}
										},
										serialNum: 0,
										id: 'tag1'	//自定义标识
									},
									{
										type: 'text',
										text: '取舍',
										fontWeight: 'bold',
										size: fontSize,
										color: 'white',
										alpha: .75,
										textAlign: 'left',
										textBaseline: 'middle',
										serialNum: 1,
										allInfoCallback({	//v3.0.1 更新 可以获取drawArray中全部数据
											drawArray
										} = {}) {
											const obj = drawArray.find(item => item.id === 'tag1');
											/* return {
												dx: obj.dx,
												dy: obj.dy + lineHeight
											} */
											//也可以return promise对象
											return new Promise((rs, rj) => {
												setTimeout(() => {
													rs({
														dx: obj.dx,
														dy: obj.dy + lineHeight
													});
												}, 1);
											});
										}
									},
									{
										type: 'text',
										text: '取舍',
										size: fontSize,
										color: 'white',
										alpha: 1,
										textAlign: 'left',
										textBaseline: 'middle',
										infoCallBack(textLength) {
											return {
												dx: bgObj.width - textLength - fontSize,
												dy: bgObj.height - lineHeight
											}
										}
									},
									{
										type: 'image',
										url: 'https://lailemefresh.oss-cn-shanghai.aliyuncs.com/imgs/iWWHE7rtFYTnG8t.jpg',
										alpha: .3,
										dx: bgObj.width * 0.05,
										dy: bgObj.height - bgObj.width * 0.25,
									}
									// {
									// 	type: 'qrcode',
									// 	text: '你好，我是取舍',
									// 	size: bgObj.width * 0.2,
									// 	dx: bgObj.width * 0.05,
									// 	dy: bgObj.height - bgObj.width * 0.25
									// }
								]);
							})
						},
						setCanvasWH: ({
							bgObj,
							type,
							bgScale
						}) => { // 为动态设置画布宽高的方法，
							this.poster = bgObj;
						}
					});
					console.log('海报生成成功, 时间:' + new Date() + '， 临时路径: ' + d.poster.tempFilePath)
					this.poster.finalPath = d.poster.tempFilePath;
					this.qrShow = true;
				} catch (e) {
					_app.hideLoading();
					_app.showToast(JSON.stringify(e));
					console.log(JSON.stringify(e));
				}
			},
			saveImage() {
				// #ifndef H5
				uni.saveImageToPhotosAlbum({
					filePath: this.poster.finalPath,
					success(res) {
						_app.showToast('保存成功');
					}
				})
				// #endif
				// #ifdef H5
				_app.showToast('保存了');
				// #endif
			},
			share() {
				// #ifdef APP-PLUS
				_app.getShare(false, false, 2, '', '', '', this.poster.finalPath, false, false);
				// #endif

				// #ifndef APP-PLUS
				_app.showToast('分享了');
				// #endif
			},
			hideQr() {
				this.qrShow = false;
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F4F6FA;
	}

	.Invitation {
		// min-height: 100vh;

		.round {
			background: #F4F6FA;
			border: none;
			outline: none;
			width: 76px;
			height: 100px;
			border: none !important;
			padding: 0;
		}

		button::after {
			border: none !important;
		}

		.column {
			flex-flow: column;
			align-items: center;
			font-size: 12px;
		}

		.center {
			justify-content: center;
		}

		.btnBox {
			width: calc(100% - 30px);
			background-color: #fff;
			box-shadow: 1px 1px 10px -6px #404044;
			border-radius: 10px;
			padding: 20px 0 15px;
			position: relative;
			margin: 0 auto;

			&::after {
				display: block;
				content: '';
				width: 1px;
				height: 117px;
				background-color: #D8D9DE;
				position: absolute;
				top: 50%;
				left: 50%;
				transform: translate(-50%, -50%);
			}

			.title {
				color: #4E4C49;
				font-weight: bold;
			}

			.box {
				// height: 156rpx;
				width: 50%;
				display: flex;
				flex-flow: column;
				justify-content: space-around;
				align-items: center;
				text-align: center;

				.content {
					color: #B47D3C;

					.number {
						font-size: 100rpx;
					}
				}

				.btn {
					width: 100px;
					height: 30px;
					background-color: #D39F62;
					text-align: center;
					line-height: 30px;
					color: #fff;
					border-radius: 30rpx;
				}
			}
		}

		.listBig {
			position: relative;
			padding: 0 30rpx 18px;
			margin-top: 10rpx;

			.listBox {
				top: 60px;
				left: 50%;
				transform: translateX(-50%);
				width: calc(100% - 59rpx);
				position: absolute;
			}

			.list {
				width: 100%;
				display: flex;
				align-items: center;
				background-color: #fff;
				border-bottom: 1px solid #F7F4F8;
				padding: 22rpx;

				.img {
					margin-right: 46rpx;
				}

				.right {
					.btn {
						// width: 230rpx;
						// height: 60rpx;
						border: 1px solid #EE4F08;
						color: #EE4F08;
						border-radius: 50px;
						padding: 7rpx 37rpx;
						padding-right: 71rpx;
						position: relative;
						max-width: 120px;
						margin: 26rpx 0;

						&::after {
							display: block;
							content: '';
							width: 42rpx;
							height: 40rpx;
							position: absolute;
							top: 50%;
							right: 10px;
							transform: translateY(-50%);
							background: url(../../static/youhui.png) no-repeat center;
							background-size: 100% 100%;
						}
					}
				}
			}
		}


	}
	// 海报滴
	.hideCanvasView {
		position: relative;
	}

	.hideCanvas {
		position: fixed;
		top: -99999upx;
		left: -99999upx;
		z-index: -99999;
	}

	.flex_row_c_c {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.modalView {
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		opacity: 0;
		outline: 0;
		transform: scale(1.2);
		perspective: 2500upx;
		background: rgba(0, 0, 0, 0.6);
		transition: all .3s ease-in-out;
		pointer-events: none;
		backface-visibility: hidden;
		z-index: 999;
	}

	.modalView.show {
		opacity: 1;
		transform: scale(1);
		pointer-events: auto;
	}

	.flex_column {
		display: flex;
		flex-direction: column;
	}

	.backgroundColor-white {
		background-color: white;
	}

	.border_radius_10px {
		border-radius: 10px;
	}

	.padding1vh {
		padding: 1vh;
	}

	.posterImage {
		width: 60vw;
	}

	.flex_row {
		display: flex;
		flex-direction: row;
	}

	.marginTop2vh {
		margin-top: 2vh;
	}
</style>
