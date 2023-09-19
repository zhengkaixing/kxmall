import request from '@/utils/request'

// 查询商品入库列表
export function listGoodsInStock(query) {
  return request({
    url: '/storage/goodsInStock/list',
    method: 'get',
    params: query
  })
}

// 查询商品入库详细
export function getGoodsInStock(id) {
  return request({
    url: '/storage/goodsInStock/' + id,
    method: 'get'
  })
}

// 新增商品入库
export function addGoodsInStock(data) {
  return request({
    url: '/storage/goodsInStock',
    method: 'post',
    data: data
  })
}

// 修改商品入库
export function updateGoodsInStock(data) {
  return request({
    url: '/storage/goodsInStock',
    method: 'put',
    data: data
  })
}

// 删除商品入库
export function delGoodsInStock(id) {
  return request({
    url: '/storage/goodsInStock/' + id,
    method: 'delete'
  })
}

// 入库
export function updateInOfStock(data) {
  return request({
    url: '/storage/goodsInStock/updateInOfStock',
    method: 'post',
    data
  })
}
