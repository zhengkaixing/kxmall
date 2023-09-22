import request from '@/utils/request'

// 查询订单列表
export function listStoreOrder(query) {
  return request({
    url: '/order/storeOrder/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getStoreOrder(id) {
  return request({
    url: '/order/storeOrder/' + id,
    method: 'get'
  })
}

// 新增订单
export function addStoreOrder(data) {
  return request({
    url: '/order/storeOrder',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateStoreOrder(data) {
  return request({
    url: '/order/storeOrder',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delStoreOrder(id) {
  return request({
    url: '/order/storeOrder/' + id,
    method: 'delete'
  })
}

export function startStocking(params) {
  return request({
    url: '/order/storeOrder/startStocking/',
    method: 'get',
    params
  })
}

export function completeAllocation(params) {
  return request({
    url: '/order/storeOrder/completeAllocation',
    method: 'get',
    params
  })
}

export function merchantDistribution(params) {
  return request({
    url: '/order/storeOrder/merchantDistribution',
    method: 'get',
    params
  })
}

export function completeDelivery(params) {
  return request({
    url: '/order/storeOrder/completeDelivery',
    method: 'get',
    params
  })
}
