<template>
  <div class="publish-container">
    <h2>发布微博</h2>
    <el-input v-model="content" type="textarea" :rows="5" placeholder="分享新鲜事..." />
    <div class="upload-section">
      <el-upload
        action="#"
        :http-request="customUpload"
        list-type="picture-card"
        :file-list="fileList"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
      >
        <el-icon><Plus /></el-icon>
      </el-upload>
      <el-dialog v-model="dialogVisible">
        <img w-full :src="dialogImageUrl" alt="Preview" />
      </el-dialog>
    </div>
    <el-button type="primary" @click="publish" :loading="publishing">发布</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { uploadImage } from '@/api/upload'
import { createPost } from '@/api/post'
import { useRouter } from 'vue-router'

const router = useRouter()
const content = ref('')
const fileList = ref([])
const uploadedUrls = ref([])
const publishing = ref(false)
const dialogVisible = ref(false)
const dialogImageUrl = ref('')

const customUpload = async (options) => {
  try {
    const res = await uploadImage(options.file)
    // 图片上传返回可能直接是 { url: ... }，没有 success 字段
    const url = res.url || res.data.url
    uploadedUrls.value.push(url)
    fileList.value.push({ name: options.file.name, url })
    options.onSuccess()
  } catch (e) {
    options.onError()
  }
}

const handleRemove = (file) => {
  const index = fileList.value.findIndex(f => f.url === file.url)
  if (index > -1) {
    uploadedUrls.value.splice(index, 1)
  }
}

const handlePreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

const publish = async () => {
  if (!content.value.trim()) {
    ElMessage.warning('内容不能为空')
    return
  }
  publishing.value = true
  try {
    const images = uploadedUrls.value.join(',')
    const res = await createPost({ content: content.value, images })
    if (res.success) {
      ElMessage.success('发布成功')
      router.push('/')
    } else {
      ElMessage.error(res.message || '发布失败')
    }
  } catch (e) {
    // 错误已由拦截器提示
  } finally {
    publishing.value = false
  }
}
</script>

<style scoped>
.publish-container { max-width: 600px; }
.upload-section { margin: 20px 0; }
</style>