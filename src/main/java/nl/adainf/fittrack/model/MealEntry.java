package nl.adainf.fittrack.model;

/*
 * MealEntry.java - Model/Entity: 1 object = 1 rij uit de database
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - geen SQL hier, dat zit in de DAO
 * - deze klasse gebruikt overerving via FitTrackItem
 */

import java.time.LocalDate;
import java.time.LocalTime;

// Dit is een modelklasse voor een maaltijd.
// MealEntry erft id en userId van FitTrackItem.
public class MealEntry extends FitTrackItem {
    // Velden die alleen bij MealEntry horen
    private LocalDate mealDate;
    private LocalTime mealTime;
    private String mealType;
    private String mealName;
    private int calories;

    public MealEntry(int id, int userId, LocalDate mealDate, LocalTime mealTime,
                     String mealType, String mealName, int calories) {
        super(id, userId);
        this.mealDate = mealDate;
        this.mealTime = mealTime;
        this.mealType = mealType;
        this.mealName = mealName;
        this.calories = calories;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public LocalTime getMealTime() {
        return mealTime;
    }

    public String getMealType() {
        return mealType;
    }

    public String getMealName() {
        return mealName;
    }

    public int getCalories() {
        return calories;
    }

    public void setId(int id) {
        super.setId(id);
    }

    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }

    public void setMealTime(LocalTime mealTime) {
        this.mealTime = mealTime;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return mealTime + " - " + mealType + " - " + mealName + " (" + calories + " kcal)";
    }
}