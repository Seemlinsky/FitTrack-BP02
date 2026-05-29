package nl.adainf.fittrack.model;

/*
 * Algemene klasse voor FitTrack onderdelen.
 *
 * Deze klasse gebruik ik voor overerving volgens de JavaFX-reader.
 * Workout en MealEntry hebben allebei een id en userId.
 * Daarom staan die gedeelde velden hier op één plek.
 */
public class FitTrackItem {
    private int id;
    private int userId;

    public FitTrackItem(int id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }
}