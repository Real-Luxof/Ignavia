package com.luxof.ignavia;

import java.util.HashMap;

import at.petrak.hexcasting.api.casting.castables.Action;

// can you register stuff at runtime? yeah.
// should that be stopped? probably.
// do i care? no.
public class ReverseActions {
    private HashMap<Action, Action> mappings = new HashMap<>();
    private HashMap<Action, Action> itsOppositeDay = new HashMap<>();

    public Action register(Action action, Action correspondingReverse) {
        mappings.put(action, correspondingReverse);
        itsOppositeDay.put(correspondingReverse, action);
        return correspondingReverse;
    }
    public Action getReversal(Action action) {
        return mappings.get(action);
    }
    public Action getReverseOfReversal(Action reversal) {
        return itsOppositeDay.get(reversal);
    }
}
