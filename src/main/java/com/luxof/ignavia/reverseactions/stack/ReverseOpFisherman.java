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

public class ReverseOpFisherman implements Action {

    @Override
    public OperationResult operate(CastingEnvironment ctx, CastingImage img, SpellContinuation cont) {
        ArrayList<Iota> stack = new ArrayList<>(img.getStack());
        if (stack.size() < 1)
            throw new MishapNotEnoughArgs(1, 0);

        int index = OperatorUtils.getInt(stack, 0, 1);
        index = index < 0 ? stack.size() - index : index;

        if (stack.size() <= index)
            throw new MishapNotEnoughArgs(index + 1, stack.size());

        return new OperationResult(
            img.copy(
                List.of(stack.get(index)),
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
