import { Navigate } from "react-router-dom";
import authFacade from "../../app/facades/AuthFacade";
import React from "react";

interface Props {
    children: React.ReactNode;
}

export default function PrivateRoute({ children }: Props) {

    if (!authFacade.isAuthenticated()) {
        return <Navigate to="/login" replace />;
    }

    return <>{children}</>;
}
