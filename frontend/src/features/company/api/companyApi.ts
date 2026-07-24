import { apiGet } from "../../../common/api/apiClient";
import type { CompanyResponse } from "../model/CompanyResponse";

export function getCompanies(): Promise<CompanyResponse[]> {
    return apiGet<CompanyResponse[]>("/api/companies");
}
