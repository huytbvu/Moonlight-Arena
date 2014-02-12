package com.game.resource;
/**
 * 
 * @author Huy Vu
 * global resources of the game
 */
public class GlobalResource {
    
    public static final int NUMBER_OF_CONTESTANT = 100;
    
    public static final double MAX_INTEL = 0.85;
    public static final double MAX_INTEL_GUEST = 0.93;
    public static final double MAX_INTEL_FINALE = 0.90;
    public static final double GUEST_CHANCE_SPECIALTY_1 = 0.99;
    public static final double GUEST_CHANCE_SPECIALTY_2 = 0.96;
    
    public static boolean finale_marker = false;

    public static final String EASY_QUESTION_FILE = "easy.csv";
    public static final String MEDIUM_QUESTION_FILE = "medium.csv";
    public static final String HARD_QUESTION_FILE = "hard.csv";
    
    public static final String INFO_FILE = "game_info.txt";
    
    public static final int FIRST_TIME_CONTESTANT = 11;
    public static final int EXPERIENCE_CONTESTANT = 12;
    public static final int SMART_CONTESTANT = 13;
    public static int TIME_LIMIT = 21000;
    
    public static final int GOLD_KEY_TO_START = 1;

    
}
