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
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.NullConversion;


import dungeonmania.TestHelpers;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class LevelTest {
    @Test
    public void testBeatMaze() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("maze", "peaceful");
        nearlyCompleteMaze(dmc);
        assertDoesNotThrow(() -> dmc.tick(null, Direction.DOWN));
        System.out.print(dmc.getCharacter().getPosition()); // will not move to exit, it is one tile above
    }

    public void nearlyCompleteMaze(DungeonManiaController dmc) {
        // Move to exit
        dmc.tick(null, Direction.DOWN); // 2 Down
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.RIGHT); // 5 Right
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.DOWN); // Zig zag Down and Right twice
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.DOWN); // 6 Down
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.RIGHT); // 2 Right
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.DOWN); // 5 Down
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.RIGHT); // 4 Right
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.UP); // 10 Down
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.UP);
        dmc.tick(null, Direction.RIGHT); // 4 Right
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.RIGHT);
        dmc.tick(null, Direction.DOWN); // 10 Down
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        dmc.tick(null, Direction.DOWN);
        // dmc.tick(null, Direction.DOWN); uncomment to complete maze.json
    }

    // CHECKING GOALS : ==================================================================================================
    @Test
    public void testExitGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("maze", "peaceful");
        String goal = ":exit";
        assertEquals(goal, dr.getGoals());
    }

    @Test
    public void testBoulderGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("boulders", "peaceful");
        String goal = ":boulders";
        assertEquals(goal, dr.getGoals());
    }

    @Test
    public void testEnemiesGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("monsters", "peaceful");
        String goal = ":enemies";
        assertEquals(goal, dr.getGoals());
    }

    @Test
    public void testTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("many_coins", "peaceful");
        String goal = ":treasure";
        assertEquals(goal, dr.getGoals());
    }

    @Test
    public void testConjunctionGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("advanced-2", "peaceful");
        String goal = ":enemies AND :treasure";
        assertEquals(goal, dr.getGoals());
    }

    // CHECKING INVENTORY: ==================================================================================================
    @Test void randomArmourdrop() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("advanced-2", "peaceful");
        List <ItemResponse> ir = Arrays.asList(new ItemResponse("43", "treasure"));
        List <ItemResponse> inventory = dr.getInventory();

        assertTrue(inventory.size() == 0);
        
        // move 10 times to the right
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(null, Direction.RIGHT);
            
        }

        inventory = dr.getInventory();
        // we happen to collect a sword on the way
        assertTrue(inventory.size() == 1);

        // move 10 times down to collect 1 wood
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(null, Direction.DOWN);
        }

        // check that we collected the 1 wood, and that we cannot build anything yet
        inventory = dr.getInventory();

        // because of the chance of dropping armour...
        assertTrue(inventory.size() == 2 || inventory.size() == 3);

        // because armour may drop, the last item is either the armour dropped if there is an extra item in inventory
        // or it is the wood.
        if (inventory.size() == 2) {

            ItemResponse wood = new ItemResponse("82", "wood");
            assertEquals(inventory.get(1).getType(), wood.getType());
            assertEquals(inventory.get(1).getId(), wood.getId());
        } else if (inventory.size() == 3) {
            ItemResponse armour = new ItemResponse("82", "armour");
            assertEquals(inventory.get(1).getType(), armour.getType());
            assertEquals(inventory.get(2).getId(), armour.getId());
        }


    }
    
    @Test
    public void testEmptyInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("advanced-2", "peaceful");
        List<String> inventory = Arrays.asList();
        assertEquals(inventory, dr.getInventory());
        assertTrue(inventory.size() == 0);
    }

    @Test
    public void testTreasureInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("many_coins", "peaceful");
        List <ItemResponse> ir = Arrays.asList(new ItemResponse("43", "treasure"));
        List <ItemResponse> inventory = dr.getInventory();


        assertTrue(inventory.size() == 0);
        
        // collect 1 coin
        dr = dmc.tick(null, Direction.RIGHT);
        inventory = dr.getInventory();
        assertEquals(ir.get(0).getType(), inventory.get(0).getType());
        assertEquals(ir.get(0).getId(), inventory.get(0).getId());
        assertTrue(inventory.size() == 1);
        
        // no coin collected, assert size of inventory didn't change
        dr = dmc.tick(null, Direction.RIGHT);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 1);

        // coin collected here
        dr = dmc.tick(null, Direction.DOWN);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 2);

        ir = Arrays.asList(new ItemResponse("43", "treasure"), new ItemResponse("44", "treasure"));
        assertEquals(ir.get(1).getType(), inventory.get(1).getType());
        assertEquals(ir.get(1).getId(), inventory.get(1).getId());

        // another coin collected here
        dr = dmc.tick(null, Direction.DOWN);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 3);
        ir = Arrays.asList(new ItemResponse("43", "treasure"), new ItemResponse("44", "treasure"), new ItemResponse("45", "treasure"));
        assertEquals(ir.get(2).getType(), inventory.get(2).getType());
        assertEquals(ir.get(2).getId(), inventory.get(2).getId());
        
    }

    // CHECKING BUILDING: ==================================================================================================

    @Test
    public void testBuildBow() {
            DungeonManiaController dmc = new DungeonManiaController();
            DungeonResponse dr = dmc.newGame("advanced-2", "peaceful");
            List <ItemResponse> inventory = dr.getInventory();
    
            assertTrue(inventory.size() == 0);
            
            // move 10 times to the right
            for (int i = 0; i < 10; i++) {
                dr = dmc.tick(null, Direction.RIGHT);
                
            }
    
            inventory = dr.getInventory();
            // we happen to collect a sword on the way
            assertTrue(inventory.size() == 1);
    
            // move 10 times down to collect 1 wood
            for (int i = 0; i < 10; i++) {
                dr = dmc.tick(null, Direction.DOWN);
            }
    
            // check that we collected the 1 wood, and that we cannot build anything yet
            inventory = dr.getInventory();
    
            // because of the chance of dropping armour...
            // but the sword get consumed
            assertTrue(inventory.size() == 1 || inventory.size() == 2);
    
            List<String> buildables = Arrays.asList();
            assertEquals(dr.getBuildables(), buildables);
    
            // collect an additional wood but it still isn't enough to build anything
            dr = dmc.tick(null, Direction.DOWN);
            inventory = dr.getInventory();
            assertTrue(inventory.size() == 3 || inventory.size() == 2);
            assertEquals(dr.getBuildables(), buildables);
        
            // collect 2 arrows but it still isn't enough to build anything
            dr = dmc.tick(null, Direction.DOWN);
            dr = dmc.tick(null, Direction.DOWN);
            inventory = dr.getInventory();
            assertTrue(inventory.size() == 5 || inventory.size() == 4);
            assertEquals(dr.getBuildables(), buildables);
    
            dr = dmc.tick(null, Direction.RIGHT);
            inventory = dr.getInventory();
            assertTrue(inventory.size() == 6 || inventory.size() == 5);
            buildables = Arrays.asList("bow");
            assertEquals(dr.getBuildables(), buildables);
    
            // try building bow here:
            dr = dmc.build("bow");
            inventory = dr.getInventory();
            // check inventory size gets updated
            assertTrue(inventory.size() == 3 || inventory.size() == 4);
    
            
            // we know that the bow built will be the last item created and inside inventory
            if (inventory.size() == 4) {
                ItemResponse bow = new ItemResponse("130", "bow");
                assertEquals(inventory.get(3).getType(), bow.getType());
                assertEquals(inventory.get(3).getId(), bow.getId());
            } else if (inventory.size() == 3) {
                ItemResponse bow = new ItemResponse("130", "bow");
                assertEquals(inventory.get(2).getType(), bow.getType());
                assertEquals(inventory.get(2).getId(), bow.getId());
            }
            
    }
        
    @Test
    public void testBuildShield() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("advanced-2", "peaceful");
        List <ItemResponse> inventory = dr.getInventory();

        assertTrue(inventory.size() == 0);
        
        // move 10 times to the right
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(null, Direction.RIGHT);
            
        }

        inventory = dr.getInventory();
        // we happen to collect a sword on the way
        assertTrue(inventory.size() == 1);

        // move 10 times down to collect 1 wood
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(null, Direction.DOWN);
        }

        // check that we collected the 1 wood, and that we cannot build anything yet
        inventory = dr.getInventory();

        // because of the chance of dropping armour...
        assertTrue(inventory.size() == 2 || inventory.size() == 1);
        List<String> buildables = Arrays.asList();
        assertEquals(dr.getBuildables(), buildables);

        // collect an additional wood but it still isn't enough to build anything
        dr = dmc.tick(null, Direction.DOWN);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 3 || inventory.size() == 2);
        assertEquals(dr.getBuildables(), buildables);
    
        // collect 2 arrows but it still isn't enough to build anything
        dr = dmc.tick(null, Direction.DOWN);
        dr = dmc.tick(null, Direction.DOWN);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 5 || inventory.size() == 4);
        assertEquals(dr.getBuildables(), buildables);

        // Should be able to build bow there
        dr = dmc.tick(null, Direction.RIGHT);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 6 || inventory.size() == 5);
        buildables = Arrays.asList("bow");
        assertEquals(dr.getBuildables(), buildables);

        // collect wood
        dr = dmc.tick(null, Direction.RIGHT);
        dr = dmc.tick(null, Direction.LEFT);
        dr = dmc.tick(null, Direction.LEFT);

        // move 10 times down to collect 1 wood
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(null, Direction.UP);
        }

        // finally once to the left to collect treasure
        dr = dmc.tick(null, Direction.LEFT);
        buildables = Arrays.asList("bow", "shield");
        assertEquals(dr.getBuildables(), buildables);
        inventory = dr.getInventory();
        assertTrue(inventory.size() == 8 || inventory.size() == 7);
        
        // try building shield here:
        dr = dmc.build("shield");
        inventory = dr.getInventory();
        // check inventory size gets updated
        assertTrue(inventory.size() == 7 || inventory.size() == 6);


        // because armour may drop, the built item remains the last created and added to inventory
        if (inventory.size() == 7) {

            ItemResponse shield = new ItemResponse("132", "shield");
            assertEquals(inventory.get(6).getType(), shield.getType());
            assertEquals(inventory.get(6).getId(), shield.getId());
        } else if (inventory.size() == 6) {
            ItemResponse shield = new ItemResponse("131", "shield");
            assertEquals(inventory.get(5).getType(), shield.getType());
            assertEquals(inventory.get(5).getId(), shield.getId());
        }
        
    }

    // CHECKING FIGHTING: ==================================================================================================
    @Test
    public void testDeadMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("advanced", "standard");
        List<EntityResponse> entities = dr.getEntities();

        // get character
        EntityResponse chara = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }
        }
        // make sure we hold the character
        //assertEquals("character 1", chara.getId());
        assertEquals(new Position(1,1,1), chara.getPosition());
        assertEquals("player", chara.getType());

        // get mercenary
        EntityResponse mercenary = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("mercenary")) {
                mercenary = e;
                break;
            }
        }

        // make sure that the mercenary is how we intend them to be
        assertEquals(new Position(3,5,0), mercenary.getPosition());
        assertEquals("mercenary", mercenary.getType());

        // tick game twice down, character and mercenary should NOT have fought yet, therefore a mercenary should still exist
        dr = dmc.tick(null, Direction.DOWN);
        dr = dmc.tick(null, Direction.DOWN);
        entities = dr.getEntities();
       
        // get mercenary again
        mercenary = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("mercenary")) {
                mercenary = e;
                break;
            }
        }

        // make sure that the mercenary is how we intend them to be, with new position
        assertEquals(new Position(1,5,1), mercenary.getPosition());
        assertEquals("mercenary", mercenary.getType());


        dr = dmc.tick(null, Direction.DOWN);
        entities = dr.getEntities();
       
        // get mercenary again, but this time they should be dead, i.e non-existent, therefore null
        mercenary = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("mercenary")) {
                mercenary = e;
                break;
            }
        }

        assertEquals(mercenary, null);


    }

    @Test
    public void testDeadSpider() {

    }

    @Test
    public void testDeadZombie() {

    }

    @Test
    public void testDeadPlayer() {

    }

    // CHECKING INTERACT: ==================================================================================================
    @Test
    public void testInteractHPPotion() {

    }

    @Test
    public void testInteractInvincibility() {

    }

    @Test
    public void testInteractInvisibility() {

    }

    @Test
    public void testInteractZombieToastSpawnerInteract() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("zombie_spawner", "standard");
        List<EntityResponse> entities = dr.getEntities();
        List <ItemResponse> inventory = dr.getInventory();
        EntityResponse chara = null;
        EntityResponse zombieSpawner = null;

        for (EntityResponse e : entities) {
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }
        }

        for (EntityResponse e : entities) {
            if (e.getType().equals("zombie_toast_spawner")) {
                zombieSpawner = e;
                break;
            }
        }

        assertTrue(chara != null);
        assertTrue(zombieSpawner != null);
        assertEquals(zombieSpawner.getPosition(), new Position(11,10,0));
        assertEquals(zombieSpawner.getId(), "76");

        // move 10 times to the right
        for (int i = 0; i < 10; i++) {
            dr = dmc.tick(null, Direction.RIGHT);
            
        }

        inventory = dr.getInventory();
        
        // we have the sword
        assertTrue(inventory.size() == 1);

        // move 10 times down
        for (int i = 0; i < 9; i++) {
            dr = dmc.tick(null, Direction.DOWN);
        }


        inventory = dr.getInventory();
        
        dr = dmc.interact("76");

        inventory = dr.getInventory();
        // we have the sword
        assertTrue(inventory.size() == 1);

        entities = dr.getEntities();
        zombieSpawner = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("zombie_toast_spawner")) {
                zombieSpawner = e;
                break;
            }
        }

        assertTrue(zombieSpawner == null);

    }

    // CHECKING MONSTER MOVEMENT : =========================================================================================
    // CHECKING BOULDER MOVEMENT : =========================================================================================

    @Test
    public void testSimpleBoulderMove() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("boulders", "standard");
        List<EntityResponse> entities = dr.getEntities();

        EntityResponse chara = null;
        EntityResponse basicBoulder = null;

        for (EntityResponse e : entities) {
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }
        }

        assertTrue(chara != null);
        assertTrue(chara.getType().equals("player"));
        assertEquals(chara.getPosition(), new Position(2,2,2));

        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(3,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        assertTrue(basicBoulder != null);
        // we use the ID to identify the specific boulder
        assertEquals(basicBoulder.getId(), "43");


        dr = dmc.tick(null, Direction.RIGHT);

        entities = dr.getEntities();
        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(4,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        // using the ID from before, we confirm that bouldre 4,2,0 is the same boulder as before at 3,2,0
        assertEquals(basicBoulder.getId(), "43");


    }

    @Test
    public void testCannotMoveBoulderWall() {
        // repeat the previous test but this time try to move the boulder an additional
        // time to the right.

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("boulders", "standard");
        List<EntityResponse> entities = dr.getEntities();

        EntityResponse chara = null;
        EntityResponse basicBoulder = null;

        for (EntityResponse e : entities) {
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }
        }

        assertTrue(chara != null);
        assertTrue(chara.getType().equals("player"));
        assertEquals(chara.getPosition(), new Position(2,2,2));

        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(3,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        assertTrue(basicBoulder != null);
        // we use the ID to identify the specific boulder
        assertEquals(basicBoulder.getId(), "43");


        dr = dmc.tick(null, Direction.RIGHT);

        entities = dr.getEntities();
        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(4,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        // using the ID from before, we confirm that bouldre 4,2,0 is the same boulder as before at 3,2,0
        assertEquals(basicBoulder.getId(), "43");

        // TRY TO MOVE BOULDER TO THE RIGHT ONCE AGAIN:
        // REPEAT CODE AND SEE THAT THE POSITION REMAINS THE SAME :
        dr = dmc.tick(null, Direction.RIGHT);

        entities = dr.getEntities();
        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(4,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        // the boulder is the same
        assertEquals(basicBoulder.getId(), "43");



    }

    @Test
    public void testCannotMoveBoulderBoulder() {
        // repeat code from testSimpleBoulderMove
        // then we try to move above that boulder (1 tile above it)
        // and try to push it down, where another boulder is located.

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("boulders", "standard");
        List<EntityResponse> entities = dr.getEntities();

        EntityResponse chara = null;
        EntityResponse basicBoulder = null;

        for (EntityResponse e : entities) {
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }
        }

        assertTrue(chara != null);
        assertTrue(chara.getType().equals("player"));
        assertEquals(chara.getPosition(), new Position(2,2,2));

        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(3,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        assertTrue(basicBoulder != null);
        // we use the ID to identify the specific boulder
        assertEquals(basicBoulder.getId(), "43");


        dr = dmc.tick(null, Direction.RIGHT);

        entities = dr.getEntities();
        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(4,2,0))) {
                basicBoulder = e;
                break;
            }
        }

        // using the ID from before, we confirm that bouldre 4,2,0 is the same boulder as before at 3,2,0
        assertEquals(basicBoulder.getId(), "43");

        // FIND THE FUTURE BLOCKING BOULDER (AT 4,3,0)
        EntityResponse blockingBoulder = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("boulder") && e.getPosition().equals(new Position(4,3,0))) {
                blockingBoulder = e;
                break;
            }
        }

        assertTrue(blockingBoulder != null);
        assertEquals(blockingBoulder.getId(), "44");

        dr = dmc.tick(null, Direction.UP);
        dr = dmc.tick(null, Direction.RIGHT);
        entities = dr.getEntities();
        for (EntityResponse e : entities) {
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }
        }

        // confirm line-up of character, basic boulder and blocking boulder:
        assertEquals(chara.getPosition(), new Position(4,1,0));
        assertEquals(basicBoulder.getPosition(), new Position(4,2,0));
        assertEquals(blockingBoulder.getPosition(), new Position(4,3,0));

        dr = dmc.tick(null, Direction.DOWN);
        entities = dr.getEntities();
        // assert that no one and nothing could move
        for (EntityResponse e : entities) {
            
            if (e.getType().equals("player")) {
                chara = e;
                break;
            }

            else if (e.getType().equals("boulder") && e.getId() == "43") {
                basicBoulder = e;
            }

            else if (e.getType().equals("boulder") && e.getId() == "44") {
                blockingBoulder = e;
            }

        }


        // assert that no one and nothing could move
        assertEquals(chara.getPosition(), new Position(4,1,0));
        assertEquals(basicBoulder.getPosition(), new Position(4,2,0));
        assertEquals(blockingBoulder.getPosition(), new Position(4,3,0));



    }

    // CHECKING WIN / LOSING GAME: =========================================================================================
    // CHECKING MONSTER: ===================================================================================================

    @Test
    public void testSpiderSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("advanced", "standard");
        List<EntityResponse> entities = dr.getEntities();

        // see if a spider spawns by default
        EntityResponse spider = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("spider")) {
                spider = e;
                break;
            }
        }

        // checl that spiders are not already in the level by default
        assertEquals(spider, null);

        entities = dr.getEntities();
        // tick 29 times to the right
        for (int i = 0; i < 29; i++) {
            dr = dmc.tick(null, Direction.UP); 
        }
        assertTrue(spider == null);

        // tick an extra time for the 30th count
        dr = dmc.tick(null, Direction.UP); 
        entities = dr.getEntities();


        for (EntityResponse e : entities) {
            if (e.getType().equals("spider")) {
                spider = e;
                break;
            }
        }

        // check that spiders are not already in the level by default
        assertTrue(spider != null);
        assertEquals(spider.getPosition(), new Position(2,2,100));
    }

    @Test
    public void testZombieSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr = dmc.newGame("zombie_spawner", "standard");
        List<EntityResponse> entities = dr.getEntities();

        // see if a zombie spawns by default
        EntityResponse zombie = null;
        for (EntityResponse e : entities) {
            if (e.getType().equals("spider")) {
                zombie = e;
                break;
            }
        }

        assertEquals(zombie, null);


    }

}
