package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import net.puzzlemc.predicates.data.DataHelper;
import org.jetbrains.annotations.Nullable;

public class AdjacentBlock extends BlockModelPredicate {

    private final @Nullable IsBlockState stateCondition;
    private final @Nullable InBlockTag tagCondition;
    private final BlockPos offset;
    private final boolean checkFullCube;
    private final boolean checkTransparent;

    public AdjacentBlock(@Nullable IsBlockState stateCondition, @Nullable InBlockTag tagCondition, BlockPos offset, boolean checkFullCube, boolean checkTransparent) {
        this.stateCondition = stateCondition;
        this.tagCondition = tagCondition;
        this.offset = offset;
        this.checkFullCube = checkFullCube;
        this.checkTransparent = checkTransparent;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        BlockState block = world.getBlockState(pos);
        boolean b = true;
        if (checkFullCube) b = block.isCollisionShapeFullBlock(world, pos);
        if (checkTransparent) b &= block.propagatesSkylightDown(/*? if < 1.21.4 {*/ /*world, pos *//*?}*/);
        if (stateCondition != null) b &= stateCondition.meetsCondition(world, pos.offset(offset), state, renderContext);
        if (tagCondition != null) b&= tagCondition.meetsCondition(world, pos.offset(offset), state, renderContext);
        return b;
    }

    public static AdjacentBlock parse(JsonElement arg) {
        JsonObject obj = arg.getAsJsonObject();
        IsBlockState stateCondition = null;
        if (obj.has("state")) {
            stateCondition = IsBlockState.parse(obj.get("state"));
        }
        InBlockTag tagCondition = null;
        if (obj.has("tag")) {
            tagCondition = InBlockTag.parse(obj.get("tag"));
        }

        boolean checkFullCube = false;
        boolean checkTransparent = false;
        if (obj.has("is_full_cube")) checkFullCube = obj.get("is_full_cube").getAsBoolean();
        if (obj.has("is_transparent")) checkTransparent = obj.get("is_transparent").getAsBoolean();

        return new AdjacentBlock(stateCondition, tagCondition, DataHelper.parseBlockPos(obj.getAsJsonObject("offset")), checkFullCube, checkTransparent);
    }
}
