import request from './request'

export const getComments = (postId) => request.get(`/comments/post/${postId}`)
export const addComment = (postId, data) => request.post(`/comments/post/${postId}`, data)