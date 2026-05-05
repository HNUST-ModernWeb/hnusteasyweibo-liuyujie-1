import request from './request'

export const getPosts = (page = 0, size = 10) => request.get('/posts', { params: { page, size } })
export const getUserPosts = (userId, page = 0, size = 10) => request.get(`/posts/user/${userId}`, { params: { page, size } })
export const createPost = (data) => request.post('/posts', data)
export const getPost = (id) => request.get(`/posts/${id}`)
export const deletePost = (id) => request.delete(`/posts/${id}`)