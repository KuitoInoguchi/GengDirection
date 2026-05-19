<template>
  <div class="container">
    <div class="page-head">
      <h1 class="page-title">梗向大数据仪表盘</h1>
      <p class="page-sub">数据链路：埋点采集 → HDFS 存储 → MapReduce 批处理 → ECharts 可视化</p>
    </div>

    <!-- 数据来源 / HDFS 状态条 -->
    <div class="src-bar">
      <div class="src-left">
        <span class="dot" :class="hdfsOk ? 'ok' : 'down'"></span>
        <span>数据源：HDFS + MapReduce（{{ hdfsOk ? 'HDFS 已连接' : 'HDFS 未就绪' }}）</span>
        <span class="src-tag" :class="mrReady ? 'ok' : 'wait'">
          {{ mrReady ? 'MapReduce 结果已就绪' : '尚未跑出 MapReduce 结果' }}
        </span>
      </div>
      <div class="src-right">
        <button class="btn" :disabled="busy" @click="doFlush">立即落盘到 HDFS</button>
        <button class="btn primary" :disabled="busy" @click="refreshAll">刷新分析数据</button>
      </div>
    </div>
    <p v-if="msg" class="msg">{{ msg }}</p>

    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-num">{{ stats.total }}</div>
        <div class="stat-label">总梗帖数</div>
      </div>
      <div class="stat-card accent">
        <div class="stat-num">{{ hotPosts.length }}</div>
        <div class="stat-label">热门帖子(MR Top-N)</div>
      </div>
      <div class="stat-card blue">
        <div class="stat-num">{{ totalActions.toLocaleString() }}</div>
        <div class="stat-label">已统计行为数</div>
      </div>
    </div>

    <!-- 热门帖子 Top-N：来自 HotPostJob -->
    <div class="chart-card">
      <h2 class="card-title">热门帖子 Top-N <span class="from">来自 MapReduce · HotPostJob</span></h2>
      <div v-show="hotPosts.length" ref="barRef" class="chart"></div>
      <div v-if="!hotPosts.length" class="empty">
        暂无数据。请先产生一些浏览/收藏/评论行为 → 点「立即落盘」→ 在服务器跑 MapReduce → 再「刷新」。
      </div>
    </div>

    <!-- 行为类型分布：来自 ActionDistJob -->
    <div class="chart-card">
      <h2 class="card-title">用户行为类型分布 <span class="from">来自 MapReduce · ActionDistJob</span></h2>
      <div v-show="actionDist.length" ref="pieRef" class="chart"></div>
      <div v-if="!actionDist.length" class="empty">暂无数据，跑完 MapReduce 后刷新即可。</div>
    </div>

    <!-- 保留原热度词云（来自 MySQL，作为对照） -->
    <div class="chart-card">
      <h2 class="card-title">热度词云 <span class="from">来自 MySQL 实时数据（对照）</span></h2>
      <div v-if="cloudLoading" class="empty">词云加载中...</div>
      <canvas v-show="!cloudLoading" ref="canvasRef" class="cloud-canvas"></canvas>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { api } from '../api/index.js'

const ACTION_LABEL = {
  VIEW: '浏览', FAVORITE: '收藏', COMMENT: '评论',
  CREATE_POST: '发帖', SEARCH: '搜索', TRACK: '其它'
}

const barRef = ref(null)
const pieRef = ref(null)
const canvasRef = ref(null)
const cloudLoading = ref(true)

const hdfsOk = ref(false)
const mrReady = ref(false)
const busy = ref(false)
const msg = ref('')

const hotPosts = ref([])
const actionDist = ref([])
const stats = ref({ total: 0 })
const totalActions = ref(0)

let barChart = null
let pieChart = null

async function loadStatus() {
  try {
    const s = await api.analyticsStatus()
    hdfsOk.value = !!s.hdfsAvailable
    mrReady.value = !!(s.hotpostReady || s.actionReady)
  } catch {
    hdfsOk.value = false
    mrReady.value = false
  }
}

async function loadHotPosts() {
  try {
    hotPosts.value = (await api.getHotPosts(15)) || []
  } catch { hotPosts.value = [] }
  renderBar()
}

async function loadActionDist() {
  try {
    actionDist.value = (await api.getActionDistribution()) || []
  } catch { actionDist.value = [] }
  totalActions.value = actionDist.value.reduce((a, b) => a + (b.count || 0), 0)
  renderPie()
}

function renderBar() {
  if (!barRef.value || !hotPosts.value.length) return
  barChart = barChart || echarts.init(barRef.value)
  const rows = [...hotPosts.value].sort((a, b) => a.heatScore - b.heatScore)
  barChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 140, right: 30, top: 20, bottom: 30 },
    xAxis: { type: 'value', name: '热度分' },
    yAxis: {
      type: 'category',
      data: rows.map(r => r.title.length > 12 ? r.title.slice(0, 12) + '…' : r.title)
    },
    series: [{
      type: 'bar',
      data: rows.map(r => r.heatScore),
      itemStyle: { color: '#4B63F7', borderRadius: [0, 6, 6, 0] },
      label: { show: true, position: 'right' }
    }]
  })
}

