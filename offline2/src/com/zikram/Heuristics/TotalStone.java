package com.zikram.Heuristics;

import com.zikram.MancalaEnvironment;

public class TotalStone implements Heuristic{
    @Override
    public int calculate(MancalaEnvironment env, boolean isP1) {
        if(isP1) return env.getPlayer1score();
        else return env.getPlayer2score();
    }
}
