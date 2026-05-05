<template>
  <article class="post-card">
    <div class="post-header">
      <router-link :to="`/profile/${post.user.id}`" class="user-link">
        <el-avatar :size="44" :src="post.user.avatar || defaultAvatar" />
        <div class="user-meta">
          <span class="username">{{ post.user.username }}</span>
          <span class="time">{{ formatTime(post.createdAt) }}</span>
        </div>
      </router-link>
      <el-dropdown v-if="isOwner" trigger="click" @command="handleCommand">
        <el-icon class="more-icon"><MoreFilled /></el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="delete">删除</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <div class="post-body">
      <p class="content">{{ post.content }}</p>
      <div v-if="imageList.length" class="image-grid" :class="`grid-${Math.min(imageList.length, 4)}`">
        <div v-for="(img, idx) in imageList.slice(0, 4)" :key="idx" class="image-item">
          <img :src="img" @click="previewImage(img)" alt="图片" />
          <div v-if="idx === 3 && imageList.length > 4" class="more-overlay">
            +{{ imageList.length - 4 }}
          </div>
        </div>
      </div>
    </div>

    <div class="post-actions">
      <div class="action-btn" @click="handleLike" :class="{ liked: liked }">
        <span class="heart-icon">{{ liked ? '❤️' : '🤍' }}</span>
        <span>{{ post.likeCount || 0 }}</span>
      </div>
      <div class="action-btn" @click="goDetail">
        <el-icon><ChatDotRound /></el-icon>
        <span>{{ commentCount }}</span>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { toggleLike } from '@/api/like'
import { getComments } from '@/api/comment'
import { deletePost } from '@/api/post'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, StarFilled, ChatDotRound, MoreFilled } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const props = defineProps({ post: Object })
const emit = defineEmits(['refresh'])
const router = useRouter()
const userStore = useUserStore()

// 关键点：从后端返回的 likedByCurrentUser 初始化
const liked = ref(props.post.likedByCurrentUser || false)
const commentCount = ref(0)
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const imageList = computed(() => props.post.images ? props.post.images.split(',') : [])
const isOwner = computed(() => userStore.userInfo?.id === props.post.user.id)

const formatTime = (time) => dayjs(time).format('MM-DD HH:mm')

const fetchComments = async () => {
  try {
    const res = await getComments(props.post.id)
    if (res.success) {
      commentCount.value = res.data.length
    }
  } catch (e) {}
}

const handleLike = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const res = await toggleLike(props.post.id)
    if (res.success) {
      liked.value = res.data
      props.post.likeCount += res.data ? 1 : -1
    }
  } catch (e) {}
}

const goDetail = () => {
  router.push(`/post/${props.post.id}`)
}

const previewImage = (url) => {
  window.open(url, '_blank')
}

const handleCommand = async (command) => {
  if (command === 'delete') {
    try {
      await ElMessageBox.confirm('确定删除这条微博吗？', '提示', { type: 'warning' })
      const res = await deletePost(props.post.id)
      if (res.success) {
        ElMessage.success('删除成功')
        emit('refresh')
      }
    } catch (e) {}
  }
}

onMounted(() => {
  fetchComments()
})
</script>

<style scoped>
.post-card {
  background: white;
  border-radius: 20px;
  padding: 18px 16px 12px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.post-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 12px;
}
.user-link {
  display: flex;
  gap: 12px;
  text-decoration: none;
}
.user-meta {
  display: flex;
  flex-direction: column;
}
.username {
  font-weight: 700;
  color: #1f2d3d;
  font-size: 16px;
}
.time {
  font-size: 13px;
  color: #86909c;
  margin-top: 2px;
}
.more-icon {
  color: #86909c;
  font-size: 18px;
  cursor: pointer;
  padding: 4px;
}
.post-body {
  margin-bottom: 16px;
}
.content {
  font-size: 15px;
  line-height: 1.6;
  color: #1f2d3d;
  margin-bottom: 12px;
  white-space: pre-wrap;
  word-break: break-word;
}
.image-grid {
  display: grid;
  gap: 6px;
  margin-top: 8px;
}
.grid-1 { grid-template-columns: 1fr; max-height: 300px; }
.grid-2 { grid-template-columns: 1fr 1fr; }
.grid-3, .grid-4 { grid-template-columns: 1fr 1fr 1fr; }
.image-item {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  background: #f2f3f5;
  aspect-ratio: 1 / 1;
}
.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.3s;
}
.image-item img:hover { transform: scale(1.03); }
.more-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
}
.post-actions {
  display: flex;
  gap: 24px;
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
  margin-top: 4px;
}
.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #86909c;
  font-size: 14px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 20px;
  transition: all 0.2s;
}
.action-btn:hover { background: #f5f7fa; color: #ff8200; }
.action-btn.liked { color: #ff8200; }

/* 爱心符号样式 */
.heart-icon {
  font-size: 18px;
  line-height: 1;
}
</style>