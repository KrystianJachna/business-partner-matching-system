import AutoAwesomeOutlinedIcon from "@mui/icons-material/AutoAwesomeOutlined";
import CheckCircleOutlineOutlinedIcon from "@mui/icons-material/CheckCircleOutlineOutlined";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import HandshakeOutlinedIcon from "@mui/icons-material/HandshakeOutlined";
import RefreshOutlinedIcon from "@mui/icons-material/RefreshOutlined";
import TrendingUpOutlinedIcon from "@mui/icons-material/TrendingUpOutlined";
import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    Divider,
    Grid,
    LinearProgress,
    Paper,
    Stack,
    Typography,
} from "@mui/material";
import { useRunMatching } from "../features/matching/hooks/useRunMatching";
import type {
    CriterionScoreResponse,
    MatchedPairResponse,
} from "../features/matching/model/MatchingResponse";

function formatCriterion(criterion: string): string {
    return criterion
        .toLowerCase()
        .split("_")
        .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
        .join(" ");
}

function formatReasonCode(code: string): string {
    return code
        .toLowerCase()
        .split("_")
        .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
        .join(" ");
}

function scoreAsPercent(score: number): string {
    return `${Math.round(score * 100)}%`;
}

function scoreColor(score: number): "success" | "warning" | "error" {
    if (score >= 0.75) {
        return "success";
    }

    if (score >= 0.5) {
        return "warning";
    }

    return "error";
}

function CriterionRow({ criterion }: { criterion: CriterionScoreResponse }) {
    return (
        <Stack spacing={0.75} sx={{ mb: 1.5 }}>
            <Stack
                direction="row"
                sx={{
                    justifyContent: "space-between",
                    alignItems: "center",
                }}
            >
                <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    {formatCriterion(criterion.criterion)}
                </Typography>

                <Typography variant="body2" color="text.secondary">
                    {scoreAsPercent(criterion.score)} · weight {scoreAsPercent(criterion.weight)}
                </Typography>
            </Stack>

            <LinearProgress
                variant="determinate"
                value={criterion.score * 100}
                color={scoreColor(criterion.score)}
                sx={{
                    height: 7,
                    borderRadius: 4,
                    bgcolor: "grey.200",
                }}
            />
        </Stack>
    );
}

function MatchCard({ match }: { match: MatchedPairResponse }) {
    const score = Math.round(match.totalScore * 100);

    return (
        <Card sx={{ height: "100%" }}>
            <CardContent sx={{ p: { xs: 2.5, md: 3 } }}>
                <Stack spacing={2.5}>
                    <Stack
                        direction={{ xs: "column", sm: "row" }}
                        spacing={2}
                        sx={{
                            justifyContent: "space-between",
                            alignItems: { xs: "flex-start", sm: "center" },
                        }}
                    >
                        <Stack
                            direction="row"
                            spacing={1.5}
                            sx={{ alignItems: "center" }}
                        >
                            <Box
                                sx={{
                                    width: 58,
                                    height: 58,
                                    borderRadius: "50%",
                                    display: "grid",
                                    placeItems: "center",
                                    bgcolor: "success.50",
                                    border: 3,
                                    borderColor: "success.200",
                                    flexShrink: 0,
                                }}
                            >
                                <Typography
                                    variant="h6"
                                    color="success.dark"
                                    sx={{ fontWeight: 800 }}
                                >
                                    {score}%
                                </Typography>
                            </Box>

                            <Box>
                                <Typography variant="overline" color="text.secondary">
                                    Compatibility score
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Need #{match.needId} · Offer #{match.offerId}
                                </Typography>
                            </Box>
                        </Stack>

                        <Chip
                            icon={<CheckCircleOutlineOutlinedIcon />}
                            label="Compatible"
                            color="success"
                            variant="outlined"
                        />
                    </Stack>

                    <Stack
                        direction={{ xs: "column", md: "row" }}
                        spacing={2}
                        divider={<Divider orientation="vertical" flexItem />}
                    >
                        <Box sx={{ flex: 1 }}>
                            <Typography variant="overline" color="primary.main">
                                Business need
                            </Typography>
                            <Typography variant="h6" sx={{ lineHeight: 1.3 }}>
                                {match.needTitle}
                            </Typography>
                            <Typography color="text.secondary">
                                {match.needCompanyName}
                            </Typography>
                        </Box>

                        <Box sx={{ flex: 1 }}>
                            <Typography variant="overline" color="primary.main">
                                Business offer
                            </Typography>
                            <Typography variant="h6" sx={{ lineHeight: 1.3 }}>
                                {match.offerTitle}
                            </Typography>
                            <Typography color="text.secondary">
                                {match.offerCompanyName}
                            </Typography>
                        </Box>
                    </Stack>

                    <Accordion
                        disableGutters
                        elevation={0}
                        sx={{
                            bgcolor: "grey.50",
                            border: 1,
                            borderColor: "divider",
                            "&:before": { display: "none" },
                        }}
                    >
                        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography sx={{ fontWeight: 700 }}>
                                Score breakdown
                            </Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            {match.criterionScores.map((criterion) => (
                                <CriterionRow
                                    key={criterion.criterion}
                                    criterion={criterion}
                                />
                            ))}
                        </AccordionDetails>
                    </Accordion>

                    <Box>
                        <Typography variant="subtitle2" sx={{ mb: 1 }}>
                            Why this match works
                        </Typography>
                        <Stack
                            direction="row"
                            sx={{ flexWrap: "wrap", gap: 1 }}
                        >
                            {match.compatibilityReasons.map((reason) => (
                                <Chip
                                    key={reason.code}
                                    label={formatReasonCode(reason.code)}
                                    size="small"
                                    variant="outlined"
                                    color="primary"
                                />
                            ))}
                        </Stack>
                    </Box>
                </Stack>
            </CardContent>
        </Card>
    );
}

