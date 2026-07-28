import type { CooperationType } from "../../../common/model/CooperationType";
import type { DateRangeRequest } from "../../../common/model/DateRange";
import type { MoneyRangeRequest } from "../../../common/model/MoneyRange";

export interface CreateBusinessOfferRequest {
    title: string;
    description: string | null;
    cooperationType: CooperationType;
    offeredSpecializationIds: number[];
    priceRange: MoneyRangeRequest | null;
    availabilityPeriod: DateRangeRequest | null;
    serviceRadiusKm: number | null;
    experienceYears: number | null;
    maxPartners: number;
}
