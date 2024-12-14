package com.spring_rag;

import java.text.Collator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SpringRagApplication {

	/*@Value("${spring.ai.openai.api-key}")
	private String apiKey;*/
	

	public static void main(String[] args) {
		SpringApplication.run(SpringRagApplication.class, args);
	}
	
	/*@Bean
	CommandLineRunner commandLineRunner(VectorStore vectorStor, JdbcTemplate jdbcTemplate, 
			@Value("classpath:totem.pdf") Resource pdfResource) {
		return args -> {
			
			OpenAiApi  openAiApi = new OpenAiApi(apiKey);
			
			OpenAiChatOptions options = OpenAiChatOptions.builder()
					.withModel("gpt-4")
					.withTemperature(0.0)
					.withMaxTokens(2000)
					.build();
			
			OpenAiChatModel openAiChatModel = new OpenAiChatModel(openAiApi, options);
			
			//testEmbadding(vectorStor, jdbcTemplate, pdfResource);
			
			String query = "il y a combien de modules  dans ce projet ?";
			
			List<Document> documentList = vectorStor.similaritySearch(query);
			
			String systemMessageText = """
						Réponds à la question en te basant sur le CONTEXT fourni.
						si tu ne trouve pas de réponse dans le contexte réponds juste par "DESOLE MAIS JE NE TROUVE PAS !".
						CONTEXT :
							{CONTEXT}
					""";
			
			Message systemMessage = new SystemPromptTemplate(systemMessageText).createMessage(Map.of("CONTEXT",documentList));
		
			UserMessage userMessage = new UserMessage(query);
			
			Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
			
			String response = openAiChatModel.call(prompt).getResult().getOutput().getContent();
			
			System.out.println(response);
		
		};
	}

	private void testEmbadding(VectorStore vectorStor, JdbcTemplate jdbcTemplate, Resource pdfResource) {
		jdbcTemplate.update("DELETE FROM vector_store");
		
		PdfDocumentReaderConfig config = PdfDocumentReaderConfig.defaultConfig();
		PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfResource, config);
		
		List<Document> documentList = pdfDocumentReader.get();
		String contentPdf = documentList.stream().map(d->d.getContent()).collect(Collectors.joining("\n"));
		
		int texteLength = contentPdf.length();
		int maxNumChunks = Math.min(200, (texteLength / 3000));
		int defaultChunkSize = texteLength / maxNumChunks;
		int minChunkSizeChars = Math.max(200, texteLength / 3000);
		int minChunkLengthToEmbed = Math.max(200, texteLength / 3000);
		
		
		TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(defaultChunkSize, minChunkSizeChars, minChunkLengthToEmbed, maxNumChunks, true);
		List<Document> chunkDocs = tokenTextSplitter.split(documentList);
		
		vectorStor.accept(chunkDocs);
	}
*/
}
