INSERT INTO industries (code, name, active)
VALUES ('INFORMATION_TECHNOLOGY', 'Technologie informatyczne', true),
       ('CONSTRUCTION', 'Budownictwo', true),
       ('TRANSPORT', 'Transport', true),
       ('LOGISTICS', 'Logistyka', true),
       ('MANUFACTURING', 'Produkcja', true),
       ('FINANCE', 'Finanse', true),
       ('MARKETING', 'Marketing i reklama', true),
       ('HEALTHCARE', 'Ochrona zdrowia', true),
       ('ENERGY', 'Energetyka', true),
       ('EDUCATION', 'Edukacja', true),
       ('AGRICULTURE', 'Rolnictwo', true),
       ('PROFESSIONAL_SERVICES', 'Usługi profesjonalne', true)
ON CONFLICT (code) DO NOTHING;
