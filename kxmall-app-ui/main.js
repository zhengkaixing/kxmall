/**
 * uni-app应用主入口文件
 * 负责初始化Vue应用、注册全局组件、挂载全局方法等
 * @author kxmall
 * @date 2024
 */
import Vue from 'vue'
import store from './store'
import App from './App'

// 导入过滤器和配置
import * as filters from './filters'
import * as config from './config'

/**
 * 自定义Modal组件注册
 * 用于全局弹窗提示
 */
import initModal from "@/components/zhangxu-showModal/initModal.js";
import showModal from '@/components/zhangxu-showModal/show-modal';
initModal(Vue);
Vue.component('show-modal', showModal);

/**
 * 注册全局过滤器
 * 将filters目录下的所有过滤器注册到Vue实例
 */
Object.keys(filters).forEach(key => {
	Vue.filter(key, filters[key])
})

//#ifdef H5
/**
 * H5平台微信相关模块
 * 仅在H5平台编译时引入
 */
let jweixin = require('./components/jweixin-module')
let jwx = require('./components/jweixin-module/jwx')
Vue.mixin({
	onShow() {
		// 页面显示时的微信配置（已注释，可根据需要启用）
		// jwx.configWeiXin(jwx => {
		// })
	}
})
//#endif

/**
 * 获取默认配置
 */
const defConfig = config.def

/**
 * 统一消息提示方法
 * 封装uni.showToast，方便全局统一修改提示样式
 * @param {string} title - 提示内容
 * @param {number} duration - 显示时长，默认1500ms
 * @param {boolean} mask - 是否显示透明蒙层，默认false
 * @param {string} icon - 图标类型，默认'none'
 */
const msg = (title, duration = 1500, mask = false, icon = 'none') => {
	// 统一提示方便全局修改
	if (Boolean(title) === false) {
		return;
	}
	uni.showToast({
		title,
		duration,
		mask,
		icon
	});
}

/**
 * 用户信息缓存
 * 用于存储当前登录用户的信息
 */
let userInfo = undefined

/**
 * 用户登出方法
 * 清除用户信息和本地存储
 */
const logout = () => {
	userInfo = undefined
	uni.removeStorage({
		key: 'userInfo'
	})
}

/**
 * 设置用户信息
 * @param {Object} i - 用户信息对象
 */
const setUserInfo = (i) => {
	userInfo = i
}

/**
 * 判断用户是否为VIP
 * @returns {boolean} 是否为VIP用户
 */
const isVip = () => {
	return userInfo && userInfo.level
}

/**
 * 登录锁定标识
 * 防止重复弹出登录提示框
 */
let loginLock = false

/**
 * 统一请求方法
 * 封装uni.request，统一处理请求、响应、错误处理等
 * @param {string} method - 请求方法（get/post/put/delete等）
 * @param {string} endpoint - 接口地址
 * @param {Object} data - 请求参数，默认空对象
 * @param {Function} failCallback - 失败回调函数
 * @returns {Promise} 请求Promise
 */
