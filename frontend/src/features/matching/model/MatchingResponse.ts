export interface CriterionScoreResponse {
    criterion: string;
    score: number;
    weight: number;
    weightedScore: number;
}

export interface CompatibilityReasonResponse {
    code: string;
    description: string;
}

export interface MatchedPairResponse {
    needId: number;
    needTitle: string;
    needCompanyId: number;
    needCompanyName: string;
    offerId: number;
    offerTitle: string;
    offerCompanyId: number;
    offerCompanyName: string;
    totalScore: number;
    criterionScores: CriterionScoreResponse[];
    compatibilityReasons: CompatibilityReasonResponse[];
}

export interface MatchingResponse {
    matchCount: number;
    matches: MatchedPairResponse[];
}
