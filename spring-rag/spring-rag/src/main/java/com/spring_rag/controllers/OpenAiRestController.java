package com.spring_rag.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring_rag.service.RagService;

@RestController
public class OpenAiRestController {
	
	@Value("${spring.ai.openai.api-key}")
	private String apiKey;
	
	private final ChatClient chatClient;
	
	@Autowired
	private RagService ragService;
	
	public OpenAiRestController(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

	@GetMapping("/chat")
	public String chat(String message) {
		return chatClient.prompt().user(message).call().content();
	}
	
	@GetMapping("/test-rag-service")
	public String testRagService(String message) {
		return ragService.asKLMM();
	}
}
