<template>
  <div class="container mx-auto px-4 py-8">
    <div class="text-center mb-8">
      <h1 class="text-3xl font-bold">正则表达式测试工具</h1>
      <p class="text-muted-foreground">学习常用正则，实时测试匹配结果</p>
    </div>
    <div v-if="store.loading" class="text-center py-12">加载中...</div>
    <div v-else class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <div class="space-y-6">
        <div v-for="regex in store.regexExamples" :key="regex.id">
          <RegexCard :regex="regex" @test="setPattern" />
        </div>
      </div>
      <div class="sticky top-24">
        <div class="bg-card rounded-2xl shadow-md p-6 border border-border">
          <h3 class="text-xl font-semibold mb-4">实时测试</h3>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">正则表达式</label>
            <input v-model="displayPattern" type="text" class="w-full px-4 py-2 border border-border rounded-lg font-mono text-sm" placeholder="输入正则，如 \d+" @input="onPatternInput" />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">测试字符串</label>
            <textarea v-model="testString" rows="6" class="w-full px-4 py-2 border border-border rounded-lg font-mono text-sm" @input="runTest" placeholder="输入要测试的文本..."></textarea>
          </div>
          <button @click="runTest" class="w-full bg-primary text-white py-2 rounded-lg font-semibold hover:bg-primary/90">执行匹配</button>
          <div class="mt-4 p-3 bg-muted/30 rounded-lg">
            <p class="text-sm font-medium mb-2">匹配结果：</p>
            <pre class="text-sm font-mono whitespace-pre-wrap">{{ matchResult }}</pre>
          </div>
          <div v-if="errorMessage" class="mt-2 text-red-500 text-sm">{{ errorMessage }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useLearningStore } from '@/stores/learning'
import RegexCard from '@/components/RegexCard.vue'
const store = useLearningStore()
const displayPattern = ref('')
const rawPattern = ref('')
const testString = ref('')
const matchResult = ref('')
const errorMessage = ref('')
function decodePattern(encoded: string): string {
  if (!encoded) return ''
  return encoded.replace(/\\\\/g, '\\')
}
function encodePattern(decoded: string): string {
  if (!decoded) return ''
  return decoded.replace(/\\/g, '\\\\')
}
function setPattern(pattern: string, exampleText: string) {
  rawPattern.value = pattern
  displayPattern.value = decodePattern(pattern)
  testString.value = exampleText || ''
  runTest()
}
function onPatternInput() {
  rawPattern.value = encodePattern(displayPattern.value)
  runTest()
}
function runTest() {
  errorMessage.value = ''
  if (!displayPattern.value.trim()) {
    matchResult.value = '请输入正则表达式'
    return
  }
  try {
    const regex = new RegExp(displayPattern.value, 'g')
    const matches = testString.value.match(regex)
    matchResult.value = matches && matches.length > 0 ? matches.join('\n') : '无匹配'
  } catch (e: any) {
    errorMessage.value = e.message
    matchResult.value = '正则表达式无效'
  }
}
watch(testString, () => { if (displayPattern.value.trim()) runTest() })
onMounted(async () => { await store.fetchRegex() })
</script>
