import { Outlet, useLocation } from "react-router-dom";
import Header from "../Header/Header";

export default function Layout() {
    const location = useLocation();

    const isLogin = location.pathname === "/login";

    return (
        <>
            {!isLogin && <Header />}

            <div style={{ paddingTop: isLogin ? 0 : 80 }}>
                <Outlet />
            </div>
        </>
    );
}
