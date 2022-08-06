package thrones;

public class Game {
    
    public static void main(String[] args) {
        Character king = CharacterFactory.createKing();
        Character queen = CharacterFactory.createQueen();
        Character knight = CharacterFactory.createKnight();
        Character dragon = CharacterFactory.createDragon();

        knight.attack(dragon);
    }

}