package nl.adainf.fittrack.util;

/*
 * Validation.java
 *
 * Dit bestand gebruik ik voor simpele invoercontroles.
 *
 * In de schermen komt invoer vaak binnen als tekst.
 * Soms moet ik controleren of tekst leeg is.
 * Soms moet ik tekst omzetten naar een positief getal.
 *
 * Door dit in een aparte class te zetten,
 * hoef ik dezelfde controle niet steeds opnieuw te schrijven.
 */

public class Validation {

    // Deze constructor is private omdat ik deze class niet als object hoef te maken.
    // Ik gebruik alleen de static methods hieronder.
    private Validation() {
    }

    // Deze methode checkt of tekst leeg is.
    // Null en alleen spaties tel ik ook als leeg.
    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    // Deze methode probeert tekst om te zetten naar een positief getal.
    // Als dat niet lukt, of als het getal 0 of lager is, geef ik fallback terug.
    public static int parsePositiveInt(String text, int fallback) {
        try {
            int value = Integer.parseInt(text.trim());
            return value > 0 ? value : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }
}