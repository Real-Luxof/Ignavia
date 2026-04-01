package com.luxof.ignavia.mixin;

import com.luxof.ignavia.IgnaviaPersistentState;
import com.luxof.ignavia.minterfaces.ParticleBlockerMinterface;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements ParticleBlockerMinterface {

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
        particlesBlocked.entrySet().forEach(
            entry -> particlesBlocked.put(entry.getKey(), entry.getValue() - 1)
        );
    }

    public HashMap<UUID, Integer> particlesBlocked = new HashMap<>();
    @Override
    public void blockOutParticles(UUID uuid) {
        particlesBlocked.put(uuid, 20*60);
    }

    @Override
    public boolean isBlockedParticles(UUID uuid) { return particlesBlocked.containsKey(uuid); }
}
