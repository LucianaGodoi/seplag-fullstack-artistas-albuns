const ACCESS = "access_token";
const REFRESH = "refresh_token";

class TokenStorage {

    setTokens(access: string, refresh: string) {
        localStorage.setItem(ACCESS, access);
        localStorage.setItem(REFRESH, refresh);
    }

    getAccessToken() {
        return localStorage.getItem(ACCESS);
    }

    getRefreshToken() {
        return localStorage.getItem(REFRESH);
    }

    clear() {
        localStorage.clear();
    }

    isAuthenticated() {
        return !!this.getAccessToken();
    }
}

export default new TokenStorage();
