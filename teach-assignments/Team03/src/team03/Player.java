package team03;

import java.util.HashMap;
import java.util.Map;

public class Player {
    String _name;
    int _health;
    int _mana;
    int _gold;
    Map<String, Integer> _equipment;

    //Constructor
    public Player(String nameInput, int healthInput, int manaInput, int goldInput){
        _name = nameInput;
        _health = healthInput;
        _mana = manaInput;
        _gold = goldInput;
        _equipment = new HashMap<>();
    }

    // Getter
    public String getName(){ return _name; }
    public int getHealth(){ return _health; }
    public int getMana(){ return _mana; }
    public int getGold(){ return _gold; }
    public Map<String, Integer> getEquipment(){ return _equipment; }

    // Setter
    public void setName(String nameInput){ _name = nameInput; }
    public void setHealth(int healthInput){ _health = healthInput; }
    public void setMana(int manaInput){ _mana = manaInput; }
    public void setGold(int goldInput){ _gold = goldInput; }

    public void displayPlayer(){
        System.out.format(
            "Name: %s\n" +
            "Health: %d\n" +
            "Mana: %d\n" +
            "Gold: %d\n"
            ,_name,_health,_mana,_gold
        );
        System.out.print("Equipments:\n");
        _equipment.forEach((k, v) -> System.out.println(" - " + k + "(" + v +")"));
    }

    public void addEquipment(String equipmentName, int equipmentValue){
        _equipment.put(equipmentName, equipmentValue);
    }
}
