import request from '@/utils/request'

// 查询发布优惠券列表
export function listStoreCouponIssue(query) {
  return request({
    url: '/coupon/storeCouponIssue/list',
    method: 'get',
    params: query
  })
}

// 查询发布优惠券详细
export function getStoreCouponIssue(id) {
  return request({
    url: '/coupon/storeCouponIssue/' + id,
    method: 'get'
  })
}

// 新增发布优惠券
export function addStoreCouponIssue(data) {
  return request({
    url: '/coupon/storeCouponIssue',
    method: 'post',
    data: data
  })
}

// 修改发布优惠券
export function updateStoreCouponIssue(data) {
  return request({
    url: '/coupon/storeCouponIssue',
    method: 'put',
    data: data
  })
}

// 删除发布优惠券
export function delStoreCouponIssue(id) {
  return request({
    url: '/coupon/storeCouponIssue/' + id,
    method: 'delete'
  })
}
