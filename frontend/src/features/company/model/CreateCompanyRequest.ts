export interface CreateCompanyRequest {
    name: string;
    description: string | null;
    industryId: number;
    specializationIds: number[];
    country: string;
    city: string;
    latitude: number;
    longitude: number;
    establishedAt: string | null;
    capabilities: string | null;
}
