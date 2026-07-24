import BusinessIcon from "@mui/icons-material/Business";
import HandshakeIcon from "@mui/icons-material/Handshake";
import SearchIcon from "@mui/icons-material/Search";
import {
    Box,
    Card,
    CardContent,
    Stack,
    Typography,
} from "@mui/material";

export function DashboardPage() {
    return (
        <Stack spacing={5}>
            <Box>
                <Typography
                    variant="h3"
                    component="h1"
                    sx={{ fontWeight: 700 }}
                >
                    Find the right business partner
                </Typography>

                <Typography
                    variant="h6"
                    color="text.secondary"
                    sx={{
                        mt: 2,
                        maxWidth: 1000,
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
                <Card>
                    <CardContent>
                        <BusinessIcon
                            color="primary"
                            sx={{ fontSize: 40, mb: 2 }}
                        />

                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Companies
                        </Typography>

                        <Typography color="text.secondary" sx={{ mt: 1 }}>
                            Manage company profiles, industries, specializations,
                            and business capabilities.
                        </Typography>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent>
                        <SearchIcon
                            color="primary"
                            sx={{ fontSize: 40, mb: 2 }}
                        />

                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Needs and offers
                        </Typography>

                        <Typography color="text.secondary" sx={{ mt: 1 }}>
                            Define what companies are looking for and what they can
                            provide to potential partners.
                        </Typography>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent>
                        <HandshakeIcon
                            color="primary"
                            sx={{ fontSize: 40, mb: 2 }}
                        />

                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Matching
                        </Typography>

                        <Typography color="text.secondary" sx={{ mt: 1 }}>
                            Generate explainable matches based on compatibility,
                            preferences, and matching scores.
                        </Typography>
                    </CardContent>
                </Card>
            </Box>
        </Stack>
    );
}
