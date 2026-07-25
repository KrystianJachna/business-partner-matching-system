import type { BusinessNeedResponse } from "../model/BusinessNeedResponse";
import type { CreateBusinessNeedRequest } from "../model/CreateBusinessNeedRequest";

export async function createBusinessNeed(
    companyId: number,
    request: CreateBusinessNeedRequest,
): Promise<BusinessNeedResponse> {
    const response = await fetch(
        `/api/companies/${companyId}/needs`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        },
    );

    if (!response.ok) {
        throw new Error(
            `Failed to create business need: ${response.status}`,
        );
    }

    return response.json() as Promise<BusinessNeedResponse>;
}
