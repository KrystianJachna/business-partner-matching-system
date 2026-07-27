import {
    Alert,
    Box,
    Button,
    Chip,
    CircularProgress, IconButton,
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
import { useCompanies } from "../features/company/hooks/useCompanies";
import { formatDate } from "../common/utils/Formatters.ts";
import Tooltip from "@mui/material/Tooltip";
import { VisibilityOutlined as VisibilityOutlinedIcon } from "@mui/icons-material";


export function CompaniesPage() {
    const {
        data: companies,
        isLoading,
        isError,
    } = useCompanies();

    const navigate = useNavigate();

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

    if (isError) {
        return (
            <Alert severity="error">
                Failed to load companies.
            </Alert>
        );
    }

    return (
        <Box>
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
                    mb: 4,
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
                        Companies
                    </Typography>

                    <Typography color="text.secondary">
                        Browse companies registered in the business partner
                        matching system.
                    </Typography>
                </Box>

                <Button
                    variant="contained"
                    onClick={() => navigate("/companies/new")}
                >
                    Add company
                </Button>
            </Stack>

            {!companies || companies.length === 0 ? (
                <Alert severity="info">
                    No companies have been registered yet.
                </Alert>
            ) : (
                <TableContainer
                    component={Paper}
                    sx={{
                        borderRadius: 1,
                        overflow: "hidden",
                        border: "1px solid",
                        borderColor: "divider",
                        boxShadow: 1,
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
                                        py: 2,
                                    }}
                                >
                                    Name
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Industry
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Location
                                </TableCell>

                                <TableCell
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Established
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
                                    align="center"
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Status
                                </TableCell>

                                <TableCell
                                    align="center"
                                    sx={{
                                        fontWeight: 700,
                                        py: 2,
                                    }}
                                >
                                    Actions
                                </TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {companies.map((company, index) => (
                                <TableRow
                                    key={company.id}
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
                                    <TableCell sx={{ py: 2 }}>
                                        <Typography
                                            sx={{
                                                fontWeight: 700,
                                            }}
                                        >
                                            {company.name}
                                        </Typography>
                                    </TableCell>

                                    <TableCell sx={{ py: 2 }}>
                                        {company.industry.name}
                                    </TableCell>

                                    <TableCell sx={{ py: 2 }}>
                                        {company.city}, {company.country}
                                    </TableCell>

                                    <TableCell sx={{ py: 2 }}>
                                        {company.establishedAt
                                            ? formatDate(company.establishedAt)
                                            : "Not provided"}
                                    </TableCell>

                                    <TableCell sx={{ py: 2 }}>
                                        <Box
                                            sx={{
                                                display: "flex",
                                                flexWrap: "wrap",
                                                gap: 1,
                                            }}
                                        >
                                            {company.specializations.map(
                                                (specialization) => (
                                                    <Chip
                                                        key={
                                                            specialization.id
                                                        }
                                                        label={
                                                            specialization.name
                                                        }
                                                        size="small"
                                                        variant="outlined"
                                                        sx={{
                                                            backgroundColor:
                                                                "background.paper",
                                                        }}
                                                    />
                                                ),
                                            )}
                                        </Box>
                                    </TableCell>

                                    <TableCell
                                        align="center"
                                        sx={{
                                            py: 2,
                                        }}
                                    >
                                        <Chip
                                            label={
                                                company.active
                                                    ? "Active"
                                                    : "Inactive"
                                            }
                                            color={
                                                company.active
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

                                    <TableCell
                                        align="right"
                                        sx={{
                                            py: 2,
                                        }}
                                    >
                                        <Stack
                                            direction="row"
                                            spacing={1}
                                            sx={{
                                                justifyContent: "flex-end",
                                                alignItems: "center",
                                            }}
                                        >
                                            <Tooltip title="View details">
                                                <IconButton
                                                    onClick={() =>
                                                        navigate(`/companies/${company.id}`)
                                                    }
                                                    sx={{
                                                        color: "text.secondary",
                                                        "&:hover": {
                                                            color: "primary.main",
                                                            backgroundColor: "action.hover",
                                                        },
                                                    }}
                                                >
                                                    <VisibilityOutlinedIcon fontSize="small" />
                                                </IconButton>
                                            </Tooltip>
                                            <Button
                                                variant="contained"
                                                size="small"
                                                disabled={!company.active}
                                                onClick={() =>
                                                    navigate(
                                                        `/companies/${company.id}/needs/new`,
                                                    )
                                                }
                                                sx={{
                                                    whiteSpace: "nowrap",
                                                    minWidth: 0,
                                                    px: 2,
                                                    py: 0.75,
                                                    borderRadius: 2,
                                                    textTransform: "none",
                                                    fontWeight: 600,
                                                }}
                                            >
                                                Add need
                                            </Button>
                                        </Stack>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Box>
    );
}
