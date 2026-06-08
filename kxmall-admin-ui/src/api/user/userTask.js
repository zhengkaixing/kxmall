import request from '@/utils/request'

// 查询用户任务列表
export function listUserTask(query) {
    return request({
        url: '/user/userTask/list',
        method: 'get',
        params: query
    })
}

// 查询用户任务详细
export function getUserTask(id) {
    return request({
        url: '/user/userTask/' + id,
        method: 'get'
    })
}

// 新增用户任务
export function addUserTask(data) {
    return request({
        url: '/user/userTask',
        method: 'post',
        data: data
    })
}

// 修改用户任务
export function updateUserTask(data) {
    return request({
        url: '/user/userTask',
        method: 'put',
        data: data
    })
}

// 删除用户任务
export function delUserTask(id) {
    return request({
        url: '/user/userTask/' + id,
        method: 'delete'
    })
}

// 导出用户任务
export function exportUserTask(query) {
    return request({
        url: '/user/userTask/export',
        method: 'get',
        params: query
    })
}
