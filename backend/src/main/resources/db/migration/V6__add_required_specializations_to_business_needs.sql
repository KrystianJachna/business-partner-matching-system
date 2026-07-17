CREATE TABLE business_need_required_specializations
(
    business_need_id BIGINT NOT NULL,
    specialization_id BIGINT NOT NULL,

    CONSTRAINT pk_business_need_required_specializations
        PRIMARY KEY (business_need_id, specialization_id),

    CONSTRAINT fk_business_need_specializations_need
        FOREIGN KEY (business_need_id)
            REFERENCES business_needs (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_business_need_specializations_specialization
        FOREIGN KEY (specialization_id)
            REFERENCES specializations (id)
);