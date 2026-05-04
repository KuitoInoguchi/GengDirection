<template>
  <div class="container detail-wrap">
    <div v-if="loading" class="loading-box">加载中...</div>
    <template v-else-if="post">
      <button class="back-btn" @click="$router.back()">← 返回</button>

      <article class="post-article">
        <div class="post-meta-row">
          <div class="heat-badge">🔥 {{ post.heatScore.toLocaleString() }}</div>
          <span class="date">{{ fmtDate(post.createdAt) }}</span>
        </div>
        <h1 class="post-title">{{ post.title }}</h1>
        <div class="author-row">
          <span class="author-avatar">{{ (post.authorNickname || '?')[0] }}</span>
          <span class="author-name">{{ post.authorNickname }}</span>
        </div>
        <div class="post-body">{{ post.content }}</div>
        <a v-if="post.sourceUrl" :href="post.sourceUrl" target="_blank" class="source-link">
          查看来源 ↗
        </a>
      </article>

      <section class="comment-section">
        <h2 class="section-title">评论 <span class="count">{{ commentTotal }}</span></h2>

        <div v-if="userStore.isLoggedIn" class="comment-form">
          <textarea
            v-model="commentText"
            placeholder="说说你的看法..."
            class="comment-input"
            maxlength="500"
            rows="3"
          ></textarea>
          <div class="form-actions">
            <span class="char-count">{{ commentText.length }}/500</span>
            <button class="btn-primary" @click="submitComment" :disabled="!commentText.trim() || submitting">
              {{ submitting ? '发送中...' : '发表评论' }}
            </button>
          </div>
        </div>
        <div v-else class="login-tip">
          <RouterLink to="/auth">登录后发表评论</RouterLink>
        </div>

        <div v-if="comments.length" class="comment-list">
          <div v-for="c in comments" :key="c.id || c.createdAt" class="comment-item">
            <div class="c-avatar">{{ (c.userNickname || '?')[0] }}</div>
            <div class="c-body">
              <div class="c-header">
                <span class="c-name">{{ c.userNickname }}</span>
                <span class="c-date">{{ fmtDate(c.createdAt) }}</span>
              </div>
              <p class="c-text">{{ c.content }}</p>
            </div>
          </div>
        </div>
        <div v-else-if="!loadingComments" class="empty-tip">还没有评论，来说第一句吧~</div>

        <div class="pagination" v-if="commentTotal > commentPageSize">
          <button class="page-btn" :disabled="commentPage <= 1" @click="changeCommentPage(commentPage - 1)">上一页</button>
          <span class="page-info">{{ commentPage }} / {{ commentTotalPages }}</span>
          <button class="page-btn" :disabled="commentPage >= commentTotalPages" @click="changeCommentPage(commentPage + 1)">下一页</button>
        </div>
      </section>
    </template>
    <div v-else class="empty-tip">梗帖不存在或已被删除</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { api } from '../api/index.js'
import { useUserStore } from '../store/user.js'

const route = useRoute()
const userStore = useUserStore()

const post = ref(null)
const loading = ref(true)
const comments = ref([])
const commentTotal = ref(0)
const commentPage = ref(1)
const commentPageSize = 20
const loadingComments = ref(false)
const commentText = ref('')
const submitting = ref(false)

const commentTotalPages = computed(() => Math.max(1, Math.ceil(commentTotal.value / commentPageSize)))

function fmtDate(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

async function loadPost() {
  try {
    post.value = await api.getPost(route.params.id)
  } catch {
    post.value = null
  } finally {
    loading.value = false
  }
}

async function loadComments() {
  loadingComments.value = true
  try {
    const data = await api.getComments(route.params.id, commentPage.value, commentPageSize)
    comments.value = data.records
    commentTotal.value = data.total
  } catch {} finally {
    loadingComments.value = false
  }
}

async function submitComment() {
  if (!commentText.value.trim()) return
  submitting.value = true
  try {
    await api.createComment(route.params.id, {
      content: commentText.value.trim(),
      userId: userStore.user.id
    })
    commentText.value = ''
    commentPage.value = 1
    await loadComments()
  } catch (e) {
    alert(e.message)
  } finally {
    submitting.value = false
  }
}

function changeCommentPage(p) {
  commentPage.value = p
  loadComments()
}

onMounted(() => {
  loadPost()
  loadComments()
})
</script>

<style scoped>
.detail-wrap { padding-bottom: 60px; }
.loading-box { text-align: center; padding: 80px 0; color: var(--text-muted); }
.back-btn {
  font-size: 14px;
  color: var(--text-muted);
  padding: 6px 0;
  margin-bottom: 20px;
  cursor: pointer;
  background: none;
  border: none;
  transition: color 0.15s;
}
.back-btn:hover { color: var(--primary); }
.post-article {
  background: var(--surface);
  border-radius: var(--radius);
  padding: 32px;
  border: 1.5px solid var(--border);
  margin-bottom: 24px;
}
.post-meta-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.heat-badge {
  background: #FFF5F0;
  color: #E05C1A;
  border-radius: 6px;
  padding: 3px 10px;
  font-size: 13px;
  font-weight: 700;
}
.date { font-size: 13px; color: var(--text-muted); }
.post-title {
  font-size: 26px;
  font-weight: 800;
  line-height: 1.35;
  letter-spacing: -0.3px;
  margin-bottom: 16px;
}
.author-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1.5px solid var(--border);
}
.author-avatar {
  width: 34px; height: 34px;
  background: var(--primary-light);
  color: var(--primary);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
}
.author-name { font-size: 14px; font-weight: 600; }
.post-body {
  font-size: 16px;
  line-height: 1.85;
  color: var(--text);
  white-space: pre-wrap;
}
.source-link {
  display: inline-block;
  margin-top: 20px;
  font-size: 13px;
  color: var(--primary);
  border: 1px solid var(--primary-light);
  border-radius: 6px;
  padding: 4px 12px;
  transition: background 0.15s;
}
.source-link:hover { background: var(--primary-light); }
.comment-section { }
.section-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.count {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-muted);
}
.comment-form {
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  padding: 16px;
  margin-bottom: 16px;
}
.comment-input {
  width: 100%;
  border: none;
  outline: none;
  font-size: 14px;
  resize: none;
  color: var(--text);
  background: transparent;
  line-height: 1.6;
}
.form-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 8px;
}
.char-count { font-size: 12px; color: var(--text-muted); }
.login-tip {
  text-align: center;
  padding: 16px;
  font-size: 14px;
  color: var(--text-muted);
  margin-bottom: 16px;
}
.login-tip a { color: var(--primary); font-weight: 500; }
.comment-list { display: flex; flex-direction: column; gap: 0; }
.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid var(--border);
}
.comment-item:last-child { border-bottom: none; }
.c-avatar {
  width: 32px; height: 32px;
  background: var(--primary-light);
  color: var(--primary);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  flex-shrink: 0;
}
.c-body { flex: 1; }
.c-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}
.c-name { font-size: 13px; font-weight: 600; color: var(--text); }
.c-date { font-size: 12px; color: var(--text-muted); }
.c-text { font-size: 14px; line-height: 1.6; color: var(--text); }
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
}
.page-btn {
  padding: 7px 18px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  background: var(--surface);
  color: var(--text);
  border: 1.5px solid var(--border);
  cursor: pointer;
  transition: all 0.15s;
}
.page-btn:hover:not(:disabled) { border-color: var(--primary); color: var(--primary); }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-info { font-size: 13px; color: var(--text-muted); }
</style>
