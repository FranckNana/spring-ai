package com.ai_agent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai_agent.agent.FinancialAnalysisAgent;

@RestController
public class MyAiAgentController {
	
	@Autowired
	private FinancialAnalysisAgent financialAnalysisAgent;
	
	@GetMapping(value = "/financialAnalysis", produces = MediaType.TEXT_MARKDOWN_VALUE)
	public String askAgent(String company) {
		return financialAnalysisAgent.financialAnalysisReport(company);
	}
}
