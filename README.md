# UUSHOP

一个基于 **Spring Cloud Alibaba** 的分布式电商教学项目，采用前后端分离架构，包含后端微服务、买家端（移动端 H5）和卖家端（PC 管理后台）三部分。

---

## 目录结构

```
UUSHOP/
├── uushop_2026/                  后端（Spring Cloud 微服务）
│   ├── pom.xml                   父 POM，统一管理依赖版本
│   ├── gateway-service/          网关 :8686
│   ├── account-service/          账户服务（用户/管理员）:8084
│   ├── product-service/          商品服务 :8081
│   ├── order-service/            订单服务 :8082
│   ├── sms-service/               短信服务 :8083
│   ├── mq-service/               消息/WebSocket 服务 :8085
│   ├── common-service/           公共组件（统一返回、异常、Swagger）
│   ├── base-service/              基础服务
│   └── repository-service/       仓储/分页配置
│
├── uushop_buyer_2026/            买家端（Vue 2 + Mint UI，移动端）:8383
└── uushop_seller_2026/           卖家端（Vue 2 + Element UI，PC 端）:8282
```

---

## 技术栈

### 后端

| 分类 | 技术 |
|---|---|
| 基础框架 | Spring Boot 2.3.0、Spring Cloud Hoxton.SR3、Spring Cloud Alibaba 2.2.1 |
| JDK | 8 |
| 注册中心/配置 | Nacos（spring-cloud-alibaba） |
| 网关 | Spring Cloud Gateway（服务发现自动路由） |
| 持久层 | MyBatis-Plus、MySQL 8 |
| 缓存 | Redis |
| 消息队列 | RocketMQ |
| 服务间调用 | OpenFeign |
| 认证 | JWT（自定义 `JwtUtil`），密码 MD5 加密 |
| 实时通信 | WebSocket（mq-service） |
| API 文档 | Swagger（common-service 统一配置） |
| Excel | EasyExcel（商品导入导出，自定义单元格样式） |

### 买家端 uushop_buyer_2026

| 分类 | 技术 |
|---|---|
| 框架 | Vue 2.6 + Vue Router + Vuex |
| UI | Mint UI（饿了么移动端组件库） |
| HTTP | Axios |
| 构建 | Vue CLI 5 |
| 端口 | 8383 |

### 卖家端 uushop_seller_2026

| 分类 | 技术 |
|---|---|
| 框架 | Vue 2.6 + Vue Router + Vuex |
| UI | Element UI |
| 图表 | ECharts（柱状图、折线图、堆叠折线图） |
| HTTP | Axios |
| 构建 | Vue CLI 5 |
| 端口 | 8282 |

---

## 微服务说明

| 服务 | 端口 | 职责 |
|---|---|---|
| **gateway-service** | 8686 | 统一入口，路由转发，CORS 跨域配置，通过 Nacos 服务发现自动路由到各微服务 |
| **account-service** | 8084 | 用户/管理员注册登录、JWT 签发与校验（`/user/checkToken`、`/admin/checkToken`）、MD5 密码加密 |
| **product-service** | 8081 | 商品与分类管理，提供买家端（列表/详情）和卖家端（增删改查）两套接口，支持 Excel 批量导入导出 |
| **order-service** | 8082 | 订单创建、查询、状态流转，通过 Feign 调用 product-service 扣减库存，RocketMQ 异步处理，提供销售统计数据（ECharts） |
| **sms-service** | 8083 | 基于互亿无线短信平台发送注册/登录验证码，验证码存入 Redis（带过期时间） |
| **mq-service** | 8085 | WebSocket 服务，向前端实时推送消息（订单状态变更等） |
| **common-service** | - | 公共组件：`ResultVO` 统一返回体、全局异常处理器、Swagger 配置、正则校验工具 |
| **repository-service** | - | MyBatis-Plus 分页插件等配置 |
| **base-service** | - | 基础服务 |

### 请求流转

```
买家端 :8383 ──┐
               ├──► gateway :8686 ──► account-service / product-service / order-service / ...
卖家端 :8282 ──┘                        └──► MySQL / Redis / RocketMQ
```

前端所有请求都打到网关 `http://localhost:8686/<service-name>/...`，网关根据 Nacos 注册的服务名自动路由。

---

## 前端页面

### 买家端（移动端）

| 路由 | 页面 | 说明 |
|---|---|---|
| `/`、`/cart` | 购物车/商品列表 | 首页 |
| `/info` | 用户信息 | |
| `/order` | 订单列表 | |
| `/orderDetail` | 订单详情 | |
| `/pay` | 支付页 | |
| `/mine` | 个人中心 | |
| `/login` | 登录 | |
| `/register` | 注册 | |

路由守卫：检查 `localStorage` 中的 `access-user`，并携带 token 调用 `account-service/user/checkToken` 校验有效性，超时则跳回登录。

### 卖家端（PC 管理后台）

| 路由 | 页面 | 说明 |
|---|---|---|
| `/productManage` | 商品管理 | 商品列表、上下架 |
| `/addProduct` | 添加商品 | |
| `/editProduct` | 编辑商品 | |
| `/orderManage` | 订单管理 | 订单列表、发货 |
| `/bar` | 总销量 | ECharts 柱状图 |
| `/basicLine` | 日销量 | ECharts 折线图 |
| `/stackedLine` | 销量明细 | ECharts 堆叠折线图 |
| `/login` | 管理员登录 | |

路由守卫：检查 `localStorage` 中的 `access-admin`，调用 `account-service/admin/checkToken` 校验。

---

## 本地环境要求

- JDK 8
- Maven 3.6+
- Node.js 14+
- MySQL 8（创建数据库 `uushop`）
- Redis
- RocketMQ（NameServer 默认端口 8908）
- Nacos（注册中心，默认 8848）

## 本地配置

各服务 `src/main/resources/application.yml` 中的以下值已替换为占位符，需替换为你自己的配置：

```yaml
# account-service / order-service / product-service
spring:
  datasource:
    username: root
    password: your_mysql_password   # ← 改成你的 MySQL 密码

# sms-service
ihuyi:
  account: your_ihuyi_account       # ← 互亿无线账号（不用短信可跳过）
  password: your_ihuyi_password    # ← 互亿无线 API 密码
```

Redis、RocketMQ、Nacos 的地址默认指向 `localhost`，如有不同请一并修改。

## 启动步骤

### 1. 启动中间件

启动 MySQL、Redis、RocketMQ、Nacos。

### 2. 启动后端

在 IDEA 中打开 `uushop_2026`，依次运行各 `*Application.java`：

```bash
cd uushop_2026
mvn clean install -DskipTests
```

推荐启动顺序：`gateway-service` → `account-service` → `product-service` → `order-service` → `sms-service` → `mq-service`。

### 3. 启动买家端

```bash
cd uushop_buyer_2026
npm install
npm run serve    # http://localhost:8383
```

### 4. 启动卖家端

```bash
cd uushop_seller_2026
npm install
npm run serve    # http://localhost:8282
```

---

## Git 说明

本仓库为 monorepo，三个子项目共用一个 Git 仓库。提交前确保已安装依赖（`node_modules/` 已被 `.gitignore` 排除，无需提交）。

```bash
git add .
git commit -m "说明本次改动"
git push
```

> 本仓库已配置本地代理 `http://127.0.0.1:7897`（Clash），推送时请确保代理已开启。
