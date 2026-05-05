<template>
  <div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="推荐" name="recommend"></el-tab-pane>
    </el-tabs>
    <div v-if="loading" v-loading="loading" style="min-height: 200px;"></div>
    <div v-else>
      <PostItem v-for="post in posts" :key="post.id" :post="post" @refresh="fetchPosts" />
      <div v-if="posts.length === 0 && !loading" class="empty-tip">
        暂无微博，快去发布第一条吧！
      </div>
      <el-pagination
        v-if="total > pageSize"
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handlePageChange"
        style="justify-content: center; margin-top: 20px;"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getPosts } from '@/api/post'
import { ElMessage } from 'element-plus'
import PostItem from '@/components/PostItem.vue'

const activeTab = ref('recommend')
const posts = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await getPosts(currentPage.value - 1, pageSize.value)
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
}

onMounted(fetchPosts)
</script>

<style scoped>
.empty-tip {
  text-align: center;
  color: #999;
  padding: 40px;
  background: white;
  border-radius: 20px;
  margin-top: 16px;
}
</style>