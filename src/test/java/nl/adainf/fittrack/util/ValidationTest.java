package nl.adainf.fittrack.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * ValidationTest.java
 *
 * In dit bestand test ik de Validation class.
 *
 * Met deze unit tests controleer ik kleine stukjes code apart.
 * Zo weet ik of isBlank() en parsePositiveInt() goed werken.
 */

class ValidationTest {

    @Test
    void isBlankGeeftTrueBijLegeTekst() {
        assertTrue(Validation.isBlank(""));
    }

    @Test
    void isBlankGeeftTrueBijAlleenSpaties() {
        assertTrue(Validation.isBlank("   "));
    }

    @Test
    void isBlankGeeftTrueBijNull() {
        assertTrue(Validation.isBlank(null));
    }

    @Test
    void isBlankGeeftFalseBijNormaleTekst() {
        assertFalse(Validation.isBlank("Stefan"));
    }

    @Test
    void parsePositiveIntGeeftGetalTerugBijGeldigeInvoer() {
        assertEquals(30, Validation.parsePositiveInt("30", 1));
    }

    @Test
    void parsePositiveIntGeeftFallbackBijNegatiefGetal() {
        assertEquals(1, Validation.parsePositiveInt("-5", 1));
    }

    @Test
    void parsePositiveIntGeeftFallbackBijTekst() {
        assertEquals(1, Validation.parsePositiveInt("abc", 1));
    }
}