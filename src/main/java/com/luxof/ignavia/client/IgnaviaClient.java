package com.luxof.ignavia.client;

import com.luxof.ignavia.minterfaces.SoundBlockerMinterface;

import static com.luxof.ignavia.Ignavia.id;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;

public class IgnaviaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
            id("block_sound"),
            (client, networkHandler, buf, sender) -> {
                ((SoundBlockerMinterface)MinecraftClient.getInstance()).blockSoundForABit(
                    Registries.SOUND_EVENT.get(buf.readIdentifier())
                );
            }
        );
    }
}
