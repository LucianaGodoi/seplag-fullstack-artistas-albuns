import { api } from "../../../services/api";

export interface Artista {
    id: number;
    nome: string;
    totalAlbuns: number;
}

class ArtistaService {
    async listar(params?: {
        page?: number;
        size?: number;
        nome?: string;
        sort?: string;
    }) {
        const { data } = await api.get("/artistas", {
            params
        });

        return data;
    }

    async buscarPorId(id: number) {
        const { data } = await api.get(`/artistas/${id}`);
        return data;
    }
}

export default new ArtistaService();
