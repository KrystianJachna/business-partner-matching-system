import ArrowForwardRoundedIcon from "@mui/icons-material/ArrowForwardRounded";
import BusinessOutlinedIcon from "@mui/icons-material/BusinessOutlined";
import CheckCircleOutlineRoundedIcon from "@mui/icons-material/CheckCircleOutlineRounded";
import HandshakeOutlinedIcon from "@mui/icons-material/HandshakeOutlined";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import {
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    Divider,
    Paper,
    Stack,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import type { ReactNode } from "react";

interface ActionCardProps {
    icon: ReactNode;
    eyebrow: string;
    title: string;
    description: string;
    actionLabel: string;
    onClick: () => void;
    accent: string;
}

function ActionCard({
    icon,
    eyebrow,
    title,
    description,
    actionLabel,
    onClick,
    accent,
}: ActionCardProps) {
    return (
        <Card
            sx={{
                height: "100%",
                transition: "transform 0.2s ease, box-shadow 0.2s ease",
                "&:hover": {
                    transform: "translateY(-4px)",
                    boxShadow: "0 16px 34px rgba(23, 32, 51, 0.12)",
                },
            }}
        >
            <CardContent
                sx={{
                    p: { xs: 2.5, md: 3 },
                    height: "100%",
                    display: "flex",
                    flexDirection: "column",
                }}
            >
                <Box
                    sx={{
                        width: 48,
                        height: 48,
                        display: "grid",
                        placeItems: "center",
                        borderRadius: 3,
                        color: accent,
                        bgcolor: `${accent}18`,
                        mb: 2.5,
                    }}
                >
                    {icon}
                </Box>

                <Typography
                    variant="overline"
                    sx={{
                        color: accent,
                        fontWeight: 800,
                        letterSpacing: "0.1em",
                    }}
                >
                    {eyebrow}
                </Typography>

                <Typography variant="h5" sx={{ mt: 0.5, fontWeight: 750 }}>
                    {title}
                </Typography>

                <Typography color="text.secondary" sx={{ mt: 1.25, lineHeight: 1.65 }}>
                    {description}
                </Typography>

                <Button
                    variant="text"
                    endIcon={<ArrowForwardRoundedIcon />}
                    onClick={onClick}
                    sx={{
                        alignSelf: "flex-start",
                        mt: "auto",
                        pt: 2.5,
                        px: 0,
                        color: accent,
                        "&:hover": {
                            bgcolor: "transparent",
                            color: "primary.dark",
                        },
                    }}
                >
                    {actionLabel}
                </Button>
            </CardContent>
        </Card>
    );
}

export function DashboardPage() {
    const navigate = useNavigate();

    return (
        <Stack spacing={{ xs: 4, md: 6 }}>
            <Paper
                sx={{
                    position: "relative",
                    overflow: "hidden",
                    p: { xs: 3, sm: 4, md: 6 },
                    color: "common.white",
                    background: "linear-gradient(120deg, #142A44 0%, #1E3A5F 58%, #315F8F 100%)",
                    boxShadow: "0 18px 44px rgba(20, 42, 68, 0.22)",
                }}
            >
                <Box
                    sx={{
                        position: "absolute",
                        width: 360,
                        height: 360,
                        right: -120,
                        top: -180,
                        borderRadius: "50%",
                        bgcolor: "rgba(255,255,255,0.08)",
                    }}
                />
                <Box
                    sx={{
                        position: "absolute",
                        width: 220,
                        height: 220,
                        right: 170,
                        bottom: -170,
                        borderRadius: "50%",
                        border: "1px solid rgba(255,255,255,0.12)",
                    }}
                />

                <Stack
                    direction={{ xs: "column", lg: "row" }}
                    spacing={{ xs: 4, lg: 8 }}
                    sx={{
                        position: "relative",
                        alignItems: { xs: "flex-start", lg: "center" },
                    }}
                >
                    <Box sx={{ flex: 1, maxWidth: 700 }}>
                        <Chip
                            icon={<HandshakeOutlinedIcon />}
                            label="Explainable partner discovery"
                            size="small"
                            sx={{
                                mb: 2.5,
                                color: "common.white",
                                bgcolor: "rgba(255,255,255,0.12)",
                                border: "1px solid rgba(255,255,255,0.18)",
                                "& .MuiChip-icon": { color: "inherit" },
                            }}
                        />

                        <Typography
                            variant="h2"
                            component="h1"
                            sx={{
                                fontWeight: 800,
                                letterSpacing: "-0.045em",
                                lineHeight: 1.08,
                                fontSize: { xs: "2.35rem", md: "3.7rem" },
                            }}
                        >
                            Find the right business partner.
                        </Typography>

                        <Typography
                            sx={{
                                mt: 2,
                                maxWidth: 620,
                                color: "rgba(255,255,255,0.75)",
                                fontSize: { xs: "1rem", md: "1.1rem" },
                                lineHeight: 1.7,
                            }}
                        >
                            Build a clear profile of what your company needs,
                            what it can offer, and where the strongest
                            opportunities are.
                        </Typography>

                        <Stack
                            direction={{ xs: "column", sm: "row" }}
                            spacing={1.5}
                            sx={{ mt: 3.5 }}
                        >
                            <Button
                                variant="contained"
                                endIcon={<ArrowForwardRoundedIcon />}
                                onClick={() => navigate("/matching")}
                                sx={{
                                    bgcolor: "common.white",
                                    color: "primary.dark",
                                    px: 2.5,
                                    "&:hover": {
                                        bgcolor: "grey.100",
                                    },
                                }}
                            >
                                Run matching
                            </Button>
                            <Button
                                variant="outlined"
                                onClick={() => navigate("/companies/new")}
                                sx={{
                                    color: "common.white",
                                    borderColor: "rgba(255,255,255,0.35)",
                                    px: 2.5,
                                    "&:hover": {
                                        borderColor: "common.white",
                                        bgcolor: "rgba(255,255,255,0.08)",
                                    },
                                }}
                            >
                                Add a company
                            </Button>
                        </Stack>
                    </Box>

                    <Box
                        sx={{
                            width: { xs: "100%", lg: 330 },
                            p: 2.5,
                            borderRadius: 3,
                            bgcolor: "rgba(255,255,255,0.1)",
                            border: "1px solid rgba(255,255,255,0.14)",
                            backdropFilter: "blur(10px)",
                        }}
                    >
                        <Typography variant="overline" sx={{ color: "rgba(255,255,255,0.62)" }}>
                            How it works
                        </Typography>

                        <Stack spacing={2.25} sx={{ mt: 1.5 }}>
                            {[
                                ["01", "Create a company profile"],
                                ["02", "Add needs and offers"],
                                ["03", "Compare the best matches"],
                            ].map(([number, label], index) => (
                                <Stack key={number} direction="row" spacing={1.5} sx={{ alignItems: "center" }}>
                                    <Box
                                        sx={{
                                            width: 30,
                                            height: 30,
                                            display: "grid",
                                            placeItems: "center",
                                            borderRadius: "50%",
                                            bgcolor: index === 2 ? "secondary.main" : "rgba(255,255,255,0.14)",
                                            color: "common.white",
                                            fontSize: "0.75rem",
                                            fontWeight: 800,
                                        }}
                                    >
                                        {number}
                                    </Box>
                                    <Typography sx={{ color: "rgba(255,255,255,0.88)", fontWeight: 600 }}>
                                        {label}
                                    </Typography>
                                </Stack>
                            ))}
                        </Stack>
                    </Box>
                </Stack>
            </Paper>

            <Box>
                <Stack
                    direction={{ xs: "column", sm: "row" }}
                    spacing={1}
                    sx={{
                        mb: 2.5,
                        justifyContent: "space-between",
                        alignItems: { xs: "flex-start", sm: "flex-end" },
                    }}
                >
                    <Box>
                        <Typography variant="h4" sx={{ fontWeight: 750 }}>
                            Start building your network
                        </Typography>
                        <Typography color="text.secondary" sx={{ mt: 0.75 }}>
                            Choose the next step for your workspace.
                        </Typography>
                    </Box>
                    <Typography variant="body2" color="text.secondary">
                        Three simple steps to a better match
                    </Typography>
                </Stack>

                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns: { xs: "1fr", md: "repeat(3, 1fr)" },
                        gap: 2.5,
                    }}
                >
                    <ActionCard
                        icon={<BusinessOutlinedIcon />}
                        eyebrow="Step 01"
                        title="Manage companies"
                        description="Create company profiles with industries, locations, capabilities, and specializations."
                        actionLabel="View companies"
                        onClick={() => navigate("/companies")}
                        accent="#3F6B96"
                    />
                    <ActionCard
                        icon={<SearchRoundedIcon />}
                        eyebrow="Step 02"
                        title="Define opportunities"
                        description="Describe what companies need and what they can provide to potential partners."
                        actionLabel="Browse needs"
                        onClick={() => navigate("/needs")}
                        accent="#6D5CA8"
                    />
                    <ActionCard
                        icon={<HandshakeOutlinedIcon />}
                        eyebrow="Step 03"
                        title="Discover matches"
                        description="Run the algorithm and understand each result through scores and matching highlights."
                        actionLabel="Open matching"
                        onClick={() => navigate("/matching")}
                        accent="#2E8B78"
                    />
                </Box>
            </Box>

            <Paper
                variant="outlined"
                sx={{
                    p: { xs: 2.5, md: 3 },
                    bgcolor: "rgba(255,255,255,0.62)",
                }}
            >
                <Stack
                    direction={{ xs: "column", md: "row" }}
                    spacing={2.5}
                    sx={{ alignItems: { xs: "flex-start", md: "center" } }}
                >
                    <Box
                        sx={{
                            width: 44,
                            height: 44,
                            display: "grid",
                            placeItems: "center",
                            borderRadius: 2.5,
                            color: "success.dark",
                            bgcolor: "success.50",
                            flexShrink: 0,
                        }}
                    >
                        <CheckCircleOutlineRoundedIcon />
                    </Box>
                    <Box sx={{ flex: 1 }}>
                        <Typography sx={{ fontWeight: 700 }}>
                            Matching results are explainable
                        </Typography>
                        <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                            Every match includes its overall score, criterion breakdown,
                            and the strongest compatibility highlights.
                        </Typography>
                    </Box>
                    <Divider flexItem orientation="vertical" sx={{ display: { xs: "none", md: "block" } }} />
                    <Button
                        variant="text"
                        endIcon={<ArrowForwardRoundedIcon />}
                        onClick={() => navigate("/matching")}
                        sx={{ flexShrink: 0 }}
                    >
                        Learn through results
                    </Button>
                </Stack>
            </Paper>

        </Stack>
    );
}
