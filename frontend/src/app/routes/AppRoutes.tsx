import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "../../modules/auth/pages/LoginPage";
import PrivateRoute from "./PrivateRoute";

function Home() {
    return <h2>Home</h2>;
}

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>

                <Route path="/login" element={<LoginPage />} />

                <Route
                    path="/"
                    element={
                        <PrivateRoute>
                            <Home />
                        </PrivateRoute>
                    }
                />

            </Routes>
        </BrowserRouter>
    );
}
