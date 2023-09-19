import request from '@/utils/request'

// 查询商铺广告列表
export function listCarousel(query) {
  return request({
    url: '/carousel/carousel/list',
    method: 'get',
    params: query
  })
}

// 查询商铺广告详细
export function getCarousel(id) {
  return request({
    url: '/carousel/carousel/' + id,
    method: 'get'
  })
}

// 新增商铺广告
export function addCarousel(data) {
  return request({
    url: '/carousel/carousel',
    method: 'post',
    data: data
  })
}

// 修改商铺广告
export function updateCarousel(data) {
  return request({
    url: '/carousel/carousel',
    method: 'put',
    data: data
  })
}

// 删除商铺广告
export function delCarousel(id) {
  return request({
    url: '/carousel/carousel/' + id,
    method: 'delete'
  })
}
