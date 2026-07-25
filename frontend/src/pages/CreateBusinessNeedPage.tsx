import {
    Alert,
    Box,
    CircularProgress,
    Container,
} from "@mui/material";
import {
    Navigate,
    useNavigate,
    useParams,
} from "react-router";
import { BusinessNeedForm } from "../features/businessNeed/components/BusinessNeedForm";
import { useCompanies } from "../features/company/hooks/useCompanies";

export function CreateBusinessNeedPage() {
    const { companyId } = useParams();
    const navigate = useNavigate();

    const companiesQuery = useCompanies();

    const parsedCompanyId = Number(companyId);

    if (
        !Number.isInteger(parsedCompanyId)
        || parsedCompanyId <= 0
    ) {
        return <Navigate to="/companies" replace />;
    }

    if (companiesQuery.isPending) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    py: 8,
                }}
            >
                <CircularProgress />
            </Box>
        );
    }

    if (companiesQuery.isError) {
        return (
            <Container maxWidth="md">
                <Alert severity="error">
                    Failed to load company information.
                </Alert>
            </Container>
        );
    }

    const company = companiesQuery.data.find(
        (loadedCompany) =>
            loadedCompany.id === parsedCompanyId,
    );

    if (!company) {
        return (
            <Container maxWidth="md">
                <Alert severity="warning">
                    The selected company does not exist.
                </Alert>
            </Container>
        );
    }

    return (
        <Container
            maxWidth="lg"
            sx={{ py: 4 }}
        >
            <BusinessNeedForm
                companyId={company.id}
                companyName={company.name}
                specializations={company.specializations}
                onSuccess={() => {
                    navigate("/companies");
                }}
            />
        </Container>
    );
}
