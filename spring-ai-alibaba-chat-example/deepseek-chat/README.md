# DeepSeek Chat + 高德地图 MCP 集成示例

本示例演示如何使用 Spring AI Alibaba 集成高德地图 MCP (Model Context Protocol) 服务，通过 DeepSeek 模型实现自然语言调用高德地图功能。

## 功能特性

- **地点搜索**: 通过自然语言查询地点信息
- **路径规划**: 支持驾车、公交、步行等多种交通方式的路线规划
- **天气查询**: 查询指定城市的天气信息
- **流式响应**: 支持 Server-Sent Events 流式输出

## 前置要求

1. **Java 17+**
2. **Node.js 18+** (用于运行高德地图 MCP Server)
3. **DeepSeek API Key** - 获取地址：https://platform.deepseek.com/
4. **高德地图 API Key** - 获取地址：https://lbs.amap.com/api/webservice/guide/create-project/get-key

## 快速开始

### 1. 配置 API Keys

复制示例配置文件并填写你的 API Keys：

```bash
cd spring-ai-alibaba-chat-example/deepseek-chat/src/main/resources
cp application.yml.example application.yml
```

编辑 `application.yml`：
- 设置 `AI_DEEPSEEK_API_KEY` 环境变量
- 设置 `AMAP_MAPS_API_KEY` 环境变量

或者直接在 `application.yml` 中配置：

```yaml
spring:
  ai:
    deepseek:
      api-key: "your-deepseek-api-key"
```

### 2. 安装高德地图 MCP Server

高德地图 MCP Server 通过 npx 运行，无需单独安装：

```bash
# 验证 npx 可用
npx --version

# 测试运行高德地图 MCP Server（可选）
npx -y @amap/amap-maps-mcp-server
```

### 3. 运行应用

```bash
cd spring-ai-alibaba-chat-example/deepseek-chat
mvn spring-boot:run
```

应用启动后，访问 http://localhost:10001

## API 端点

### 1. 地点搜索

**请求:**
```bash
GET /agent/amap/search?query=北京市的医院
```

**响应示例:**
```
北京市有多家医院，以下是部分推荐：
1. 北京协和医院 - 东城区帅府园一号
2. 北京大学人民医院 - 西直门南大街 11 号
3. 北京朝阳医院 - 朝阳区工体南路 8 号
```

### 2. 路径规划

**请求:**
```bash
GET /agent/amap/route?origin=北京西站&destination=天安门&mode=driving
```

**参数说明:**
- `origin`: 起点（必填）
- `destination`: 终点（必填）
- `mode`: 交通方式（可选），支持 `driving`（驾车）、`transit`（公交）、`walking`（步行），默认为 `driving`

**响应示例:**
```
从北京西站到天安门的驾车路线：
全程约 10.5 公里，预计耗时 35 分钟
途经：莲花池东路 -> 前门西大街 -> 前门东大街
```

### 3. 流式调用

**请求:**
```bash
GET /agent/amap/stream?query=从首都机场到中关村怎么走
```

**响应:**
以 Server-Sent Events 格式流式返回响应内容。

## 配置说明

### application.yml

```yaml
spring:
  ai:
    deepseek:
      api-key: ${AI_DEEPSEEK_API_KEY}
      base-url: "https://api.deepseek.com"
      chat:
        options:
          model: deepseek-chat
    
    mcp:
      client:
        stdio:
          servers-configuration: classpath:/mcp-servers-config.json
        toolcallback:
          enabled: true
```

### mcp-servers-config.json

```json
{
  "mcpServers": {
    "amap-maps": {
      "command": "npx",
      "args": [
        "-y",
        "@amap/amap-maps-mcp-server"
      ],
      "env": {
        "AMAP_MAPS_API_KEY": "your-amap-api-key"
      }
    }
  }
}
```

## 已知问题

1. **MCP Server 启动失败**: 确保 Node.js 版本 >= 18，且网络连接正常
2. **API 调用限额**: 高德地图 API 有每日调用限额，请留意你的配额
3. **首次调用较慢**: 首次调用时 MCP Server 需要启动，可能需要几秒延迟

## 注意事项

- 高德地图 API 调用可能产生费用，请留意你的账户余额
- 生产环境请使用环境变量配置 API Key，不要硬编码在配置文件中
- MCP Server 通过 STDIO 与主应用通信，确保应用有权限执行 npx 命令

## 参考资料

- [Spring AI Alibaba 官方文档](https://sca.aliyun.com/)
- [高德地图 MCP Server](https://github.com/amap-maps/amap-maps-mcp-server)
- [DeepSeek 开放平台](https://platform.deepseek.com/)
