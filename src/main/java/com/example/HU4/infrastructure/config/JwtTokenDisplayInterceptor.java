package com.example.HU4.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JwtTokenDisplayInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {

        String path = request.getRequestURI();

        // Solo interceptar endpoints de autenticación
        if (path.contains("/api/auth/register") || path.contains("/api/auth/login")) {
            if (response instanceof ContentCachingResponseWrapper) {
                ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
                byte[] content = wrapper.getContentAsByteArray();

                if (content.length > 0 && response.getStatus() == 200) {
                    String responseBody = new String(content, response.getCharacterEncoding());

                    try {
                        // Parsear la respuesta JSON para extraer el token
                        var jsonNode = objectMapper.readTree(responseBody);
                        if (jsonNode.has("token")) {
                            String token = jsonNode.get("token").asText();
                            String username = "";

                            // Intentar obtener el username del request body
                            try {
                                String requestBody = request.getReader().lines()
                                        .reduce("", (accumulator, actual) -> accumulator + actual);
                                var reqNode = objectMapper.readTree(requestBody);
                                if (reqNode.has("username")) {
                                    username = reqNode.get("username").asText();
                                }
                            } catch (Exception ignored) {
                            }

                            displayTokenInfo(path, username, token);
                        }
                    } catch (Exception e) {
                        // Ignorar errores de parsing
                    }
                }
            }
        }
    }

    private void displayTokenInfo(String endpoint, String username, String token) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String action = endpoint.contains("register") ? "REGISTRO" : "LOGIN";

        System.out.println("\n" + "━".repeat(80));
        System.out.println("🔑 NUEVO TOKEN JWT GENERADO - " + timestamp);
        System.out.println("━".repeat(80));
        System.out.println("📌 Acción: " + action);
        if (!username.isEmpty()) {
            System.out.println("👤 Usuario: " + username);
        }
        System.out.println("🎫 Token completo:");
        System.out.println("   " + token);
        System.out.println("\n💡 Para usar este token, copia y pega en el header:");
        System.out.println("   Authorization: Bearer " + token);
        System.out.println("━".repeat(80) + "\n");
    }
}
