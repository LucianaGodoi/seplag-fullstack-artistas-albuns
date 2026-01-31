import axios from "axios";
import AuthService from "../modules/auth/services/AuthService";
import TokenStorage from "../modules/auth/services/TokenStorage";

const httpClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        "Content-Type": "application/json"
    }
});

// REQUEST
httpClient.interceptors.request.use(config => {
    const token = TokenStorage.getAccessToken();

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

// RESPONSE
httpClient.interceptors.response.use(
    response => response,

    async error => {
        if (error.response?.status === 401) {
            try {
                await AuthService.refresh();

                const token = TokenStorage.getAccessToken();
                error.config.headers.Authorization = `Bearer ${token}`;

                return httpClient(error.config);

            } catch {
                AuthService.logout();
                window.location.href = "/login";
            }
        }

        return Promise.reject(error);
    }
);

export default httpClient;


