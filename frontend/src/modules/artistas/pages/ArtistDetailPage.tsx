import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ArtistaService from "../services/ArtistaService";
import AlbumService from "../../albuns/services/AlbumService";
import "./ArtistDetailPage.css";

interface Artista {
    id: number;
    nome: string;
}

interface AlbumCover {
    id: number;
    url: string;
}

interface Album {
    id: number;
    nome: string;
    anoLancamento: number | null;
    capas?: AlbumCover[];
}

export default function ArtistDetailPage() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [artista, setArtista] = useState<Artista | null>(null);
    const [albuns, setAlbuns] = useState<Album[]>([]);

    useEffect(() => {
        carregarDados();
    }, []);

    async function carregarDados() {
        if (!id) return;

        const artistaResp = await ArtistaService.buscarPorId(Number(id));
        const albunsResp = await AlbumService.listarPorArtista(Number(id));

        setArtista(artistaResp);
        setAlbuns(albunsResp.content);
    }

    return (

        <div className="detail-container">
            <div className="detail-header">
                <h2 className="detail-title">
                    {artista?.nome}
                </h2>
                <div className="detail-actions">
                    <button
                        className="btn-secondary"
                        onClick={() => navigate("/artistas")}
                    >
                        Voltar
                    </button>
                    <button
                        className="btn-primary"
                        onClick={() =>
                            navigate(`/albuns/novo?artistaId=${artista?.id}`)
                        }
                    >
                        + Novo Álbum
                    </button>
                </div>
            </div>

            <h3>Álbuns</h3>

            {albuns.length === 0 && (
                <p>Este artista ainda não possui álbuns cadastrados.</p>
            )}

            <div className="album-grid">

                {albuns.map(album => {

                    const capa = album.capas?.[0]?.url;

                    return (
                        <div
                            key={album.id}
                            className="album-card"
                            onClick={() => navigate(`/albuns/${album.id}/editar`)}
                        >

                            {capa ? (
                                <img src={capa} alt={album.nome} />
                            ) : (
                                <div className="no-cover">
                                    Sem capa
                                </div>
                            )}

                            <div className="album-name">
                                {album.nome}
                            </div>

                            {album.anoLancamento && (
                                <div className="album-year">
                                    {album.anoLancamento}
                                </div>
                            )}

                        </div>
                    );

                })}

            </div>


        </div>
    );
}
