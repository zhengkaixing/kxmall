import Vue from 'vue'
import store from './store'
import App from './App'

import * as filters from './filters'
import * as config from './config'

//自定义modal
import initModal from "@/components/zhangxu-showModal/initModal.js";
import showModal from '@/components/zhangxu-showModal/show-modal';
initModal(Vue);
Vue.component('show-modal',showModal);

Object.keys(filters).forEach(key => {
	Vue.filter(key, filters[key])
})

//#ifdef H5
let jweixin = require('./components/jweixin-module')
let jwx = require('./components/jweixin-module/jwx')
Vue.mixin({
	onShow() {
		// jwx.configWeiXin(jwx => {
		// })
	}
})
//#endif

const defConfig = config.def

const msg = (title, duration = 1500, mask = false, icon = 'none') => {
	//统一提示方便全局修改
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

let userInfo = undefined

const logout = () => {
	userInfo = undefined
	uni.removeStorage({
		key: 'userInfo'
	})
}

const setUserInfo = (i) => {
	userInfo = i
}

const isVip = () => {
	return userInfo && userInfo.level
}

let loginLock = false

const request = (method, endpoint, data = {}, failCallback) => {
	//异步请求数据
	return new Promise(resolve => {
			userInfo = uni.getStorageSync('userInfo')
		let accessToken = userInfo ? userInfo.accessToken : ''
		let addresses = uni.getStorageSync('addresses') || '';
		let baseUrl = config.def().baseUrl
        let content = 'application/x-www-form-urlencoded; charset=UTF-8';
        if (method === 'post') {
            content = 'application/json; charset=UTF-8';
        }
		uni.request({
			url: baseUrl+"/"+ endpoint,
			method: method,
			data: data,
			header: {
				'Content-Type': content,
				'Authorization': 'Bearer '+accessToken
			},
			success: (res) => {
                if (res.data.code === 200) {
                    if(!addresses && accessToken){
                        uni.showModal({
                            title: '地图提示',
                            content: '您尚未选择，您的位置？',
                            showCancel: true,
                            confirmText: '选择',
                            success: (e) => {
                                if (e.confirm) {
                                    uni.chooseLocation({
                                        success: (res1) => {
                                            console.log(res1)
                                            if (res1.name === "") {
                                                return
                                            }
                                            let addressesInfo = {};
                                            addressesInfo.lng = res1.longitude;
                                            addressesInfo.lat = res1.latitude;
                                            addressesInfo.addressesName = res1.name;
                                            uni.setStorageSync('addresses', addressesInfo);
                                            uni.reLaunch({
                                                url: '/pages/index/index?type=1'
                                            });
                                        },fail: (e) => {
                                            console.log(e)
                                        }
                                    });
                                }
                            },
                            fail: () => {}
                        })
                    }
                    resolve(res.data);
                } else if (res.data.code === 401) {
                    if (failCallback) {
                        failCallback(res.data)
                    }
                    if (!loginLock) {
                        loginLock = true
                        uni.showModal({
                            title: '登录提示',
                            content: '您尚未登录，是否立即登录？',
                            showCancel: true,
                            confirmText: '登录',
                            success: (e) => {
                                if (e.confirm) {
                                    uni.navigateTo({
                                        url: '/pages/public/login'
                                    })
                                }
                            },
                            fail: () => {},
                            complete: () => {
                                loginLock = false
                            }
                        })
                    }

                } else {
                    if (failCallback) {
                        failCallback(res.data)
                    } else {
                        uni.showToast({
                            title: res.data.msg,
                            icon: 'none'
                        })
                    }
                }
			}
		})
	})
}

const uploadImg = (num = 9,successCallback) => {
	let baseUrl = config.def().baseUrl
	uni.chooseImage({
		sizeType: ['compressed'],
		count:num,
		success: function(res) {
			for (let i = 0; i < res.tempFilePaths.length; i++) {
				uni.request({
					url: baseUrl + '/upload',
					method: 'get',
					success: function(signRes) {
						uni.showLoading({
							title: '图片上传中',
						})
						let fileName = ('imgs/' + random_string(15) + get_suffix(res.tempFilePaths[i]))
						uni.uploadFile({
							url: signRes.data.baseUrl,
							filePath: res.tempFilePaths[i],
							name: 'file',
							formData: {
								name: res.tempFilePaths[i],
								key: fileName,
								policy: signRes.data.policy,
								OSSAccessKeyId: signRes.data.accessid,
								success_action_status: '200',
								signature: signRes.data.signature
							},
							success: function(uploadRes) {
								uni.hideLoading()
								if (uploadRes.code === 200) {
									if (successCallback) {
										successCallback(signRes.data.baseUrl + fileName)
									} else {
										uni.showToast({
											title: '上传成功',
											icon: 'none'
										})
									}
								} else {
									uni.hideLoading()
									uni.showToast({
										title: '网络错误 code=' + uploadRes.code,
										icon: 'none'
									})
								}
							}
						})
					}
				})
			}
		}
	})
}

function get_suffix(filename) {
	var pos = filename.lastIndexOf('.')
	var suffix = ''
	if (pos != -1) {
		suffix = filename.substring(pos)
	}
	return suffix;
}

function random_string(len) {
	len = len || 32;
	var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
	var maxPos = chars.length;
	var pwd = '';
	for (var i = 0; i < len; i++) {
		pwd += chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return pwd;
}

const prePage = () => {
	let pages = getCurrentPages();
	let prePage = pages[pages.length - 2];
	// #ifdef H5
	return prePage;
	// #endif
	return prePage.$vm;
}

const globalData = {}

Vue.config.productionTip = false
Vue.prototype.$fire = new Vue();
Vue.prototype.$store = store;
Vue.prototype.$api = {
	msg,
	prePage,
	request,
	uploadImg,
	logout,
	isVip,
	setUserInfo,
	defConfig,
	globalData
};
//#ifdef H5
Vue.prototype.$jweixin = jweixin;
//#endif

//zj 2.17 统一获取地理信息
//isChoose 是否 chooseLocation 接口返回的经纬度 该接口返回的address没做省市区划分
Vue.prototype.$getLocation = async (isChoose=false,latitude='',longitude='')=>{
	var ret = {}
	if(isChoose){
		ret.latitude = res.latitude
		ret.longitude = res.longitude
	}else{
		var [err,res] =  await uni.getLocation({
			type: 'wgs84'
		})
		if(res && res.errMsg === 'getLocation:ok'){
			ret.latitude = res.latitude
			ret.longitude = res.longitude
		}else{
			console.log(err.errMsg)
			return false
		}
	}
	var [error,geocodeData] = await uni.request({
		url:"https://restapi.amap.com/v3/geocode/regeo",
		method:"GET",
		data:{
			key:'a0096c306be491b44b6ffc21c3af9dd4',
			location:ret.longitude+','+ret.latitude,
			extensions:'all',
			// poitype:'120000|060000'返回poi限定范围
		}
	})
	if(geocodeData.statusCode === 200){
		console.log(geocodeData)
		var data = geocodeData.data.regeocode.addressComponent
		for(var key in data){
			ret[key] = data[key]
		}
		ret.pois = geocodeData.data.regeocode.pois
		return ret
	}else{
		return false
	}
}

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()
