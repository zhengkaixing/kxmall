import request from '@/utils/request'

// 查询商品秒杀列表
export function listStoreSeckill(query) {
  return request({
    url: '/seckill/storeSeckill/list',
    method: 'get',
    params: query
  })
}

// 查询商品秒杀详细
export function getStoreSeckill(id) {
  return request({
    url: '/seckill/storeSeckill/' + id,
    method: 'get'
  })
}

// 新增商品秒杀
export function addStoreSeckill(data) {
  return request({
    url: '/seckill/storeSeckill',
    method: 'post',
    data: data
  })
}

// 修改商品秒杀
export function updateStoreSeckill(data) {
  return request({
    url: '/seckill/storeSeckill',
    method: 'put',
    data: data
  })
}

// 删除商品秒杀
export function delStoreSeckill(id) {
  return request({
    url: '/seckill/storeSeckill/' + id,
    method: 'delete'
  })
}
