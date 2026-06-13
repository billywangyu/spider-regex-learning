<template>
  <div class="bg-muted dark:bg-gray-700 rounded-full h-2 overflow-hidden"><div class="h-full bg-primary rounded-full transition-all duration-500" :style="{ width: percent + '%' }"></div></div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
const props = defineProps<{ value: number; max: number }>()
const percent = computed(() => props.max ? (props.value / props.max) * 100 : 0)
</script>
