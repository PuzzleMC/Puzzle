package net.puzzlemc.predicates.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.MovingBlockRenderStateContext;
import net.puzzlemc.predicates.util.PredicateStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.feature.BlockFeatureRenderer;

import java.util.Optional;

@Mixin(BlockFeatureRenderer.class)
public class MixinBlockFeatureRenderer {
    /**
     * For Fabric, this is implemented in {@link net.puzzlemc.predicates.mixin.fabric.MixinIndigoRenderer}
     */
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/model/BlockStateModel;"), method = "render")
    public BlockStateModel puzzle$renderCustomMovingBlock(BlockRenderDispatcher instance, BlockState state, Operation<BlockStateModel> original, @Local MovingBlockRenderState movingBlock) {
        Optional<Identifier> identifier = MBPData.meetsPredicate(movingBlock.level, movingBlock.blockPos, state, MovingBlockRenderStateContext.of(movingBlock).puzzle$getContextId());

        return identifier.map(resourceLocation -> PredicateStore.reallyGetModel(resourceLocation).raw())
                .orElseGet(() -> original.call(instance, state));
    }
}
