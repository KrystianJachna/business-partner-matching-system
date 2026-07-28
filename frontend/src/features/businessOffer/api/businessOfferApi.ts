import {
    apiGet,
    apiPost,
} from "../../../common/api/apiClient";
import type { BusinessOfferResponse } from "../model/BusinessOfferResponse";
import type { CreateBusinessOfferRequest } from "../model/CreateBusinessOfferRequest";

export function getCompanyBusinessOffers(
    companyId: number,
): Promise<BusinessOfferResponse[]> {
    return apiGet<BusinessOfferResponse[]>(
        `/api/companies/${companyId}/offers`,
    );
}

export function getBusinessOffers(): Promise<
    BusinessOfferResponse[]
> {
    return apiGet<BusinessOfferResponse[]>(
        "/api/business-offers",
    );
}

export function createBusinessOffer(
    companyId: number,
    request: CreateBusinessOfferRequest,
): Promise<BusinessOfferResponse> {
    return apiPost<
        BusinessOfferResponse,
        CreateBusinessOfferRequest
    >(
        `/api/companies/${companyId}/offers`,
        request,
    );
}

export function getBusinessOffer(
    businessOfferId: number,
): Promise<BusinessOfferResponse> {
    return apiGet<BusinessOfferResponse>(
        `/api/business-offers/${businessOfferId}`,
    );
}
