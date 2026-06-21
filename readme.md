# 爬虫与正则学习网站 - 开发文档

## 1. 项目概述

本项目是一个面向爬虫初学者和正则表达式学习者的**全栈学习平台**，主要功能包括：

- **词汇学习**：分类学习爬虫相关英文词汇，支持翻转卡片、发音朗读、标记掌握进度。
- **正则工具**：提供常用正则表达式示例，支持实时测试匹配，并将存储的 `\d` 等元字符正确显示为 `\d` 而非 `\\d`。
- **进度持久化**：用户学习进度存储于数据库（SQLite），支持跨设备同步（基于前端生成的 `userId`）。
- **暗黑模式**：一键切换亮色/暗色主题，用户偏好保存在浏览器本地。

技术栈采用**前后端分离**架构，后端提供 REST API，前端独立部署。

## 2. 技术栈

| 模块 | 技术 | 版本/说明 |
|------|------|------------|
| 后端框架 | Spring Boot | 2.7.18 |
| 数据库 | SQLite | 3.36.0.3（通过 JPA 自动建表）|
| ORM | Spring Data JPA | - |
| 前端框架 | Vue 3 | Composition API + TypeScript |
| 状态管理 | Pinia | - |
| 路由 | Vue Router | 4.x (hash 模式) |
| 构建工具 | Vite | 5.x |
| 样式 | Tailwind CSS | 3.4.0 + 自定义暗色变量 |
| HTTP 客户端 | Axios | - |
| 部署 | Railway（后端） + GitHub Pages（前端） | - |

## 3. 项目结构

```
spider-regex-learning/
├── backend/                      # Spring Boot 后端
│   ├── pom.xml
│   ├── src/main/
│   │   ├── java/com/learning/
│   │   │   ├── LearningApplication.java
│   │   │   ├── config/CorsFilter.java
│   │   │   ├── controller/LearningController.java
│   │   │   ├── service/LearningService.java
│   │   │   ├── repository/       # JPA Repository 接口
│   │   │   ├── entity/           # 实体类（WordCategory, Word, RegexExample, UserWordProgress）
│   │   │   └── DataInitializer.java
│   │   └── resources/application.yml
│   └── learning.db               # SQLite 数据库文件（运行后生成）
├── frontend/                     # Vue 3 前端
│   ├── package.json
│   ├── vite.config.ts
│   ├── tailwind.config.js
│   ├── index.html
│   ├── src/
│   │   ├── main.ts
│   │   ├── App.vue
│   │   ├── index.css             # 全局样式（含暗色主题）
│   │   ├── router/index.ts
│   │   ├── stores/learning.ts    # Pinia 状态管理
│   │   ├── pages/                # Home, WordsLearning, RegexTools
│   │   ├── components/           # 公共组件（NavBar, WordCard, RegexCard, ProgressBar）
│   │   └── types/index.ts        # TypeScript 类型定义
│   └── .gitignore
├── .github/workflows/deploy-frontend.yml   # GitHub Actions 自动部署前端
├── data.json                     # 示例数据文件（需手动导入）
└── README.md
```

## 4. 本地开发环境搭建

### 4.1 前置条件

- JDK 11 或更高版本
- Maven 3.6+
- Node.js 18+ 和 npm

### 4.2 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端将启动在 `http://localhost:8080`，数据库文件 `learning.db` 会自动生成在 `backend/` 目录下。

### 4.3 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务器运行在 `http://localhost:5173`，API 请求会被 Vite 代理到 `http://localhost:8080`。

### 4.4 导入初始数据

首次启动后数据库为空，需通过 JSON 文件导入数据：

```bash
curl -X POST http://localhost:8080/api/import-data -F "file=@./data.json"
```

`data.json` 的格式参见第 6 节。

## 5. 数据库设计

### 5.1 表结构

```sql
-- 单词分类表
CREATE TABLE word_categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    icon TEXT
);

-- 单词表
CREATE TABLE words (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word TEXT NOT NULL,
    translation TEXT NOT NULL,
    pronunciation TEXT,
    part_of_speech TEXT,
    example TEXT,
    category_id INTEGER NOT NULL,
    difficulty TEXT,
    FOREIGN KEY (category_id) REFERENCES word_categories(id) ON DELETE CASCADE
);

-- 正则示例表
CREATE TABLE regex_examples (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    pattern TEXT NOT NULL,
    description TEXT,
    example_text TEXT
);

-- 用户学习进度表（复合主键）
CREATE TABLE user_word_progress (
    user_id TEXT NOT NULL,
    word_id INTEGER NOT NULL,
    mastered BOOLEAN DEFAULT 0,
    PRIMARY KEY (user_id, word_id),
    FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
);
```

### 5.2 JPA 实体映射

- `WordCategory` 对应 `word_categories`，一对多关联 `Word`（使用 `@JsonIgnore` 避免循环引用）。
- `Word` 对应 `words`，多对一关联 `WordCategory`。
- `RegexExample` 对应 `regex_examples`。
- `UserWordProgress` 使用复合主键 `UserWordProgressId`。

## 6. API 接口文档

基础路径：`/api`（本地开发为 `http://localhost:8080/api`）

| 方法 | 路径 | 说明 | 请求参数/体 |
|------|------|------|-------------|
| GET | `/categories` | 获取所有单词分类 | - |
| GET | `/words` | 获取指定分类下的单词 | `categoryId` (query) |
| GET | `/progress` | 获取某用户的所有单词进度 | `userId` (query) |
| POST | `/progress` | 更新某个单词的掌握状态 | `{"userId":"xxx","wordId":1,"mastered":true}` |
| GET | `/regex` | 获取所有正则表达式示例 | - |
| GET | `/category-stats` | 获取每个分类的总单词数和用户已掌握数 | `userId` (query) |
| POST | `/import-data` | 导入 JSON 数据（分类+单词+正则） | `file` (multipart/form-data) |

