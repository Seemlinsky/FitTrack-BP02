package nl.adainf.fittrack.model;

/*
 * User.java
 *
 * Dit is een modelklasse voor een gebruiker van FitTrack.
 *
 * Een gebruiker heeft:
 * - een id uit de database
 * - een naam
 *
 * In Java gebruik ik deze klasse als object.
 * Zo kan ik een gebruiker makkelijk doorgeven tussen schermen en DAO-klassen.
 *
 * Hier staat geen SQL-code.
 * SQL staat in UserDao.
 */

public class User {

    // id is het unieke nummer van de gebruiker in de database.
    private int id;

    // name is de naam van de gebruiker.
    private String name;

    // Constructor voor een gebruiker die al een id heeft, bijvoorbeeld uit de database.
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor voor een nieuwe gebruiker. De database maakt het id later aan.
    public User(String name) {
        this.name = name;
    }

    // Getter: hiermee haal ik het id van de gebruiker op.
    public int getId() {
        return id;
    }

    // Setter: hiermee kan ik het id later zetten.
    public void setId(int id) {
        this.id = id;
    }

    // Getter: hiermee haal ik de naam van de gebruiker op.
    public String getName() {
        return name;
    }

    // Setter: hiermee kan ik de naam aanpassen.
    public void setName(String name) {
        this.name = name;
    }

    // Dit zorgt ervoor dat in een ListView alleen de naam wordt getoond.
    @Override
    public String toString() {
        return name;
    }
}