package com.zikram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class MinMaxTest {
    public static void main(String[] args) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int nTests = 50;
        int p1Win = 0;
        int p2Win = 0;
        int draw = 0;
        for(int i = 0; i < nTests; i++) {
            System.out.println("Testing for " + i);
            MinMaxAgent agent1 = new MinMaxAgent(true, "score_pit");
//            RandomAgent agent2 = new RandomAgent(false);
            MinMaxAgent agent2 = new MinMaxAgent(false);
            MancalaEnvironment env = new MancalaEnvironment();
            boolean isP1Turn = true;
            while (true) {
                if (isP1Turn) {
                    ReturnNode ret = agent1.peek(env.clone(), 7);
                    Map<String, Boolean> response = env.playerStep1(ret.bestMove);
                    if (!response.get("Repeat")) {
                        isP1Turn = !isP1Turn;
                    }
                    if (response.get("Error")) {
                        System.out.println("Try Again :(");
                    }
                    if (response.get("Terminated")) {
                        break;
                    }

                } else {
                    ReturnNode ret = agent2.peek(env.clone(), 6);
//                    ReturnNode ret = agent2.peek(env.clone());
                    Map<String, Boolean> response = env.playerStep2(ret.bestMove);
                    if (!response.get("Repeat")) {
                        isP1Turn = !isP1Turn;
                    }
                    if (response.get("Error")) {
                        System.out.println("Try Again :(");
                    }
                    if (response.get("Terminated")) {
                        break;
                    }
                }
//                env.printEnv();

            }
            if(env.getPlayer2score() > env.getPlayer1score()) p2Win++;
            else if(env.getPlayer1score() > env.getPlayer2score()) p1Win++;
            else draw++;
        }
        System.out.println("P1 Win : " + p1Win + "\nP2 Win : " + p2Win + "\nDraws : " + draw);
    }
}
