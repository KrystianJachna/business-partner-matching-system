import type { SpecializationResponse } from "../../specialization/model/SpecializationResponse";
import type { CooperationType } from "./CooperationType";
import type { DateRangeResponse } from "./DateRange";
import type { MoneyRangeResponse } from "./MoneyRange";

export interface BusinessNeedResponse {
    id: number;
    companyId: number;
    companyName: string;
    title: string;
    description: string | null;
    cooperationType: CooperationType;
    requiredSpecializations: SpecializationResponse[];
    budget: MoneyRangeResponse | null;
    requiredPeriod: DateRangeResponse | null;
    maxDistanceKm: number | null;
    minPartnerExperienceYears: number | null;
    maxPartners: number;
    active: boolean;
    createdAt: string;
    updatedAt: string;
}
