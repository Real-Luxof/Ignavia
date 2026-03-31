package com.luxof.ignavia.reverseactions.list;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.ArrayList;
import java.util.List;

public class ReverseOpSlice extends ConstMediaActionNCT {
    public int argc = 3;
    public long mediaCost = 0L;

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        ArrayList<Iota> list = stack.getJUSTAList(0);

        ArrayList<Iota> newList = new ArrayList<>(list.subList(0, stack.getPositiveInt(1)));

        newList.addAll(list.subList(stack.getPositiveInt(2), list.size()));

        return List.of(new ListIota(newList));
    }
}
