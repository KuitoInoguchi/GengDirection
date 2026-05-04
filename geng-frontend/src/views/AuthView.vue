<template>
  <div class="auth-wrap">
    <div class="auth-card">
      <div class="auth-logo">
        <span class="logo-badge">梗</span>
        <span class="logo-word">梗向</span>
      </div>

      <div class="tab-row">
        <button :class="['tab-btn', { active: mode === 'login' }]" @click="mode = 'login'">登录</button>
        <button :class="['tab-btn', { active: mode === 'register' }]" @click="mode = 'register'">注册</button>
      </div>

      <form @submit.prevent="handleSubmit">
        <div class="field">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="3-64 位字符" required />
        </div>

        <div v-if="mode === 'register'" class="field">
          <label>昵称</label>
          <input v-model="form.nickname" type="text" placeholder="显示名称" required />
        </div>

        <div class="field">
          <label>密码</label>
          <input v-model="form.password" type="password"
            :placeholder="mode === 'register' ? '8位以上，含大小写字母、数字、特殊符号' : ''" required />
        </div>

        <div v-if="errMsg" class="err-msg">{{ errMsg }}</div>

        <button type="submit" class="btn-primary submit-btn" :disabled="loading">
          {{ loading ? '处理中...' : (mode === 'login' ? '登录' : '注册') }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api/index.js'
import { useUserStore } from '../store/user.js'

const router = useRouter()
const userStore = useUserStore()

const mode = ref('login')
const loading = ref(false)
const errMsg = ref('')
const form = reactive({ username: '', password: '', nickname: '' })

async function handleSubmit() {
  errMsg.value = ''
  loading.value = true
  try {
    if (mode.value === 'login') {
      const user = await api.login({ username: form.username, password: form.password })
      userStore.login(user)
      router.push('/')
    } else {
      await api.register({ username: form.username, password: form.password, nickname: form.nickname })
      mode.value = 'login'
      errMsg.value = '注册成功，请登录'
    }
  } catch (e) {
    errMsg.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-wrap {
  min-height: calc(100vh - var(--nav-h) - 32px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}
.auth-card {
  width: 100%;
  max-width: 420px;
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  padding: 40px;
  box-shadow: var(--shadow);
}
.auth-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 32px;
}
.logo-badge {
  width: 42px; height: 42px;
  background: var(--primary);
  color: #fff;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
}
.logo-word { font-size: 22px; font-weight: 800; color: var(--primary); }
.tab-row {
  display: flex;
  gap: 0;
  background: var(--bg);
  border-radius: 9px;
  padding: 4px;
  margin-bottom: 28px;
}
.tab-btn {
  flex: 1;
  padding: 9px;
  border-radius: 7px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-muted);
  transition: all 0.15s;
}
.tab-btn.active { background: var(--surface); color: var(--primary); box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}
.field label { font-size: 13px; font-weight: 600; color: var(--text); }
.field input {
  padding: 10px 14px;
  border-radius: 8px;
  border: 1.5px solid var(--border);
  font-size: 14px;
  color: var(--text);
  outline: none;
  transition: border-color 0.15s;
  background: var(--bg);
}
.field input:focus { border-color: var(--primary); background: var(--surface); }
.err-msg {
  background: #FFF0F0;
  color: var(--accent);
  border-radius: 7px;
  padding: 8px 12px;
  font-size: 13px;
  margin-bottom: 12px;
}
.submit-btn {
  width: 100%;
  padding: 12px;
  font-size: 15px;
  font-weight: 700;
  margin-top: 4px;
}
</style>
