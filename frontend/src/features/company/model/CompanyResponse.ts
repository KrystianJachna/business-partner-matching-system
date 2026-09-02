import type { IndustryResponse } from "../../industry/model/IndustryResponse";

export interface CompanyResponse {
    id: number;
    name: string;
    description: string;
    industry: IndustryResponse;
    country: string;
    city: string;
    latitude: number;
    longitude: number;
    establishedAt: string;
    capabilities: string;
    active: boolean;
}
