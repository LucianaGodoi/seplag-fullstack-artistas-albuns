import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import AlbumService from "../services/AlbumService";
import "./AlbumCreatePage.css";

export default function AlbumEditPage() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [nome, setNome] = useState("");
    const [anoLancamento, setAnoLancamento] = useState("");
    const [artistaNome, setArtistaNome] = useState("");
    const [files, setFiles] = useState<File[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        carregarAlbum();
    }, []);

    async function carregarAlbum() {
        if (!id) return;

        const album = await AlbumService.buscarPorId(Number(id));

        setNome(album.nome);
        setAnoLancamento(album.anoLancamento || "");
        setArtistaNome(album.artista?.nome);
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setLoading(true);

        try {

            await AlbumService.atualizar(Number(id), {
                nome,
                anoLancamento: Number(anoLancamento)
            });

            if (files.length > 0) {
                await AlbumService.uploadCapas(Number(id), files);
            }

            alert("Álbum atualizado com sucesso!");
            navigate(-1);

        } catch {
            alert("Erro ao atualizar álbum");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="album-page">

            <h2 className="album-title">
                Editar Álbum
            </h2>

            <div className="album-card">

                <form onSubmit={handleSubmit}>

                    <input
                        className="album-field"
                        placeholder="Nome do álbum"
                        value={nome}
                        onChange={e => setNome(e.target.value)}
                        required
                    />

                    <input
                        className="album-field"
                        type="number"
                        placeholder="Ano de lançamento"
                        value={anoLancamento}
                        onChange={e => setAnoLancamento(e.target.value)}
                    />

                    <div style={{ marginBottom: 12 }}>
                        <strong>Artista:</strong> {artistaNome}
                    </div>

                    <input
                        className="album-file"
                        type="file"
                        multiple
                        accept="image/*"
                        onChange={e => setFiles(Array.from(e.target.files || []))}
                    />

                    <div className="album-actions">

                        <button
                            type="button"
                            className="btn-secondary"
                            onClick={() => navigate(-1)}
                        >
                            Voltar
                        </button>

                        <button
                            type="submit"
                            disabled={loading}
                            className="btn-primary"
                        >
                            {loading ? "Salvando..." : "Salvar"}
                        </button>

                    </div>

                </form>

            </div>

        </div>
    );
}
