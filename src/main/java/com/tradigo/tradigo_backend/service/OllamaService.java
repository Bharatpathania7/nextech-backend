package com.tradigo.tradigo_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OllamaService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String analyze(String prompt) {

        String url = "http://localhost:11434/api/generate";

        Map<String, Object> request = Map.of(
                "model", "tinyllama",
                "prompt", prompt,
                "stream", false
        );

        Map response = restTemplate.postForObject(url, request, Map.class);

        return response.get("response").toString();
    }
}