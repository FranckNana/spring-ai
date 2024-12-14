package com.ai_agent.agent;

import org.springframework.ai.chat.client.ChatClient;

import com.ai_agent.annotations.AiAgent;

@AiAgent
public class FinancialAnalysisAgent {
	
	String systemPrompt = """
			Pourrais-tu générer le rapport financiers de l'entreprise fournie en utilisant les données actuelles.
			Ton rapport devrait contenir les informations de l'entreprise (nom, pays, domain, année de création).
			N'oublie pas de fournir à la fin de ton rapport une petite conclusion de l'analyse financière.
			""";
	
	private ChatClient chatClient;
	
	public FinancialAnalysisAgent(ChatClient.Builder chatClient) {
		// on peut definir par defaut ici le systemPrompt et les tools
		this.chatClient = chatClient
							//.defaultSystem("systemPrompt")
							//.defaultFunctions(tools)
							.build();
	}
	
	private String[] tools= new String[] {"countryIdentityInfo", "financialDataTool", "additionalFinancialInfos"};
	public String financialAnalysisReport(String company) {
		return this.chatClient.prompt()
				.user("nom de l'entreprise : "+company)
				.system(systemPrompt)
				.functions(tools)
				.call()
				.content();
	}
}
