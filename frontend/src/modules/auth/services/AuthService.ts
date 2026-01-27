import httpClient from "../../../services/httpClient";

export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    accessToken: string;
    refreshToken: string;
}

class AuthService {

    async login(data: LoginRequest): Promise<LoginResponse> {
        const response = await httpClient.post("/auth/login", data);
        return response.data;
    }

    async refresh(refreshToken: string): Promise<LoginResponse> {
        const response = await httpClient.post("/auth/refresh", {
            refreshToken
        });
        return response.data;
    }

}

export default new AuthService();
