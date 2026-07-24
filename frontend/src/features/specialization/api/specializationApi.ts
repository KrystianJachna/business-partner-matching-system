import { apiGet } from "../../../common/api/apiClient";
import type { SpecializationResponse } from "../model/SpecializationResponse";

export function getSpecializationsByIndustry(
    industryId: number,
): Promise<SpecializationResponse[]> {
    return apiGet<SpecializationResponse[]>(
        `/api/industries/${industryId}/specializations`,
    );
}
