import { Paper, Stack, Typography } from "@mui/material";

export function DashboardPage() {
    return (
        <Stack spacing={3}>
            <Typography variant="h4" component="h1">
                Dashboard
            </Typography>

            <Paper sx={{ p: 3 }}>
                <Typography variant="h6">
                    Business Partner Matching System
                </Typography>

                <Typography color="text.secondary" sx={{ mt: 1 }}>
                    The frontend application is running successfully.
                </Typography>
            </Paper>
        </Stack>
    );
}
