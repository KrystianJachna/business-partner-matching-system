import type { IndustryResponse } from "../../industry/model/IndustryResponse";
import type { SpecializationResponse } from "../../specialization/model/SpecializationResponse";

export interface CompanyResponse {
    id: number;
    name: string;
    description: string;
    industry: IndustryResponse;
    specializations: SpecializationResponse[];
    country: string;
    city: string;
    latitude: number;
    longitude: number;
    establishedAt: string;
    capabilities: string;
    active: boolean;
}
