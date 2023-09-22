import request from '@/utils/request'

// 查询评论管理列表
export function listStoreAppraise(query) {
  return request({
    url: '/order/storeAppraise/list',
    method: 'get',
    params: query
  })
}

// 查询评论管理详细
export function getStoreAppraise(id) {
  return request({
    url: '/order/storeAppraise/' + id,
    method: 'get'
  })
}

// 新增评论管理
export function addStoreAppraise(data) {
  return request({
    url: '/order/storeAppraise',
    method: 'post',
    data: data
  })
}

// 修改评论管理
export function updateStoreAppraise(data) {
  return request({
    url: '/order/storeAppraise',
    method: 'put',
    data: data
  })
}

// 删除评论管理
export function delStoreAppraise(id) {
  return request({
    url: '/order/storeAppraise/' + id,
    method: 'delete'
  })
}
