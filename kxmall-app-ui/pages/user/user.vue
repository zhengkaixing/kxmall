<template>
  <view >
    <view v-if="storage">
      <view class="bg-user-gradual-green" style=" background-image: url(https://kxmalls.oss-cn-hangzhou.aliyuncs.com/bg/bg.png);">
        <view :style="'height:'+statusBarHeight+'px;'" style="width: 750rpx;display: flex; position: relative;"></view>
        <view class="flex align-center justify-center"
              style="height: 88rpx;font-size: 36rpx;"
        >
          个人中心
        </view>
        <view class="padding-top-lg flex justify-between align-end padding-lr" style="padding-top: 30rpx;">
          <view class="">
            <view @click="toLogin" style="margin-top: 16rpx;font-size: 40rpx;height: 56rpx;">
              HI，{{ hasLogin? (userInfo.nickname || '未设置昵称') : '请注册/登录' }}
            </view>
          </view>
          <view @click="navTo('/pages/user/profile')" style="position: relative;padding-right: 8rpx;">
            <image :src="hasLogin && userInfo.avatarUrl ? userInfo.avatarUrl :'../../static/user/touxiang-@2x.png'"
                   class="round" mode="aspectFill"
                   style="width: 144rpx;height: 144rpx;"></image>
            <view v-if="hasLogin" class="flex align-center justify-center" style="position: absolute;bottom: 0rpx;left: 29rpx; background-color: #8AD62A;border-radius: 200rpx;width: 86rpx;height: 38rpx;color: #FFFFFF;font-size: 22rpx;"
            >编辑</view>
          </view>
        </view>
      </view>
      <view class="margin-lr bg-white shadow"
            style="border-radius: 16rpx 16rpx 8rpx 8rpx;width: 690rpx;height: 244rpx;margin-top: -146rpx;">
        <view class="padding-left padding-top padding-bottom-sm">
          我的订单
        </view>
        <view class="flex justify-around align-center padding-top-sm">
          <view @click="navTo('/pages/order/list?state=0')" class="flex align-center justify-center flex-direction" style="width: 108rpx;">
            <image src="../../static/user/all_order.png" mode="aspectFit" style="width: 46rpx;height: 50rpx;"></image>
            <view style="padding-top: 20rpx;font-size: 24rpx;color: #272B54;">全部订单</view>
          </view>
          <view @click="navTo('/pages/order/list?state=1')" class="flex align-center justify-center flex-direction" style="width: 108rpx;">
            <image src="../../static/user/waitpay.png" mode="aspectFit" style="width: 46rpx;height: 50rpx;"></image>
            <view style="padding-top: 20rpx;font-size: 24rpx;color: #272B54;">待付款</view>
          </view>
          <view @click="navTo('/pages/order/list?state=2')" class="flex align-center justify-center flex-direction" style="width: 108rpx;">
            <image src="../../static/user/daishouhuo@3x.png" mode="aspectFit" style="width: 46rpx;height: 50rpx;"></image>
            <view style="padding-top: 20rpx;font-size: 24rpx;color: #272B54;">待收货</view>
          </view>
          <view @click="navTo('/pages/order/list?state=3')" class="flex align-center justify-center flex-direction" style="width: 108rpx;">
            <image src="../../static/user/shouhou@3x.png" mode="aspectFit" style="width: 46rpx;height: 50rpx;"></image>
            <view style="padding-top: 20rpx;font-size: 24rpx;color: #272B54;">待评价</view>
          </view>
        </view>
      </view>
      <view class="padding-lr padding-tb-sm">
        <image src="https://s1.ax1x.com/2020/04/07/G2usUI.png" mode="aspectFit"
               style="width: 690rpx;height: 184rpx;"></image>
      </view>

      <view class="margin-lr margin-bottom-sm shadow bg-white" style="border-radius: 10rpx;">
        <view @click="navTo('/pages/address/list')" class="flex align-center padding">
          <image src="../../static/user/location@3x.png" mode="aspectFit"
                 style="width: 32rpx;height: 36rpx;"></image>
          <view class="padding-left-sm lem-text-black" style="font-size: 28rpx;">地址管理</view>
        </view>
        <button open-type="contact" class="flex align-center padding contact-btn" >
          <image src="../../static/user/secretary@3x.png" mode="aspectFit"
                 style="width: 32rpx;height: 36rpx;"></image>
          <view class="padding-left-sm lem-text-black" style="font-size: 28rpx;">在线客服</view>
        </button>
      </view>
      <view class="margin-lr margin-bottom-sm shadow bg-white" style="border-radius: 10rpx;">
        <view @click="navTo('/pages/product/favorite')" class="flex align-center padding">
          <image src="../../static/user/collect@3x.png" mode="aspectFit"
                 style="width: 32rpx;height: 36rpx;"></image>
          <view class="padding-left-sm lem-text-black" style="font-size: 28rpx;">我的收藏</view>
        </view>
        <view @click="navTo('/pages/user/about')" class="flex align-center padding">
          <image src="../../static/user/tip_round@3x.png" mode="aspectFit"
                 style="width: 32rpx;height: 36rpx;"></image>
          <view class="padding-left-sm lem-text-black" style="font-size: 28rpx;">关于</view>
        </view>
        <view @click="navTo('/pages/set/set')" class="flex align-center padding">
          <image src="../../static/user/setting@3x.png" mode="aspectFit"
                 style="width: 32rpx;height: 36rpx;"></image>
          <view class="padding-left-sm lem-text-black" style="font-size: 28rpx;">设置</view>
        </view>
      </view>
    </view>
    <view v-else style="padding-top: 180rpx;padding-bottom: 180rpx;">
      <missing :buttonName="'换个地址试试吧~'" :handlerName="'buttonClick'"
               @buttonClick="chooseLocation"
               :imgUrl="'http://qiniuoss.nauzone.cn/%E7%BB%84%204%20%E6%8B%B7%E8%B4%9D@3x.png'" :desc="'当前地区不在配送范围哦'"></missing>
    </view>
  </view>
