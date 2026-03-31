package com.luxof.ignavia.reverseactions;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

// he's an idjit, but he's MY idjit.
// no homo
public class ReverseOpIdjitThatAlwaysSaysYes extends ConstMediaActionNCT {
    public int argc = 0;
    public long mediaCost = 0L;

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        return List.of(new BooleanIota(true));
    }
}
