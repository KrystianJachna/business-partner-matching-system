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
                bgcolor: "background.default",
            }}
        >
            <AppBar position="static">
                <Toolbar>
                    <BusinessCenterIcon sx={{ mr: 1.5 }} />

                    <Typography variant="h6" component="div">
                        Business Partner Matching
                    </Typography>
                </Toolbar>
            </AppBar>

            <Container maxWidth="xl" sx={{ py: 4 }}>
                <Outlet />
            </Container>
        </Box>
    );
}
