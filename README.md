# BookSystem 图书管理系统

## 项目简介

BookSystem 是一个基于 Java Swing 的现代化图书管理系统，支持图书的搜索、借阅、归还、用户管理等功能，界面美观，交互友好，适合高校、图书馆等场景使用。

---

## 系统架构说明

本系统采用典型的三层架构设计，结构清晰，易于维护和扩展：

1. **表示层（GUI）**  
   - 基于 Java Swing 实现，负责与用户的交互，包含自定义UI组件和多种视图界面。
   - 主要目录：`src/gui/`  
     - `components/`：自定义UI组件（如按钮、输入框、对话框等）
     - `views/`：各功能页面（如图书管理、借阅、用户管理等）

2. **业务逻辑层（Controller）**  
   - 负责处理具体的业务逻辑，如图书借阅、归还、用户验证等。
   - 主要目录：`src/sql/`  
     - 例如：`Book.java`、`BorrowController.java`、`UserController.java` 等

3. **数据访问层（DAO）**  
   - 负责与数据库的交互，包括数据的增删改查。
   - 主要目录：`src/sql/`  
     - 例如：`MySQLConnection.java`、`ConnectionPool.java`、`MySQLCMD.java` 等

4. **工具与资源层**  
   - 工具类：`src/utils/`，如常量定义、通用方法等
   - 资源文件：`resource/`，如图片、图标等

---

## 功能模块说明

### 1. 图书管理模块
- 图书的添加、删除、修改
- 图书信息展示（卡片式/列表式）
- 图书库存管理（借出/归还自动更新库存）

### 2. 图书搜索模块
- 支持书名、作者、ISBN 的模糊与实时搜索
- 搜索结果高亮与状态保持

### 3. 用户管理模块
- 用户注册、登录、信息维护
- 普通用户与管理员权限分离
- 用户借阅历史查询

### 4. 借阅管理模块
- 借阅操作（防止重复借阅、借阅上限控制）
- 归还操作（自动更新库存与借阅记录）
- 借阅记录统计与展示

### 5. 系统管理与辅助模块
- 登录状态管理
- 统一风格UI与响应式设计
- 友好的错误与空状态提示
- 数据库连接池管理，提升访问效率

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

---

## 数据库结构说明

本系统数据库主要包含以下表：

### 1. book（图书表）
| 字段名    | 类型         | 说明         |
|-----------|--------------|--------------|
| id        | int          | 主键，自增   |
| isbn      | varchar(32)  | 图书ISBN     |
| title     | varchar(255) | 书名         |
| author    | varchar(255) | 作者         |
| numLeft   | int          | 剩余可借数量 |
| numAll    | int          | 总藏书数量   |
| addTime   | date         | 入库时间     |

### 2. user（用户表）
| 字段名       | 类型    | 说明           |
|--------------|---------|----------------|
| id           | int     | 主键，自增     |
| username     | text    | 用户名         |
| password     | text    | 密码           |
| registerTime | date    | 注册时间       |
| comment      | text    | 备注           |

### 3. borrow_record（借阅记录表）
| 字段名      | 类型 | 说明             |
|-------------|------|------------------|
| id          | int  | 主键，自增       |
| book_id     | int  | 图书ID，外键     |
| user_id     | int  | 用户ID，外键     |
| borrow_time | date | 借阅时间         |
| return_time | date | 归还时间（可空） |

### 4. 视图 v_book_borrow
用于联合查询图书与借阅信息，便于统计和展示。

#### 主要表关系
- `borrow_record.book_id` 外键关联 `book.id`
- `borrow_record.user_id` 外键关联 `user.id`
- 通过视图 `v_book_borrow` 可方便获取每本书的借阅情况及借阅人信息

### 数据初始化
- `database/database.sql` 文件已包含部分图书、用户和借阅记录的初始化数据，可直接导入。

### 编译与启动
```bash
javac -cp "src;src/lib/*" src/Main.java
java -cp "src;src/lib/*" Main
```

---

## 目录结构（详细说明）

```
bookSystem/
├── src/                  # 源码目录
│   ├── gui/              # 图形界面相关代码
│   │   ├── components/   # 自定义UI组件（如按钮、输入框、对话框等）
│   │   └── views/        # 各功能页面视图（如图书管理、借阅、用户管理等）
│   ├── sql/              # 数据库操作与业务逻辑（如图书、用户、借阅控制器等）
│   ├── utils/            # 工具类（如常量定义、通用方法等）
│   └── lib/              # 第三方库（如MySQL驱动）
│   └── Main.java         # 程序主入口
├── database/             # 数据库脚本
│   └── database.sql      # 数据库结构及初始化数据
├── resource/             # 静态资源文件
│   ├── icon/             # 图标资源
│   ├── img/              # 图片资源
│   └── META-INF/         # Java元数据（如MANIFEST.MF）
├── README.md             # 项目说明文档
└── bookSystem.iml        # IDEA项目文件
```

### 详细说明

- **src/**  
  源代码主目录，包含所有Java源文件和依赖库。
  - **gui/**  
    图形界面相关代码，负责用户交互和界面展示。
    - **components/**  
      存放自定义UI组件，如按钮（iButton）、输入框（iField）、对话框（iDialog）、标签（iLabel）等，便于界面复用和统一风格。
    - **views/**  
      各功能页面的实现，如用户管理（UserView）、图书借阅（BorrowView）、图书管理（BookChangeView）等。
    - 其他如Sys.java、actions.java等为界面控制和全局事件处理类。
  - **sql/**  
    负责与数据库的交互和业务逻辑处理。包括：
    - 数据库连接与配置（MySQLConnection、ConnectionPool、MySQLConfig等）
    - 业务控制器（Book、UserController、BorrowController等）
    - 数据模型（UserItem、BorrowRecord等）
  - **utils/**  
    工具类目录，包含常量定义（Constants.java）、通用方法（u.java）等，提升代码复用性。
  - **lib/**  
    存放第三方依赖库，如MySQL JDBC驱动（mysql-connector-j-8.0.33.jar）。
  - **Main.java**  
    程序主入口，负责启动整个应用。
- **database/**  
  数据库相关文件，主要为数据库结构和初始化数据脚本（database.sql）。
- **resource/**  
  静态资源文件目录。
  - **icon/**  
    存放系统所用的图标文件（如add.png、search.png等）。
  - **img/**  
    存放界面图片（如404页面、封面等）。
  - **META-INF/**  
    Java应用的元数据文件（如MANIFEST.MF）。
- **README.md**  
  项目说明文档，包含系统介绍、安装使用方法、架构与模块说明等。
- **bookSystem.iml**  
  IntelliJ IDEA项目配置文件。

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
- 数据库需包含项目所需的所有表结构和初始数据，建议直接通过 `database/database.sql` 脚本进行初始化，避免缺表或字段缺失导致运行异常

---

## 贡献方式
欢迎提交 Issue 或 Pull Request 参与改进！

---

## 许可证
MIT License 