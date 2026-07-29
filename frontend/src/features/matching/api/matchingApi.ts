import { apiPost } from "../../../common/api/apiClient";
import type { MatchingAlgorithmType } from "../model/MatchingAlgorithmType";
import type { MatchingResponse } from "../model/MatchingResponse";

export function runMatching(
    algorithmType: MatchingAlgorithmType,
): Promise<MatchingResponse> {
    return apiPost<MatchingResponse, Record<string, never>>(
        `/api/matching?algorithmType=${algorithmType}`,
        {},
    );
}
