import request from '@/utils/request'

// 查询中国地区信息列表
export function listRegion(query) {
  return request({
    url: '/region/region/list',
    method: 'get',
    params: query
  })
}

// 查询中国地区信息详细
export function getRegion(id) {
  return request({
    url: '/region/region/' + id,
    method: 'get'
  })
}

// 新增中国地区信息
export function addRegion(data) {
  return request({
    url: '/region/region',
    method: 'post',
    data: data
  })
}

// 修改中国地区信息
export function updateRegion(data) {
  return request({
    url: '/region/region',
    method: 'put',
    data: data
  })
}

// 删除中国地区信息
export function delRegion(id) {
  return request({
    url: '/region/region/' + id,
    method: 'delete'
  })
}

export function getProvinceAll() {
  return request({
    url: '/region/region/getProvinceAll',
    method: 'get'
  })
}

// 根据省份主键获取城市
export function getCity(params) {
  return request({
    url: '/region/region/getCity',
    method: 'get',
    params
  })
}

// 根据城市主键获取区(县)
export function getCounty(params) {
  return request({
    url: '/region/region/getCounty',
    method: 'get',
    params
  })
}
