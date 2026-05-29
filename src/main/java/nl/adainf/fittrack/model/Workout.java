package nl.adainf.fittrack.model;

/*
 * Workout.java - Model/Entity: 1 object = 1 rij uit de database
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - geen SQL hier, dat zit in de DAO
 * - deze klasse gebruikt overerving via FitTrackItem
 */

import java.time.LocalDate;

// Dit is een modelklasse voor een workout.
// Workout erft id en userId van FitTrackItem.
public class Workout extends FitTrackItem {
    // Velden die alleen bij Workout horen
    private LocalDate workoutDate;
    private String note;

    public Workout(int id, int userId, LocalDate workoutDate, String note) {
        super(id, userId);
        this.workoutDate = workoutDate;
        this.note = note;
    }

    // Voor insert: id bestaat nog niet, daarom is id eerst 0
    public Workout(int userId, LocalDate workoutDate, String note) {
        this(0, userId, workoutDate, note);
    }

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}