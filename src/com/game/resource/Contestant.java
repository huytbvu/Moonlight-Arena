package com.game.resource;
/**
 * 
 * @author Huy Vu
 * contestant class
 */
import java.util.Random;

public class Contestant{
    
    protected double intel;
    protected int type;
    protected boolean stillInGame;
    protected int myanswer;
    protected int guestID;
    
    public Contestant(int type, int guestID){
        this.stillInGame = true;
        this.type = type;
        this.myanswer = -1;
        intel = Math.random()/5;
        this.guestID = guestID;
        switch(type){
            case GlobalResource.FIRST_TIME_CONTESTANT: intel += 0.5; break;
            case GlobalResource.EXPERIENCE_CONTESTANT: intel += 0.55; break;
            case GlobalResource.SMART_CONTESTANT: intel += 0.6; break;
            default: break;
        }
    }
    
    public void gainIntel(Random gen){
        double limit = (GlobalResource.finale_marker) ? 
                GlobalResource.MAX_INTEL_FINALE:GlobalResource.MAX_INTEL;
        switch(type){
            case GlobalResource.FIRST_TIME_CONTESTANT: intel += 0.01*gen.nextInt(3); break;
            case GlobalResource.EXPERIENCE_CONTESTANT: intel += 0.01*gen.nextInt(4); break;
            case GlobalResource.SMART_CONTESTANT: intel += 0.01*gen.nextInt(5); break;
            default: break;
        }
        if(intel > limit) intel = limit;
    }
    
    public void adjustIntelToLevel(Random gen, int level){
        intel -= 0.005*gen.nextInt(5)*Math.floor(Math.sqrt(level));
    }
    
    public void answer(Question currentQuestion, Random gen, int level){
        if(Math.random() < intel){
            gainIntel(gen);
            adjustIntelToLevel(gen, level);
            myanswer = currentQuestion.getCorrectAnswer(); 
        }
        else myanswer = (gen.nextInt(3) + 1 + currentQuestion.getCorrectAnswer())%4;
    }
    
    public boolean stillInGame(){
        return stillInGame;
    }
    
    public void eliminated(){
        stillInGame = false;
    }
    
    public int getAnswer(){
        return myanswer;
    }
    
    public int getContestantID(){
        return guestID;
    }
    
}
