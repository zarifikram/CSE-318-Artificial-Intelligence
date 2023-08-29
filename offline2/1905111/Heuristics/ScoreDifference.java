package com.zikram.Heuristics;

import com.zikram.MancalaEnvironment;

public class ScoreDifference implements Heuristic{
    @Override
    public int calculate(MancalaEnvironment env, boolean isP1) {
        if(isP1) return env.getPlayer1score() - env.getPlayer2score();
        else return env.getPlayer2score() - env.getPlayer1score();
    }
}
