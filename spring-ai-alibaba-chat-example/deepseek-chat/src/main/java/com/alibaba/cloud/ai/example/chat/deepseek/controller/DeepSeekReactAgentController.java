package com.alibaba.cloud.ai.example.chat.deepseek.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.BiFunction;

/**
 * @author yangzhongyu
 * @version 1.0
 * @date 2026/3/31 11:58
 */
@RestController
@RequestMapping("/agent")
public class DeepSeekReactAgentController {
    private final ChatModel DeepSeekChatModel;

    private final ReactAgent agent;


    public DeepSeekReactAgentController (ChatModel chatModel) {
        this.DeepSeekChatModel = chatModel;

        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("Get weather for a given city")
                .inputType(WeatherRequest.class)
                .build();

        this.agent = ReactAgent.builder()
                .name("weather_agent")
                .model(DeepSeekChatModel)
                .tools(weatherTool)
                .systemPrompt("You are a helpful assistant")
                .saver(new MemorySaver())
                .build();
    }

    // 定义天气查询工具
    public class WeatherTool implements BiFunction<WeatherRequest, ToolContext, String> {
        @Override
        public String apply(WeatherRequest request, ToolContext toolContext) {
            return "It's always sunny in " + request.getCity() + "!";
        }
    }

    @GetMapping("/simple/chat")
    public String simpleChat () throws GraphRunnerException {
        AssistantMessage response = agent.call("what is the weather in San Francisco");
        return response.getText();
    }

    // ... existing code ...
}

// Add input object class to define function parameters (required for object schema)
class WeatherRequest {
    private String city;

    // Getters and setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}

