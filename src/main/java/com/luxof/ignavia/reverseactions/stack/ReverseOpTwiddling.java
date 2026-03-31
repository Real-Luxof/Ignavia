package com.luxof.ignavia.reverseactions.stack;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

public class ReverseOpTwiddling extends ConstMediaActionNCT {
    public int argc;
    public long meidaCost = 0L;
    public ReverseOpTwiddling(int delete) {
        this.argc = delete;
    }

    @SuppressWarnings("unchecked") // fuck off
    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        return (List<Iota>)stack.stack.subList(0, argc - 1);
    }
}
