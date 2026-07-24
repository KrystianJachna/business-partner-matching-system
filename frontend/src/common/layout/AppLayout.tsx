import BusinessCenterIcon from "@mui/icons-material/BusinessCenter";
import {
    AppBar,
    Box,
    Button,
    Container,
    Stack,
    Toolbar,
    Typography,
} from "@mui/material";
import { NavLink, Outlet } from "react-router";
import { navigationItems } from "../navigation/navigationItems";

export function AppLayout() {
    return (
        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "background.default",
            }}
        >
            <AppBar
                position="static"
                elevation={0}
                sx={{
                    borderBottom: 1,
                    borderColor: "rgba(255, 255, 255, 0.08)",
                }}
            >
                <Container maxWidth="xl">
                    <Toolbar
                        disableGutters
                        sx={{
                            minHeight: 68,
                            gap: 4,
                        }}
                    >
                        <Stack
                            direction="row"
                            spacing={1.5}
                            sx={{
                                alignItems: "center",
                                flexShrink: 0,
                            }}
                        >
                            <BusinessCenterIcon sx={{ fontSize: 22 }} />

                            <Typography
                                variant="h6"
                                component="div"
                                sx={{
                                    fontWeight: 700,
                                    letterSpacing: "-0.01em",
                                }}
                            >
                                Business Partner Matching
                            </Typography>
                        </Stack>

                        <Stack
                            component="nav"
                            direction="row"
                            spacing={0.5}
                            sx={{
                                ml: "auto",
                                alignItems: "center",
                            }}
                        >
                            {navigationItems.map((item) => (
                                <NavLink
                                    key={item.path}
                                    to={item.path}
                                    end={item.path === "/dashboard"}
                                    style={{
                                        textDecoration: "none",
                                        color: "inherit",
                                    }}
                                >
                                    {({ isActive }) => (
                                        <Button
                                            color="inherit"
                                            sx={{
                                                minWidth: "auto",
                                                px: 1.75,
                                                py: 0.75,
                                                borderRadius: 2,
                                                textTransform: "none",
                                                fontSize: "0.9rem",
                                                fontWeight: isActive ? 700 : 500,
                                                color: isActive
                                                    ? "common.white"
                                                    : "rgba(255, 255, 255, 0.76)",
                                                bgcolor: isActive
                                                    ? "rgba(255, 255, 255, 0.14)"
                                                    : "transparent",
                                                "&:hover": {
                                                    color: "common.white",
                                                    bgcolor: "rgba(255, 255, 255, 0.09)",
                                                },
                                            }}
                                        >
                                            {item.label}
                                        </Button>
                                    )}
                                </NavLink>
                            ))}
                        </Stack>
                    </Toolbar>
                </Container>
            </AppBar>

            <Container maxWidth="lg" sx={{ py: 6 }}>
                <Outlet />
            </Container>
        </Box>
    );
}
