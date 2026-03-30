package com.luxof.ignavia.frames;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;

public class FrameFuckedEval implements ContinuationFrame {
    private final ArrayList<Iota> instrs;

    public FrameFuckedEval(ArrayList<Iota> instructions) {
        this.instrs = instructions;
    }

    @Override
    public Pair<Boolean, List<Iota>> breakDownwards(List<? extends Iota> stack) {
        // mf
        return new Pair<>(false, stack.stream().map(iota -> (Iota)iota).toList());
    }

    @Override
    public CastResult evaluate(SpellContinuation cont, ServerWorld world, CastingVM vm) {
        if (instrs.isEmpty())
            return new CastResult(
                new ListIota(instrs),
                cont,
                vm.getImage(),
                List.of(),
                ResolvedPatternType.EVALUATED,
                HexEvalSounds.MISHAP // :)
            );

        SpellContinuation newCont = cont.pushFrame(
            new FrameFuckedEval(new ArrayList<>(instrs.subList(1, instrs.size())))
        );

        CastResult result = vm.executeInner(instrs.get(0), world, newCont);

        return result.copy(
            result.getCast(),
            result.getContinuation(),
            result.getNewData(),
            result.getSideEffects(),
            result.getResolutionType(),
            HexEvalSounds.MISHAP // :)
        );
    }

    @Override
    public NbtCompound serializeToNBT() {
        NbtCompound nbt = new NbtCompound();
        NbtList list = new NbtList();
        instrs.forEach(iota -> list.add(IotaType.serialize(iota)));
        nbt.put("instrs", list);
        return nbt;
    }

    @Override public Type<?> getType() { return TYPE; }
    @Override public int size() { return instrs.size(); }

    public ContinuationFrame.Type<?> TYPE = new ContinuationFrame.Type<FrameFuckedEval>() {
        @Override
        public FrameFuckedEval deserializeFromNBT(NbtCompound nbt, ServerWorld world) {

            List<Iota> instrs = nbt.getList("instrs", NbtElement.COMPOUND_TYPE).stream()
                .map(iota -> IotaType.deserialize((NbtCompound)iota, world))
                .toList();

            return new FrameFuckedEval(
                new ArrayList<>(instrs)
            );
        }
    };
}
