package com.luxof.ignavia.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import com.luxof.ignavia.IgnaviaPersistentState;
import com.luxof.ignavia.minterfaces.ExplosionMinterface;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Explosion.class)
public class ExplosionMixin implements ExplosionMinterface {

    @Shadow
    @Final
    private World world;
    @Shadow
    @Final
    private ObjectArrayList<BlockPos> affectedBlocks;

    private boolean crazyDiamond = false;
    public void makeCrazyDiamond() { crazyDiamond = true; }

    @Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
    public void ignavia$trackExplodedBlocks(boolean particles, CallbackInfo ci) {
        if (this.world.isClient) return;
        ServerWorld world = (ServerWorld)this.world;
        if (!crazyDiamond) return;
        IgnaviaPersistentState persistentState = IgnaviaPersistentState.get(world);

        affectedBlocks.forEach(
            pos -> {
                BlockState prev = persistentState.popExploded(world, pos);
                if (prev != null)
                    world.setBlockState(pos, prev);
            }
        );

        ci.cancel();
    }

    @Inject(
        method = "affectWorld",
        at = @At(
            value = "INVOKE",
            // net.minecraft.util.math.BlockPos.toImmutable()
            target = "Lnet/minecraft/util/math/BlockPos;toImmutable()Lnet/minecraft/util/math/BlockPos;",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void trackExplodedBlocks(
        boolean particles,
        CallbackInfo ci,
        @Local BlockPos blockPos,
        @Local BlockState blockState
    ) {
        if (this.world.isClient) return;
        ServerWorld world = (ServerWorld)this.world;

        // point is only reached if this is false due to ci.cancel() but whatever why not
        if (crazyDiamond) return;

        IgnaviaPersistentState persistentState = IgnaviaPersistentState.get(world);
        persistentState.set(world, blockPos, blockState);
    }
}
