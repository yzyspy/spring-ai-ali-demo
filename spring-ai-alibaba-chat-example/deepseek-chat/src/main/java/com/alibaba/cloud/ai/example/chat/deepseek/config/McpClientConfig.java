package com.alibaba.cloud.ai.example.chat.deepseek.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.client.McpClient;
import org.springframework.ai.mcp.client.transport.ServerParameters;
import org.springframework.ai.mcp.client.transport.StdioClientTransport;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public McpClient amapMcpClient() {
        log.info("Initializing Amap MCP Client...");
        
        ServerParameters serverParams = ServerParameters.builder("npx")
                .args("-y", "@amap/amap-maps-mcp-server")
                .env("AMAP_MAPS_API_KEY", System.getenv("AMAP_MAPS_API_KEY"))
                .build();
        
        StdioClientTransport transport = new StdioClientTransport(serverParams);
        
        McpClient client = McpClient.builder()
                .transport(transport)
                .build();
        
        log.info("Amap MCP Client initialized successfully");
        return client;
    }

    /**
     * 提供 MCP 工具回调
     * Spring AI 会自动从 MCP Server 发现可用工具
     */
    @Bean
    public ToolCallbackProvider amapToolCallbackProvider(McpClient amapMcpClient) {
        log.info("Registering Amap MCP tools...");
        return amapMcpClient;
    }
}
