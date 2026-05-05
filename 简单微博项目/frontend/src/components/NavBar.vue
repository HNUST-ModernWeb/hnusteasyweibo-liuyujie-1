<template>
  <div class="navbar">
    <router-link to="/" class="logo">微博</router-link>
    <div class="nav-links">
      <router-link to="/">首页</router-link>
      <router-link to="/publish" v-if="userStore.token">发布</router-link>
      <router-link :to="`/profile/${userStore.userInfo?.id}`" v-if="userStore.token">
        {{ userStore.userInfo?.username }}
      </router-link>
      <a v-if="userStore.token" @click="handleLogout">退出</a>
      <router-link to="/login" v-else>登录</router-link>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}
.logo { font-size: 24px; font-weight: bold; text-decoration: none; color: #333; }
.nav-links a { margin-left: 20px; text-decoration: none; color: #666; cursor: pointer; }
.nav-links a.router-link-active { color: #409EFF; }
</style>