<template>
  <div class="container">
    <div class="page-head">
      <h1 class="page-title">梗向仪表盘</h1>
      <p class="page-sub">浏览量越高，词云中越突出</p>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-num">{{ stats.total }}</div>
        <div class="stat-label">总梗帖数</div>
      </div>
      <div class="stat-card accent">
        <div class="stat-num">{{ stats.topHeat.toLocaleString() }}</div>
        <div class="stat-label">最高热度</div>
      </div>
      <div class="stat-card blue">
        <div class="stat-num">{{ stats.avgHeat }}</div>
        <div class="stat-label">平均热度</div>
      </div>
    </div>

    <div class="cloud-card">
      <h2 class="card-title">热度词云</h2>
      <div v-if="cloudLoading" class="cloud-loading">词云加载中...</div>
      <canvas v-show="!cloudLoading" ref="canvasRef" class="cloud-canvas"></canvas>
    </div>

    <div class="hot-card">
      <h2 class="card-title">热度排行榜 TOP {{ hotList.length }}</h2>
      <div class="hot-list">
        <div v-for="(p, idx) in hotList" :key="p.id" class="hot-item" @click="$router.push(`/post/${p.id}`)">
          <span class="rank" :class="rankClass(idx)">{{ idx + 1 }}</span>
          <div class="hot-info">
            <span class="hot-title">{{ p.title }}</span>
            <span class="hot-author">{{ p.authorNickname }}</span>
          </div>
          <div class="hot-heat">
            <span class="heat-icon">🔥</span>
            <span>{{ p.heatScore.toLocaleString() }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { api } from '../api/index.js'

const canvasRef = ref(null)
const cloudLoading = ref(true)
const allPosts = ref([])

const stats = computed(() => {
  const posts = allPosts.value
  if (!posts.length) return { total: 0, topHeat: 0, avgHeat: 0 }
  const heats = posts.map(p => p.heatScore)
  return {
    total: posts.length,
    topHeat: Math.max(...heats),
    avgHeat: Math.round(heats.reduce((a, b) => a + b, 0) / heats.length)
  }
})

const hotList = computed(() =>
  [...allPosts.value].sort((a, b) => b.heatScore - a.heatScore).slice(0, 15)
)

function rankClass(idx) {
  if (idx === 0) return 'gold'
  if (idx === 1) return 'silver'
  if (idx === 2) return 'bronze'
  return ''
}

async function loadAndRender() {
  try {
    const data = await api.getAllPostsForCloud()
    allPosts.value = data.records || []
    await renderWordCloud(allPosts.value)
  } catch (e) {
    console.error(e)
  } finally {
    cloudLoading.value = false
  }
}

async function renderWordCloud(posts) {
  if (!posts.length || !canvasRef.value) return
  const { default: WordCloud } = await import('wordcloud')
  const canvas = canvasRef.value
  const w = canvas.parentElement.clientWidth - 48
  canvas.width = Math.max(600, w)
  canvas.height = 420

  const list = posts.map(p => [p.title, p.heatScore])
  const colors = ['#4B63F7', '#FF6B6B', '#52C41A', '#FA8C16', '#722ED1', '#13C2C2', '#EB2F96']

  WordCloud(canvas, {
    list,
    gridSize: Math.round(canvas.width / 80),
    weightFactor: s => {
      const max = Math.max(...posts.map(p => p.heatScore))
      return Math.pow(s / max, 0.55) * (canvas.width / 14)
    },
    fontFamily: '"PingFang SC", "Microsoft YaHei", "Helvetica Neue", sans-serif',
    fontWeight: 'bold',
    color: () => colors[Math.floor(Math.random() * colors.length)],
    rotateRatio: 0.25,
    minSize: 10,
    backgroundColor: '#FFFFFF',
    shrinkToFit: true,
  })
}

onMounted(loadAndRender)
</script>

<style scoped>
.page-head { margin-bottom: 28px; }
.page-title { font-size: 28px; font-weight: 800; letter-spacing: -0.5px; }
.page-sub { font-size: 15px; color: var(--text-muted); margin-top: 6px; }

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}
.stat-card {
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  padding: 24px;
  text-align: center;
}
.stat-card.accent { border-color: #FFD4C8; background: #FFF8F5; }
.stat-card.blue { border-color: #C5D0FA; background: #F5F7FF; }
.stat-num { font-size: 32px; font-weight: 800; color: var(--primary); line-height: 1.2; }
.stat-card.accent .stat-num { color: var(--accent); }
.stat-card.blue .stat-num { color: #4B63F7; }
.stat-label { font-size: 13px; color: var(--text-muted); margin-top: 6px; }

.cloud-card, .hot-card {
  background: var(--surface);
  border-radius: var(--radius);
  border: 1.5px solid var(--border);
  padding: 28px;
  margin-bottom: 24px;
}
.card-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 20px;
}
.cloud-loading {
  text-align: center;
  color: var(--text-muted);
  padding: 60px 0;
  font-size: 15px;
}
.cloud-canvas {
  display: block;
  max-width: 100%;
  border-radius: 8px;
}

.hot-list { display: flex; flex-direction: column; gap: 0; }
.hot-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 0;
  border-bottom: 1px solid var(--border);
  cursor: pointer;
  transition: background 0.15s;
  border-radius: 6px;
  padding-left: 8px;
  padding-right: 8px;
}
.hot-item:last-child { border-bottom: none; }
.hot-item:hover { background: var(--bg); }
.rank {
  width: 28px; height: 28px;
  border-radius: 7px;
  background: var(--border);
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.rank.gold { background: #FFD700; color: #7A5800; }
.rank.silver { background: #C0C0C0; color: #444; }
.rank.bronze { background: #CD7F32; color: #fff; }
.hot-info { flex: 1; display: flex; flex-direction: column; gap: 2px; min-width: 0; }
.hot-title {
  font-size: 15px;
  font-weight: 600;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.hot-author { font-size: 12px; color: var(--text-muted); }
.hot-heat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 700;
  color: #E05C1A;
  flex-shrink: 0;
}
.heat-icon { font-size: 13px; }
</style>
