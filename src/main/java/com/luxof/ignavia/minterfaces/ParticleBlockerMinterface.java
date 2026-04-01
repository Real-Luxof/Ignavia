package com.luxof.ignavia.minterfaces;

import java.util.UUID;

public interface ParticleBlockerMinterface {
    public void blockOutParticles(UUID uuid);
    public boolean isBlockedParticles(UUID uuid);
}
