import { useQuery } from "@tanstack/react-query";
import { getCompanyBusinessNeeds } from "../api/businessNeedApi";

export function useCompanyBusinessNeeds(
    companyId: number | undefined,
) {
    return useQuery({
        queryKey: ["companies", companyId, "business-needs"],
        queryFn: () => getCompanyBusinessNeeds(companyId!),
        enabled: companyId !== undefined,
    });
}
