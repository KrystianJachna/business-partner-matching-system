import { apiGet, apiPost } from "../../../common/api/apiClient";
import type { CompanyResponse } from "../model/CompanyResponse";
import type { CreateCompanyRequest } from "../model/CreateCompanyRequest";

export function getCompanies(): Promise<CompanyResponse[]> {
    return apiGet<CompanyResponse[]>("/api/companies");
}

export function getCompany(companyId: number): Promise<CompanyResponse> {
    return apiGet<CompanyResponse>(`/api/companies/${companyId}`);
}

export function createCompany(
    request: CreateCompanyRequest,
): Promise<CompanyResponse> {
    return apiPost<CompanyResponse, CreateCompanyRequest>(
        "/api/companies",
        request,
    );
}
