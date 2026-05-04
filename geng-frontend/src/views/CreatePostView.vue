<template>
  <div class="container create-wrap">
    <h1 class="page-title">发布新梗帖</h1>

    <div class="form-card">
      <div class="field">
        <label>梗的名称 <span class="req">*</span></label>
        <input v-model="form.title" type="text" placeholder="例：耗子尾汁、yyds、内卷..." maxlength="120" />
        <span class="hint">{{ form.title.length }}/120</span>
      </div>

      <div class="field">
        <label>梗的释义 <span class="req">*</span></label>
        <textarea
          v-model="form.content"
          rows="8"
          placeholder="详细介绍这个梗的来源、含义和使用场景..."
        ></textarea>
      </div>

      <div class="field">
        <label>来源链接</label>
        <input v-model="form.sourceUrl" type="url" placeholder="https://..." />
      </div>

      <div v-if="errMsg" class="err-msg">{{ errMsg }}</div>
      <div v-if="successMsg" class="ok-msg">{{ successMsg }}</div>

      <div class="form-actions">
        <button class="btn-ghost" @click="$router.back()">取消</button>
        <button class="btn-primary" @click="submit" :disabled="loading">
          {{ loading ? '发布中...' : '发布梗帖' }}
        </button>
      </div>
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

const loading = ref(false)
const errMsg = ref('')
const successMsg = ref('')
const form = reactive({ title: '', content: '', sourceUrl: '' })

async function submit() {
  errMsg.value = ''
  if (!form.title.trim()) { errMsg.value = '标题不能为空'; return }
  if (!form.content.trim()) { errMsg.value = '内容不能为空'; return }
  loading.value = true
  try {
    const id = await api.createPost({
      title: form.title.trim(),
      content: form.content.trim(),
      sourceUrl: form.sourceUrl.trim() || null,
      authorId: userStore.user.id
    })
    successMsg.value = '发布成功！正在跳转...'
    setTimeout(() => router.push(`/post/${id}`), 800)
  } catch (e) {
    errMsg.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.create-wrap { max-width: 720px; padding-bottom: 60px; }
.page-title { font-size: 26px; font-weight: 800; margin-bottom: 24px; }
.form-card {
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  padding: 32px;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 20px;
}
.field label {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
}
.req { color: var(--accent); }
.field input, .field textarea {
  padding: 10px 14px;
  border-radius: 8px;
  border: 1.5px solid var(--border);
  font-size: 14px;
  color: var(--text);
  outline: none;
  transition: border-color 0.15s;
  background: var(--bg);
  resize: vertical;
  line-height: 1.6;
}
.field input:focus, .field textarea:focus {
  border-color: var(--primary);
  background: var(--surface);
}
.hint { font-size: 12px; color: var(--text-muted); align-self: flex-end; }
.err-msg {
  background: #FFF0F0;
  color: var(--accent);
  border-radius: 7px;
  padding: 10px 14px;
  font-size: 13px;
  margin-bottom: 16px;
}
.ok-msg {
  background: #F6FFED;
  color: #52C41A;
  border-radius: 7px;
  padding: 10px 14px;
  font-size: 13px;
  margin-bottom: 16px;
}
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
