// import { useState } from "react";
// import { useNavigate } from "react-router";
// import {
//     Alert,
//     Box,
//     Button,
//     CircularProgress,
//     FormControl,
//     InputLabel,
//     MenuItem,
//     OutlinedInput,
//     Paper,
//     Select,
//     Stack,
//     TextField,
//     Typography,
// } from "@mui/material";
// import { useIndustries } from "../features/industry/hooks/useIndustries";
// import { useSpecializationsByIndustry } from "../features/specialization/hooks/useSpecializationsByIndustry";
// import { useCreateCompany } from "../features/company/hooks/useCreateCompany";
// import type { CreateCompanyRequest } from "../features/company/model/CreateCompanyRequest";
//
// export function CreateCompanyPage() {
//     const navigate = useNavigate();
//
//     const [name, setName] = useState("");
//     const [description, setDescription] = useState("");
//     const [industryId, setIndustryId] = useState<number | null>(null);
//     const [specializationIds, setSpecializationIds] = useState<number[]>([]);
//     const [country, setCountry] = useState("");
//     const [city, setCity] = useState("");
//     const [latitude, setLatitude] = useState("");
//     const [longitude, setLongitude] = useState("");
//     const [establishedAt, setEstablishedAt] = useState("");
//     const [capabilities, setCapabilities] = useState("");
//
//     const {
//         data: industries,
//         isLoading: industriesLoading,
//         isError: industriesError,
//     } = useIndustries();
//
//     const {
//         data: specializations,
//         isLoading: specializationsLoading,
//         isError: specializationsError,
//     } = useSpecializationsByIndustry(industryId);
//
//     const createCompanyMutation = useCreateCompany();
//
//     function handleIndustryChange(selectedIndustryId: number) {
//         setIndustryId(selectedIndustryId);
//         setSpecializationIds([]);
//     }
//
//     function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
//         event.preventDefault();
//
//         if (industryId === null) {
//             return;
//         }
//
//         const request: CreateCompanyRequest = {
//             name,
//             description: description.trim() || null,
//             industryId,
//             specializationIds,
//             country,
//             city,
//             latitude: Number(latitude),
//             longitude: Number(longitude),
//             establishedAt: establishedAt || null,
//             capabilities: capabilities.trim() || null,
//         };
//
//         createCompanyMutation.mutate(request, {
//             onSuccess: () => {
//                 navigate("/companies");
//             },
//         });
//     }
//
//     return (
//         <Box>
//             <Typography
//                 variant="h4"
//                 component="h1"
//                 sx={{
//                     mb: 1,
//                     fontWeight: 700,
//                 }}
//             >
//                 Add company
//             </Typography>
//
//             <Typography
//                 color="text.secondary"
//                 sx={{
//                     mb: 4,
//                 }}
//             >
//                 Register a company that can publish business needs and offers.
//             </Typography>
//
//             <Paper
//                 sx={{
//                     maxWidth: 900,
//                     p: 4,
//                     borderRadius: 3,
//                 }}
//             >
//                 <Box
//                     component="form"
//                     onSubmit={handleSubmit}
//                 >
//                     <Stack spacing={3}>
//                         {createCompanyMutation.isError && (
//                             <Alert severity="error">
//                                 Failed to create company.
//                             </Alert>
//                         )}
//
//                         {industriesError && (
//                             <Alert severity="error">
//                                 Failed to load industries.
//                             </Alert>
//                         )}
//
//                         <TextField
//                             label="Company name"
//                             value={name}
//                             onChange={(event) => setName(event.target.value)}
//                             required
//                             fullWidth
//                         />
//
//                         <TextField
//                             label="Description"
//                             value={description}
//                             onChange={(event) =>
//                                 setDescription(event.target.value)
//                             }
//                             multiline
//                             minRows={3}
//                             fullWidth
//                         />
//
//                         <FormControl
//                             required
//                             fullWidth
//                         >
//                             <InputLabel id="industry-label">
//                                 Industry
//                             </InputLabel>
//
//                             <Select
//                                 labelId="industry-label"
//                                 value={industryId ?? ""}
//                                 label="Industry"
//                                 disabled={industriesLoading}
//                                 onChange={(event) =>
//                                     handleIndustryChange(
//                                         Number(event.target.value),
//                                     )
//                                 }
//                             >
//                                 {industries?.map((industry) => (
//                                     <MenuItem
//                                         key={industry.id}
//                                         value={industry.id}
//                                     >
//                                         {industry.name}
//                                     </MenuItem>
//                                 ))}
//                             </Select>
//                         </FormControl>
//
//                         <FormControl
//                             required
//                             fullWidth
//                             disabled={
//                                 industryId === null ||
//                                 specializationsLoading
//                             }
//                         >
//                             <InputLabel id="specializations-label">
//                                 Specializations
//                             </InputLabel>
//
//                             <Select
//                                 labelId="specializations-label"
//                                 multiple
//                                 value={specializationIds}
//                                 input={
//                                     <OutlinedInput label="Specializations" />
//                                 }
//                                 onChange={(event) => {
//                                     const value = event.target.value;
//
//                                     setSpecializationIds(
//                                         typeof value === "string"
//                                             ? value
//                                                 .split(",")
//                                                 .map(Number)
//                                             : value,
//                                     );
//                                 }}
//                                 renderValue={(selectedIds) =>
//                                     specializations
//                                         ?.filter((specialization) =>
//                                             selectedIds.includes(
//                                                 specialization.id,
//                                             ),
//                                         )
//                                         .map(
//                                             (specialization) =>
//                                                 specialization.name,
//                                         )
//                                         .join(", ") ?? ""
//                                 }
//                             >
//                                 {specializations?.map((specialization) => (
//                                     <MenuItem
//                                         key={specialization.id}
//                                         value={specialization.id}
//                                     >
//                                         {specialization.name}
//                                     </MenuItem>
//                                 ))}
//                             </Select>
//                         </FormControl>
//
//                         {specializationsError && (
//                             <Alert severity="error">
//                                 Failed to load specializations.
//                             </Alert>
//                         )}
//
//                         <Stack
//                             direction={{
//                                 xs: "column",
//                                 sm: "row",
//                             }}
//                             spacing={2}
//                         >
//                             <TextField
//                                 label="Country"
//                                 value={country}
//                                 onChange={(event) =>
//                                     setCountry(event.target.value)
//                                 }
//                                 required
//                                 fullWidth
//                             />
//
//                             <TextField
//                                 label="City"
//                                 value={city}
//                                 onChange={(event) =>
//                                     setCity(event.target.value)
//                                 }
//                                 required
//                                 fullWidth
//                             />
//                         </Stack>
//
//                         <Stack
//                             direction={{
//                                 xs: "column",
//                                 sm: "row",
//                             }}
//                             spacing={2}
//                         >
//                             <TextField
//                                 label="Latitude"
//                                 type="number"
//                                 value={latitude}
//                                 onChange={(event) =>
//                                     setLatitude(event.target.value)
//                                 }
//                                 slotProps={{
//                                     htmlInput: {
//                                         step: "any",
//                                         min: -90,
//                                         max: 90,
//                                     },
//                                 }}
//                                 required
//                                 fullWidth
//                             />
//
//                             <TextField
//                                 label="Longitude"
//                                 type="number"
//                                 value={longitude}
//                                 onChange={(event) =>
//                                     setLongitude(event.target.value)
//                                 }
//                                 slotProps={{
//                                     htmlInput: {
//                                         step: "any",
//                                         min: -180,
//                                         max: 180,
//                                     },
//                                 }}
//                                 required
//                                 fullWidth
//                             />
//                         </Stack>
//
//                         <TextField
//                             label="Established at"
//                             type="date"
//                             value={establishedAt}
//                             onChange={(event) =>
//                                 setEstablishedAt(event.target.value)
//                             }
//                             slotProps={{
//                                 inputLabel: {
//                                     shrink: true,
//                                 },
//                             }}
//                             fullWidth
//                         />
//
//                         <TextField
//                             label="Capabilities"
//                             value={capabilities}
//                             onChange={(event) =>
//                                 setCapabilities(event.target.value)
//                             }
//                             multiline
//                             minRows={3}
//                             fullWidth
//                         />
//
//                         <Stack
//                             direction="row"
//                             spacing={2}
//                             sx={{
//                                 justifyContent: "flex-end",
//                             }}
//                         >
//                             <Button
//                                 type="button"
//                                 variant="outlined"
//                                 disabled={createCompanyMutation.isPending}
//                                 onClick={() => navigate("/companies")}
//                             >
//                                 Cancel
//                             </Button>
//
//                             <Button
//                                 type="submit"
//                                 variant="contained"
//                                 disabled={
//                                     createCompanyMutation.isPending ||
//                                     industryId === null ||
//                                     specializationIds.length === 0
//                                 }
//                             >
//                                 {createCompanyMutation.isPending ? (
//                                     <CircularProgress
//                                         size={22}
//                                         color="inherit"
//                                     />
//                                 ) : (
//                                     "Create company"
//                                 )}
//                             </Button>
//                         </Stack>
//                     </Stack>
//                 </Box>
//             </Paper>
//         </Box>
//     );
// }


import {
    Box,
    Button,
    Paper,
    Stack,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";

export function CreateCompanyPage() {
    const navigate = useNavigate();

    return (
        <Box>
            <Stack
                direction="row"
                sx={{
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 4,
                }}
            >
                <Box>
                    <Typography
                        variant="h4"
                        component="h1"
                        sx={{
                            mb: 1,
                            fontWeight: 700,
                        }}
                    >
                        Add company
                    </Typography>

                    <Typography color="text.secondary">
                        Register a new company in the system.
                    </Typography>
                </Box>

                <Button
                    variant="outlined"
                    onClick={() => navigate("/companies")}
                >
                    Back to companies
                </Button>
            </Stack>

            <Paper
                sx={{
                    maxWidth: 900,
                    p: 4,
                    borderRadius: 3,
                }}
            >
                <Typography
                    variant="h6"
                    sx={{
                        mb: 1,
                        fontWeight: 700,
                    }}
                >
                    Company form
                </Typography>

                <Typography color="text.secondary">
                    The company creation form will be implemented here.
                </Typography>
            </Paper>
        </Box>
    );
}
