# FitTrack BP02 Herkansing

## Studentgegevens

- **Naam:** Stefan Stojkovic
- **Studentnummer:** 2237080
- **Opleiding:** Associate degree Informatica
- **Beroepsproduct:** BP02 Realisatie / JavaFX
- **Project:** FitTrack

---

## Korte uitleg van het project

FitTrack is een JavaFX desktopapplicatie waarmee een gebruiker zijn workouts en maaltijden kan bijhouden.
De gebruiker kan gegevens invoeren, opslaan in een MySQL-database en later terugzien in een overzicht.

Ik heb dit project gemaakt voor BP02. Bij de herkansing heb ik extra gelet op de lesstof uit de JavaFX-reader. Daarom is het project opgebouwd met JavaFX-schermen, usercontrols, eigen modelklassen, databasekoppeling, SQL-statements en unittests.

---

## Functionaliteiten

De applicatie bevat de volgende onderdelen:

* Gebruiker aanmaken of selecteren
* Workout toevoegen
* Maaltijd toevoegen
* Dagoverzicht bekijken
* Data opslaan in een MySQL-database
* Data ophalen uit de database
* Data aanpassen of verwijderen waar dit nodig is
* Simpele validatie op invoer
* Unit tests voor belangrijke logica

---

## Gebruikte technieken

Voor dit project heb ik de volgende technieken gebruikt:

* Java
* JavaFX
* Maven
* MySQL
* JDBC
* JUnit
* IntelliJ IDEA
* Git en GitHub

---

## Koppeling met de lesstof

In de JavaFX-reader worden verschillende onderdelen behandeld. Deze onderdelen komen ook terug in mijn FitTrack-project.

### JavaFX schermopbouw

De applicatie gebruikt een JavaFX `Stage` en `Scene`.
De `Stage` is het hoofdvenster van de applicatie.
De `Scene` bevat de inhoud van het scherm, zoals knoppen, labels en invoervelden.

### Usercontrols

In de schermen gebruik ik JavaFX-controls zoals:

* `Label` voor tekst op het scherm
* `TextField` voor invoer
* `Button` voor acties
* `ComboBox` voor keuzes
* `DatePicker` voor datumkeuze
* `TableView` of lijsten voor overzicht van data

### Layoutmanagers

Voor de opmaak van de schermen gebruik ik layoutmanagers zoals:

* `VBox`
* `HBox`
* `GridPane`
* `BorderPane`

Deze zorgen ervoor dat de onderdelen netjes op het scherm staan.

### Data invoeren en ophalen

De gebruiker vult gegevens in via invoervelden.
Met methodes zoals `getText()` haal ik de ingevoerde data op.
Met `setOnAction()` geef ik knoppen een actie, bijvoorbeeld het opslaan van een workout.

### Object georiënteerd programmeren

De applicatie gebruikt eigen modelklassen, zoals:

* `User`
* `Workout`
* `WorkoutEntry`
* `MealEntry`
* `ActivityType`

Deze klassen slaan gegevens op in objecten.
De objecten worden daarna gebruikt om data overzichtelijk door de applicatie te sturen.

### Constructor en getters

De modelklassen hebben constructors om objecten aan te maken.
Met getters kan de applicatie gegevens uit deze objecten ophalen.

### Overerving

In het project gebruik ik overerving met de klasse `FitTrackItem`.

`FitTrackItem` is de algemene klasse. Hierin staan gedeelde velden zoals `id` en `userId`.

De klassen `Workout` en `MealEntry` erven van `FitTrackItem`. Hierdoor hoef ik `id` en `userId` niet dubbel in beide klassen te zetten.

Dit sluit aan op de lesstof over superklassen en subklassen.

### Polymorfie en instanceof

Omdat `Workout` en `MealEntry` allebei erven van `FitTrackItem`, kan ik ze behandelen als een algemeen `FitTrackItem` object.

In `FitTrackItemUtil` gebruik ik `instanceof` om te controleren welk specifiek type object het is.

Bijvoorbeeld:

- als het object een `Workout` is, geeft de methode `"Workout"` terug
- als het object een `MealEntry` is, geeft de methode `"MealEntry"` terug

Dit sluit aan op de lesstof over polymorfie en `instanceof`.

### Dataconversie

