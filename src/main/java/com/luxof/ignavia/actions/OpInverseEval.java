package com.luxof.ignavia.actions;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame;
import at.petrak.hexcasting.api.casting.eval.vm.FrameFinishEval;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;

import com.luxof.ignavia.frames.FrameFuckedEval;

import java.util.ArrayList;
import java.util.List;

public class OpInverseEval implements Action {

    @Override
    public OperationResult operate(CastingEnvironment ctx, CastingImage img, SpellContinuation cont) {
        List<Iota> stack = new ArrayList<>(img.getStack());
        if (stack.size() == 0)
            throw new MishapNotEnoughArgs(1, 0);

        SpellList instrSpellList = OperatorUtils.getList(stack, 0, 1);
        stack.remove(stack.size() - 1);

        SpellContinuation newCont = cont instanceof SpellContinuation.NotDone notDone
            && notDone.getFrame() instanceof FrameFinishEval
            ? cont
            : cont.pushFrame(FrameFinishEval.INSTANCE);

        ArrayList<Iota> instructions = new ArrayList<>();
        instrSpellList.forEach(instructions::add);
        ContinuationFrame frame = new FrameFuckedEval(instructions);
        CastingImage newImg = img.copy(
            stack,
            img.getParenCount(),
            img.getParenthesized(),
            img.getEscapeNext(),
            img.getOpsConsumed() + 1,
            img.getUserData()
        );

        return new OperationResult(
            newImg,
            List.of(),
            newCont.pushFrame(frame),
            HexEvalSounds.MISHAP // :)
        );
    }
}
