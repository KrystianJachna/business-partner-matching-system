import type { SpecializationResponse } from "./SpecializationResponse";

export interface SpecializationGroupResponse {
    industryId: number;
    industryName: string;
    specializations: SpecializationResponse[];
}
