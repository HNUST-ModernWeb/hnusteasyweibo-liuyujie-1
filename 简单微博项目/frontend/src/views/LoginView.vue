<template>
  <div class="login-container">
    <h2>登录</h2>
    <el-form :model="form" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin">登录</el-button>
        <router-link to="/register">去注册</router-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
const form = reactive({ username: '', password: '' })

const handleLogin = async () => {
  const success = await userStore.login(form)
  if (success) router.push('/')
}
</script>

<style scoped>
.login-container { max-width: 400px; margin: 50px auto; }
</style>