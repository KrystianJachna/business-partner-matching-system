import SearchOffOutlinedIcon from "@mui/icons-material/SearchOffOutlined";
import { Button, Paper, Stack, Typography } from "@mui/material";
import { Link } from "react-router";

export function NotFoundPage() {
    return (
        <Paper
            variant="outlined"
            sx={{
                maxWidth: 620,
                mx: "auto",
                p: { xs: 4, md: 7 },
                textAlign: "center",
            }}
        >
            <SearchOffOutlinedIcon
                sx={{ fontSize: 56, color: "primary.light", mb: 1 }}
            />

            <Stack spacing={2} sx={{ alignItems: "center" }}>
                <Typography variant="h4" component="h1" sx={{ fontWeight: 700 }}>
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
        </Paper>
    );
}