Invoer uit een `TextField` komt eerst binnen als tekst.
Wanneer ik een getal nodig heb, zet ik deze tekst om naar bijvoorbeeld een `int` of `double`.
Hierbij gebruik ik foutafhandeling zodat de applicatie niet crasht bij verkeerde invoer.

### Databasekoppeling

De applicatie maakt verbinding met een MySQL-database via JDBC.
De databaseverbinding staat apart in een databaseklasse, zodat de verbinding op één plek beheerd wordt.

### SQL-statements

De applicatie gebruikt SQL-statements voor databasebewerkingen:

* `INSERT` om data toe te voegen
* `SELECT` om data op te halen
* `UPDATE` om data aan te passen
* `DELETE` om data te verwijderen

### Unittests

Er zijn meerdere JUnit-tests toegevoegd om belangrijke logica te testen.
Hiermee controleer ik bijvoorbeeld validatie en berekeningen.

---

## Installatie en configuratie

### Benodigdheden

Zorg dat de volgende programma’s geïnstalleerd zijn:

* IntelliJ IDEA
* Java JDK
* Maven
* MySQL Server
* MySQL Workbench of phpMyAdmin

### Database instellen

1. Open MySQL Workbench of phpMyAdmin.
2. Maak een database aan met de naam:

```sql
fittrack
```

3. Importeer het bestand:

```text
fittrack.sql
```

4. Controleer in de Java-code of de databasegegevens kloppen, zoals gebruikersnaam en wachtwoord.

Voorbeeld:

```java
private static final String URL = "jdbc:mysql://localhost:3306/fittrack";
private static final String USER = "root";
private static final String PASSWORD = "";
```

Pas het wachtwoord aan als jouw MySQL een wachtwoord gebruikt.

---

## Applicatie starten

Open het project in IntelliJ IDEA.

Voer daarna in de terminal uit:

```bash
mvn clean javafx:run
```

Als je eerst de tests wilt uitvoeren:

```bash
mvn clean test
```

---

## Gebruik van de applicatie

1. Start de applicatie.
2. Maak een gebruiker aan of kies een bestaande gebruiker.
3. Voeg een workout toe.
4. Voeg een maaltijd toe.
5. Bekijk het dagoverzicht.
6. Controleer of de gegevens correct worden opgeslagen en opgehaald.

Een uitgebreidere gebruikershandleiding staat in:

```text
USAGE.md
```

---

## Projectstructuur

```text
src/main/java
└── nl/adainf/fittrack
    ├── MainApp.java
    ├── database
    │   └── Database.java
    ├── dao
    │   ├── UserDao.java
    │   ├── WorkoutDao.java
    │   └── MealEntryDao.java
    ├── model
    │   ├── User.java
    │   ├── Workout.java
    │   ├── WorkoutEntry.java
    │   ├── MealEntry.java
    │   └── ActivityType.java
    ├── screens
    │   ├── StartScreen.java
    │   ├── WorkoutScreen.java
    │   └── OverviewScreen.java
    └── util
        └── Validator.java
```

---

## Versiebeheer

Tijdens de herkansing heb ik gelet op kleinere commits met duidelijke beschrijvingen.
Hierdoor is beter te zien welke onderdelen stap voor stap zijn gemaakt.

Voorbeelden van commitberichten:

| Commit                                        | Beschrijving                             |
| --------------------------------------------- | ---------------------------------------- |
| `docs: verbeter README met projectinformatie` | README duidelijker gemaakt               |
| `feat: voeg modelklassen toe`                 | Modelklassen toegevoegd                  |
| `feat: voeg databaseverbinding toe`           | JDBC databasekoppeling toegevoegd        |
| `feat: maak workout scherm`                   | Scherm gemaakt om workouts toe te voegen |
| `test: voeg unit tests toe`                   | Extra unit tests toegevoegd              |
| `fix: verbeter invoervalidatie`               | Validatie verbeterd                      |

---

## Roadmap

Dingen die ik in de toekomst nog zou willen verbeteren:

* Login met wachtwoord toevoegen
* Grafieken maken voor voortgang
* Meer sportcategorieën toevoegen
* Export naar PDF of CSV toevoegen
* Betere foutmeldingen voor de gebruiker
* Meer filters in het overzicht

---

## Bronnen

* Reader Programmeren in JavaFX - Blok 2
* JavaFX documentatie
* MySQL documentatie
* Maven documentatie
* JUnit documentatie
