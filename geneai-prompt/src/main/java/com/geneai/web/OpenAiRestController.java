package com.geneai.web;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geneai.models.Identity;

@RestController
public class OpenAiRestController {
	
	@Value("${spring.ai.openai.api-key}")
	private String apiKey;

	private final ChatClient chatClient;
	
	public OpenAiRestController(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

	@GetMapping("/chat")
	public String chat(String message) {
		return chatClient.prompt().user(message).call().content();
	}
	
	@GetMapping("/movies")
	public String movies(@RequestParam(name = "category", defaultValue = "action") String category,
						 @RequestParam(name = "year", defaultValue = "2019") int year) {
		
		OpenAiApi  openAiApi = new OpenAiApi(apiKey);
		
		OpenAiChatOptions options = OpenAiChatOptions.builder()
				.withModel("gpt-4")
				.withTemperature(0.0)
				.withMaxTokens(2000)
				.build();
		
		OpenAiChatModel openAiChatModel = new OpenAiChatModel(openAiApi, options);
		
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
				"""
					Je veux que tu me donner le meilleur film de la catégorie : {category}
					pour l'année : {year} le tout au format json en présentant comme suit :
					- categorie<la catégorie donnée en paramètre>
					- année<l'année donnée en paramètre>
					- title<le titre du film>
					- note<la note 
				"""
		);
		
		Prompt prompt = systemPromptTemplate.create(Map.of("category", category , "year", year));
		
		return openAiChatModel.call(prompt).getResult().getOutput().getContent();
	}
	
	@GetMapping("/calcul")
	public String sentimentAnalysis(String elem1, String elem2) {
		
		OpenAiApi  openAiApi = new OpenAiApi(apiKey);
		
		OpenAiChatOptions options = OpenAiChatOptions.builder()
				.withModel("gpt-4")
				.withTemperature(0.0)
				.withMaxTokens(2000)
				.build();
		
		OpenAiChatModel openAiChatModel = new OpenAiChatModel(openAiApi, options);
		
		String systemMessageText = """
				donne moi la somme des éléments envoyés qui sont séparé par: - 
				renvoie moi un json avec le champ:
				- Total : <le résultat du calcul>
				""";
		SystemMessage systemMessage = new SystemMessage(systemMessageText);
		UserMessage userMessage = new UserMessage(elem1+"-"+elem2);
		
		Prompt zeroShotPrompt = new Prompt(List.of(systemMessage, userMessage));
		return openAiChatModel.call(zeroShotPrompt).getResult().getOutput().getContent();
	}
	
	@GetMapping("/identity")
	public Identity getIdentity(@RequestParam String year) {
		
		String systemMessageText = """
			Qui est le président de la république de France de l'année qui sera fournie
		""";
		
		return this.chatClient.prompt()
				.system(systemMessageText)
				.user(year)
				.call()
				.entity(Identity.class);
	}
	
	@GetMapping("/identities")
	public List<Identity> getIdentities(@RequestParam String pays) {
		
		String systemMessageText = """
			Donne mois la liste de 5 présidents du pays qui sera fourni.
		""";
		
		return this.chatClient.prompt()
				.system(systemMessageText)
				.user(pays)
				.call()
				.entity(new ParameterizedTypeReference<List<Identity>>(){});
	}
	
	@GetMapping("/ocr")
	public String ocr() throws IOException {
		Resource resource = new ClassPathResource("test.jpg");
		//byte[] data = resource.getContentAsByteArray();
		
		String userMeassageText = """
			analyse moi l'image fournie et donne moi les informations que tu as pu extraire.
		""";
		
		UserMessage userMessage = new UserMessage(userMeassageText, List.of(
			new Media(MimeTypeUtils.IMAGE_JPEG, resource)
		));
		
		Prompt zeroShotPrompt = new Prompt(userMessage);
		return this.chatClient.prompt(zeroShotPrompt).call().content();
	}
	
	@GetMapping(path="/generateImage", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] generateImage() {
		
		OpenAiImageApi openAiImageApi = new OpenAiImageApi(apiKey);
		
		OpenAiImageModel openAiImageModel = new OpenAiImageModel(openAiImageApi);
		
		ImageResponse imageResponse = openAiImageModel.call(
				new ImagePrompt("un chien avec un costume dans une fête en causerie avec un chat",
						OpenAiImageOptions.builder()
							.withModel("dall-e-3")
							.withQuality("hd")
							.withN(1)
							.withResponseFormat("b64_json")
							.withHeight(1024)
							.withWidth(1024)
							.build()
				)
		);
		
		String b64JsonImgStr = imageResponse.getResult().getOutput().getB64Json();
		
		byte[] generatedImage = Base64.getDecoder().decode(b64JsonImgStr);
		
		return generatedImage;
	}
}
