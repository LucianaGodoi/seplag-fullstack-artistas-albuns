import { BrowserRouter, Routes, Route } from "react-router-dom";

import LoginPage from "../../modules/auth/pages/LoginPage";
import ArtistsListPage from "../../modules/artistas/pages/ArtistsListPage";
import ArtistDetailPage from "../../modules/artistas/pages/ArtistDetailPage";
import AlbumCreatePage from "../../modules/albuns/pages/AlbumCreatePage";
import ArtistaFormPage from "../../modules/artistas/pages/ArtistaFormPage";
import AlbumEditPage from "../../modules/albuns/pages/AlbumEditPage";

import PrivateRoute from "./PrivateRoute";
import Layout from "../../components/Layout/Layout";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route element={<Layout />}>
                    <Route
                        path="/"
                        element={
                            <PrivateRoute>
                                <ArtistsListPage />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/artistas"
                        element={
                            <PrivateRoute>
                                <ArtistsListPage />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/artistas/novo"
                        element={
                            <PrivateRoute>
                                <ArtistaFormPage />
                            </PrivateRoute>
                        }
                    />

                    <Route
                        path="/artistas/:id"
                        element={
                            <PrivateRoute>
                                <ArtistDetailPage />
                            </PrivateRoute>
                        }
                    />

                    <Route
                        path="/albuns/novo"
                        element={
                            <PrivateRoute>
                                <AlbumCreatePage />
                            </PrivateRoute>
                        }
                    />

                    <Route
                        path="/albuns/:id/editar"
                        element={
                            <PrivateRoute>
                                <AlbumEditPage />
                            </PrivateRoute>
                        }
                    />

                </Route>

            </Routes>

        </BrowserRouter>
    );
}
