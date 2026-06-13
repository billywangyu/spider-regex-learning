<template>
  <div>
    <NavBar />
    <router-view />
    <button
      @click="toggleTheme"
      class="fixed bottom-6 right-6 z-50 p-3 rounded-full shadow-lg bg-white/80 dark:bg-gray-800 backdrop-blur border border-gray-200 dark:border-gray-700 transition-all hover:scale-110"
      :title="isDark ? '切换亮色模式' : '切换暗黑模式'"
    >
      <span class="text-xl">{{ isDark ? '☀️' : '🌙' }}</span>
    </button>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import NavBar from '@/components/NavBar.vue'
const isDark = ref(false)
const initTheme = () => {
  const saved = localStorage.getItem('theme')
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
  isDark.value = saved === 'dark' || (saved === null && prefersDark)
  applyTheme()
}
const applyTheme = () => {
  if (isDark.value) {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
}
const toggleTheme = () => {
  isDark.value = !isDark.value
  applyTheme()
}
onMounted(initTheme)
</script>
