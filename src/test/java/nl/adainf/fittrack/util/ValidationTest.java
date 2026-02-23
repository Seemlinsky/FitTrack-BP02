package nl.adainf.fittrack.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    void isBlankWorks() {
        assertTrue(Validation.isBlank(null));
        assertTrue(Validation.isBlank(""));
        assertTrue(Validation.isBlank("   "));
        assertFalse(Validation.isBlank("x"));
    }

    @Test
    void parsePositiveIntWorks() {
        assertEquals(10, Validation.parsePositiveInt("10", -1));
        assertEquals(-1, Validation.parsePositiveInt("0", -1));
        assertEquals(-1, Validation.parsePositiveInt("-5", -1));
        assertEquals(7, Validation.parsePositiveInt("abc", 7));
    }
}
