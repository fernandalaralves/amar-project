"use client";

import { useState } from "react";
import ReactMarkdown from "react-markdown";
import { jsPDF } from "jspdf";

function getDeviceId() {
  if (typeof window === "undefined") return "";

  let id = window.localStorage.getItem("amar_device_id");
  if (!id) {
    id = window.crypto.randomUUID();
    window.localStorage.setItem("amar_device_id", id);
  }

  return id;
}

export default function Home() {
  const [hiperfoco, setHiperfoco] = useState("");
  const [conteudo, setConteudo] = useState("");
  const [resultado, setResultado] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [deviceId] = useState(getDeviceId);
  const [erro, setErro] = useState<string | null>(null);

  const gerarMetodologia = async () => {
    if (!hiperfoco || !conteudo) {
      setErro("Preencha todos os campos!");
      return;
    }
    
    setLoading(true);
    setResultado(null);
    setErro(null);

    try {
      const response = await fetch("http://localhost:8000/api/metodologia/gerar", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Device-Id": deviceId,
        },
        body: JSON.stringify({ hiperfoco, conteudo, deviceId }),
      });

      const data: {
        sucesso?: boolean;
        sugestao?: string;
        mensagem?: string;
        detail?: string;
      } = await response.json();

      if (!response.ok) {
        throw new Error(data.detail || data.mensagem || "Erro ao gerar metodologia.");
      }

      if (data.sucesso) {
        setResultado(data.sugestao || "");
      } else {
        throw new Error(data.mensagem || "Erro desconhecido.");
      }
    } catch (error: unknown) {
      setErro(error instanceof Error ? error.message : "Erro ao gerar metodologia.");
    } finally {
      setLoading(false);
    }
  };

  const exportarPDF = () => {
    if (!resultado) return;
    const doc = new jsPDF();
    
    // Configurações básicas do PDF
    doc.setFont("helvetica", "bold");
    doc.setFontSize(18);
    doc.text("A.M.A.R - Sugestão de Intervenção", 15, 20);
    
    doc.setFont("helvetica", "normal");
    doc.setFontSize(12);
    
    // Divide o texto em linhas para caber no PDF
    const linhas = doc.splitTextToSize(resultado, 180);
    doc.text(linhas, 15, 35);
    
    doc.save("metodologia-amar.pdf");
  };

  const copiarTexto = () => {
    if (resultado) {
      navigator.clipboard.writeText(resultado);
      alert("Texto copiado!");
    }
  };

  return (
    <main className="min-h-screen flex flex-col items-center py-10 px-4 font-sans">
      {/* Header */}
      <header className="flex items-center gap-2 mb-8 w-full max-w-3xl">
        <div className="w-10 h-10 bg-blue-500 rounded-full flex items-center justify-center text-white font-bold text-xl">
          A
        </div>
        <div>
          <h1 className="text-xl font-bold text-gray-800">A.M.A.R</h1>
          <p className="text-xs text-gray-500">Assistência e Mediação no Aprendizado do Espectro Autista</p>
        </div>
      </header>

      {/* Card Principal */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 w-full max-w-3xl">
        <h2 className="text-2xl font-bold text-gray-800 mb-2">Como posso ajudar sua aula hoje?</h2>
        <p className="text-sm text-gray-500 mb-6">Transforme os interesses do aluno em possibilidades de aprendizagem.</p>

        {/* Inputs */}
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">Interesse ou Hiperfoco do aluno</label>
            <input 
              type="text" 
              value={hiperfoco}
              onChange={(e) => setHiperfoco(e.target.value)}
              placeholder="Ex: Dinossauros, Astronomia, Trens..." 
              className="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
            />
          </div>
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">Conteúdo ou Habilidade a ser trabalhada</label>
            <input 
              type="text" 
              value={conteudo}
              onChange={(e) => setConteudo(e.target.value)}
              placeholder="Ex: Cronologia da História do Brasil, Frações..." 
              className="w-full p-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 text-gray-900"
            />
          </div>
        </div>

        {/* Botão Gerar */}
        <button 
          onClick={gerarMetodologia}
          disabled={loading}
          className="w-full mt-6 bg-[#2B9BDE] hover:bg-blue-600 text-white font-semibold py-3 rounded-lg flex items-center justify-center gap-2 transition-colors disabled:opacity-70"
        >
          {loading ? "Gerando Metodologia..." : "Gerar Metodologia"}
        </button>

        {/* Mensagem de Erro */}
        {erro && (
          <div className="mt-4 p-3 bg-red-100 text-red-700 rounded-lg text-sm">
            {erro}
          </div>
        )}

        {/* Resultado */}
        {resultado && (
          <div className="mt-8 bg-[#E8F9F1] border border-[#BEE3D6] rounded-xl p-6">
            <h3 className="text-lg font-bold text-gray-800 mb-3">Sugestão de Intervenção A.M.A.R</h3>
            
            <div className="text-gray-700 text-sm leading-relaxed mb-4 prose prose-sm max-w-none">
              <ReactMarkdown>{resultado}</ReactMarkdown>
            </div>
            
            <div className="flex gap-3">
              <button 
                onClick={copiarTexto}
                className="bg-[#18C298] hover:bg-[#15A685] text-white text-sm font-semibold px-4 py-2 rounded-lg transition-colors"
              >
                Copiar Texto
              </button>
              <button 
                onClick={exportarPDF}
                className="bg-white border border-gray-300 text-gray-700 text-sm font-semibold px-4 py-2 rounded-lg hover:bg-gray-50 transition-colors flex items-center gap-2"
              >
                Exportar PDF
              </button>
            </div>
          </div>
        )}
      </div>
    </main>
  );
}