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
    List,
    ListItemButton,
    ListItemText,
    MenuItem,
    Paper,
    Popover,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import {
    useMemo,
    useState,
} from "react";
import {
    cooperationTypeLabels,
    cooperationTypes,
    type CooperationType,
} from "../../../common/model/CooperationType";
import {
    currencyCodes,
    type CurrencyCode,
} from "../../../common/model/CurrencyCode";
import type { SpecializationGroupResponse } from "../../specialization/model/SpecializationGroupResponse";
import type { SpecializationResponse } from "../../specialization/model/SpecializationResponse";
import { useCreateBusinessOffer } from "../hooks/useCreateBusinessOffer";
import type { CreateBusinessOfferRequest } from "../model/CreateBusinessOfferRequest";

interface BusinessOfferFormProps {
    companyId: number;
    companyName: string;
    companySpecializations: SpecializationResponse[];
    specializationGroups: SpecializationGroupResponse[];
    onSuccess?: () => void;
}

interface FormErrors {
    title?: string;
    cooperationType?: string;
    offeredSpecializationIds?: string;
    priceRange?: string;
    availabilityPeriod?: string;
    serviceRadiusKm?: string;
    experienceYears?: string;
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

export function BusinessOfferForm({
                                      companyId,
                                      companyName,
                                      companySpecializations,
                                      specializationGroups,
                                      onSuccess,
                                  }: BusinessOfferFormProps) {
    const createBusinessOfferMutation =
        useCreateBusinessOffer();

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    const [cooperationType, setCooperationType] =
        useState<CooperationType | "">("");

    const [
        offeredSpecializationIds,
        setOfferedSpecializationIds,
    ] = useState<number[]>([]);

    const [
        specializationPickerAnchor,
        setSpecializationPickerAnchor,
    ] = useState<HTMLElement | null>(null);

    const [
        selectedIndustryId,
        setSelectedIndustryId,
    ] = useState<number | null>(
        specializationGroups[0]?.industryId ?? null,
    );

    const [priceRangeEnabled, setPriceRangeEnabled] =
        useState(false);

    const [priceRangeMin, setPriceRangeMin] =
        useState("");

    const [priceRangeMax, setPriceRangeMax] =
        useState("");

    const [currency, setCurrency] =
        useState<CurrencyCode>("PLN");

    const [
        availabilityPeriodEnabled,
        setAvailabilityPeriodEnabled,
    ] = useState(false);

    const [
        availabilityPeriodFrom,
        setAvailabilityPeriodFrom,
    ] = useState("");

    const [
        availabilityPeriodUntil,
        setAvailabilityPeriodUntil,
    ] = useState("");

    const [serviceRadiusKm, setServiceRadiusKm] =
        useState("");

    const [experienceYears, setExperienceYears] =
        useState("");

    const [maxPartners, setMaxPartners] =
        useState("1");

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

    const allSpecializations = useMemo(
        () =>
            specializationGroups.flatMap(
                (group) => group.specializations,
            ),
        [specializationGroups],
    );

    const specializationsById = useMemo(() => {
        const result = new Map<
            number,
            SpecializationResponse
        >();

        for (const specialization of allSpecializations) {
            result.set(
                specialization.id,
                specialization,
            );
        }

        for (
            const specialization
            of companySpecializations
            ) {
            result.set(
                specialization.id,
                specialization,
            );
        }

        return result;
    }, [
        allSpecializations,
        companySpecializations,
    ]);

    const selectedSpecializations = useMemo(
        () =>
            offeredSpecializationIds
                .map((specializationId) =>
                    specializationsById.get(
                        specializationId,
                    ),
                )
                .filter(
                    (
                        specialization,
                    ): specialization is SpecializationResponse =>
                        specialization !== undefined,
                ),
        [
            offeredSpecializationIds,
            specializationsById,
        ],
    );

    const selectedIndustryGroup = useMemo(
        () =>
            specializationGroups.find(
                (group) =>
                    group.industryId
                    === selectedIndustryId,
            ) ?? null,
        [
            selectedIndustryId,
            specializationGroups,
        ],
    );

    const specializationPickerOpen =
        specializationPickerAnchor !== null;

    function openSpecializationPicker(
        event: React.MouseEvent<HTMLElement>,
    ) {
        setSpecializationPickerAnchor(
            event.currentTarget,
        );
    }

    function closeSpecializationPicker() {
        setSpecializationPickerAnchor(null);
    }

    function handleSpecializationChange(
        specializationId: number,
    ) {
        setOfferedSpecializationIds(
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
            offeredSpecializationIds: undefined,
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

        if (offeredSpecializationIds.length === 0) {
            validationErrors.offeredSpecializationIds =
                "Select at least one specialization.";
        }

        if (priceRangeEnabled) {
            const min = Number(priceRangeMin);
            const max = Number(priceRangeMax);

            if (
                priceRangeMin.trim() === ""
                || priceRangeMax.trim() === ""
                || !Number.isFinite(min)
                || !Number.isFinite(max)
                || min < 0
                || max < 0
            ) {
                validationErrors.priceRange =
                    "Provide valid non-negative minimum and maximum price values.";
            } else if (min > max) {
                validationErrors.priceRange =
                    "Minimum price cannot be greater than maximum price.";
            }
        }

        if (availabilityPeriodEnabled) {
            if (
                availabilityPeriodFrom === ""
                || availabilityPeriodUntil === ""
            ) {
                validationErrors.availabilityPeriod =
                    "Provide both the start and end date.";
            } else if (
                availabilityPeriodFrom
                > availabilityPeriodUntil
            ) {
                validationErrors.availabilityPeriod =
                    "Start date cannot be later than end date.";
            }
        }

        if (serviceRadiusKm.trim() !== "") {
            const parsedServiceRadius =
                parseOptionalNonNegativeInteger(
                    serviceRadiusKm,
                );

            if (parsedServiceRadius === null) {
                validationErrors.serviceRadiusKm =
                    "Service radius must be a non-negative integer.";
            }
        }

        if (experienceYears.trim() !== "") {
            const parsedExperienceYears =
                parseOptionalNonNegativeInteger(
                    experienceYears,
                );

            if (parsedExperienceYears === null) {
                validationErrors.experienceYears =
                    "Experience must be a non-negative integer.";
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

        const request: CreateBusinessOfferRequest = {
            title: title.trim(),
            description:
                description.trim() === ""
                    ? null
                    : description.trim(),
            cooperationType,
            offeredSpecializationIds,
            priceRange: priceRangeEnabled
                ? {
                    min: Number(priceRangeMin),
                    max: Number(priceRangeMax),
                    currency,
                }
                : null,
            availabilityPeriod: availabilityPeriodEnabled
                ? {
                    from: availabilityPeriodFrom,
                    until: availabilityPeriodUntil,
                }
                : null,
            serviceRadiusKm:
                parseOptionalNonNegativeInteger(
                    serviceRadiusKm,
                ),
            experienceYears:
                parseOptionalNonNegativeInteger(
                    experienceYears,
                ),
            maxPartners: Number(maxPartners),
        };

        createBusinessOfferMutation.mutate(
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
                        Create business offer
                    </Typography>

                    <Typography
                        variant="body1"
                        color="text.secondary"
                        sx={{ mt: 1 }}
                    >
                        The business offer will be created
                        for {companyName}.
                    </Typography>
                </Box>

                {createBusinessOfferMutation.isError && (
                    <Alert severity="error">
                        Failed to create the business offer.
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
                                    cooperationType: undefined,
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
                        Offered specializations
                    </Typography>

                    <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{ mt: 0.5 }}
                    >
                        Select the skills and services offered
                        by your company.
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
                            sx={{ fontWeight: 700 }}
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
                                                checked={offeredSpecializationIds.includes(
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
                            mb: 1,
                            fontWeight: 700,
                        }}
                    >
                        All specializations
                    </Typography>

                    <Button
                        variant="outlined"
                        fullWidth
                        onClick={openSpecializationPicker}
                        disabled={
                            specializationGroups.length === 0
                        }
                        aria-haspopup="dialog"
                        aria-expanded={
                            specializationPickerOpen
                        }
                        sx={{
                            minHeight: 56,
                            justifyContent:
                                "space-between",
                            px: 2,
                            textTransform: "none",
                        }}
                    >
                        <Typography
                            component="span"
                            color={
                                offeredSpecializationIds.length
                                > 0
                                    ? "text.primary"
                                    : "text.secondary"
                            }
                        >
                            {offeredSpecializationIds.length
                            > 0
                                ? `${offeredSpecializationIds.length} specializations selected`
                                : "Select specializations"}
                        </Typography>

                        <Typography
                            component="span"
                            aria-hidden="true"
                        >
                            ▾
                        </Typography>
                    </Button>

                    <Popover
                        open={specializationPickerOpen}
                        anchorEl={
                            specializationPickerAnchor
                        }
                        onClose={
                            closeSpecializationPicker
                        }
                        anchorOrigin={{
                            vertical: "bottom",
                            horizontal: "left",
                        }}
                        transformOrigin={{
                            vertical: "top",
                            horizontal: "left",
                        }}
                        slotProps={{
                            paper: {
                                sx: {
                                    mt: 1,
                                    width: {
                                        xs: "calc(100vw - 32px)",
                                        sm: 760,
                                    },
                                    maxWidth:
                                        "calc(100vw - 32px)",
                                    maxHeight: 480,
                                    overflow: "hidden",
                                },
                            },
                        }}
                    >
                        <Box
                            sx={{
                                display: "grid",
                                gridTemplateColumns: {
                                    xs: "1fr",
                                    sm: "260px minmax(0, 1fr)",
                                },
                                minHeight: {
                                    xs: 300,
                                    sm: 380,
                                },
                            }}
                        >
                            <Box
                                sx={{
                                    borderRight: {
                                        xs: "none",
                                        sm: 1,
                                    },
                                    borderBottom: {
                                        xs: 1,
                                        sm: "none",
                                    },
                                    borderColor: "divider",
                                    overflowY: "auto",
                                    maxHeight: {
                                        xs: 160,
                                        sm: 480,
                                    },
                                }}
                            >
                                <Typography
                                    variant="overline"
                                    color="text.secondary"
                                    sx={{
                                        display: "block",
                                        px: 2,
                                        pt: 2,
                                        pb: 1,
                                    }}
                                >
                                    Industries
                                </Typography>

                                <List
                                    disablePadding
                                    sx={{ pb: 1 }}
                                >
                                    {specializationGroups.map(
                                        (group) => (
                                            <ListItemButton
                                                key={
                                                    group.industryId
                                                }
                                                selected={
                                                    group.industryId
                                                    === selectedIndustryId
                                                }
                                                onMouseEnter={() =>
                                                    setSelectedIndustryId(
                                                        group.industryId,
                                                    )
                                                }
                                                onClick={() =>
                                                    setSelectedIndustryId(
                                                        group.industryId,
                                                    )
                                                }
                                            >
                                                <ListItemText
                                                    primary={
                                                        group.industryName
                                                    }
                                                    secondary={`${group.specializations.length} specializations`}
                                                />

                                                <Typography
                                                    color="text.secondary"
                                                    aria-hidden="true"
                                                >
                                                    ›
                                                </Typography>
                                            </ListItemButton>
                                        ),
                                    )}
                                </List>
                            </Box>

                            <Box
                                sx={{
                                    p: 2,
                                    overflowY: "auto",
                                }}
                            >
                                {selectedIndustryGroup ? (
                                    <>
                                        <Typography
                                            variant="subtitle1"
                                            sx={{
                                                mb: 1,
                                                fontWeight: 700,
                                            }}
                                        >
                                            {
                                                selectedIndustryGroup.industryName
                                            }
                                        </Typography>

                                        <Stack spacing={0.25}>
                                            {selectedIndustryGroup.specializations.map(
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
                                                                borderRadius: 1,
                                                                px: 1,
                                                                "&:hover":
                                                                    {
                                                                        backgroundColor:
                                                                            "action.hover",
                                                                    },
                                                            }}
                                                        >
                                                            <FormControlLabel
                                                                sx={{
                                                                    flex: 1,
                                                                    minWidth: 0,
                                                                    m: 0,
                                                                }}
                                                                control={
                                                                    <Checkbox
                                                                        checked={offeredSpecializationIds.includes(
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
                                                                />
                                                            )}
                                                        </Box>
                                                    );
                                                },
                                            )}
                                        </Stack>
                                    </>
                                ) : (
                                    <Typography color="text.secondary">
                                        Select an industry.
                                    </Typography>
                                )}
                            </Box>
                        </Box>

                        <Divider />

                        <Box
                            sx={{
                                display: "flex",
                                justifyContent:
                                    "space-between",
                                alignItems: "center",
                                gap: 2,
                                px: 2,
                                py: 1.5,
                            }}
                        >
                            <Typography
                                variant="body2"
                                color="text.secondary"
                            >
                                {
                                    offeredSpecializationIds.length
                                }{" "}
                                selected
                            </Typography>

                            <Button
                                onClick={
                                    closeSpecializationPicker
                                }
                            >
                                Done
                            </Button>
                        </Box>
                    </Popover>

                    {selectedSpecializations.length > 0 && (
                        <Stack
                            direction="row"
                            useFlexGap
                            spacing={1}
                            sx={{
                                mt: 2,
                                flexWrap: "wrap",
                            }}
                        >
                            {selectedSpecializations.map(
                                (specialization) => (
                                    <Chip
                                        key={specialization.id}
                                        label={
                                            specialization.name
                                        }
                                        onDelete={() =>
                                            handleSpecializationChange(
                                                specialization.id,
                                            )
                                        }
                                        color={
                                            companySpecializationIds.has(
                                                specialization.id,
                                            )
                                                ? "primary"
                                                : "default"
                                        }
                                        variant="outlined"
                                    />
                                ),
                            )}
                        </Stack>
                    )}

                    {errors.offeredSpecializationIds && (
                        <FormHelperText
                            error
                            sx={{ mt: 1 }}
                        >
                            {
                                errors.offeredSpecializationIds
                            }
                        </FormHelperText>
                    )}
                </Box>

                <Divider />

                <Box>
                    <Typography variant="h6">
                        Price range
                    </Typography>

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={priceRangeEnabled}
                                onChange={(event) => {
                                    setPriceRangeEnabled(
                                        event.target.checked,
                                    );

                                    setErrors(
                                        (currentErrors) => ({
                                            ...currentErrors,
                                            priceRange: undefined,
                                        }),
                                    );
                                }}
                            />
                        }
                        label="Specify price range"
                    />
                </Box>

                {priceRangeEnabled && (
                    <Stack
                        direction={{
                            xs: "column",
                            sm: "row",
                        }}
                        spacing={2}
                    >
                        <TextField
                            label="Minimum price"
                            type="number"
                            value={priceRangeMin}
                            onChange={(event) =>
                                setPriceRangeMin(
                                    event.target.value,
                                )
                            }
                            fullWidth
                            required
                            error={Boolean(
                                errors.priceRange,
                            )}
                            slotProps={{
                                htmlInput: {
                                    min: 0,
                                    step: "0.01",
                                },
                            }}
                        />

                        <TextField
                            label="Maximum price"
                            type="number"
                            value={priceRangeMax}
                            onChange={(event) =>
                                setPriceRangeMax(
                                    event.target.value,
                                )
                            }
                            fullWidth
                            required
                            error={Boolean(
                                errors.priceRange,
                            )}
                            helperText={
                                errors.priceRange
                            }
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
                        Availability period
                    </Typography>

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={
                                    availabilityPeriodEnabled
                                }
                                onChange={(event) => {
                                    setAvailabilityPeriodEnabled(
                                        event.target.checked,
                                    );

                                    setErrors(
                                        (currentErrors) => ({
                                            ...currentErrors,
                                            availabilityPeriod:
                                            undefined,
                                        }),
                                    );
                                }}
                            />
                        }
                        label="Specify availability period"
                    />
                </Box>

                {availabilityPeriodEnabled && (
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
                            value={
                                availabilityPeriodFrom
                            }
                            onChange={(event) =>
                                setAvailabilityPeriodFrom(
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            error={Boolean(
                                errors.availabilityPeriod,
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
                                availabilityPeriodUntil
                            }
                            onChange={(event) =>
                                setAvailabilityPeriodUntil(
                                    event.target.value,
                                )
                            }
                            required
                            fullWidth
                            error={Boolean(
                                errors.availabilityPeriod,
                            )}
                            helperText={
                                errors.availabilityPeriod
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
                    Offer details
                </Typography>

                <Stack
                    direction={{
                        xs: "column",
                        sm: "row",
                    }}
                    spacing={2}
                >
                    <TextField
                        label="Service radius"
                        type="number"
                        value={serviceRadiusKm}
                        onChange={(event) => {
                            setServiceRadiusKm(
                                event.target.value,
                            );

                            setErrors((currentErrors) => ({
                                ...currentErrors,
                                serviceRadiusKm: undefined,
                            }));
                        }}
                        fullWidth
                        error={Boolean(
                            errors.serviceRadiusKm,
                        )}
                        helperText={
                            errors.serviceRadiusKm
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
                        label="Experience"
                        type="number"
                        value={experienceYears}
                        onChange={(event) => {
                            setExperienceYears(
                                event.target.value,
                            );

                            setErrors((currentErrors) => ({
                                ...currentErrors,
                                experienceYears: undefined,
                            }));
                        }}
                        fullWidth
                        error={Boolean(
                            errors.experienceYears,
                        )}
                        helperText={
                            errors.experienceYears
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
                        onChange={(event) => {
                            setMaxPartners(
                                event.target.value,
                            );

                            setErrors((currentErrors) => ({
                                ...currentErrors,
                                maxPartners: undefined,
                            }));
                        }}
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
                            createBusinessOfferMutation.isPending
                            || specializationGroups.length
                            === 0
                        }
                    >
                        {createBusinessOfferMutation.isPending
                            ? "Creating..."
                            : "Create business offer"}
                    </Button>
                </Box>
            </Stack>
        </Paper>
    );
}
