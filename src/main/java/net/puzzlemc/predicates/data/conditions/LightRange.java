package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.DataHelper;

public class LightRange extends BlockModelPredicate {

    public final InclusiveRange<Integer> range;

    public LightRange(InclusiveRange<Integer> range) {
        this.range = range;
    }

    @Override
    public boolean meetsCondition(BlockGetter _unused, BlockPos pos, BlockState state, Identifier renderContext) {
        Level world = Minecraft.getInstance().level;

        // ;-;
        assert world != null;
        return range.isValueInRange(world.getLightEmission(pos))
             || range.isValueInRange(world.getLightEmission(pos.above()))
             || range.isValueInRange(world.getLightEmission(pos.below()))
             || range.isValueInRange(world.getLightEmission(pos.north()))
             || range.isValueInRange(world.getLightEmission(pos.south()))
             || range.isValueInRange(world.getLightEmission(pos.east()))
             || range.isValueInRange(world.getLightEmission(pos.west()));
    }

    public static LightRange parse(JsonElement arg) {
        return new LightRange(DataHelper.parseIntRange(arg));
    }
}
