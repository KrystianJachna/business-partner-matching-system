export interface GeocodingResult {
    latitude: number;
    longitude: number;
    displayName: string;
    country: string;
    city: string;
}

interface NominatimSearchResult {
    lat: string;
    lon: string;
    display_name: string;
    address: {
        country?: string;
        city?: string;
        town?: string;
        village?: string;
        municipality?: string;
    };
}

export async function findLocation(
    country: string,
    city: string,
): Promise<GeocodingResult | null> {
    const searchParams = new URLSearchParams({
        country,
        city,
        format: "jsonv2",
        addressdetails: "1",
        limit: "1",
        "accept-language": "en",
    });

    const response = await fetch(
        `https://nominatim.openstreetmap.org/search?${searchParams.toString()}`,
        {
            headers: {
                Accept: "application/json",
            },
        },
    );

    if (!response.ok) {
        throw new Error(
            `Geocoding request failed with status ${response.status}`,
        );
    }

    const results =
        (await response.json()) as NominatimSearchResult[];

    const firstResult = results[0];

    if (!firstResult) {
        return null;
    }

    const resolvedCity =
        firstResult.address.city ??
        firstResult.address.town ??
        firstResult.address.village ??
        firstResult.address.municipality;

    const resolvedCountry = firstResult.address.country;

    if (!resolvedCity || !resolvedCountry) {
        return null;
    }

    return {
        latitude: Number(firstResult.lat),
        longitude: Number(firstResult.lon),
        displayName: firstResult.display_name,
        country: resolvedCountry,
        city: resolvedCity,
    };
}
