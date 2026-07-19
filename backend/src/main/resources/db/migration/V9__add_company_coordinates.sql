ALTER TABLE companies
    ADD COLUMN latitude NUMERIC(9, 6),
    ADD COLUMN longitude NUMERIC(9, 6);

ALTER TABLE companies
    ADD CONSTRAINT chk_companies_latitude
        CHECK (
            latitude IS NULL
                OR latitude BETWEEN -90 AND 90
            );

ALTER TABLE companies
    ADD CONSTRAINT chk_companies_longitude
        CHECK (
            longitude IS NULL
                OR longitude BETWEEN -180 AND 180
            );

ALTER TABLE companies
    ADD CONSTRAINT chk_companies_coordinates_pair
        CHECK (
            (latitude IS NULL AND longitude IS NULL)
                OR
            (latitude IS NOT NULL AND longitude IS NOT NULL)
            );
