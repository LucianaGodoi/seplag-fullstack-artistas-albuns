type Album = {
    id: number;
    titulo: string;
    anoLancamento: number;
    capas?: { url: string }[];
};

type Props = {
    album: Album;
};

export function AlbumCard({ album }: Props) {
    const capa = album.capas?.[0]?.url;

    return (
        <div className="album-card">
            {capa ? (
                <img src={capa} alt={album.titulo} />
            ) : (
                <div className="no-cover">Sem Capa</div>
            )}

            <strong>{album.titulo}</strong>
            <span>{album.anoLancamento}</span>
        </div>
    );
}
