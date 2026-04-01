package com.luxof.ignavia.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;

import com.luxof.ignavia.IgnaviaPersistentState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.BlockPos;

@Mixin(
    targets = {"at.petrak.hexcasting.common.casting.actions.spells.OpBreakBlock$Spell"},
    remap = false
)
public abstract class OpBreakBlock$SpellMixin {
    @Shadow
    @Final
    public abstract BlockPos getPos();

    @Inject(method = "cast", at = @At("HEAD"))
    public void cast(CastingEnvironment ctx, CallbackInfo ci) {
        IgnaviaPersistentState persistentState = IgnaviaPersistentState.get(ctx.getWorld());
        persistentState.set(
            ctx.getWorld(),
            getPos(),
            ctx.getWorld().getBlockState(getPos())
        );
    }
}
