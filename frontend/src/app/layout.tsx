import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "A.M.A.R - Assistência Pedagógica",
  description: "Transforme os interesses do aluno em possibilidades de aprendizagem.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <body className="antialiased">
        {children}
      </body>
    </html>
  );
}