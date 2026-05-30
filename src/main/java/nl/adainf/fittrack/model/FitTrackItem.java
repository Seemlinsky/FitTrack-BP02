package nl.adainf.fittrack.model;

/*
 * FitTrackItem.java
 *
 * Dit is een algemene modelklasse voor FitTrack onderdelen.
 *
 * Deze klasse gebruik ik voor overerving.
 * Workout en MealEntry hebben allebei een id en userId.
 * Daarom zet ik die gedeelde velden hier op één plek.
 *
 * Daardoor hoef ik id en userId niet dubbel in Workout en MealEntry te zetten.
 */

public class FitTrackItem {

    // id is het unieke nummer van dit item in de database.
    private int id;

    // userId geeft aan bij welke gebruiker dit item hoort.
    private int userId;

    // Constructor: hiermee maak ik een FitTrackItem object aan.
    public FitTrackItem(int id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    // Getter: hiermee haal ik het id op.
    public int getId() {
        return id;
    }

    // Getter: hiermee haal ik de userId op.
    public int getUserId() {
        return userId;
    }

    // Setter: hiermee kan ik het id later aanpassen, bijvoorbeeld na een insert in de database.
    public void setId(int id) {
        this.id = id;
    }
}