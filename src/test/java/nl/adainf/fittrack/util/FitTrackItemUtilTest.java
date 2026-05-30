package nl.adainf.fittrack.util;

import nl.adainf.fittrack.model.MealEntry;
import nl.adainf.fittrack.model.Workout;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * FitTrackItemUtilTest.java
 *
 * In dit bestand test ik de FitTrackItemUtil class.
 *
 * Deze tests horen bij polymorfie en instanceof.
 * Workout en MealEntry erven allebei van FitTrackItem.
 * Met deze tests controleer ik of getItemType() het juiste type teruggeeft.
 */

class FitTrackItemUtilTest {

    @Test
    void getItemTypeGeeftWorkoutTerugBijWorkoutObject() {
        Workout workout = new Workout(1, 1, LocalDate.now(), "Test workout");

        assertEquals("Workout", FitTrackItemUtil.getItemType(workout));
    }

    @Test
    void getItemTypeGeeftMealEntryTerugBijMealEntryObject() {
        MealEntry meal = new MealEntry(
                1,
                1,
                LocalDate.now(),
                LocalTime.now(),
                "Lunch",
                "Broodje kip",
                450
        );

        assertEquals("MealEntry", FitTrackItemUtil.getItemType(meal));
    }
}