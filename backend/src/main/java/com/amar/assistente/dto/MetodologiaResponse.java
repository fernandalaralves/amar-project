package com.amar.assistente.dto;

public record MetodologiaResponse(
        boolean sucesso,
        String sugestao,
        String mensagem
) {
    public static MetodologiaResponse ok(String sugestao) {
        return new MetodologiaResponse(true, sugestao, null);
    }

    public static MetodologiaResponse erro(String mensagem) {
        return new MetodologiaResponse(false, null, mensagem);
    }
}
