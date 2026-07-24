import {
    Alert,
    Box,
    Chip,
    CircularProgress,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";
import { useCompanies } from "../features/company/hooks/useCompanies";

export function CompaniesPage() {
    const {
        data: companies,
        isLoading,
        isError,
    } = useCompanies();

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

            <Typography
                color="text.secondary"
                sx={{
                    mb: 4,
                }}
            >
                Browse companies registered in the business partner matching
                system.
            </Typography>

            {!companies || companies.length === 0 ? (
                <Alert severity="info">
                    No companies have been registered yet.
                </Alert>
            ) : (
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Name</TableCell>
                                <TableCell>Industry</TableCell>
                                <TableCell>Location</TableCell>
                                <TableCell>Established</TableCell>
                                <TableCell>Specializations</TableCell>
                                <TableCell>Status</TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {companies.map((company) => (
                                <TableRow
                                    key={company.id}
                                    hover
                                >
                                    <TableCell>
                                        <Typography fontWeight={600}>
                                            {company.name}
                                        </Typography>
                                    </TableCell>

                                    <TableCell>
                                        {company.industry.name}
                                    </TableCell>

                                    <TableCell>
                                        {company.city}, {company.country}
                                    </TableCell>

                                    <TableCell>
                                        {company.establishedAt ?? "Not provided"}
                                    </TableCell>

                                    <TableCell>
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
                                                        key={specialization.id}
                                                        label={
                                                            specialization.name
                                                        }
                                                        size="small"
                                                    />
                                                ),
                                            )}
                                        </Box>
                                    </TableCell>

                                    <TableCell>
                                        <Chip
                                            label={
                                                company.active
                                                    ? "Active"
                                                    : "Inactive"
                                            }
                                            color={
                                                company.active
                                                    ? "success"
                                                    : "default"
                                            }
                                            size="small"
                                        />
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
