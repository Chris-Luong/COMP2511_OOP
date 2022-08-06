package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import dungeonmania.entities.*;
import dungeonmania.entities.Character;

import org.junit.jupiter.api.Test;

import dungeonmania.entities.Entity;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonManiaControllerTest {

    @Test 
    public void testValidNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.newGame("boulders", "peaceful"));
    }

    @Test 
    public void testLongGameNameNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.newGame("boulders", "peaceful"));
        assertDoesNotThrow(() -> dmc.newGame("advanced", "standard"));
        assertDoesNotThrow(() -> dmc.newGame("maze", "hard"));
    }

    // TODO: can put both tests below in same test because they check similar things
    @Test
    public void testNoNameNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("", "peaceful"));
    }

    @Test
    public void testEmptyNameNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame(" ", "peaceful"));
    }

    @Test
    public void testDuplicateNameNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.newGame("maze", "peaceful"));
        assertDoesNotThrow(() -> dmc.newGame("maze", "peaceful"));
    }

    @Test 
    public void testInvalidGameModeNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("maze", "Super - Extreme"));
    }

    @Test
    public void testInvalidNameLoadGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("maze", "peaceful");
        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("notTheRightOne"));
    }

    @Test
    public void testLoadGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("maze", "peaceful");
        dmc.saveGame("maze_save");
        assertDoesNotThrow(() -> dmc.loadGame("maze_save"));
    }

    @Test
    public void testCharacterMovement() {
        DungeonManiaController dmc = new DungeonManiaController();      
        dmc.newGame("maze", "peaceful");

        //assertDoesNotThrow(() -> dmc.newGame("dungeonOne", "peaceful"));
        assertDoesNotThrow(() -> dmc.newGame("advanced", "peaceful"));
        dmc.saveGame("maze_save2");
        assertDoesNotThrow(() -> dmc.loadGame("maze_save2"));

        Character character = dmc.getCharacter();
        assertEquals(new Position(1, 1), character.getPosition());
        assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        
        //character.move(Direction.UP);   // TODO : THIS IS NOT GOOD : ONLY USE INTERACT()

        assertEquals(new Position(1, 2), character.getPosition());

        //character.move(Direction.RIGHT);
        // cannot move right as there is a wall, position unchanged
        assertDoesNotThrow(() -> dmc.tick(null, Direction.RIGHT));
        //assertEquals(new Position(1, 2), character.getPosition());

    }
    
    

}