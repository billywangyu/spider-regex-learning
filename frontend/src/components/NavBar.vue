<template>
  <nav class="sticky top-0 z-50 bg-white/80 dark:bg-gray-800 backdrop-blur-md border-b border-border dark:border-gray-700 shadow-sm">
    <div class="container mx-auto px-4 flex items-center justify-between h-16">
      <router-link to="/" class="flex items-center gap-2"><span class="text-2xl">🕷️</span><span class="text-xl font-bold text-primary dark:text-primary">爬虫·正则</span></router-link>
      <div class="flex gap-6">
        <router-link to="/" class="text-foreground/70 dark:text-gray-300 hover:text-primary dark:hover:text-primary">首页</router-link>
        <router-link to="/words" class="text-foreground/70 dark:text-gray-300 hover:text-primary dark:hover:text-primary">单词学习</router-link>
        <router-link to="/regex" class="text-foreground/70 dark:text-gray-300 hover:text-primary dark:hover:text-primary">正则工具</router-link>
      </div>
    </div>
  </nav>
</template>
