# UUSHOP

一个基于 Spring Cloud Alibaba 的分布式电商学习项目，包含后端微服务、买家端（移动端）和卖家端（PC 端）三部分。

## 项目结构

```
UUSHOP/
├── uushop_2026/          后端（Spring Cloud 微服务）
│   ├── base-service          基础服务
│   ├── repository-service   仓储服务
│   ├── common-service       公共服务
│   ├── product-service       商品服务（:8081）
│   ├── order-service         订单服务（:8082）
│   ├── account-service       账户服务（:8084）
│   ├── sms-service           短信服务（:8083）
│   ├── gateway-service       网关（:8686）
│   ├── mq-service           消息队列服务（:8085）
│   └── pom.xml
│
├── uushop_buyer_2026/   买家端（Vue 2 + Mint UI，移动端）
└── uushop_seller_2026/  卖家端（Vue 2 + Element UI + ECharts，PC 端）
```

## 技术栈

- **后端**：Spring Boot 2.3.0、Spring Cloud Hoxton.SR3、Spring Cloud Alibaba 2.2.1、MyBatis-Plus、RocketMQ、Redis、MySQL 8、JDK 8
- **买家端**：Vue 2.6、Vue Router、Vuex、Mint UI、Axios
- **卖家端**：Vue 2.6、Vue Router、Vuex、Element UI、ECharts、Axios

## 本地环境要求

- JDK 8
- Maven 3.6+
- Node.js 14+
- MySQL 8（数据库名 `uushop`）
- Redis
- RocketMQ

## 本地配置

各服务的 `src/main/resources/application.yml` 中的以下值已替换为占位符，请替换为你自己的配置：

- `your_mysql_password` → 你的 MySQL 密码
- `your_ihuyi_account` / `your_ihuyi_password` → 互亿无线短信平台凭证（如不需要短信功能可忽略 sms-service）

## 启动

### 后端

```bash
cd uushop_2026
mvn clean install -DskipTests
# 依次启动各服务（建议配合 Nacos 注册中心）
```

### 买家端

```bash
cd uushop_buyer_2026
npm install
npm run serve
```

### 卖家端

```bash
cd uushop_seller_2026
npm install
npm run serve
```
