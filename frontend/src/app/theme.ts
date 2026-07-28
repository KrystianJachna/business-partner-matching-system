import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
    palette: {
        mode: "light",
        primary: {
            main: "#1E3A5F",
            light: "#3F6B96",
            dark: "#142A44",
            contrastText: "#FFFFFF",
        },
        secondary: {
            main: "#4F86C6",
        },
        background: {
            default: "#F4F7FA",
            paper: "#FFFFFF",
        },
        text: {
            primary: "#172033",
            secondary: "#5F6B7A",
        },
        divider: "#DCE3EA",
    },
    shape: {
        borderRadius: 14,
    },
    typography: {
        fontFamily: "Roboto, Arial, sans-serif",
    },
    components: {
        MuiAppBar: {
            styleOverrides: {
                root: {
                    backgroundColor: "#1E3A5F",
                },
            },
        },
        MuiCard: {
            styleOverrides: {
                root: {
                    border: "1px solid #DCE3EA",
                    boxShadow: "0 8px 24px rgba(23, 32, 51, 0.06)",
                },
            },
        },
        MuiButton: {
            defaultProps: {
                disableElevation: true,
            },
            styleOverrides: {
                root: {
                    borderRadius: 10,
                    fontWeight: 600,
                    textTransform: "none",
                },
            },
        },
        MuiPaper: {
            styleOverrides: {
                outlined: {
                    borderColor: "#DCE3EA",
                },
            },
        },
        MuiChip: {
            styleOverrides: {
                label: {
                    fontWeight: 600,
                },
            },
        },
        MuiTableCell: {
            styleOverrides: {
                head: {
                    color: "#425168",
                    fontSize: "0.78rem",
                    letterSpacing: "0.04em",
                    textTransform: "uppercase",
                },
            },
        },
    },
});
