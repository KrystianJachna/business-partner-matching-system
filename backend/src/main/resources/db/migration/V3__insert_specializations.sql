INSERT INTO specializations (code, name, active, industry_id)
SELECT specialization.code,
       specialization.name,
       true,
       industry.id
FROM (VALUES
          -- Technologie informatyczne
          ('SOFTWARE_DEVELOPMENT', 'Tworzenie oprogramowania', 'INFORMATION_TECHNOLOGY'),
          ('CYBERSECURITY', 'Cyberbezpieczeństwo', 'INFORMATION_TECHNOLOGY'),
          ('DATA_ANALYTICS', 'Analiza danych', 'INFORMATION_TECHNOLOGY'),
          ('CLOUD_INFRASTRUCTURE', 'Usługi chmurowe i infrastruktura IT', 'INFORMATION_TECHNOLOGY'),
          ('IT_CONSULTING', 'Doradztwo informatyczne', 'INFORMATION_TECHNOLOGY'),

          -- Budownictwo
          ('GENERAL_CONSTRUCTION', 'Budownictwo ogólne', 'CONSTRUCTION'),
          ('RESIDENTIAL_CONSTRUCTION', 'Budownictwo mieszkaniowe', 'CONSTRUCTION'),
          ('INDUSTRIAL_CONSTRUCTION', 'Budownictwo przemysłowe', 'CONSTRUCTION'),
          ('CONSTRUCTION_INSTALLATIONS', 'Instalacje budowlane', 'CONSTRUCTION'),
          ('CONSTRUCTION_DESIGN', 'Projektowanie budowlane', 'CONSTRUCTION'),

          -- Transport
          ('ROAD_FREIGHT_TRANSPORT', 'Transport drogowy towarów', 'TRANSPORT'),
          ('PASSENGER_TRANSPORT', 'Transport pasażerski', 'TRANSPORT'),
          ('RAIL_TRANSPORT', 'Transport kolejowy', 'TRANSPORT'),
          ('MARITIME_TRANSPORT', 'Transport morski', 'TRANSPORT'),
          ('SPECIALIZED_TRANSPORT', 'Transport specjalistyczny', 'TRANSPORT'),

          -- Logistyka
          ('WAREHOUSING', 'Magazynowanie', 'LOGISTICS'),
          ('SUPPLY_CHAIN_MANAGEMENT', 'Zarządzanie łańcuchem dostaw', 'LOGISTICS'),
          ('FREIGHT_FORWARDING', 'Spedycja', 'LOGISTICS'),
          ('LAST_MILE_DELIVERY', 'Dostawy ostatniej mili', 'LOGISTICS'),
          ('FULFILLMENT_SERVICES', 'Obsługa zamówień i fulfillment', 'LOGISTICS'),

          -- Produkcja
          ('METAL_PROCESSING', 'Obróbka metali', 'MANUFACTURING'),
          ('PLASTICS_MANUFACTURING', 'Produkcja tworzyw sztucznych', 'MANUFACTURING'),
          ('ELECTRONICS_MANUFACTURING', 'Produkcja elektroniki', 'MANUFACTURING'),
          ('FOOD_MANUFACTURING', 'Produkcja spożywcza', 'MANUFACTURING'),
          ('INDUSTRIAL_AUTOMATION', 'Automatyka przemysłowa', 'MANUFACTURING'),

          -- Finanse
          ('ACCOUNTING_SERVICES', 'Usługi księgowe', 'FINANCE'),
          ('TAX_ADVISORY', 'Doradztwo podatkowe', 'FINANCE'),
          ('FINANCIAL_ANALYSIS', 'Analiza finansowa', 'FINANCE'),
          ('INSURANCE_SERVICES', 'Usługi ubezpieczeniowe', 'FINANCE'),
          ('PAYMENT_SERVICES', 'Usługi płatnicze', 'FINANCE'),

          -- Marketing i reklama
          ('DIGITAL_MARKETING', 'Marketing cyfrowy', 'MARKETING'),
          ('CONTENT_MARKETING', 'Content marketing', 'MARKETING'),
          ('BRANDING', 'Budowanie marki', 'MARKETING'),
          ('MARKET_RESEARCH', 'Badania rynku', 'MARKETING'),
          ('PUBLIC_RELATIONS', 'Public relations', 'MARKETING'),

          -- Ochrona zdrowia
          ('MEDICAL_SERVICES', 'Usługi medyczne', 'HEALTHCARE'),
          ('MEDICAL_DIAGNOSTICS', 'Diagnostyka medyczna', 'HEALTHCARE'),
          ('PHARMACEUTICAL_SERVICES', 'Usługi farmaceutyczne', 'HEALTHCARE'),
          ('MEDICAL_EQUIPMENT', 'Sprzęt medyczny', 'HEALTHCARE'),
          ('REHABILITATION_SERVICES', 'Rehabilitacja', 'HEALTHCARE'),

          -- Energetyka
          ('RENEWABLE_ENERGY', 'Energia odnawialna', 'ENERGY'),
          ('POWER_ENGINEERING', 'Energetyka zawodowa', 'ENERGY'),
          ('ELECTRICAL_INSTALLATIONS', 'Instalacje elektryczne', 'ENERGY'),
          ('ENERGY_AUDITING', 'Audyty energetyczne', 'ENERGY'),
          ('ENERGY_STORAGE', 'Magazynowanie energii', 'ENERGY'),

          -- Edukacja
          ('CORPORATE_TRAINING', 'Szkolenia dla firm', 'EDUCATION'),
          ('ONLINE_EDUCATION', 'Edukacja internetowa', 'EDUCATION'),
          ('LANGUAGE_TRAINING', 'Szkolenia językowe', 'EDUCATION'),
          ('VOCATIONAL_TRAINING', 'Szkolenia zawodowe', 'EDUCATION'),
          ('EDUCATIONAL_CONTENT', 'Tworzenie materiałów edukacyjnych', 'EDUCATION'),

          -- Rolnictwo
          ('CROP_PRODUCTION', 'Produkcja roślinna', 'AGRICULTURE'),
          ('ANIMAL_HUSBANDRY', 'Hodowla zwierząt', 'AGRICULTURE'),
          ('AGRICULTURAL_MACHINERY', 'Maszyny rolnicze', 'AGRICULTURE'),
          ('AGRICULTURAL_SERVICES', 'Usługi rolnicze', 'AGRICULTURE'),
          ('ORGANIC_FARMING', 'Rolnictwo ekologiczne', 'AGRICULTURE'),

          -- Usługi profesjonalne
          ('LEGAL_SERVICES', 'Usługi prawne', 'PROFESSIONAL_SERVICES'),
          ('BUSINESS_CONSULTING', 'Doradztwo biznesowe', 'PROFESSIONAL_SERVICES'),
          ('HR_SERVICES', 'Usługi HR i rekrutacja', 'PROFESSIONAL_SERVICES'),
          ('PROJECT_MANAGEMENT', 'Zarządzanie projektami', 'PROFESSIONAL_SERVICES'),
          ('AUDIT_SERVICES', 'Usługi audytorskie',
           'PROFESSIONAL_SERVICES')) AS specialization(code, name, industry_code)
         JOIN industries industry
              ON industry.code = specialization.industry_code
    ON CONFLICT (code) DO NOTHING;