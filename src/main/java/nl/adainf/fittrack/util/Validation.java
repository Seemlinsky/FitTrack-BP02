package nl.adainf.fittrack.util;



/*
 * Validation.java - simpele hulpfuncties om invoer te checken
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

public class Validation {

    // private constructor: we willen deze class niet "new"-en (alleen static methods)
    private Validation() {}

    // Check: is de string null of alleen maar spaties?
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Probeert een positief getal te lezen uit tekst.
    // Lukt het niet (of <= 0), dan geven we fallback terug.
    public static int parsePositiveInt(String s, int fallback) {
        try {
            int v = Integer.parseInt(s.trim());
            return v > 0 ? v : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }
}
