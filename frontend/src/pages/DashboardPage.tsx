import BusinessIcon from "@mui/icons-material/Business";
import HandshakeIcon from "@mui/icons-material/Handshake";
import SearchIcon from "@mui/icons-material/Search";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Stack,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";

export function DashboardPage() {
    const navigate = useNavigate();

    return (
        <Stack spacing={5}>
            <Box
                sx={{
                    px: { xs: 0, md: 1 },
                    py: { xs: 1, md: 2 },
                }}
            >
                <Typography
                    variant="h2"
                    component="h1"
                    sx={{
                        fontWeight: 800,
                        letterSpacing: "-0.04em",
                        maxWidth: 760,
                        fontSize: { xs: "2.25rem", md: "3.4rem" },
                    }}
                >
                    Find the right business partner
                </Typography>

                <Typography
                    variant="h6"
                    color="text.secondary"
                    sx={{
                        mt: 2,
                        maxWidth: 820,
                        fontWeight: 400,
                    }}
                >
                    Create business needs and offers, compare compatibility,
                    and discover partnerships based on shared expectations.
                </Typography>
            </Box>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: {
                        xs: "1fr",
                        md: "repeat(3, 1fr)",
                    },
                    gap: 3,
                }}
            >
                <Card
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                    }}
                >
                    <CardContent sx={{ flexGrow: 1 }}>
                        <Box
                            sx={{
                                display: "inline-flex",
                                alignItems: "center",
                                justifyContent: "center",
                                width: 48,
                                height: 48,
                                borderRadius: 3,
                                bgcolor: "primary.main",
                                color: "primary.contrastText",
                                mb: 2,
                            }}
                        >
                            <BusinessIcon />
                        </Box>

                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Companies
                        </Typography>

                        <Typography color="text.secondary" sx={{ mt: 1 }}>
                            Manage company profiles, industries, specializations,
                            and business capabilities.
                        </Typography>
                    </CardContent>

                    <CardActions sx={{ px: 2, pb: 2 }}>
                        <Button
                            variant="contained"
                            onClick={() => navigate("/needs")}
                        >
                            Open companies
                        </Button>
                    </CardActions>
                </Card>

                <Card
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                    }}
                >
                    <CardContent sx={{ flexGrow: 1 }}>
                        <Box
                            sx={{
                                display: "inline-flex",
                                alignItems: "center",
                                justifyContent: "center",
                                width: 48,
                                height: 48,
                                borderRadius: 3,
                                bgcolor: "primary.main",
                                color: "primary.contrastText",
                                mb: 2,
                            }}
                        >
                            <SearchIcon />
                        </Box>

                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Needs and offers
                        </Typography>

                        <Typography color="text.secondary" sx={{ mt: 1 }}>
                            Define what companies are looking for and what they can
                            provide to potential partners.
                        </Typography>
                    </CardContent>

                    <CardActions sx={{ px: 2, pb: 2 }}>
                        <Button
                            variant="contained"
                            onClick={() => navigate("/companies")}
                        >
                            Open needs and offers
                        </Button>
                    </CardActions>
                </Card>

                <Card
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                    }}
                >
                    <CardContent sx={{ flexGrow: 1 }}>
                        <Box
                            sx={{
                                display: "inline-flex",
                                alignItems: "center",
                                justifyContent: "center",
                                width: 48,
                                height: 48,
                                borderRadius: 3,
                                bgcolor: "primary.main",
                                color: "primary.contrastText",
                                mb: 2,
                            }}
                        >
                            <HandshakeIcon />
                        </Box>

                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Matching
                        </Typography>

                        <Typography color="text.secondary" sx={{ mt: 1 }}>
                            Generate explainable matches based on compatibility,
                            preferences, and matching scores.
                        </Typography>
                    </CardContent>

                    <CardActions sx={{ px: 2, pb: 2 }}>
                        <Button
                            variant="contained"
                            onClick={() => navigate("/matching")}
                        >
                            Run matching
                        </Button>
                    </CardActions>
                </Card>
            </Box>
        </Stack>
    );
}
