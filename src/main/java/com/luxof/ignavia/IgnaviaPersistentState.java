package com.luxof.ignavia;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import org.jetbrains.annotations.Nullable;

public class IgnaviaPersistentState extends PersistentState {
    // surely this won't have any performance implications :clueless:
    public HashMap<Identifier, HashMap<BlockPos, BlockState>> exploded;
    public boolean inDesperateNeedOfYuriComics = false;

    // oh god
    private <E extends Enum<E> & StringIdentifiable> EnumProperty<E> evil(
        BlockState state,
        EnumProperty<E> prop,
        String name
    ) {
        state = state.with(
            prop,
            Enum.valueOf(prop.getType(), name)
        );
        return prop;
    }

    private BlockState readProperties(NbtCompound nbt, Block block) {

        BlockState state = block.getDefaultState();
        Collection<Property<?>> properties = state.getProperties();

        for (Property<?> property : properties) {
            String propName = property.getName();

            if (property instanceof IntProperty intProp)
                state = state.with(intProp, nbt.getInt(propName));

            else if (property instanceof BooleanProperty boolProp)
                state = state.with(boolProp, nbt.getBoolean(propName));

            else if (property instanceof EnumProperty<?> enumProp)
                evil(state, enumProp, propName);

        }

        return state;
    }

    private HashMap<BlockPos, BlockState> readDimension(NbtCompound dimension) {
        HashMap<BlockPos, BlockState> exploded = new HashMap<>();
        int entries = dimension.getInt("entries");

        for (int i = 0; i < entries; i++) {
            NbtCompound entry = dimension.getCompound(String.valueOf(i));

            exploded.put(
                new BlockPos(entry.getInt("x"), entry.getInt("y"), entry.getInt("z")),
                readProperties(
                    entry,
                    Registries.BLOCK.get(new Identifier(entry.getString("block")))
                )
            );
        }

        return exploded;
    }

    public IgnaviaPersistentState() {
        exploded = new HashMap<>();
    }
    public IgnaviaPersistentState(NbtCompound nbt) {
        this();

        nbt.getKeys().forEach(
            key -> exploded.put(new Identifier(key), readDimension(nbt.getCompound(key)))
        );
    }

    // DOMAIN EXPANSION: STUPENDIUM OF INFINITE DOORS
    private <ENUM extends Enum<?> & StringIdentifiable> NbtCompound writeBlockStateProperties(
        BlockState state
    ) {
        Collection<Property<?>> properties = state.getProperties();
        NbtCompound nbt = new NbtCompound();

        for (Property<?> property : properties) {
            String propName = property.getName();

            if (property instanceof IntProperty intProp)
                nbt.putInt(propName, state.get(intProp));

            else if (property instanceof BooleanProperty boolProp)
                nbt.putBoolean(propName, state.get(boolProp));

            else if (property instanceof EnumProperty<?> enumProp) {
                nbt.putString(
                    propName,
                    enumProp.getType().cast(state.get(property)).name()
                );
            }
        }

        return nbt;
    }

    private NbtCompound writeDimension(HashMap<BlockPos, BlockState> dimension) {
        NbtCompound nbt = new NbtCompound();

        int i = 0;
        for (var entry : dimension.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            NbtCompound posNbt = new NbtCompound();
            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());
            posNbt.putString("block", Registries.BLOCK.getId(state.getBlock()).toString());
            posNbt.put("properties", writeBlockStateProperties(state));

            nbt.put(String.valueOf(i), posNbt);
            i++;
        }

        nbt.putInt("entries", i);
        return nbt;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        exploded.forEach(
            (id, dimension) -> nbt.put(id.toString(), writeDimension(dimension))
        );
        return nbt;
    }

    public static IgnaviaPersistentState get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(
            IgnaviaPersistentState::new,
            IgnaviaPersistentState::new,
            "ignavia_explosions"
        );
    }

    @Nullable
    public BlockState popExploded(ServerWorld world, BlockPos pos) {
        var dimension = exploded.get(world.getDimensionKey().getValue());
        if (dimension == null) return null;
        inDesperateNeedOfYuriComics = true;
        return dimension.remove(pos);
    }

    @Nullable
    public BlockState peepExploded(ServerWorld world, BlockPos pos) {
        var dimension = exploded.get(world.getDimensionKey().getValue());
        if (dimension == null) return null;
        return dimension.get(pos);
    }

    public void set(ServerWorld world, BlockPos pos, BlockState state) {
        Identifier id = world.getDimensionKey().getValue();
        if (!exploded.containsKey(id))
            exploded.put(id, new HashMap<>());
        var dimension = exploded.get(id);
        dimension.put(pos, state);
        inDesperateNeedOfYuriComics = true;
    }
}
