import request from '@/utils/request'

// 查询商品分类列表
export function listStoreCategory(query) {
  return request({
    url: '/product/storeCategory/list',
    method: 'get',
    params: query
  })
}

// 查询商品分类详细
export function getStoreCategory(id) {
  return request({
    url: '/product/storeCategory/' + id,
    method: 'get'
  })
}

// 新增商品分类
export function addStoreCategory(data) {
  return request({
    url: '/product/storeCategory',
    method: 'post',
    data: data
  })
}

// 修改商品分类
export function updateStoreCategory(data) {
  return request({
    url: '/product/storeCategory',
    method: 'put',
    data: data
  })
}

// 删除商品分类
export function delStoreCategory(id) {
  return request({
    url: '/product/storeCategory/' + id,
    method: 'delete'
  })
}

// 查询商品分类项目
export function listStoreCategoryTree(query) {
  return request({
    url: '/product/storeCategory/listTree',
    method: 'get',
    params: query
  })
}
