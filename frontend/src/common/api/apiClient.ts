const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

if (!API_BASE_URL) {
    throw new Error("VITE_API_BASE_URL is not configured");
}

export async function apiGet<T>(path: string): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: "GET",
        headers: {
            Accept: "application/json",
        },
    });

    if (!response.ok) {
        throw new Error(
            `Request failed with status ${response.status}`,
        );
    }

    return response.json() as Promise<T>;
}