const request = (method, endpoint, data = {}, failCallback) => {
	// 异步请求数据
	return new Promise(resolve => {
		// 从本地存储获取用户信息和token
		userInfo = uni.getStorageSync('userInfo')
		let accessToken = userInfo ? userInfo.accessToken : ''
		let addresses = uni.getStorageSync('addresses') || '';
		let baseUrl = config.def().baseUrl
		
		// 根据请求方法设置Content-Type
		let content = 'application/x-www-form-urlencoded; charset=UTF-8';
		if (method === 'post') {
			content = 'application/json; charset=UTF-8';
		}
		
		uni.request({
			url: baseUrl + "/" + endpoint,
			method: method,
			data: data,
			header: {
				'Content-Type': content,
				'Authorization': 'Bearer ' + accessToken
			},
			success: (res) => {
				// 200: 请求成功
				if (res.data.code === 200) {
					// 如果用户已登录但未选择地址，提示选择地址
					if (!addresses && accessToken) {
						uni.showModal({
							title: '地图提示',
							content: '您尚未选择，您的位置？',
							showCancel: true,
							confirmText: '选择',
							success: (e) => {
								if (e.confirm) {
									// 用户确认选择位置
									uni.chooseLocation({
										success: (res1) => {
											console.log('选择位置成功:', res1)
											if (res1.name === "") {
												return
											}
											// 保存地址信息到本地存储
											let addressesInfo = {};
											addressesInfo.lng = res1.longitude;
											addressesInfo.lat = res1.latitude;
											addressesInfo.addressesName = res1.name;
											uni.setStorageSync('addresses', addressesInfo);
											// 重新加载首页
											uni.reLaunch({
												url: '/pages/index/index?type=1'
											});
										},
										fail: (e) => {
											console.error('选择位置失败:', e)
										}
									});
								}
							},
							fail: () => {}
						})
					}
					resolve(res.data);
				} 
				// 401: 未授权，需要登录
				else if (res.data.code === 401) {
					// 执行失败回调
					if (failCallback) {
						failCallback(res.data)
					}
					// 防止重复弹出登录提示
					if (!loginLock) {
						loginLock = true
						uni.showModal({
							title: '登录提示',
							content: '您尚未登录，是否立即登录？',
							showCancel: true,
							confirmText: '登录',
							success: (e) => {
								if (e.confirm) {
									// 跳转到登录页
									uni.navigateTo({
										url: '/pages/public/login'
									})
								}
							},
							fail: () => {},
							complete: () => {
								// 无论成功或失败，都要解锁
								loginLock = false
							}
						})
					}
				} 
				// 其他错误码
				else {
					// 执行失败回调
					if (failCallback) {
						failCallback(res.data)
					} else {
						// 显示错误提示
						uni.showToast({
							title: res.data.msg,
							icon: 'none'
						})
					}
				}
			},
			fail: (err) => {
				// 请求失败处理
				console.error('请求失败:', err)
				if (failCallback) {
					failCallback(err)
				} else {
					uni.showToast({
						title: '网络请求失败，请检查网络连接',
						icon: 'none'
					})
				}
				resolve(null)
			}
		})
	})
}

/**
 * 上传图片方法
 * @param {number} num - 可选择图片数量，默认9（当前实现为1）
 * @param {Function} successCallback - 上传成功回调函数，参数为图片URL
 */
const uploadImg = (num = 9, successCallback) => {
	// 获取用户信息和token
	userInfo = uni.getStorageSync('userInfo');
	var accessToken = userInfo ? userInfo.accessToken : '';
	let baseUrl = config.def().baseUrl
	
	// 选择图片
	uni.chooseImage({
		count: 1, // 当前实现为选择1张图片
		sourceType: ['album'], // 从相册选择
		success: res => {
			// 获取图片信息
			uni.getImageInfo({
				src: res.tempFilePaths[0],
				success: image => {
					console.log('图片信息:', image)
					// 显示上传加载提示
					uni.showLoading({ title: '图片上传中', mask: true })
					// 上传文件
					uni.uploadFile({
						url: baseUrl + `/oss/app/upload`,
						file: image,
						filePath: image.path,
						header: {
							Authorization: 'Bearer ' + accessToken,
						},
						name: 'file', // 文件对应的key
						success: res => {
							// 解析响应数据并执行回调
							if (successCallback) {
								successCallback(JSON.parse(res.data).data.url)
							}
						},
						fail: err => {
							// 上传失败提示
							console.error('图片上传失败:', err)
							uni.showToast({
								title: '上传图片失败',
								icon: 'none',
								duration: 2000,
							})
						},
						complete: res => {
							// 无论成功或失败，都隐藏加载提示
							uni.hideLoading()
						},
					})
				},
				fail: err => {
					// 获取图片信息失败
					console.error('获取图片信息失败:', err)
					uni.showToast({
						title: '获取图片信息失败',
						icon: 'none',
						duration: 2000,
					})
				},
			})
		},
		fail: err => {
			// 选择图片失败
			console.error('选择图片失败:', err)
			uni.showToast({
				title: '选择图片失败',
				icon: 'none',
				duration: 2000,
			})
		}
	})
}

/**
 * 获取文件后缀名
 * @param {string} filename - 文件名
 * @returns {string} 文件后缀名（包含点号）
 */
