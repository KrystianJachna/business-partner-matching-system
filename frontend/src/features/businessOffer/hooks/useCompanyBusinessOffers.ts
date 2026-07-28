import { useQuery } from "@tanstack/react-query";
import { getCompanyBusinessOffers } from "../api/businessOfferApi";

export function useCompanyBusinessOffers(
    companyId: number | undefined,
) {
    return useQuery({
        queryKey: [
            "companies",
            companyId,
            "business-offers",
        ],
        queryFn: () =>
            getCompanyBusinessOffers(companyId!),
        enabled: companyId !== undefined,
    });
}
