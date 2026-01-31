import { createContext, useContext, useEffect, useState } from "react";
import authFacade from "../../../app/facades/AuthFacade";

interface AuthContextType {
    isAuthenticated: boolean;
    login: () => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export function AuthProvider({ children }: { children: React.ReactNode }) {

    const [isAuthenticated, setIsAuthenticated] = useState(
        authFacade.isAuthenticated()
    );

    useEffect(() => {
        const sub = authFacade.authenticated$.subscribe(value => {
            setIsAuthenticated(value);
        });

        return () => sub.unsubscribe();
    }, []);

    function login() {
        setIsAuthenticated(true);
    }

    function logout() {
        authFacade.logout();
        setIsAuthenticated(false);
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
