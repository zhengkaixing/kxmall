import request from '@/utils/request'

// 查询活动商品列表
export function listStoreActivity(query) {
  return request({
    url: '/activity/storeActivity/list',
    method: 'get',
    params: query
  })
}

// 查询活动商品详细
export function getStoreActivity(id) {
  return request({
    url: '/activity/storeActivity/' + id,
    method: 'get'
  })
}

// 新增活动商品
export function addStoreActivity(data) {
  return request({
    url: '/activity/storeActivity',
    method: 'post',
    data: data
  })
}

// 修改活动商品
export function updateStoreActivity(data) {
  return request({
    url: '/activity/storeActivity',
    method: 'put',
    data: data
  })
}

// 删除活动商品
export function delStoreActivity(id) {
  return request({
    url: '/activity/storeActivity/' + id,
    method: 'delete'
  })
}
