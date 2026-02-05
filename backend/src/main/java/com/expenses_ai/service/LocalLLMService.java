package com.expenses_ai.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalLLMService {

    private final WebClient webClient;

    public LocalLLMService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:11434")
                .build();
    }

    public String generate(String userPrompt) {

        String strictPrompt = """
You are a financial insight generator.

ABSOLUTE RULES:
- DO NOT use numbers, digits, currency symbols, or percentages
- DO NOT use symbols like ₹, %, or numeric words
- Use ONLY simple English explanations
- Use ONLY the information provided
- DO NOT guess reasons, behavior, dates, or plans
- If information is limited, say so clearly

OUTPUT RULES:
- Write only short sentences
- Keep the tone clear and helpful
- Avoid technical language

INFORMATION:
""" + userPrompt;

        Map<String, Object> request = new HashMap<>();
        request.put("model", "phi3");
        request.put("prompt", strictPrompt);
        request.put("stream", false);

        Map<?, ?> response = webClient.post()
                .uri("/api/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("response")) {
            return "The insight could not be generated.";
        }

        return response.get("response").toString().trim();
    }
}
