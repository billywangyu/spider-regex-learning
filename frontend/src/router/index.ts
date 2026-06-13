import { createRouter, createWebHashHistory } from 'vue-router'
import Home from '@/pages/Home.vue'
import WordsLearning from '@/pages/WordsLearning.vue'
import RegexTools from '@/pages/RegexTools.vue'
const routes = [
  { path: '/', component: Home },
  { path: '/words', component: WordsLearning },
  { path: '/regex', component: RegexTools }
]
export default createRouter({ history: createWebHashHistory(), routes })
