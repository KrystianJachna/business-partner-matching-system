import { Button, Stack, Typography } from "@mui/material";
import { Link } from "react-router";

export function NotFoundPage() {
    return (
        <Stack spacing={2}>
            <Typography variant="h4" component="h1">
                Page not found
            </Typography>

            <Typography color="text.secondary">
                The page you are looking for does not exist.
            </Typography>

            <Button
                component={Link}
                to="/dashboard"
                variant="contained"
            >
                Go to dashboard
            </Button>
        </Stack>
    );
}
