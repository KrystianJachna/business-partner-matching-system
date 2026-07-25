export interface NavigationItem {
    label: string;
    path: string;
}

export const navigationItems: NavigationItem[] = [
    {
        label: "Dashboard",
        path: "/dashboard",
    },
    {
        label: "Companies",
        path: "/companies",
    },
    {
        label: "Needs",
        path: "/needs",
    },
    {
        label: "Offers",
        path: "/offers",
    },
    {
        label: "Matching",
        path: "/matching",
    },
];
