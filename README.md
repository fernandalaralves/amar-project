# A.M.A.R - Assistência e Mediação no Aprendizado Repensado no Espectro Autista

O A.M.A.R é uma plataforma web que auxilia professores a criar metodologias de ensino personalizadas para crianças atípicas (TEA), conectando os hiperfocos dos alunos a conteúdos pedagógicos por meio de Inteligência Artificial Generativa.

## Objetivo

Transformar os interesses específicos de crianças neurodivergentes em pontes para o aprendizado, respeitando suas particularidades sensoriais e cognitivas.

## Tecnologias

| Camada | Stack |
|--------|-------|
| Backend | Python 3.11, FastAPI, Uvicorn |
| Frontend | Next.js 14, TypeScript, Tailwind CSS |
| IA | Google Gemini API (gemini-1.5-flash)  |
| Infra | Rate limiting por dispositivo (sem armazenamento de dados pessoais) |

## Estrutura do Projeto

```text
amar-project/
├── backend/          
│   ├── app/
│   ├── requirements.txt
│   └── .env
├── frontend/         
└── README.md
```

## Privacidade
O sistema não armazena dados pessoais de alunos ou professores. O controle de uso é feito por meio de um identificador anônimo (UUID) gerado no navegador do usuário, respeitando a LGPD.


## Limite de Uso
Cada dispositivo pode gerar até 5 metodologias por dia (configurável em application.properties). O limite é reiniciado automaticamente à meia-noite.


