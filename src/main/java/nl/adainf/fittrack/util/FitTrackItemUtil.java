package nl.adainf.fittrack.util;

import nl.adainf.fittrack.model.FitTrackItem;
import nl.adainf.fittrack.model.MealEntry;
import nl.adainf.fittrack.model.Workout;

/*
 * FitTrackItemUtil.java
 *
 * Dit is een hulpklasse voor FitTrackItem objecten.
 *
 * Workout en MealEntry erven allebei van FitTrackItem.
 * Daardoor kan ik ze allebei behandelen als een algemeen FitTrackItem object.
 *
 * Met instanceof controleer ik daarna welk soort object het echt is.
 * Dit gebruik ik om polymorfie uit de reader te laten zien.
 */

public class FitTrackItemUtil {

    // Deze constructor is private omdat ik deze class niet als object hoef te maken.
    // Ik gebruik alleen de static method hieronder.
    private FitTrackItemUtil() {
    }

    // Deze methode geeft terug welk soort FitTrackItem object het is.
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