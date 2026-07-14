# Ustalenia

## Ogólne
- System będzie kojarzył firmy zgłaszające potrzeby biznesowe z firmami posiadającymi odpowiadające im oferty. (potrzeba firmy A ↔ oferta firmy B)
- algorytm proponuje partnerów, ale nie narzuca współpracy.
- Aplikacja będzie platformą do publikowania i wyszukiwania ofert oraz potrzeb współpracy biznesowej, rozszerzoną o mechanizm automatycznego rekomendowania partnerów na podstawie preferencji obu stron.
- system ma wspomagać kojarzenie potrzeb biznesowych z ofertami współpracy przy uwzględnieniu preferencji i ograniczeń obu stron.

## Aplikacji
- atrybuty: 
  - wspólne dla każdego (niezależnie czy firma szuka czy oferuje)
    - branża,
    - specjalizacja,
    - lokalizacja,
    - doświadczenie,
    - możliwości firmy.
  - potrzeby firmy (opis firmy czego oczekuje od partnera)
    - rodzaj poszukiwanej współpracy,
    - wymagane kompetencje lub specjalizacja,
    - dostępny budżet,
    - oczekiwana skala realizacji,
    - wymagany termin,
    - ewentualnie maksymalna odległość lub minimalne doświadczenie partnera.
    - maksymalna liczba poszukiwanych partnerów.
  - oferta firmy (firma określa co może zaoferować)
    - rodzaj oferowanej współpracy,
    - posiadane kompetencje lub specjalizacja,
    - oczekiwana cena,
    - możliwa skala realizacji,
    - dostępny termin,
    - obszar działania,
    - maksymalna liczba obsługiwanych partnerów.
- metoda AHP do ustalenia wag systemu scoringowego

## Teorii
- formalizacja problemu biznesowego,
- model scoringowego (ahp do ustalenia wag scoringu)
- zastosowania wariantu wielu-do-wielu,
- analiza algorytmu,
- porównania wyników w różnych scenariuszach,
- omówienia ograniczeń rozwiązania.