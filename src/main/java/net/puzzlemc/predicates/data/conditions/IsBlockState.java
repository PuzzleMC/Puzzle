package net.puzzlemc.predicates.data.conditions;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.puzzlemc.predicates.data.BlockModelPredicate;
import org.jetbrains.annotations.Nullable;

public class IsBlockState extends BlockModelPredicate {

    final @Nullable BlockPredicateImpl blockStatePredicate;
    public IsBlockState(@Nullable BlockPredicateImpl blockStatePredicate) {
        this.blockStatePredicate = blockStatePredicate;
    }

    @Override
    public boolean meetsCondition(BlockGetter world, BlockPos pos, BlockState state, ResourceLocation renderContext) {
        if (blockStatePredicate == null) return true;
        return blockStatePredicate.test(new BlockInWorld(Minecraft.getInstance().level, pos, false));
    }

    public static IsBlockState parse(JsonElement arg) {
        try {
            String str = arg.getAsString();

            if (str != null) {
                return new IsBlockState(new BlockPredicateImpl(str));
            }

            return null; // explode
        } catch (CommandSyntaxException e) {
           throw new RuntimeException(e);
        }
    }

    public static class BlockPredicateImpl implements BlockPredicateArgument.Result {
        final BlockStateParser.BlockResult res;
        public final String stateString;
        public final boolean fuzzy;

        public BlockPredicateImpl(String stateString) throws CommandSyntaxException {
            this.stateString = stateString;
            fuzzy = !stateString.contains("[");
            res = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), stateString, false);
        }

        @Override
        public boolean requiresNbt() {
            return false;
        }

        @Override
        public boolean test(BlockInWorld cachedBlockPosition) {
            if (fuzzy) return cachedBlockPosition.getState().getBlock() == res.blockState().getBlock();
            return cachedBlockPosition.getState().equals(res.blockState());
        }
    }


}
