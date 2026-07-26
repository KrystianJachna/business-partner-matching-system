import { apiGet } from "../../../common/api/apiClient";
import type { SpecializationResponse } from "../model/SpecializationResponse";
import type { SpecializationGroupResponse } from "../model/SpecializationGroupResponse";

export function getSpecializationsByIndustry(
    industryId: number,
): Promise<SpecializationResponse[]> {
    return apiGet<SpecializationResponse[]>(
        `/api/industries/${industryId}/specializations`,
    );
}


export function getSpecializationGroups(): Promise<
    SpecializationGroupResponse[]
> {
    return apiGet<SpecializationGroupResponse[]>(
        "/api/specializations/grouped",
    );
}
