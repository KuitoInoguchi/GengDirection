<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <RouterLink class="logo" to="/">
        <span class="logo-badge">梗</span>
        <span class="logo-word">梗向</span>
      </RouterLink>

      <div class="nav-links">
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/dashboard">仪表盘</RouterLink>
        <RouterLink v-if="userStore.isLoggedIn" to="/create">发帖</RouterLink>
      </div>

      <div class="nav-user">
        <template v-if="userStore.isLoggedIn">
          <RouterLink :to="`/profile/${userStore.user.id}`" class="user-chip">
            <span class="avatar">{{ userStore.user.nickname[0] }}</span>
            <span class="nick">{{ userStore.user.nickname }}</span>
          </RouterLink>
          <button class="logout-btn" @click="handleLogout">退出</button>
        </template>
        <RouterLink v-else to="/auth" class="login-btn">登录 / 注册</RouterLink>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user.js'

const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0; left: 0; right: 0;
  height: var(--nav-h);
  background: rgba(255, 255, 255, 0.94);
  backdrop-filter: blur(14px);
  border-bottom: 1px solid var(--border);
  z-index: 1000;
  box-shadow: 0 2px 20px rgba(75, 99, 247, 0.06);
}
.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 24px;
  gap: 24px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  flex-shrink: 0;
}
.logo-badge {
  width: 36px; height: 36px;
  background: var(--primary);
  color: #fff;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 17px;
  font-weight: 700;
}
.logo-word {
  font-size: 19px;
  font-weight: 700;
  color: var(--primary);
  letter-spacing: 0.5px;
}
.nav-links {
  display: flex;
  gap: 2px;
  flex: 1;
}
.nav-links a {
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  color: var(--text-muted);
  transition: all 0.18s;
  text-decoration: none;
}
.nav-links a:hover { background: var(--bg); color: var(--text); }
.nav-links a.router-link-exact-active {
  background: var(--primary-light);
  color: var(--primary);
}
.nav-user {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}
.user-chip {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  padding: 4px 12px 4px 4px;
  border-radius: 20px;
  transition: background 0.15s;
}
.user-chip:hover { background: var(--bg); }
.avatar {
  width: 32px; height: 32px;
  background: var(--primary);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
}
.nick { font-size: 14px; font-weight: 500; color: var(--text); }
.logout-btn {
  font-size: 13px;
  color: var(--text-muted);
  padding: 6px 12px;
  border-radius: 7px;
  transition: all 0.15s;
}
.logout-btn:hover { background: #FFF0F0; color: var(--accent); }
.login-btn {
  padding: 8px 20px;
  background: var(--primary);
  color: #fff;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  transition: opacity 0.2s;
}
.login-btn:hover { opacity: 0.85; }
</style>
