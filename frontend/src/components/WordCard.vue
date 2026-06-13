<template>
  <div class="bg-card dark:bg-gray-800 rounded-xl shadow-sm border border-border dark:border-gray-700 p-5 hover:shadow-md transition-all cursor-pointer" @click="flipped = !flipped">
    <div v-if="!flipped">
      <div class="flex justify-between items-start">
        <div><span class="text-xs bg-primary/10 text-primary px-2 py-1 rounded-full">{{ word.partOfSpeech }}</span><span v-if="mastered" class="ml-2 text-xs text-green-600 dark:text-green-400">✓ 已掌握</span></div>
        <button @click.stop="playAudio" class="text-primary">🔊</button>
      </div>
      <h3 class="text-2xl font-bold mt-3 dark:text-white">{{ word.word }}</h3>
      <p class="text-sm text-muted-foreground dark:text-gray-400">{{ word.pronunciation }}</p>
      <p class="text-xs text-center text-muted-foreground dark:text-gray-400 mt-4">点击翻转查看释义</p>
    </div>
    <div v-else>
      <h3 class="text-xl font-semibold mb-2 dark:text-white">{{ word.translation }}</h3>
      <p class="text-sm text-muted-foreground dark:text-gray-300 mb-3">{{ word.example }}</p>
      <button @click.stop="$emit('toggle')" :class="['w-full py-2 rounded-lg font-medium', mastered ? 'bg-green-500 text-white' : 'bg-primary text-white hover:bg-primary/90']">{{ mastered ? '已掌握 ✓' : '标记为已掌握' }}</button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const props = defineProps<{ word: any; mastered: boolean }>()
defineEmits(['toggle'])
const flipped = ref(false)
const playAudio = () => { const u = new SpeechSynthesisUtterance(props.word.word); u.lang = 'en-US'; u.rate = 0.8; speechSynthesis.speak(u) }
</script>
