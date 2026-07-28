import type { SxProps, Theme } from "@mui/material";
import type { CooperationType } from "../model/CooperationType";

export function getCooperationTypeStyle(
    cooperationType: CooperationType,
): SxProps<Theme> {
    switch (cooperationType) {
        case "SUBCONTRACTING":
            return {
                backgroundColor: "#FFF3E0",
                color: "#E65100",
            };

        case "SUPPLY":
            return {
                backgroundColor: "#E8F5E9",
                color: "#2E7D32",
            };

        case "DISTRIBUTION":
            return {
                backgroundColor: "#E0F2F1",
                color: "#00796B",
            };

        case "OUTSOURCING":
            return {
                backgroundColor: "#E3F2FD",
                color: "#1565C0",
            };

        case "CONSULTING":
            return {
                backgroundColor: "#FFF8E1",
                color: "#F57F17",
            };

        case "TECHNOLOGY_PARTNERSHIP":
            return {
                backgroundColor: "#F3E5F5",
                color: "#7B1FA2",
            };

        case "JOINT_PROJECT":
            return {
                backgroundColor: "#E8EAF6",
                color: "#3949AB",
            };
    }
}
