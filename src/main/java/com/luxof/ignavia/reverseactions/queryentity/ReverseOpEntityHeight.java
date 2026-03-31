package com.luxof.ignavia.reverseactions.queryentity;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class ReverseOpEntityHeight extends ConstMediaActionNCT {
    public int argc = 1;
    public long mediaCost = 0L;

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        Entity entity = stack.getEntity(0);
        ctx.assertEntityInRange(entity);
        Box box = entity.getBoundingBox();

        return List.of(new DoubleIota(Math.max(box.getXLength(), box.getZLength()))); // :)
    }
}
