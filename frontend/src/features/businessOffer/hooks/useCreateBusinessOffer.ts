import {
    useMutation,
    useQueryClient,
} from "@tanstack/react-query";
import { createBusinessOffer } from "../api/businessOfferApi";
import type { CreateBusinessOfferRequest } from "../model/CreateBusinessOfferRequest";

interface CreateBusinessOfferVariables {
    companyId: number;
    request: CreateBusinessOfferRequest;
}

export function useCreateBusinessOffer() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({
                         companyId,
                         request,
                     }: CreateBusinessOfferVariables) =>
            createBusinessOffer(companyId, request),

        onSuccess: async (_, variables) => {
            await Promise.all([
                queryClient.invalidateQueries({
                    queryKey: ["business-offers"],
                }),
                queryClient.invalidateQueries({
                    queryKey: [
                        "companies",
                        variables.companyId,
                        "business-offers",
                    ],
                }),
            ]);
        },
    });
}
