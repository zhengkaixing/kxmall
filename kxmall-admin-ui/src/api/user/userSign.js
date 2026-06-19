import request from '@/utils/request'

// 查询签到记录列表
export function listUserSign(query) {
  return request({
    url: '/user/userSign/list',
    method: 'get',
    params: query
  })
}

// 查询签到记录详细
export function getUserSign(id) {
  return request({
    url: '/user/userSign/' + id,
    method: 'get'
  })
}

// 新增签到记录
export function addUserSign(data) {
  return request({
    url: '/user/userSign',
    method: 'post',
    data: data
  })
}

// 修改签到记录
export function updateUserSign(data) {
  return request({
    url: '/user/userSign',
    method: 'put',
    data: data
  })
}

// 删除签到记录
export function delUserSign(id) {
  return request({
    url: '/user/userSign/' + id,
    method: 'delete'
  })
}
