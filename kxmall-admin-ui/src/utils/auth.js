/**
 * Token认证工具类
 * 用于管理用户登录token的存储和获取
 * @author kxmall
 * @date 2024
 */
import Cookies from 'js-cookie'

/**
 * Token在Cookie中的键名
 */
const TokenKey = 'Admin-Token'

/**
 * 获取Token
 * 从Cookie中读取用户登录token
 * @returns {string} 用户token，如果不存在则返回undefined
 */
export function getToken() {
  return Cookies.get(TokenKey)
}

/**
 * 设置Token
 * 将用户登录token存储到Cookie中
 * @param {string} token - 用户登录token
 * @returns {*} Cookie设置结果
 */
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

/**
 * 移除Token
 * 从Cookie中删除用户登录token（用于登出）
 * @returns {*} Cookie删除结果
 */
export function removeToken() {
  return Cookies.remove(TokenKey)
}
