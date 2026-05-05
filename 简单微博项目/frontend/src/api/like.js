import request from './request'

export const toggleLike = (postId) => request.post(`/likes/post/${postId}`)