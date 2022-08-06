package dungeonmania;

import java.util.List;
import dungeonmania.*;
import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class DungeonGame {
    // dungeon Ids will be assigned as "d-" + the dungeon's order
    // i.e. 1st dungeon is "d-1", 2nd dungeon is "d-2", etc.
    private String id;
    // name of dungeonGame file, used to load game
    private String name;
    private String gameMode;
    private boolean beaten;
    private Level currentLevel;
    ArrayList<String> levelNames = new ArrayList<String>(Arrays.asList("advanced-2", "advanced", "boulders", "custom_portals", "maze", "many_coins", "potions", "portals", "single_boulder", "zombie_spawner")); 
    private List<String> levels = new ArrayList<String>();

    // inventory is of type entity and not collectable nor buildable because we want to combine them both in here.

    // Dev note: because we do not want level to touch dungeonGame but the latter to touch level:
    // have inventory in both, at end of level add everything in inventory in level to dungeonGame inventory
    // and add dungeonGame inventory to the new level when it is created. 


    private List<Entity> inventory = new ArrayList<Entity>();

    // called in DMC at newGame()
    public DungeonGame(String dungeonName, String gameMode) {
        this.id = "d-1";
        this.name = dungeonName;
        this.gameMode = gameMode;
        this.beaten = false;
        // hardcoded below:
        Level newLvl = new Level(dungeonName, inventory, gameMode, 0);
        setCurrentLevel(newLvl);
        levels.add(newLvl.toString());
    }
    
    /**
     * 
     * ------------------------- FOR LOADGAME ---------------------------------
     * 
     * @param dungeonName
     */
    public DungeonGame(String dungeonName) {
        //this.id = "d-1";
        this.name = dungeonName;
        Level newLevel = new Level(dungeonName);
        this.gameMode = newLevel.getGameMode();
        this.levels = newLevel.getLevels();
        setCurrentLevel(newLevel);
    }

    public String getId() {
        return id;
    }

    public String getDungeonName() {
        return name;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInventory(List<Entity> newItems) {

        this.inventory = newItems;

    }

    public List<EntityResponse> getDungeonEntities() {

        List<EntityResponse> allEntityInfoRes = new ArrayList<EntityResponse>();

        //System.out.println("\t Dungeon entities called, levels: " + levels + "\n");

        // for (Level level : levels) {
            
        //     List<EntityResponse> levelEntityInfoRes = level.getLevelEntityResponses();

        //     for (EntityResponse entityRes : levelEntityInfoRes) {
        //         allEntityInfoRes.add(entityRes);
        //     }
            
        // }

        // return allEntityInfoRes;

        return currentLevel.getLevelEntityResponses();
    }

    public List<String> getDungeonBuildables() {
        return currentLevel.getBuildables();
    }

    public List<Entity> getDungeonInventory() {
        return inventory;
    }

    public List<ItemResponse> getDungeonInventoryResponse() {
        
        return currentLevel.getLevelInventoryResponses();
    }

    // TODO : return type must be a string!
    // create a way to get all the goals within a single string and make it an assumption (ez extra assumption)

    // TODO: we assume this is just for the current goal at stake
    public String getDungeonGoals() {

        if (beaten) {
            return "";
        }

        return currentLevel.getLevelGoals();
    }

    // goes through JSON files for levels, calls create Level for them, then append in levels. 
    // BTW json files for levels are in test/java/resources/dungeons
    public void createLevels() {
        
        // choose 3-4 levels, make them by browsing files here, make new level based off them and add to levels parameter.
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void tick(String itemUsed, Direction direction) {

        currentLevel.tick(itemUsed, direction);
        // if level has been beaten here, go to the next one
        if (currentLevel.isLevelBeaten()) {

            // assumption for now - that there are 4 levels in a dungeon
            if (levels.size() == 4) {

                // frontend expects - you lose if DMC doesn't have character anymore
                // return null string for goals
                this.beaten = true;
                System.out.println("YOU  WIN !!!\n");

            } else {

                nextLevelMaker();
            
            }
        }
    }

    public void interact(String entityId) {
        currentLevel.interact(entityId);
    }

    public void nextLevelMaker() {
        setInventory(currentLevel.getInventory());
        Random rand = new Random(System.currentTimeMillis());
        //list[r.nextInt(list.length)];
        // make sure that we remove the option for playing that game again 
        levelNames.remove(currentLevel.getName());
        Level nextLvl = new Level(levelNames.get(rand.nextInt(levelNames.size())), inventory, gameMode, currentLevel.getCharacter().getHealth());
        setCurrentLevel(nextLvl);

        // 10% chance to get ring after beating level
        if (randomProbability(10)) {
            TheOneRing ring = new TheOneRing(new Position(0,0), "one_ring", currentLevel.idMaker());
            currentLevel.addToInventory(ring);
        }

        levels.add(nextLvl.toString());
    }

    public Character getCharacter() {
        return currentLevel.getCharacter();
    }

    public void build(String buildable) {
        currentLevel.build(buildable);
    }

    // returns a boolean for a 1 in p chance to succeed
    public boolean randomProbability(int p) {
        boolean val = new Random().nextInt(p) == 0;
        return val;
    }
    public List<String> getLevels() {
        return levels;
    }
}
