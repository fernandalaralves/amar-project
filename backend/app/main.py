from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from app.config import settings
from app.schemas import MetodologiaRequest, MetodologiaResponse
from app.services.ia_service import ia_service
from app.middlewares.rate_limit import rate_limit_middleware

app = FastAPI(
    title="A.M.A.R API",
    description="Assistencia e Mediacao no Aprendizado do Espectro Autista",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.middleware("http")(rate_limit_middleware)

@app.get("/api/metodologia/health")
def health():
    return {"status": "A.M.A.R Backend funcionando"}

@app.post("/api/metodologia/gerar", response_model=MetodologiaResponse)
def gerar(request: MetodologiaRequest):
    try:
        sugestao = ia_service.gerar_metodologia(request.hiperfoco, request.conteudo)
        return MetodologiaResponse(sucesso=True, sugestao=sugestao)
    except Exception as e:
        return MetodologiaResponse(sucesso=False, mensagem=str(e))
