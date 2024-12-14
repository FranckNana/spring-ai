package com.rag_ollama.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatAiService {
	
	private final ChatClient chatClient;
	
	public ChatAiService(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

	public String ragChat(String question) {
		return chatClient.prompt()
						 .user(question)
						 .call()
						 .content();
	}
}
