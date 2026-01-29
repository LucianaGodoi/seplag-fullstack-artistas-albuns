import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "../../modules/auth/pages/LoginPage";
import ArtistsListPage from "../../modules/artistas/pages/ArtistsListPage";
import ArtistDetailPage from "../../modules/artistas/pages/ArtistDetailPage";
import AlbumCreatePage from "../../modules/albuns/pages/AlbumCreatePage";


import PrivateRoute from "./PrivateRoute";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>

                <Route path="/login" element={<LoginPage />} />
                <Route path="/artistas/:id" element={<ArtistDetailPage />} />
                <Route path="/albuns/novo" element={<AlbumCreatePage />} />

                <Route
                    path="/"
                    element={
                        <PrivateRoute>
                            <ArtistsListPage />
                        </PrivateRoute>
                    }
                />

            </Routes>
        </BrowserRouter>
    );
}
