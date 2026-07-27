import { useQuery } from "@tanstack/react-query";
import { getBusinessNeeds } from "../api/businessNeedApi";

export function useBusinessNeeds() {
    return useQuery({
        queryKey: ["business-needs"],
        queryFn: getBusinessNeeds,
    });
}
