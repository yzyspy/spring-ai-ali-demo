## ADDED Requirements

### Requirement: MCP Client 配置和初始化
系统 SHALL 支持通过 Spring AI Alibaba MCP Client 连接高德地图 MCP Server。

#### Scenario: 成功配置 MCP Client
- **WHEN** 用户在 application.yml 中配置了高德地图 MCP Server 地址和 API Key
- **THEN** Spring 容器启动时自动初始化 MCP Client 并注册高德地图工具

#### Scenario: MCP Server 连接失败
- **WHEN** MCP Server 地址不可达或配置错误
- **THEN** 应用启动时抛出明确的异常信息，指示 MCP 连接失败原因

### Requirement: 地点搜索功能
系统 SHALL 支持通过自然语言调用高德地图 MCP 服务进行地点搜索。

#### Scenario: 用户搜索地点
- **WHEN** 用户发送包含地点名称的请求（如"查找北京市的医院"）
- **THEN** DeepSeek 模型识别意图并调用高德地图 MCP 的地点搜索工具，返回搜索结果

#### Scenario: 地点搜索无结果
- **WHEN** 用户搜索的地点不存在或无匹配结果
- **THEN** 系统返回友好的提示信息，告知用户未找到相关地点

### Requirement: 路径规划功能
系统 SHALL 支持通过自然语言调用高德地图 MCP 服务进行路径规划。

#### Scenario: 用户查询驾车路线
- **WHEN** 用户发送包含起点和终点的请求（如"从北京西站到天安门怎么走"）
- **THEN** DeepSeek 模型识别意图并调用高德地图 MCP 的路径规划工具，返回驾车路线信息

#### Scenario: 用户查询公交路线
- **WHEN** 用户发送包含起点、终点和交通方式的请求（如"从首都机场到中关村坐地铁怎么走"）
- **THEN** DeepSeek 模型识别意图并调用高德地图 MCP 的路径规划工具，返回公交/地铁路线信息

### Requirement: 天气查询功能
系统 SHALL 支持通过自然语言调用高德地图 MCP 服务查询天气信息。

#### Scenario: 用户查询城市天气
- **WHEN** 用户发送包含城市名称的天气查询请求（如"北京今天天气怎么样"）
- **THEN** DeepSeek 模型识别意图并调用高德地图 MCP 的天气查询工具，返回天气信息

### Requirement: REST API 端点
系统 SHALL 提供 REST API 端点供用户测试高德地图 MCP 功能。

#### Scenario: 同步调用地点搜索
- **WHEN** 用户访问 GET `/agent/amap/search?query=北京市的医院`
- **THEN** 系统返回 DeepSeek 模型调用高德地图 MCP 后的响应文本

#### Scenario: 同步调用路径规划
- **WHEN** 用户访问 GET `/agent/amap/route?origin=北京西站&destination=天安门`
- **THEN** 系统返回 DeepSeek 模型调用高德地图 MCP 后的路线规划响应文本

#### Scenario: 流式调用
- **WHEN** 用户访问 GET `/agent/amap/stream?query=从北京西站到天安门的路线`
- **THEN** 系统以 Server-Sent Events 或 Flux 形式流式返回响应内容

### Requirement: 错误处理
系统 SHALL 妥善处理 MCP 调用过程中的各种异常情况。

#### Scenario: 高德地图 API 调用失败
- **WHEN** 高德地图 API 返回错误或超时
- **THEN** 系统返回友好的错误提示，记录详细日志

#### Scenario: MCP Server 不可用
- **WHEN** MCP Server 未启动或连接断开
- **THEN** 系统返回明确的错误信息，提示用户检查 MCP Server 配置

#### Scenario: API Key 无效或配额耗尽
- **WHEN** 高德地图 API Key 无效或当日调用次数耗尽
- **THEN** 系统返回明确的错误信息，提示用户检查 API Key 配置
