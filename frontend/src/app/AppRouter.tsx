import { Navigate, Route, Routes } from "react-router";
import { AppLayout } from "../common/layout/AppLayout";
import { BusinessNeedsPage } from "../pages/BusinessNeedsPage";
import { BusinessOffersPage } from "../pages/BusinessOffersPage";
import { CompaniesPage } from "../pages/CompaniesPage";
import { CompanyDetailsPage } from "../pages/CompanyDetailsPage";
import { CreateBusinessNeedPage } from "../pages/CreateBusinessNeedPage";
import { CreateCompanyPage } from "../pages/CreateCompanyPage";
import { DashboardPage } from "../pages/DashboardPage";
import { MatchingPage } from "../pages/MatchingPage";
import { NotFoundPage } from "../pages/NotFoundPage";
import { BusinessNeedDetailsPage } from "../pages/BusinessNeedDetailsPage";

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

                <Route
                    path="/companies"
                    element={<CompaniesPage />}
                />

                <Route
                    path="/companies/new"
                    element={<CreateCompanyPage />}
                />

                <Route
                    path="/companies/:companyId"
                    element={<CompanyDetailsPage />}
                />

                <Route
                    path="/companies/:companyId/needs/new"
                    element={<CreateBusinessNeedPage />}
                />

                <Route
                    path="/business-needs/:businessNeedId"
                    element={<BusinessNeedDetailsPage />}
                />

                <Route
                    path="/needs"
                    element={<BusinessNeedsPage />}
                />

                <Route
                    path="/business-needs"
                    element={<BusinessNeedsPage />}
                />

                <Route
                    path="/offers"
                    element={<BusinessOffersPage />}
                />

                <Route
                    path="/matching"
                    element={<MatchingPage />}
                />

                <Route
                    path="*"
                    element={<NotFoundPage />}
                />
            </Route>
        </Routes>
    );
}
