import request from '@/utils/request'

// 查询快递公司列表
export function listExpress(query) {
  return request({
    url: '/system/express/list',
    method: 'get',
    params: query
  })
}

// 查询快递公司详细
export function getExpress(id) {
  return request({
    url: '/system/express/' + id,
    method: 'get'
  })
}

// 新增快递公司
export function addExpress(data) {
  return request({
    url: '/system/express',
    method: 'post',
    data: data
  })
}

// 修改快递公司
export function updateExpress(data) {
  return request({
    url: '/system/express',
    method: 'put',
    data: data
  })
}

// 删除快递公司
export function delExpress(id) {
  return request({
    url: '/system/express/' + id,
    method: 'delete'
  })
}
