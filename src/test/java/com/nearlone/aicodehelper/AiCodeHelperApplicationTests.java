package com.nearlone.aicodehelper;

import com.nearlone.aicodehelper.ai.AiCodeHelper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeHelperApplicationTests {

    @Resource
    private AiCodeHelper aiCodeHelper;
    @Test
    void chat() {
        aiCodeHelper.chat("你好，我正在学习Java+ai项目的开发，采用langchain4j");
    }
}
