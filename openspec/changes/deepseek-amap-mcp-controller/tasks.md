## 1. 依赖和配置

- [x] 1.1 在 pom.xml 中添加 Spring AI Alibaba MCP Client 依赖
- [x] 1.2 在 application.yml 中配置高德地图 MCP Server 地址
- [x] 1.3 在 application.yml 中配置高德地图 API Key（使用环境变量注入）
- [x] 1.4 创建 application.yml.example 示例配置文件

## 2. Controller 实现

- [x] 2.1 创建 DeepSeekAmapMcpController 类，沿用现有 Controller 包结构
- [x] 2.2 注入 ChatModel 并构建 ReactAgent，注册高德地图 MCP 工具
- [x] 2.3 实现地点搜索端点 GET `/agent/amap/search`
- [x] 2.4 实现路径规划端点 GET `/agent/amap/route`
- [x] 2.5 实现流式调用端点 GET `/agent/amap/stream`
- [x] 2.6 实现错误处理逻辑，妥善处理 MCP 调用异常

## 3. MCP 工具配置

- [x] 3.1 创建 MCP Client 配置类，定义高德地图 MCP 工具注册逻辑
- [x] 3.2 配置 MCP Client 连接参数（超时、重试等）
- [x] 3.3 实现 MCP Server 连接健康检查

## 4. 测试和验证

- [x] 4.1 编写单元测试验证 Controller 端点映射
- [x] 4.2 编写集成测试验证 MCP 工具调用（Mock MCP Server）
- [ ] 4.3 手动测试地点搜索功能（需要真实 MCP Server）
- [ ] 4.4 手动测试路径规划功能（需要真实 MCP Server）
- [ ] 4.5 手动测试错误处理场景

## 5. 文档

- [x] 5.1 更新 README.md，添加高德地图 MCP 集成说明
- [x] 5.2 编写运行指南，说明如何配置和启动 MCP Server
- [x] 5.3 添加 API 使用示例和请求/响应示例
- [x] 5.4 记录已知问题和注意事项
