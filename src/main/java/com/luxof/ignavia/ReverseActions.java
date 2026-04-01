package com.luxof.ignavia;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntitiesBy;
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntityAt;
import at.petrak.hexcasting.common.lib.hex.HexActions;

import com.luxof.ignavia.reverseactions.*;
import com.luxof.ignavia.reverseactions.list.*;
import com.luxof.ignavia.reverseactions.math.*;
import com.luxof.ignavia.reverseactions.queryentity.*;
import com.luxof.ignavia.reverseactions.selectors.*;
import com.luxof.ignavia.reverseactions.spells.ReverseOpExplode;
import com.luxof.ignavia.reverseactions.stack.*;

import java.util.HashMap;

// can you register stuff at runtime? yeah.
// should that be stopped? probably.
// do i care? no.
public class ReverseActions {
    private static HashMap<Action, Action> mappings = new HashMap<>();
    private static HashMap<Action, Action> itsOppositeDay = new HashMap<>();

    public static Action register(Action action, Action correspondingReverse) {
        mappings.put(action, correspondingReverse);
        itsOppositeDay.put(correspondingReverse, action);
        return correspondingReverse;
    }
    public static ActionRegistryEntry register(ActionRegistryEntry action, ActionRegistryEntry correspondingReverse) {
        register(action.action(), correspondingReverse.action());
        return correspondingReverse;
    }
    public static Action getReversal(Action action) {
        return mappings.get(action);
    }
    public static Action getReversal(ActionRegistryEntry action) {
        return mappings.get(action.action());
    }
    public static Action getReverseOfReversal(Action reversal) {
        return itsOppositeDay.get(reversal);
    }
    public static Action getReverseOfReversal(ActionRegistryEntry reversal) {
        return itsOppositeDay.get(reversal.action());
    }

