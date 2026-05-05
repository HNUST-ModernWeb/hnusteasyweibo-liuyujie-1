<template>
  <div v-if="post">
    <PostItem :post="post" @refresh="fetchPost" />
    <div class="comments">
      <h3>评论 ({{ comments.length }})</h3>
      <CommentItem v-for="c in comments" :key="c.id" :comment="c" />
      <div class="add-comment">
        <el-input v-model="commentContent" placeholder="写评论..." />
        <el-button type="primary" @click="submitComment" :loading="commentSubmitting">发表</el-button>
      </div>
    </div>
  </div>
  <div v-else-if="loading" v-loading="loading" style="min-height: 200px;"></div>
  <div v-else class="not-found">微博不存在或已被删除</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPost } from '@/api/post'
import { getComments, addComment } from '@/api/comment'
import PostItem from '@/components/PostItem.vue'
import CommentItem from '@/components/CommentItem.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const post = ref(null)
const comments = ref([])
const commentContent = ref('')
const loading = ref(true)
const commentSubmitting = ref(false)

const fetchPost = async () => {
  try {
    const res = await getPost(route.params.postId)
    if (res.success) {
      post.value = res.data
    } else {
      post.value = null
    }
  } catch (e) {
    post.value = null
  } finally {
    loading.value = false
  }
}

const fetchComments = async () => {
  try {
    const res = await getComments(route.params.postId)
    if (res.success) {
      comments.value = res.data
    } else {
      comments.value = []
    }
  } catch (e) {
    comments.value = []
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }
  commentSubmitting.value = true
  try {
    const res = await addComment(route.params.postId, { content: commentContent.value })
    if (res.success) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      fetchComments()
    }
  } finally {
    commentSubmitting.value = false
  }
}

onMounted(() => {
  fetchPost()
  fetchComments()
})
</script>

<style scoped>
.comments {
  background: white;
  border-radius: 20px;
  padding: 20px;
  margin-top: 16px;
}
.add-comment {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}
.not-found {
  text-align: center;
  color: #999;
  padding: 40px;
}
</style>