import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { CssBaseline } from "@mui/material";
import App from "./App";

const rootElement = document.getElementById("root");

if (!rootElement) {
    throw new Error("Root element was not found");
}

createRoot(rootElement).render(
    <StrictMode>
        <CssBaseline />
        <App />
    </StrictMode>,
);
