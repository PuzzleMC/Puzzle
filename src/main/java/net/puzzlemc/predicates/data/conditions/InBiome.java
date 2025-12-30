package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.data.BlockModelPredicate;

public class InBiome extends BlockModelPredicate {

    final Identifier biomeID;

    public InBiome(Identifier biomeID) {
        this.biomeID = biomeID;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, Identifier renderContext) {
        Level w = Minecraft.getInstance().level;
        assert w != null;
        return w.getBiome(pos).is(biomeID);
    }

    public static InBiome parse(JsonElement arg) {
        return new InBiome(Identifier.tryParse(arg.getAsString()));
    }
}
