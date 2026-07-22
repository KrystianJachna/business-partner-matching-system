-- =========================================================
-- Companies
-- =========================================================

INSERT INTO companies (
    active,
    name,
    description,
    industry_id,
    country,
    city,
    latitude,
    longitude,
    established_at,
    capabilities
)
VALUES
    (
        TRUE,
        'Krakow Digital Solutions',
        'Firma tworząca platformy internetowe i systemy dla biznesu.',
        (
            SELECT id
            FROM industries
            WHERE code = 'INFORMATION_TECHNOLOGY'
        ),
        'Poland',
        'Krakow',
        50.064650,
        19.944980,
        DATE '2018-03-15',
        'Projektowanie produktów cyfrowych, backend i integracje systemów.'
    ),
    (
        TRUE,
        'Cloud Experts',
        'Dostawca usług programistycznych i infrastruktury chmurowej.',
        (
            SELECT id
            FROM industries
            WHERE code = 'INFORMATION_TECHNOLOGY'
        ),
        'Poland',
        'Krakow',
        50.061430,
        19.936580,
        DATE '2014-06-01',
        'Tworzenie oprogramowania, migracje do chmury i DevOps.'
    ),
    (
        TRUE,
        'Data Systems',
        'Firma specjalizująca się w analizie danych i integracji systemów.',
        (
            SELECT id
            FROM industries
            WHERE code = 'INFORMATION_TECHNOLOGY'
        ),
        'Poland',
        'Katowice',
        50.264890,
        19.023780,
        DATE '2012-01-10',
        'Analiza danych, integracja systemów i tworzenie aplikacji.'
    ),
    (
        TRUE,
        'Secure IT Partners',
        'Firma świadcząca usługi związane z cyberbezpieczeństwem.',
        (
            SELECT id
            FROM industries
            WHERE code = 'INFORMATION_TECHNOLOGY'
        ),
        'Poland',
        'Rzeszow',
        50.041190,
        21.999120,
        DATE '2016-09-20',
        'Audyty bezpieczeństwa, testy penetracyjne i zabezpieczanie systemów.'
    ),
    (
        TRUE,
        'Marketing House',
        'Agencja marketingu cyfrowego i budowania marek.',
        (
            SELECT id
            FROM industries
            WHERE code = 'MARKETING'
        ),
        'Poland',
        'Warsaw',
        52.229770,
        21.011780,
        DATE '2019-05-20',
        'Kampanie internetowe, branding i strategia komunikacji.'
    ),
    (
        TRUE,
        'Steel Manufacturing',
        'Zakład produkcyjny zajmujący się obróbką elementów metalowych.',
        (
            SELECT id
            FROM industries
            WHERE code = 'MANUFACTURING'
        ),
        'Poland',
        'Gliwice',
        50.294490,
        18.671380,
        DATE '2008-02-01',
        'Obróbka metali, produkcja komponentów i automatyzacja procesów.'
    );

-- =========================================================
-- Company specializations
-- =========================================================

INSERT INTO company_specializations (
    company_id,
    specialization_id
)
SELECT
    company.id,
    specialization.id
FROM companies company
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'CLOUD_INFRASTRUCTURE'
                  )
WHERE company.name = 'Krakow Digital Solutions';

INSERT INTO company_specializations (
    company_id,
    specialization_id
)
SELECT
    company.id,
    specialization.id
FROM companies company
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'CLOUD_INFRASTRUCTURE'
                  )
WHERE company.name = 'Cloud Experts';

INSERT INTO company_specializations (
    company_id,
    specialization_id
)
SELECT
    company.id,
    specialization.id
FROM companies company
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'DATA_ANALYTICS'
                  )
WHERE company.name = 'Data Systems';

INSERT INTO company_specializations (
    company_id,
    specialization_id
)
SELECT
    company.id,
    specialization.id
FROM companies company
         JOIN specializations specialization
              ON specialization.code IN (
                                         'CYBERSECURITY',
                                         'CLOUD_INFRASTRUCTURE'
                  )
WHERE company.name = 'Secure IT Partners';

INSERT INTO company_specializations (
    company_id,
    specialization_id
)
SELECT
    company.id,
    specialization.id
FROM companies company
         JOIN specializations specialization
              ON specialization.code IN (
                                         'DIGITAL_MARKETING',
                                         'BRANDING'
                  )
WHERE company.name = 'Marketing House';

INSERT INTO company_specializations (
    company_id,
    specialization_id
)
SELECT
    company.id,
    specialization.id
FROM companies company
         JOIN specializations specialization
              ON specialization.code IN (
                                         'METAL_PROCESSING',
                                         'INDUSTRIAL_AUTOMATION'
                  )
WHERE company.name = 'Steel Manufacturing';

-- =========================================================
-- Business needs
-- =========================================================

