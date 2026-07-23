import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
    palette: {
        mode: "light",
        primary: {
            main: "#1565c0",
        },
    },
    shape: {
        borderRadius: 8,
    },
    typography: {
        fontFamily: "Roboto, Arial, sans-serif",
    },
});
