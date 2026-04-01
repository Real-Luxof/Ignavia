package com.luxof.ignavia.mixin;

import com.luxof.ignavia.minterfaces.SoundBlockerMinterface;

import java.util.HashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements SoundBlockerMinterface {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        for (SoundEvent sound : blockedSounds.keySet()) {
            blockedSounds.put(sound, blockedSounds.get(sound) - 1);
        }
    }

    public HashMap<SoundEvent, Integer> blockedSounds = new HashMap<>();
    @Override
    public void blockSoundForABit(SoundEvent sound) { blockedSounds.put(sound, 20*60); }
    
    @Override
    public HashMap<SoundEvent, Integer> getBlockedSounds() { return blockedSounds; }
}
