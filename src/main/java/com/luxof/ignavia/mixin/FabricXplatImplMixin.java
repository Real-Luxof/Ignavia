package com.luxof.ignavia.mixin;

import at.petrak.hexcasting.common.msgs.IMessage;
import at.petrak.hexcasting.fabric.xplat.FabricXplatImpl;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.luxof.ignavia.minterfaces.ParticleBlockerMinterface;

@Mixin(FabricXplatImpl.class)
public class FabricXplatImplMixin {
    // i guess i'll fucking bald then
    @Inject(method = "sendPacketToPlayers", at = @At("HEAD"), cancellable = true)
    private void ignavia$DONTPARTICLE(
        Collection<ServerPlayerEntity> players,
        IMessage packet,
        CallbackInfo ci
    ) {
        var pkt = ServerPlayNetworking.createS2CPacket(packet.getFabricId(), packet.toBuf());
        for (var p : players) {
            if (!((ParticleBlockerMinterface)p.getServer()).isBlockedParticles(p.getUuid()))
                p.networkHandler.sendPacket(pkt);
        }
        ci.cancel(); // TIME
    }
}
