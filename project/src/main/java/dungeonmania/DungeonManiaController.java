package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.goals.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.Level;
//import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Current thoughts: DungeonResponse is like one game and the controller is like the whole app
 * so it stores all the games happening which means it won't be storing entities and stuff.
 * It would just be storing games (DungeonResponses? or dungeons as in /dungeons). The only
 * thing is that the bottom of this file has build and tick which would be things specific to
 * a dungeon game so this is probs not just storing games happening but updating their states
 * too.
 */
public class DungeonManiaController {

    private String name;
    private String gameMode;

    ArrayList<String> validBuildables = new ArrayList<String>(Arrays.asList("bow", "shield", "sceptre", "midnight_armour"));

    // list of goals
    private List<Goal> goals = new ArrayList<Goal>();

    // important for load game, and to know what to build. 
    // this refers to the game, not the level.
    private String currentGameName;
    private DungeonGame currentGame;

    /**
     * Creates game state or smthn idk
     * @param
     */
    public DungeonManiaController() {

    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
    }

    // gets a DungeonGame in any list of dungeonGames
    public DungeonGame getDungeonGame(String name, List<DungeonGame> dungeonList) {

        for (DungeonGame game : dungeonList) {
            if (game.getDungeonName() == name) {

                return game;

            }
        }

        return null;
    }

