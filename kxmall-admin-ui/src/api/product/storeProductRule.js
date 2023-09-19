import request from '@/utils/request'

// 查询商品规格列表
export function listStoreProductRule(query) {
  return request({
    url: '/product/storeProductRule/list',
    method: 'get',
    params: query
  })
}

// 查询商品规格详细
export function getStoreProductRule(id) {
  return request({
    url: '/product/storeProductRule/' + id,
    method: 'get'
  })
}

// 新增商品规格
export function addStoreProductRule(data) {
  return request({
    url: '/product/storeProductRule',
    method: 'post',
    data: data
  })
}

// 修改商品规格
export function updateStoreProductRule(data) {
  return request({
    url: '/product/storeProductRule',
    method: 'put',
    data: data
  })
}

// 删除商品规格
export function delStoreProductRule(id) {
  return request({
    url: '/product/storeProductRule/' + id,
    method: 'delete'
  })
}
