package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.DataHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LightRange extends BlockModelPredicate {

    public final InclusiveRange<@NotNull Integer> range;

    public LightRange(InclusiveRange<@NotNull Integer> range) {
        this.range = range;
    }

    @Override
    public boolean meetsCondition(BlockGetter _unused, BlockPos pos, BlockState state, Identifier renderContext) {
        Level world = Minecraft.getInstance().level;

        // ;-;
        assert world != null;
        return range.isValueInRange(world.getRawBrightness(pos, 0))
                  || Arrays.stream(Direction.values()).anyMatch(
                          dir -> !world.getBlockState(pos.relative(dir)).isCollisionShapeFullBlock(world, pos.relative(dir))
                                  && range.isValueInRange(world.getRawBrightness(pos.relative(dir), 0)));
    }

    public static LightRange parse(JsonElement arg) {
        return new LightRange(DataHelper.parseIntRange(arg));
    }
}
