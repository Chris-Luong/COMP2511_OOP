package thrones;

import java.util.Random;

public class GameBuilder {

    private Game game;
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean first = true;

    public GameBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        game = new Game();
    }

    private boolean checkFirst(int x, int y) {
        if (first) {
            first = false;
            this.x = x;
            this.y = y;

            return true;
        }

        return false;
    }

    private void update(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public GameBuilder createKing(int x, int y) {
        if (checkFirst(x, y)) {
            game.addCharacter(new King(x, y));
        } else {
            update(x, y);
            game.addCharacter(new King(this.x, this.y));
        }

        return this;
    }


    public GameBuilder createQueen(int x, int y) {
        if (checkFirst(x, y)) {
            game.addCharacter(new Queen(x, y));
            
        } else {
            update(x, y);
            game.addCharacter(new Queen(this.x, this.y));
        }

        return this;
    }

    public GameBuilder createDragon() {
        Random rand = new Random();
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        
        if (checkFirst(x, y)) {
            game.addCharacter(new Dragon(x, y));
        } else {
            update(x, y);
            game.addCharacter(new Dragon(this.x, this.y));
        }

        return this;
    }

    public static void main(String[] args) {
        new GameBuilder(10, 10)
            .createKing(1, 1)
            .createQueen(1, 2)
            .createDragon();
    }

}