package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    @Test
    public void testSeedZeroBattle() {
        // Values: [60 48 29 47 15 53 91 61]
        // Test: True if value is < 50
        Game game = new Game(0);
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertFalse(game.battle());
        assertFalse(game.battle());
    }

    @Test
    public void testSeedFourBattle() {
        // Values: [62 52 3 58 67 5 11 46]
        // Test: True if value is < 50
        Game game = new Game(4);
        assertFalse(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
    }

    @Test
    public void testSeedNegativeFourBattle() {
        // Values: [39 13 98 5 43 89 20 23]
        // Test: True if value is < 50
        Game game = new Game(-4);
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
        assertFalse(game.battle());
        assertTrue(game.battle());
        assertTrue(game.battle());
    }
}