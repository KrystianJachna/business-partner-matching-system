import {
    useMutation,
    useQueryClient,
} from "@tanstack/react-query";
import { createBusinessNeed } from "../api/businessNeedApi";
import type { CreateBusinessNeedRequest } from "../model/CreateBusinessNeedRequest";

interface CreateBusinessNeedVariables {
    companyId: number;
    request: CreateBusinessNeedRequest;
}

export function useCreateBusinessNeed() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({
                         companyId,
                         request,
                     }: CreateBusinessNeedVariables) =>
            createBusinessNeed(companyId, request),

        onSuccess: async (_, variables) => {
            await queryClient.invalidateQueries({
                queryKey: [
                    "business-needs",
                    variables.companyId,
                ],
            });
        },
    });
}