function get_suffix(filename) {
	var pos = filename.lastIndexOf('.')
	var suffix = ''
	if (pos != -1) {
		suffix = filename.substring(pos)
	}
	return suffix;
}

/**
 * 生成随机字符串
 * @param {number} len - 字符串长度，默认32
 * @returns {string} 随机字符串
 */
function random_string(len) {
	len = len || 32;
	// 排除容易混淆的字符（0、O、1、I、l等）
	var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
	var maxPos = chars.length;
	var pwd = '';
	for (var i = 0; i < len; i++) {
		pwd += chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return pwd;
}

/**
 * 获取上一页页面实例
 * @returns {Object} 上一页的页面实例
 */
const prePage = () => {
	let pages = getCurrentPages();
	let prePage = pages[pages.length - 2];
	// #ifdef H5
	// H5平台直接返回页面对象
	return prePage;
	// #endif
	// 其他平台返回页面实例
	return prePage.$vm;
}

/**
 * 全局数据对象
 * 用于存储全局共享的数据
 */
const globalData = {}

/**
 * 关闭生产环境提示
 */
Vue.config.productionTip = false

/**
 * 全局事件总线
 * 用于组件间通信
 */
Vue.prototype.$fire = new Vue();

/**
 * 挂载Vuex store
 */
Vue.prototype.$store = store;

/**
 * 挂载全局API方法
 * 在组件中可通过 this.$api.xxx 调用
 */
Vue.prototype.$api = {
	msg, // 消息提示
	prePage, // 获取上一页
	request, // 请求方法
	uploadImg, // 上传图片
	logout, // 登出
	isVip, // 判断是否VIP
	setUserInfo, // 设置用户信息
	defConfig, // 默认配置
	globalData // 全局数据
};

//#ifdef H5
/**
 * H5平台微信相关API
 */
Vue.prototype.$jweixin = jweixin;
//#endif

/**
 * 统一获取地理信息方法
 * 获取当前位置的经纬度和地址信息（通过高德地图API）
 * @param {boolean} isChoose - 是否使用chooseLocation接口返回的经纬度，该接口返回的address没做省市区划分
 * @param {string} latitude - 纬度（当isChoose为true时使用）
 * @param {string} longitude - 经度（当isChoose为true时使用）
 * @returns {Object|boolean} 返回地理信息对象，失败返回false
 */
Vue.prototype.$getLocation = async (isChoose = false, latitude = '', longitude = '') => {
	var ret = {}
	
	// 如果使用chooseLocation的经纬度
	if (isChoose) {
		ret.latitude = latitude
		ret.longitude = longitude
	} else {
		// 获取当前位置
		var [err, res] = await uni.getLocation({
			type: 'wgs84' // 使用wgs84坐标系
		})
		if (res && res.errMsg === 'getLocation:ok') {
			ret.latitude = res.latitude
			ret.longitude = res.longitude
		} else {
			console.error('获取位置失败:', err.errMsg)
			return false
		}
	}
	
	// 调用高德地图逆地理编码API获取地址信息
	var [error, geocodeData] = await uni.request({
		url: "https://restapi.amap.com/v3/geocode/regeo",
		method: "GET",
		data: {
			key: 'a0096c306be491b44b6ffc21c3af9dd4', // 高德地图API Key
			location: ret.longitude + ',' + ret.latitude, // 经纬度（经度在前）
			extensions: 'all', // 返回详细信息
			// poitype:'120000|060000' // 返回poi限定范围（已注释）
		}
	})
	
	if (geocodeData.statusCode === 200) {
		console.log('地理编码数据:', geocodeData)
		var data = geocodeData.data.regeocode.addressComponent
		// 将地址组件信息合并到返回对象
		for (var key in data) {
			ret[key] = data[key]
		}
		// 添加POI（兴趣点）信息
		ret.pois = geocodeData.data.regeocode.pois
		return ret
	} else {
		console.error('地理编码失败:', error)
		return false
	}
}

/**
 * 设置应用类型
 */
App.mpType = 'app'

/**
 * 创建Vue实例并挂载
 */
const app = new Vue({
	...App
})
app.$mount()
