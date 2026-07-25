import { useQuery } from "@tanstack/react-query";
import { getIndustries } from "../api/industryApi";

export function useIndustries() {
    return useQuery({
        queryKey: ["industries"],
        queryFn: getIndustries,
    });
}
