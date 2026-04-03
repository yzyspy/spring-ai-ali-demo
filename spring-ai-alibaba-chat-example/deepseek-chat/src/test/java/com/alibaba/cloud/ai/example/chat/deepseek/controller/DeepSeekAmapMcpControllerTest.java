//package com.alibaba.cloud.ai.example.chat.deepseek.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * DeepSeekAmapMcpController 单元测试
// *
// * @author yangzhongyu
// * @version 1.0
// * @date 2026/3/31
// */
//@WebMvcTest(DeepSeekAmapMcpController.class)
//class DeepSeekAmapMcpControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DeepSeekAmapMcpController controller;
//
//    /**
//     * 测试地点搜索端点映射
//     */
//    @Test
//    void testSearchLocation() throws Exception {
//        mockMvc.perform(get("/agent/amap/search")
//                        .param("query", "北京市的医院"))
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * 测试路径规划端点映射
//     */
//    @Test
//    void testPlanRoute() throws Exception {
//        mockMvc.perform(get("/agent/amap/route")
//                        .param("origin", "北京西站")
//                        .param("destination", "天安门")
//                        .param("mode", "driving"))
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * 测试流式调用端点映射
//     */
//    @Test
//    void testStreamChat() throws Exception {
//        mockMvc.perform(get("/agent/amap/stream")
//                        .param("query", "从北京西站到天安门的路线"))
//                .andExpect(status().isOk());
//    }
//}
