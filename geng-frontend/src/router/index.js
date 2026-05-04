import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PostDetailView from '../views/PostDetailView.vue'
import DashboardView from '../views/DashboardView.vue'
import CreatePostView from '../views/CreatePostView.vue'
import AuthView from '../views/AuthView.vue'
import ProfileView from '../views/ProfileView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomeView },
    { path: '/post/:id', component: PostDetailView },
    { path: '/dashboard', component: DashboardView },
    { path: '/create', component: CreatePostView, meta: { requiresAuth: true } },
    { path: '/auth', component: AuthView },
    { path: '/profile/:id', component: ProfileView },
  ],
  scrollBehavior: () => ({ top: 0 })
})

router.beforeEach((to, _from, next) => {
  if (to.meta.requiresAuth) {
    const u = JSON.parse(localStorage.getItem('gd_user') || 'null')
    if (!u) return next('/auth')
  }
  next()
})

export default router
