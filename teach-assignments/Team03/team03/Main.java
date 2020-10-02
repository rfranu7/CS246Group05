package team03;

import com.google.gson.Gson;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        Player player = new Player("Hero", 10, 10, 50);
        player.addItem("axe", 3);
        player.addItem("dungeon map", 1);
        Game game = new Game(player);
        game.saveGame();
        Game loadedGame = game.loadGame("fname.json");
        loadedGame.player.display();
    }
}