### 6.1 数据导入 JSON 格式

```json
{
  "categories": [
    {
      "name": "分类名",
      "description": "描述",
      "icon": "🕷️",
      "words": [
        {
          "word": "Crawler",
          "translation": "爬虫程序",
          "pronunciation": "/ˈkrɔːlər/",
          "partOfSpeech": "名词",
          "example": "The crawler extracts data from websites.",
          "difficulty": "beginner"
        }
      ]
    }
  ],
  "regexExamples": [
    {
      "name": "手机号",
      "pattern": "^(13[0-9]|14[5|7]|15[0-9]|18[0-9])\\d{8}$",
      "description": "中国大陆手机号",
      "exampleText": "13812345678"
    }
  ]
}
```

## 7. 前端核心功能实现

### 7.1 暗黑模式切换

在 `App.vue` 中监听主题切换，将 `.dark` 类添加到 `<html>` 元素，配合 Tailwind 的 `dark:` 变体实现样式自动切换。用户偏好存入 `localStorage`。

关键代码片段：

```ts
const applyTheme = () => {
  if (isDark.value) {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
}
```

### 7.2 正则表达式显示优化

后端存储的 `pattern` 字段因 JSON 转义包含 `\\d`，前端展示时需解码为 `\d`。在 `RegexCard.vue` 中使用计算属性：

```ts
const displayPattern = computed(() => {
  return props.regex.pattern.replace(/\\\\/g, '\\')
})
```

### 7.3 单词学习逻辑

- 左侧分类列表使用 `getStatsByCategoryId` 从 store 中获取每个分类的进度（无需点击即显示）。
- 点击分类后，调用 `fetchWords(categoryId)` 获取单词列表，并支持翻转卡片、发音、标记掌握。
- 标记掌握时自动更新 `categoryStats` 中对应分类的 mastered 计数。

## 8. 部署指南

### 8.1 后端部署到 Railway

1. 将代码推送到 GitHub 仓库（含 `backend/` 目录）。
2. 登录 Railway → New Project → Deploy from GitHub repo。
3. 选择仓库，设置 **Root Directory** 为 `backend`。
4. Railway 自动识别 Spring Boot 并构建启动。
5. 应用运行后，获得一个 `.up.railway.app` 域名。

> 注意：Railway 会持久化工作目录中的 `learning.db` 文件，数据不会丢失。

### 8.2 前端部署到 GitHub Pages（自动 CI/CD）

项目已配置 `.github/workflows/deploy-frontend.yml`，推送到 `main` 分支时会自动构建前端并部署到 `gh-pages` 分支。

**前置步骤**：

1. 在 `frontend/vite.config.ts` 中修改 `base` 为你的仓库名（如 `/spider-regex-learning/`）。
2. 在 GitHub 仓库的 Settings → Pages 中，将 Source 设为 `gh-pages` 分支。
3. 在 GitHub Actions 工作流中（或通过 GitHub Secrets）设置环境变量 `VITE_API_BASE_URL`，值为 Railway 后端域名（例如 `https://xxx.up.railway.app/api`）。
4. 推送代码，Actions 自动运行，前端将部署到 `https://你的用户名.github.io/仓库名/`。

### 8.3 数据导入到生产环境

部署完成后，通过 Railway 提供的 Shell 终端导入数据：

1. 在 Railway 项目页面，点击后端服务 → **Shell** 选项卡。
2. 下载或上传 `data.json`（例如 `curl -O https://raw.githubusercontent.com/.../data.json`）。
3. 执行导入命令：
   ```bash
   curl -X POST http://localhost:8080/api/import-data -F "file=@./data.json"
   ```

## 9. 常见问题

### Q1: 后端启动时提示 `No suitable driver found for jdbc:sqlite`

- 检查 `pom.xml` 中是否包含 `sqlite-jdbc` 依赖。
- 确认 `application.yml` 中的驱动类名为 `org.sqlite.JDBC`。

### Q2: 前端请求 API 返回 CORS 错误

- 后端已配置 `CorsFilter`，允许所有来源。如果仍出现，检查后端是否正常运行，或前端代理配置是否正确。

### Q3: 正则表达式在卡片中显示为 `\\d`

- 这是 JSON 转义导致的正常现象，前端已通过 `displayPattern` 计算属性修复。如果未修复，请更新 `RegexCard.vue` 中的解码逻辑。

### Q4: 暗黑模式切换后部分组件颜色未变化

- 确保组件中使用了 Tailwind 的 `dark:` 变体类，例如 `bg-white dark:bg-gray-800`。
- 确认 `tailwind.config.js` 中 `darkMode` 设置为 `'class'`。

### Q5: Railway 上导入数据后，重启应用数据丢失

- Railway 会持久化应用工作目录中的文件（包括 `learning.db`），但如果你在部署设置中启用了“每次部署清空环境”，则会导致数据丢失。建议在 Railway 仪表盘中将数据库文件所在目录标记为持久化卷（默认已持久化）。

## 10. 贡献与维护

- 项目采用 MIT 许可证。
- 欢迎提交 Issue 和 Pull Request。
- 如需扩展功能（如用户登录、多语言支持），可参考现有架构进行二次开发。

---
supabase postgre password JRrPx0YqNOh5XvZg
WWuZ0vlfyrN4fRUR

**文档版本**：1.0  
**最后更新**：2026-06-13