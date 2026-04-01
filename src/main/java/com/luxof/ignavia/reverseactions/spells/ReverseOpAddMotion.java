package com.luxof.ignavia.reverseactions.spells;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.misc.MediaConstants;

import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import java.util.List;

import net.minecraft.util.math.Vec3d;

public class ReverseOpAddMotion extends SpellActionNCT {
    public int argc = 0;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {
        return new Result(
            new Spell(),
            MediaConstants.CRYSTAL_UNIT,
            List.of(ParticleSpray.burst(ctx.mishapSprayPos(), 3, 15)),
            1
        );
    }

    public class Spell implements RenderedSpellNCT {
        public void cast(CastingEnvironment ctx) {
            Vec3d pos = ctx.getCastingEntity().getPos();
            Vec3d by = ctx.getCastingEntity().getRotationVector().multiply(Math.random() * 3.0);
            ctx.getCastingEntity().teleport(
                pos.x + by.x,
                pos.y + by.y,
                pos.z + by.z
            );
        }
    }
}