</template>
<script>
import listCell from '@/components/mix-list-cell';
import missing from "@/components/missing.vue"
import {
  mapState
} from 'vuex';
let startY = 0, moveY = 0, pageAtTop = true;
export default {
  components: {
    listCell,missing
  },
  data(){
    return {
      coverTransform: 'translateY(0px)',
      coverTransition: '0s',
      moving: false,
      footprintList: [],
      isVip: false,
      statusBarHeight:88,
      storage:true,
      banner:{
        imgUrl:''
      }
    }
  },
  onShow() {
    this.isVip = this.$api.isVip()
    //this.loadFootprint()
    this.$store.state.storageId ? this.storage = true : this.storage = false
    //如果用户已登录，获取购物车数量
    if(this.$store.state.userInfo.accessToken){
      this.$api.request('get','cart/app/countCart',{
        storageId:this.$store.state.storageId
      }).then(res=>{
        if(res.data > 0){
          uni.setTabBarBadge({
            index:2,
            text:res.data+''
          })
        }
        this.$store.commit('addCart',res.data)
      }).catch(err=>{
        // this.$api.msg('请求失败，请稍后再试')
      })
    }
  },
  onLoad(){
    try {
      const res = uni.getSystemInfoSync();
      console.log(res.statusBarHeight);
      this.statusBarHeight = res.statusBarHeight
    } catch (e) {
      // error
    }
    this.$api.request('get','carousel/app/getCarouselActive',{
      adType:8
    }).then(res=>{
      this.banner = res.data[0]
    })
  },
  // #ifndef MP
  onNavigationBarButtonTap(e) {
    const index = e.index;
    if (index === 0) {
      this.navTo('/pages/set/set');
    }else if(index === 1){
      // #ifdef APP-PLUS
      const pages = getCurrentPages();
      const page = pages[pages.length - 1];
      const currentWebview = page.$getAppWebview();
      currentWebview.hideTitleNViewButtonRedDot({
        index
      });
      // #endif
      uni.navigateTo({
        url: '/pages/notice/notice'
      })
    }
  },
  // #endif
  computed: {
    ...mapState(['hasLogin','userInfo'])
  },
  methods: {
    // async loadFootprint() {
    // 	const that = this
    // 	that.$api.request('get', 'getAllFootprint').then(res => {
    // 		that.footprintList = res.data
    // 	})
    // },

    deleteFootprint(item) {
      const that = this
      uni.showModal({
        title: '删除？',
        content: '您确定要删除此足迹吗？',
        success : (e) => {
          if (e.confirm) {
            that.$api.request('get', '/deleteFootprint', {
              footprintId: item.id
            }).then(res => {
              that.loadFootprint()
            })
          }
        }
      })
    },

    toLogin() {
      if (!this.hasLogin) {
        uni.navigateTo({
          url: '/pages/public/login'
        })
      }
    },

    logout() {
      const that = this
      uni.showModal({
        title: '询问',
        content: '您确定要退出吗？',
        cancelText: '取消',
        confirmText: '确定',
        success: (e) => {
          if (e.confirm) {
            that.$store.commit('logout')
            that.$api.logout()
          }
        }
      })
    },

    /**
     * 统一跳转接口,拦截未登录路由
     * navigator标签现在默认没有转场动画，所以用view
     */
    navTo(url){
      if(!this.hasLogin){
        url = '/pages/public/login';
      }
      uni.navigateTo({
        url
      })
    },

    /**
     *  会员卡下拉和回弹
     *  1.关闭bounce避免ios端下拉冲突
     *  2.由于touchmove事件的缺陷（以前做小程序就遇到，比如20跳到40，h5反而好很多），下拉的时候会有掉帧的感觉
     *    transition设置0.1秒延迟，让css来过渡这段空窗期
     *  3.回弹效果可修改曲线值来调整效果，推荐一个好用的bezier生成工具 http://cubic-bezier.com/
     */
    coverTouchstart(e){
      if(pageAtTop === false){
        return;
      }
      this.coverTransition = 'transform .1s linear';
      startY = e.touches[0].clientY;
    },
    coverTouchmove(e){
      moveY = e.touches[0].clientY;
      let moveDistance = moveY - startY;
      if(moveDistance < 0){
        this.moving = false;
        return;
      }
      this.moving = true;
      if(moveDistance >= 80 && moveDistance < 100){
        moveDistance = 80;
      }

      if(moveDistance > 0 && moveDistance <= 80){
        this.coverTransform = `translateY(${moveDistance}px)`;
      }
    },
    coverTouchend(){
      if(this.moving === false){
        return;
      }
      this.moving = false;
      this.coverTransition = 'transform 0.3s cubic-bezier(.21,1.93,.53,.64)';
      this.coverTransform = 'translateY(0px)';
    },
    //配送外区域选择区域
    chooseLocation(){
      uni.chooseLocation({
        success: (res1)=> {
          console.log(res1)
          this.district = res1.name
          uni.showLoading({
            title:"加载中..."
          })
          this.$api.request('get','storage/position/getRecentlyStorage',{
            lng:res1.longitude,
            lat:res1.latitude
          },failres => {
            uni.hideLoading()
            this.logining = false
            this.$api.msg(failres.msg)
            this.storage ? this.storage = false : this.storage = true
          }).then(res=>{
            uni.hideLoading()
            console.log(res)
            this.$store.commit('setStorage',res.data.id)
            this.newTop = []
            this.cheapRecommend = []
            this.salesTop = []
            this.loadData(res.data.id)
            if(!res.data.id){
              this.storage = false
            }else{
              this.storage ? this.storage = false : this.storage = true
              // this.loadRecommand('refresh')
            }
          })
        }
      });
    }
  }
}
</script>
<style lang='scss'>
page{
  background-color: #f1f1f1;;
}

