package com.luxof.ignavia.mixin;

import com.luxof.ignavia.IgnaviaPersistentState;

import java.util.function.BooleanSupplier;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    // PARANOIA REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    @Inject(method = "tick", at = @At("HEAD"))
    private void ignavia$markExplosionsStateDirtyIfRequired(
        BooleanSupplier shouldKeepTicking,
        CallbackInfo ci
    ) {
        IgnaviaPersistentState persistentState = IgnaviaPersistentState.get(
            ((MinecraftServer)(Object)this).getOverworld()
        );
        if (persistentState.inDesperateNeedOfYuriComics) {
            persistentState.markDirty();
            persistentState.inDesperateNeedOfYuriComics = false;
        }
    }
}