export function MatchingPage() {
    const matchingMutation = useRunMatching();
    const matchingResult = matchingMutation.data;

    return (
        <Stack spacing={4} sx={{ py: 4 }}>
            <Stack
                direction={{ xs: "column", md: "row" }}
                spacing={2}
                sx={{
                    justifyContent: "space-between",
                    alignItems: { xs: "flex-start", md: "center" },
                }}
            >
                <Box>
                    <Typography
                        variant="h4"
                        component="h1"
                        sx={{ fontWeight: 700 }}
                    >
                        Partner matching
                    </Typography>
                    <Typography color="text.secondary" sx={{ mt: 1 }}>
                        Find the strongest business partnerships using the matching algorithm.
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    size="large"
                    startIcon={
                        matchingMutation.isPending
                            ? <CircularProgress size={18} color="inherit" />
                            : matchingResult
                                ? <RefreshOutlinedIcon />
                                : <AutoAwesomeOutlinedIcon />
                    }
                    onClick={() => matchingMutation.mutate()}
                    disabled={matchingMutation.isPending}
                >
                    {matchingMutation.isPending ? "Running matching..." : matchingResult ? "Run again" : "Run matching"}
                </Button>
            </Stack>

            {matchingMutation.isError && (
                <Alert severity="error">
                    Could not run matching. Check that the backend is running and try again.
                </Alert>
            )}

            {!matchingResult && !matchingMutation.isPending && !matchingMutation.isError && (
                <Paper
                    variant="outlined"
                    sx={{
                        p: { xs: 4, md: 7 },
                        textAlign: "center",
                        borderStyle: "dashed",
                    }}
                >
                    <HandshakeOutlinedIcon
                        sx={{ fontSize: 56, color: "primary.light", mb: 1 }}
                    />
                    <Typography variant="h5" sx={{ fontWeight: 700 }}>
                        Ready to find your best matches?
                    </Typography>
                    <Typography color="text.secondary" sx={{ mt: 1 }}>
                        Run the algorithm to compare active business needs and offers.
                    </Typography>
                </Paper>
            )}

            {matchingResult && (
                <Stack spacing={3}>
                    <Grid container spacing={2}>
                        <Grid size={{ xs: 12, sm: 4 }}>
                            <Paper variant="outlined" sx={{ p: 2.5, height: "100%" }}>
                                <Stack
                                    direction="row"
                                    spacing={1.5}
                                    sx={{ alignItems: "center" }}
                                >
                                    <HandshakeOutlinedIcon color="primary" />
                                    <Box>
                                        <Typography variant="h4" sx={{ fontWeight: 800 }}>
                                            {matchingResult.matchCount}
                                        </Typography>
                                        <Typography color="text.secondary">
                                            Matches found
                                        </Typography>
                                    </Box>
                                </Stack>
                            </Paper>
                        </Grid>
                        <Grid size={{ xs: 12, sm: 4 }}>
                            <Paper variant="outlined" sx={{ p: 2.5, height: "100%" }}>
                                <Stack
                                    direction="row"
                                    spacing={1.5}
                                    sx={{ alignItems: "center" }}
                                >
                                    <TrendingUpOutlinedIcon color="success" />
                                    <Box>
                                        <Typography variant="h4" sx={{ fontWeight: 800 }}>
                                            {matchingResult.matchCount > 0
                                                ? scoreAsPercent(
                                                    matchingResult.matches.reduce(
                                                        (sum, match) => sum + match.totalScore,
                                                        0,
                                                    ) / matchingResult.matchCount,
                                                )
                                                : "—"}
                                        </Typography>
                                        <Typography color="text.secondary">
                                            Average compatibility
                                        </Typography>
                                    </Box>
                                </Stack>
                            </Paper>
                        </Grid>
                        <Grid size={{ xs: 12, sm: 4 }}>
                            <Paper variant="outlined" sx={{ p: 2.5, height: "100%" }}>
                                <Stack
                                    direction="row"
                                    spacing={1.5}
                                    sx={{ alignItems: "center" }}
                                >
                                    <AutoAwesomeOutlinedIcon color="secondary" />
                                    <Box>
                                        <Typography variant="h4" sx={{ fontWeight: 800 }}>
                                            {matchingResult.matches.filter((match) => match.totalScore >= 0.75).length}
                                        </Typography>
                                        <Typography color="text.secondary">
                                            Strong matches
                                        </Typography>
                                    </Box>
                                </Stack>
                            </Paper>
                        </Grid>
                    </Grid>

                    {matchingResult.matches.length === 0 ? (
                        <Paper variant="outlined" sx={{ p: 5, textAlign: "center" }}>
                            <Typography variant="h6">No compatible matches found</Typography>
                            <Typography color="text.secondary" sx={{ mt: 1 }}>
                                Add compatible active needs and offers, then run matching again.
                            </Typography>
                        </Paper>
                    ) : (
                        <Grid container spacing={3}>
                            {matchingResult.matches.map((match) => (
                                <Grid key={`${match.needId}-${match.offerId}`} size={{ xs: 12, lg: 6 }}>
                                    <MatchCard match={match} />
                                </Grid>
                            ))}
                        </Grid>
                    )}
                </Stack>
            )}
        </Stack>
    );
}
