## Why

当前项目中已有 DeepSeek + Spring AI Alibaba 的基础实现和 ReAct Agent 工具调用示例，但缺少与真实外部服务（高德地图）集成的 MCP (Model Context Protocol) 实践。本 change 旨在创建一个演示 controller，展示如何通过 DeepSeek + Spring AI Alibaba 调用高德地图 MCP 服务，实现真实的地理位置相关功能（如地点搜索、路径规划等）。

## What Changes

- 新增一个 `DeepSeekAmapMcpController`，演示通过 MCP 协议调用高德地图服务
- 使用 Spring AI Alibaba 的 MCP Client 能力连接高德地图 MCP Server
- 通过 DeepSeek 模型进行自然语言理解，自动调用高德地图工具
- 提供 REST API 端点测试地点搜索、路径规划等场景

## Capabilities

### New Capabilities
- `amap-mcp-integration`: 集成高德地图 MCP 服务，实现地点搜索、路径规划、天气查询等地理位置相关功能

### Modified Capabilities
- 

## Impact

- **新增代码**: `spring-ai-alibaba-chat-example/deepseek-chat/src/main/java/com/alibaba/cloud/ai/example/chat/deepseek/controller/DeepSeekAmapMcpController.java`
- **新增依赖**: 需要添加 Spring AI Alibaba MCP Client 相关依赖到 pom.xml
- **配置变更**: 需要在 application.yml 中配置高德地图 API Key 和 MCP Server 地址
- **环境要求**: 需要运行高德地图 MCP Server 或配置相应的 MCP 服务地址
