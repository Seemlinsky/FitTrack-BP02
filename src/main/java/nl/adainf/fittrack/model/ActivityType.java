package nl.adainf.fittrack.model;

/*
 * ActivityType.java
 *
 * Dit is een modelklasse voor een soort activiteit.
 * Bijvoorbeeld: hardlopen, fietsen, fitness of wandelen.
 *
 * In de database staat dit als een rij in de activity_type tabel.
 * In Java maak ik daar een object van.
 *
 * Hier staat geen SQL-code.
 * SQL staat in de DAO-klassen, zoals ActivityTypeDao.
 */

public class ActivityType {

    // id is het unieke nummer van de activiteit in de database.
    private int id;

    // name is de naam van de activiteit.
    private String name;

    // Constructor: hiermee maak ik een ActivityType object aan.
    public ActivityType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter: hiermee haal ik het id van de activiteit op.
    public int getId() {
        return id;
    }

    // Getter: hiermee haal ik de naam van de activiteit op.
    public String getName() {
        return name;
    }

    // Deze methode zorgt dat de naam netjes zichtbaar is in bijvoorbeeld een ComboBox.
    @Override
    public String toString() {
        return name;
    }
}