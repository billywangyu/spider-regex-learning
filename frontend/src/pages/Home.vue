<template>
  <div class="container mx-auto px-4 py-12">
    <div class="text-center mb-12">
      <h1 class="text-5xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">爬虫与正则学习站</h1>
      <p class="text-lg text-muted-foreground mt-3">掌握网络爬虫核心词汇 · 精通正则表达式</p>
    </div>
    <div class="grid md:grid-cols-2 gap-8">
      <router-link to="/words" class="group block bg-card rounded-2xl shadow-md hover:shadow-xl transition-all p-8 border border-border">
        <div class="text-6xl mb-4">🕷️</div>
        <h2 class="text-2xl font-bold mb-2 group-hover:text-primary">爬虫单词学习</h2>
        <p class="text-muted-foreground">分类学习爬虫相关英文词汇，支持发音、标记掌握进度</p>
        <div class="mt-6 text-primary font-semibold">立即学习 →</div>
      </router-link>
      <router-link to="/regex" class="group block bg-card rounded-2xl shadow-md hover:shadow-xl transition-all p-8 border border-border">
        <div class="text-6xl mb-4">🔧</div>
        <h2 class="text-2xl font-bold mb-2 group-hover:text-accent">正则表达式工具</h2>
        <p class="text-muted-foreground">常用正则示例，实时校验测试，附详细说明</p>
        <div class="mt-6 text-accent font-semibold">开始使用 →</div>
      </router-link>
    </div>
  </div>
</template>
