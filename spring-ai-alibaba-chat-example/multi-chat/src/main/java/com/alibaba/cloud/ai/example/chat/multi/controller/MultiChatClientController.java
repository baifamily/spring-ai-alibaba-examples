package com.alibaba.cloud.ai.example.chat.multi.controller;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baifamily
 */

@RestController
@RequestMapping
public class MultiChatClientController {

    private static final String DEFAULT_PROMPT = "你好，介绍下你自己！";

    private static final String ASSISTANT_CHAT_PROMPT = "你是一名CEO助理";

    private final ChatClient defaultChatClient;

    private final ChatClient assistantChatClient;

    private final ChatClient deepseekChatClient;

    public MultiChatClientController(@Qualifier("dashscopeChatModel")ChatModel dashscopeChatModel,@Qualifier("deepSeekChatModel")ChatModel deepSeekChatModel) {
        this.defaultChatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withMaxToken(1000)
                                .build()
                )
                .build();

        this.assistantChatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(ASSISTANT_CHAT_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withMaxToken(1000)
                                .build()
                )
                .build();

        this.deepseekChatClient = ChatClient.builder(deepSeekChatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .defaultOptions(
                        DeepSeekChatOptions.builder()
                                .topP(0.7)
                                .build()
                )
                .build();
    }

    /**
     * 利用单个ChatModel创建多个ChatClient的简单调用
     * */
    @GetMapping("/single/chat")
    public String singleChat() {
        StringBuilder sb = new StringBuilder();
        sb.append("defaultChatClient:");
        sb.append(defaultChatClient.prompt(DEFAULT_PROMPT).call().content());
        sb.append(System.lineSeparator());
        sb.append("assistantChatClient:");
        sb.append(assistantChatClient.prompt(DEFAULT_PROMPT).call().content());
        return sb.toString();
    }

    /**
     * 利用dashscopeChatModel和deepSeekChatModel两个模型分别创建ChatClient的简单调用
     * */
    @GetMapping("/multi/chat")
    public String multiChat() {
        StringBuilder sb = new StringBuilder();
        sb.append("defaultChatClient:");
        sb.append(defaultChatClient.prompt(DEFAULT_PROMPT).call().content());
        sb.append(System.lineSeparator());
        sb.append("DeepSeekChatClient:");
        sb.append(deepseekChatClient.prompt(DEFAULT_PROMPT).call().content());
        return sb.toString();
    }
}
