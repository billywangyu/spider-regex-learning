<template>
  <div class="container mx-auto px-4 py-8">
    <div class="flex flex-col lg:flex-row gap-8">
      <aside class="lg:w-64 shrink-0">
        <h2 class="font-bold text-lg mb-4">词汇分类</h2>
        <div class="space-y-2">
          <button v-for="cat in store.categories" :key="cat.id" @click="selectCategory(cat)" :class="['w-full text-left px-4 py-3 rounded-xl transition-all', selectedCategory?.id === cat.id ? 'bg-primary text-white shadow-md' : 'bg-card hover:bg-muted border border-border']">
            <div class="flex items-center gap-2"><span>{{ cat.icon }}</span><span>{{ cat.name }}</span></div>
            <div class="text-xs mt-1" :class="selectedCategory?.id === cat.id ? 'text-white/80' : 'text-muted-foreground'">
              进度: {{ store.getStatsByCategoryId(cat.id).mastered }}/{{ store.getStatsByCategoryId(cat.id).total }}
            </div>
          </button>
        </div>
      </aside>
      <div class="flex-1">
        <div v-if="!selectedCategory" class="text-center py-20 text-muted-foreground">请从左侧选择一个分类</div>
        <div v-else>
          <div class="mb-6"><h2 class="text-2xl font-bold">{{ selectedCategory.name }}</h2><p class="text-muted-foreground">{{ selectedCategory.description }}</p></div>
          <div v-if="store.loading" class="text-center py-12">加载中...</div>
          <div v-else-if="currentWords.length === 0" class="text-center py-12 text-muted-foreground">该分类暂无单词，请导入数据。</div>
          <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <WordCard v-for="word in currentWords" :key="word.id" :word="word" :mastered="store.userProgress.get(word.id) || false" @toggle="store.toggleWord(word.id)" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useLearningStore } from '@/stores/learning'
import WordCard from '@/components/WordCard.vue'
const store = useLearningStore()
const selectedCategory = ref<any>(null)
const currentWords = ref<any[]>([])
const selectCategory = async (cat: any) => {
  selectedCategory.value = cat
  store.loading = true
  try {
    await store.fetchWords(cat.id)
    currentWords.value = store.words.filter(w => w.category.id === cat.id)
  } catch (error) {
    console.error('加载单词失败', error)
    currentWords.value = []
  } finally {
    store.loading = false
  }
}
onMounted(async () => { if (!store.categories.length) await store.loadAll() })
</script>
