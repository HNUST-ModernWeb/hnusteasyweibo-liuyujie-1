import { defineStore } from 'pinia'
import { login as apiLogin, register as apiRegister } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null')
  }),
  
  actions: {
    async login(credentials) {
      try {
        const res = await apiLogin(credentials)
        if (res.success) {
          this.token = res.data.token
          this.userInfo = res.data.user
          localStorage.setItem('token', this.token)
          localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
          ElMessage.success('登录成功')
          return true
        } else {
          ElMessage.error(res.message || '登录失败')
          return false
        }
      } catch (error) {
        ElMessage.error('登录失败')
        return false
      }
    },
    
    async register(userData) {
      try {
        const res = await apiRegister(userData)
        if (res.success) {
          ElMessage.success('注册成功，请登录')
          return true
        } else {
          ElMessage.error(res.message || '注册失败')
          return false
        }
      } catch (error) {
        ElMessage.error('注册失败')
        return false
      }
    },
    
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.info('已退出登录')
    }
  },
  
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user',
        storage: localStorage,
        paths: ['token', 'userInfo']
      }
    ]
  }
})