import { Navigate, Route, Routes } from "react-router";
import { AppLayout } from "../common/layout/AppLayout";
import { DashboardPage } from "../pages/DashboardPage";

export function AppRouter() {
    return (
        <Routes>
            <Route element={<AppLayout />}>
                <Route
                    index
                    element={<Navigate to="/dashboard" replace />}
                />

                <Route
                    path="/dashboard"
                    element={<DashboardPage />}
                />
            </Route>
        </Routes>
    );
}
