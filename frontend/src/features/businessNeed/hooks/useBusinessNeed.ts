import { useQuery } from "@tanstack/react-query";
import { getBusinessNeed } from "../api/businessNeedApi";

export function useBusinessNeed(
    businessNeedId: number | undefined,
) {
    return useQuery({
        queryKey: ["business-needs", businessNeedId],
        queryFn: () => getBusinessNeed(businessNeedId!),
        enabled: businessNeedId !== undefined,
    });
}
