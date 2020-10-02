package team03;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.*;


public class Game {

    Player player;

    public Game(Player _player){
        player = _player;
    }

    public void saveGame()throws IOException{
        Gson gson = new Gson();
        String data = gson.toJson(player);
        System.out.print(data + "\n");
        Writer file = new FileWriter("fname.json");
        file.write(data);
        file.close();
    }

    public static Game loadGame(String fileName)throws FileNotFoundException{
        Gson gson = new Gson();
        JsonReader fRead = new JsonReader(new FileReader(fileName));
        Player player = gson.fromJson(fRead, Player.class);
        Game game = new Game(player);
        return game;
    }
}
