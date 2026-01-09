package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import org.jetbrains.annotations.NotNull;

public class InBlockTag extends BlockModelPredicate {
    final TagKey<@NotNull Block> tagKey;

    public InBlockTag(Identifier tagId) {
        this.tagKey = TagKey.create(Registries.BLOCK, tagId);
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        return world.getBlockState(pos).is(tagKey);
    }

    public static InBlockTag parse(JsonElement arg) {
        return new InBlockTag(Identifier.tryParse(arg.getAsString()));
    }
}
