package com.luxof.ignavia.reverseactions.queryentity;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

import net.minecraft.server.network.ServerPlayerEntity;

public class ReverseOpCanEntityHexFly extends ConstMediaActionNCT {
    public int argc = 1;
    public long mediaCost = 0L;

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        ServerPlayerEntity player = stack.getPlayer(0);
        ctx.assertEntityInRange(player);

        return List.of(new BooleanIota(player.isFallFlying())); // ??? why is it named that
    }
}
