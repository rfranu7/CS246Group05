package team03;

import java.util.HashMap;
import java.util.Map;

public class Player {
    String name;
    int health;
    int mana;
    int gold;
    Map<String, Integer> equipment;

    public Player(String _name, int _health, int _mana, int _gold){
        name = _name;
        health = _health;
        mana = _mana;
        gold = _gold;
        equipment = new HashMap<>();
    }

    public void addItem(String name, int value){
        equipment.put(name, value);
    }

    public void display() {
        System.out.format("Name: %s\n" +
        "Health: %d\n" +
        "Mana: %d\n" +
        "Gold: %d\n" +
        "Equipment: \n" ,name, health, mana, gold);
        equipment.forEach((k, v) -> System.out.println(" - " + k + "(" + v +")"));
    }
}