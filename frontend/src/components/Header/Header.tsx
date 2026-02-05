import { useNavigate } from "react-router-dom";
import "./Header.css";

export default function Header() {
    const navigate = useNavigate();

    function handleLogout() {
        navigate("/login");
    }

    return (
        <header className="app-header">
            <div className="header-left">
                🎵 Artistas & Albums
            </div>

            <div className="header-right">
                <button className="logout-btn" onClick={handleLogout}>
                    Sair
                </button>
            </div>
        </header>
    );
}
