package com.luxof.ignavia.reverseactions.spells;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.misc.MediaConstants;

import com.luxof.ignavia.minterfaces.ExplosionMinterface;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import java.util.List;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class ReverseOpExplode extends SpellActionNCT {
    public int argc = 2;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {
        Vec3d pos = stack.getVec3InRange(0);
        double power = stack.getDoubleBetween(1, 0.0, 10.0);

        return new Result(
            new Spell(pos, power),
            (long)(MediaConstants.DUST_UNIT * 0.125 + MediaConstants.DUST_UNIT * power * 3),
            List.of(
                ParticleSpray.burst(pos, 5, 10 + (int)power * 2),
                ParticleSpray.burst(ctx.mishapSprayPos(), 5, 20)
            ),
            1
        );
    }

    public class Spell implements RenderedSpellNCT {
        Vec3d pos;
        double power;
        public Spell(Vec3d pos, double power) { this.pos = pos; this.power = power; }

        public void cast(CastingEnvironment ctx) {
            // manually reimplement World.createExplosion because.
            Explosion.DestructionType destructionType = world.getGameRules()
                .getBoolean(GameRules.TNT_EXPLOSION_DROP_DECAY)
                ? DestructionType.DESTROY_WITH_DECAY
                : DestructionType.DESTROY;

            Explosion explosion = new Explosion(
                (World)world,
                ctx.getCastingEntity(),
                null,
                null,
                pos.x,
                pos.y,
                pos.z,
                (float)power,
                false,
                destructionType
            );
            ((ExplosionMinterface)explosion).makeCrazyDiamond();
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);
        }
    }
}
