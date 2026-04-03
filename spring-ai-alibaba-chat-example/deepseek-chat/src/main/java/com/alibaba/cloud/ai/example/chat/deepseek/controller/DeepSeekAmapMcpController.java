package com.alibaba.cloud.ai.example.chat.deepseek.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * DeepSeek + Spring AI Alibaba 调用高德地图 MCP 服务演示 Controller
 * 
 * 通过 MCP 协议连接高德地图服务，实现地点搜索、路径规划等功能
 * 
 * @author yangzhongyu
 * @version 1.0
 * @date 2026/3/31
 */
@RestController
@RequestMapping("/agent/amap")
public class DeepSeekAmapMcpController {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekAmapMcpController.class);

    private final ChatModel deepSeekChatModel;
    private final ReactAgent amapAgent;

    /**
     * 构造函数注入 ChatModel 并构建 ReactAgent
     * 通过 ToolCallbackProvider 自动注册 MCP 工具
     */
    public DeepSeekAmapMcpController(ChatModel chatModel,
                                     @Qualifier("amapToolCallbackProvider") ToolCallbackProvider toolCallbackProvider) {
        this.deepSeekChatModel = chatModel;
        
        // 从 MCP Client 获取高德地图工具
        ToolCallback[] amapTools = toolCallbackProvider.getToolCallbacks();
        
        // 构建 ReactAgent，注册高德地图 MCP 工具
        this.amapAgent = ReactAgent.builder()
                .name("amap_agent")
                .model(deepSeekChatModel)
                .tools(amapTools)
                .systemPrompt("你是一个智能助手，可以调用高德地图 MCP 服务帮助用户查询地点、规划路线、查询天气等。请使用中文回答。")
                .saver(new MemorySaver())
                .build();
        
        log.info("DeepSeekAmapMcpController initialized with {} MCP tools", amapTools.length);
    }

    /**
     * 在浏览器输入这个地址
     * http://localhost:10001/agent/amap/search?query=北京市的医院
     *
     * 地点搜索端点
     * 
     * @param query 搜索关键词，如"北京市的医院"、"上海中心大厦"
     * @return DeepSeek 模型调用高德地图 MCP 后的响应文本
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchLocation(@RequestParam(name = "query") String query) {
        try {
            log.info("Searching location: {}", query);
            String prompt = "请帮我查询：" + query;
            AssistantMessage response = amapAgent.call(prompt);
            return ResponseEntity.ok(response.getText());
        } catch (GraphRunnerException e) {
            log.error("Failed to search location: {}", query, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("地点搜索失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during location search: {}", query, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("系统错误，请稍后重试");
        }
    }

    /**
     * 路径规划端点
     * 
     * @param origin 起点，如"北京西站"
     * @param destination 终点，如"天安门"
     * @param mode 交通方式（可选）：driving（驾车）、transit（公交）、walking（步行），默认为 driving
     * @return DeepSeek 模型调用高德地图 MCP 后的路线规划响应文本
     */
    @GetMapping("/route")
    public ResponseEntity<String> planRoute(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(defaultValue = "driving") String mode) {
        try {
            log.info("Planning route from {} to {}, mode: {}", origin, destination, mode);
            String prompt = String.format("请帮我规划从 %s 到 %s 的路线，交通方式：%s", origin, destination, mode);
            AssistantMessage response = amapAgent.call(prompt);
            return ResponseEntity.ok(response.getText());
        } catch (GraphRunnerException e) {
            log.error("Failed to plan route from {} to {}", origin, destination, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("路径规划失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during route planning: {} -> {}", origin, destination, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("系统错误，请稍后重试");
        }
    }

    /**
     * 流式调用端点
     * 
     * @param query 查询内容，如"从北京西站到天安门的路线"
     * @return 流式响应内容
     */
    @GetMapping("/stream")
    public Flux<String> streamChat(@RequestParam String query) {
        return Flux.create(emitter -> {
            try {
                log.info("Streaming chat: {}", query);
                AssistantMessage response = amapAgent.call(query);
                emitter.next(response.getText());
                emitter.complete();
            } catch (GraphRunnerException e) {
                log.error("Failed to stream chat: {}", query, e);
                emitter.error(e);
            } catch (Exception e) {
                log.error("Unexpected error during streaming: {}", query, e);
                emitter.error(e);
            }
        });
    }
}