    /**
     * /dungeons
     * 
     * Done for you.\\\\
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Creates a new game, where dungeonName is the name of the dungeon map
     * (corresponding to a JSON file stored in the model)
     * and gameMode is one of "standard", "peaceful" or "hard".
     * @param dungeonName
     * @param gameMode
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {

        if (!validGameMode(gameMode)) {
            throw new IllegalArgumentException("ERROR : Invalid game mode: " + gameMode + ". Please select one of the following : Peaceful, Standard, Hard.\n");
        }
        if (!dungeonNameExists(dungeonName)) {
            throw new IllegalArgumentException("ERROR: Invalid dungeon file " + dungeonName + ".json; Please select an existing dungeon!");
        }

        DungeonGame newGame = new DungeonGame(dungeonName, gameMode);
        this.currentGame = newGame;
        this.gameMode = gameMode;
        // one of the parameters is returning null

        // System.out.println("id is: " + newGame.getId() + "\n");
        // System.out.println("name is: " + newGame.getDungeonName() + "\n");
        // //System.out.println("entities is: " + newGame.getDungeonEntities() + "\n");
        // System.out.println("inventory response is: " + newGame.getDungeonInventory() + "\n");
        // System.out.println("buildables is: " + newGame.getDungeonBuildables() + "\n");
        // System.out.println("goals is: " + newGame.getDungeonGoals() + "\n");

        return new DungeonResponse(newGame.getId(), newGame.getDungeonName(), newGame.getDungeonEntities(), 
        newGame.getDungeonInventoryResponse(), newGame.getDungeonBuildables(), newGame.getDungeonGoals());
    }
    
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        
        // create a savedGames directory where each game file will be saved
        
        // have to save to the files in such a way that loadGame can then read the file
        // JSONObject
        // JSONHashMap for each entity containing the x,y and type keys
        // Then, a list of hashmaps (JSONArray) with entities as the key
        // Need something for goals
        // gameMode?

        // do above things first as below things haven't been implemented
        // Need something for the health of each movableEntity
        // Need something for inventory
        // sword, armour, shield durability
        
        try {
            Path dir = Files.createDirectories(Paths.get("savedGames"));
            String filePath = dir.toString() + '/' + name + ".json";
            FileWriter file = new FileWriter(filePath);
            JSONObject gameData = new JSONObject();
            JSONArray entities = new JSONArray();
            JSONArray goals = new JSONArray();
            JSONArray inventory = new JSONArray();
            JSONArray levels = new JSONArray();
            gameData.put("gamemode", this.gameMode);
            for (Entity entity : currentGame.getCurrentLevel().getEntities()) {
                if (!(entity instanceof MovableEntity)) {
                    HashMap<String, Object> entity_map = new HashMap<String, Object>();
                    entity_map.put("x", entity.getPosition().getX());
                    entity_map.put("y", entity.getPosition().getY());
                    entity_map.put("type", entity.getType());
                    entities.put(entity_map);
                }
            }
            for (MovableEntity movableEntity : currentGame.getCurrentLevel().getMovableEntities()) {
                if (!(movableEntity instanceof Mercenary) && !movableEntity.isDead()) {
                    HashMap<String, Object> entity_map = new HashMap<String, Object>();
                    entity_map.put("x", movableEntity.getPosition().getX());
                    entity_map.put("y", movableEntity.getPosition().getY());
                    entity_map.put("type", movableEntity.getType());
                    entity_map.put("health", movableEntity.getHealth());
                    entity_map.put("attack", movableEntity.getAttack());
                    entities.put(entity_map);
                }
            }
            for (Mercenary mercenary : currentGame.getCurrentLevel().getMercenaries()) {
                if (!mercenary.isDead()) {
                    HashMap<String, Object> entity_map = new HashMap<String, Object>();
                    entity_map.put("x", mercenary.getPosition().getX());
                    entity_map.put("y", mercenary.getPosition().getY());
                    entity_map.put("type", mercenary.getType());
                    entity_map.put("health", mercenary.getHealth());
                    entity_map.put("attack", mercenary.getAttack());
                    entity_map.put("status", mercenary.getStatus());
                    entities.put(entity_map);
                }
            }
            gameData.put("entities", entities);
            if (!currentGame.getCurrentLevel().isLevelBeaten()) {
                String goal_operator = currentGame.getCurrentLevel().getGoalPool().getOperator();
                if (goal_operator != null) {
                    gameData.put("goal_operator", goal_operator);
                }
                for (Goal goal : currentGame.getCurrentLevel().getGoalPool().getGoalList()) {
                    if (!goal.getCompleted()) {
                        goals.put(goal.getGoalName());
                    }
                }
            }
            gameData.put("goals", goals);
            for (Entity item : currentGame.getCurrentLevel().getInventory()) {
                inventory.put(item.getType());
            }
            gameData.put("inventory", inventory);
            for (String level : currentGame.getLevels()) {
                levels.put(level);
            }
            gameData.put("levels", levels);
            file.write(gameData.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DungeonResponse(currentGame.getDungeonName(), currentGame.getGameMode(), currentGame.getDungeonEntities(), 
        currentGame.getDungeonInventoryResponse(), currentGame.getDungeonBuildables(), currentGame.getDungeonGoals());
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        try {
            List<String> savedFiles = FileLoader.listFileNamesInDirectoryOutsideOfResources("savedGames");
            if (!savedFiles.contains(name)) {
                throw new IllegalArgumentException(name + " is not a saved game.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        DungeonGame loadDungeonGame = new DungeonGame(name);
        this.currentGame = loadDungeonGame;

        return new DungeonResponse(currentGame.getDungeonName(), currentGame.getGameMode(), currentGame.getDungeonEntities(), 
        currentGame.getDungeonInventoryResponse(), currentGame.getDungeonBuildables(), currentGame.getDungeonGoals());
    }

    public List<String> allGames() {
        try {
            return FileLoader.listFileNamesInDirectoryOutsideOfResources("savedGames");
        } catch (IOException e) {
            return new ArrayList<String>();
        }
    }

    public DungeonResponse tick(String itemUsed, Direction direction) throws IllegalArgumentException, InvalidActionException {
        
        // loop through all entities within level, within game, then just call Entity.move(). If they have all the same name, 
        // then it will still trigger. 

        if (itemUsed == null && (direction == null || direction == Direction.NONE)) {
            throw new InvalidActionException("You must either move or use an item. \n");
        }

        if (itemUsed != null && direction != Direction.NONE) {
            throw new InvalidActionException("Cannot move character and use an item at the same time. \n");
        }

        try {
            currentGame.tick(itemUsed, direction);
            // add to unsaved games (overwrite the older version)
            // return new DungeonResponse(currentGame.getDungeonName(), currentGame.getGameMode(), currentGame.getDungeonEntities(), 
            // currentGame.getDungeonInventory(), currentGame.getDungeonBuildables(), currentGame.getDungeonGoals());
            // System.out.println("TICK!!\n");

            // System.out.println("id is: " + currentGame.getId() + "\n");
            // System.out.println("name is: " + currentGame.getDungeonName() + "\n");
            // System.out.println("entities has a null!: " + currentGame.getDungeonEntities().contains(null) + "\n");
            // System.out.println("inventory response is: " + currentGame.getDungeonInventory() + "\n");
            // System.out.println("buildables is: " + currentGame.getDungeonBuildables() + "\n");
            // System.out.println("goals is: " + currentGame.getDungeonGoals() + "\n");

            return new DungeonResponse(currentGame.getDungeonName(), currentGame.getGameMode(), currentGame.getDungeonEntities(), 
            currentGame.getDungeonInventoryResponse(), currentGame.getDungeonBuildables(), currentGame.getDungeonGoals()); 
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * Interacts with a mercenary (where the character bribes the mercenary)
     * or a zombie spawner, where the character destroys the spawner.
     * @param entityId
     * @param entities
     * @return DungeonResponse
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {

        // try {
        //     /**
        //      * Make sure interact just returns dungeonResponse for this entity
        //      */

        //     Level level = currentGame.getCurrentLevel();

        //     Entity entity = level.getEntities().stream()
        //         .filter(e -> entityId.equals(e.getId()))
        //         .findAny()
        //         .orElse(null);

