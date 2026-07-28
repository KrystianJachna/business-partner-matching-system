import { useMutation } from "@tanstack/react-query";
import { runMatching } from "../api/matchingApi";

export function useRunMatching() {
    return useMutation({
        mutationFn: runMatching,
    });
}
