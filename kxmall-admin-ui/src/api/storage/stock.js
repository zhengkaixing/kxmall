import request from '@/utils/request'

// 查询前置仓商品列表
export function listStock(query) {
  return request({
    url: '/storage/stock/list',
    method: 'get',
    params: query
  })
}

// 查询前置仓商品详细
export function getStock(id) {
  return request({
    url: '/storage/stock/' + id,
    method: 'get'
  })
}

// 新增前置仓商品
export function addStock(data) {
  return request({
    url: '/storage/stock',
    method: 'post',
    data: data
  })
}

// 修改前置仓商品
export function updateStock(data) {
  return request({
    url: '/storage/stock',
    method: 'put',
    data: data
  })
}

// 删除前置仓商品
export function delStock(id) {
  return request({
    url: '/storage/stock/' + id,
    method: 'delete'
  })
}

export function updatePrice(data) {
  return request({
    url: '/storage/stock/updatePrice',
    method: 'post',
    data
  })
}

export function updateStatus(data) {
  return request({
    url: '/storage/stock/freezeOrActivation',
    method: 'post',
    data
  })
}

export function warningList(params) {
  return request({
    url: '/storage/stock/warningList',
    params
  })
}

export function warningUpdate(data) {
  return request({
    url: '/storage/stock/warningUpdate',
    method: 'post',
    data
  })
}
