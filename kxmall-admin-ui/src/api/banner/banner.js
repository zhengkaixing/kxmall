import request from '@/utils/request'

// 查询广告图列表
export function listBanner(query) {
  return request({
    url: '/banner/banner/list',
    method: 'get',
    params: query
  })
}

// 查询广告图详细
export function getBanner(id) {
  return request({
    url: '/banner/banner/' + id,
    method: 'get'
  })
}

// 新增广告图
export function addBanner(data) {
  return request({
    url: '/banner/banner',
    method: 'post',
    data: data
  })
}

// 修改广告图
export function updateBanner(data) {
  return request({
    url: '/banner/banner',
    method: 'put',
    data: data
  })
}

// 删除广告图
export function delBanner(id) {
  return request({
    url: '/banner/banner/' + id,
    method: 'delete'
  })
}
