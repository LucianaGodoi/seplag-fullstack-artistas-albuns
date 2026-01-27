import { BehaviorSubject } from "rxjs";
import AuthService from "../../modules/auth/services/AuthService";
import type { LoginRequest } from "../../modules/auth/services/AuthService";

class AuthFacade {

    private userSubject = new BehaviorSubject<boolean>(
        !!localStorage.getItem("accessToken")
    );

    user$ = this.userSubject.asObservable();

    async login(data: LoginRequest) {
        const result = await AuthService.login(data);

        localStorage.setItem("accessToken", result.accessToken);
        localStorage.setItem("refreshToken", result.refreshToken);

        this.userSubject.next(true);
    }

    logout() {
        localStorage.clear();
        this.userSubject.next(false);
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem("accessToken");
    }
}

export default new AuthFacade();
