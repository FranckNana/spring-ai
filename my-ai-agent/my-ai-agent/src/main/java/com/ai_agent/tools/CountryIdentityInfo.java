package com.ai_agent.tools;

import java.util.function.Function;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

@Service("countryIdentityInfo")
@Description("""
		Obtenir les informations d'identité de l'entreprise donnée en incluant:
		- le nom de l'entreprisee,
		- le pays de l'entreprise,
		- le domaine de l'entreprise
		- l'année de création de l'entreprise
		""")
public class CountryIdentityInfo implements Function<CountryIdentityInfo.Request, CountryIdentityInfo.Response>{
	
	public record Request(String companyName) {};
	
	public record Response(String companyName, String country, String industryDomain, int foundedYear) {};

	@Override
	public Response apply(Request request) {
		return new Response(request.companyName, "France", "Conseil en innovation et transformation numérique", 2020);
	}
}
