import request from '@/utils/request'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/user/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUser(uid) {
  return request({
    url: '/user/user/' + uid,
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/user/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/user/user',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(uid) {
  return request({
    url: '/user/user/' + uid,
    method: 'delete'
  })
}

// 更改用户状态
export function onStatus(uid, data) {
  return request({
    url: '/user/user/onStatus/' + uid,
    method: 'post',
    data
  })
}

// 查看下级
export function spread(data) {
  return request({
    url: '/user/user/spread',
    method: 'post',
    data
  })
}

// 修改余额
export function money(data) {
  return request({
    url: '/user/user/money',
    method: 'post',
    data
  })
}
