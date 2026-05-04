<template>
  <div class="container">
    <div class="page-head">
      <h1 class="page-title">梗帖广场</h1>
      <p class="page-sub">当代网络流行梗汇总，带你读懂年轻人的语言</p>
    </div>

    <div class="tag-bar" v-if="tags.length">
      <button
        class="tag-btn"
        :class="{ active: !activeTag }"
        @click="activeTag = null; page = 1; loadPosts()"
      >全部</button>
      <button
        v-for="t in tags"
        :key="t.id"
        class="tag-btn"
        :class="{ active: activeTag === t.id }"
        @click="activeTag = t.id; page = 1; loadPosts()"
      >{{ t.tagName }}</button>
    </div>

    <div v-if="loading" class="loading-grid">
      <div v-for="n in 12" :key="n" class="skeleton-card"></div>
    </div>

    <div v-else-if="posts.length" class="post-grid">
      <PostCard v-for="p in posts" :key="p.id" :post="p" />
    </div>

    <div v-else class="empty-tip">暂时没有梗帖，快去发第一篇吧~</div>

    <div class="pagination" v-if="total > pageSize">
      <button class="page-btn" :disabled="page <= 1" @click="changePage(page - 1)">上一页</button>
      <span class="page-info">{{ page }} / {{ totalPages }}</span>
      <button class="page-btn" :disabled="page >= totalPages" @click="changePage(page + 1)">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '../api/index.js'
import PostCard from '../components/PostCard.vue'

const posts = ref([])
const tags = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = 12
const loading = ref(false)
const activeTag = ref(null)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize)))

async function loadPosts() {
  loading.value = true
  try {
    const data = await api.getPosts(page.value, pageSize, activeTag.value)
    posts.value = data.records
    total.value = data.total
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function loadTags() {
  try { tags.value = await api.getTags() } catch {}
}

function changePage(p) {
  page.value = p
  loadPosts()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  loadPosts()
  loadTags()
})
</script>

<style scoped>
.page-head {
  margin-bottom: 28px;
}
.page-title {
  font-size: 28px;
  font-weight: 800;
  color: var(--text);
  letter-spacing: -0.5px;
}
.page-sub {
  font-size: 15px;
  color: var(--text-muted);
  margin-top: 6px;
}
.tag-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}
.tag-btn {
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  color: var(--text-muted);
  background: var(--surface);
  border: 1.5px solid var(--border);
  cursor: pointer;
  transition: all 0.15s;
}
.tag-btn:hover { border-color: var(--primary); color: var(--primary); }
.tag-btn.active { background: var(--primary); color: #fff; border-color: var(--primary); }
.post-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.skeleton-card {
  height: 180px;
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  animation: pulse 1.4s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 40px;
  padding-bottom: 40px;
}
.page-btn {
  padding: 8px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  background: var(--surface);
  color: var(--text);
  border: 1.5px solid var(--border);
  cursor: pointer;
  transition: all 0.15s;
}
.page-btn:hover:not(:disabled) { border-color: var(--primary); color: var(--primary); }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-info { font-size: 14px; color: var(--text-muted); min-width: 60px; text-align: center; }
</style>
