import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    Container, IconButton,
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
import { VisibilityOutlined as VisibilityOutlinedIcon } from "@mui/icons-material";
import Tooltip from "@mui/material/Tooltip";
import { formatDate, formatMoneyValue, formatCooperationType } from "../common/utils/Formatters.ts";

function getCooperationTypeStyle(
    cooperationType: string,
) {
    switch (cooperationType) {
        case "SUBCONTRACTING":
            return {
                backgroundColor: "#FFF3E0",
                color: "#E65100",
            };

        case "SUPPLY":
            return {
                backgroundColor: "#E8F5E9",
                color: "#2E7D32",
            };

        case "DISTRIBUTION":
            return {
                backgroundColor: "#E0F2F1",
                color: "#00796B",
            };

        case "OUTSOURCING":
            return {
                backgroundColor: "#E3F2FD",
                color: "#1565C0",
            };

        case "CONSULTING":
            return {
                backgroundColor: "#FFF8E1",
                color: "#F57F17",
            };

        case "TECHNOLOGY_PARTNERSHIP":
            return {
                backgroundColor: "#F3E5F5",
                color: "#7B1FA2",
            };

        case "JOINT_PROJECT":
            return {
                backgroundColor: "#E8EAF6",
                color: "#3949AB",
            };

        default:
            return {
                backgroundColor: "#F5F5F5",
                color: "#616161",
            };
    }
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
                    gap: 2,
                    flexDirection: {
                        xs: "column",
                        sm: "row",
                    },
                    justifyContent: "space-between",
                    alignItems: {
                        xs: "flex-start",
                        sm: "center",
                    },
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
                    sx={{
                        borderRadius: 1,
                        overflowX: "auto",
                    }}
                >
                    <Table
                        sx={{
                            width: "100%",
                            tableLayout: "auto",
                            "& .MuiTableCell-root": {
                                px: 1.5,
                            },
                        }}
                    >
                        <TableHead>
                            <TableRow
                                sx={{
                                    backgroundColor: "grey.100",
                                }}
                            >
                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Business need
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Cooperation type
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Specializations
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Budget
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Required period
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Partner criteria
                                </TableCell>

                                <TableCell
                                    align="center"
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Status
                                </TableCell>

                                <TableCell
                                    align="right"
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                </TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {businessNeeds.map(
                                (businessNeed, index) => (
                                    <TableRow
                                        key={businessNeed.id}
                                        hover
                                        sx={{
                                            backgroundColor:
                                                index % 2 === 0
                                                    ? "background.paper"
                                                    : "grey.50",
                                            transition:
                                                "background-color 0.2s ease",
                                            "&:last-child td": {
                                                borderBottom: 0,
                                            },
                                        }}
                                    >
                                        <TableCell
                                            sx={{
                                                py: 2,
                                                minWidth: 180,
                                                width: 180
                                            }}
                                        >
                                            <Stack spacing={0.5}>
                                                <Typography
                                                    sx={{
                                                        fontWeight: 600,
                                                        lineHeight: 1.35,
                                                    }}
                                                >
                                                    {
                                                        businessNeed.title
                                                    }
                                                </Typography>

                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    {
                                                        businessNeed.companyName
                                                    }
                                                </Typography>
                                            </Stack>
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                py: 2,
                                                minWidth: 135,
                                            }}
                                        >
                                            <Chip
                                                label={formatCooperationType(
                                                    businessNeed.cooperationType,
                                                )}
                                                size="small"
                                                sx={{
                                                    height: 26,
                                                    fontWeight: 500,
                                                    px: 0.5,
                                                    border: "none",
                                                    ...getCooperationTypeStyle(
                                                        businessNeed.cooperationType,
                                                    ),
                                                }}
                                            />
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                py: 2,
                                                minWidth: 170,
                                                width: 170,
                                            }}
                                        >
                                            <Stack
                                                direction="row"
                                                spacing={0.5}
                                                useFlexGap
                                                sx={{
                                                    flexWrap: "wrap",
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

                                        <TableCell
                                            sx={{
                                                py: 2,
                                                minWidth: 130,
                                            }}
                                        >
                                            {businessNeed.budget ? (
                                                <Stack spacing={0.25}>
                                                    <Typography variant="body2">
                                                        {formatMoneyValue(
                                                            businessNeed
                                                                .budget
                                                                .min,
                                                            businessNeed
                                                                .budget
                                                                .currency,
                                                        )}
                                                    </Typography>

                                                    <Typography
                                                        variant="body2"
                                                        color="text.secondary"
                                                    >
                                                        —
                                                    </Typography>

                                                    <Typography variant="body2">
                                                        {formatMoneyValue(
                                                            businessNeed
                                                                .budget
                                                                .max,
                                                            businessNeed
                                                                .budget
                                                                .currency,
                                                        )}
                                                    </Typography>
                                                </Stack>
                                            ) : (
                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    Not specified
                                                </Typography>
                                            )}
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                py: 2,
                                                minWidth: 130,
                                            }}
                                        >
                                            {businessNeed.requiredPeriod ? (
                                                <Stack spacing={0.25}>
                                                    <Typography variant="body2">
                                                        {formatDate(
                                                            businessNeed
                                                                .requiredPeriod
                                                                .from,
                                                        )}
                                                    </Typography>

                                                    <Typography
                                                        variant="body2"
                                                        color="text.secondary"
                                                    >
                                                        —
                                                    </Typography>

                                                    <Typography variant="body2">
                                                        {formatDate(
                                                            businessNeed
                                                                .requiredPeriod
                                                                .until,
                                                        )}
                                                    </Typography>
                                                </Stack>
                                            ) : (
                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    Not specified
                                                </Typography>
                                            )}
                                        </TableCell>

                                        <TableCell
                                            align="center"
                                            sx={{
                                                py: 2,
                                                width: 80,
                                            }}
                                        >
                                            <TableCell
                                                sx={{
                                                    py: 2,
                                                    minWidth: 140,
                                                    width: 140,
                                                }}
                                            >
                                                <Stack spacing={0.5}>
                                                    <Chip
                                                        size="small"
                                                        variant="outlined"
                                                        label={
                                                            businessNeed.maxDistanceKm !== null
                                                                ? `dist. ≤ ${businessNeed.maxDistanceKm} km`
                                                                : "No distance limit"
                                                        }
                                                        sx={{
                                                            alignSelf: "flex-start",
                                                        }}
                                                    />

                                                    <Chip
                                                        size="small"
                                                        variant="outlined"
                                                        label={
                                                            businessNeed.minPartnerExperienceYears !==
                                                            null
                                                                ? `${businessNeed.minPartnerExperienceYears} ${
                                                                    businessNeed.minPartnerExperienceYears
                                                                    === 1
                                                                        ? "year"
                                                                        : "years"
                                                                }`
                                                                : "Any experience"
                                                        }
                                                        sx={{
                                                            alignSelf: "flex-start",
                                                        }}
                                                    />

                                                    <Chip
                                                        size="small"
                                                        variant="outlined"
                                                        label={`${businessNeed.maxPartners} ${
                                                            businessNeed.maxPartners === 1
                                                                ? "partner"
                                                                : "partners"
                                                        }`}
                                                        sx={{
                                                            alignSelf: "flex-start",
                                                        }}
                                                    />
                                                </Stack>
                                            </TableCell>
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                py: 2,
                                                width: 80,
                                                minWidth: 80
                                            }}
                                        >
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

                                        <TableCell
                                            align="center"
                                            sx={{
                                                py: 2,
                                                px: 0.25,
                                                width: 40,
                                                minWidth: 40,
                                            }}
                                        >
                                            <Tooltip title="View details">
                                                <IconButton
                                                    size="small"
                                                    aria-label="View business need details"
                                                    sx={{
                                                        color: "text.secondary",
                                                        "&:hover": {
                                                            color: "primary.main",
                                                            backgroundColor: "action.hover",
                                                        },
                                                    }}
                                                    onClick={() => {
                                                        navigate(
                                                            `/business-needs/${businessNeed.id}`,
                                                        );
                                                    }}
                                                >
                                                    <VisibilityOutlinedIcon fontSize="small" />
                                                </IconButton>
                                            </Tooltip>
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
