from google import genai
from app.config import settings

client = genai.Client(
    api_key=settings.GEMINI_API_KEY,
    http_options={'headers': {'x-goog-api-key': settings.GEMINI_API_KEY}}
)
class IaService:
    def gerar_metodologia(self, hiperfoco: str, conteudo: str) -> str:
        prompt = self._construir_prompt(hiperfoco, conteudo)
        try:
            response = client.models.generate_content(
                model=settings.GEMINI_MODEL,
                contents=prompt
            )
            return response.text
        except Exception as e:
            raise RuntimeError(f"Falha ao chamar a IA (Gemini): {str(e)}")

    def _construir_prompt(self, hiperfoco: str, conteudo: str) -> str:
        return f"""
        Voce e um assistente pedagogico especializado em educacao inclusiva e autismo (TEA).
        Suas respostas devem ser praticas, ludicas, acolhedoras e detalhadas.

        Crie uma metodologia de ensino para uma crianca atipica (TEA) que tem hiperfoco em: "{hiperfoco}".
        O conteudo/habilidade a ser trabalhada e: "{conteudo}".

        A resposta deve seguir este formato em Markdown:

        ## Objetivo Pedagogico
        (1-2 frases sobre o que se espera alcancar)

        ## Estrategia de Intervencao
        (Explicar como conectar o hiperfoco ao conteudo, com uma narrativa ludica)

        ## Atividades Sugeridas
        (Listar 3 a 5 atividades concretas, com materiais e duracao)

        ## Adaptacoes e Cuidados
        (Sugestoes de adaptacoes sensoriais, comunicacao alternativa, etc.)

        ## Como Avaliar
        (Indicadores simples de progresso)

        Seja especifico, criativo e acolhedor. Use linguagem clara para professores.
        """

ia_service = IaService()
