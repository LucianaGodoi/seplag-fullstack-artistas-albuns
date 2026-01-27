import { BehaviorSubject } from "rxjs";
import AuthService from "../../modules/auth/services/AuthService";
import type { LoginRequest } from "../../modules/auth/services/AuthService";

class AuthFacade {

    private authenticatedSubject = new BehaviorSubject<boolean>(
        !!localStorage.getItem("accessToken")
    );

    authenticated$ = this.authenticatedSubject.asObservable();

    async login(data: LoginRequest) {
        const result = await AuthService.login(data);

        localStorage.setItem("accessToken", result.accessToken);
        localStorage.setItem("refreshToken", result.refreshToken);

        this.authenticatedSubject.next(true);
    }

    logout() {
        localStorage.clear();
        this.authenticatedSubject.next(false);
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem("accessToken");
    }
}

export default new AuthFacade();
