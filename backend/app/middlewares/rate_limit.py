from datetime import date
from fastapi import Request, HTTPException
from app.config import settings

class RateLimitMiddleware:
    def __init__(self):
        self.contadores = {}

    async def __call__(self, request: Request, call_next):
        if "/api/metodologia/gerar" not in request.url.path:
            return await call_next(request)

        device_id = request.headers.get("X-Device-Id") or request.client.host
        hoje = date.today()

        if device_id not in self.contadores:
            self.contadores[device_id] = {"data": hoje, "quantidade": 0}

        contador = self.contadores[device_id]

        if contador["data"] != hoje:
            contador["data"] = hoje
            contador["quantidade"] = 0

        if contador["quantidade"] >= settings.RATE_LIMIT_MAX_REQUESTS:
            raise HTTPException(
                status_code=429,
                detail=f"Limite diario de {settings.RATE_LIMIT_MAX_REQUESTS} geracoes atingido para este dispositivo. Tente novamente amanha."
            )

        contador["quantidade"] += 1
        return await call_next(request)

rate_limit_middleware = RateLimitMiddleware()
