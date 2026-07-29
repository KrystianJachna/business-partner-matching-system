import { apiPost } from "../../../common/api/apiClient";
import type { MatchingResponse } from "../model/MatchingResponse";

export function runMatching(): Promise<MatchingResponse> {
    return apiPost<MatchingResponse, Record<string, never>>(
        "/api/matching",
        {},
    );
}
