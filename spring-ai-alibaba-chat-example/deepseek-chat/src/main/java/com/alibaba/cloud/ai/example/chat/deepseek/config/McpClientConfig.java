package com.alibaba.cloud.ai.example.chat.deepseek.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP Client 配置类
 *
 * 配置高德地图 MCP Server 连接参数
 *
 * @author yangzhongyu
 * @version 1.0
 * @date 2026/3/31
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.ai.mcp.client", name = "enabled", havingValue = "true", matchIfMissing = true)
public class McpClientConfig {

    private static final Logger log = LoggerFactory.getLogger(McpClientConfig.class);

    /**
     * 高德地图 MCP Client Bean
     * 通过 STDIO 传输方式连接本地 MCP Server
     */
    @Bean
    public McpSyncClient amapMcpClient() {
        log.info("Initializing Amap MCP Client...");

        Map<String, String> env = new HashMap<>();
        env.put("AMAP_MAPS_API_KEY", "dd3d43aeefd49d680398a203f5a2bfdf");

        ServerParameters serverParams = ServerParameters.builder("npx")
                .args("-y", "@amap/amap-maps-mcp-server")
                .env(env)
                .build();

        // 2. 创建 Stdio 传输层
        ObjectMapper MAPPER = new ObjectMapper();
        StdioClientTransport transport = new StdioClientTransport(serverParams, new JacksonMcpJsonMapper(MAPPER));

        // 3. 构建同步 MCP 客户端，设置请求超时
        McpSyncClient client = McpClient.sync(transport)
                .requestTimeout(Duration.ofSeconds(30))
                .build();


        log.info("Amap MCP Client initialized successfully");
        return client;
    }

    /**
     * 提供 MCP 工具回调
     * 使用 McpToolCallbackProvider 将 McpClient 转换为 Spring AI ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider amapToolCallbackProvider(McpSyncClient amapMcpClient) {
        log.info("Registering Amap MCP tools via McpToolCallbackProvider...");
        return new SyncMcpToolCallbackProvider(amapMcpClient);
    }
}
