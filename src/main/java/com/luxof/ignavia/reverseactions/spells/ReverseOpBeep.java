package com.luxof.ignavia.reverseactions.spells;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.misc.MediaConstants;

import static com.luxof.ignavia.Ignavia.id;

import java.util.List;

import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

public class ReverseOpBeep extends SpellActionNCT {
    public int argc = 0;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {
        if (!(ctx.getCastingEntity() instanceof ServerPlayerEntity))
            throw new MishapBadCaster();
        SoundEvent[] sounds = Registries.SOUND_EVENT.getIds().toArray(new SoundEvent[0]);
        SoundEvent sound = sounds[(int)(Math.random() * sounds.length)];

        return new Result(
            new Spell(sound),
            MediaConstants.CRYSTAL_UNIT * 2L,
            List.of(
                ParticleSpray.cloud(ctx.mishapSprayPos(), 5, 45)
            ),
            1L
        );
    }

    public class Spell implements RenderedSpellNCT {
        public final SoundEvent sound;
        public Spell(SoundEvent sound) {
            this.sound = sound;
        }

        public void cast(CastingEnvironment ctx) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(sound.getId());
            ServerPlayNetworking.send(
                ((ServerPlayerEntity)ctx.getCastingEntity()),
                id("block_sound"),
                buf
            );
        }
    }
}
