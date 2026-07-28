import { useQuery } from "@tanstack/react-query";
import { getBusinessOffers } from "../api/businessOfferApi";

export function useBusinessOffers() {
    return useQuery({
        queryKey: ["business-offers"],
        queryFn: getBusinessOffers,
    });
}
