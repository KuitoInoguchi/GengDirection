import axios from 'axios'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000
})

http.interceptors.response.use(
  res => {
    const { statusCode, data, message } = res.data
    if (statusCode === 200) return data
    return Promise.reject(new Error(message || '请求失败'))
  },
  err => Promise.reject(new Error(err.response?.data?.message || err.message || '网络错误'))
)

export const api = {
  login: dto => http.post('/users/login', dto),
  register: dto => http.post('/users/register', dto),
  getUser: id => http.get(`/users/${id}`),

  getPosts: (pageNum = 1, pageSize = 12, tagId = null) => http.get('/posts', { params: { pageNum, pageSize, ...(tagId != null && { tagId }) } }),
  getAllPostsForCloud: () => http.get('/posts', { params: { pageNum: 1, pageSize: 100 } }),
  getPost: id => http.get(`/posts/${id}`),
  createPost: dto => http.post('/posts', dto),
  deletePost: id => http.delete(`/posts/${id}`),

  getTags: () => http.get('/tags'),

  getComments: (postId, pageNum = 1, pageSize = 20) =>
    http.get(`/posts/${postId}/comments`, { params: { pageNum, pageSize } }),
  createComment: (postId, dto) => http.post(`/posts/${postId}/comments`, dto),
  deleteComment: (id, userId) => http.delete(`/comments/${id}`, { params: { userId } }),

  addFavorite: (postId, userId) =>
    http.post(`/posts/${postId}/favorite`, null, { params: { userId } }),
  removeFavorite: (postId, userId) =>
    http.delete(`/posts/${postId}/favorite`, { params: { userId } }),
  getFavorites: (userId, pageNum = 1, pageSize = 20) =>
    http.get(`/users/${userId}/favorites`, { params: { pageNum, pageSize } }),

  // ===== 实验四：大数据链路 =====
  // 前端主动埋点上报（如帖子详情页浏览）
  trackBehavior: (event) => http.post('/behavior/track', event),
  // 立即把缓冲落盘到 HDFS（演示/截图用）
  flushBehavior: () => http.post('/behavior/flush'),
  behaviorStatus: () => http.get('/behavior/status'),
  // 读取 MapReduce 分析结果
  getHotPosts: (topN = 15) => http.get('/analytics/hot-posts', { params: { topN } }),
  getActionDistribution: () => http.get('/analytics/action-distribution'),
  analyticsStatus: () => http.get('/analytics/status'),
}