.contact-btn{
  border: none;
  background-color: #FFFFFF;
}

.contact-btn::after{
  border: none;
}

.bg-user-gradual-green {
  background-repeat: no-repeat;
  background-size: 100%;
  color: #fff;
  height: 260*2rpx;
  width: 750rpx;
}

.shadow {
  box-shadow: 0 1rpx 6rpx rgba(0, 0, 0, 0.1);
}

%flex-center {
  display:flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
%section {
  display:flex;
  justify-content: space-around;
  align-content: center;
  background: #fff;
  border-radius: 10upx;
}

.user-section{
  height: 520upx;
  padding: 100upx 30upx 0;
  position:relative;
  .bg{
    position:absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    filter: blur(1px);
    opacity: .7;
  }
}
.user-info-box{
  height: 180upx;
  display:flex;
  align-items:center;
  position:relative;
  z-index: 1;
  .portrait{
    width: 130upx;
    height: 130upx;
    border:5upx solid #fff;
    border-radius: 50%;
  }
  .username{
    font-size: $font-lg + 6upx;
    color: $font-color-dark;
    margin-left: 20upx;
  }
}

.vip-card-box{
  display:flex;
  flex-direction: column;
  color: #f7d680;
  height: 240upx;
  background: linear-gradient(left, rgba(0,0,0,.7), rgba(0,0,0,.8));
  border-radius: 16upx 16upx 0 0;
  overflow: hidden;
  position: relative;
  padding: 20upx 24upx;
  .card-bg{
    position:absolute;
    top: 20upx;
    right: 0;
    width: 380upx;
    height: 260upx;
  }
  .b-btn{
    position: absolute;
    right: 20upx;
    top: 16upx;
    width: 132upx;
    height: 40upx;
    text-align: center;
    line-height: 40upx;
    font-size: 22upx;
    color: #36343c;
    border-radius: 20px;
    background: linear-gradient(left, #f9e6af, #ffd465);
    z-index: 1;
  }
  .tit{
    font-size: $font-base+2upx;
    color: #f7d680;
    margin-bottom: 28upx;
    .yticon{
      color: #f6e5a3;
      margin-right: 16upx;
    }
  }
  .e-b{
    font-size: $font-sm;
    color: #d8cba9;
    margin-top: 10upx;
  }
}
.cover-container{
  background: $page-color-base;
  margin-top: -150upx;
  padding: 0 30upx;
  position:relative;
  background: #f5f5f5;
  padding-bottom: 20upx;
  .arc{
    position:absolute;
    left: 0;
    top: -34upx;
    width: 100%;
    height: 36upx;
  }
}
.tj-sction{
  @extend %section;
  .tj-item{
    @extend %flex-center;
    flex-direction: column;
    height: 140upx;
    font-size: $font-sm;
    color: #75787d;
  }
  .num{
    font-size: $font-lg;
    color: $font-color-dark;
    margin-bottom: 8upx;
  }
}
.order-section{
  @extend %section;
  padding: 28upx 0;
  margin-top: 20upx;
  .order-item{
    @extend %flex-center;
    width: 120upx;
    height: 120upx;
    border-radius: 10upx;
    font-size: $font-sm;
    color: $base-color;
  }
  .yticon{
    font-size: 48upx;
    margin-bottom: 18upx;
    color: $base-color;
  }
  .icon-shouhoutuikuan{
    font-size:44upx;
  }
}
.history-section{
  padding: 30upx 0 0;
  margin-top: 20upx;
  background: #fff;
  border-radius:10upx;
  .sec-header{
    display:flex;
    align-items: center;
    font-size: $font-base;
    color: $font-color-dark;
    line-height: 40upx;
    margin-left: 30upx;
    .yticon{
      font-size: 44upx;
      color: #5eba8f;
      margin-right: 16upx;
      line-height: 40upx;
    }
  }
  .h-list{
    white-space: nowrap;
    padding: 30upx 30upx 0;
    image{
      display:inline-block;
      width: 160upx;
      height: 160upx;
      margin-right: 20upx;
      border-radius: 10upx;
    }
  }
}

</style>
