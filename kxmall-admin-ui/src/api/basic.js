import request from '@/utils/request'

export function querBasic(params) {
  return request({
    method: 'get',
    url: '/basic/queryBasic',
    params
  })
}
