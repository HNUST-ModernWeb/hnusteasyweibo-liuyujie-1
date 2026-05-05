import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  { path: '/', component: () => import('@/views/HomeView.vue') },
  { path: '/login', component: () => import('@/views/LoginView.vue') },
  { path: '/register', component: () => import('@/views/RegisterView.vue') },
  { 
    path: '/publish', 
    component: () => import('@/views/PublishView.vue'),
    meta: { requiresAuth: true }
  },
  { 
    path: '/profile/:userId', 
    component: () => import('@/views/ProfileView.vue'),
    props: true
  },
  { 
    path: '/post/:postId', 
    component: () => import('@/views/PostDetailView.vue'),
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router