import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import BusinessOutlinedIcon from "@mui/icons-material/BusinessOutlined";
import CalendarMonthOutlinedIcon from "@mui/icons-material/CalendarMonthOutlined";
import GroupsOutlinedIcon from "@mui/icons-material/GroupsOutlined";
import LocationOnOutlinedIcon from "@mui/icons-material/LocationOnOutlined";
import WorkHistoryOutlinedIcon from "@mui/icons-material/WorkHistoryOutlined";
import HandshakeOutlinedIcon from "@mui/icons-material/HandshakeOutlined";
import PaymentsOutlinedIcon from "@mui/icons-material/PaymentsOutlined";
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
import type {ReactNode} from "react";
import {useNavigate, useParams, useSearchParams} from "react-router";
import {formatDate, formatMoneyValue, formatCooperationType, formatDateTime } from "../common/utils/Formatters";
import {useBusinessNeed} from "../features/businessNeed/hooks/useBusinessNeed";


interface InformationItemProps {
    icon?: ReactNode;
    label: string;
    value: string;
}

export function BusinessNeedDetailsPage() {
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

    const {businessNeedId} = useParams();

    const parsedBusinessNeedId = businessNeedId
        ? Number(businessNeedId)
        : undefined;

    const validBusinessNeedId =
        parsedBusinessNeedId !== undefined &&
        Number.isInteger(parsedBusinessNeedId) &&
        parsedBusinessNeedId > 0
            ? parsedBusinessNeedId
            : undefined;

    const {
        data: businessNeed,
        isLoading,
        isError,
    } = useBusinessNeed(validBusinessNeedId);

    const handleBack = () => {
        if (validFromCompanyId !== undefined) {
            navigate(`/companies/${validFromCompanyId}`);
            return;
        }

        navigate("/business-needs");
    };

    if (validBusinessNeedId === undefined) {
        return (
            <Alert severity="error">
                Invalid business need identifier.
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
                <CircularProgress/>
            </Box>
        );
    }

    if (isError || !businessNeed) {
        return (
            <Alert severity="error">
                Failed to load business need details.
            </Alert>
        );
    }

    return (
        <Box>
            <Button
                startIcon={<ArrowBackOutlinedIcon/>}
                onClick={handleBack}
                sx={{
                    mb: 3,
                    textTransform: "none",
                }}
            >
                {validFromCompanyId !== undefined
                    ? "Back to company"
                    : "Back to business needs"}
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
                            {businessNeed.title}
                        </Typography>

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
                    </Stack>

                    <Typography color="text.secondary">
                        Published by {businessNeed.companyName}
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
                                `/companies/${businessNeed.companyId}?fromNeed=${businessNeed.id}`,
                            )
                        }
                    >
                        View company
                    </Button>

                    <Button
                        variant="contained"
                        disabled={!businessNeed.active}
                        onClick={() =>
                            navigate(
                                `/business-needs/${businessNeed.id}/matches`,
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
                        {businessNeed.description}
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
                        Business need information
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
                            icon={<HandshakeOutlinedIcon/>}
                            label="Cooperation type"
                            value={formatCooperationType(
                                businessNeed.cooperationType,
                            )}
                        />

                        <InformationItem
                            icon={<PaymentsOutlinedIcon/>}
                            label="Budget"
                            value={
                                businessNeed.budget
                                    ? `${formatMoneyValue(
                                        businessNeed.budget.min,
                                        businessNeed.budget.currency,
                                    )} – ${formatMoneyValue(
                                        businessNeed.budget.max,
                                        businessNeed.budget.currency,
                                    )}`
                                    : "Not specified"
                            }
                        />

                        <InformationItem
                            icon={<CalendarMonthOutlinedIcon/>}
                            label="Required period"
                            value={
                                businessNeed.requiredPeriod
                                    ? `${formatDate(
                                        businessNeed.requiredPeriod.from,
                                    )} – ${formatDate(
                                        businessNeed.requiredPeriod.until,
                                    )}`
                                    : "Not specified"
                            }
                        />

                        <InformationItem
                            icon={<LocationOnOutlinedIcon />}
                            label="Maximum distance"
                            value={
                                businessNeed.maxDistanceKm !== null
                                    ? `${businessNeed.maxDistanceKm} km`
                                    : "No distance limit"
                            }
                        />

                        <InformationItem
                            icon={<WorkHistoryOutlinedIcon />}
                            label="Minimum partner experience"
                            value={
                                businessNeed.minPartnerExperienceYears !== null
                                    ? `${businessNeed.minPartnerExperienceYears} years`
                                    : "Any experience"
                            }
                        />

                        <InformationItem
                            icon={<GroupsOutlinedIcon/>}
                            label="Maximum number of partners"
                            value={String(businessNeed.maxPartners)}
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
                        Required specializations
                    </Typography>

                    <Box
                        sx={{
                            display: "flex",
                            flexWrap: "wrap",
                            gap: 1,
                        }}
                    >
                        {businessNeed.requiredSpecializations.map(
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
                                businessNeed.createdAt,
                            )}
                        />

                        <InformationItem
                            label="Updated at"
                            value={formatDateTime(
                                businessNeed.updatedAt,
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
                    sx={{mb: 0.25}}
                >
                    {label}
                </Typography>

                <Typography sx={{fontWeight: 600}}>
                    {value}
                </Typography>
            </Box>
        </Stack>
    );
}
