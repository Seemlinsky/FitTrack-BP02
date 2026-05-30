package nl.adainf.fittrack.util;

import nl.adainf.fittrack.model.FitTrackItem;
import nl.adainf.fittrack.model.MealEntry;
import nl.adainf.fittrack.model.Workout;

/*
 * Hulpfunctie voor FitTrackItem objecten.
 *
 * Dit gebruik ik om polymorfie en instanceof uit de reader te laten zien.
 * Workout en MealEntry erven allebei van FitTrackItem.
 * Met instanceof controleer ik welk specifiek type het object is.
 */
public class FitTrackItemUtil {

    private FitTrackItemUtil() {
        // Deze class heeft alleen static methods.
    }

    public static String getItemType(FitTrackItem item) {
        if (item instanceof Workout) {
            return "Workout";
        }

        if (item instanceof MealEntry) {
            return "MealEntry";
        }

        return "Onbekend";
    }
}