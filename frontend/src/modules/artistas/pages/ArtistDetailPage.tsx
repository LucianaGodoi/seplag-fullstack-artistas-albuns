import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import ArtistaService from "../services/ArtistaService.ts";
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
    const [artista, setArtista] = useState<Artista | null>(null);
    const [albuns, setAlbuns] = useState<Album[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        carregarDados();
    }, []);

    async function carregarDados() {
        if (!id) return;

        const artistaResp = await ArtistaService.buscarPorId(Number(id));
        const albunsResp = await AlbumService.listarPorArtista(Number(id));

        setArtista(artistaResp);
        setAlbuns(albunsResp.content);
        console.log(albunsResp.content);
    }

    return (
        <div style={{ padding: 40 }}>
            <h2>{artista?.nome}</h2>
            <button
                onClick={() => navigate(`/albuns/novo?artistaId=${artista?.id}`)}
            >
                + Novo Álbum
            </button>
            <h3>Álbuns</h3>

            {albuns.length === 0 && (
                <p>Este artista ainda não possui álbuns cadastrados.</p>
            )}

            <div className="album-grid">
                {albuns.map(album => {
                    console.log(album);
                    const capa = album.capas?.[0]?.url;
                    return (
                        <div key={album.id} className="album-card">
                            {capa ? (
                                <img src={capa} alt={album.nome} />
                            ) : (
                                <div className="no-cover">Sem capa</div>
                            )}

                            <strong>{album.nome}</strong>
                            <span>{album.anoLancamento}</span>
                        </div>
                    );
                })}
            </div>

        </div>

    );
}
