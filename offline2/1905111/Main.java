package com.zikram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        MancalaEnvironment env = new MancalaEnvironment();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//        MinMaxAgent agent1 = new MinMaxAgent(true);
        AlphaBetaPruningAgent agent1 = new AlphaBetaPruningAgent(true, "score_pit");
//        RandomAgent agent2 = new RandomAgent(false);
        AlphaBetaPruningAgent agent2 = new AlphaBetaPruningAgent(false);
        boolean isP1Turn = true;
        while(true){
            if(isP1Turn) {
                ReturnNode ret = agent1.peek(env.clone(), 10);
                System.out.println("Good agent : Taking " + ret.bestMove + " move. Expected value : " + ret.bestValue);
                Map<String, Boolean> response = env.playerStep1(ret.bestMove);
                if(!response.get("Repeat")){
                   isP1Turn = !isP1Turn;
                }
//                else{  System.out.println("Agent 1 Gets A Turn Again!"); }
                if(response.get("Error")){
                    System.out.println("Try Again :(");
                }
                if(response.get("Terminated")){
                    break;
                }

            }
            else{
                ReturnNode ret = agent2.peek(env, 10);
//                ReturnNode ret = agent2.peek(env);
                System.out.println("Random agent : Taking " + ret.bestMove + " move.");
                Map<String, Boolean> response = env.playerStep2(ret.bestMove);
                if(!response.get("Repeat")){
                    isP1Turn = !isP1Turn;
                }
//                else{  System.out.println("Player 2 Gets A Turn Again!"); }
                if(response.get("Error")){
                    System.out.println("Try Again :(");
                }
                if(response.get("Terminated")){
                    break;
                }
            }
            env.printEnv();
            env.printMetrics();
        }
        System.out.println("Player 1 : " + env.getPlayer1score() + "\nPlayer 2 : " + env.getPlayer2score());
    }
}
