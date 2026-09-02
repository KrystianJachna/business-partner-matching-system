import {
    useRef,
    useState,
} from "react";
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import { useIndustries } from "../../industry/hooks/useIndustries";
import { findLocation } from "../api/geocodingApi";
import { useCreateCompany } from "../hooks/useCreateCompany";
import type { CreateCompanyRequest } from "../model/CreateCompanyRequest";
import { CompanyLocationPicker } from "./CompanyLocationPicker";

export function CompanyForm() {
    const navigate = useNavigate();

    const countryInputRef = useRef<HTMLInputElement>(null);
    const locationSectionRef = useRef<HTMLDivElement>(null);

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [industryId, setIndustryId] =
        useState<number | null>(null);
    const [country, setCountry] = useState("");
    const [city, setCity] = useState("");
    const [latitude, setLatitude] = useState("");
    const [longitude, setLongitude] = useState("");
    const [establishedAt, setEstablishedAt] = useState("");
    const [capabilities, setCapabilities] = useState("");

    const [isFindingLocation, setIsFindingLocation] =
        useState(false);
    const [locationSearchError, setLocationSearchError] =
        useState<string | null>(null);
    const [locationVerified, setLocationVerified] =
        useState(false);

    const {
        data: industries,
        isLoading: industriesLoading,
        isError: industriesError,
    } = useIndustries();

    const createCompanyMutation = useCreateCompany();

    function moveToLocationFields() {
        locationSectionRef.current?.scrollIntoView({
            behavior: "smooth",
            block: "center",
        });

        window.setTimeout(() => {
            countryInputRef.current?.focus();
        }, 400);
    }

    async function resolveLocation() {
        if (!country.trim() || !city.trim()) {
            setLocationSearchError(
                "Enter both country and city.",
            );
            setLocationVerified(false);
            moveToLocationFields();

            return null;
        }

        setIsFindingLocation(true);
        setLocationSearchError(null);
        setLocationVerified(false);

        try {
            const result = await findLocation(
                country.trim(),
                city.trim(),
            );

            if (!result) {
                setLocationSearchError(
                    "Location could not be found. Check the country and city.",
                );
                moveToLocationFields();

                return null;
            }

            setCountry(result.country);
            setCity(result.city);
            setLatitude(result.latitude.toFixed(6));
            setLongitude(result.longitude.toFixed(6));
            setLocationVerified(true);

            return result;
        } catch {
            setLocationSearchError(
                "Failed to search for the location. Try again.",
            );
            moveToLocationFields();

            return null;
        } finally {
            setIsFindingLocation(false);
        }
    }

    async function handleFindLocation() {
        await resolveLocation();
    }

    async function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        if (industryId === null) {
            return;
        }

        let resolvedLatitude = latitude;
        let resolvedLongitude = longitude;
        let resolvedCountry = country;
        let resolvedCity = city;

        if (!locationVerified) {
            const location = await resolveLocation();

            if (!location) {
                return;
            }

            resolvedLatitude =
                location.latitude.toFixed(6);
            resolvedLongitude =
                location.longitude.toFixed(6);
            resolvedCountry = location.country;
            resolvedCity = location.city;
        }

        const request: CreateCompanyRequest = {
            name: name.trim(),
            description:
                description.trim() || null,
            industryId,
            country: resolvedCountry.trim(),
            city: resolvedCity.trim(),
            latitude: Number(resolvedLatitude),
            longitude: Number(resolvedLongitude),
            establishedAt:
                establishedAt || null,
            capabilities:
                capabilities.trim() || null,
        };

        createCompanyMutation.mutate(request, {
            onSuccess: () => {
                navigate("/companies");
            },
        });
    }

    return (
        <Paper
            sx={{
                maxWidth: 900,
                p: 4,
                borderRadius: 3,
                border: "1px solid",
                borderColor: "divider",
                boxShadow: 1,
            }}
        >
            <Box
                component="form"
                onSubmit={handleSubmit}
            >
                <Stack spacing={4}>
                    {createCompanyMutation.isError && (
                        <Alert severity="error">
                            Failed to create company.
                        </Alert>
                    )}

                    {industriesError && (
                        <Alert severity="error">
                            Failed to load industries.
                        </Alert>
                    )}

                    <Box>
                        <Typography
                            variant="h6"
                            sx={{
                                mb: 2,
                                fontWeight: 700,
                            }}
                        >
                            General information
                        </Typography>

                        <Stack spacing={3}>
                            <TextField
                                label="Company name"
                                value={name}
                                onChange={(event) =>
                                    setName(
                                        event.target.value,
                                    )
                                }
                                required
                                fullWidth
                            />

                            <TextField
                                label="Description"
                                value={description}
                                onChange={(event) =>
                                    setDescription(
                                        event.target.value,
                                    )
                                }
                                multiline
                                minRows={3}
                                fullWidth
                            />

                            <FormControl
                                required
                                fullWidth
                            >
                                <InputLabel id="industry-label">
                                    Industry
                                </InputLabel>

                                <Select
                                    labelId="industry-label"
                                    value={industryId ?? ""}
                                    label="Industry"
                                    disabled={
                                        industriesLoading
                                    }
                                    onChange={(event) =>
                                        setIndustryId(
                                            Number(
                                                event.target
                                                    .value,
                                            ),
                                        )
                                    }
                                >
                                    {industries?.map(
                                        (industry) => (
                                            <MenuItem
                                                key={
                                                    industry.id
                                                }
                                                value={
                                                    industry.id
                                                }
                                            >
                                                {
                                                    industry.name
                                                }
                                            </MenuItem>
                                        ),
                                    )}
                                </Select>
                            </FormControl>
                        </Stack>
                    </Box>

                    <Box
                        ref={locationSectionRef}
                    >
                        <Typography
                            variant="h6"
                            sx={{
                                mb: 2,
                                fontWeight: 700,
                            }}
                        >
                            Location
                        </Typography>

                        <Stack spacing={3}>
                            <Stack
                                direction={{
                                    xs: "column",
                                    sm: "row",
                                }}
                                spacing={2}
                            >
                                <TextField
                                    inputRef={
                                        countryInputRef
                                    }
                                    label="Country"
                                    value={country}
                                    onChange={(event) => {
                                        setCountry(
                                            event.target.value,
                                        );
                                        setLatitude("");
                                        setLongitude("");
                                        setLocationVerified(
                                            false,
                                        );
                                        setLocationSearchError(
                                            null,
                                        );
                                    }}
                                    required
                                    fullWidth
                                />

                                <TextField
                                    label="City"
                                    value={city}
                                    onChange={(event) => {
                                        setCity(
                                            event.target.value,
                                        );
                                        setLatitude("");
                                        setLongitude("");
                                        setLocationVerified(
                                            false,
                                        );
                                        setLocationSearchError(
                                            null,
                                        );
                                    }}
                                    required
                                    fullWidth
                                />
                            </Stack>

                            <Box>
                                <Button
                                    type="button"
                                    variant="outlined"
                                    onClick={
                                        handleFindLocation
                                    }
                                    disabled={
                                        isFindingLocation ||
                                        !country.trim() ||
                                        !city.trim()
                                    }
                                >
                                    {isFindingLocation
                                        ? "Finding location..."
                                        : "Find on map"}
                                </Button>
                            </Box>

                            {locationSearchError && (
                                <Alert severity="warning">
                                    {
                                        locationSearchError
                                    }
                                </Alert>
                            )}

                            {locationVerified && (
                                <Alert severity="success">
                                    Location verified.
                                </Alert>
                            )}

                            <Box>
                                <Typography
                                    sx={{
                                        mb: 1,
                                        fontWeight: 600,
                                    }}
                                >
                                    Select location on map
                                </Typography>

                                <Typography
                                    color="text.secondary"
                                    sx={{
                                        mb: 2,
                                    }}
                                >
                                    Click on the map to set
                                    the exact company
                                    coordinates.
                                </Typography>

                                <CompanyLocationPicker
                                    latitude={
                                        latitude === ""
                                            ? null
                                            : Number(
                                                latitude,
                                            )
                                    }
                                    longitude={
                                        longitude === ""
                                            ? null
                                            : Number(
                                                longitude,
                                            )
                                    }
                                    onLocationChange={(
                                        selectedLatitude,
                                        selectedLongitude,
                                    ) => {
                                        setLatitude(
                                            selectedLatitude.toFixed(
                                                6,
                                            ),
                                        );
                                        setLongitude(
                                            selectedLongitude.toFixed(
                                                6,
                                            ),
                                        );
                                    }}
                                />
                            </Box>
                        </Stack>
                    </Box>

                    <Box>
                        <Typography
                            variant="h6"
                            sx={{
                                mb: 2,
                                fontWeight: 700,
                            }}
                        >
                            Additional information
                        </Typography>

                        <Stack spacing={3}>
                            <TextField
                                label="Established at"
                                type="date"
                                value={establishedAt}
                                onChange={(event) =>
                                    setEstablishedAt(
                                        event.target.value,
                                    )
                                }
                                slotProps={{
                                    inputLabel: {
                                        shrink: true,
                                    },
                                    htmlInput: {
                                        max: new Date()
                                            .toISOString()
                                            .split("T")[0],
                                    },
                                }}
                                fullWidth
                            />

                            <TextField
                                label="Capabilities"
                                value={capabilities}
                                onChange={(event) =>
                                    setCapabilities(
                                        event.target.value,
                                    )
                                }
                                multiline
                                minRows={3}
                                fullWidth
                            />
                        </Stack>
                    </Box>

                    <Stack
                        direction="row"
                        spacing={2}
                        sx={{
                            justifyContent:
                                "flex-end",
                        }}
                    >
                        <Button
                            type="button"
                            variant="outlined"
                            disabled={
                                createCompanyMutation.isPending ||
                                isFindingLocation
                            }
                            onClick={() =>
                                navigate("/companies")
                            }
                        >
                            Cancel
                        </Button>

                        <Button
                            type="submit"
                            variant="contained"
                            disabled={
                                createCompanyMutation.isPending ||
                                isFindingLocation ||
                                industryId === null
                            }
                        >
                            {createCompanyMutation.isPending ||
                            isFindingLocation ? (
                                <CircularProgress
                                    size={22}
                                    color="inherit"
                                />
                            ) : (
                                "Create company"
                            )}
                        </Button>
                    </Stack>
                </Stack>
            </Box>
        </Paper>
    );
}
