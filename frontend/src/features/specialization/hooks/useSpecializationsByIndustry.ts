import { useQuery } from "@tanstack/react-query";
import { getSpecializationsByIndustry } from "../api/specializationApi";

export function useSpecializationsByIndustry(
    industryId: number | null,
) {
    return useQuery({
        queryKey: ["specializations", industryId],
        queryFn: () => getSpecializationsByIndustry(industryId!),
        enabled: industryId !== null,
    });
}
