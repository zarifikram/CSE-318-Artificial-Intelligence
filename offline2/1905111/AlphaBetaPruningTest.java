package com.zikram;

import java.util.Map;

public class AlphaBetaPruningTest {
    public static void main(String[] args) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int nTests = 50;
        int p1Win = 0;
        int p2Win = 0;
        int draw = 0;
        for(int i = 0; i < nTests; i++) {
            System.out.println("Testing for " + i);
            AlphaBetaPruningAgent agent1 = new AlphaBetaPruningAgent(true, "opp_stone");
//            RandomAgent agent2 = new RandomAgent(false);
            AlphaBetaPruningAgent agent2 = new AlphaBetaPruningAgent(false, "score");
            MancalaEnvironment env = new MancalaEnvironment();
            boolean isP1Turn = true;
            while (true) {
                if (isP1Turn) {
                    ReturnNode ret = agent1.peek(env.clone(), 10);
                    Map<String, Boolean> response = env.playerStep1(ret.bestMove);
                    if (!response.get("Repeat")) {
                        isP1Turn = !isP1Turn;
                    }
                    //                else{  System.out.println("Agent 1 Gets A Turn Again!"); }
                    if (response.get("Error")) {
                        System.out.println("Try Again :(");
                    }
                    if (response.get("Terminated")) {
                        break;
                    }

                } else {
                    ReturnNode ret = agent2.peek(env.clone(), 10);
//                    System.out.println("Random agent : Taking " + ret.bestMove + " move.");
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
