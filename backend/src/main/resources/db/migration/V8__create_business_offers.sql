CREATE TABLE business_offers
(
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT       NOT NULL,
    title               VARCHAR(150) NOT NULL,
    description         VARCHAR(2000),
    cooperation_type    VARCHAR(50)  NOT NULL,

    price_min           NUMERIC(15, 2),
    price_max           NUMERIC(15, 2),
    price_currency      VARCHAR(3),

    available_from      DATE,
    available_until     DATE,

    service_radius_km   INTEGER,
    max_partners        INTEGER      NOT NULL,

    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP    NOT NULL,
    updated_at          TIMESTAMP    NOT NULL,

    CONSTRAINT fk_business_offers_company
        FOREIGN KEY (company_id)
            REFERENCES companies (id),

    CONSTRAINT chk_business_offers_price_min
        CHECK (price_min IS NULL OR price_min >= 0),

    CONSTRAINT chk_business_offers_price_max
        CHECK (price_max IS NULL OR price_max >= 0),

    CONSTRAINT chk_business_offers_price_range
        CHECK (
            price_min IS NULL
                OR price_max IS NULL
                OR price_min <= price_max
            ),

    CONSTRAINT chk_business_offers_price_completeness
        CHECK (
            (
                price_min IS NULL
                    AND price_max IS NULL
                    AND price_currency IS NULL
                )
                OR
            (
                price_min IS NOT NULL
                    AND price_max IS NOT NULL
                    AND price_currency IS NOT NULL
                )
            ),

    CONSTRAINT chk_business_offers_availability_completeness
        CHECK (
            (
                available_from IS NULL
                    AND available_until IS NULL
                )
                OR
            (
                available_from IS NOT NULL
                    AND available_until IS NOT NULL
                )
            ),

    CONSTRAINT chk_business_offers_availability_range
        CHECK (
            available_from IS NULL
                OR available_until IS NULL
                OR available_from <= available_until
            ),

    CONSTRAINT chk_business_offers_service_radius
        CHECK (
            service_radius_km IS NULL
                OR service_radius_km >= 0
            ),

    CONSTRAINT chk_business_offers_max_partners
        CHECK (max_partners > 0)
);

CREATE TABLE business_offer_offered_specializations
(
    business_offer_id BIGINT NOT NULL,
    specialization_id BIGINT NOT NULL,

    PRIMARY KEY (business_offer_id, specialization_id),

    CONSTRAINT fk_business_offer_specializations_offer
        FOREIGN KEY (business_offer_id)
            REFERENCES business_offers (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_business_offer_specializations_specialization
        FOREIGN KEY (specialization_id)
            REFERENCES specializations (id)
);
