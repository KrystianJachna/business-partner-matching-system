ALTER TABLE business_needs
    ADD COLUMN budget_min                     NUMERIC(15, 2),
    ADD COLUMN budget_max                     NUMERIC(15, 2),
    ADD COLUMN budget_currency                VARCHAR(3),
    ADD COLUMN required_from                  DATE,
    ADD COLUMN required_until                 DATE,
    ADD COLUMN max_distance_km                INTEGER,
    ADD COLUMN min_partner_experience_years   INTEGER,
    ADD COLUMN created_at                     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at                     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE business_needs
    ADD CONSTRAINT chk_business_needs_budget_values
        CHECK (
            budget_min IS NULL
                OR (
                budget_min >= 0
                    AND budget_max >= 0
                    AND budget_min <= budget_max
                )
            ),

    ADD CONSTRAINT chk_business_needs_budget_completeness
        CHECK (
            (
                budget_min IS NULL
                    AND budget_max IS NULL
                    AND budget_currency IS NULL
                )
                OR (
                budget_min IS NOT NULL
                    AND budget_max IS NOT NULL
                    AND budget_currency IS NOT NULL
                )
            ),

    ADD CONSTRAINT chk_business_needs_required_period
        CHECK (
            required_from IS NULL
                OR required_from <= required_until
            ),

    ADD CONSTRAINT chk_business_needs_required_period_completeness
        CHECK (
            (
                required_from IS NULL
                    AND required_until IS NULL
                )
                OR (
                required_from IS NOT NULL
                    AND required_until IS NOT NULL
                )
            ),

    ADD CONSTRAINT chk_business_needs_max_distance
        CHECK (
            max_distance_km IS NULL
                OR max_distance_km >= 0
            ),

    ADD CONSTRAINT chk_business_needs_min_experience
        CHECK (
            min_partner_experience_years IS NULL
                OR min_partner_experience_years >= 0
            );