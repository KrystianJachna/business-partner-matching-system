import { useQuery } from "@tanstack/react-query";
import { getCompany } from "../api/companyApi";

export function useCompany(companyId: number | undefined) {
    return useQuery({
        queryKey: ["companies", companyId],
        queryFn: () => getCompany(companyId!),
        enabled: companyId !== undefined,
    });
}
