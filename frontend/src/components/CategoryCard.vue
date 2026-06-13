<template>
  <div class="bg-card rounded-xl p-4 shadow-sm border hover:shadow-md cursor-pointer transition-all" @click="$emit('click')">
    <div class="flex items-center gap-3"><span class="text-3xl">{{ category.icon }}</span><div><h3 class="font-bold">{{ category.name }}</h3><p class="text-xs text-muted-foreground">{{ category.description }}</p></div></div>
    <div class="mt-3"><ProgressBar :value="progress.mastered" :max="progress.total" /><div class="flex justify-between text-xs mt-1"><span>进度</span><span>{{ progress.mastered }}/{{ progress.total }}</span></div></div>
  </div>
</template>
<script setup lang="ts">
import ProgressBar from './ProgressBar.vue'
defineProps<{ category: any; progress: { total: number; mastered: number } }>()
defineEmits(['click'])
</script>
