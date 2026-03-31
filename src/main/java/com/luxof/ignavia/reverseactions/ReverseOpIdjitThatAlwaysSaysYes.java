package com.luxof.ignavia.reverseactions;

import java.util.List;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;

public class ReverseOpIdjitThatAlwaysSaysYes extends ConstMediaActionNCT {
    public int argc = 0;
    public long mediaCost = 0L;

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        return List.of(new BooleanIota(true));
    }
}
