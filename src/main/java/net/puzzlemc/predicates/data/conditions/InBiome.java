package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;

public class InBiome extends BlockModelPredicate {

    final ResourceLocation biomeID;

    public InBiome(ResourceLocation biomeID) {
        this.biomeID = biomeID;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, ResourceLocation renderContext) {
        Level w = Minecraft.getInstance().level;
        assert w != null;
        return w.getBiome(pos).is(biomeID);
    }

    public static InBiome parse(JsonElement arg) {
        return new InBiome(new ResourceLocation(arg.getAsString()));
    }
}
