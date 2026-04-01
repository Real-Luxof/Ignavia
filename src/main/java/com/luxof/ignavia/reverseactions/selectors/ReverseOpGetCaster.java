package com.luxof.ignavia.reverseactions.selectors;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.misc.MediaConstants;

import com.luxof.ignavia.minterfaces.OverrideCasterMinterface;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import java.util.List;

import net.minecraft.entity.LivingEntity;

// set the caster instead!
public class ReverseOpGetCaster extends SpellActionNCT {
    public int argc = 1;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {
        LivingEntity overrideWith = stack.getLivingEntityButNotArmorStand(0);
        ctx.assertEntityInRange(overrideWith);
        return new Result(
            new Spell(overrideWith),
            MediaConstants.CRYSTAL_UNIT,
            ctx.getCastingEntity() != null
                ? List.of(
                    ParticleSpray.burst(ctx.getCastingEntity().getPos(), 10, 50),
                    ParticleSpray.burst(overrideWith.getPos(), 10, 50)
                )
                : List.of(
                    ParticleSpray.burst(overrideWith.getPos(), 10, 50)
                ),
            1
        );
    }

    public class Spell implements RenderedSpellNCT {
        public LivingEntity entity;
        public Spell(LivingEntity entity) { this.entity = entity; }

        public void cast(CastingEnvironment ctx) {
            ((OverrideCasterMinterface)ctx).override(entity);
        }
    }
}
