import {useEffect, useState} from "react";
import ArtistaService from "../services/ArtistaService";
import type {Artista} from "../services/ArtistaService";
import {useNavigate} from "react-router-dom";
import "./ArtistsListPage.css";

export default function ArtistsListPage() {

    const [artistas, setArtistas] = useState<Artista[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const [page, setPage] = useState(0);
    const [size] = useState(5);
    const [totalPages, setTotalPages] = useState(0);
    const [search, setSearch] = useState("");
    const [sortDir, setSortDir] = useState<"asc" | "desc">("asc");


    async function carregarArtistas() {
        try {
            const response = await ArtistaService.listar({
                page,
                size,
                nome: search,
                sort: `nome,${sortDir}`
            });

            setArtistas(response.content);
            setTotalPages(response.totalPages);

        } catch (error) {
            alert("Erro ao carregar artistas");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        carregarArtistas();
    }, [page, search, sortDir]);

    if (loading) {
        return <p>Carregando artistas...</p>;
    }

    return (

        <div style={{padding: 40}}>
            <h2>Artistas</h2>
            <div className="filters">
                <input
                    placeholder="Buscar artista..."
                    value={search}
                    onChange={e => {
                        setSearch(e.target.value);
                        setPage(0);
                    }}
                />
                <button
                    onClick={() =>
                        setSortDir(dir => dir === "asc" ? "desc" : "asc")
                    }
                >
                    Ordenar: {sortDir === "asc" ? "A → Z" : "Z → A"}
                </button>
            </div>
            <button
                className="btn-primary"
                onClick={() => navigate("/artistas/novo")}
            >
                + Novo Artista
            </button>
            {artistas.length === 0 && <p>Nenhum artista encontrado.</p>}

            <ul>
                {artistas.map(artista => (
                    <li
                        style={{cursor: "pointer"}}
                        onClick={() => navigate(`/artistas/${artista.id}`)}
                    >
                        {artista.nome}
                    </li>
                ))}
            </ul>
            <div className="pagination">
                <button
                    disabled={page === 0}
                    onClick={() => setPage(p => p - 1)}
                >
                    Anterior
                </button>
                <span>
                    Página {page + 1} de {totalPages}
                  </span>
                <button
                    disabled={page + 1 >= totalPages}
                    onClick={() => setPage(p => p + 1)}
                >
                    Próximo
                </button>
            </div>
        </div>
    );
}
