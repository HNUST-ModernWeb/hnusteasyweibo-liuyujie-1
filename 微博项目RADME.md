# 微博平台 (Weibo Platform)

一个基于 **Spring Boot + Vue 3** 的全栈微博系统，支持用户注册登录、发布图文微博、评论、点赞、个人主页等核心功能。

---

## 🛠️ 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.2.5 |
| 安全认证 | Spring Security + JWT | 6.2.4 / 0.11.5 |
| 数据库 | H2 (内存模式) | 2.2.224 |
| ORM | Spring Data JPA (Hibernate) | 6.4.4 |
| 前端框架 | Vue 3 (Composition API) | 3.4+ |
| 构建工具 | Vite | 5.0+ |
| UI 组件库 | Element Plus | 2.4+ |
| 网络请求 | Axios | 1.6+ |
| 状态管理 | Pinia | 2.1+ |
| 路由 | Vue Router | 4.2+ |

---

## ✨ 功能特性

- **用户系统**：注册、登录、JWT 认证
- **发布微博**：支持文字 + 多图上传
- **微博流**：首页信息流分页展示
- **评论功能**：对微博发表评论
- **点赞功能**：点赞/取消点赞，红色爱心图标
- **个人主页**：查看用户信息及其发布的所有微博
- **权限控制**：匿名用户可浏览微博和评论，登录后可发布、点赞、评论

---

## 📁 完整项目结构

### 🔹 后端 `backend/` (共 31 个文件)
backend/
├── pom.xml
├── src/
│ └── main/
│ ├── java/
│ │ └── com/
│ │ └── weibo/
│ │ ├── WeiboApplication.java
│ │ ├── config/
│ │ │ ├── CorsConfig.java
│ │ │ ├── SecurityConfig.java
│ │ │ └── WebConfig.java
│ │ ├── controller/
│ │ │ ├── AuthController.java
│ │ │ ├── PostController.java
│ │ │ ├── CommentController.java
│ │ │ ├── LikeController.java
│ │ │ └── UploadController.java
│ │ ├── dto/
│ │ │ ├── ApiResponse.java
│ │ │ ├── CommentRequest.java
│ │ │ ├── LoginRequest.java
│ │ │ ├── PostRequest.java
│ │ │ └── RegisterRequest.java
│ │ ├── entity/
│ │ │ ├── User.java
│ │ │ ├── Post.java
│ │ │ ├── Comment.java
│ │ │ └── Like.java
│ │ ├── repository/
│ │ │ ├── UserRepository.java
│ │ │ ├── PostRepository.java
│ │ │ ├── CommentRepository.java
│ │ │ └── LikeRepository.java
│ │ ├── security/
│ │ │ ├── JwtAuthenticationFilter.java
│ │ │ ├── JwtTokenProvider.java
│ │ │ └── UserPrincipal.java
│ │ ├── service/
│ │ │ ├── AuthService.java
│ │ │ ├── PostService.java
│ │ │ ├── CommentService.java
│ │ │ ├── LikeService.java
│ │ │ └── FileStorageService.java
│ │ └── exception/
│ │ └── GlobalExceptionHandler.java
│ └── resources/
│ └── application.properties

### 🔹 前端 `frontend/` (共 20 个文件)
frontend/
├── index.html
├── vite.config.js
├── package.json
├── package-lock.json
└── src/
├── main.js
├── App.vue
├── api/
│ ├── request.js
│ ├── auth.js
│ ├── post.js
│ ├── comment.js
│ └── upload.js
├── components/
│ ├── PostItem.vue
│ ├── CommentItem.vue
│ └── NavBar.vue
├── router/
│ └── index.js
├── stores/
│ └── user.js
└── views/
├── LoginView.vue
├── RegisterView.vue
├── HomeView.vue
├── PublishView.vue
├── ProfileView.vue
└── PostDetailView.vue

---

## 🚀 如何运行

### 1. 环境准备
- **JDK 17+**（推荐 Java 17 LTS）
- **Node.js 18+**（LTS 版本）
- **VS Code**（或其他 Java IDE）

### 2. 启动后端
```bash
cd backend

# Windows (Maven Wrapper)
mvnw.cmd spring-boot:run

# 或直接在 VS Code 中打开 WeiboApplication.java，点击 ▶ Run Java

后端运行在 http://localhost:8080。
H2 控制台：http://localhost:8080/h2-console
(JDBC URL: jdbc:h2:mem:weibodb，用户名 sa，密码空)
cd frontend
npm install
npm run dev
前端运行在 http://localhost:5173，已配置 Vite 代理将 /api 请求转发到后端。

功能
注册与登录
表单验证，JWT 安全登录

首页推荐流
分页展示所有微博，显示点赞数、评论数

发布微博
支持文字 + 多图上传，发布成功后自动刷新首页

个人主页
展示用户信息及微博列表，可删除自己的微博

微博详情与评论
查看单条微博，发表评论，点赞/取消点赞（红色爱心图标）


配置说明
application.properties 中可修改：服务端口、文件上传大小、JWT 密钥及过期时间等。

H2 数据库默认使用内存模式，重启后数据清空；如需持久化，将 spring.datasource.url 改为 jdbc:h2:file:./data/weibodb 即可。

跨域已配置允许 http://localhost:5173；若部署到其他域名，请修改 SecurityConfig.java 中的 CORS 设置。


姓名/学号：_刘宇杰2405010903_________________

课程名称：_Web技术_________________

提交日期：2026-05-05