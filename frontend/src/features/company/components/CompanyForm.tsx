import { useState } from "react";
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Paper,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import { useIndustries } from "../../industry/hooks/useIndustries";
import { useSpecializationsByIndustry } from "../../specialization/hooks/useSpecializationsByIndustry";
import { useCreateCompany } from "../hooks/useCreateCompany";
import type { CreateCompanyRequest } from "../model/CreateCompanyRequest";
import { CompanyLocationPicker } from "./CompanyLocationPicker";
import { findLocation } from "../api/geocodingApi";

export function CompanyForm() {
    const navigate = useNavigate();

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [industryId, setIndustryId] = useState<number | null>(null);
    const [specializationIds, setSpecializationIds] = useState<number[]>([]);
    const [country, setCountry] = useState("");
    const [city, setCity] = useState("");
    const [latitude, setLatitude] = useState("");
    const [longitude, setLongitude] = useState("");
    const [establishedAt, setEstablishedAt] = useState("");
    const [capabilities, setCapabilities] = useState("");
    const [isFindingLocation, setIsFindingLocation] = useState(false);
    const [locationSearchError, setLocationSearchError] =
        useState<string | null>(null);
    const [locationVerified, setLocationVerified] =
        useState(false);

    const {
        data: industries,
        isLoading: industriesLoading,
        isError: industriesError,
    } = useIndustries();

    const {
        data: specializations,
        isLoading: specializationsLoading,
        isError: specializationsError,
    } = useSpecializationsByIndustry(industryId);

    const createCompanyMutation = useCreateCompany();

    function handleIndustryChange(selectedIndustryId: number) {
        setIndustryId(selectedIndustryId);
        setSpecializationIds([]);
    }

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (industryId === null) {
            return;
        }

        if (!locationVerified) {
            setLocationSearchError(
                "Verify the location before creating the company.",
            );
            return;
        }

        const request: CreateCompanyRequest = {
            name: name.trim(),
            description: description.trim() || null,
            industryId,
            specializationIds,
            country: country.trim(),
            city: city.trim(),
            latitude: Number(latitude),
            longitude: Number(longitude),
            establishedAt: establishedAt || null,
            capabilities: capabilities.trim() || null,
        };

        createCompanyMutation.mutate(request, {
            onSuccess: () => {
                navigate("/companies");
            },
        });
    }

    async function handleFindLocation() {
        if (!country.trim() || !city.trim()) {
            setLocationSearchError(
                "Enter both country and city.",
            );
            return;
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
                    "Location could not be found.",
                );
                return;
            }

            setCountry(result.country);
            setCity(result.city);
            setLatitude(result.latitude.toFixed(6));
            setLongitude(result.longitude.toFixed(6));
            setLocationVerified(true);
        } catch {
            setLocationSearchError(
                "Failed to search for the location.",
            );
        } finally {
            setIsFindingLocation(false);
        }
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
                                    setName(event.target.value)
                                }
                                required
                                fullWidth
                            />

                            <TextField
                                label="Description"
                                value={description}
                                onChange={(event) =>
                                    setDescription(event.target.value)
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
                                    disabled={industriesLoading}
                                    onChange={(event) =>
                                        handleIndustryChange(
                                            Number(event.target.value),
                                        )
                                    }
                                >
                                    {industries?.map((industry) => (
                                        <MenuItem
                                            key={industry.id}
                                            value={industry.id}
                                        >
                                            {industry.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>

                            <FormControl
                                required
                                fullWidth
                                disabled={
                                    industryId === null ||
                                    specializationsLoading
                                }
                            >
                                <InputLabel id="specializations-label">
                                    Specializations
                                </InputLabel>

                                <Select
                                    labelId="specializations-label"
                                    multiple
                                    value={specializationIds}
                                    input={
                                        <OutlinedInput label="Specializations" />
                                    }
                                    onChange={(event) => {
                                        const value = event.target.value;

                                        setSpecializationIds(
                                            typeof value === "string"
                                                ? value
                                                    .split(",")
                                                    .map(Number)
                                                : value,
                                        );
                                    }}
                                    renderValue={(selectedIds) =>
                                        specializations
                                            ?.filter((specialization) =>
                                                selectedIds.includes(
                                                    specialization.id,
                                                ),
                                            )
                                            .map(
                                                (specialization) =>
                                                    specialization.name,
                                            )
                                            .join(", ") ?? ""
                                    }
                                >
                                    {specializations?.map((specialization) => (
                                        <MenuItem
                                            key={specialization.id}
                                            value={specialization.id}
                                        >
                                            {specialization.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>

                            {specializationsError && (
                                <Alert severity="error">
                                    Failed to load specializations.
                                </Alert>
                            )}
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
                                    label="Country"
                                    value={country}
                                    onChange={(event) => {
                                        setCountry(event.target.value);
                                        setLatitude("");
                                        setLongitude("");
                                        setLocationVerified(false);
                                        setLocationSearchError(null);
                                    }}
                                    required
                                    fullWidth
                                />

                                <TextField
                                    label="City"
                                    value={city}
                                    onChange={(event) => {
                                        setCity(event.target.value);
                                        setLatitude("");
                                        setLongitude("");
                                        setLocationVerified(false);
                                        setLocationSearchError(null);
                                    }}
                                    required
                                    fullWidth
                                />

                            </Stack>


                            <Box>
                                <Button
                                    type="button"
                                    variant="outlined"
                                    onClick={handleFindLocation}
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
                                    {locationSearchError}
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
                                    Click on the map to set the company coordinates.
                                </Typography>

                                <CompanyLocationPicker
                                    latitude={
                                        latitude === ""
                                            ? null
                                            : Number(latitude)
                                    }
                                    longitude={
                                        longitude === ""
                                            ? null
                                            : Number(longitude)
                                    }
                                    onLocationChange={(
                                        selectedLatitude,
                                        selectedLongitude,
                                    ) => {
                                        setLatitude(
                                            selectedLatitude.toFixed(6),
                                        );
                                        setLongitude(
                                            selectedLongitude.toFixed(6),
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
                                    setEstablishedAt(event.target.value)
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
                                    setCapabilities(event.target.value)
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
                            justifyContent: "flex-end",
                        }}
                    >
                        <Button
                            type="button"
                            variant="outlined"
                            disabled={createCompanyMutation.isPending}
                            onClick={() => navigate("/companies")}
                        >
                            Cancel
                        </Button>

                        <Button
                            type="submit"
                            variant="contained"
                            disabled={
                                createCompanyMutation.isPending ||
                                industryId === null ||
                                specializationIds.length === 0 ||
                                !locationVerified
                            }
                        >
                            {createCompanyMutation.isPending ? (
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
