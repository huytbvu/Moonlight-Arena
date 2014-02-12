package com.game.resource;

/**
 * 
 * @author Huy Vu
 * chest class
 */

public class Chest {
    
    private int value;
    private int chestResourceID;
    private int chestID;

    
    public Chest(int chest_type, int chestSource, int newID){
        this.value = chest_type;
        this.chestResourceID = chestSource;
        this.chestID = newID;
    }
    
    public int getValue(){
        if(isTrap()||isKey()||isMoon()) return 0;
        else return value;
    }
    
    public boolean isTrap(){
        return (value == ChestResource.TRAP_CHEST);
    }
    
    public boolean isKey(){
        return (value == ChestResource.KEY_CHEST);
    }
    
    public boolean isMoon(){
        return (value == ChestResource.MOON_CHEST);
    }
    
    public boolean isEmpty(){
        return (value == ChestResource.EMPTY_CHEST);
    }
    
    public String getString(){
        if(isTrap()) return "This is a trap chest";
        if(isKey()) return "This is a key chest";
        if(isMoon()) return "This is a moon piece chest";
        return "This is a normal chest";
    }
    
    public int getChestResource(){
        return chestResourceID;
    }
    
    public int getID(){
        return chestID;
    }

}
