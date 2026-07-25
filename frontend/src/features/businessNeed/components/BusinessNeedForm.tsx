import {
    Alert,
    Box,
    Button,
    Checkbox,
    Divider,
    FormControl,
    FormControlLabel,
    FormHelperText,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Paper,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import type { SelectChangeEvent } from "@mui/material";
import { useState } from "react";
import type { SpecializationResponse } from "../../specialization/model/SpecializationResponse";
import { useCreateBusinessNeed } from "../hooks/useCreateBusinessNeed";
import {
    cooperationTypeLabels,
    cooperationTypes,
    type CooperationType,
} from "../model/CooperationType";
import type { CreateBusinessNeedRequest } from "../model/CreateBusinessNeedRequest";
import {
    currencyCodes,
    type CurrencyCode,
} from "../model/CurrencyCode";

interface BusinessNeedFormProps {
    companyId: number;
    companyName: string;
    specializations: SpecializationResponse[];
    onSuccess?: () => void;
}

interface FormErrors {
    title?: string;
    cooperationType?: string;
    requiredSpecializationIds?: string;
    budget?: string;
    requiredPeriod?: string;
    maxDistanceKm?: string;
    minPartnerExperienceYears?: string;
    maxPartners?: string;
}

function parseOptionalNonNegativeInteger(
    value: string,
): number | null {
    if (value.trim() === "") {
        return null;
    }

    const parsedValue = Number(value);

    if (
        !Number.isInteger(parsedValue)
        || parsedValue < 0
    ) {
        return null;
    }

    return parsedValue;
}

export function BusinessNeedForm({
                                     companyId,
                                     companyName,
                                     specializations,
                                     onSuccess,
                                 }: BusinessNeedFormProps) {
    const createBusinessNeedMutation =
        useCreateBusinessNeed();

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    const [cooperationType, setCooperationType] =
        useState<CooperationType | "">("");

    const [
        requiredSpecializationIds,
        setRequiredSpecializationIds,
    ] = useState<number[]>([]);

    const [budgetEnabled, setBudgetEnabled] =
        useState(false);

    const [budgetMin, setBudgetMin] = useState("");
    const [budgetMax, setBudgetMax] = useState("");

    const [currency, setCurrency] =
        useState<CurrencyCode>("PLN");

    const [requiredPeriodEnabled, setRequiredPeriodEnabled] =
        useState(false);

    const [requiredPeriodFrom, setRequiredPeriodFrom] =
        useState("");

    const [requiredPeriodUntil, setRequiredPeriodUntil] =
        useState("");

    const [maxDistanceKm, setMaxDistanceKm] =
        useState("");

    const [
        minPartnerExperienceYears,
        setMinPartnerExperienceYears,
    ] = useState("");

    const [maxPartners, setMaxPartners] = useState("1");

    const [errors, setErrors] =
        useState<FormErrors>({});

    function handleSpecializationsChange(
        event: SelectChangeEvent<number[]>,
    ) {
        const value = event.target.value;

        setRequiredSpecializationIds(
            typeof value === "string"
                ? value
                    .split(",")
                    .map(Number)
                : value,
        );
    }

    function validateForm(): boolean {
        const validationErrors: FormErrors = {};

        if (title.trim() === "") {
            validationErrors.title =
                "Title is required.";
        } else if (title.trim().length > 150) {
            validationErrors.title =
                "Title cannot exceed 150 characters.";
        }

        if (cooperationType === "") {
            validationErrors.cooperationType =
                "Cooperation type is required.";
        }

        if (requiredSpecializationIds.length === 0) {
            validationErrors.requiredSpecializationIds =
                "Select at least one specialization.";
        }

        if (budgetEnabled) {
            const min = Number(budgetMin);
            const max = Number(budgetMax);

            if (
                budgetMin.trim() === ""
                || budgetMax.trim() === ""
                || !Number.isFinite(min)
                || !Number.isFinite(max)
                || min < 0
                || max < 0
            ) {
                validationErrors.budget =
                    "Provide valid non-negative minimum and maximum budget values.";
            } else if (min > max) {
                validationErrors.budget =
                    "Minimum budget cannot be greater than maximum budget.";
            }
        }

        if (requiredPeriodEnabled) {
            if (
                requiredPeriodFrom === ""
                || requiredPeriodUntil === ""
            ) {
                validationErrors.requiredPeriod =
                    "Provide both the start and end date.";
            } else if (
                requiredPeriodFrom > requiredPeriodUntil
            ) {
                validationErrors.requiredPeriod =
                    "Start date cannot be later than end date.";
            }
        }

        if (maxDistanceKm.trim() !== "") {
            const parsedMaxDistance =
                parseOptionalNonNegativeInteger(
                    maxDistanceKm,
                );

            if (parsedMaxDistance === null) {
                validationErrors.maxDistanceKm =
                    "Maximum distance must be a non-negative integer.";
            }
        }

        if (minPartnerExperienceYears.trim() !== "") {
            const parsedExperience =
                parseOptionalNonNegativeInteger(
                    minPartnerExperienceYears,
                );

            if (parsedExperience === null) {
                validationErrors.minPartnerExperienceYears =
                    "Minimum experience must be a non-negative integer.";
            }
        }

        const parsedMaxPartners = Number(maxPartners);

        if (
            !Number.isInteger(parsedMaxPartners)
            || parsedMaxPartners <= 0
        ) {
            validationErrors.maxPartners =
                "Maximum number of partners must be a positive integer.";
        }

        setErrors(validationErrors);

        return Object.keys(validationErrors).length === 0;
    }

    function handleSubmit(
        event: React.FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        if (cooperationType === "") {
            return;
        }

        const request: CreateBusinessNeedRequest = {
            title: title.trim(),
            description:
                description.trim() === ""
                    ? null
                    : description.trim(),
            cooperationType,
            requiredSpecializationIds,
            budget: budgetEnabled
                ? {
                    min: Number(budgetMin),
                    max: Number(budgetMax),
                    currency,
                }
                : null,
            requiredPeriod: requiredPeriodEnabled
                ? {
                    from: requiredPeriodFrom,
                    until: requiredPeriodUntil,
                }
                : null,
            maxDistanceKm:
                parseOptionalNonNegativeInteger(
                    maxDistanceKm,
                ),
            minPartnerExperienceYears:
                parseOptionalNonNegativeInteger(
                    minPartnerExperienceYears,
                ),
            maxPartners: Number(maxPartners),
        };

        createBusinessNeedMutation.mutate(
            {
                companyId,
                request,
            },
            {
                onSuccess,
            },
        );
    }

    return (
        <Paper
            component="form"
            onSubmit={handleSubmit}
            sx={{
                p: 4,
                maxWidth: 900,
                mx: "auto",
            }}
        >
            <Stack spacing={3}>
                <Box>
                    <Typography
                        component="h1"
                        variant="h4"
                    >
                        Create business need
                    </Typography>

                    <Typography
                        variant="body1"
                        color="text.secondary"
                        sx={{ mt: 1 }}
                    >
                        The business need will be created
                        for {companyName}.
                    </Typography>
                </Box>

                {createBusinessNeedMutation.isError && (
                    <Alert severity="error">
                        Failed to create the business need.
                        Please verify the entered data and try
                        again.
                    </Alert>
                )}

                <Divider />

                <Typography variant="h6">
                    Basic information
                </Typography>

                <TextField
                    label="Title"
                    value={title}
                    onChange={(event) =>
                        setTitle(event.target.value)
                    }
                    required
                    fullWidth
                    error={Boolean(errors.title)}
                    helperText={
                        errors.title
                        ?? `${title.length}/150`
                    }
                    slotProps={{
                        htmlInput: {
                            maxLength: 150,
                        },
                    }}
                />

                <TextField
                    label="Description"
                    value={description}
                    onChange={(event) =>
                        setDescription(event.target.value)
                    }
                    fullWidth
                    multiline
                    minRows={4}
                    helperText={`${description.length}/2000`}
                    slotProps={{
                        htmlInput: {
                            maxLength: 2000,
                        },
                    }}
                />

                <FormControl
                    fullWidth
                    required
                    error={Boolean(
                        errors.cooperationType,
                    )}
                >
                    <InputLabel id="cooperation-type-label">
                        Cooperation type
                    </InputLabel>

                    <Select
                        labelId="cooperation-type-label"
                        value={cooperationType}
                        label="Cooperation type"
                        onChange={(event) =>
                            setCooperationType(
                                event.target
                                    .value as CooperationType,
                            )
                        }
                    >
                        {cooperationTypes.map((type) => (
                            <MenuItem
                                key={type}
                                value={type}
                            >
                                {
                                    cooperationTypeLabels[
                                        type
                                        ]
                                }
                            </MenuItem>
                        ))}
                    </Select>

                    <FormHelperText>
                        {errors.cooperationType}
                    </FormHelperText>
                </FormControl>

                <FormControl
                    fullWidth
                    required
                    error={Boolean(
                        errors.requiredSpecializationIds,
                    )}
                >
                    <InputLabel id="required-specializations-label">
                        Required specializations
                    </InputLabel>

                    <Select
                        labelId="required-specializations-label"
                        multiple
                        value={requiredSpecializationIds}
                        onChange={
                            handleSpecializationsChange
                        }
                        input={
                            <OutlinedInput label="Required specializations" />
                        }
                        renderValue={(selectedIds) =>
                            selectedIds
                                .map((specializationId) =>
                                    specializations.find(
                                        (specialization) =>
                                            specialization.id
                                            === specializationId,
                                    ),
                                )
                                .filter(
                                    (
                                        specialization,
                                    ): specialization is SpecializationResponse =>
                                        specialization
                                        !== undefined,
                                )
                                .map(
                                    (specialization) =>
                                        specialization.name,
                                )
                                .join(", ")
                        }
                    >
                        {specializations.map(
                            (specialization) => (
                                <MenuItem
                                    key={specialization.id}
                                    value={
                                        specialization.id
                                    }
                                >
                                    <Checkbox
                                        checked={requiredSpecializationIds.includes(
                                            specialization.id,
                                        )}
                                    />

                                    {specialization.name}
                                </MenuItem>
                            ),
                        )}
                    </Select>

                    <FormHelperText>
                        {
                            errors.requiredSpecializationIds
                        }
                    </FormHelperText>
                </FormControl>

                <Divider />

                <Box>
                    <Typography variant="h6">
                        Budget
                    </Typography>

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={budgetEnabled}
                                onChange={(event) =>
                                    setBudgetEnabled(
                                        event.target.checked,
                                    )
                                }
                            />
                        }
                        label="Specify budget range"
                    />
                </Box>

                {budgetEnabled && (
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                    >
                        <TextField
                            label="Minimum budget"
                            type="number"
                            value={budgetMin}
                            onChange={(event) =>
                                setBudgetMin(
                                    event.target.value,
                                )
                            }
                            fullWidth
                            required
                            error={Boolean(errors.budget)}
                            slotProps={{
                                htmlInput: {
                                    min: 0,
                                    step: "0.01",
                                },
                            }}
                        />

                        <TextField
                            label="Maximum budget"
                            type="number"
                            value={budgetMax}
                            onChange={(event) =>
                                setBudgetMax(
                                    event.target.value,
                                )
                            }
                            fullWidth
                            required
                            error={Boolean(errors.budget)}
                            helperText={errors.budget}
                            slotProps={{
                                htmlInput: {
                                    min: 0,
                                    step: "0.01",
                                },
                            }}
                        />

                        <FormControl
                            sx={{
                                minWidth: {
                                    xs: "100%",
                                    sm: 140,
                                },
                            }}
                        >
                            <InputLabel id="currency-label">
                                Currency
                            </InputLabel>

                            <Select
                                labelId="currency-label"
                                value={currency}
                                label="Currency"
                                onChange={(event) =>
                                    setCurrency(
                                        event.target
                                            .value as CurrencyCode,
                                    )
                                }
                            >
                                {currencyCodes.map(
                                    (currencyCode) => (
                                        <MenuItem
                                            key={
                                                currencyCode
                                            }
                                            value={
                                                currencyCode
                                            }
                                        >
                                            {currencyCode}
                                        </MenuItem>
                                    ),
                                )}
                            </Select>
                        </FormControl>
                    </Stack>
                )}

                <Divider />

                <Box>
                    <Typography variant="h6">
                        Required period
                    </Typography>

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={
                                    requiredPeriodEnabled
                                }
                                onChange={(event) =>
                                    setRequiredPeriodEnabled(
                                        event.target.checked,
                                    )
                                }
                            />
                        }
                        label="Specify required cooperation period"
                    />
                </Box>

                {requiredPeriodEnabled && (
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                    >
                        <TextField
                            label="From"
                            type="date"
                            value={requiredPeriodFrom}
                            onChange={(event) =>
                                setRequiredPeriodFrom(
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            error={Boolean(
                                errors.requiredPeriod,
                            )}
                            slotProps={{
                                inputLabel: {
                                    shrink: true,
                                },
                            }}
                        />

                        <TextField
                            label="Until"
                            type="date"
                            value={
                                requiredPeriodUntil
                            }
                            onChange={(event) =>
                                setRequiredPeriodUntil(
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            error={Boolean(
                                errors.requiredPeriod,
                            )}
                            helperText={
                                errors.requiredPeriod
                            }
                            slotProps={{
                                inputLabel: {
                                    shrink: true,
                                },
                            }}
                        />
                    </Stack>
                )}

                <Divider />

                <Typography variant="h6">
                    Partner requirements
                </Typography>

                <Stack
                    direction={{
                        xs: "column",
                        sm: "row",
                    }}
                    spacing={2}
                >
                    <TextField
                        label="Maximum distance"
                        type="number"
                        value={maxDistanceKm}
                        onChange={(event) =>
                            setMaxDistanceKm(
                                event.target.value,
                            )
                        }
                        fullWidth
                        error={Boolean(
                            errors.maxDistanceKm,
                        )}
                        helperText={
                            errors.maxDistanceKm
                            ?? "Optional, in kilometres"
                        }
                        slotProps={{
                            htmlInput: {
                                min: 0,
                                step: 1,
                            },
                        }}
                    />

                    <TextField
                        label="Minimum partner experience"
                        type="number"
                        value={
                            minPartnerExperienceYears
                        }
                        onChange={(event) =>
                            setMinPartnerExperienceYears(
                                event.target.value,
                            )
                        }
                        fullWidth
                        error={Boolean(
                            errors.minPartnerExperienceYears,
                        )}
                        helperText={
                            errors.minPartnerExperienceYears
                            ?? "Optional, in years"
                        }
                        slotProps={{
                            htmlInput: {
                                min: 0,
                                step: 1,
                            },
                        }}
                    />

                    <TextField
                        label="Maximum partners"
                        type="number"
                        value={maxPartners}
                        onChange={(event) =>
                            setMaxPartners(
                                event.target.value,
                            )
                        }
                        required
                        fullWidth
                        error={Boolean(
                            errors.maxPartners,
                        )}
                        helperText={errors.maxPartners}
                        slotProps={{
                            htmlInput: {
                                min: 1,
                                step: 1,
                            },
                        }}
                    />
                </Stack>

                <Box
                    sx={{
                        display: "flex",
                        justifyContent: "flex-end",
                    }}
                >
                    <Button
                        type="submit"
                        variant="contained"
                        size="large"
                        disabled={
                            createBusinessNeedMutation.isPending
                            || specializations.length === 0
                        }
                    >
                        {createBusinessNeedMutation.isPending
                            ? "Creating..."
                            : "Create business need"}
                    </Button>
                </Box>
            </Stack>
        </Paper>
    );
}
