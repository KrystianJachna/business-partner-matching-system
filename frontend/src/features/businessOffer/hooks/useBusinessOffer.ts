import { useQuery } from "@tanstack/react-query";
import { getBusinessOffer } from "../api/businessOfferApi";

export function useBusinessOffer(
    businessOfferId: number | undefined,
) {
    return useQuery({
        queryKey: [
            "business-offers",
            businessOfferId,
        ],
        queryFn: () =>
            getBusinessOffer(businessOfferId!),
        enabled: businessOfferId !== undefined,
    });
}
