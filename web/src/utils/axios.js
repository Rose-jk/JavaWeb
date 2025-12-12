import axios from 'axios'
import { Message } from 'element-ui'
import router from '../router'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8081/api',
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在请求头中添加token
    const token = localStorage.getItem('userToken')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果后端返回的状态码不是200，则判断为错误
    if (res.code !== 200 && res.code !== undefined) {
      Message({
        message: res.message || '请求失败',
        type: 'error',
        duration: 3000
      })
      
      // 如果是未授权，跳转到登录页
      if (res.code === 401) {
        localStorage.removeItem('userToken')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    // 如果后端使用success字段
    if (res.success !== undefined && !res.success) {
      Message({
        message: res.message || '请求失败',
        type: 'error',
        duration: 3000
      })
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  error => {
    console.error('响应错误:', error)
    
    if (error.response) {
      switch (error.response.status) {
        case 400:
          error.message = '请求参数错误'
          break
        case 401:
          error.message = '未授权，请重新登录'
          localStorage.removeItem('userToken')
          localStorage.removeItem('userInfo')
          router.push('/login')
          break
        case 403:
          error.message = '拒绝访问'
          break
        case 404:
          error.message = '请求地址不存在'
          break
        case 500:
          error.message = '服务器内部错误'
          break
        default:
          error.message = `连接错误 ${error.response.status}`
      }
    } else {
      error.message = '网络连接异常，请检查网络设置'
    }
    
    Message({
      message: error.message,
      type: 'error',
      duration: 3000
    })
    
    return Promise.reject(error)
  }
)

export default service