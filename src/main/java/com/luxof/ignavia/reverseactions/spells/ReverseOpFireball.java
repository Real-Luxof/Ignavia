package com.luxof.ignavia.reverseactions.spells;

import java.util.List;

import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ReverseOpFireball extends SpellActionNCT {
    public int argc = 2;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {
        Vec3d pos = stack.getVec3InRange(0);
        double power = stack.getDoubleBetween(1, 0.0, 10.0);

        return new Result(
            new Spell(pos, power),
            (long)(MediaConstants.DUST_UNIT * 0.125 + MediaConstants.DUST_UNIT * power * 3),
            List.of(
                ParticleSpray.burst(pos, 7, 10 + (int)power * 5),
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
            for (int i = 0; i < power*power*power; i++) {
                BlockPos here = BlockPos.ofFloored(pos.add(
                    i % power,
                    Math.floor(i / power) % power,
                    Math.floor(i / (power*power))
                ));
                if (!ctx.canEditBlockAt(here))
                    continue;

                BlockState before = world.getBlockState(here);
                if (before.isOf(Blocks.CAULDRON)) {
                    world.setBlockState(here, Blocks.WATER_CAULDRON.getDefaultState());
                } else {
                    ((BucketItem)Items.WATER_BUCKET).placeFluid(
                        null,
                        world,
                        here,
                        null
                    );
                }
            }
        }
    }
}
