import request from '@/utils/request'

// 仓库分析
export function warehouseAnalysis(query,storageId) {
  return request({
    url: '/finance/warehouse-analysis?month='+query+"&storageId="+storageId,
    method: 'get'
  })
}
// 仓库对比
export function warehouseCompare(query) {
  return request({
    url: '/finance/warehouse-compares?month='+query,
    method: 'get'
  })
}
// 商品汇总报表
export function productSummary(startDate, endDate, storageId) {
  let url = '/finance/product-summary'
  const params = []
  if (startDate) {
    params.push('startDateStr=' + startDate)
  }
  if (endDate) {
    params.push('endDateStr=' + endDate)
  }
  if (storageId) {
    params.push('storageId=' + storageId)
  }
  if (params.length > 0) {
    url += '?' + params.join('&')
  }
  return request({
    url: url,
    method: 'get'
  })
}
