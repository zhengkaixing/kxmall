import request from '@/utils/request'

// 查询运费模板列表
export function listTemplates(query) {
  return request({
    url: '/templates/templates/list',
    method: 'get',
    params: query
  })
}

// 查询运费模板详细
export function getTemplates(id) {
  return request({
    url: '/templates/templates/' + id,
    method: 'get'
  })
}

// 新增运费模板
export function addTemplates(data) {
  return request({
    url: '/templates/templates',
    method: 'post',
    data: data
  })
}

// 修改运费模板
export function updateTemplates(data) {
  return request({
    url: '/templates/templates',
    method: 'put',
    data: data
  })
}

// 删除运费模板
export function delTemplates(id) {
  return request({
    url: '/templates/templates/' + id,
    method: 'delete'
  })
}

// 删除运费模板
export function listCities() {
  return request({
    url: '/templates/templates/citys',
    method: 'get'
  })
}
