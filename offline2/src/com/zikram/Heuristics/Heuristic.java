package com.zikram.Heuristics;

import com.zikram.MancalaEnvironment;

public interface Heuristic {
    int calculate(MancalaEnvironment env, boolean isP1);
}
