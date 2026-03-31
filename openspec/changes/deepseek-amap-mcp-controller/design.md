## Context

当前 deepseek-chat 模块已经实现了基础的 DeepSeek 聊天功能和 ReAct Agent 工具调用示例（WeatherTool）。然而，现有的工具调用都是本地实现的模拟工具，缺少与真实外部服务集成的 MCP (Model Context Protocol) 实践。

Spring AI Alibaba 支持通过 MCP 协议连接外部服务，高德地图提供了丰富的地理位置服务能力。通过 MCP 集成，可以让 DeepSeek 模型理解用户自然语言请求，自动调用高德地图服务完成地点搜索、路径规划等任务。

**技术栈:**
- Spring Boot 3.5.7
- Spring AI 1.1.0
- Spring AI Alibaba 1.1.0.0
- DeepSeek Chat Model

## Goals / Non-Goals

**Goals:**
- 创建一个 Controller 演示 DeepSeek + Spring AI Alibaba 调用高德地图 MCP 服务
- 实现地点搜索、路径规划等核心地理位置功能
- 提供 REST API 端点供用户测试和体验
- 展示 MCP 协议在 Spring AI Alibaba 中的使用方式
- 保持与现有代码风格一致（参考 DeepSeekReactAgentController）

**Non-Goals:**
- 不实现完整的 MCP Server（使用外部高德地图 MCP Server 或官方 MCP 服务）
- 不修改现有的 ChatModel/ChatClient/Agent Controller
- 不实现复杂的 UI 界面，仅提供 REST API
- 不处理高并发、缓存等生产级特性

## Decisions

### 1. MCP 集成方式

**决策**: 使用 Spring AI Alibaba MCP Client 连接外部高德地图 MCP Server

**理由:**
- Spring AI Alibaba 已内置 MCP Client 支持
- 高德地图提供标准 MCP 服务接口
- 分离关注点：本示例专注于展示如何调用 MCP 服务，而非实现 MCP Server

**替代方案:**
- 方案 A: 自己实现高德地图 MCP Server
  - 缺点：增加复杂度，偏离演示目标
- 方案 B: 直接使用高德地图 REST API + Function Tool
  - 缺点：无法展示 MCP 协议的优势

### 2. Controller 设计模式

**决策**: 沿用现有 `DeepSeekReactAgentController` 的 ReAct Agent 模式

**理由:**
- 与现有代码风格保持一致
- ReAct Agent 天然适合工具调用场景
- 支持多轮对话和上下文记忆

**实现方式:**
- 注入 `ChatModel` 构建 ReactAgent
- 通过 MCP Client 注册高德地图工具
- 提供同步和流式两种调用方式

### 3. 配置管理

**决策**: 在 application.yml 中配置 MCP Server 地址和高德地图 API Key

**理由:**
- 遵循现有配置模式（参考 DeepSeek API Key 配置）
- 便于用户自行修改配置
- 支持环境变量注入敏感信息

### 4. 依赖管理

**决策**: 在 pom.xml 中添加 `spring-ai-alibaba-starter-mcp` 依赖

**理由:**
- Spring AI Alibaba 官方 MCP 支持
- 与现有 Spring AI Alibaba Agent Framework 兼容

## Risks / Trade-offs

**[Risk] 高德地图 MCP Server 可用性**
→  mitigations: 
- 提供本地 Mock 实现作为备选
- 在文档中说明如何配置和启动 MCP Server

**[Risk] API Key 管理和费用**
→ mitigations:
- 使用环境变量配置，避免硬编码
- 在 README 中说明费用相关信息

**[Risk] MCP 协议版本兼容性**
→ mitigations:
- 明确记录测试通过的版本组合
- 提供版本兼容性说明

**[Trade-off] 依赖外部 MCP Server**
- 优点：简化示例代码，聚焦核心演示
- 缺点：用户需要额外配置 MCP Server 才能运行

## Migration Plan

本 change 为新增功能，不影响现有代码，无需迁移计划。

**部署步骤:**
1. 创建新的 Controller 类
2. 更新 pom.xml 添加 MCP 依赖
3. 更新 application.yml 添加 MCP 配置
4. 用户配置高德地图 API Key 和 MCP Server 地址

**回滚策略:**
- 删除新增的 Controller 文件
- 还原 pom.xml 和 application.yml 的修改

## Open Questions

1. 高德地图 MCP Server 的具体部署方式和地址？
   - 需要确认是否有官方提供的 MCP Server 或参考实现

2. 高德地图 API 的计费模式和使用限制？
   - 需要在文档中说明

3. 是否需要提供 Docker Compose 配置简化 MCP Server 部署？
   - 作为后续优化项
