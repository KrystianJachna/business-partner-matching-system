import { useQuery } from "@tanstack/react-query";
import { getSpecializationGroups } from "../api/specializationApi";

export function useSpecializationGroups() {
    return useQuery({
        queryKey: [
            "specializations",
            "grouped",
        ],
        queryFn: getSpecializationGroups,
    });
}
