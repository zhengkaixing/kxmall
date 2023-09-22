import request from '@/utils/request'

// 查询用户等级列表
export function listUserLevel(query) {
  return request({
    url: '/user/userLevelSetting/list',
    method: 'get',
    params: query
  })
}

// 查询用户等级详细
export function getUserLevel(id) {
  return request({
    url: '/user/userLevelSetting/' + id,
    method: 'get'
  })
}

// 新增用户等级
export function addUserLevel(data) {
  return request({
    url: '/user/userLevelSetting',
    method: 'post',
    data: data
  })
}

// 修改用户等级
export function updateUserLevel(data) {
  return request({
    url: '/user/userLevelSetting',
    method: 'put',
    data: data
  })
}

// 删除用户等级
export function delUserLevel(id) {
  return request({
    url: '/user/userLevelSetting/' + id,
    method: 'delete'
  })
}