    public static void curseHex() {
            /// akashic
        register(HexActions.AKASHIC$READ, HexActions.AKASHIC$WRITE);
        register(HexActions.AKASHIC$WRITE, HexActions.AKASHIC$READ);
            /// circle
        register(HexActions.CIRCLE$BOUNDS$MAX, HexActions.CIRCLE$BOUNDS$MIN);
        register(HexActions.CIRCLE$IMPETUS_DIR, HexActions.CIRCLE$IMPETUS_POST);
        register(HexActions.CIRCLE$IMPETUS_POST, HexActions.CIRCLE$IMPETUS_DIR);
            /// eval
        register(HexActions.EVAL, Patterns.AERGIS);
        register(Patterns.AERGIS, HexActions.EVAL);
        // evalbreakable
        // halt
        // thanatos
            /// lists
        register(HexActions.APPEND, HexActions.UNAPPEND);
        register(HexActions.UNAPPEND, HexActions.APPEND);
        // concat
        register(HexActions.CONSTRUCT, HexActions.DECONSTRUCT);
        register(HexActions.DECONSTRUCT, HexActions.CONSTRUCT);
        register(HexActions.EMPTY_LIST, HexActions.SINGLETON);
        register(HexActions.SINGLETON, HexActions.EMPTY_LIST);
        register(HexActions.INDEX, HexActions.INDEX_OF);
        register(HexActions.INDEX_OF, HexActions.INDEX);
        register(HexActions.LAST_N_LIST, HexActions.SPLAT);
        register(HexActions.SPLAT, HexActions.LAST_N_LIST);
        // list size
        register(HexActions.REVERSE.action(), new ReverseOpReverski());
        register(HexActions.SLICE.action(), new ReverseOpSlice());
            /// local
        register(HexActions.READ$LOCAL, HexActions.READ);
        register(HexActions.READ, HexActions.READ$LOCAL);
        register(HexActions.WRITE$LOCAL, HexActions.WRITE);
        register(HexActions.WRITE, HexActions.WRITE$LOCAL);
        // the ravenmind is ALWAYS readable! :devious:
        register(HexActions.READABLE.action(), new ReverseOpIdjitThatAlwaysSaysYes());
        register(HexActions.WRITABLE.action(), new ReverseOpIdjitThatAlwaysSaysYes());
            /// math
        // abslen
        // i mean, if you think about it!
        // also see PE - MD:AS
        register(HexActions.ADD, HexActions.DIV_CROSS);
        register(HexActions.SUB, HexActions.MUL_DOT);
        register(HexActions.MUL_DOT, HexActions.SUB);
        register(HexActions.DIV_CROSS, HexActions.ADD);

        register(HexActions.CEIL, HexActions.FLOOR);
        register(HexActions.FLOOR, HexActions.CEIL);
        register(HexActions.COERCE_AXIAL.action(), new ReverseOpCoerceToAxial());
        register(HexActions.CONSTRUCT_VEC, HexActions.DECONSTRUCT_VEC);
        register(HexActions.DECONSTRUCT_VEC, HexActions.CONSTRUCT_VEC);
        register(HexActions.POW_PROJ, HexActions.LOGARITHM);
        register(HexActions.LOGARITHM, HexActions.POW_PROJ);
                /// trig
        register(HexActions.ARCCOS, HexActions.COS);
        register(HexActions.COS, HexActions.ARCCOS);
        register(HexActions.ARCSIN, HexActions.SIN);
        register(HexActions.SIN, HexActions.ARCSIN);
        register(HexActions.ARCTAN, HexActions.TAN);
        register(HexActions.TAN, HexActions.ARCTAN);
        register(HexActions.ARCTAN2, HexActions.ARCTAN); // i dunno.
                /// logic
        register(HexActions.AND, HexActions.XOR);
        register(HexActions.IF, HexActions.BOOL_COERCE); // remember when there was no augur exalt?
        register(HexActions.BOOL_COERCE, HexActions.IF);
        register(HexActions.OR, HexActions.AND);
        register(HexActions.AND, HexActions.OR);
        register(HexActions.GREATER, HexActions.LESS_EQ);
        register(HexActions.LESS_EQ, HexActions.GREATER);
        register(HexActions.LESS, HexActions.GREATER_EQ);
        register(HexActions.GREATER_EQ, HexActions.LESS);
        register(HexActions.EQUALS, HexActions.NOT_EQUALS);
        register(HexActions.NOT_EQUALS, HexActions.EQUALS);
        // not
        register(HexActions.UNIQUE.action(), new ReverseOpReverski()); // :troll:
            /// queryentity
        register(HexActions.FLIGHT$CAN_FLY.action(), new ReverseOpCanEntityHexFly());
        register(HexActions.ENTITY_HEIGHT.action(), new ReverseOpEntityHeight());
        register(HexActions.ENTITY_POS$EYE, HexActions.ENTITY_POS$FOOT);
        register(HexActions.ENTITY_POS$EYE, HexActions.ENTITY_POS$FOOT);
        register(HexActions.ENTITY_LOOK, HexActions.ENTITY_VELOCITY);
        register(HexActions.ENTITY_VELOCITY, HexActions.ENTITY_LOOK);
            /// raycast
        register(HexActions.RAYCAST_AXIS, HexActions.RAYCAST);
        register(HexActions.RAYCAST, HexActions.RAYCAST_ENTITY);
        register(HexActions.RAYCAST_ENTITY, HexActions.RAYCAST_AXIS);
            /// rw
        // :devious:
        register(HexActions.READ$ENTITY, HexActions.WRITABLE$ENTITY);
        register(HexActions.WRITE$ENTITY, HexActions.READABLE$ENTITY);
        register(HexActions.READABLE$ENTITY, HexActions.WRITE$ENTITY);
        register(HexActions.WRITABLE$ENTITY, HexActions.READ$ENTITY);
            /// selectors
        register(HexActions.GET_CASTER.action(), new ReverseOpGetCaster());
        register(HexActions.ZONE_ENTITY, HexActions.GET_ENTITY);
        register(HexActions.ZONE_ENTITY$ANIMAL.action(), new OpGetEntityAt(e -> !OpGetEntitiesBy.isAnimal(e)));
        register(HexActions.ZONE_ENTITY$MONSTER.action(), new OpGetEntityAt(e -> !OpGetEntitiesBy.isMonster(e)));
        register(HexActions.ZONE_ENTITY$ITEM.action(), new OpGetEntityAt(e -> !OpGetEntitiesBy.isItem(e)));
        register(HexActions.ZONE_ENTITY$PLAYER.action(), new OpGetEntityAt(e -> !OpGetEntitiesBy.isPlayer(e)));
        register(HexActions.ZONE_ENTITY$LIVING.action(), new OpGetEntityAt(e -> !OpGetEntitiesBy.isLiving(e)));
        register(HexActions.ZONE_ENTITY$NOT_ANIMAL, HexActions.GET_ENTITY$ANIMAL);
        register(HexActions.ZONE_ENTITY$NOT_MONSTER, HexActions.GET_ENTITY$MONSTER);
        register(HexActions.ZONE_ENTITY$NOT_ITEM, HexActions.GET_ENTITY$ITEM);
        register(HexActions.ZONE_ENTITY$NOT_PLAYER, HexActions.GET_ENTITY$PLAYER);
        register(HexActions.ZONE_ENTITY$NOT_LIVING, HexActions.GET_ENTITY$LIVING);
        register(HexActions.GET_ENTITY, HexActions.ZONE_ENTITY);
        register(HexActions.GET_ENTITY$ANIMAL, HexActions.ZONE_ENTITY$NOT_ANIMAL);
        register(HexActions.GET_ENTITY$MONSTER, HexActions.ZONE_ENTITY$NOT_MONSTER);
        register(HexActions.GET_ENTITY$ITEM, HexActions.ZONE_ENTITY$NOT_ITEM);
        register(HexActions.GET_ENTITY$PLAYER, HexActions.ZONE_ENTITY$NOT_PLAYER);
        register(HexActions.GET_ENTITY$LIVING, HexActions.ZONE_ENTITY$NOT_LIVING);
            /// stack
        register(HexActions.DUPLICATE_N.action(), new ReverseOpDuplicateN());
        register(HexActions.DUPLICATE.action(), new ReverseOpDuplicate(2));
        register(HexActions.TWO_DUP.action(), new ReverseOpDuplicate(4));
        register(HexActions.TUCK, HexActions.OVER);
        register(HexActions.OVER, HexActions.TUCK);
        register(HexActions.STACK_LEN.action(), new ReverseOpStackSize());
        register(HexActions.FISHERMAN.action(), new ReverseOpFisherman());
            /// spells
        register(HexActions.EXPLODE.action(), new ReverseOpExplode());
        // fireball
        register(HexActions.ADD_MOTION, HexActions.BLINK);
    }
}
