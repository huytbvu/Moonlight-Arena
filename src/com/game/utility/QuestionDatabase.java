package com.game.utility;
/**
 * 
 * @author Huy Vu
 * question database of the game
 * handle parsing question files and randomization
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import com.game.resource.Option;
import com.game.resource.Question;

public class QuestionDatabase {
    
 // import questions from files
    public static ArrayList<Question> importQuestionToDB(InputStream in, Random gen) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        ArrayList<Question> db = new ArrayList<Question>();
        while(br.ready()) db.add(createQuestion(br.readLine(), gen));
        br.close();
        return db;
    }
    
    // create a question using its string representation
    private static Question createQuestion(String questionInfo, Random gen){
        String[] elements = questionInfo.split(",");
        int correctChoice = Integer.parseInt(elements[5]);
        Question q = new Question(elements[0], Integer.parseInt(elements[6]));
        q.setOption(0, new Option(elements[1]));
        q.setOption(1, new Option(elements[2]));
        q.setOption(2, new Option(elements[3]));
        q.setOption(3, new Option(elements[4]));
        q.getAllChoices()[correctChoice-1].setTrue();
        q.shuffle(gen);
        return q;
    }

}
