package com.zikram;

import com.zikram.Heuristics.*;

import java.util.*;

public class MinMaxAgent {
    private boolean isP1;
    private Map<String, Heuristic> heuristics;
    private final int INF = 100000000;
    private String heuristicChoice;

    public MinMaxAgent(boolean isP1, String heuristicChoice) {
        setupHeuristics();
        this.isP1 = isP1;
        this.heuristicChoice = heuristicChoice;
    }

    public MinMaxAgent(boolean isP1) {
        setupHeuristics();
        this.isP1 = isP1;
        this.heuristicChoice = "score"; // default heuristic
    }

    private void setupHeuristics() {
        this.heuristics = new HashMap<>();
        heuristics.put("score", new ScoreDifference());
        heuristics.put("score_pit", new ScorePitDifference());
        heuristics.put("score_pit_extraMoves", new ScorePitAdditionalMoveDifference());
        heuristics.put("score_pit_extraMoves_capturedStone", new ScorePitAdditionalMoveCapturedDifference());
        heuristics.put("stone", new TotalStone());
        heuristics.put("opp_stone", new OpponentStone());
    }

    public ReturnNode peek(MancalaEnvironment env, int MaxDepth){
        return algorithm(env, true, MaxDepth);
    }

    private ReturnNode algorithm(MancalaEnvironment env, boolean isMax, int depth){
        if(depth == 0 || env.isGameOver()){
            int score =  heuristics.get(this.heuristicChoice).calculate(env, isP1);
            return new ReturnNode(-1, score);
        }

        ReturnNode bestNode;
        List<Integer> moves = new ArrayList<>();
        if(isMax){ // implement the max part
            bestNode = new ReturnNode(-1, -INF);
            for(int i = 0; i < 6; i++) if(env.isValidMove(i, isP1)) moves.add(i);
            reorderMoves(moves);
            for(int move : moves){
                MancalaEnvironment tempEnv = env.clone();
                Map<String, Boolean> response = tempEnv.step(move, isP1);
                ReturnNode returnNode = algorithm(tempEnv, response.get("Repeat"), depth - 1);
                if(returnNode.bestValue > bestNode.bestValue){
                    bestNode.bestValue = returnNode.bestValue;
                    bestNode.bestMove = move;
                }
            }
        }
        else{ // implement the min part (aka opposition worst case moves)
            bestNode = new ReturnNode(-1, INF);
            for(int i = 0; i < 6; i++) if(env.isValidMove(i, !isP1)) moves.add(i);
            reorderMoves(moves);
            for(int move : moves){
                MancalaEnvironment tempEnv = env.clone();
                Map<String, Boolean> response = tempEnv.step(move, !isP1);
                ReturnNode returnNode = algorithm(tempEnv, response.get("Repeat"), depth - 1);
                if(returnNode.bestValue < bestNode.bestValue){
                    bestNode.bestValue = returnNode.bestValue;
                    bestNode.bestMove = move;
                }
            }
        }
        return bestNode;

    }

    private void reorderMoves(List<Integer> moves){
        Collections.shuffle(moves);
    }
}


