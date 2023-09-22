import request from '@/utils/request'

// 查询商品列表
export function listStoreProduct(query) {
  return request({
    url: '/product/storeProduct/list',
    method: 'get',
    params: query
  })
}

// 查询商品详细
export function getStoreProduct(id) {
  return request({
    url: '/product/storeProduct/' + id,
    method: 'get'
  })
}

// 新增商品
export function addStoreProduct(data) {
  return request({
    url: '/product/storeProduct',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateStoreProduct(data) {
  return request({
    url: '/product/storeProduct',
    method: 'put',
    data: data
  })
}

// 删除商品
export function delStoreProduct(id) {
  return request({
    url: '/product/storeProduct/' + id,
    method: 'delete'
  })
}

// 商品规格
export function isFormatAttr(id, data) {
  return request({
    url: '/product/storeProduct/isFormatAttr/' + id,
    method: 'post',
    data
  })
}

// 上架状态
export function onsale(id, data) {
  return request({
    url: '/product/storeProduct/onsale/' + id,
    method: 'post',
    data
  })
}

export function batchAuthorizeGoods(data) {
  return request({
    url: '/product/storeProduct/batchAuthorizeGoods',
    method: 'post',
    data
  })
}

export function getProductBigTree() {
  return request({
    url: '/product/storeProduct/getProductBigTree',
    method: 'post'
  })
}
