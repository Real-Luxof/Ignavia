package com.luxof.ignavia.reverseactions.spells;

import java.util.List;

import com.luxof.ignavia.minterfaces.ParticleBlockerMinterface;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT;
import com.luxof.ignavia.nocarpaltunnel.SpellActionNCT.RenderedSpellNCT;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

public class ReverseOpColorize extends SpellActionNCT {
    public int argc = 0;

    public Result execute(HexIotaStack stack, CastingEnvironment ctx) {

        return new Result(
            new Spell(),
            MediaConstants.CRYSTAL_UNIT * 2L,
            List.of(
                ParticleSpray.cloud(ctx.mishapSprayPos(), 5, 45)
            ),
            1L
        );
    }

    public class Spell implements RenderedSpellNCT {

        public void cast(CastingEnvironment ctx) {
            ((ParticleBlockerMinterface)ctx.getWorld().getServer())
                .blockOutParticles(ctx.getCastingEntity().getUuid());
        }
    }
}
