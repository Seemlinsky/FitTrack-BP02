package nl.adainf.fittrack.model;



/*
 * Workout.java - Model/Entity: 1 object = 1 rij uit de database
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import java.time.LocalDate;

// Dit is een "plain" model: velden + constructor + getters/setters.
// Geen SQL hier, dat zit in de DAO.
public class Workout {
    // Velden (kolommen uit de DB)
    private int id;
    private int userId;
    private LocalDate workoutDate;
    private String note;

    public Workout(int id, int userId, LocalDate workoutDate, String note) {
        this.id = id;
        this.userId = userId;
        this.workoutDate = workoutDate;
        this.note = note;
    }

    // voor insert (id bestaat nog niet)
    public Workout(int userId, LocalDate workoutDate, String note) {
        this(0, userId, workoutDate, note);
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDate getWorkoutDate() { return workoutDate; }
    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }
}
