import request from '@/utils/request'

// 查询推荐管理列表
export function listRecommend(query) {
  return request({
    url: '/recommend/recommend/list',
    method: 'get',
    params: query
  })
}

// 查询推荐管理详细
export function getRecommend(id) {
  return request({
    url: '/recommend/recommend/' + id,
    method: 'get'
  })
}

// 新增推荐管理
export function addRecommend(data) {
  return request({
    url: '/recommend/recommend',
    method: 'post',
    data: data
  })
}

// 修改推荐管理
export function updateRecommend(data) {
  return request({
    url: '/recommend/recommend',
    method: 'put',
    data: data
  })
}

// 删除推荐管理
export function delRecommend(id) {
  return request({
    url: '/recommend/recommend/' + id,
    method: 'delete'
  })
}

export function addRecommendBatch(data) {
  return request({
    url: '/recommend/recommend/addRecommendBatch',
    method: 'post',
    data
  })
}
