package com.luxof.ignavia.reverseactions.stack;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

public class ReverseOpDuplicate extends ConstMediaActionNCT {
    public int argc;
    public long mediaCost = 0L;
    public ReverseOpDuplicate(int delete) {
        this.argc = delete;
    }

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        return List.of();
    }
}
