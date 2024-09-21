import request from '@/utils/request'


// 首行统计内容
export function dataCount(data) {
  return request({
    method: 'get',
    url: '/dashboard/dashboard/data/count?storageId='+data
  })
}
// 用户数量统计
export function countUser(data) {
  return request({
    method: 'get',
    url: '/dashboard/dashboard/countUser',
  })
}
// 销售统计
export function getSalesStatement(data) {
  return request({
    method: 'get',
    url: '/dashboard/dashboard/getSalesStatement?storageId='+data,
  })
}

// 近两日销售统计
export function getTodayAndYesterdaySales(data) {
  return request({
    method: 'get',
    url: '/dashboard/dashboard/getTodayAndYesterdaySales?storageId='+data,
  })
}

// 聚合数据
export function integral(data) {
  return request({
    method: 'get',
    url: '/dashboard/dashboard/integral?storageId='+data,
  })
}

// 按小时统计销量
export function getSalesByHour(data) {
  return request({
    method: 'get',
    url: '/dashboard/dashboard/getSalesByHour?storageId='+data,
  })
}

// 获取仓库
export function storageList() {
  return request({
    url: '/storage/storage/listAll',
    method: 'get'
  })
}
