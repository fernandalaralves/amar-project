package com.amar.assistente.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Value("${rate.limit.max-requests:5}")
    private int maxRequests;

    /**
     * Estrutura: deviceId -> (data -> quantidade)
     * Reinicia o contador automaticamente a cada novo dia.
     */
    private final Map<String, ContadorDiario> contadores = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getRequestURI().contains("/gerar")) {
            return true;
        }

        String deviceId = request.getHeader("X-Device-Id");
        if (deviceId == null || deviceId.isBlank()) {
            deviceId = request.getRemoteAddr();
        }

        LocalDate hoje = LocalDate.now();
        ContadorDiario contador = contadores.computeIfAbsent(deviceId, k -> new ContadorDiario());

        synchronized (contador) {
            if (!hoje.equals(contador.data)) {
                contador.data = hoje;
                contador.quantidade = 0;
            }

            if (contador.quantidade >= maxRequests) {
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("""
                        {"sucesso":false,"mensagem":"Limite diário de %d gerações atingido para este dispositivo. Tente novamente amanhã."}
                        """.formatted(maxRequests));
                return false;
            }

            contador.quantidade++;
        }

        return true;
    }

    private static class ContadorDiario {
        LocalDate data = LocalDate.now();
        int quantidade = 0;
    }
}
