import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import BusinessCenterOutlinedIcon from "@mui/icons-material/BusinessCenterOutlined";
import CalendarMonthOutlinedIcon from "@mui/icons-material/CalendarMonthOutlined";
import FactoryOutlinedIcon from "@mui/icons-material/FactoryOutlined";
import LocationOnOutlinedIcon from "@mui/icons-material/LocationOnOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress,
    Divider,
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
import { MapContainer, Marker, Popup, TileLayer } from "react-leaflet";
import { useCompanyBusinessNeeds } from "../features/businessNeed/hooks/useCompanyBusinessNeeds";
import { useCompanyBusinessOffers } from "../features/businessOffer/hooks/useCompanyBusinessOffers";
import {useNavigate, useParams, useSearchParams} from "react-router";
import { formatDate, formatCooperationType, formatMoneyValue } from "../common/utils/Formatters";
import { useCompany } from "../features/company/hooks/useCompany";

export function CompanyDetailsPage() {
    const [searchParams] = useSearchParams();

    const fromNeedId = Number(searchParams.get("fromNeed"));
    const fromOfferId = Number(searchParams.get("fromOffer"));

    const validFromNeedId =
        Number.isInteger(fromNeedId) && fromNeedId > 0
            ? fromNeedId
            : undefined;

    const validFromOfferId =
        Number.isInteger(fromOfferId) && fromOfferId > 0
            ? fromOfferId
            : undefined;

    const navigate = useNavigate();
    const { companyId } = useParams();

    const parsedCompanyId = companyId ? Number(companyId) : undefined;

    const validCompanyId =
        parsedCompanyId !== undefined &&
        Number.isInteger(parsedCompanyId) &&
        parsedCompanyId > 0
            ? parsedCompanyId
            : undefined;

    const {
        data: company,
        isLoading,
        isError,
    } = useCompany(validCompanyId);

    const {
        data: businessNeeds,
        isLoading: areBusinessNeedsLoading,
        isError: areBusinessNeedsError,
    } = useCompanyBusinessNeeds(validCompanyId);

    const {
        data: businessOffers,
        isLoading: areBusinessOffersLoading,
        isError: areBusinessOffersError,
    } = useCompanyBusinessOffers(validCompanyId);

    const handleBack = () => {
        if (validFromNeedId !== undefined) {
            navigate(`/business-needs/${validFromNeedId}`);
            return;
        }

        if (validFromOfferId !== undefined) {
            navigate(`/business-offers/${validFromOfferId}`);
            return;
        }

        navigate("/companies");
    };

    if (validCompanyId === undefined) {
        return (
            <Alert severity="error">
                Invalid company identifier.
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

    if (isError || !company) {
        return (
            <Alert severity="error">
                Failed to load company details.
            </Alert>
        );
    }

    const hasLocationCoordinates =
        company.latitude !== null &&
        company.latitude !== undefined &&
        company.longitude !== null &&
        company.longitude !== undefined;

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
                {validFromNeedId !== undefined
                    ? "Back to business need"
                    : validFromOfferId !== undefined
                        ? "Back to business offer"
                        : "Back to companies"}
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
                            {company.name}
                        </Typography>

                        <Chip
                            label={company.active ? "Active" : "Inactive"}
                            color={company.active ? "success" : "error"}
                            size="small"
                            sx={{
                                minWidth: 76,
                                fontWeight: 600,
                            }}
                        />
                    </Stack>

                    <Typography color="text.secondary">
                        Company profile and business information.
                    </Typography>
                </Box>

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
                        About
                    </Typography>

                    <Typography
                        color={
                            company.description
                                ? "text.primary"
                                : "text.secondary"
                        }
                        sx={{
                            whiteSpace: "pre-line",
                        }}
                    >
                        {company.description || "No description provided."}
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
                        Company information
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
                            icon={<FactoryOutlinedIcon />}
                            label="Industry"
                            value={company.industry.name}
                        />

                        <InformationItem
                            icon={<LocationOnOutlinedIcon />}
                            label="Location"
                            value={`${company.city}, ${company.country}`}
                        />

                        <InformationItem
                            icon={<CalendarMonthOutlinedIcon />}
                            label="Established"
                            value={
                                company.establishedAt
                                    ? formatDate(company.establishedAt)
                                    : "Not provided"
                            }
                        />

                        <InformationItem
                            icon={<BusinessCenterOutlinedIcon />}
                            label="Status"
                            value={company.active ? "Active" : "Inactive"}
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
                        Specializations
                    </Typography>

                    {company.specializations.length === 0 ? (
                        <Typography color="text.secondary">
                            No specializations provided.
                        </Typography>
                    ) : (
                        <Box
                            sx={{
                                display: "flex",
                                flexWrap: "wrap",
                                gap: 1,
                            }}
                        >
                            {company.specializations.map((specialization) => (
                                <Chip
                                    key={specialization.id}
                                    label={specialization.name}
                                    variant="outlined"
                                />
                            ))}
                        </Box>
                    )}
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
                        Capabilities
                    </Typography>

                    <Typography
                        color={
                            company.capabilities
                                ? "text.primary"
                                : "text.secondary"
                        }
                        sx={{
                            whiteSpace: "pre-line",
                        }}
                    >
                        {company.capabilities || "No capabilities provided."}
                    </Typography>
                </Paper>

                <Paper
                    variant="outlined"
                    sx={{
                        overflow: "hidden",
                        borderRadius: 2,
                    }}
                >
                    <Box sx={{ p: 3 }}>
                        <Typography
                            variant="h6"
                            component="h2"
                            sx={{
                                mb: 1,
                                fontWeight: 700,
                            }}
                        >
                            Location
                        </Typography>

                        <Typography color="text.secondary">
                            {company.city}, {company.country}
                        </Typography>
                    </Box>

                    <Divider />

                    {hasLocationCoordinates ? (
                        <Box
                            sx={{
                                height: 380,
                                width: "100%",
                            }}
                        >
                            <MapContainer
                                center={[
                                    company.latitude,
                                    company.longitude,
                                ]}
                                zoom={13}
                                scrollWheelZoom={false}
                                style={{
                                    height: "100%",
                                    width: "100%",
                                }}
                            >
                                <TileLayer
                                    attribution='&copy; OpenStreetMap contributors'
                                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                />

                                <Marker
                                    position={[
                                        company.latitude,
                                        company.longitude,
                                    ]}
                                >
                                    <Popup>
                                        <strong>{company.name}</strong>
                                        <br />
                                        {company.city}, {company.country}
                                    </Popup>
                                </Marker>
                            </MapContainer>
                        </Box>
                    ) : (
                        <Box sx={{ p: 3 }}>
                            <Alert severity="info">
                                Location coordinates are not available for this
                                company.
                            </Alert>
                        </Box>
                    )}
                </Paper>

                <Paper
                    variant="outlined"
                    sx={{
                        p: 3,
                        borderRadius: 2,
                    }}
                >
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                        sx={{
                            justifyContent: "space-between",
                            alignItems: {
                                xs: "flex-start",
                                sm: "center",
                            },
                            mb: 3,
                        }}
                    >
                        <Box>
                            <Typography
                                variant="h6"
                                component="h2"
                                sx={{
                                    fontWeight: 700,
                                    mb: 0.5,
                                }}
                            >
                                Business needs
                            </Typography>

                            <Typography color="text.secondary">
                                Business needs published by this company.
                            </Typography>
                        </Box>

                        <Button
                            variant="contained"
                            disabled={!company.active}
                            onClick={() =>
                                navigate(`/companies/${company.id}/needs/new`)
                            }
                        >
                            Add business need
                        </Button>
                    </Stack>

                    {areBusinessNeedsLoading ? (
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                                py: 5,
                            }}
                        >
                            <CircularProgress size={32} />
                        </Box>
                    ) : areBusinessNeedsError ? (
                        <Alert severity="error">
                            Failed to load business needs.
                        </Alert>
                    ) : !businessNeeds || businessNeeds.length === 0 ? (
                        <Alert severity="info">
                            This company has not published any business needs yet.
                        </Alert>
                    ) : (
                        <TableContainer
                            sx={{
                                border: "1px solid",
                                borderColor: "divider",
                                borderRadius: 1,
                            }}
                        >
                            <Table>
                                <TableHead>
                                    <TableRow
                                        sx={{
                                            backgroundColor: "grey.100",
                                        }}
                                    >
                                        <TableCell
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Business need
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Cooperation type
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Budget
                                        </TableCell>

                                        <TableCell
                                            align="center"
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Status
                                        </TableCell>

                                        <TableCell
                                            align="center"
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Actions
                                        </TableCell>
                                    </TableRow>
                                </TableHead>

                                <TableBody>
                                    {businessNeeds.map((businessNeed, index) => (
                                        <TableRow
                                            key={businessNeed.id}
                                            hover
                                            sx={{
                                                backgroundColor:
                                                    index % 2 === 0
                                                        ? "background.paper"
                                                        : "grey.50",
                                                "&:last-child td": {
                                                    borderBottom: 0,
                                                },
                                            }}
                                        >
                                            <TableCell>
                                                <Typography
                                                    sx={{
                                                        fontWeight: 700,
                                                    }}
                                                >
                                                    {businessNeed.title}
                                                </Typography>

                                                <Typography
                                                    variant="body2"
                                                    color="text.secondary"
                                                >
                                                    {
                                                        businessNeed.requiredSpecializations
                                                            .length
                                                    }{" "}
                                                    specializations
                                                </Typography>
                                            </TableCell>

                                            <TableCell>
                                                <Chip
                                                    label={formatCooperationType(
                                                        businessNeed.cooperationType,
                                                    )}
                                                    size="small"
                                                    variant="outlined"
                                                />
                                            </TableCell>

                                            <TableCell>
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

                                            <TableCell align="center">
                                                <Chip
                                                    label={
                                                        businessNeed.active
                                                            ? "Active"
                                                            : "Inactive"
                                                    }
                                                    color={
                                                        businessNeed.active
                                                            ? "success"
                                                            : "error"
                                                    }
                                                    size="small"
                                                    sx={{
                                                        minWidth: 76,
                                                        fontWeight: 600,
                                                    }}
                                                />
                                            </TableCell>

                                            <TableCell align="center">
                                                <Tooltip title="View details">
                                                    <IconButton
                                                        onClick={() =>
                                                            navigate(
                                                                `/business-needs/${businessNeed.id}?fromCompany=${company.id}`,
                                                            )
                                                        }
                                                        sx={{
                                                            color: "text.secondary",
                                                            "&:hover": {
                                                                color: "primary.main",
                                                                backgroundColor:
                                                                    "action.hover",
                                                            },
                                                        }}
                                                    >
                                                        <VisibilityOutlinedIcon fontSize="small" />
                                                    </IconButton>
                                                </Tooltip>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    )}
                </Paper>

                <Paper
                    variant="outlined"
                    sx={{
                        p: 3,
                        borderRadius: 2,
                    }}
                >
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                        sx={{
                            justifyContent: "space-between",
                            alignItems: {
                                xs: "flex-start",
                                sm: "center",
                            },
                            mb: 3,
                        }}
                    >
                        <Box>
                            <Typography
                                variant="h6"
                                component="h2"
                                sx={{
                                    fontWeight: 700,
                                    mb: 0.5,
                                }}
                            >
                                Business offers
                            </Typography>

                            <Typography color="text.secondary">
                                Business offers published by this company.
                            </Typography>
                        </Box>

                        <Button
                            variant="contained"
                            disabled={!company.active}
                            onClick={() =>
                                navigate(`/companies/${company.id}/offers/new`)
                            }
                        >
                            Add business offer
                        </Button>
                    </Stack>

                    {areBusinessOffersLoading ? (
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "center",
                                py: 5,
                            }}
                        >
                            <CircularProgress size={32} />
                        </Box>
                    ) : areBusinessOffersError ? (
                        <Alert severity="error">
                            Failed to load business offers.
                        </Alert>
                    ) : !businessOffers || businessOffers.length === 0 ? (
                        <Alert severity="info">
                            This company has not published any business offers yet.
                        </Alert>
                    ) : (
                        <TableContainer
                            sx={{
                                border: "1px solid",
                                borderColor: "divider",
                                borderRadius: 1,
                            }}
                        >
                            <Table>
                                <TableHead>
                                    <TableRow
                                        sx={{
                                            backgroundColor: "grey.100",
                                        }}
                                    >
                                        <TableCell
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Business offer
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Cooperation type
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Price range
                                        </TableCell>

                                        <TableCell
                                            align="center"
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Status
                                        </TableCell>

                                        <TableCell
                                            align="center"
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            Actions
                                        </TableCell>
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
                                                    "&:last-child td": {
                                                        borderBottom: 0,
                                                    },
                                                }}
                                            >
                                                <TableCell>
                                                    <Typography
                                                        sx={{
                                                            fontWeight: 700,
                                                        }}
                                                    >
                                                        {businessOffer.title}
                                                    </Typography>

                                                    <Typography
                                                        variant="body2"
                                                        color="text.secondary"
                                                    >
                                                        {
                                                            businessOffer
                                                                .offeredSpecializations
                                                                .length
                                                        }{" "}
                                                        specializations
                                                    </Typography>
                                                </TableCell>

                                                <TableCell>
                                                    <Chip
                                                        label={formatCooperationType(
                                                            businessOffer.cooperationType,
                                                        )}
                                                        size="small"
                                                        variant="outlined"
                                                    />
                                                </TableCell>

                                                <TableCell>
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

                                                <TableCell align="center">
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
                                                </TableCell>

                                                <TableCell align="center">
                                                    <Tooltip title="View details">
                                                        <IconButton
                                                            onClick={() =>
                                                                navigate(
                                                                    `/business-offers/${businessOffer.id}?fromCompany=${company.id}`,
                                                                )
                                                            }
                                                            sx={{
                                                                color: "text.secondary",
                                                                "&:hover": {
                                                                    color: "primary.main",
                                                                    backgroundColor:
                                                                        "action.hover",
                                                                },
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
                </Paper>
            </Stack>
        </Box>
    );
}

interface InformationItemProps {
    icon: React.ReactNode;
    label: string;
    value: string;
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
                alignItems: "flex-start",
            }}
        >
            <Box
                sx={{
                    display: "flex",
                    color: "text.secondary",
                    pt: 0.25,
                }}
            >
                {icon}
            </Box>

            <Box>
                <Typography
                    variant="body2"
                    color="text.secondary"
                    sx={{
                        mb: 0.5,
                    }}
                >
                    {label}
                </Typography>

                <Typography sx={{ fontWeight: 600 }}>
                    {value}
                </Typography>
            </Box>
        </Stack>
    );
}
