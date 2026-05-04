<template>
  <div class="container profile-wrap">
    <div v-if="userInfo" class="profile-card">
      <div class="avatar-big">{{ userInfo.nickname[0] }}</div>
      <div class="profile-info">
        <h2 class="profile-nick">{{ userInfo.nickname }}</h2>
        <span class="profile-username">@{{ userInfo.username }}</span>
        <span class="profile-date">加入于 {{ fmtDate(userInfo.createdAt) }}</span>
      </div>
    </div>

    <div class="section-title">我的收藏</div>

    <div v-if="loadingFav" class="loading-box">加载中...</div>
    <div v-else-if="favorites.length" class="fav-list">
      <div
        v-for="f in favorites"
        :key="f.postId"
        class="fav-item"
        @click="$router.push(`/post/${f.postId}`)"
      >
        <div class="fav-info">
          <span class="fav-title">{{ f.title }}</span>
          <span class="fav-meta">{{ f.authorNickname }} · {{ fmtDate(f.postCreatedAt) }}</span>
        </div>
        <div class="fav-heat">🔥 {{ f.heatScore }}</div>
      </div>
    </div>
    <div v-else class="empty-tip">还没有收藏任何梗帖~</div>

    <div class="pagination" v-if="favTotal > favPageSize">
      <button class="page-btn" :disabled="favPage <= 1" @click="changeFavPage(favPage - 1)">上一页</button>
      <span class="page-info">{{ favPage }} / {{ favTotalPages }}</span>
      <button class="page-btn" :disabled="favPage >= favTotalPages" @click="changeFavPage(favPage + 1)">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { api } from '../api/index.js'

const route = useRoute()
const userId = route.params.id

const userInfo = ref(null)
const favorites = ref([])
const favTotal = ref(0)
const favPage = ref(1)
const favPageSize = 20
const loadingFav = ref(false)

const favTotalPages = computed(() => Math.max(1, Math.ceil(favTotal.value / favPageSize)))

function fmtDate(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

async function loadUser() {
  try { userInfo.value = await api.getUser(userId) } catch {}
}

async function loadFavorites() {
  loadingFav.value = true
  try {
    const data = await api.getFavorites(userId, favPage.value, favPageSize)
    favorites.value = data.records
    favTotal.value = data.total
  } catch {} finally {
    loadingFav.value = false
  }
}

function changeFavPage(p) {
  favPage.value = p
  loadFavorites()
}

onMounted(() => {
  loadUser()
  loadFavorites()
})
</script>

<style scoped>
.profile-wrap { padding-bottom: 60px; }
.profile-card {
  display: flex;
  align-items: center;
  gap: 20px;
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  padding: 28px;
  margin-bottom: 28px;
}
.avatar-big {
  width: 64px; height: 64px;
  background: var(--primary);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  font-weight: 800;
  flex-shrink: 0;
}
.profile-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.profile-nick { font-size: 22px; font-weight: 800; }
.profile-username { font-size: 14px; color: var(--text-muted); }
.profile-date { font-size: 13px; color: var(--text-muted); }
.section-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 16px;
}
.loading-box { text-align: center; color: var(--text-muted); padding: 40px 0; }
.fav-list { display: flex; flex-direction: column; gap: 0; }
.fav-item {
  display: flex;
  align-items: center;
  padding: 16px 12px;
  border-bottom: 1px solid var(--border);
  cursor: pointer;
  border-radius: 8px;
  transition: background 0.15s;
  gap: 12px;
}
.fav-item:hover { background: var(--bg); }
.fav-info { flex: 1; display: flex; flex-direction: column; gap: 3px; min-width: 0; }
.fav-title {
  font-size: 15px;
  font-weight: 600;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.fav-meta { font-size: 12px; color: var(--text-muted); }
.fav-heat {
  font-size: 13px;
  font-weight: 700;
  color: #E05C1A;
  flex-shrink: 0;
}
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
