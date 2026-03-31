package com.luxof.ignavia.reverseactions.selectors;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

// get the farthest non-player entity instead :)
public class ReverseOpGetCaster extends ConstMediaActionNCT {
    public int argc = 0;
    public long mediaCost = 0L;

    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        Box box = new Box(
            ctx.mishapSprayPos().subtract(32.0, 32.0, 32.0),
            ctx.mishapSprayPos().add(32.0, 32.0, 32.0)
        );
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box, entity -> true);
        double distanceToClosest = 0.0;
        Entity closest = null;
        for (Entity entity : entities) {
            if (
                (closest != null &&
                closest.squaredDistanceTo(entity) <= distanceToClosest) ||
                entity instanceof ServerPlayerEntity ||
                !ctx.isEntityInRange(entity)
            ) continue;

            closest = entity;
            distanceToClosest = closest.squaredDistanceTo(entity);
        }

        return List.of(closest == null ? new NullIota() : new EntityIota(closest));
    }
}
