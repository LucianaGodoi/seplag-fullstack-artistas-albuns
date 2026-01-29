import { useEffect, useState } from "react";
import ArtistaService from "../services/ArtistaService";
import type { Artista } from "../services/ArtistaService";
import { useNavigate } from "react-router-dom";

export default function ArtistsListPage() {

    const [artistas, setArtistas] = useState<Artista[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    async function carregarArtistas() {
        try {
            const response = await ArtistaService.listar();
            setArtistas(response.content ?? response);
        } catch (error) {
            alert("Erro ao carregar artistas");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        carregarArtistas();
    }, []);

    if (loading) {
        return <p>Carregando artistas...</p>;
    }

    return (
        <div style={{ padding: 40 }}>
            <h2>Artistas</h2>

            {artistas.length === 0 && <p>Nenhum artista encontrado.</p>}

            <ul>
                {artistas.map(artista => (
                    <li
                        style={{ cursor: "pointer" }}
                        onClick={() => navigate(`/artistas/${artista.id}`)}
                    >
                        {artista.nome}
                    </li>
                ))}
            </ul>
        </div>
    );
}
