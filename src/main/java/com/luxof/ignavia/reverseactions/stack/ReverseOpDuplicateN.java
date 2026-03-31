package com.luxof.ignavia.reverseactions.stack;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;

import java.util.ArrayList;
import java.util.List;

public class ReverseOpDuplicateN implements Action {

    @Override
    public OperationResult operate(CastingEnvironment ctx, CastingImage img, SpellContinuation cont) {
        ArrayList<Iota> stack = new ArrayList<>(img.getStack());
        if (stack.size() < 1)
            throw new MishapNotEnoughArgs(1, 0);

        int amount = OperatorUtils.getPositiveInt(stack, 0, 1);
        stack.remove(stack.size() - 1);

        if (stack.size() < amount)
            throw new MishapNotEnoughArgs(amount + 1, stack.size());

        for (int i = 0; i < amount; i++) { stack.remove(stack.size() - 1); }
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
