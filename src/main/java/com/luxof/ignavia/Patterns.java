package com.luxof.ignavia;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;

import static com.luxof.ignavia.Ignavia.id;

import com.luxof.ignavia.actions.OpInverseEval;

import net.minecraft.registry.Registry;

public class Patterns {
    public static ActionRegistryEntry AERGIS = Registry.register(HexActions.REGISTRY, id("inverse_eval"), new ActionRegistryEntry(HexPattern.fromAngles("deaqqeedqa", HexDir.SOUTH_EAST), new OpInverseEval()));

    public static void ezilaitini() {}
}
