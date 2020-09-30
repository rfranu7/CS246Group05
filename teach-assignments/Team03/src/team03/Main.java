package team03;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Player newPlayer = new Player("Randeep", 10,5,50);
        newPlayer.addEquipment("armor", 1);
        newPlayer.addEquipment("sword", 1);
        newPlayer.addEquipment("potion", 3);

        // Add player to Game
        Game newGame = new Game(newPlayer);
        newGame.saveGame();

        Game loadedGame = Game.loadGame(newGame._player.getName()+".json");
        loadedGame._player.displayPlayer();

    }
}
