package com.ai_agent.tools;

import java.util.function.Function;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

@Service("additionalFinancialInfos")
@Description("""
		Obtenir les informations financières additionnelles de l'entreprise donnée depuis l'an passé.
		""")
public class AdditionalFinancialInfos implements Function<AdditionalFinancialInfos.Request, AdditionalFinancialInfos.Response>{
	
	public record Request(String companyName) {};
	
	public record Response(String additionalFinancialInfos) {}

	@Override
	public Response apply(Request t) {
		return new Response(" le nombre d'abonnées est dans une tendance très haussière depuis l'année passée");
	};
	
	
}
