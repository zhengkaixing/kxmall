import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)



const store = new Vuex.Store({
	state: {
		hasLogin: false,
		userInfo: {},
		storageId:0,
		cartNum:0,
		storageObj:{},
		InvitationCode:'',
		headerTop:''
	},
	mutations: {
		login(state, provider) {
			state.hasLogin = true;
			state.userInfo = provider;
			uni.setStorageSync('userInfo', provider)
		},
		logout(state) {
			state.hasLogin = false;
			state.userInfo = {};
			uni.removeStorageSync('userInfo')
		},
		setStorage(state,id){
			state.storageId = id
		},
		setStorageCode(state,InvitationCode){
			state.InvitationCode = InvitationCode
		},
		setStorageObj(state,obj){
			state.storageObj = obj
		},
		addCart(state,num){
			state.cartNum = num
		}
	},
	actions: {

	},
	getters: {
		getUserInfo(state) {
			return state.userInfo
		}
	}
})

export default store
