import request from '@/utils/request'

// 查询商品出库列表
export function listGoodsOutStock(query) {
  return request({
    url: '/storage/goodsOutStock/list',
    method: 'get',
    params: query
  })
}

// 查询商品出库详细
export function getGoodsOutStock(id) {
  return request({
    url: '/storage/goodsOutStock/' + id,
    method: 'get'
  })
}

// 新增商品出库
export function addGoodsOutStock(data) {
  return request({
    url: '/storage/goodsOutStock',
    method: 'post',
    data: data
  })
}

// 修改商品出库
export function updateGoodsOutStock(data) {
  return request({
    url: '/storage/goodsOutStock',
    method: 'put',
    data: data
  })
}

// 删除商品出库
export function delGoodsOutStock(id) {
  return request({
    url: '/storage/goodsOutStock/' + id,
    method: 'delete'
  })
}

export function updateOutOfStock(data) {
  return request({
    url: '/storage/goodsOutStock/updateOutOfStock',
    method: 'post',
    data
  })
}
