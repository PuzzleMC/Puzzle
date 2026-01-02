package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.DataHelper;
import org.jetbrains.annotations.NotNull;

public class CoordinateRange extends BlockModelPredicate {

    public final InclusiveRange<@NotNull Integer> range;
    public final Direction.Axis axis;

    public CoordinateRange(InclusiveRange<@NotNull Integer> range, Direction.Axis axis) {
        this.range = range;
        this.axis = axis;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        return range.isValueInRange(pos.get(this.axis));
    }

    public static CoordinateRange parse(JsonElement arg) {
        JsonObject object = arg.getAsJsonObject();

        Direction.Axis axis = Direction.Axis.byName(object.get("axis").getAsString());
        return new CoordinateRange(DataHelper.parseIntRange(arg), axis);
    }
}
