package com.ai_agent.tools;

import java.util.function.Function;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

@Service("financialDataTool")
@Description("""
		Obtenir les informations financières de l'entreprise donnée en incluant:
		- le chiffre d'affaire de l'entreprise sur les 3 dernières années,
		- le profit de l'entreprise sur les 3 dernières années,
		- la valeur des actions sur les 3 derniers jours
		""")
public class FinancialDataTool implements Function<FinancialDataTool.Request, FinancialDataTool.Response>{

	public record Request(String companyName) {};
	
	public record Response(double[] chiffreAffaire, double[] profit, double[] stock) {};

	@Override
	public Response apply(Request t) {
		return new Response(
				new double[] {100000, 200000, 300000}, 
				new double[] {10000, 20000, 30000}, 
				new double[] {1000, 2000, 3000}
		);
	}
}
