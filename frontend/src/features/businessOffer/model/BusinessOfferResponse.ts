import type { CooperationType } from "../../../common/model/CooperationType";
import type { DateRangeResponse } from "../../../common/model/DateRange";
import type { MoneyRangeResponse } from "../../../common/model/MoneyRange";
import type { SpecializationResponse } from "../../specialization/model/SpecializationResponse";

export interface BusinessOfferResponse {
    id: number;
    companyId: number;
    companyName: string;
    title: string;
    description: string | null;
    cooperationType: CooperationType;
    offeredSpecializations: SpecializationResponse[];
    priceRange: MoneyRangeResponse | null;
    availabilityPeriod: DateRangeResponse | null;
    serviceRadiusKm: number | null;
    experienceYears: number | null;
    maxPartners: number;
    active: boolean;
    createdAt: string;
    updatedAt: string;
}
