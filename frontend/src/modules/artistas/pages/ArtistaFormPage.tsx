import { useState } from "react";
import { useNavigate } from "react-router-dom";
import ArtistaService from "../services/ArtistaService";

export default function ArtistaFormPage() {

    const [nome, setNome] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    async function salvar() {
        if (!nome.trim()) {
            alert("Informe o nome do artista");
            return;
        }

        try {
            setLoading(true);

            await ArtistaService.criar({
                nome
            });

            alert("Artista cadastrado com sucesso!");

            navigate("/artistas");

        } catch (err) {
            alert("Erro ao salvar artista");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ padding: 40, maxWidth: 500 }}>

            <h2>Novo Artista</h2>

            <input
                placeholder="Nome do artista"
                value={nome}
                onChange={e => setNome(e.target.value)}
                style={{
                    width: "100%",
                    padding: 10,
                    marginBottom: 16,
                    borderRadius: 6,
                    border: "1px solid #444",
                    background: "#1f1f1f",
                    color: "#fff"
                }}
            />

            <button
                onClick={salvar}
                disabled={loading}
                className="btn-primary"
            >
                {loading ? "Salvando..." : "Salvar"}
            </button>

        </div>
    );
}
