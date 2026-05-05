<template>
  <div class="profile-page">
    <div class="profile-header">
      <el-avatar :size="80" :src="displayUser?.avatar || defaultAvatar" class="avatar" />
      <h2 class="profile-name">{{ displayUser?.username || '用户' }}</h2>
      <p class="profile-bio">{{ displayUser?.bio || '暂无简介' }}</p>
      <p class="profile-email" v-if="displayUser?.email">{{ displayUser.email }}</p>
      <div class="profile-stats">
        <div class="stat-item">
          <span class="stat-value">{{ total }}</span>
          <span class="stat-label">微博</span>
        </div>
      </div>
    </div>

    <div class="profile-posts">
      <div class="section-title">
        <span>Ta 的微博</span>
        <span class="post-count">共 {{ total }} 条</span>
      </div>
      <div v-if="loading" class="loading-state" v-loading="loading"></div>
      <div v-else-if="posts.length === 0" class="empty-state">
        <el-empty description="还没有发布过微博" />
      </div>
      <div v-else>
        <PostItem v-for="post in posts" :key="post.id" :post="post" @refresh="fetchPosts" />
        <div class="pagination-wrapper" v-if="total > pageSize">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUserPosts } from '@/api/post'
import PostItem from '@/components/PostItem.vue'

const route = useRoute()
const userStore = useUserStore()
const userId = computed(() => route.params.userId)

const posts = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const isSelf = computed(() => {
  return userStore.userInfo && userStore.userInfo.id == userId.value
})

const displayUser = computed(() => {
  if (isSelf.value) {
    return userStore.userInfo
  }
  return { username: `用户${userId.value}`, bio: '这个人很懒，什么都没写~', email: '' }
})

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await getUserPosts(userId.value, currentPage.value - 1, pageSize.value)
    if (res.success) {
      const page = res.data
      posts.value = page.content
      total.value = page.totalElements
    } else {
      posts.value = []
      total.value = 0
    }
  } catch (e) {
    posts.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchPosts()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.profile-page { max-width: 680px; margin: 0 auto; }
.profile-header { background: white; border-radius: 24px; padding: 32px 24px 24px; margin-bottom: 20px; text-align: center; box-shadow: 0 4px 16px rgba(0,0,0,0.04); }
.avatar { border: 3px solid white; box-shadow: 0 4px 12px rgba(255,130,0,0.2); margin-bottom: 16px; }
.profile-name { font-size: 24px; font-weight: 700; margin-bottom: 8px; color: #1f2d3d; }
.profile-bio { color: #4a5568; font-size: 15px; margin-bottom: 8px; }
.profile-email { color: #86909c; font-size: 14px; margin-bottom: 20px; }
.profile-stats { display: flex; justify-content: center; border-top: 1px solid #f0f0f0; padding-top: 20px; }
.stat-item { display: flex; flex-direction: column; align-items: center; }
.stat-value { font-size: 20px; font-weight: 700; color: #1f2d3d; }
.stat-label { font-size: 14px; color: #86909c; margin-top: 4px; }
.section-title { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 16px; font-size: 18px; font-weight: 600; color: #1f2d3d; }
.post-count { font-size: 14px; font-weight: 400; color: #86909c; }
.empty-state { background: white; border-radius: 20px; padding: 40px; text-align: center; }
.pagination-wrapper { display: flex; justify-content: center; margin-top: 24px; }
.loading-state { min-height: 200px; background: white; border-radius: 20px; }
</style>