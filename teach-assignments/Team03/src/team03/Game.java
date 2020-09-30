package team03;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Game {
    Player _player;

    public Game(Player newPlayer){
        _player = newPlayer;
    }

    public void saveGame() throws IOException {
        Gson gson = new Gson();
        String playerData = gson.toJson(_player);
        System.out.print("saving "+playerData+"\n");

        Writer writer = new FileWriter(_player.getName()+".json");
        writer.write(playerData);
        writer.close();

        System.out.print("File Saved."+"\n\n");
    }

    public static Game loadGame(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));
        Player playerData = gson.fromJson(reader, Player.class);
        Game loadedGame = new Game(playerData);
        return loadedGame;
    }
}
