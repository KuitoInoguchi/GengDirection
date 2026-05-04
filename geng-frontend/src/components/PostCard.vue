<template>
  <div class="card" @click="$router.push(`/post/${post.id}`)">
    <div class="card-top">
      <div class="heat-badge">
        <span class="heat-icon">🔥</span>
        <span class="heat-val">{{ post.heatScore.toLocaleString() }}</span>
      </div>
    </div>
    <h3 class="title">{{ post.title }}</h3>
    <p class="excerpt">{{ post.content }}</p>
    <div class="card-foot">
      <span class="author-name">{{ post.authorNickname }}</span>
      <span class="sep">·</span>
      <span class="date">{{ fmtDate(post.createdAt) }}</span>
    </div>
  </div>
</template>

<script setup>
defineProps({ post: { type: Object, required: true } })

function fmtDate(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.card {
  background: var(--surface);
  border-radius: var(--radius);
  padding: 20px;
  cursor: pointer;
  border: 1.5px solid var(--border);
  transition: all 0.2s;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.card:hover {
  border-color: var(--primary);
  box-shadow: var(--shadow);
  transform: translateY(-2px);
}
.card-top {
  display: flex;
  align-items: center;
  gap: 8px;
}
.heat-badge {
  display: flex;
  align-items: center;
  gap: 4px;
  background: #FFF5F0;
  border-radius: 6px;
  padding: 2px 8px;
}
.heat-icon { font-size: 13px; }
.heat-val {
  font-size: 12px;
  font-weight: 700;
  color: #E05C1A;
}
.title {
  font-size: 16px;
  font-weight: 700;
  color: var(--text);
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.excerpt {
  font-size: 13px;
  color: var(--text-muted);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}
.card-foot {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 4px;
}
.author-name { font-weight: 500; }
.sep { opacity: 0.5; }
</style>
