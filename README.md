# Springboot-Vue-Admin

## 简介

一个常见的后台管理项目，一个轮子（程序员总是乐此不疲的造轮子/dog）

这是一个前后端分离项目，但是前端也使用 maven 管理

maven 会把前端打包好的文件，放到后端项目的 static 文件夹下，使用 springboot 来做静态资源管理

这是使得项目同时具有了 `部署方便` 和 `单页面应用` 优势

初始化数据库文件放在了这里：`admin-backend\src\main\resources\init.sql`

目前已经实现用户管理，角色管理，菜单管理，动态权限菜单

## 版本信息

| 依赖                  | 版本     |
|---------------------|--------|
| JDK                 | 17+    |
| MySQL               | 8+     |
| Springboot          | 3.2.0  |
| Mybatis-Plus        | 3.5.6  |
| Springboot-Security | 3.2.0  |
| Java-Jwt            | 4.4.0  |
| Node.js             | 18+    |
| pnpm                | 9+     |
| Vue                 | 3.4.21 |
| Element-Plus        | 2.7.2  |
| Pinia               | 2.1.7  |
| Axios               | 1.6.8  |

## tip

运行打包需要本地拥有 pnpm 环境，否则前端无法打包（个人比较喜欢用 pnpm）
