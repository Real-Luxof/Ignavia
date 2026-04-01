package com.luxof.ignavia.minterfaces;

import java.util.HashMap;

import net.minecraft.sound.SoundEvent;

public interface SoundBlockerMinterface {
    public void blockSoundForABit(SoundEvent sound);
    public HashMap<SoundEvent, Integer> getBlockedSounds();
}
