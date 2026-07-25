import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createCompany } from "../api/companyApi";

export function useCreateCompany() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: createCompany,
        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["companies"],
            });
        },
    });
}
