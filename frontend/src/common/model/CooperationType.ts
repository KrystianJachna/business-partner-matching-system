export type CooperationType =
    | "SUBCONTRACTING"
    | "SUPPLY"
    | "DISTRIBUTION"
    | "OUTSOURCING"
    | "CONSULTING"
    | "TECHNOLOGY_PARTNERSHIP"
    | "JOINT_PROJECT";

export const cooperationTypes: CooperationType[] = [
    "SUBCONTRACTING",
    "SUPPLY",
    "DISTRIBUTION",
    "OUTSOURCING",
    "CONSULTING",
    "TECHNOLOGY_PARTNERSHIP",
    "JOINT_PROJECT",
];

export const cooperationTypeLabels: Record<
    CooperationType,
    string
> = {
    SUBCONTRACTING: "Subcontracting",
    SUPPLY: "Supply",
    DISTRIBUTION: "Distribution",
    OUTSOURCING: "Outsourcing",
    CONSULTING: "Consulting",
    TECHNOLOGY_PARTNERSHIP: "Technology partnership",
    JOINT_PROJECT: "Joint project",
};
