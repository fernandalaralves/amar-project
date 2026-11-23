package com.amar.assistente.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class IaService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String gerarMetodologia(String hiperfoco, String conteudo) {
        try {
            String prompt = construirPrompt(hiperfoco, conteudo);

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "Você é um assistente pedagógico especializado em educação inclusiva e autismo (TEA). " +
                                            "Suas respostas devem ser práticas, lúdicas, acolhedoras e detalhadas."),
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0.8,
                    "max_tokens", 1200
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            throw new RuntimeException("Falha ao chamar a IA: " + e.getMessage(), e);
        }
    }

    private String construirPrompt(String hiperfoco, String conteudo) {
        return """
                Crie uma metodologia de ensino para uma criança atípica (TEA) que tem hiperfoco em: "%s".
                O conteúdo/habilidade a ser trabalhada é: "%s".

                A resposta deve seguir este formato em Markdown:

                ## Objetivo Pedagógico
                (1-2 frases sobre o que se espera alcançar)

                ## Estratégia de Intervenção
                (Explicar como conectar o hiperfoco ao conteúdo, com uma narrativa lúdica)

                ## Atividades Sugeridas
                (Listar 3 a 5 atividades concretas, com materiais e duração)

                ## Adaptações e Cuidados
                (Sugestões de adaptações sensoriais, comunicação alternativa, etc.)

                ## Como Avaliar
                (Indicadores simples de progresso)

                Seja específico, criativo e acolhedor. Use linguagem clara para professores.
                """.formatted(hiperfoco, conteudo);
    }
}