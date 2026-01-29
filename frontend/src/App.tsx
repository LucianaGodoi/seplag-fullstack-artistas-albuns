import { useEffect } from "react";
import AppRoutes from "./app/routes/AppRoutes";
import { connectWebSocket } from "./services/websocket";

export default function App() {

    useEffect(() => {
        connectWebSocket((msg) => {
            alert(`🎵 Novo álbum cadastrado: ${msg.albumNome} - ${msg.artistaNome}`);
        });
    }, []);

    return <AppRoutes />;
}
