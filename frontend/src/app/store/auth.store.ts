import { BehaviorSubject } from 'rxjs';

export interface AuthState {
    token: string | null;
    refreshToken: string | null;
    isAuthenticated: boolean;
}

const initialState: AuthState = {
    token: null,
    refreshToken: null,
    isAuthenticated: false
};

class AuthStore {
    private subject = new BehaviorSubject<AuthState>(initialState);
    state$ = this.subject.asObservable();

    get value() {
        return this.subject.value;
    }

    setAuth(token: string, refreshToken: string) {
        this.subject.next({
            token,
            refreshToken,
            isAuthenticated: true
        });
    }

    clear() {
        this.subject.next(initialState);
    }
}

export const authStore = new AuthStore();
