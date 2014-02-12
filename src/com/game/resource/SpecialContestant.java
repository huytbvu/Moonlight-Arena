package com.game.resource;
/**
 * 
 * @author Huy Vu
 * specialcontestant class, subclass of contestant
 */
import java.util.Random;


public class SpecialContestant extends Contestant{

    private String contestantName;
    private int specialty_1;
    private int specialty_2;
    private boolean intelAdjustSkip = false;
    private boolean immunity = true;
    
    public SpecialContestant(String name, int specialty_1, int specialty_2, int guestID) {
        super(GlobalResource.SMART_CONTESTANT, guestID);
        this.contestantName = name;
        this.specialty_1 = specialty_1;
        this.specialty_2 = specialty_2;
        intel = 0.90;
    }
    
    @Override
    public void answer(Question currentQuestion, Random gen, int level){
        if(immunity){
            myanswer = currentQuestion.getCorrectAnswer();
            return;
        }
        double newIntel = 0.0;
        if(currentQuestion.getTopic() == specialty_1) newIntel = GlobalResource.GUEST_CHANCE_SPECIALTY_1;
        else if(currentQuestion.getTopic() == specialty_2) newIntel = GlobalResource.GUEST_CHANCE_SPECIALTY_2;
        if(Math.random() < Math.max(newIntel,intel)){
            gainIntel(gen);
            adjustIntelToLevel(gen,level);
            myanswer = currentQuestion.getCorrectAnswer(); 
        }
        else myanswer = (gen.nextInt(3) + 1 + currentQuestion.getCorrectAnswer())%4;
    }
    
    @Override
    public void gainIntel(Random gen){
        intel += 0.005*gen.nextInt(4);
        if(intel > 0.93) intel = 0.93;
    }
    
    @Override
    public void adjustIntelToLevel(Random gen, int level){
        intelAdjustSkip = !intelAdjustSkip;
        if(intelAdjustSkip) return;
        intel -= 0.005*gen.nextInt(4);
        if(intel < 0.80) intel = 0.80;
    }
    
    public void turnDownImmunity(int prob){
        if(prob==0)
            immunity = false;
    }
    
    public String getContestantName(){
        return contestantName;
    }
    
    
}