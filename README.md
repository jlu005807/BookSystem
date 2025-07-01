# BookSystem 图书管理系统

## 项目简介

BookSystem 是一个基于 Java Swing 的现代化图书管理系统，支持图书的搜索、借阅、归还、用户管理等功能，界面美观，交互友好，适合高校、图书馆等场景使用。

---

## 主要特性

- 🔍 **图书搜索**：支持书名、作者、ISBN 的模糊与实时搜索，结果统计与状态保持
- 📚 **图书管理**：卡片式展示，支持添加、删除、借阅、归还，防止重复借阅，借阅上限控制
- 👤 **用户管理**：普通用户与管理员分权，登录、借阅状态实时显示
- 🖥️ **现代UI**：自定义组件，统一风格，响应式设计，空状态友好提示
- 🗃️ **数据库支持**：MySQL 持久化存储，JDBC 连接池高效管理

---

## 技术栈

- 前端：Java Swing，自定义 UI 组件
- 后端：MySQL、JDBC
- 其他：自定义工具类、连接池、资源管理

---

## 安装与运行

### 环境要求
- Java 8 及以上
- MySQL 数据库

### 数据库初始化
```sql
CREATE DATABASE rental;
source database/database.sql;
```

### 编译与启动
```bash
javac -cp "src;src/lib/*" src/Main.java
java -cp "src;src/lib/*" Main
```

---

## 目录结构

```
bookSystem/
├── src/              # 源码
│   ├── gui/          # 图形界面
│   │   ├── components/ # UI组件
│   │   └── views/    # 界面视图
│   ├── sql/          # 数据库操作
│   ├── utils/        # 工具类
│   └── test/         # 测试程序
├── database/         # 数据库脚本
├── resource/         # 图片、图标等资源
└── README.md         # 项目说明
```

---

## 使用说明

### 用户
1. 登录系统
2. 搜索图书（支持实时、模糊搜索）
3. 借阅/归还图书
4. 查看"我的借阅"记录

### 管理员
1. 管理员账号登录
2. 添加/删除图书
3. 管理所有用户借阅记录

---

## 测试与演示

```bash
# 用户界面测试
java -cp "src;src/lib/*" test.UserViewTest

# 搜索组件演示
java -cp "src;src/lib/*" test.SearchDemo
```

---

## 注意事项
- 确保 MySQL 服务已启动，数据库用户有权限
- 推荐使用 UTF-8 编码，避免中文乱码

---

## 贡献方式
欢迎提交 Issue 或 Pull Request 参与改进！

---

## 许可证
MIT License 