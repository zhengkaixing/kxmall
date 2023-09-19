import request from '@/utils/request'

// 查询优惠券列表
export function listStoreCoupon(query) {
  return request({
    url: '/coupon/storeCoupon/list',
    method: 'get',
    params: query
  })
}

// 查询优惠券详细
export function getStoreCoupon(id) {
  return request({
    url: '/coupon/storeCoupon/' + id,
    method: 'get'
  })
}

// 新增优惠券
export function addStoreCoupon(data) {
  return request({
    url: '/coupon/storeCoupon',
    method: 'post',
    data: data
  })
}

// 修改优惠券
export function updateStoreCoupon(data) {
  return request({
    url: '/coupon/storeCoupon',
    method: 'put',
    data: data
  })
}

// 删除优惠券
export function delStoreCoupon(id) {
  return request({
    url: '/coupon/storeCoupon/' + id,
    method: 'delete'
  })
}
