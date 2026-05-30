package nl.adainf.fittrack.model;

/*
 * MealEntry.java
 *
 * Dit is een modelklasse voor één maaltijd.
 *
 * Een maaltijd hoort bij een gebruiker en heeft:
 * - een datum
 * - een tijd
 * - een type maaltijd
 * - een naam
 * - calorieën
 *
 * Deze klasse erft van FitTrackItem.
 * Daardoor krijgt MealEntry de velden id en userId van FitTrackItem.
 *
 * Hier staat geen SQL-code.
 * SQL staat in MealEntryDao.
 */

import java.time.LocalDate;
import java.time.LocalTime;

public class MealEntry extends FitTrackItem {

    // Datum van de maaltijd.
    private LocalDate mealDate;

    // Tijd van de maaltijd.
    private LocalTime mealTime;

    // Type maaltijd, bijvoorbeeld Breakfast, Lunch, Dinner of Snack.
    private String mealType;

    // Naam van de maaltijd, bijvoorbeeld banana of pasta.
    private String mealName;

    // Aantal calorieën van de maaltijd.
    private int calories;

    // Constructor: hiermee maak ik een MealEntry object aan.
    public MealEntry(int id, int userId, LocalDate mealDate, LocalTime mealTime,
                     String mealType, String mealName, int calories) {
        super(id, userId);
        this.mealDate = mealDate;
        this.mealTime = mealTime;
        this.mealType = mealType;
        this.mealName = mealName;
        this.calories = calories;
    }

    // Getter: hiermee haal ik de datum van de maaltijd op.
    public LocalDate getMealDate() {
        return mealDate;
    }

    // Getter: hiermee haal ik de tijd van de maaltijd op.
    public LocalTime getMealTime() {
        return mealTime;
    }

    // Getter: hiermee haal ik het type maaltijd op.
    public String getMealType() {
        return mealType;
    }

    // Getter: hiermee haal ik de naam van de maaltijd op.
    public String getMealName() {
        return mealName;
    }

    // Getter: hiermee haal ik de calorieën op.
    public int getCalories() {
        return calories;
    }

    // Setter: hiermee kan ik het id later aanpassen.
    public void setId(int id) {
        super.setId(id);
    }

    // Setter: hiermee kan ik de datum aanpassen.
    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }

    // Setter: hiermee kan ik de tijd aanpassen.
    public void setMealTime(LocalTime mealTime) {
        this.mealTime = mealTime;
    }

    // Setter: hiermee kan ik het type maaltijd aanpassen.
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    // Setter: hiermee kan ik de naam van de maaltijd aanpassen.
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    // Setter: hiermee kan ik de calorieën aanpassen.
    public void setCalories(int calories) {
        this.calories = calories;
    }

    // Dit zorgt ervoor dat een maaltijd netjes zichtbaar is in de ListView.
    @Override
    public String toString() {
        return mealTime + " - " + mealType + " - " + mealName + " (" + calories + " kcal)";
    }
}