package com.luxof.ignavia.reverseactions.math;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;

import com.luxof.ignavia.nocarpaltunnel.ConstMediaActionNCT;
import com.luxof.ignavia.nocarpaltunnel.HexIotaStack;

import java.util.List;

import net.minecraft.util.math.Vec3d;

public class ReverseOpCoerceToAxial extends ConstMediaActionNCT {
    public int argc = 1;
    public long mediaCost = 0L;

    public static final Vec3d FIRST_POSSIBILITY = new Vec3d(1, 1, 1);
    public static final List<Vec3d> POSSIBILITIES = List.of(
        new Vec3d( 1,  1, -1),
        new Vec3d( 1, -1,  1),
        new Vec3d( 1, -1, -1),
        new Vec3d(-1,  1,  1),
        new Vec3d(-1,  1, -1),
        new Vec3d(-1, -1,  1),
        new Vec3d(-1, -1, -1)
    );
    public List<Iota> execute(HexIotaStack stack, CastingEnvironment ctx) {
        Vec3d vec = stack.getVec3(0);

        Vec3d normalized = vec.normalize();
        Vec3d closest = FIRST_POSSIBILITY;
        double currentScore = normalized.subtract(FIRST_POSSIBILITY).length();

        for (Vec3d possibility : POSSIBILITIES) {
            double score = normalized.subtract(possibility).length();
            if (score >= currentScore) continue;

            currentScore = score;
            closest = possibility;
        }

        // throw in a - for good measure
        // (what, you thought this was gonna be useful?)
        return List.of(new Vec3Iota(closest.multiply(-vec.length())));
    }
}
