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
            params: {
                page: params?.page ?? 0,
                size: params?.size ?? 50,
                nome: params?.nome,
                sort: params?.sort
            }
        });

        return data;
    }


    async buscarPorId(id: number) {
        const { data } = await api.get(`/artistas/${id}`);
        return data;
    }

    async criar(payload: { nome: string }) {
        const { data } = await api.post("/artistas", payload);
        return data;
    }

}

export default new ArtistaService();
