import {
    Alert,
    Box,
    Button,
    Checkbox,
    Chip,
    Divider,
    FormControl,
    FormControlLabel,
    FormHelperText,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import { useMemo, useState } from "react";
import type { SpecializationGroupResponse } from "../../specialization/model/SpecializationGroupResponse";
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
    companySpecializations: SpecializationResponse[];
    specializationGroups: SpecializationGroupResponse[];
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
                                     companySpecializations,
                                     specializationGroups,
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

    const [
        requiredPeriodEnabled,
        setRequiredPeriodEnabled,
    ] = useState(false);

    const [requiredPeriodFrom, setRequiredPeriodFrom] =
        useState("");

    const [
        requiredPeriodUntil,
        setRequiredPeriodUntil,
    ] = useState("");

    const [maxDistanceKm, setMaxDistanceKm] =
        useState("");

    const [
        minPartnerExperienceYears,
        setMinPartnerExperienceYears,
    ] = useState("");

    const [maxPartners, setMaxPartners] = useState("1");

    const [errors, setErrors] =
        useState<FormErrors>({});

    const companySpecializationIds = useMemo(
        () =>
            new Set(
                companySpecializations.map(
                    (specialization) =>
                        specialization.id,
                ),
            ),
        [companySpecializations],
    );

    const availableSpecializationsCount = useMemo(
        () =>
            specializationGroups.reduce(
                (count, group) =>
                    count
                    + group.specializations.length,
                0,
            ),
        [specializationGroups],
    );

    function handleSpecializationChange(
        specializationId: number,
    ) {
        setRequiredSpecializationIds(
            (currentIds) => {
                if (
                    currentIds.includes(
                        specializationId,
                    )
                ) {
                    return currentIds.filter(
                        (currentId) =>
                            currentId
                            !== specializationId,
                    );
                }

                return [
                    ...currentIds,
                    specializationId,
                ];
            },
        );

        setErrors((currentErrors) => ({
            ...currentErrors,
            requiredSpecializationIds: undefined,
        }));
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
                requiredPeriodFrom
                > requiredPeriodUntil
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

        if (
            minPartnerExperienceYears.trim() !== ""
        ) {
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
                p: {
                    xs: 2,
                    sm: 4,
                },
                maxWidth: 1000,
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
                    onChange={(event) => {
                        setTitle(event.target.value);

                        setErrors((currentErrors) => ({
                            ...currentErrors,
                            title: undefined,
                        }));
                    }}
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
                        onChange={(event) => {
                            setCooperationType(
                                event.target
                                    .value as CooperationType,
                            );

                            setErrors(
                                (currentErrors) => ({
                                    ...currentErrors,
                                    cooperationType:
                                    undefined,
                                }),
                            );
                        }}
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

                <Divider />

                <Box>
                    <Typography variant="h6">
                        Required specializations
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{ mt: 0.5 }}
                    >
                        Select the skills and services
                        required from a potential business
                        partner.
                    </Typography>
                </Box>

                {companySpecializations.length > 0 && (
                    <Paper
                        variant="outlined"
                        sx={{
                            p: 2.5,
                            backgroundColor:
                                "action.hover",
                        }}
                    >
                        <Typography
                            variant="subtitle1"
                            sx={{
                                fontWeight: 700,
                            }}
                        >
                            Company specializations
                        </Typography>

                        <Typography
                            variant="body2"
                            color="text.secondary"
                            sx={{ mt: 0.5, mb: 2 }}
                        >
                            Quick access to specializations
                            associated with your company.
                        </Typography>

                        <Box
                            sx={{
                                display: "grid",
                                gridTemplateColumns: {
                                    xs: "1fr",
                                    sm: "repeat(2, minmax(0, 1fr))",
                                },
                                columnGap: 2,
                                rowGap: 0.5,
                            }}
                        >
                            {companySpecializations.map(
                                (specialization) => (
                                    <FormControlLabel
                                        key={
                                            specialization.id
                                        }
                                        control={
                                            <Checkbox
                                                checked={requiredSpecializationIds.includes(
                                                    specialization.id,
                                                )}
                                                onChange={() =>
                                                    handleSpecializationChange(
                                                        specialization.id,
                                                    )
                                                }
                                            />
                                        }
                                        label={
                                            specialization.name
                                        }
                                    />
                                ),
                            )}
                        </Box>
                    </Paper>
                )}

                <Box>
                    <Typography
                        variant="subtitle1"
                        sx={{
                            mb: 2,
                            fontWeight: 700,
                        }}
                    >
                        All specializations
                    </Typography>

                    {specializationGroups.length === 0 ? (
                        <Alert severity="warning">
                            No active specializations are
                            currently available.
                        </Alert>
                    ) : (
                        <Stack spacing={2}>
                            {specializationGroups.map(
                                (group) => (
                                    <Paper
                                        key={
                                            group.industryId
                                        }
                                        variant="outlined"
                                        sx={{ p: 2.5 }}
                                    >
                                        <Typography
                                            variant="subtitle1"
                                            sx={{
                                                mb: 1.5,
                                                fontWeight: 700,
                                            }}
                                        >
                                            {
                                                group.industryName
                                            }
                                        </Typography>

                                        <Box
                                            sx={{
                                                display:
                                                    "grid",
                                                gridTemplateColumns:
                                                    {
                                                        xs: "1fr",
                                                        md: "repeat(2, minmax(0, 1fr))",
                                                    },
                                                columnGap: 3,
                                                rowGap: 0.5,
                                            }}
                                        >
                                            {group.specializations.map(
                                                (
                                                    specialization,
                                                ) => {
                                                    const isCompanySpecialization =
                                                        companySpecializationIds.has(
                                                            specialization.id,
                                                        );

                                                    return (
                                                        <Box
                                                            key={
                                                                specialization.id
                                                            }
                                                            sx={{
                                                                display:
                                                                    "flex",
                                                                alignItems:
                                                                    "center",
                                                                justifyContent:
                                                                    "space-between",
                                                                gap: 1,
                                                                minWidth: 0,
                                                            }}
                                                        >
                                                            <FormControlLabel
                                                                sx={{
                                                                    flex: 1,
                                                                    minWidth: 0,
                                                                    mr: 0,
                                                                }}
                                                                control={
                                                                    <Checkbox
                                                                        checked={requiredSpecializationIds.includes(
                                                                            specialization.id,
                                                                        )}
                                                                        onChange={() =>
                                                                            handleSpecializationChange(
                                                                                specialization.id,
                                                                            )
                                                                        }
                                                                    />
                                                                }
                                                                label={
                                                                    specialization.name
                                                                }
                                                            />

                                                            {isCompanySpecialization && (
                                                                <Chip
                                                                    label="Your company"
                                                                    size="small"
                                                                    color="primary"
                                                                    variant="outlined"
                                                                    sx={{
                                                                        flexShrink: 0,
                                                                    }}
                                                                />
                                                            )}
                                                        </Box>
                                                    );
                                                },
                                            )}
                                        </Box>
                                    </Paper>
                                ),
                            )}
                        </Stack>
                    )}

                    {errors.requiredSpecializationIds && (
                        <FormHelperText
                            error
                            sx={{ mt: 1 }}
                        >
                            {
                                errors.requiredSpecializationIds
                            }
                        </FormHelperText>
                    )}
                </Box>

                <Divider />

                <Box>
                    <Typography variant="h6">
                        Budget
                    </Typography>

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={budgetEnabled}
                                onChange={(event) => {
                                    setBudgetEnabled(
                                        event.target.checked,
                                    );

                                    setErrors(
                                        (currentErrors) => ({
                                            ...currentErrors,
                                            budget: undefined,
                                        }),
                                    );
                                }}
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
                                onChange={(event) => {
                                    setRequiredPeriodEnabled(
                                        event.target.checked,
                                    );

                                    setErrors(
                                        (currentErrors) => ({
                                            ...currentErrors,
                                            requiredPeriod:
                                            undefined,
                                        }),
                                    );
                                }}
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
                            || availableSpecializationsCount
                            === 0
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
