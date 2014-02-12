package com.game.resource;
/**
 * 
 * @author Huy Vu
 * the option class
 */
public class Option{
    
    
    private String text;
    private boolean correct;
    
    public Option(String optionText){
        text = optionText;
        correct = false;
    }
    
    public void setTrue(){correct = true;}
    public String getText(){return text;}
    public boolean isCorrect(){return correct;}
}