import type { CooperationType } from "../../../common/model/CooperationType.ts";
import type { DateRangeRequest } from "../../../common/model/DateRange.ts";
import type { MoneyRangeRequest } from "../../../common/model/MoneyRange.ts";

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
