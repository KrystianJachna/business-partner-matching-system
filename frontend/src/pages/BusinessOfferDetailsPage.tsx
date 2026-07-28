import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import BusinessOutlinedIcon from "@mui/icons-material/BusinessOutlined";
import CalendarMonthOutlinedIcon from "@mui/icons-material/CalendarMonthOutlined";
import GroupsOutlinedIcon from "@mui/icons-material/GroupsOutlined";
import HandshakeOutlinedIcon from "@mui/icons-material/HandshakeOutlined";
import LocationOnOutlinedIcon from "@mui/icons-material/LocationOnOutlined";
import PaymentsOutlinedIcon from "@mui/icons-material/PaymentsOutlined";
import WorkHistoryOutlinedIcon from "@mui/icons-material/WorkHistoryOutlined";
import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    Paper,
    Stack,
    Typography,
} from "@mui/material";
import type { ReactNode } from "react";
import {
    useNavigate,
    useParams,
    useSearchParams,
} from "react-router";
import {
    formatCooperationType,
    formatDate,
    formatDateTime,
    formatMoneyValue,
} from "../common/utils/Formatters";
import { useBusinessOffer } from "../features/businessOffer/hooks/useBusinessOffer";

interface InformationItemProps {
    icon?: ReactNode;
    label: string;
    value: string;
}

