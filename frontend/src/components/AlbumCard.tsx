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
   // const capa = album.capas?.[0]?.url;

    return (
        // <div className="album-card">
        //     {capa ? (
        //         <img src={capa} alt={album.titulo} />
        //     ) : (
        //         <div className="no-cover">Sem Capa</div>
        //     )}
        //
        //     <strong>{album.titulo}</strong>
        //     <span>{album.anoLancamento}</span>
        // </div>

        <div className="album-card">

            <div className="album-cover">
                {album.capas && album.capas.length > 0 ? (
                    <img
                        src={album.capas[0].url}
                        alt={album.titulo}
                        onError={(e) => {
                            (e.currentTarget as HTMLImageElement).src = "/no-cover.png";
                        }}
                    />
                ) : (
                    <span>Sem capa</span>
                )}
            </div>

            <div className="album-title">{album.titulo}</div>

            {album.anoLancamento && (
                <div className="album-year">{album.anoLancamento}</div>
            )}

        </div>

);
}
