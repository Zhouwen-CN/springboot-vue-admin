# Springboot-Vue-Admin

## 项目亮点

1. 前后端纯手搓，不是二开
2. 使用的技术、版本都是比较新的
3. 足够精简，没有多余的代码和功能，代码注释较全
4. 有数据库文件，前后端一篮子解决
5. maven会将前端打包好的文件放入springboot，同时具备 `部署方便` 和 `单页面应用` 优势（当然也可以分别部署）

## 设计原则

更注重代码的质量和程序的健壮性，轻样式设计

主要是因为后台管理项目不需要那么好看的样式，还有就是本人更偏后端🐶

## 目前已实现

权限管理

- 用户管理
- 角色管理
- 菜单管理（动态权限菜单、菜单缓存）

开发者工具

- 接口文档（swagger）
- 字典管理

日志管理

- 登入日志
- 操作日志
- 异常日志

## 版本信息

| 依赖                  | 版本     |
|---------------------|--------|
| JDK                 | 17+    |
| MySQL               | 8+     |
| Springboot          | 3.2.12 |
| Mybatis-Plus        | 3.5.7  |
| Springboot-Security | 3.2.12 |
| Java-Jwt            | 4.4.0  |
| Node.js             | 18+    |
| pnpm                | 9+     |
| Vue                 | 3.4.21 |
| Element-Plus        | 2.7.2  |
| Pinia               | 2.1.7  |
| Axios               | 1.6.8  |

## tips

运行打包需要本地拥有 pnpm 环境，否则前端无法打包（个人比较喜欢用 pnpm）

idea打开最好右键排除掉一些不需要索引的文件夹

- admin-backend/src/main/resources/static
- admin-frontend/dist

## 项目截图

![登入](./images/1登入.png)

![首页](./images/2首页.png)

![菜单管理](./images/3菜单管理.png)

![接口文档](./images/4接口文档.png)

![字典管理](./images/5字典管理.png)

![日志管理](./images/6日志管理.png)
