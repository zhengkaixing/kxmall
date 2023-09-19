import request from '@/utils/request'

// 查询用户账单列表
export function listUserBill(query) {
  return request({
    url: '/user/userBill/list',
    method: 'get',
    params: query
  })
}

// 查询用户账单详细
export function getUserBill(id) {
  return request({
    url: '/user/userBill/' + id,
    method: 'get'
  })
}

// 新增用户账单
export function addUserBill(data) {
  return request({
    url: '/user/userBill',
    method: 'post',
    data: data
  })
}

// 修改用户账单
export function updateUserBill(data) {
  return request({
    url: '/user/userBill',
    method: 'put',
    data: data
  })
}

// 删除用户账单
export function delUserBill(id) {
  return request({
    url: '/user/userBill/' + id,
    method: 'delete'
  })
}
