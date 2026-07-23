import { Container, Typography } from "@mui/material";

function App() {
    return (
        <Container sx={{ py: 4 }}>
            <Typography variant="h3" component="h1">
                Business Partner Matching
            </Typography>

            <Typography color="text.secondary" sx={{ mt: 2 }}>
                Frontend aplikacji działa poprawnie.
            </Typography>
        </Container>
    );
}

export default App;