export function BusinessOfferDetailsPage() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    const fromCompanyParam = searchParams.get("fromCompany");

    const parsedFromCompanyId = fromCompanyParam
        ? Number(fromCompanyParam)
        : undefined;

    const validFromCompanyId =
        parsedFromCompanyId !== undefined &&
        Number.isInteger(parsedFromCompanyId) &&
        parsedFromCompanyId > 0
            ? parsedFromCompanyId
            : undefined;

    const { businessOfferId } = useParams();

    const parsedBusinessOfferId = businessOfferId
        ? Number(businessOfferId)
        : undefined;

    const validBusinessOfferId =
        parsedBusinessOfferId !== undefined &&
        Number.isInteger(parsedBusinessOfferId) &&
        parsedBusinessOfferId > 0
            ? parsedBusinessOfferId
            : undefined;

    const {
        data: businessOffer,
        isLoading,
        isError,
    } = useBusinessOffer(validBusinessOfferId);

    const handleBack = () => {
        if (validFromCompanyId !== undefined) {
            navigate(`/companies/${validFromCompanyId}`);
            return;
        }

        navigate("/business-offers");
    };

    if (validBusinessOfferId === undefined) {
        return (
            <Alert severity="error">
                Invalid business offer identifier.
            </Alert>
        );
    }

    if (isLoading) {
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

    if (isError || !businessOffer) {
        return (
            <Alert severity="error">
                Failed to load business offer details.
            </Alert>
        );
    }

    return (
        <Box>
            <Button
                startIcon={<ArrowBackOutlinedIcon />}
                onClick={handleBack}
                sx={{
                    mb: 3,
                    textTransform: "none",
                }}
            >
                {validFromCompanyId !== undefined
                    ? "Back to company"
                    : "Back to business offers"}
            </Button>

            <Stack
                direction={{
                    xs: "column",
                    md: "row",
                }}
                spacing={2}
                sx={{
                    justifyContent: "space-between",
                    alignItems: {
                        xs: "flex-start",
                        md: "center",
                    },
                    mb: 4,
                }}
            >
                <Box>
                    <Stack
                        direction="row"
                        spacing={2}
                        sx={{
                            alignItems: "center",
                            flexWrap: "wrap",
                            mb: 1,
                        }}
                    >
                        <Typography
                            variant="h4"
                            component="h1"
                            sx={{
                                fontWeight: 700,
                            }}
                        >
                            {businessOffer.title}
                        </Typography>

                        <Chip
                            label={
                                businessOffer.active
                                    ? "Active"
                                    : "Inactive"
                            }
                            color={
                                businessOffer.active
                                    ? "success"
                                    : "error"
                            }
                            size="small"
                            sx={{
                                minWidth: 76,
                                fontWeight: 600,
                            }}
                        />
                    </Stack>

                    <Typography color="text.secondary">
                        Published by {businessOffer.companyName}
                    </Typography>
                </Box>

                <Stack
                    direction={{
                        xs: "column",
                        sm: "row",
                    }}
                    spacing={1.5}
                >
                    <Button
                        variant="outlined"
                        startIcon={<BusinessOutlinedIcon />}
                        onClick={() =>
                            navigate(
                                `/companies/${businessOffer.companyId}?fromOffer=${businessOffer.id}`,
                            )
                        }
                    >
                        View company
                    </Button>

                    <Button
                        variant="contained"
                        disabled={!businessOffer.active}
                        onClick={() =>
                            navigate(
                                `/business-offers/${businessOffer.id}/matches`,
                            )
                        }
                    >
                        Find matching partners
                    </Button>
                </Stack>
            </Stack>

            <Stack spacing={3}>
                <Paper
                    variant="outlined"
                    sx={{
                        p: 3,
                        borderRadius: 2,
                    }}
                >
                    <Typography
                        variant="h6"
                        component="h2"
                        sx={{
                            mb: 2,
                            fontWeight: 700,
                        }}
                    >
                        Description
                    </Typography>

                    <Typography
                        sx={{
                            whiteSpace: "pre-line",
                        }}
                    >
                        {businessOffer.description}
                    </Typography>
                </Paper>

                <Paper
                    variant="outlined"
                    sx={{
                        p: 3,
                        borderRadius: 2,
                    }}
                >
                    <Typography
                        variant="h6"
                        component="h2"
                        sx={{
                            mb: 3,
                            fontWeight: 700,
                        }}
                    >
                        Business offer information
                    </Typography>

                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                md: "repeat(2, minmax(0, 1fr))",
                            },
                            gap: 3,
                        }}
                    >
                        <InformationItem
                            icon={<HandshakeOutlinedIcon />}
                            label="Cooperation type"
                            value={formatCooperationType(
                                businessOffer.cooperationType,
                            )}
                        />

                        <InformationItem
                            icon={<PaymentsOutlinedIcon />}
                            label="Price range"
                            value={
                                businessOffer.priceRange
                                    ? `${formatMoneyValue(
                                        businessOffer.priceRange.min,
                                        businessOffer.priceRange.currency,
                                    )} – ${formatMoneyValue(
                                        businessOffer.priceRange.max,
                                        businessOffer.priceRange.currency,
                                    )}`
                                    : "Not specified"
                            }
                        />

                        <InformationItem
                            icon={<CalendarMonthOutlinedIcon />}
                            label="Availability period"
                            value={
                                businessOffer.availabilityPeriod
                                    ? `${formatDate(
                                        businessOffer.availabilityPeriod.from,
                                    )} – ${formatDate(
                                        businessOffer.availabilityPeriod.until,
                                    )}`
                                    : "Not specified"
                            }
                        />

                        <InformationItem
                            icon={<LocationOnOutlinedIcon />}
                            label="Service radius"
                            value={
                                businessOffer.serviceRadiusKm !== null
                                    ? `${businessOffer.serviceRadiusKm} km`
                                    : "Not specified"
                            }
                        />

                        <InformationItem
                            icon={<WorkHistoryOutlinedIcon />}
                            label="Experience"
                            value={
                                businessOffer.experienceYears !== null
                                    ? `${businessOffer.experienceYears} ${
                                        businessOffer.experienceYears === 1
                                            ? "year"
                                            : "years"
                                    }`
                                    : "Not specified"
                            }
                        />

                        <InformationItem
                            icon={<GroupsOutlinedIcon />}
                            label="Maximum number of partners"
                            value={String(
                                businessOffer.maxPartners,
                            )}
                        />
                    </Box>
                </Paper>

                <Paper
                    variant="outlined"
                    sx={{
                        p: 3,
                        borderRadius: 2,
                    }}
                >
                    <Typography
                        variant="h6"
                        component="h2"
                        sx={{
                            mb: 2,
                            fontWeight: 700,
                        }}
                    >
                        Offered specializations
                    </Typography>

                    <Box
                        sx={{
                            display: "flex",
                            flexWrap: "wrap",
                            gap: 1,
                        }}
                    >
                        {businessOffer.offeredSpecializations.map(
                            (specialization) => (
                                <Chip
                                    key={specialization.id}
                                    label={specialization.name}
                                    variant="outlined"
                                />
                            ),
                        )}
                    </Box>
                </Paper>

                <Paper
                    variant="outlined"
                    sx={{
                        p: 3,
                        borderRadius: 2,
                    }}
                >
                    <Typography
                        variant="h6"
                        component="h2"
                        sx={{
                            mb: 2,
                            fontWeight: 700,
                        }}
                    >
                        Metadata
                    </Typography>

                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                md: "repeat(2, minmax(0, 1fr))",
                            },
                            columnGap: 4,
                            rowGap: 3,
                        }}
                    >
                        <InformationItem
                            label="Created at"
                            value={formatDateTime(
                                businessOffer.createdAt,
                            )}
                        />

                        <InformationItem
                            label="Updated at"
                            value={formatDateTime(
                                businessOffer.updatedAt,
                            )}
                        />
                    </Box>
                </Paper>
            </Stack>
        </Box>
    );
}

function InformationItem({
                             icon,
                             label,
                             value,
                         }: InformationItemProps) {
    return (
        <Stack
            direction="row"
            spacing={2}
            sx={{
                alignItems: "center",
                minHeight: 56,
            }}
        >
            <Box
                sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    color: "text.secondary",
                    width: 24,
                    flexShrink: 0,
                }}
            >
                {icon}
            </Box>

            <Box>
                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        mb: 0.25,
                    }}
                >
                    {label}
                </Typography>

                <Typography
                    sx={{
                        fontWeight: 600,
                    }}
                >
                    {value}
                </Typography>
            </Box>
        </Stack>
    );
}
