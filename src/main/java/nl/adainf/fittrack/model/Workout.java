package nl.adainf.fittrack.model;

/*
 * Workout.java
 *
 * Dit is een modelklasse voor een workout.
 *
 * Een workout hoort bij een gebruiker en een datum.
 * De workout kan ook een korte notitie hebben.
 *
 * Deze klasse erft van FitTrackItem.
 * Daardoor krijgt Workout de velden id en userId van FitTrackItem.
 *
 * Hier staat geen SQL-code.
 * SQL staat in WorkoutDao.
 */

import java.time.LocalDate;

public class Workout extends FitTrackItem {

    // De datum waarop de workout is gedaan.
    private LocalDate workoutDate;

    // Korte notitie bij de workout. Dit mag ook leeg zijn.
    private String note;

    // Constructor voor een workout die al een id heeft, bijvoorbeeld uit de database.
    public Workout(int id, int userId, LocalDate workoutDate, String note) {
        super(id, userId);
        this.workoutDate = workoutDate;
        this.note = note;
    }

    // Constructor voor een nieuwe workout.
    // Het id is dan nog 0, omdat de database het echte id later maakt.
    public Workout(int userId, LocalDate workoutDate, String note) {
        this(0, userId, workoutDate, note);
    }

    // Getter: hiermee haal ik de datum van de workout op.
    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    // Getter: hiermee haal ik de notitie van de workout op.
    public String getNote() {
        return note;
    }

    // Setter: hiermee kan ik de notitie aanpassen.
    public void setNote(String note) {
        this.note = note;
    }
}