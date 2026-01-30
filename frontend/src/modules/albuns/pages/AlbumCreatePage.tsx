import { useEffect, useState } from "react";
import { useNavigate,useSearchParams } from "react-router-dom";
import AlbumService from "../services/AlbumService";
import ArtistaService from "../../artistas/services/ArtistaService";

interface Artista {
    id: number;
    nome: string;
}

export default function AlbumCreatePage() {

    const navigate = useNavigate();

    const [nome, setNome] = useState("");
    const [anoLancamento, setAnoLancamento] = useState("");
    const [artistaId, setArtistaId] = useState<number>();
    const [artistas, setArtistas] = useState<Artista[]>([]);
    const [files, setFiles] = useState<File[]>([]);
    const [loading, setLoading] = useState(false);
    const [searchParams] = useSearchParams();
    const [artistaNome, setArtistaNome] = useState<string>();



    // useEffect(() => {
    //     carregarArtistas();
    // }, []);
    //
    // useEffect(() => {
    //     const id = searchParams.get("artistaId");
    //
    //     if (id) {
    //         setArtistaId(Number(id));
    //     }
    // }, []);

    useEffect(() => {
        async function init() {
            const id = searchParams.get("artistaId");

            if (id) {
                const artistaIdNum = Number(id);
                setArtistaId(artistaIdNum);

                const data = await ArtistaService.listar({ page: 0, size: 100 });
                setArtistas(data.content);

                const artista = data.content.find(
                    (a: Artista) => a.id === artistaIdNum
                );

                if (artista) {
                    setArtistaNome(artista.nome);
                }
            } else {
                const data = await ArtistaService.listar({ page: 0, size: 100 });
                setArtistas(data.content);
            }
        }

        init();
    }, []);


    // async function carregarArtistas() {
    //     const data = await ArtistaService.listar({ page: 0, size: 100 });
    //     setArtistas(data.content);
    //     const id = searchParams.get("artistaId");
    //
    //     if (id) {
    //         const artista = data.content.find(
    //             (a: Artista) => a.id === Number(id)
    //         );
    //
    //         if (artista) {
    //             setArtistaNome(artista.nome);
    //         }
    //     }
    // }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        if (!artistaId) {
            alert("Artista não informado");
            return;
        }
        setLoading(true);

        try {

            const album = await AlbumService.criar({
                nome,
                anoLancamento: Number(anoLancamento),
                artistaId
            });

            if (files.length > 0) {
                await AlbumService.uploadCapas(album.id, files);
            }

            alert("Álbum cadastrado com sucesso!");
            navigate(`/artistas/${artistaId}`);

        } catch (err) {
            alert("Erro ao cadastrar álbum");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ padding: 40, maxWidth: 500 }}>

            <h2>Cadastrar Álbum</h2>

            <form onSubmit={handleSubmit}>

                <input
                    placeholder="Nome do álbum"
                    value={nome}
                    onChange={e => setNome(e.target.value)}
                    required
                />

                <input
                    type="number"
                    placeholder="Ano de lançamento"
                    value={anoLancamento}
                    onChange={e => setAnoLancamento(e.target.value)}
                    required
                />

                {artistaId ? (
                    <div style={{ marginBottom: 12 }}>
                        <strong>Artista:</strong> {artistaNome}
                    </div>
                ) : (
                    <select
                        value={artistaId}
                        onChange={e => setArtistaId(Number(e.target.value))}
                        required
                    >
                        <option value="">Selecione um artista</option>

                        {artistas.map(a => (
                            <option key={a.id} value={a.id}>
                                {a.nome}
                            </option>
                        ))}
                    </select>
                )}


                <input
                    type="file"
                    multiple
                    accept="image/*"
                    onChange={e => setFiles(Array.from(e.target.files || []))}
                />

                <button type="submit" disabled={loading}>
                    {loading ? "Salvando..." : "Salvar"}
                </button>

            </form>

        </div>
    );
}
