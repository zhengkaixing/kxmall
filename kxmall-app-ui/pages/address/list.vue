<template>
  <!-- <view class="content b-t">
    <view class="list b-b" v-for="(item, index) in addressList" :key="index" @click="checkAddress(item)" @longpress="deleteShow(item)">
      <view class="wrapper">
        <view class="u-box">
          <text class="name">{{item.consignee}}</text>
          <text class="mobile">{{item.phone}}</text>
        </view>
        <view class="address-box">
          <text v-if="item.defaultAddress" class="tag">默认</text>
          <text class="address">{{item.province}} {{item.city}} {{item.county}} {{item.address}}</text>
        </view>
      </view>
      <text class="yticon icon-bianji" @click.stop="addAddress('edit', item)"></text>
    </view>
    <button class="add-btn" @click="addAddress('add')">新增地址</button>
  </view> -->
  <view class="">
    <view v-for="(item, index) in addressList" :key="index"
          @click="checkAddress(item)" @longpress="deleteShow(item)"
          class="bg-white" style="padding: 30rpx;margin: 20rpx 0;">
      <view class="flex justify-between align-center">
        <view style="font-size: 32rpx;line-height: 44rpx;color: #333333;font-weight: 500;">
          {{item.address}}
        </view>
        <view @click.stop="addAddress('edit', item)" class="text-green" style="font-size: 24rpx;line-height: 34rpx;">
          编辑
        </view>
      </view>
      <view style="padding: 22rpx 0;font-size: 28rpx;line-height: 40rpx;color: #333333;">
        {{item.province}}{{item.city}}{{item.county}}
      </view>
      <view class="flex" style="font-size: 28rpx;line-height: 40rpx;color: #333333;">
        <view v-if="item.defaultAddress"
              style="line-height: 1.5;padding: 0 10rpx;border-radius: 8rpx;background-color: #2AAC34;color: #FFFFFF;margin-right: 20rpx;">默认</view>
        {{item.consignee}} {{item.phone}}
      </view>
    </view>
    <view style="width: 750rpx;display: flex; position: relative;height: 144rpx;"></view>
    <view class="flex justify-center " style="width: 750rpx;position: fixed;bottom: 0;background-color: #F6F6F6;">
      <button @click="addAddress('add')" class="round" style="background-color: #2AAC34;width: 560rpx;height: 90rpx;margin-bottom: 54rpx;line-height: 90rpx;color: #FFFFFF;">新增收货地址</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      source: 0,
      addressList: [

      ]
    }
  },
  onLoad(option) {
    this.refreshList()
    this.source = option.source;
  },
  methods: {
    //选择地址
    checkAddress(item) {
      if (this.source == 1) {
        //this.$api.prePage()获取上一页实例，在App.vue定义
        this.$api.prePage().addressData = item
        this.$api.prePage().calcFreightPrice()
        uni.navigateBack()
      }
    },
    addAddress(type, item) {
      uni.navigateTo({
        url: `/pages/address/create?type=${type}&data=${JSON.stringify(item)}`
      })
    },
    //添加或修改成功之后回调
    refreshList(data, type) {
      const that = this
      that.$api.request('get', 'address/app/getAllAddress').then(res => {
        that.addressList = res.data
      })
    },
    deleteShow(item) {
      const that = this
      uni.showModal({
        title: '删除提示',
        content: '您确定要删除该地址吗？',
        showCancel: true,
        confirmText: '删除',
        success: (e) => {
          if (e.confirm) {
            that.$api.request('post', 'address/app/deleteAddress',{
              id: item.id
            }).then(res => {
              this.refreshList()
            })
          }
        },
        fail: () => {}
      })
    }
  }
}
</script>

<style lang='scss'>
page {
  padding-bottom: 120upx;
  background-color: #F6F6F6;
}

.content {
  position: relative;
}

.list {
  display: flex;
  align-items: center;
  padding: 20upx 30upx;
;
  background: #fff;
  position: relative;
}

.wrapper {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.address-box {
  display: flex;
  align-items: center;

  .tag {
    font-size: 24upx;
    color: $base-color;
    margin-right: 10upx;
    background: #fffafb;
    border: 1px solid #ffb4c7;
    border-radius: 4upx;
    padding: 4upx 10upx;
    line-height: 1;
  }

  .address {
    font-size: 28upx;
    color: $font-color-light;
  }
}

.u-box {
  font-size: 30upx;
  color: $font-color-dark;
  margin-top: 16upx;

  .name {
    margin-right: 30upx;
  }
}

.icon-bianji {
  display: flex;
  align-items: center;
  height: 80upx;
  font-size: 40upx;
  color: $font-color-light;
  padding-left: 30upx;
}

.add-btn {
  position: fixed;
  left: 30upx;
  right: 30upx;
  bottom: 16upx;
  z-index: 95;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 690upx;
  height: 80upx;
  font-size: 32upx;
  color: #fff;
  background-color: $base-color;
  border-radius: 10upx;
  box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
}
</style>
