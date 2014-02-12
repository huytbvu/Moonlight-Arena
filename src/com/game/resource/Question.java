package com.game.resource;
/**
 * 
 * @author Huy Vu
 * the question class
 */
import java.util.HashMap;
import java.util.Random;


public class Question{
    
    static final int TOPIC_SCIENCE = 101;
    static final int TOPIC_TECHNOLOGY = 102;
    static final int TOPIC_ENTERTAINMENT = 103;
    static final int TOPIC_SOCIAL = 104;
    static final int TOPIC_HISTORY = 105;
    static final int TOPIC_LITERATURE = 106;
    static final int TOPIC_ART_CULTURE = 107;
    static final int TOPIC_SPORT = 108;
    
    public static final HashMap<Integer,String> SPECIALTY_CODE = new HashMap<Integer, String>(){{
        put(Question.TOPIC_ART_CULTURE,"Art & Culture");
        put(Question.TOPIC_SOCIAL,"Social Knowledge");
        put(Question.TOPIC_ENTERTAINMENT,"Entertainment");
        put(Question.TOPIC_HISTORY,"History");
        put(Question.TOPIC_LITERATURE,"Literature");
        put(Question.TOPIC_SCIENCE,"Science");
        put(Question.TOPIC_SPORT,"Sport");
        put(Question.TOPIC_TECHNOLOGY,"Technology");
    }};
    
    private String title;
    private Option[] opts;
    private int topic;
    
    public Question(String questionTitle, int questionTopic){
        title = questionTitle;
        opts = new Option[4];
        topic = questionTopic;
    }
    
    public void setOption(int pos, Option opt){
        opts[pos] = opt;
    }
    
    public int getCorrectAnswer(){
        for(int i=0; i<4; i++)
            if(opts[i].isCorrect()) return i;
        return -1;
    }
    
    public void shuffle(Random gen){
        for(int i=3; i>0; i--){
            int pos = gen.nextInt(i+1);
            Option temp = opts[i];
            opts[i] = opts[pos];
            opts[pos] = temp;
        }
    }
    
    public int getTopic() {
        return topic;
    }
    
    public String getTopicDisplay(){
        return SPECIALTY_CODE.get(topic);
    }
    
    public String getTitle(){
        return title;
    }
    
    public Option[] getAllChoices(){
        return opts;
    }
}
