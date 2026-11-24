from pydantic import BaseModel, Field

class MetodologiaRequest(BaseModel):
    hiperfoco: str = Field(..., max_length=200, description="Hiperfoco do aluno")
    conteudo: str = Field(..., max_length=300, description="Conteúdo a ser trabalhado")
    deviceId: str = Field(..., description="ID anônimo do dispositivo")

class MetodologiaResponse(BaseModel):
    sucesso: bool
    sugestao: str | None = None
    mensagem: str | None = None
