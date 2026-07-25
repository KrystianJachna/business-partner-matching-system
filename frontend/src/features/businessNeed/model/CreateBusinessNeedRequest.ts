import type { CooperationType } from "./CooperationType";
import type { DateRangeRequest } from "./DateRange";
import type { MoneyRangeRequest } from "./MoneyRange";

export interface CreateBusinessNeedRequest {
    title: string;
    description: string | null;
    cooperationType: CooperationType;
    requiredSpecializationIds: number[];
    budget: MoneyRangeRequest | null;
    requiredPeriod: DateRangeRequest | null;
    maxDistanceKm: number | null;
    minPartnerExperienceYears: number | null;
    maxPartners: number;
}