function renderPie() {
  if (!pieRef.value || !actionDist.value.length) return
  pieChart = pieChart || echarts.init(pieRef.value)
  pieChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['40%', '68%'],
      center: ['50%', '45%'],
      data: actionDist.value.map(a => ({
        name: ACTION_LABEL[a.action] || a.action,
        value: a.count
      })),
      label: { formatter: '{b}\n{d}%' }
    }]
  })
}

async function doFlush() {
  busy.value = true
  msg.value = ''
  try {
    await api.flushBehavior()
    msg.value = '已触发落盘：缓冲中的行为已写入 HDFS。接下来在服务器运行 MapReduce，再点「刷新分析数据」。'
    await loadStatus()
  } catch (e) {
    msg.value = '落盘失败：' + (e.message || '未知错误') + '（多半是 HDFS / DataNode 还没就绪）'
  } finally {
    busy.value = false
  }
}

async function refreshAll() {
  busy.value = true
  msg.value = ''
  await loadStatus()
  await Promise.all([loadHotPosts(), loadActionDist()])
  busy.value = false
}

async function renderWordCloud() {
  try {
    const data = await api.getAllPostsForCloud()
    const posts = data.records || []
    stats.value.total = posts.length
    if (posts.length && canvasRef.value) {
      const { default: WordCloud } = await import('wordcloud')
      const canvas = canvasRef.value
      canvas.width = Math.max(600, canvas.parentElement.clientWidth - 56)
      canvas.height = 380
      const max = Math.max(...posts.map(p => p.heatScore || 1))
      WordCloud(canvas, {
        list: posts.map(p => [p.title, p.heatScore || 1]),
        gridSize: Math.round(canvas.width / 80),
        weightFactor: s => Math.pow(s / max, 0.55) * (canvas.width / 14),
        fontFamily: '"PingFang SC", "Microsoft YaHei", sans-serif',
        fontWeight: 'bold',
        color: () => ['#4B63F7', '#FF6B6B', '#52C41A', '#FA8C16', '#722ED1'][Math.floor(Math.random() * 5)],
        rotateRatio: 0.25, minSize: 10, backgroundColor: '#FFFFFF', shrinkToFit: true
      })
    }
  } catch (e) {
    console.error(e)
  } finally {
    cloudLoading.value = false
  }
}

function onResize() {
  barChart && barChart.resize()
  pieChart && pieChart.resize()
}

onMounted(async () => {
  window.addEventListener('resize', onResize)
  await renderWordCloud()
  await refreshAll()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  barChart && barChart.dispose()
  pieChart && pieChart.dispose()
})
</script>

<style scoped>
.page-head { margin-bottom: 20px; }
.page-title { font-size: 28px; font-weight: 800; letter-spacing: -0.5px; }
.page-sub { font-size: 14px; color: var(--text-muted); margin-top: 6px; }

.src-bar {
  display: flex; align-items: center; justify-content: space-between;
  gap: 12px; flex-wrap: wrap;
  background: var(--surface); border: 1.5px solid var(--border);
  border-radius: var(--radius); padding: 14px 18px; margin-bottom: 12px;
}
.src-left { display: flex; align-items: center; gap: 10px; font-size: 14px; flex-wrap: wrap; }
.dot { width: 9px; height: 9px; border-radius: 50%; display: inline-block; }
.dot.ok { background: #52C41A; }
.dot.down { background: #FF6B6B; }
.src-tag { font-size: 12px; padding: 2px 10px; border-radius: 999px; }
.src-tag.ok { background: #EAF7E6; color: #2F8E1F; }
.src-tag.wait { background: #FFF3E6; color: #C77700; }
.src-right { display: flex; gap: 10px; }
.btn {
  border: 1.5px solid var(--border); background: var(--surface);
  padding: 8px 16px; border-radius: 8px; font-size: 13px; font-weight: 600;
  cursor: pointer;
}
.btn.primary { background: var(--primary); color: #fff; border-color: var(--primary); }
.btn:disabled { opacity: .55; cursor: not-allowed; }
.msg { font-size: 13px; color: var(--text-muted); margin: 0 0 16px; }

.stats-row {
  display: grid; grid-template-columns: repeat(3, 1fr);
  gap: 16px; margin-bottom: 20px;
}
.stat-card {
  background: var(--surface); border-radius: var(--radius);
  border: 1.5px solid var(--border); padding: 22px; text-align: center;
}
.stat-card.accent { border-color: #FFD4C8; background: #FFF8F5; }
.stat-card.blue { border-color: #C5D0FA; background: #F5F7FF; }
.stat-num { font-size: 30px; font-weight: 800; color: var(--primary); }
.stat-card.accent .stat-num { color: var(--accent); }
.stat-card.blue .stat-num { color: #4B63F7; }
.stat-label { font-size: 13px; color: var(--text-muted); margin-top: 6px; }

.chart-card {
  background: var(--surface); border-radius: var(--radius);
  border: 1.5px solid var(--border); padding: 24px; margin-bottom: 20px;
}
.card-title { font-size: 18px; font-weight: 700; margin-bottom: 16px; }
.card-title .from { font-size: 12px; font-weight: 500; color: var(--text-muted); margin-left: 8px; }
.chart { width: 100%; height: 420px; }
.empty { color: var(--text-muted); padding: 48px 0; text-align: center; font-size: 14px; }
.cloud-canvas { display: block; max-width: 100%; border-radius: 8px; }
</style>
