import BusinessCenterIcon from "@mui/icons-material/BusinessCenter";
import {
    AppBar,
    Box,
    Container,
    Toolbar,
    Typography,
} from "@mui/material";
import { Outlet } from "react-router";

export function AppLayout() {
    return (
        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "grey.50",
            }}
        >
            <AppBar
                position="static"
                elevation={0}
                sx={{
                    borderBottom: 1,
                    borderColor: "divider",
                }}
            >
                <Container maxWidth="xl">
                    <Toolbar disableGutters>
                        <BusinessCenterIcon sx={{ mr: 1.5 }} />

                        <Typography
                            variant="h6"
                            component="div"
                            sx={{ fontWeight: 600 }}
                        >
                            Business Partner Matching
                        </Typography>
                    </Toolbar>
                </Container>
            </AppBar>

            <Container maxWidth="lg" sx={{ py: 6 }}>
                <Outlet />
            </Container>
        </Box>
    );
}
