import {
    Box,
    Button,
    Stack,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import { CompanyForm } from "../features/company/components/CompanyForm";

export function CreateCompanyPage() {
    const navigate = useNavigate();

    return (
        <Box>
            <Stack
                direction={{
                    xs: "column",
                    sm: "row",
                }}
                spacing={2}
                sx={{
                    justifyContent: "space-between",
                    alignItems: {
                        xs: "flex-start",
                        sm: "center",
                    },
                    mb: 4,
                }}
            >
                <Box>
                    <Typography
                        variant="h4"
                        component="h1"
                        sx={{
                            mb: 1,
                            fontWeight: 700,
                        }}
                    >
                        Add company
                    </Typography>

                    <Typography color="text.secondary">
                        Register a company that can publish business needs and
                        offers.
                    </Typography>
                </Box>

                <Button
                    variant="outlined"
                    onClick={() => navigate("/companies")}
                >
                    Back to companies
                </Button>
            </Stack>

            <CompanyForm />
        </Box>
    );
}
