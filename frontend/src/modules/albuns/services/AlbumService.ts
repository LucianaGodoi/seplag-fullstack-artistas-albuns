import { api } from "../../../services/api";

class AlbumService {

    async listarPorArtista(artistaId: number) {
        const { data } = await api.get(
            `/albuns?artistaId=${artistaId}`
        );

        return data;
    }

    async criar(payload: {
        nome: string;
        anoLancamento: number;
        artistaId?: number;
    }) {
        const { data } = await api.post("/albuns", payload);
        return data;
    }

    async uploadCapas(albumId: number, files: File[]) {
        const formData = new FormData();
        files.forEach(f => formData.append("files", f));

        await api.post(`/albuns/${albumId}/capas`, formData);
    }

}

export default new AlbumService();
