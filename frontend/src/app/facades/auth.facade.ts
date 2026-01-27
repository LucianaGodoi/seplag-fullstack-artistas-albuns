import { api } from '../../services/api';
import { authStore } from '../store/auth.store';

class AuthFacade {

    async login(username: string, password: string) {
        const { data } = await api.post('/auth/login', {
            username,
            password
        });

        authStore.setAuth(
            data.accessToken,
            data.refreshToken
        );
    }

    logout() {
        authStore.clear();
    }
}

export const authFacade = new AuthFacade();
