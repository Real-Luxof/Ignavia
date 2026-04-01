package com.luxof.ignavia.reverseactions.spells;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.mod.HexTags;

import com.luxof.ignavia.IgnaviaPersistentState;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

public class ReverseOpBreakBlock extends SpellActionNCT {
    public int argc = 1;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {
        BlockPos pos = stack.getBlockPosInRange(0);
        BlockState peep = IgnaviaPersistentState.get(world).popExploded(world, pos);

        return new Result(
            new Spell(pos, peep),
            peep == null
                ? 0L
                : peep.isIn(HexTags.Blocks.CHEAP_TO_BREAK_BLOCK)
                    ? 1250L
                    : 100L,
            List.of(
                ParticleSpray.burst(ctx.mishapSprayPos(), 3, 15),
                ParticleSpray.burst(pos.toCenterPos(), 5, 20)
            ),
            1
        );
    }

    public class Spell implements RenderedSpellNCT {
        public final BlockPos pos;
        @Nullable public final BlockState state;

        public Spell(BlockPos pos, @Nullable BlockState state) {
            this.pos = pos;
            this.state = state;
        }

        public void cast(CastingEnvironment ctx) {
            if (state == null) return;
            ctx.getWorld().setBlockState(pos, state);
        }
    }
}
