import { api } from "../../../services/api";

class AlbumService {

    async listarPorArtista(artistaId: number) {
        const { data } = await api.get(
            `/albuns?artistaId=${artistaId}`
        );

        return data;
    }

}

export default new AlbumService();
