package com.luxof.ignavia.reverseactions.stack;

import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;

import java.util.ArrayList;
import java.util.List;

public class ReverseOpStackSize implements Action {

    @Override
    public OperationResult operate(CastingEnvironment ctx, CastingImage img, SpellContinuation cont) {
        ArrayList<Iota> stack = new ArrayList<>(img.getStack());

        int totalSize = 0;
        for (Iota iota : stack) { totalSize += iota.size(); }

        stack.add(new DoubleIota(1024 - totalSize));

        return new OperationResult(
            img.withUsedOp().copy(
                stack,
                img.getParenCount(),
                img.getParenthesized(),
                img.getEscapeNext(),
                img.getOpsConsumed(),
                img.getUserData()
            ),
            List.of(),
            cont,
            HexEvalSounds.MISHAP // :)
        );
    }
}
