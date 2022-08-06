package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import dungeonmania.*;
import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.*;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public class Level {

    private String name;
    private static final int SPIDER_SPAWN_TIME = 30;
    private static final int ZOMBIE_SPAWN_TIME_STANDARD = 20;
    private static final int ZOMBIE_SPAWN_TIME_HARD = 15;
    private static final int HYDRA_SPAWN_TIME = 50;
    private static final int MAX_NUM_ZOMBIES = 5;
    private static final int MAX_NUM_SPIDERS = 5;
    private static final int MAX_NUM_HYDRAS = 3;

    private List<Entity> entities = new ArrayList<Entity>();
    private List<StaticEntity> staticEntities = new ArrayList<StaticEntity>();
    private List<MovableEntity> movableEntities = new ArrayList<MovableEntity>();
    private List<CollectableEntity> collectableEntities = new ArrayList<CollectableEntity>();
    private List<BuildableEntity> buildableEntities = new ArrayList<BuildableEntity>();

    private List<Mercenary> mercenaries = new ArrayList<Mercenary>();
    private List<Portal> portals = new ArrayList<Portal>();
    private List<InvincibilityPotion> invincibilityPotions = new ArrayList<InvincibilityPotion>();
    private List<InvisibilityPotion> invisibilityPotions = new ArrayList<InvisibilityPotion>();
    private List<HealthPotion> healthPotions = new ArrayList<HealthPotion>();
    private List<Bomb> bombs = new ArrayList<Bomb>();
    private List<Spider> spiders = new ArrayList<Spider>();
    private List<ZombieToast> zombies = new ArrayList<ZombieToast>();
    private List<ZombieToastSpawner> zombieToastSpawners = new ArrayList<ZombieToastSpawner>();
    private List<Hydra> hydras = new ArrayList<Hydra>();
    private List<String> levels = new ArrayList<String>();

    private Character character;  

    private GoalPool goalPool;

    private int round;

    private boolean beaten;

    private int idCounter;

    private String gameMode;

    // explanation why this is here in dungeonGame:
    private List<Entity> inventory = new ArrayList<Entity>();

    // given a JSON file, reads everything in and initialises all parameters above    
    public Level(String fileName, List<Entity> inventory, String gameMode, int health) {

        //System.out.println("\t Level being created !");
        this.beaten = false;
        this.round = 0;
        this.idCounter = 0;
        this.inventory = inventory;
        this.gameMode = gameMode;
        this.name = fileName;
        
        try {
            
            String filename = "src/main/resources/dungeons/" + fileName + ".json";
            JsonObject newObj =  JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonArray entityList = newObj.getAsJsonArray("entities");
            System.out.println("\t Level entityList is : " + entityList + "\n");
            List<EntityResponse> entityResponseList = new ArrayList<EntityResponse>();

            // Loop through the entities array in the JSON file and get the arguments within
            for (JsonElement itr : entityList) {
                //System.out.println("\t \t ENTITIES JSON CHECKING!\n");
                JsonObject objInArray = itr.getAsJsonObject();
                int x = objInArray.get("x").getAsInt();
                int y = objInArray.get("y").getAsInt();
                String type = objInArray.get("type").getAsString();
                // added this for portals:
                //String colour = objInArray.get("colour").getAsString();
                //System.out.println("entity : " + itr + ", colour: " + colour + "\n");
                Position pos = new Position(x, y);

                // method here to create the right one
                // Entity newEntity = new Entity();
                //createEntityFromType(type, x, y, pos, colour);
                createEntityFromType(type, x, y, pos, health);
            }

            // GOALS:
            JsonObject goalWrapper = newObj.getAsJsonObject("goal-condition");
            String goalOperator = goalWrapper.get("goal").getAsString();
            JsonArray subgoals = goalWrapper.getAsJsonArray("subgoals");
            List<String> goals = new ArrayList<String>();
            
            if (subgoals != null) {

                // Get the goals from the subgoals array and add them to a list of goals
                for (JsonElement sg : subgoals) {
                    JsonObject goalObj = sg.getAsJsonObject();
                    String goal = goalObj.get("goal").getAsString();
                    goals.add(goal);
                }
                setGoalPool(goalOperator, goals);
            } else {
                setGoalPool(goalOperator);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        if (entities.contains(null)) {
            System.out.println("Entities in level has a null\n");
        }
    }

    /**
     * 
     * --------------------------------------- FOR LOADGAME ----------------------------------
     * 
     * @param dungeonName
     */
    public Level(String dungeonName) {
        
        try {
            
            String filename = "savedGames/" + dungeonName + ".json";
            JsonObject newObj =  JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
            JsonArray entityList = newObj.getAsJsonArray("entities");
            String gameMode = newObj.get("gamemode").getAsString();
            this.gameMode = gameMode;

            // Loop through the entities array in the JSON file and get the arguments within
            for (JsonElement itr : entityList) {
                //System.out.println("\t \t ENTITIES JSON CHECKING!\n");
                JsonObject objInArray = itr.getAsJsonObject();
                int x = objInArray.get("x").getAsInt();
                int y = objInArray.get("y").getAsInt();
                String type = objInArray.get("type").getAsString();
                if (objInArray.has("health")) {
                    int health = objInArray.get("health").getAsInt();
                    int attack = objInArray.get("attack").getAsInt();
                    String status = null;
                    if (objInArray.has("status")) {
                        status = objInArray.get("status").getAsString();
                    }
                    loadMovableEntity(x, y, type, health, attack, status);
                }
                else {
                    Position pos = new Position(x, y);
                    createEntityFromType(type, x, y, pos, 0);
                }
            }
            
            JsonArray goalList = newObj.getAsJsonArray("goals");
            if (goalList.size() > 1) {
                String goalOperator = newObj.get("goal_operator").getAsString();
                List<String> goals = new ArrayList<String>();
                for (JsonElement g : goalList) {
                    String goal = g.getAsString();
                    goals.add(goal);
                }
                setGoalPool(goalOperator, goals);
            }
            else {
                String goal = goalList.get(0).getAsString();
                setGoalPool(goal);
            }

            JsonArray inventoryList = newObj.getAsJsonArray("inventory");
            for (JsonElement inv : inventoryList) {
                String type = inv.getAsString();
                createInventoryEntity(type);
            }
            JsonArray levelList = newObj.getAsJsonArray("levels");
            for (JsonElement l : levelList) {
                levels.add(l.getAsString());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public List<String> getLevels() {
        return levels;
    }

    // do all the logic that should happen in a round : update everything
    public void tick(String itemUsed, Direction direction) {
        
        this.round += 1;

        // Spawna  new spider every 30 ticks, unless there are 4 spiders on the level already
        if (round % SPIDER_SPAWN_TIME == 0 && spiders.size() < MAX_NUM_SPIDERS) {
            Spider spider = spawnSpider();
            //entities.add(spider);
            //movableEntities.add(spider);
            //spiders.add(spider);
        }

        // spawn a new zombie every 20 ticks if standard or peaceful gamemode
        // else spawn every 15 ticks in Hard gamemode
        if (gameMode.equals("standard") || gameMode.equals("peaceful")) {
            if (round % ZOMBIE_SPAWN_TIME_STANDARD == 0 && 
                zombies.size() < MAX_NUM_ZOMBIES) {
                ZombieToast zombie = spawnZombie();
                entities.add(zombie);
                movableEntities.add(zombie);
                zombies.add(zombie);
            }
        } else {
            if (round % ZOMBIE_SPAWN_TIME_HARD == 0 && 
                zombies.size() < MAX_NUM_ZOMBIES) {
                ZombieToast zombie = spawnZombie();
                entities.add(zombie);
                movableEntities.add(zombie);
                zombies.add(zombie);
            }
        }
        
        if (gameMode.equals("hard")) {
            if (round % HYDRA_SPAWN_TIME == 0 &&
                hydras.size() < MAX_NUM_HYDRAS) {
                Hydra hydra = spawnHydra();
                entities.add(hydra);
                movableEntities.add(hydra);
                hydras.add(hydra);
            }
        }

        Iterator <Spider> spiderIterator = spiders.iterator();
        while (spiderIterator.hasNext()) {
            Spider spider = spiderIterator.next();
            if (spider != null) {
                spider.tick();
            }
        }

        Iterator <ZombieToast> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            ZombieToast zombie = zombieIterator.next();
            if (zombie != null) {
                moveZombieRandomly(zombie);
            }
        }

        Iterator <Hydra> hydraIterator = hydras.iterator();
        while (hydraIterator.hasNext()) {
            Hydra hydra = hydraIterator.next();
            if (hydra != null) {
                moveHydraRandomly(hydra);
            }
        }

        // if (zombies != null) {
        //     for (ZombieToast zombie : zombies) {
        //         moveZombieRandomly(zombie);
        //     }
        // }
        
        // Iterator <CollectableEntity> collectablesIterator = collectableEntities.iterator();
        // while (collectablesIterator.hasNext()) {
        //     CollectableEntity entity = collectablesIterator.next();
        //     if(triggersCollect(entity)) {
        //         collectablesIterator.remove();
        //     }
        // }

        if (direction != Direction.NONE) {
            // System.out.println("BEFORE: CHARA POSITION: " + character.getPosition() + "\n");
            moveCharacter(itemUsed, direction);
            // System.out.println("AFTER: CHARA POSITION: " + character.getPosition() + "\n");
        }
        
        if (itemUsed != null) {
            interact(itemUsed);
        }
       
        // TODO tick
        // ticking & fighting MOVABLE ENTITIES
        // for (MovableEntity entity : movableEntities) {
        //     if (triggersCombat(entity)) {
        //         fight(entity);
        //     }
        // }

        Iterator <MovableEntity> movablesIterator = movableEntities.iterator();
        while (movablesIterator.hasNext()) {
            MovableEntity entity = movablesIterator.next();
            tickMoveableEntity(entity, round);
            if(triggersCombat(entity)) {
                fight(entity);
                movablesIterator.remove();
            }
        }

        for (MovableEntity movableEntity : movableEntities) {
            if (movableEntity.isDead()) {
                movableEntities.remove(movableEntity);
            }
        }
        // TODO tick
        // ticking COLLECTING ENTITIES
        // ERROR BECAUSE NEED TO USE A LIST ITERATOR : WE ARE REMOVING FROM COLLECTABLE ENTITIES
        // for (CollectableEntity entity : collectableEntities) {
        //     System.out.println("Entity: " + entity + "\n");
        //     triggersCollect(entity);
        // }

        Iterator <CollectableEntity> collectablesIterator = collectableEntities.iterator();
        while (collectablesIterator.hasNext()) {
            CollectableEntity entity = collectablesIterator.next();
            if(triggersCollect(entity)) {
                collectablesIterator.remove();
            }
        }

        
        setGoalAchieved();
        goalPool.tick();
        if (goalPool.isAchieved()) {
            this.beaten = true;
            System.out.println("LEVEL HAS BEEN BEATEN W0000!!!!\n");
        }

        // System.out.println("END: CHARA POSITION: " + character.getPosition() + "\n");
    }

    public void tickMoveableEntity(Entity entity, int round) {
        String entityId = entity.getId();
        switch(entity.getType()) {
            case "player":
                // System.out.println("\t ticking player...\n");
                return;
            case "mercenary":
                System.out.println("\t ticking merc...\n");
                Mercenary merc = getMercenary(entityId);
                moveMercenary(merc);
                return;
            case "spider":
                System.out.println("\t ticking spider...\n");
                Spider spider = getSpider(entityId);
                // If the spider just spawned, don't tick it
                if (round % SPIDER_SPAWN_TIME == 0) {
                    return;
                }
                if (isBoulderBlockingSpider(spider.checkNextPosition())) {
                    spider.setReversedDirection(true);
                } else {
                    spider.setReversedDirection(false);
                }
                spider.tick();
                return;
            default:
                //System.out.println("Invalid entity id\n");
        }
    }

    public void build(String buildables) {
        
        switch(buildables) {
            case "bow":
                buildBow();
                return;
            case "shield":
                buildShield();
                return;
            case "sceptre":
                buildSceptre();
                return;
            case "midnight_armour":
                buildMidnightArmour();
                return;
        }

    }

    public void buildSceptre() {

        List<Entity> inventoryWood = new ArrayList<Entity>();
        List<Entity> inventoryArrow = new ArrayList<Entity>();
        List<Entity> inventoryKey = new ArrayList<Entity>();
        List<Entity> inventoryTreasure = new ArrayList<Entity>();
        List<Entity> inventorySunStone = new ArrayList<Entity>();

        for (Entity e : inventory) {

            if (e instanceof Wood) {
                inventoryWood.add(e);
            } else if (e instanceof Arrow) {
                inventoryArrow.add(e);
            } else if (e instanceof Treasure) {
                inventoryTreasure.add(e);
            } else if (e instanceof SunStone) {
                inventorySunStone.add(e);
            } else if (e instanceof Key) {
                inventoryKey.add(e);
            }

            
        }

        // one wood or two arrows
        // one key or treasure
        // 1 sun stone

        if ((inventoryWood.size() >= 1 || inventoryArrow.size() >= 2) && 
            (inventoryKey.size() >= 1 || inventoryTreasure.size() >= 1) && 
            inventorySunStone.size() >= 1) {
            Position pos = new Position(0,0);
            Sceptre sceptre = new Sceptre(pos, "sceptre", idMaker(), 10);
            inventory.add(sceptre);
            
            // prioritise consuming wood over arrows
            // consume 1 wood
            if (inventoryWood.size() >= 1) {
                inventory.remove(inventoryWood.get(0));
            // otherwise consume 2 arrows
            } else {
                inventory.remove(inventoryArrow.get(0));
                inventory.remove(inventoryArrow.get(1));
            }
            
            // prioritise consuming treasure over key
            // consume 1 treasure
            if (inventoryTreasure.size() >= 1) {
                inventory.remove(inventoryTreasure.get(0));
            // otherwise consume 1 key
            } else {
                inventory.remove(inventoryKey.get(0));
            }

            inventory.remove(inventorySunStone.get(0));

        }

    }
    
    public void buildMidnightArmour() {

        // 1 sun stone + 1 armour required
        List<Entity> inventorySunStone = new ArrayList<Entity>();
        List<Entity> inventoryArmour = new ArrayList<Entity>();

        for (Entity e : inventory) {
            if (e instanceof Armour) {
                inventoryArmour.add(e);
            } else if (e instanceof SunStone) {
                inventorySunStone.add(e);
            } 
        }

        if (inventorySunStone.size() >= 1 && inventoryArmour.size() >= 1) {
            Position pos = new Position(0,0);
            MidnightArmour mArmour = new MidnightArmour(pos,"shield", idMaker(), 1);
            // remove treasure and key
            inventory.add(mArmour);

            // update inventory to remove the wood
            inventory.remove(inventoryArmour.get(0));
            inventory.remove(inventorySunStone.get(0));          
        }
    }

    // bow : 1 wood 3 arrows    
    public void buildBow() throws InvalidActionException {

        List<Entity> inventoryWood = new ArrayList<Entity>();
        List<Entity> inventoryArrow = new ArrayList<Entity>();

        for (Entity e : inventory) {

            if (e instanceof Wood) {
                inventoryWood.add(e);
            } else if (e instanceof Arrow) {
                inventoryArrow.add(e);
            }
        }

        if (inventoryWood.size() >= 1 && inventoryArrow.size() >= 3) {
            Position pos = new Position(0,0);
            Bow bow = new Bow(pos,"bow", idMaker(), 1);
            // remove treasure and key
            //inventoryIterator.add(bow);
            inventory.add(bow);
            // update inventory to remove the wood
            for (int i = 0; i < 2; i++) {
                inventory.remove(inventoryArrow.get(i));
            }
            
            inventory.remove(inventoryWood.get(0));
        }

    }

    public void buildShield() throws InvalidActionException {

        List<Entity> inventoryWood = new ArrayList<Entity>();
        List<Entity> inventoryTreasure = new ArrayList<Entity>();
        List<Entity> inventoryKey = new ArrayList<Entity>();

        // 2 wood
        // 1 treasure OR key
        for (Entity e : inventory) {

            if (e instanceof Wood) {
                inventoryWood.add(e);
            } else if (e instanceof Treasure) {
                inventoryTreasure.add(e);
            } else if (e instanceof Key) {
                inventoryKey.add(e);
            }
        }

        if (inventoryWood.size() >= 3 && (inventoryTreasure.size() >= 1 || inventoryKey.size() >= 1)) {
            Position pos = new Position(0,0);
            Shield shield = new Shield(pos,"shield", idMaker(), 1);
            // remove treasure and key
            inventory.add(shield);

            // update inventory to remove the wood
            for (int i = 0; i < 1; i++) {
                inventory.remove(inventoryWood.get(i));
            }
            
            // prioritise treasure removal instead of key removal
            if (inventoryTreasure.size() >= 1) {
                inventory.remove(inventoryTreasure.get(0));
            } else {
                inventory.remove(inventoryKey.get(0));
            }
            
        }
        
    }
    
    public void interact(String entityId) {

        System.out.println("INTERACTING\n");
        Entity entity = getEntity(entityId);

        // TODO: (String entityId, fromInventory bool)
        if (entity == null) {
            entity = getFromInventory(entityId);
        }
    
        switch(entity.getType()) {

            // case "bow":
            //     System.out.println("\t creating a bow...\n");
            //     buildBow();
            //     return;
            case "health_potion":
                System.out.println("\t interacting with HP potions...\n");
                HealthPotion hpPotion = getHealthPotion(entityId);
                useHealthPotion(hpPotion);
                return;
            case "invincibility_potion":
                System.out.println("\t interacting with invincibility...\n");
                InvincibilityPotion invincibilityPotion = getInvincibilityPotion(entityId);
                useInvincibilityPotion(invincibilityPotion);
                return;
            case "invisibility_potion":
                System.out.println("\t interacting with invisibility...\n");
                InvisibilityPotion invisibilityPotion = getInvisibilityPotion(entityId);
                useInvisibilityPotion(invisibilityPotion);
                return;
            case "bomb":
                System.out.println("\t interacting with bomb...\n");
                Bomb bomb = getBomb(entityId);
                dropBomb(bomb);
                return;
            case "mercenary":
                System.out.println("\t interacting with merc...\n");
                Mercenary merc = getMercenary(entityId);
                bribeMercenary(merc);
                return;
            case "zombie_toast_spawner":
                System.out.println("\t interacting with zombie spawner...\n");
                ZombieToastSpawner zombieSpawner = getZombieSpawner(entityId);
                zombieSpawnerInteract(zombieSpawner);
                return;
            default:
                System.out.println("Invalid entity id\n");
        }

    }

    // checks if the position of two entities are within range
    public boolean isAdjacent(Entity first, Entity second, int range) {

        int firstX = first.getPosition().getX();
        int firstY = first.getPosition().getY();
        int secondX = second.getPosition().getX();
        int secondY = second.getPosition().getX();

        if ((Math.abs(firstX - secondX) + Math.abs(firstY - secondY)) <= range) {
            return true;
        }

        return false;
    }

    public void zombieSpawnerInteract(ZombieToastSpawner z) {
        
        if (Position.isAdjacent(character.getPosition(), z.getPosition())) {
            System.out.println("Adjacent by 1 indeed\n");
            if (useSword()) {
                System.out.println("Using Sword\n");
                entities.remove(z);
            } else {
                System.out.println("Cannot use Sword...\n");
            }
        } else {
            System.out.println("Not Adjacent...\n");
        }

    }

    public boolean useSword() {
        for (Entity e : inventory) {
            if (e instanceof Sword) {
                inventory.remove(e);
                return true;
            }
        }
        return false;
    }

    public boolean useArmour() {
        for (Entity e : inventory) {
            if (e instanceof Armour) {
                inventory.remove(e);
                return true;
            }
        }
        return false;
    }

    public boolean useMidnightArmour() {
        for (Entity e : inventory) {
            if (e instanceof MidnightArmour) {
                inventory.remove(e);
                return true;
            }
        }
        return false;
    }

    public boolean useBow() {
        for (Entity e : inventory) {
            if (e instanceof Bow) {
                inventory.remove(e);
                return true;
            }
        }
        return false;
    }

    public boolean useShield() {
        for (Entity e : inventory) {
            if (e instanceof Shield) {
                inventory.remove(e);
                return true;
            }
        }
        return false;
    }

    public ZombieToastSpawner getZombieSpawner(String entityId) {
        
        for (ZombieToastSpawner z : zombieToastSpawners) {
            if (z.getId().equals(entityId)) {
                return z;
            }
        }

        return null;
    }

    public void useHealthPotion(HealthPotion potion) {

        character.heal();
        inventory.remove(potion);
        return;
    }

    public HealthPotion getHealthPotion(String entityId) {

        for (HealthPotion i : healthPotions) {
            if (i.getId().equals(entityId)) {
                return i;
            }
        }

        return null;
    }

    public void useInvincibilityPotion(InvincibilityPotion potion) {

        character.setIsInvincible(true);
        inventory.remove(potion);
    
        return;
    }

    public InvincibilityPotion getInvincibilityPotion(String entityId) {
        
        for (InvincibilityPotion i : invincibilityPotions) {
            if (i.getId().equals(entityId)) {
                return i;
            }
        }

        return null;
    }

    public InvisibilityPotion getInvisibilityPotion(String entityId) {
        
        for (InvisibilityPotion i : invisibilityPotions) {
            if (i.getId().equals(entityId)) {
                return i;
            }
        }

        return null;
    }

    public void useInvisibilityPotion(InvisibilityPotion potion) {

        character.setIsInvisible(true);
        inventory.remove(potion);

        return;
    }

    public boolean IsInteractable(String entityId) {
        
        return false;
    }

    public Spider getSpider(String entityId) {

        for (Spider spider : spiders) {
            if (spider.getId().equals(entityId)) {
                return spider;
            }
        }

        return null;
    }

    public Spider spawnSpider() {
        // spawn spider in random location with rand function maybe using height&width given in json? Spawns diagonally down and right of player for now
        Position position = new Position(character.getPosition().getX() + 1, character.getPosition().getY() + 1, 100);
        Spider spider = new Spider(position, "spider", idMaker(), gameMode);
        entities.add(spider);
        movableEntities.add(spider);
        spiders.add(spider);
        System.out.println("Spider has been spawned in level at X: " + spider.getPosition().getX() + " Y: " + spider.getPosition().getY() +" tick: "+ round);
        return spider;
    }

    public Boolean isBoulderBlockingSpider(Position pos) {
        for (StaticEntity statEnt : staticEntities) {
            if (statEnt instanceof Boulder &&
                (statEnt.getPosition().getX() == pos.getX() && statEnt.getPosition().getY() == pos.getY())){
                return true;
            }
        }
        return false;
    } 

    public Mercenary getMercenary(String entityId) {

        for (Mercenary merc : mercenaries) {
            if (merc.getId().equals(entityId)) {
                return merc;
            }
        }

        return null;
    }

    public void moveMercenary(Mercenary merc) {
        Position chrPos = character.getPosition();
        System.out.println("MoveMercenary determine position\n");
        List<Position> possiblePositions = merc.determinePossiblePositions(chrPos);
        Iterator<Position> itr = possiblePositions.iterator();

        while (itr.hasNext()) {
            Position p = itr.next();
            Boulder boulder = (Boulder) findBoulder(p);
            if (isWall(p)) {
                itr.remove();
                System.out.println("MoveMercenary ticking isWall");
            } else if (boulder != null) {
                System.out.println("MoveMercenary ticking boulder");
                if (!moveBoulderAndEntity(boulder, p, merc.getDirection(), merc, "")) {
                    itr.remove();
                }
            }

        }
        System.out.println("MoveMercenary ticking after removing bad positions");
        System.out.println("Player pos: ("+chrPos.getX()+","+chrPos.getY()+").");
        merc.tick(chrPos);
        return;
    }

    public void bribeMercenary(Mercenary mercenary) {
        
        int treasureQuantity = 0;
        for (Entity e : inventory) {
            if (e instanceof Treasure) {
                treasureQuantity++;
                break;
            }
        }

        // we assume it takes only 1 treasure to bribe a merc within 3 tile range
        if (treasureQuantity > 0 && isAdjacent(character, mercenary, 3)) {
            // remove 1 treasure
            useTreasure();
            // bribe merc
            mercenary.setStatus("friendly");
            System.out.println("Mercenary is now friendly\n");
        }

    }

    public ZombieToast getZombie(String entityId) {
        for (ZombieToast zombie : zombies) {
            if (zombie.getId().equals(entityId)) {
                return zombie;
            }
        }
        return null;
    }

    public void useTreasure() {

        for (Entity e : inventory) {
            if (e instanceof Treasure) {
                inventory.remove(e);
                return;
            }
        }

        return;
    }

    public Bomb getBomb(String entityId) {

        for (Bomb b : bombs) {
            if (b.getId().equals(entityId)) {
                return b;
            }
        }

        return null;
    }

    public void dropBomb(Bomb bomb) {
        inventory.remove(bomb);
        Bomb droppedBomb = new Bomb(character.getPosition(), "bomb", bomb.getId());
        entities.add(droppedBomb);
    }

    public boolean triggersCombat(Entity entity) {

        // if the entity is a non-character movable entity and is on the same spot as the character
        if (!(entity instanceof Character) && (entity instanceof MovableEntity) && (entity.getPosition().getX() == character.getPosition().getX() && entity.getPosition().getY() == character.getPosition().getY())) {
            return true;
        }
        return false;
    }

    // simulates combat between character and entity
    public void fight(MovableEntity entity) {

        int character_attack = character.getAttack();
        int entity_attack = entity.getAttack();

        // if (useSword()) {
        //     character_attack += 3;
        // }
        if (useShield()) {
            character_attack -= 5;
        }
        if (useArmour()) {
            entity_attack /= 2;
        }
        if (useMidnightArmour()) {
            character_attack += 5;
            entity_attack /= 2;
        }
        if (useBow()) {
            character_attack *= 2;
        }
        
        if (!character.getIsInvincible()) {
            character.setHealth(character.getHealth() - (entity_attack * entity.getHealth() / 10));
        }
        
        entity.setHealth(entity.getHealth() - (character_attack * character.getHealth() / 5));

        System.out.println("Your health: + " + character.getHealth()  + "\n");
        System.out.println("Fighting :" + entity + "it has health : " + entity.getHealth()  + "\n");

        if (character.getHealth() <= 0) {
            if (!reviveCharacter()) {
                System.out.println("You died ...\n");
                entities.remove(character);
            } else {
                System.out.println("You got revived ../.\n");
            }
            
        }

        if (entity.getHealth() <= 0) {
            System.out.println("Killed entity " + entity.getType() + "\n");
            killEntity(entity);
            
        }

    }

    public void killEntity(Entity entity) {
        
        switch(entity.getType()) {
            case "zombie":
                entities.remove(entity);
                dropArmourZombie();
                zombies.remove(entity);
            case "mercenary":
                entities.remove(entity);
                dropArmourMercenary();
                mercenaries.remove(entity);
            case "spider":
                entities.remove(entity);
                spiders.remove(entity);
        }
    }

    public void dropArmourZombie() {

        // 1 in 5 chance to get armour from zombie
        boolean val = new Random().nextInt(5) == 0;

        if(val) {

            Position pos = new Position(0,0);
            Armour armour = new Armour(pos, "armour", idMaker(), 1);
            inventory.add(armour);
        }

    }

    public void dropArmourMercenary() {

        // 1 in 3 chance to get armour from merc
        boolean val = new Random().nextInt(3) == 0;

        if(val) {

            Position pos = new Position(0,0);
            Armour armour = new Armour(pos, "armour", idMaker(), 1);
            inventory.add(armour);
        }
        
    }

    public boolean reviveCharacter() {

        Entity ring = null;

        for (Entity e : inventory) {
            if (e instanceof TheOneRing) {
                ring = e;
                break;
            }
        }

        if (ring != null) {
            inventory.remove(ring);
            if (gameMode.equals("hard")) {
                character.setHealth(150);
            }
            else {
                character.setHealth(200);
            }
            return true;
        }

        return false;
    }

    public boolean triggersCollect(CollectableEntity entity) {

        // if the entity is a non-character movable entity and is on the same spot as the character
        if ((entity instanceof CollectableEntity) && (entity.getPosition().getX() == character.getPosition().getX() && entity.getPosition().getY() == character.getPosition().getY())) {
            System.out.println("\t\t TRIGGERS COLLECT ENTERED IF CONDITION\n");
            inventory.add(entity);
            entities.remove(entity);
            //collectableEntities.remove(entity);
            return true;
        }

        return false;
    }

    public int getRound() {
        return round;
    }

    public boolean getBeaten() {
        return beaten;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void tickId() {
        this.idCounter += 1;
    }

    public String getGameMode() {
        return gameMode;
    }

    public List<MovableEntity> getMovableEntities() {
        return movableEntities;
    }

    public List<Mercenary> getMercenaries() {
        return mercenaries;
    }
    // public void observeGoalPool(GoalPool goalPool) {
    //     goalPool.addLevel(this);
    // }

    // assuming only one operator and two goals
    public void setGoalPool(String operator, List<String> goals) {
        this.goalPool = new GoalPool(operator, goals);
        //observeGoalPool(this.goalPool);
    }

    // assuming only one operator and two goals
    public void setGoalPool(String goal) {
        this.goalPool = new GoalPool(goal);
        //observeGoalPool(this.goalPool);
    }

    public GoalPool getGoalPool() {
        return goalPool;
    }

    // TODO:
    public int getId() {
        return 1;
    }

    public List<Entity> getInventory() {
        return inventory;
    }

    public void addToInventory(Entity e) {
        inventory.add(e);
    }

    public void removeFromInventory(Entity e) {
        inventory.remove(e);
    }

    public void setGoalAchieved() {

        List<String> goalNames = goalPool.getGoalNames();

        if (character.getHealth() == 0) {
            System.out.println("GAME OVER - CHARACTER DIED\n");
            return;
        }

        if (goalNames.contains("enemies") && allEnemiesDead()) {
            System.out.println("Enemy goal done.\n");
            goalPool.setGoalAchieved("enemies");

        }

        if (goalNames.contains("exit") && exitFound()) {
            System.out.println("Exit goal done.\n");
            goalPool.setGoalAchieved("exit");

        }

        if (goalNames.contains("treasure") && allTreasureFound()) {
            System.out.println("Treasure goal done.\n");
            goalPool.setGoalAchieved("treasure");

        }

        if (goalNames.contains("boulders") && allBouldersOnSwitches()) {
            System.out.println("Boulders goal done.\n");
            goalPool.setGoalAchieved("boulders");

        }
    }

    public boolean checkToOpenExit(Exit exit) {
        if (goalPool.isAchieved()) {
            return true;
        }
        return false;
    }
    
    // public boolean checkPlayerInExit(Character character, Exit exit) {
    //     if (character.getPosition().equals(exit.getPosition()) && checkToOpenExit(exit)) {

    //     }
    // }

    public boolean isWall(Position pos) {
        for (StaticEntity entity : staticEntities) {
            if (entity.getPosition().equals(pos) && entity instanceof Wall) {
                return true;   
            }
        }
        return false;
    }

    public StaticEntity findDoor(Position pos) {
        for (StaticEntity entity : staticEntities) {
            if (entity.getPosition().equals(pos) && entity instanceof Door) {
                return entity;
            }
        }
        return null;
    }

    public CollectableEntity findKey(Position pos) {
        for (CollectableEntity entity : collectableEntities) {
            if (entity.getPosition().equals(pos) && entity instanceof Key) {
                return entity;
            }
        }
        return null;
    }

    public StaticEntity findBoulder(Position pos) {

        for (StaticEntity entity : staticEntities) {
            if (entity.getPosition().equals(pos) && entity instanceof Boulder) {
                return entity;
            }
        }

        return null;
    }

    public Portal isPortal(Position pos) {
        for (Portal entity : portals) {
            if (entity.getPosition().equals(pos)) {
                return entity;   
            }
        }
        return null;
    }

    public boolean isZombie(Position pos) {
        for (MovableEntity entity : movableEntities) {
            if (entity.getPosition().equals(pos) && entity instanceof ZombieToast) {
                return true;
            }
        }
        return false;
    }

    // only checks to see if a wall or boulder is blocking the movement of the character
    public void moveCharacter(String itemUsed, Direction direction) {

        // BOULDER LOGIC ==============================
        // hypothetic new position of the character based on the direction
        // System.out.println("Player position is " + character.getPosition());
        Position pos = character.directionToPosition(direction, character.getPosition());

        // System.out.println("we here 1\n");
        if (isWall(pos)) {
            System.out.println("There is a wall where you want to move! \n" + pos);
            character.tick(itemUsed, Direction.NONE);
            return;
        }

        Boulder boulder = (Boulder) findBoulder(pos);

        // if the adjacent boulder exists (therefore isn't null)
        if (boulder != null) {
            // //System.out.println("we here 3\n");
            // // if we continue by +1 in the same direction we are moving
            // Position secondBoulderPosition = character.directionToPosition(direction, pos);
            // // a character cannot move two boulders at once
            // // also checks if boulder is against another boulder or a wall
            // if (findBoulder(secondBoulderPosition) != null || isWall(secondBoulderPosition)) {
            //     //System.out.println("There is a wall/boulder behind the boulder you wish to move \n");
            //     // causes character to have NONE in tick
            //     character.tick(itemUsed, Direction.NONE);
            //     // C B B
            //     // C B W
            //     // C B ""
            // }
            // // changes the boulder's position 
            // boulder.setPosition(secondBoulderPosition);
            // character.tick(itemUsed, direction);
            moveBoulderAndEntity(boulder, pos, direction, character, itemUsed);
            return;
        }
        
        // PORTAL LOGIC ================================
        // if we are stepping into a portal
        Portal entry = isPortal(pos);
        //System.out.println("ENTRY POSITION IS : " + entry.getPosition() + "\n");
        // if you are entering a portal
        if (entry != null) {
            //System.out.println("we here 4\n");
            //System.out.println("ENTRY POSITION IS : " + entry.getPosition() + "\n");
            // get the corresponding portal 
            Portal exit = getCorrespondingPortal(entry);
            //System.out.println("EXIT POSITION IS : " + exit.getPosition() + "\n");
            // check if the exit from his position is valid (e.g : not a wall)
            Position characterExitPosition = character.directionToPosition(direction, exit.getPosition());
            //System.out.println("CHARA EXIT POSITION IS : " + characterExitPosition + "\n");
            if (findBoulder(characterExitPosition) != null || isWall(characterExitPosition)) {
                System.out.println("There is a wall/boulder behind the boulder you wish to move \n");
                // causes character to have NONE in tick
                character.tick(itemUsed, Direction.NONE);
                return;
            } else {
                character.setPosition(characterExitPosition);
                return;
            }
            // if not set character direction is NONE
        }

        //System.out.println("we here 5\n");
        character.tick(itemUsed, direction);
        //character.setPosition(character.directionToPosition(direction, character.getPosition()));
    }

    // spawn zombie cardinally adjacent to the zombie toast spawner
    public ZombieToast spawnZombie() {
        ZombieToast zombie = null;
        for (ZombieToastSpawner zombieSpawner : zombieToastSpawners) {
            Position zombieSpawnerPos = zombieSpawner.getPosition();
            Position potentialPos1 = new Position(zombieSpawnerPos.getX() + 1, zombieSpawnerPos.getY(), 0);
            if (isWall(potentialPos1) || findBoulder(potentialPos1) != null) {
                Position potentialPos2 = new Position(zombieSpawnerPos.getX() - 1, zombieSpawnerPos.getY(), 0);
                if (isWall(potentialPos2) || findBoulder(potentialPos2) != null) {
                    Position potentialPos3 = new Position(zombieSpawnerPos.getX(), zombieSpawnerPos.getY() + 1, 0);
                    zombie = new ZombieToast(potentialPos3, "zombie_toast", idMaker(), gameMode);
                } else {
                    zombie = new ZombieToast(potentialPos2, "zombie_toast", idMaker(), gameMode);
                }
            } else {
                zombie = new ZombieToast(potentialPos1, "zombie_toast", idMaker(), gameMode);
            }            
        }
        return zombie;
    }

    public Hydra spawnHydra() {
        Hydra hydra = null;
        for (ZombieToastSpawner zombieSpawner : zombieToastSpawners) {
            Position zombieSpawnerPos = zombieSpawner.getPosition();
            Position potentialPos1 = new Position(zombieSpawnerPos.getX() - 1, zombieSpawnerPos.getY(), 0);
            if (isWall(potentialPos1) || findBoulder(potentialPos1) != null) {
                Position potentialPos2 = new Position(zombieSpawnerPos.getX() + 1, zombieSpawnerPos.getY(), 0);
                if (isWall(potentialPos2) || findBoulder(potentialPos2) != null) {
                    Position potentialPos3 = new Position(zombieSpawnerPos.getX(), zombieSpawnerPos.getY() - 1, 0);
                    hydra = new Hydra(potentialPos3, "hydra", idMaker(), gameMode);
                } else {
                    hydra = new Hydra(potentialPos2, "hydra", idMaker(), gameMode);
                }
            } else {
                hydra = new Hydra(potentialPos1, "hydra", idMaker(), gameMode);
            }            
        }
        return hydra;
    }

    public void moveHydraRandomly(Hydra hydra) {
        Random random = new Random();
        int number = random.nextInt(4);
        // if zombie just spawned dont move it
        if (hydra.isJustSpawned()) {
            hydra.setJustSpawned();
            return;
        }
        if (number == 0) {
            if (isWall(hydra.directionToPosition(Direction.UP)) || 
                findBoulder(hydra.directionToPosition(Direction.UP)) != null) {
                moveHydraRandomly(hydra);
            } else {
                hydra.tick(Direction.UP);
            }
        } else if (number == 1) {
            if (isWall(hydra.directionToPosition(Direction.DOWN)) || 
                findBoulder(hydra.directionToPosition(Direction.DOWN)) != null) {
                moveHydraRandomly(hydra);
            } else {
                hydra.tick(Direction.DOWN);
            }
        } else if (number == 2) {
            if (isWall(hydra.directionToPosition(Direction.LEFT)) || 
                findBoulder(hydra.directionToPosition(Direction.LEFT)) != null) {
                moveHydraRandomly(hydra);
            } else {
                hydra.tick(Direction.LEFT);
            }
        } else {
            if (isWall(hydra.directionToPosition(Direction.RIGHT)) || 
                findBoulder(hydra.directionToPosition(Direction.RIGHT)) != null) {
                moveHydraRandomly(hydra);
            } else {
                hydra.tick(Direction.RIGHT);
            }
        }
    }


    // move in direction randomly based on number generated
    public void moveZombieRandomly(ZombieToast zombie) {
        Random random = new Random();
        int number = random.nextInt(4);
        // if zombie just spawned dont move it
        if (zombie.isJustSpawned()) {
            zombie.setJustSpawned();
            return;
        }
        if (number == 0) {
            if (isWall(zombie.directionToPosition(Direction.UP)) || 
                findBoulder(zombie.directionToPosition(Direction.UP)) != null) {
                moveZombieRandomly(zombie);
            } else {
                zombie.tick(Direction.UP);
            }
        } else if (number == 1) {
            if (isWall(zombie.directionToPosition(Direction.DOWN)) || 
                findBoulder(zombie.directionToPosition(Direction.DOWN)) != null) {
                moveZombieRandomly(zombie);
            } else {
                zombie.tick(Direction.DOWN);
            }
        } else if (number == 2) {
            if (isWall(zombie.directionToPosition(Direction.LEFT)) || 
                findBoulder(zombie.directionToPosition(Direction.LEFT)) != null) {
                moveZombieRandomly(zombie);
            } else {
                zombie.tick(Direction.LEFT);
            }
        } else {
            if (isWall(zombie.directionToPosition(Direction.RIGHT)) || 
                findBoulder(zombie.directionToPosition(Direction.RIGHT)) != null) {
                moveZombieRandomly(zombie);
            } else {
                zombie.tick(Direction.RIGHT);
            }
        }
    }

    /**
     * Moves a boulder if a character or mercenary tries to move it
     * and the conditions for moving a boulder are right (i.e. no wall
     * or boulder blocking the boulder to be moved).
     * @param nextPos
     * @param direction
     * @param entity
     * @param itemUsed
     */
    public boolean moveBoulderAndEntity(Boulder boulder, Position nextPos, Direction direction, MovableEntity entity, String itemUsed) {
        //System.out.println("we here 3\n");
        // if we continue by +1 in the same direction we are moving
        Position  secondBoulderPosition = null;
        Mercenary merc = null;
        boolean isPlayer = false;

        // Only runs code if entity is a player or mercenary
        if (entity.getType().equals("player")) {
            secondBoulderPosition = character.directionToPosition(direction, nextPos);
            isPlayer = true;
        } else if (entity.getType().equals("mercenary")) {
            merc = (Mercenary) entity;
            secondBoulderPosition = merc.directionToPosition(direction, nextPos);
        } else {
            System.out.println("moveBoulderAndEntity in Level only works for mercenary and player, current entity is " + entity.getType());
            return false;
        }
        // a character/mercenary cannot move two boulders at once
        // also checks if boulder is against another boulder or a wall
        if (findBoulder(secondBoulderPosition) != null || isWall(secondBoulderPosition)) {
            //System.out.println("There is a wall/boulder behind the boulder you wish to move \n");
            // causes character to have NONE in tick
            if (isPlayer) {
                character.tick(itemUsed, Direction.NONE);
            } else {
                System.out.println("moveBoulder ticking for 2 boulders/wall blocking boulder"+". Player pos: ("+character.getPosition().getX()+","+character.getPosition().getY()+").");
                // merc.tick(character.getPosition()); // might not want to tick since there are a few positions
            }
            // C B B
            // C B W
            // C B ""
            return false;
        }
        // changes the boulder's position 
        boulder.setPosition(secondBoulderPosition);
        if (isPlayer) {
            character.tick(itemUsed, direction);
        } else {
            System.out.println("moveBoulder ticking for nothing blocking boulder"+". Player pos: ("+character.getPosition().getX()+","+character.getPosition().getY()+").");
            merc.tick(character.getPosition());
        }
        return true;
    }
    
    public void update(boolean achieved) {
        this.beaten = achieved;
    }
    
    public boolean isLevelBeaten() {
        return this.beaten;
    }

    public boolean allEnemiesDead() {
        
        for (MovableEntity movable : movableEntities) {

            if (!(movable instanceof Character)) {
                return false;
            }
            
        }

        return true;
    }

    public boolean exitFound() {

        // declare non-equal positions 
        Position playerPosition = character.getPosition();
        Position exitPosition = null;

        // get the exit's position and update our variable if found
        for (StaticEntity entity : staticEntities) {

            if (entity instanceof Exit) {
                exitPosition = entity.getPosition();
            }

        }

        return (playerPosition.getX() == exitPosition.getX() && playerPosition.getY() == exitPosition.getY());
    }

    public boolean allTreasureFound() {

        for (CollectableEntity entity : collectableEntities) {
            if (entity instanceof Treasure) {
                return false;
            }
        }

        return true;
    }

    public boolean allBouldersOnSwitches() {

        // initialise positions as non-equal
        Position boulderPosition = new Position(0,0);
        Position switchPosition = new Position (1,1);
        Boolean boulderOnSwitch = false;

        
        for (StaticEntity floorSwitch : staticEntities) {
            if (!(floorSwitch instanceof Wall)) {
                System.out.println("STATIC class: " + floorSwitch.getClass() + ", position : " + floorSwitch.getPosition() + "\n");
            }
           
        }


        for (StaticEntity floorSwitch : staticEntities) {

            if (floorSwitch instanceof FloorSwitch) {
                System.out.println("Found switch, position: " + floorSwitch.getPosition() + "\n");
                boulderOnSwitch = false;
                switchPosition = floorSwitch.getPosition();

                for (StaticEntity boulder : staticEntities) {

                    if (boulder instanceof Boulder) {
                        System.out.println("Found boulder, position: " + boulder.getPosition() + "\n");
                        boulderPosition = boulder.getPosition();
                    }
    
                    if (boulderPosition.equals(switchPosition)) {
                        boulderOnSwitch = true;
                    }
                    
                }

                if (!boulderOnSwitch) {
                    return false;
                }

            }
            
                

        }

        return true;

    }

    // creates the entity based on the information provided in JSON file
    //public void createEntityFromType(String type, int x, int y, Position pos, String colour) {
    public void createEntityFromType(String type, int x, int y, Position pos, int health) {

        switch(type) {

            // TODO : zombie, toaster and more missing: add them to JSON file then below
            case "boulder":
                Boulder boulder = new Boulder(pos, type, idMaker());
                entities.add(boulder);
                staticEntities.add(boulder);
                return;
            case "bow": 
                // TODO : make sure that it is collectable - may have to create a method for collecting buildables and put 
                // it in tick like we do for collectables but for builables
                // Bow bow = new Bow(pos, type, idMaker(), 1);
                // entities.add(bow);
                // collectableEntities.add(bow);
                return;
            case "player":  
                Position charPosition = new Position(pos.getX(), pos.getY(), 1);
                Character character = new Character(charPosition, type, idMaker(), gameMode);
                if (health != 0) {
                    character.setHealth(health);
                }
                entities.add(character);
                movableEntities.add(character);
                setCharacter(character);
                return;
            case "door":
                // TODO
                Door door = new Door(pos, type, idMaker());
                entities.add(door);
                staticEntities.add(door);
                return;
            case "bomb":
                Bomb bomb = new Bomb(pos, type, idMaker());
                entities.add(bomb);
                collectableEntities.add(bomb);
                bombs.add(bomb);
                return;
            case "wood":
                Wood wood = new Wood(pos, type, idMaker());
                entities.add(wood);
                collectableEntities.add(wood);
                return;
            case "arrow":
                Arrow arrow = new Arrow(pos, type, idMaker());
                entities.add(arrow);
                collectableEntities.add(arrow);
                return;
            case "exit":
                Exit exit = new Exit(pos, type, idMaker());
                entities.add(exit);
                staticEntities.add(exit);
                return;
            case "treasure": 
                Treasure treasure = new Treasure(pos, type, idMaker());
                entities.add(treasure);
                collectableEntities.add(treasure);
                return;
            case "wall": 
                Wall wall = new Wall(pos, type, idMaker());
                entities.add(wall);
                staticEntities.add(wall);
                return;
            case "spider":
                Spider spider = new Spider(pos, type, idMaker(), gameMode);
                entities.add(spider);
                movableEntities.add(spider);
                return;
            case "zombie_toast_spawner":
                ZombieToastSpawner zombieSpawner = new ZombieToastSpawner(pos, type, idMaker());
                entities.add(zombieSpawner);
                staticEntities.add(zombieSpawner);
                zombieToastSpawners.add(zombieSpawner);
                return;
            case "zombie_toast":
                ZombieToast zombie = new ZombieToast(pos, type, idMaker(), gameMode);
                entities.add(zombie);
                movableEntities.add(zombie);
                zombies.add(zombie);
                return;
            case "mercenary":
                Mercenary merc = new Mercenary(pos, type, idMaker(), gameMode);
                entities.add(merc);
                movableEntities.add(merc);
                mercenaries.add(merc);
                return;
            case "switch":
                FloorSwitch switchObject = new FloorSwitch(pos, type, idMaker());
                entities.add(switchObject);
                staticEntities.add(switchObject);
                return;
            case "health_potion": 
                HealthPotion healthPotion = new HealthPotion(pos, type, idMaker());
                entities.add(healthPotion);
                collectableEntities.add(healthPotion);
                healthPotions.add(healthPotion);
                return;
            case "invincibility_potion": 
                InvincibilityPotion invincibilityPotion = new InvincibilityPotion(pos, type, idMaker());
                entities.add(invincibilityPotion);
                collectableEntities.add(invincibilityPotion);
                invincibilityPotions.add(invincibilityPotion);
                return;
            case "invinsibility_potion": 
                InvisibilityPotion invisibilityPotion = new InvisibilityPotion(pos, type, idMaker());
                entities.add(invisibilityPotion);
                collectableEntities.add(invisibilityPotion);
                invisibilityPotions.add(invisibilityPotion);
                return;
            case "sword":
                Sword sword = new Sword(pos, type, idMaker(), 1);
                entities.add(sword);
                collectableEntities.add(sword);
                return;
            case "portal":
                //Portal portal = new Portal(pos, type, idMaker(), colour);
                Portal portal = new Portal(pos, type, idMaker());
                entities.add(portal);
                staticEntities.add(portal);
                portals.add(portal);
                return;
            case "sun_stone":
                SunStone sunStone = new SunStone(pos, type, idMaker());
                entities.add(sunStone);
                collectableEntities.add(sunStone);
                return;
        }
    }

    public void createInventoryEntity(String type) {
        Position pos = new Position(0, 0);
        switch(type) {
            case "treasure":
                Treasure treasure = new Treasure(pos, type, idMaker());
                inventory.add(treasure);
                return;
            case "arrow":
                Arrow arrow = new Arrow(pos, type, idMaker());
                inventory.add(arrow);
                return;
            case "bomb":
                Bomb bomb = new Bomb(pos, type, idMaker());
                inventory.add(bomb);
                return;
            case "wood":
                Wood wood = new Wood(pos, type, idMaker());
                inventory.add(wood);
                return;
            case "sword":
                Sword sword = new Sword(pos, type, idMaker(), 1);
                inventory.add(sword);
                return;
            case "health_potion":
                HealthPotion healthPotion = new HealthPotion(pos, type, idMaker());
                inventory.add(healthPotion);
                return;
            case "invincibility_potion":
                InvincibilityPotion invincibilityPotion = new InvincibilityPotion(pos, type, idMaker());
                inventory.add(invincibilityPotion);
                return;
            case "invisibility_potion":
                InvisibilityPotion invisibilityPotion = new InvisibilityPotion(pos, type, idMaker());
                inventory.add(invisibilityPotion);
                return;
            // case "key":
            //     Key key = new Key(pos, type, idMaker());
            //     inventory.add(key);
            //     return;    
            case "armour":
                Armour armour = new Armour(pos, type, idMaker(), 1);
                inventory.add(armour);
                return;
            case "bow":
                Bow bow = new Bow(pos, type, idMaker(), 1);
                inventory.add(bow);
                return;
            case "shield":
                Shield shield = new Shield(pos, type, idMaker(), 1);
                inventory.add(shield);
                return;
            case "midnight_armour":
                MidnightArmour midnightArmour = new MidnightArmour(pos, type, idMaker(), 1);
                inventory.add(midnightArmour);
                return;
            case "one_ring":
                TheOneRing theOneRing = new TheOneRing(pos, type, idMaker());
                inventory.add(theOneRing);
                return;
            case "sun_stone":
                SunStone sunStone = new SunStone(pos, type, idMaker());
                inventory.add(sunStone);
                return;
            case "sceptre":
                Sceptre sceptre = new Sceptre(pos, type, idMaker(), 10);
                inventory.add(sceptre);
                return;
        }
    }   

    public void loadMovableEntity(int x, int y, String type, int health, int attack, String status) {
        Position pos = new Position(x, y, 1);
        switch(type) {
            case "player":
                Character character = new Character(pos, type, idMaker(), gameMode);
                character.setHealth(health);
                character.setAttack(attack);
                entities.add(character);
                movableEntities.add(character);
                setCharacter(character);
                return;
            case "mercenary":
                Mercenary mercenary = new Mercenary(pos, type, idMaker(), gameMode);
                mercenary.setHealth(health);
                mercenary.setAttack(attack);
                mercenary.setStatus(status);
                entities.add(mercenary);
                movableEntities.add(mercenary);
                mercenaries.add(mercenary);
                return;
            case "spider":
                Spider spider = new Spider(pos, type, idMaker(), gameMode);
                spider.setHealth(health);
                spider.setAttack(attack);
                entities.add(spider);
                movableEntities.add(spider);
                return;
            case "zombie_toast":
                ZombieToast zombie = new ZombieToast(pos, type, idMaker(), gameMode);
                zombie.setHealth(health);
                zombie.setAttack(attack);
                entities.add(zombie);
                movableEntities.add(zombie);
                zombies.add(zombie);
                return;
        }

    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getEntity(String entityId) {

        for (Entity entity : entities) {
            if (entity.getId().equals(entityId)) {
                return entity;
            }
        }

        return null;
    }

    public Entity getFromInventory(String entityId) {

        for (Entity entity : inventory) {
            if (entity.getId().equals(entityId)) {
                return entity;
            }
        }

        return null;
    }

    public List<EntityResponse> getLevelEntityResponses() {
        // loop through entities in level and store the entity info response of them : method in level
        // add list to list declared above and return that!

        List<EntityResponse> entityInfoResponses = new ArrayList<EntityResponse>();

        //System.out.println("\t level's entities are : "  + entities + "\n");
        for (Entity entity : entities) {
            entityInfoResponses.add(new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(), entity.getIsInteractable()));
        }

        //System.out.println("level's entityInfoResponse: " + entityInfoResponses + "\n");
        return entityInfoResponses;
    }

    public List<ItemResponse> getLevelInventoryResponses() {

        List<ItemResponse> inventoryInfoResponses = new ArrayList<ItemResponse>();

        for (Entity entity : inventory) {
            inventoryInfoResponses.add(new ItemResponse(entity.getId(), entity.getType()));
        }

        return inventoryInfoResponses;
    }

    public List<String> getBuildables() {

        // POTENTIAL! list of buildable things

        // go through my inventory 
        // if there's enough ingredients -> then add the result to the string

        List<String> buildables = new ArrayList<String>();
        
        if (sufficientBowMaterials()) {
            buildables.add("bow");
        }
        if (sufficientShieldMaterials()) {
            buildables.add("shield");
        }
        if (sufficientSceptreMaterials()) {
            buildables.add("sceptre");
        }
        if (sufficientMidnightArmourMaterials()) {
            buildables.add("midnight_armour");
        }


        return buildables;
    }

    public boolean sufficientMidnightArmourMaterials() {

        // 1 sun stone + 1 armour + 0 zombies on level
        List<Entity> inventorySunStone = new ArrayList<Entity>();
        List<Entity> inventoryArmour = new ArrayList<Entity>();

        for (Entity e : inventory) {

            if (e instanceof SunStone) {
                inventorySunStone.add(e);
            } else if (e instanceof Armour) {
                inventoryArmour.add(e);
            }

            if (inventorySunStone.size() >= 1 && inventoryArmour.size() >= 1 && zombies.size() == 0) {
                return true;
            }
        }

        return false;
    }

    public boolean sufficientSceptreMaterials() {

        // one wood or two arrows
        // one key or treasure
        // 1 sun stone
        List<Entity> inventoryWood = new ArrayList<Entity>();
        List<Entity> inventoryArrow = new ArrayList<Entity>();
        List<Entity> inventoryKey = new ArrayList<Entity>();
        List<Entity> inventoryTreasure = new ArrayList<Entity>();
        List<Entity> inventorySunStone = new ArrayList<Entity>();

        for (Entity e : inventory) {

            if (e instanceof Wood) {
                inventoryWood.add(e);
            } else if (e instanceof Arrow) {
                inventoryArrow.add(e);
            } else if (e instanceof Treasure) {
                inventoryTreasure.add(e);
            } else if (e instanceof SunStone) {
                inventorySunStone.add(e);
            } else if (e instanceof Key) {
                inventoryKey.add(e);
            }

            if ((inventoryWood.size() >= 1 || inventoryArrow.size() >= 2) && 
            (inventoryKey.size() >= 1 || inventoryTreasure.size() >= 1) && 
            inventorySunStone.size() >= 1) {

                return true;
            }
        }


        return false;
    }

    public boolean sufficientBowMaterials() {

        List<Entity> inventoryWood = new ArrayList<Entity>();
        List<Entity> inventoryArrow = new ArrayList<Entity>();

        for (Entity e : inventory) {

            if (e instanceof Wood) {
                inventoryWood.add(e);
            } else if (e instanceof Arrow) {
                inventoryArrow.add(e);
            }

            if (inventoryWood.size() >= 1 && inventoryArrow.size() >= 3) {
                return true;
            }
        }

        return false;
    }

    public boolean sufficientShieldMaterials() {
        List<Entity> inventoryWood = new ArrayList<Entity>();
        List<Entity> inventoryTreasure = new ArrayList<Entity>();
        List<Entity> inventoryKey = new ArrayList<Entity>();

        // 2 wood
        // 1 treasure OR key
        for (Entity e : inventory) {

            if (e instanceof Wood) {
                inventoryWood.add(e);
            } else if (e instanceof Treasure) {
                inventoryTreasure.add(e);
            } else if (e instanceof Key) {
                inventoryKey.add(e);
            }

            if (inventoryWood.size() >= 3 && (inventoryTreasure.size() >= 1 || inventoryKey.size() >= 1)) {
                return true;   
            }
        }

        return false;
    }




    public String getLevelGoals() {

        List<String> goalName = goalPool.getGoalNames();
        String levelGoals = ":" + goalName.get(0);

        // goal string should resemble : "ennemies, treasure, exit"
        //for (String goal : goalName) {
        for (Integer i = 1; i < goalName.size(); i ++) { 
            levelGoals += " " + goalPool.getOperator() + " :" + goalName.get(i);
        }
        
        return levelGoals;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String idMaker() {

        int id = idCounter;
        tickId();
        return Integer.toString(id);
    }

    /**
     * keeps the attack of the character at 25 if the character has a sword in the inventory
     * base attack value - 10 
     * sword attack value - 15
     * @param character
     */
    public void checkSword(Character character) {
        character.setAttack(15);
        for (Entity entity : inventory) {
            if (entity instanceof Sword) {
                character.setAttack(25);
            }
        }
    }
    //check if the movement is null when using potions

    // public void useHealth() {
    //     int health = character.getHealth();
    //     for (Entity entity : inventory) {
    //         if (entity instanceof HealthPotion) {
    //             inventory.remove(entity);
    //             if (health >= 100) {
    //                 character.setHealth(200);
    //             } else {
    //                 character.setHealth(health + 100);
    //             }
    //         } else {
    //             throw new InvalidActionException("No health potion available.");
    //         }
    //     }
    // }

    public Portal getCorrespondingPortal(Portal portal) {

        // since the number of portals is assumed even, the odd number will always be the greater bound
        // since 0 & 1, 2 & 3, etc. are linked, the pair is always the next portal in portals
        if (portals.indexOf(portal) % 2 == 0) {
            return portals.get(portals.indexOf(portal) + 1);
        } else {
            return portals.get(portals.indexOf(portal) - 1); 
        }

    }

    public String getName() {
        return name;
    }

}
