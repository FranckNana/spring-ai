package com.rag_ollama.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rag_ollama.service.ChatAiService;
@RestController
@RequestMapping("/chat")
public class ChatRestController {
	
	@Autowired
	private ChatAiService chatAiService;
	
	@Autowired
	private VectorStore vectorStore;
	
	@Value("classpath:/prompts/promptTemplate.st")
	private Resource promptResource;
	
	private final ChatClient chatClient;
	
	public ChatRestController(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}
	
	@GetMapping("/ask")
	public String ask(String question) {
		List<Document> documents = vectorStore.similaritySearch(question);
		List<String> context = documents.stream().map(d->d.getContent()).collect(Collectors.toList());
		//System.out.println("****************"+context);
		
		PromptTemplate promptTemplate = new PromptTemplate(promptResource);
		Prompt prompt = promptTemplate.create(Map.of("CONTEXT", context, "question", question));
		return this.chatClient.prompt(prompt)
				.call()
				.content();
	}
}
