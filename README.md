# FitTrack – BP02 Realisatie

## Wat is dit project?

FitTrack is een JavaFX applicatie waarmee een gebruiker:

- Workouts kan registreren
- Maaltijden kan toevoegen
- Een dagoverzicht kan bekijken (calorie totalen)
- Gegevens kan aanpassen en verwijderen (CRUD)

De applicatie gebruikt een MySQL database via JDBC (externe systeemkoppeling).

---

## Wat toont dit project aan (BP02 eisen)?

Dit project bevat:

- Volledig object-georiënteerde Java code
- Externe koppeling via JDBC
- Minimaal 4 database tabellen
- Alle CRUD operaties (Create, Read, Update, Delete)
- Minimaal 5 SQL query clauses:
  - WHERE
  - ORDER BY
  - GROUP BY
  - LIMIT
  - JOIN
- Meerdere unit tests (JUnit 5)
- Werkend artefact (uitvoerbare applicatie)
- Implementatieplan
- Testgevallen + Acceptatietest rapport

---

## Database installatie

1. Start XAMPP en zet MySQL aan.
2. Ga naar http://localhost/phpmyadmin
3. Klik op "Nieuw" en maak een database aan met naam: `fittrack`
4. Selecteer de database.
5. Klik op het tabblad **Importeren**.
6. Kies het bestand `fittrack.sql` (staat in de root van dit project).
7. Klik op **Start**.
8. Controleer of de tabellen zijn aangemaakt:
  - users
  - workout
  - workout_entry
  - meal_entry
  - activity_type


`fittrack-structure.sql`


4. Controleer of database `fittrack` bestaat.

---

## Applicatie starten

### Optie 1 – Via IntelliJ

- Open het project.
- Run `Launcher` of `MainApp`.

---

### Optie 2 – Via Maven (aanbevolen)


```bash
.\mvnw.cmd clean test
.\mvnw.cmd javafx:run
```


---

### Optie 3 – Jar bestand gebruiken

De uitvoerbare jar staat in:


out/artifacts/fittrack_jar/fittrack.jar


Starten met:


```bash
java -jar out/artifacts/fittrack_jar/fittrack.jar
```


---

## Bewijs (handig voor beoordeling)

- CRUD overzicht: `Overzicht_CRUD_Statements.docx`
- Acceptatietest rapport: `FitTrack_Acceptatietest_Rapport.docx`
- Implementatieplan: `FitTrack_Implementatieplan_BP02.docx`

Tip: zet deze bestanden ook in de root van je repo of in een map `docs/`.

## SQL Overzicht

De volgende SQL onderdelen zijn gebruikt:

- SELECT
- INSERT
- UPDATE
- DELETE
- WHERE
- ORDER BY
- GROUP BY
- LIMIT
- JOIN

Alle CRUD statements zijn aantoonbaar aanwezig in de DAO-klassen.

---

## Unit Tests

- Meerdere JUnit 5 tests aanwezig (zie map `src/test/`)
- Tests draaien via Maven:

```bash
.\mvnw.cmd clean test
```

---

## Implementatieplan

Bevat:

- Huidige situatie
- Implementatiestappen
- Tijdlijn
- Communicatiestrategie
- Evaluatie en verificatie

Apart ingeleverd via Brightspace.

---

## Testdocumentatie

De volgende documenten zijn opgenomen in de map `docs/`:
Ingeleverd via Brightspace (en/of in repo `docs/`):

- `FitTrack_Testgevallen.xlsx`  
  → Overzicht van alle uitgevoerde testgevallen met PASS/FAIL resultaten.

- `FitTrack_Acceptatietest_Rapport_.docx`  
  → Samenvatting van de acceptatietest inclusief bevindingen.

- `FitTrack_Implementatieplan_BP02.docx`  
  → Beschrijving van huidige situatie, implementatiestappen, planning en evaluatie.

- `Overzicht_CRUD_Statements_FitTrack.docx`  
  → Overzicht van alle gebruikte SQL statements (Create, Read, Update, Delete).
---

## Artefact

De applicatie is uitvoerbaar via:


out/artifacts/fittrack_jar/fittrack.jar


De build is succesvol getest.

---

## Inlevering BP02

- GitHub repository (public)
- SQL export (.sql)
- Werkend artefact
- Unit tests
- Testgevallen spreadsheet
- Acceptatietest rapport
- Implementatieplan