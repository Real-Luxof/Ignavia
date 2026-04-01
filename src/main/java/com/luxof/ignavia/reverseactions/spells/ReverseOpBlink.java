package com.luxof.ignavia.reverseactions.spells;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.misc.MediaConstants;

import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import java.util.List;  

public class ReverseOpBlink extends SpellActionNCT {
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
            ctx.getCastingEntity().addVelocity(
                Math.random() * 5.0,
                Math.random() * 5.0,
                Math.random() * 5.0
            );
        }
    }
}
