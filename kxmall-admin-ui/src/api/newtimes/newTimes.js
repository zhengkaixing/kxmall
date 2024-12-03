import request from '@/utils/request'

// 查询新鲜时报列表
export function listNewTimes(query) {
  return request({
    url: '/newtimes/newTimes/list',
    method: 'get',
    params: query
  })
}

// 查询新鲜时报详细
export function getNewTimes(id) {
  return request({
    url: '/newtimes/newTimes/' + id,
    method: 'get'
  })
}

// 新增新鲜时报
export function addNewTimes(data) {
  return request({
    url: '/newtimes/newTimes',
    method: 'post',
    data: data
  })
}

// 修改新鲜时报
export function updateNewTimes(data) {
  return request({
    url: '/newtimes/newTimes',
    method: 'put',
    data: data
  })
}

// 删除新鲜时报
export function delNewTimes(id) {
  return request({
    url: '/newtimes/newTimes/' + id,
    method: 'delete'
  })
}
