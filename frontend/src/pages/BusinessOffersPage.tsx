import { VisibilityOutlined as VisibilityOutlinedIcon } from "@mui/icons-material";
import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    Container,
    IconButton,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Tooltip,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import {
    formatCooperationType,
    formatDate,
    formatMoneyValue,
} from "../common/utils/Formatters";
import { useBusinessOffers } from "../features/businessOffer/hooks/useBusinessOffers";
import { getCooperationTypeStyle } from "../common/utils/CooperationTypeStyles";

export function BusinessOffersPage() {
    const businessOffersQuery = useBusinessOffers();
    const navigate = useNavigate();

    if (businessOffersQuery.isPending) {
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

    if (businessOffersQuery.isError) {
        return (
            <Container
                maxWidth="xl"
                sx={{ py: 4 }}
            >
                <Alert severity="error">
                    Failed to load business offers.
                </Alert>
            </Container>
        );
    }

    const businessOffers = businessOffersQuery.data;

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
                        Business Offers
                    </Typography>

                    <Typography color="text.secondary">
                        Browse all business offers registered
                        in the system.
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    onClick={() => {
                        navigate("/companies");
                    }}
                >
                    Create business offer
                </Button>
            </Stack>

            {businessOffers.length === 0 ? (
                <Paper
                    variant="outlined"
                    sx={{
                        p: 4,
                        textAlign: "center",
                    }}
                >
                    <Typography variant="h6">
                        No business offers found
                    </Typography>

                    <Typography
                        color="text.secondary"
                        sx={{ mt: 1 }}
                    >
                        Create a business offer from a company
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
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Business offer
                                </TableCell>

                                <TableCell
                                    sx={{
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Cooperation type
                                </TableCell>

                                <TableCell
                                    sx={{
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Specializations
                                </TableCell>

                                <TableCell
                                    sx={{
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Price range
                                </TableCell>

                                <TableCell
                                    sx={{
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Availability period
                                </TableCell>

                                <TableCell
                                    sx={{
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Offer details
                                </TableCell>

                                <TableCell
                                    align="center"
                                    sx={{
                                        py: 2,
                                        fontWeight: 700,
                                    }}
                                >
                                    Status
                                </TableCell>

                                <TableCell
                                    align="center"
                                    sx={{
                                        py: 2,
                                        width: 40,
                                    }}
                                />
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {businessOffers.map(
                                (businessOffer, index) => (
                                    <TableRow
                                        key={businessOffer.id}
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
                                                width: 180,
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
                                                        businessOffer.title
                                                    }
                                                </Typography>

                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    Company ID:{" "}
                                                    {
                                                        businessOffer.companyId
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
                                                    businessOffer.cooperationType,
                                                )}
                                                size="small"
                                                sx={{
                                                    height: 26,
                                                    px: 0.5,
                                                    border: "none",
                                                    fontWeight: 500,
                                                    ...getCooperationTypeStyle(
                                                        businessOffer.cooperationType,
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
                                                {businessOffer.offeredSpecializations.map(
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
                                            {businessOffer.priceRange ? (
                                                <Stack spacing={0.25}>
                                                    <Typography variant="body2">
                                                        {formatMoneyValue(
                                                            businessOffer
                                                                .priceRange
                                                                .min,
                                                            businessOffer
                                                                .priceRange
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
                                                            businessOffer
                                                                .priceRange
                                                                .max,
                                                            businessOffer
                                                                .priceRange
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
                                                minWidth: 140,
                                            }}
                                        >
                                            {businessOffer.availabilityPeriod ? (
                                                <Stack spacing={0.25}>
                                                    <Typography variant="body2">
                                                        {formatDate(
                                                            businessOffer
                                                                .availabilityPeriod
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
                                                            businessOffer
                                                                .availabilityPeriod
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
                                            sx={{
                                                py: 2,
                                                minWidth: 150,
                                                width: 150,
                                            }}
                                        >
                                            <Stack spacing={0.5}>
                                                <Chip
                                                    size="small"
                                                    variant="outlined"
                                                    label={
                                                        businessOffer.serviceRadiusKm
                                                        !== null
                                                            ? `Radius ≤ ${businessOffer.serviceRadiusKm} km`
                                                            : "No radius specified"
                                                    }
                                                    sx={{
                                                        alignSelf:
                                                            "flex-start",
                                                    }}
                                                />

                                                <Chip
                                                    size="small"
                                                    variant="outlined"
                                                    label={
                                                        businessOffer.experienceYears
                                                        !== null
                                                            ? `${businessOffer.experienceYears} ${
                                                                businessOffer.experienceYears
                                                                === 1
                                                                    ? "year"
                                                                    : "years"
                                                            } experience`
                                                            : "Experience not specified"
                                                    }
                                                    sx={{
                                                        alignSelf:
                                                            "flex-start",
                                                    }}
                                                />

                                                <Chip
                                                    size="small"
                                                    variant="outlined"
                                                    label={`${businessOffer.maxPartners} ${
                                                        businessOffer.maxPartners
                                                        === 1
                                                            ? "partner"
                                                            : "partners"
                                                    }`}
                                                    sx={{
                                                        alignSelf:
                                                            "flex-start",
                                                    }}
                                                />
                                            </Stack>
                                        </TableCell>

                                        <TableCell
                                            align="center"
                                            sx={{
                                                py: 2,
                                                width: 80,
                                                minWidth: 80,
                                            }}
                                        >
                                            <Chip
                                                label={
                                                    businessOffer.active
                                                        ? "Active"
                                                        : "Inactive"
                                                }
                                                color={
                                                    businessOffer.active
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
                                                    aria-label="View business offer details"
                                                    sx={{
                                                        color: "text.secondary",
                                                        "&:hover": {
                                                            color: "primary.main",
                                                            backgroundColor:
                                                                "action.hover",
                                                        },
                                                    }}
                                                    onClick={() => {
                                                        navigate(
                                                            `/business-offers/${businessOffer.id}`,
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
