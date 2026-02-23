package nl.adainf.fittrack.model;



/*
 * ActivityType.java - Model/Entity: 1 object = 1 rij uit de database
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

// Dit is een "plain" model: velden + constructor + getters/setters.
// Geen SQL hier, dat zit in de DAO.
public class ActivityType {
    // Velden (kolommen uit de DB)

    private int id;
    private String name;

    public ActivityType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
