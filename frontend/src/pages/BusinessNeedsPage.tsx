import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    Container,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import { useBusinessNeeds } from "../features/businessNeed/hooks/useBusinessNeeds";
import type { BusinessNeedResponse } from "../features/businessNeed/model/BusinessNeedResponse";

function formatMoney(
    businessNeed: BusinessNeedResponse,
): string {
    const budget = businessNeed.budget;

    if (!budget) {
        return "Not specified";
    }

    const formatter = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: budget.currency,
        maximumFractionDigits: 0,
    });

    return `${formatter.format(budget.min)} – ${formatter.format(
        budget.max,
    )}`;
}

function formatPeriod(
    businessNeed: BusinessNeedResponse,
): string {
    const period = businessNeed.requiredPeriod;

    if (!period) {
        return "Not specified";
    }

    return `${period.from} – ${period.until}`;
}

function formatCooperationType(
    value: string,
): string {
    return value
        .toLowerCase()
        .split("_")
        .map(
            (part) =>
                part.charAt(0).toUpperCase()
                + part.slice(1),
        )
        .join(" ");
}

export function BusinessNeedsPage() {
    const businessNeedsQuery = useBusinessNeeds();
    const navigate = useNavigate();

    if (businessNeedsQuery.isPending) {
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

    if (businessNeedsQuery.isError) {
        return (
            <Container
                maxWidth="xl"
                sx={{ py: 4 }}
            >
                <Alert severity="error">
                    Failed to load business needs.
                </Alert>
            </Container>
        );
    }

    const businessNeeds = businessNeedsQuery.data;

    return (
        <Container
            maxWidth="xl"
            sx={{ py: 4 }}
        >
            <Stack
                sx={{
                    mb: 3,
                    flexDirection: "row",
                    justifyContent: "space-between",
                    alignItems: "center",
                }}
            >
                <Box>
                    <Typography
                        variant="h4"
                        component="h1"
                        gutterBottom
                    >
                        Business Needs
                    </Typography>

                    <Typography color="text.secondary">
                        Browse all business needs registered
                        in the system.
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    onClick={() => {
                        navigate("/companies");
                    }}
                >
                    Create Business Need
                </Button>
            </Stack>

            {businessNeeds.length === 0 ? (
                <Paper
                    variant="outlined"
                    sx={{
                        p: 4,
                        textAlign: "center",
                    }}
                >
                    <Typography variant="h6">
                        No business needs found
                    </Typography>

                    <Typography
                        color="text.secondary"
                        sx={{ mt: 1 }}
                    >
                        Create a business need from a company
                        details page.
                    </Typography>
                </Paper>
            ) : (
                <TableContainer
                    component={Paper}
                    variant="outlined"
                >
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Title</TableCell>
                                <TableCell>
                                    Cooperation type
                                </TableCell>
                                <TableCell>
                                    Specializations
                                </TableCell>
                                <TableCell>Budget</TableCell>
                                <TableCell>
                                    Required period
                                </TableCell>
                                <TableCell align="center">
                                    Partners
                                </TableCell>
                                <TableCell>Status</TableCell>
                                <TableCell align="right">
                                    Actions
                                </TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {businessNeeds.map(
                                (businessNeed) => (
                                    <TableRow
                                        key={businessNeed.id}
                                        hover
                                    >
                                        <TableCell>
                                            <Stack spacing={0.5}>
                                                <Typography sx={{ fontWeight: 600 }}>
                                                    {
                                                        businessNeed.title
                                                    }
                                                </Typography>

                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    Company ID:{" "}
                                                    {
                                                        businessNeed.companyId
                                                    }
                                                </Typography>
                                            </Stack>
                                        </TableCell>

                                        <TableCell>
                                            {formatCooperationType(
                                                businessNeed.cooperationType,
                                            )}
                                        </TableCell>

                                        <TableCell>
                                            <Stack
                                                direction="row"
                                                spacing={0.5}
                                                useFlexGap
                                                sx={{
                                                    flexWrap:
                                                        "wrap",
                                                }}
                                            >
                                                {businessNeed.requiredSpecializations.map(
                                                    (
                                                        specialization,
                                                    ) => (
                                                        <Chip
                                                            key={
                                                                specialization.id
                                                            }
                                                            label={
                                                                specialization.name
                                                            }
                                                            size="small"
                                                            variant="outlined"
                                                        />
                                                    ),
                                                )}
                                            </Stack>
                                        </TableCell>

                                        <TableCell>
                                            {formatMoney(
                                                businessNeed,
                                            )}
                                        </TableCell>

                                        <TableCell>
                                            {formatPeriod(
                                                businessNeed,
                                            )}
                                        </TableCell>

                                        <TableCell align="center">
                                            {
                                                businessNeed.maxPartners
                                            }
                                        </TableCell>

                                        <TableCell>
                                            <Chip
                                                label={
                                                    businessNeed.active
                                                        ? "Active"
                                                        : "Inactive"
                                                }
                                                color={
                                                    businessNeed.active
                                                        ? "success"
                                                        : "default"
                                                }
                                                size="small"
                                            />
                                        </TableCell>

                                        <TableCell align="right">
                                            <Button
                                                size="small"
                                                onClick={() => {
                                                    navigate(
                                                        `/business-needs/${businessNeed.id}`,
                                                    );
                                                }}
                                            >
                                                View
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ),
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Container>
    );
}
