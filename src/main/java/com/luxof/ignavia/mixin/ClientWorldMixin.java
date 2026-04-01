package com.luxof.ignavia.mixin;

import com.luxof.ignavia.minterfaces.SoundBlockerMinterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(
        method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ignavia$fuckingDontPlayThatSound(
        double x, double y, double z,
        SoundEvent event, SoundCategory category,
        float volume, float pitch, boolean useDistance, long seed,
        CallbackInfo ci
    ) {
        ((SoundBlockerMinterface)MinecraftClient.getInstance()).getBlockedSounds().keySet()
            .forEach(sound -> { if (event == sound) ci.cancel(); });
    }
}