        //     // if(entity == null) {
        //     //     throw new IllegalArgumentException("Entity is not in this dungeon/level. Check if entityId is valid"
        //     //         + " and if the entity belongs to this dungeon/level.");
        //     // }
        //     switch(entity.getType()) {
        //         case "mercenary":
        //             // if(isBribing == true)
        //             mercenaryInteract(level.getCharacter(), (Mercenary) entity);
        //             break;
        //         case "zombie spawner":
        //             // if(isAttacking == true)
        //             zombieSpawnerInteract(level.getCharacter(), (ZombieToastSpawner) entity);
        //             break;
        //         default:
        //             throw new IllegalArgumentException("The entity " + entity.getType() +
        //                 " is not interactable as of Milestone 2. Valid types: mercernary and zombie spawner.");
        //     }
        //     return null;            
        // } catch(IllegalArgumentException e) {
        //     System.out.println("ILLEGAL ARGUMENT EXCEPTION: entityId is not valid.\n" + e.getMessage());
        // } catch(InvalidActionException e) {
        //     System.out.println("INVALID ACTION EXCEPTION: \n" + e.getMessage());
        // }

        currentGame.interact(entityId);

        return new DungeonResponse(currentGame.getDungeonName(), currentGame.getGameMode(), currentGame.getDungeonEntities(), 
        currentGame.getDungeonInventoryResponse(), currentGame.getDungeonBuildables(), currentGame.getDungeonGoals());
    }

    public void mercenaryInteract(Character player, Mercenary mercenary) throws InvalidActionException {
        List<Position> possiblePossitions = getAdjacentPositions(player.getPosition());
        Position mercWithinRangePosition = possiblePossitions.stream()
            .filter(p -> p == mercenary.getPosition())
            .findAny()
            .orElse(null);
        if(mercWithinRangePosition == null) {
            throw new InvalidActionException("Player must be within 2 cardinal tiles of to mercernary to attack!");
        }
        // if(player.inventory.gold == 0) {
        //     throw new InvalidActionException("Player does not have enough gold to bribe mercenary!");
        // }
        
        // Bribe mercenary: make method
    }

    public void zombieSpawnerInteract(Character player, ZombieToastSpawner zombieSpawner) throws InvalidActionException {
        if(!Position.isAdjacent(player.getPosition(), zombieSpawner.getPosition())) {
            throw new InvalidActionException("Player must be adjacent to zombie spawner to attack!");
        }
        // if(player.inventory.weapon == null) {
        //     throw new InvalidActionException("Player does not have weapon to attack zombie spawner!");
        // }

        // Attack zombie spawner: make method
    }

    // Return positions within a 2-tile perimeter of the player in an array list with the following element positions:
    // 0 1 2 3 4
    // x x x x 5
    // x x p x 6
    // x x x x 7
    // x x x 9 8
    // Starts from top left corner and follows a clockwise spiral
    public List<Position> getAdjacentPositions(Position position) {
        int x = position.getX();
        int y = position.getY();
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x-2, y-2));
        adjacentPositions.add(new Position(x-1, y-2));
        adjacentPositions.add(new Position(x, y-2));
        adjacentPositions.add(new Position(x+1, y-2));
        adjacentPositions.add(new Position(x+2, y-2));
        adjacentPositions.add(new Position(x+2, y-1));
        adjacentPositions.add(new Position(x+2, y));
        adjacentPositions.add(new Position(x+2, y+1));
        adjacentPositions.add(new Position(x+2, y+2));
        adjacentPositions.add(new Position(x+1, y+2));
        adjacentPositions.add(new Position(x, y+2));
        adjacentPositions.add(new Position(x-1, y+2));
        adjacentPositions.add(new Position(x-2, y+2));
        adjacentPositions.add(new Position(x-2, y+1));
        adjacentPositions.add(new Position(x-2, y));
        adjacentPositions.add(new Position(x-2, y-1));
        adjacentPositions.add(new Position(x-1, y-1));
        adjacentPositions.add(new Position(x, y-1));
        adjacentPositions.add(new Position(x+1, y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x+1, y+1));
        adjacentPositions.add(new Position(x, y+1));
        adjacentPositions.add(new Position(x-1, y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {

        if (!validBuildable(buildable)) {
            throw new IllegalArgumentException("Buildable: " + buildable + " is not a valid buildable\n");
        }

        currentGame.build(buildable);
        
        return new DungeonResponse(currentGame.getDungeonName(), currentGame.getGameMode(), currentGame.getDungeonEntities(), 
        currentGame.getDungeonInventoryResponse(), currentGame.getDungeonBuildables(), currentGame.getDungeonGoals());
    }

    public boolean dungeonNameExists(String dungeonName) {
        
        if (dungeons().contains(dungeonName)) {
            return true;
        }
        return false;
    }

    public boolean validGameMode(String dungeonMode) {

        if (getGameModes().contains(dungeonMode)) {
            return true;
        }
        return false;
    }

    public Character getCharacter() {
        return currentGame.getCharacter();
    }

    public boolean validBuildable(String buildable) {

        if (validBuildables.contains(buildable)) {

            return true;

        }

        return false;
    }
}
