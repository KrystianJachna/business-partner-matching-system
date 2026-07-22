INSERT INTO industries (
    code,
    name,
    active
)
VALUES
    (
        'INFORMATION_TECHNOLOGY',
        'Technologie informatyczne',
        TRUE
    ),
    (
        'MARKETING',
        'Marketing i reklama',
        TRUE
    ),
    (
        'MANUFACTURING',
        'Produkcja',
        TRUE
    );

INSERT INTO specializations (
    code,
    name,
    industry_id,
    active
)
SELECT
    specialization.code,
    specialization.name,
    industry.id,
    TRUE
FROM (
         VALUES
             (
                 'INFORMATION_TECHNOLOGY',
                 'SOFTWARE_DEVELOPMENT',
                 'Tworzenie oprogramowania'
             ),
             (
                 'INFORMATION_TECHNOLOGY',
                 'CLOUD_INFRASTRUCTURE',
                 'Infrastruktura chmurowa'
             ),
             (
                 'INFORMATION_TECHNOLOGY',
                 'DATA_ANALYTICS',
                 'Analiza danych'
             ),
             (
                 'INFORMATION_TECHNOLOGY',
                 'CYBERSECURITY',
                 'Cyberbezpieczeństwo'
             ),
             (
                 'MARKETING',
                 'DIGITAL_MARKETING',
                 'Marketing cyfrowy'
             ),
             (
                 'MARKETING',
                 'BRANDING',
                 'Budowanie marki'
             ),
             (
                 'MANUFACTURING',
                 'METAL_PROCESSING',
                 'Obróbka metali'
             ),
             (
                 'MANUFACTURING',
                 'INDUSTRIAL_AUTOMATION',
                 'Automatyka przemysłowa'
             )
     ) AS specialization(
                         industry_code,
                         code,
                         name
    )
         JOIN industries industry
              ON industry.code = specialization.industry_code;
