import type { CurrencyCode } from "./CurrencyCode";

export interface MoneyRangeRequest {
    min: number;
    max: number;
    currency: CurrencyCode;
}

export interface MoneyRangeResponse {
    min: number;
    max: number;
    currency: CurrencyCode;
}
