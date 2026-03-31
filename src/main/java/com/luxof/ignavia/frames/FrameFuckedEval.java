package com.luxof.ignavia.frames;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.PatternShapeMatch;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidPattern;
import at.petrak.hexcasting.api.casting.mishaps.MishapUnenlightened;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.common.casting.PatternRegistryManifest;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import at.petrak.hexcasting.xplat.IXplatAbstractions;

import com.luxof.ignavia.ReverseActions;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;

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

    // :)
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

        CastingImage img = vm.getImage();
        Iota next = instrs.get(0);
        Supplier<@Nullable Text> actionName = () -> null;
        try {

            if (!next.executable()) {
                ArrayList<Iota> stack = new ArrayList<>(img.getStack());
                stack.add(next);
                img = img.copy(
                    stack,
                    img.getParenCount(),
                    img.getParenthesized(),
                    img.getEscapeNext(),
                    img.getOpsConsumed(),
                    img.getUserData()
                );
                return new CastResult(
                    next,
                    cont,
                    img,
                    List.of(),
                    ResolvedPatternType.ESCAPED,
                    HexEvalSounds.MISHAP // :)
                );

            } else if (next instanceof PatternIota patternIota) {
                CastingEnvironment ctx = vm.getEnv();
                HexPattern pattern = patternIota.getPattern();

                PatternShapeMatch match = PatternRegistryManifest.matchPattern(
                    pattern, ctx, false
                );
                ctx.precheckAction(match);
                if (
                    !(match instanceof PatternShapeMatch.Normal ||
                    match instanceof PatternShapeMatch.PerWorld)
                )
                    throw new MishapInvalidPattern(pattern);

                RegistryKey<ActionRegistryEntry> key;
                if (match instanceof PatternShapeMatch.Normal norm) key = norm.key;
                else if (match instanceof PatternShapeMatch.PerWorld pw) key = pw.key;
                else key = null; // stop complaining!

                boolean reqsEnlightenment = HexUtils.isOfTag(
                    IXplatAbstractions.INSTANCE.getActionRegistry(),
                    key,
                    HexTags.Actions.REQUIRES_ENLIGHTENMENT
                );
                actionName = () -> HexAPI.instance().getActionI18n(
                    key,
                    reqsEnlightenment
                );
                Action normalAction = IXplatAbstractions.INSTANCE.getActionRegistry().get(key).action();

                if (reqsEnlightenment && !ctx.isEnlightened())
                    throw new MishapUnenlightened();

                Action reverseAction = ReverseActions.getReversal(normalAction);
                if (reverseAction == null)
                    throw new MishapInvalidPattern(pattern);

                var operateResult = reverseAction.operate(ctx, img, newCont);

                return new CastResult(
                    next,
                    operateResult.getNewContinuation(),
                    operateResult.getNewImage(),
                    operateResult.getSideEffects(),
                    ResolvedPatternType.EVALUATED,
                    HexEvalSounds.MISHAP
                );

            } else {
                // :rolling_eyes:
                return next.execute(vm, world, cont);
            }

        } catch (Exception exception) {
            HexPattern pattern = next instanceof PatternIota patternIota
                ? patternIota.getPattern()
                : new HexPattern(HexDir.WEST, List.of());

            if (!(exception instanceof Mishap)) {
                exception.printStackTrace();
                return new CastResult(
                    next,
                    cont,
                    img,
                    List.of(
                        new OperatorSideEffect.DoMishap(
                            new MishapInternalException(exception),
                            new Mishap.Context(pattern, null)
                        )
                    ),
                    ResolvedPatternType.ERRORED,
                    HexEvalSounds.HERMES // :)
                );
            }

            return new CastResult(
                next,
                cont,
                null,
                List.of(
                    new OperatorSideEffect.DoMishap(
                        (Mishap)exception,
                        new Mishap.Context(
                            pattern,
                            actionName.get()
                        )
                    )
                ),
                ResolvedPatternType.ERRORED,
                HexEvalSounds.HERMES // :)
            );
        }
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
