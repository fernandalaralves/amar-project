package com.amar.assistente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MetodologiaRequest(

        @NotBlank(message = "O hiperfoco do aluno é obrigatório")
        @Size(max = 200, message = "O hiperfoco deve ter no máximo 200 caracteres")
        String hiperfoco,

        @NotBlank(message = "O conteúdo a ser trabalhado é obrigatório")
        @Size(max = 300, message = "O conteúdo deve ter no máximo 300 caracteres")
        String conteudo,

        @NotBlank(message = "O deviceId é obrigatório")
        String deviceId
) {}