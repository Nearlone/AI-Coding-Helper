package com.nearlone.aicodehelper.ai;

import com.nearlone.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

//@AiService
@InputGuardrails({SafeInputGuardrail.class})
@Service
public interface AiCodeHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String message);

    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String userMessage);

    // 流式对话
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
