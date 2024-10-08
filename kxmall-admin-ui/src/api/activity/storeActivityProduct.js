import request from '@/utils/request'


export function updatePrice(data) {
  return request({
    url: '/activity/storeActivityProduct/updatePrice',
    method: 'post',
    data
  })
}


// 查询活动类型
export function listActivityType() {
  return request({
    url: '/activity/storeActivityProduct/listActivityType',
    method: 'get'
  })
}
export function addProductBatch(data) {
  return request({
    url: '/activity/storeActivityProduct/addProductBatch',
    method: 'post',
    data
  })
}




// 查询活动商品列表
export function listStoreActivityProduct(query) {
  return request({
    url: '/activity/storeActivityProduct/list',
    method: 'get',
    params: query
  })
}

// 查询活动商品详细
export function getStoreActivityProduct(id) {
  return request({
    url: '/activity/storeActivityProduct/' + id,
    method: 'get'
  })
}

// 新增活动商品
export function addStoreActivityProduct(data) {
  return request({
    url: '/activity/storeActivityProduct',
    method: 'post',
    data: data
  })
}

// 修改活动商品
export function updateStoreActivityProduct(data) {
  return request({
    url: '/activity/storeActivityProduct',
    method: 'put',
    data: data
  })
}

// 删除活动商品
export function delStoreActivityProduct(id) {
  return request({
    url: '/activity/storeActivityProduct/' + id,
    method: 'delete'
  })
}
