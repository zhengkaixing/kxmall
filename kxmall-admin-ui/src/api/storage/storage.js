import request from '@/utils/request'

// 查询仓库管理列表
export function listStorage(query) {
  return request({
    url: '/storage/storage/list',
    method: 'get',
    params: query
  })
}

// 查询仓库管理详细
export function getStorage(id) {
  return request({
    url: '/storage/storage/' + id,
    method: 'get'
  })
}

// 新增仓库管理
export function addStorage(data) {
  return request({
    url: '/storage/storage',
    method: 'post',
    data: data
  })
}

// 修改仓库管理
export function updateStorage(data) {
  return request({
    url: '/storage/storage',
    method: 'put',
    data: data
  })
}

// 删除仓库管理
export function delStorage(id) {
  return request({
    url: '/storage/storage/' + id,
    method: 'delete'
  })
}

// 前置仓状态批量更新为正常
export function updateStateToNomral(data) {
  return request({
    url: '/storage/storage/updateStateToNomral',
    method: 'post',
    data
  })
}

// 前置仓状态批量更新为禁用
export function updateStateToAbort(data) {
  return request({
    url: '/storage/storage/updateStateToAbort',
    method: 'post',
    data
  })
}

// 前置仓营业状态批量更新为休息中
export function updateBusinessStateToRest(data) {
  return request({
    url: '/storage/storage/updateBusinessStateToRest',
    method: 'post',
    data
  })
}

// 前置仓营业状态批量更新为营业中
export function updateBusinessStateToOpen(data) {
  return request({
    url: '/storage/storage/updateBusinessStateToOpen',
    method: 'post',
    data
  })
}

export function listAllStorage() {
  return request({
    url: '/storage/storage/listAll',
    method: 'get'
  })
}
