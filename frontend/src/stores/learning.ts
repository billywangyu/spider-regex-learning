import { defineStore } from 'pinia'
import axios from 'axios'
const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
const api = axios.create({ baseURL: API_BASE, timeout: 10000 })
export const useLearningStore = defineStore('learning', {
  state: () => ({
    categories: [] as any[],
    words: [] as any[],
    regexExamples: [] as any[],
    userProgress: new Map(),
    categoryStats: [] as any[],
    userId: '',
    loading: false
  }),
  getters: {
    getStatsByCategoryId: (state) => (categoryId: number) => {
      const stat = state.categoryStats.find(s => s.categoryId === categoryId)
      return stat ? { total: stat.total, mastered: stat.mastered } : { total: 0, mastered: 0 }
    }
  },
  actions: {
    initUserId() {
      let id = localStorage.getItem('learning_user_id')
      if (!id) { id = 'user_' + Math.random().toString(36).slice(2); localStorage.setItem('learning_user_id', id) }
      this.userId = id
    },
    async fetchCategories() { const res = await api.get('/categories'); this.categories = res.data },
    async fetchWords(categoryId: number) { const res = await api.get('/words', { params: { categoryId } }); this.words = res.data },
    async fetchUserProgress() { const res = await api.get('/progress', { params: { userId: this.userId } }); this.userProgress.clear(); res.data.forEach((p: any) => { this.userProgress.set(p.wordId, p.mastered) }) },
    async fetchCategoryStats() { const res = await api.get('/category-stats', { params: { userId: this.userId } }); this.categoryStats = res.data },
    async toggleWord(wordId: number) {
      const current = this.userProgress.get(wordId) || false
      const newMastered = !current
      await api.post('/progress', { userId: this.userId, wordId, mastered: newMastered })
      this.userProgress.set(wordId, newMastered)
      const word = this.words.find(w => w.id === wordId)
      if (word) {
        const stat = this.categoryStats.find(s => s.categoryId === word.category.id)
        if (stat) { const delta = newMastered ? 1 : -1; stat.mastered = Math.max(0, stat.mastered + delta) }
      }
    },
    async fetchRegex() { const res = await api.get('/regex'); this.regexExamples = res.data },
    async loadAll() { await Promise.all([this.fetchCategories(), this.fetchUserProgress(), this.fetchRegex(), this.fetchCategoryStats()]) }
  }
})
