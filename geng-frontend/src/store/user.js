import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const user = ref(JSON.parse(localStorage.getItem('gd_user') || 'null'))
  const isLoggedIn = computed(() => !!user.value)

  function login(userData) {
    user.value = userData
    localStorage.setItem('gd_user', JSON.stringify(userData))
  }

  function logout() {
    user.value = null
    localStorage.removeItem('gd_user')
  }

  return { user, isLoggedIn, login, logout }
})