INSERT INTO business_needs (
    created_at,
    updated_at,
    active,
    company_id,
    title,
    description,
    cooperation_type,
    max_distance_km,
    min_partner_experience_years,
    max_partners,
    budget_min,
    budget_max,
    budget_currency,
    required_from,
    required_until
)
VALUES
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Krakow Digital Solutions'
                ),
                'Development of a partner portal',
                'Poszukiwani partnerzy do stworzenia backendu oraz infrastruktury chmurowej portalu B2B.',
                'OUTSOURCING',
                200,
                3,
                2,
                40000.00,
                85000.00,
                'PLN',
                DATE '2026-08-01',
                DATE '2026-12-31'
    ),
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Marketing House'
                ),
                'Marketing analytics platform',
                'Poszukiwany partner do stworzenia systemu analizującego dane z kampanii marketingowych.',
                'OUTSOURCING',
                350,
                4,
                1,
                30000.00,
                65000.00,
                'PLN',
                DATE '2026-09-01',
                DATE '2027-02-28'
    ),
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Steel Manufacturing'
                ),
                'Industrial production line automation',
                'Poszukiwany partner do automatyzacji linii produkcyjnej.',
                'OUTSOURCING',
                150,
                5,
                1,
                100000.00,
                180000.00,
                'PLN',
                DATE '2026-10-01',
                DATE '2027-05-31'
    );

-- =========================================================
-- Required specializations for needs
-- =========================================================

INSERT INTO business_need_required_specializations (
    business_need_id,
    specialization_id
)
SELECT
    need.id,
    specialization.id
FROM business_needs need
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'CLOUD_INFRASTRUCTURE'
                  )
WHERE need.title = 'Development of a partner portal';

INSERT INTO business_need_required_specializations (
    business_need_id,
    specialization_id
)
SELECT
    need.id,
    specialization.id
FROM business_needs need
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'DATA_ANALYTICS'
                  )
WHERE need.title = 'Marketing analytics platform';

INSERT INTO business_need_required_specializations (
    business_need_id,
    specialization_id
)
SELECT
    need.id,
    specialization.id
FROM business_needs need
         JOIN specializations specialization
              ON specialization.code = 'INDUSTRIAL_AUTOMATION'
WHERE need.title = 'Industrial production line automation';

-- =========================================================
-- Business offers
-- =========================================================

INSERT INTO business_offers (
    created_at,
    updated_at,
    active,
    company_id,
    title,
    description,
    cooperation_type,
    service_radius_km,
    max_partners,
    price_min,
    price_max,
    price_currency,
    available_from,
    available_until
)
VALUES
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Cloud Experts'
                ),
                'Custom software and cloud implementation',
                'Tworzenie aplikacji biznesowych oraz wdrożenia infrastruktury chmurowej.',
                'OUTSOURCING',
                300,
                2,
                45000.00,
                75000.00,
                'PLN',
                DATE '2026-07-15',
                DATE '2027-01-31'
    ),
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Data Systems'
                ),
                'Software development and data analytics',
                'Tworzenie oprogramowania oraz rozwiązań analitycznych dla biznesu.',
                'OUTSOURCING',
                400,
                2,
                35000.00,
                70000.00,
                'PLN',
                DATE '2026-08-01',
                DATE '2027-03-31'
    ),
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Secure IT Partners'
                ),
                'Cloud security implementation',
                'Zabezpieczanie infrastruktury chmurowej i audyty bezpieczeństwa.',
                'OUTSOURCING',
                250,
                1,
                50000.00,
                90000.00,
                'PLN',
                DATE '2026-08-15',
                DATE '2027-02-28'
    ),
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                TRUE,
                (
                    SELECT id
                    FROM companies
                    WHERE name = 'Marketing House'
                ),
                'Digital marketing and branding services',
                'Prowadzenie kampanii cyfrowych i budowanie identyfikacji marki.',
                'OUTSOURCING',
                500,
                2,
                15000.00,
                40000.00,
                'PLN',
                DATE '2026-08-01',
                DATE '2027-06-30'
    );

-- =========================================================
-- Offered specializations
-- =========================================================

INSERT INTO business_offer_offered_specializations (
    business_offer_id,
    specialization_id
)
SELECT
    offer.id,
    specialization.id
FROM business_offers offer
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'CLOUD_INFRASTRUCTURE'
                  )
WHERE offer.title = 'Custom software and cloud implementation';

INSERT INTO business_offer_offered_specializations (
    business_offer_id,
    specialization_id
)
SELECT
    offer.id,
    specialization.id
FROM business_offers offer
         JOIN specializations specialization
              ON specialization.code IN (
                                         'SOFTWARE_DEVELOPMENT',
                                         'DATA_ANALYTICS'
                  )
WHERE offer.title = 'Software development and data analytics';

INSERT INTO business_offer_offered_specializations (
    business_offer_id,
    specialization_id
)
SELECT
    offer.id,
    specialization.id
FROM business_offers offer
         JOIN specializations specialization
              ON specialization.code IN (
                                         'CYBERSECURITY',
                                         'CLOUD_INFRASTRUCTURE'
                  )
WHERE offer.title = 'Cloud security implementation';

INSERT INTO business_offer_offered_specializations (
    business_offer_id,
    specialization_id
)
SELECT
    offer.id,
    specialization.id
FROM business_offers offer
         JOIN specializations specialization
              ON specialization.code IN (
                                         'DIGITAL_MARKETING',
                                         'BRANDING'
                  )
WHERE offer.title = 'Digital marketing and branding services';
