<template>
  <div class="bg-card dark:bg-gray-800 rounded-xl p-4 shadow-sm border border-border dark:border-gray-700 hover:border-primary transition-all">
    <h3 class="font-bold text-lg dark:text-white">{{ regex.name }}</h3>
    <pre class="bg-muted/30 dark:bg-gray-700 p-2 rounded text-sm font-mono my-2 overflow-x-auto dark:text-gray-200">{{ displayPattern }}</pre>
    <p class="text-sm text-muted-foreground dark:text-gray-400">{{ regex.description }}</p>
    <button @click="$emit('test', regex.pattern, regex.exampleText)" class="mt-3 text-primary text-sm hover:underline">▶ 测试此正则</button>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
const props = defineProps<{ regex: any }>()
defineEmits(['test'])
const displayPattern = computed(() => {
  if (!props.regex.pattern) return ''
  return props.regex.pattern.replace(/\\\\/g, '\\')
})
</script>
