package com.luxof.ignavia.mixin;

import com.luxof.ignavia.minterfaces.OverrideCasterMinterface;

import net.minecraft.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    targets = {
        "at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv",
        "at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv"
    },
    remap = false
)
public class OverrideCastersMixin implements OverrideCasterMinterface {
    protected LivingEntity trueCaster = null;

    @Inject(method = "getCastingEntity", at = @At("HEAD"), cancellable = true)
    private void ignavia$overrideTheCaster(CallbackInfoReturnable<LivingEntity> cir) {
        if (trueCaster != null)
            cir.setReturnValue(trueCaster);
    }

    public void override(LivingEntity entity) {
        this.trueCaster = entity;
    }
}
