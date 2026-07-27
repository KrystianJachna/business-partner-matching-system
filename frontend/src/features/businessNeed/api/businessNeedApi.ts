import {
    apiGet,
    apiPost,
} from "../../../common/api/apiClient";
import type { BusinessNeedResponse } from "../model/BusinessNeedResponse";
import type { CreateBusinessNeedRequest } from "../model/CreateBusinessNeedRequest";

export function getCompanyBusinessNeeds(
    companyId: number,
): Promise<BusinessNeedResponse[]> {
    return apiGet<BusinessNeedResponse[]>(
        `/api/companies/${companyId}/needs`,
    );
}

export function getBusinessNeeds(): Promise<
    BusinessNeedResponse[]
> {
    return apiGet<BusinessNeedResponse[]>(
        "/api/business-needs",
    );
}

export function createBusinessNeed(
    companyId: number,
    request: CreateBusinessNeedRequest,
): Promise<BusinessNeedResponse> {
    return apiPost<
        BusinessNeedResponse,
        CreateBusinessNeedRequest
    >(
        `/api/companies/${companyId}/needs`,
        request,
    );
}
