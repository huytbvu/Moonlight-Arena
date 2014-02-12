package com.game.resource;
/**
 * 
 * @author Huy Vu
 * resource for chests (name, type, photo, value)
 */
import com.game.activity.R;

public class ChestResource {
    
    public static final int BONUS_TIME = 46000;
    
    public static final int EMPTY_CHEST = 0;
    public static final int SMALL_CHEST = 100;
    public static final int MEDIUM_CHEST = 600;
    public static final int BIG_CHEST = 1000;
    public static final int TRAP_CHEST = -1;
    public static final int KEY_CHEST = 1;
    public static final int MOON_CHEST = 99;
    public static final int[] GOLDEN_MOON = {25000,75000,150000,250000,500000};
    
    public static final int NUMBER_OF_CHESTS = 24;
    public static final int MAX_KEY = 6;
    
    public static final int KEY_VALUE = 500;
    public static final int KEY_PHOTO = R.drawable.gold_key;
    
    public static final int KEY_CHEST_PHOTO = R.drawable.bigkey;
    public static final int EMPTY_CHEST_PHOTO = R.drawable.gold_empty;
    public static final int SMALL_CHEST_PHOTO = R.drawable.gold_small;
    public static final int MEDIUM_CHEST_PHOTO = R.drawable.gold_medium;
    public static final int BIG_CHEST_PHOTO = R.drawable.gold_big;
    public static final int TRAP_CHEST_PHOTO = R.drawable.gold_boom;
    
    public static final int[] MOON_CHEST_PHOTO = {
        R.drawable.moon_piece_a,
        R.drawable.moon_piece_b,
        R.drawable.moon_piece_c,
        R.drawable.moon_piece_d,
        R.drawable.moon_piece_e};
    
    public static final int[] MOON_CHEST_PHOTO_MINI = {
        R.drawable.mini_moon_piece_a,
        R.drawable.mini_moon_piece_b,
        R.drawable.mini_moon_piece_c,
        R.drawable.mini_moon_piece_d,
        R.drawable.mini_moon_piece_e
    };

}
