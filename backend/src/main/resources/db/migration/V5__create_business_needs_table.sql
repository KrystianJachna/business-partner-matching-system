CREATE TABLE business_needs
(
    id               BIGSERIAL PRIMARY KEY,
    company_id       BIGINT       NOT NULL,
    title            VARCHAR(150) NOT NULL,
    description      VARCHAR(2000),
    cooperation_type VARCHAR(50)  NOT NULL,
    max_partners     INTEGER      NOT NULL,
    active           BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_business_needs_company
        FOREIGN KEY (company_id)
            REFERENCES companies (id),

    CONSTRAINT chk_business_needs_max_partners
        CHECK (max_partners > 0)
);