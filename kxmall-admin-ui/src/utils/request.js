/**
 * HTTP请求工具类
 * 封装axios请求，统一处理请求拦截、响应拦截、错误处理等
 * @author kxmall
 * @date 2024
 */
import axios from 'axios'
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from "@/utils/kxmall";
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'

/**
 * 下载加载实例
 * @type {Object}
 */
let downloadLoadingInstance;

/**
 * 是否显示重新登录弹窗标识
 * 防止重复弹出登录提示框
 * @type {Object}
 */
export let isRelogin = { show: false };

/**
 * 设置axios默认请求头
 * Content-Type: 指定请求体的媒体类型
 * Content-Language: 指定响应内容的语言
 */
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
axios.defaults.headers['Content-Language'] = 'zh_CN'

/**
 * 创建axios实例
 * 配置基础URL和超时时间
 */
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 请求超时时间，单位：毫秒
  timeout: 10000
})

/**
 * 请求拦截器
 * 在请求发送前统一处理：添加token、参数转换、防重复提交等
 */
service.interceptors.request.use(config => {
  // 是否需要设置 token（通过headers中的isToken字段控制，默认为true）
  const isToken = (config.headers || {}).isToken === false
  // 是否需要防止数据重复提交（通过headers中的repeatSubmit字段控制，默认为true）
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  
  // 如果存在token且需要设置token，则在请求头中添加Authorization
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken()
  }
  
  // GET请求参数处理：将params转换为URL查询字符串
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    // 移除最后一个&符号
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }
  
  // POST/PUT请求防重复提交处理
  if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,  // 请求地址
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,  // 请求数据（转为字符串便于比较）
      time: new Date().getTime()  // 请求时间戳
    }
    const sessionObj = cache.session.getJSON('sessionObj')
    
    // 如果session中没有存储的请求信息，则直接存储
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      // 获取上次请求的信息
      const s_url = sessionObj.url;                  // 上次请求地址
      const s_data = sessionObj.data;                // 上次请求数据
      const s_time = sessionObj.time;                // 上次请求时间
      const interval = 1000;                         // 间隔时间(ms)，小于此时间视为重复提交
      
      // 判断是否为重复提交：相同URL、相同数据、时间间隔小于1秒
      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = '数据正在处理，请勿重复提交';
        console.warn(`[${s_url}]: ` + message)
        return Promise.reject(new Error(message))
      } else {
        // 不是重复提交，更新session中的请求信息
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }
  return config
}, error => {
    // 请求错误处理
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
})

/**
 * 响应拦截器
 * 统一处理响应数据、错误码、异常情况等
 */
service.interceptors.response.use(res => {
    // 获取响应状态码，未设置则默认200
    const code = res.data.code || 200;
    // 获取错误信息，优先使用errorCode中的定义，其次使用响应中的msg，最后使用默认错误信息
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    
    // 二进制数据（blob或arraybuffer）则直接返回，不进行业务逻辑处理
    if(res.request.responseType ===  'blob' || res.request.responseType ===  'arraybuffer'){
      return res.data
    }
    
    // 401: 未授权，需要重新登录
    if (code === 401) {
      // 防止重复弹出登录提示框
      if (!isRelogin.show) {
        isRelogin.show = true;
        ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { 
          confirmButtonText: '重新登录', 
          cancelButtonText: '取消', 
          type: 'warning' 
        }).then(() => {
          // 用户确认重新登录
          isRelogin.show = false;
          store.dispatch('LogOut').then(() => {
            location.href = import.meta.env.VITE_APP_CONTEXT_PATH + 'index'
          })
        }).catch(() => {
          // 用户取消登录
          isRelogin.show = false;
        });
      }
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } 
    // 500: 服务器内部错误
    else if (code === 500) {
      ElMessage({ message: msg, type: 'error' })
      return Promise.reject(new Error(msg))
    } 
    // 601: 业务警告
    else if (code === 601) {
      ElMessage({ message: msg, type: 'warning' })
      return Promise.reject('error')
    } 
    // 其他非200状态码
    else if (code !== 200) {
      ElNotification.error({ title: msg })
      return Promise.reject('error')
    } 
    // 200: 成功，返回响应数据
    else {
      return res.data
    }
  },
  error => {
    // 网络请求错误处理
    console.error('响应拦截器错误:', error)
    let { message } = error;
    
    // 根据不同的错误类型，提供友好的错误提示
    if (message == "Network Error") {
      message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
      // 提取HTTP状态码
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    
    // 显示错误提示，持续5秒
    ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    return Promise.reject(error)
  }
)

/**
 * 通用文件下载方法
 * @param {string} url - 下载接口地址
 * @param {Object} params - 请求参数
 * @param {string} filename - 下载文件名
 * @param {Object} config - 额外的axios配置
 * @returns {Promise} 下载Promise
 */
export function download(url, params, filename, config) {
  // 显示下载加载提示
  downloadLoadingInstance = ElLoading.service({
    text: '正在下载数据，请稍候',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  
  return service.post(url, params, {
    // 将参数转换为URL编码格式
    transformRequest: [(params) => { return tansParams(params) }],
    // 设置请求头为表单格式
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    // 设置响应类型为blob（二进制数据）
    responseType: 'blob',
    // 合并额外的配置
    ...config
  }).then(async (data) => {
    // 验证返回的数据是否为blob格式（即是否为文件）
    const isLogin = await blobValidate(data);
    if (isLogin) {
      // 是文件数据，创建Blob对象并下载
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      // 不是文件数据，说明是错误信息，解析并显示
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      ElMessage.error(errMsg)
    }
    // 关闭加载提示
    downloadLoadingInstance.close();
  }).catch((r) => {
    // 下载失败处理
    console.error('文件下载失败:', r)
    ElMessage.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close();
  })
}

/**
 * 导出axios实例
 * 供其他模块使用
 */
export default service
