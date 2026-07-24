import { apiGet } from "../../../common/api/apiClient";
import type { IndustryResponse } from "../model/IndustryResponse";

export function getIndustries(): Promise<IndustryResponse[]> {
    return apiGet<IndustryResponse[]>("/api/industries");
}
