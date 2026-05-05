<template>
  <div class="comment-item">
    <span class="username">{{ comment.user.username }}</span>:
    <span class="content">{{ comment.content }}</span>
    <span class="time">{{ formatTime(comment.createdAt) }}</span>
  </div>
</template>

<script setup>
import dayjs from 'dayjs'
const props = defineProps({ comment: Object })
const formatTime = (time) => dayjs(time).format('MM-DD HH:mm')
</script>

<style scoped>
.comment-item { padding: 8px 0; border-bottom: 1px dashed #eee; }
.username { font-weight: bold; margin-right: 5px; }
.time { font-size: 12px; color: #999; margin-left: 10px; }
</style>