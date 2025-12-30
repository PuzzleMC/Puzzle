package net.puzzlemc.predicates.common;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import net.puzzlemc.predicates.util.PredicateModel;
import net.puzzlemc.predicates.util.PredicateStore;

import java.util.Optional;

public class BlockRendering {

    public static Optional<PredicateModel> tryModelOverride(BlockModelShaper models, BlockAndTintGetter world, BlockState state, BlockPos pos, Identifier renderContext) {
        RenderShape blockRenderType = state.getRenderShape();
        if (blockRenderType == RenderShape.MODEL) {
            Optional<Identifier> override = MBPData.meetsPredicate(world, pos, state, renderContext);
            if (override.isPresent()) {
                PredicateModel model;
//                BakedModelManagerAccess manager = ((BakedModelManagerAccess) models.getModelManager());
                model = PredicateStore.reallyGetModel(override.get());
//                if (model == PredicateModel.MISSING) {
//                    var overId = override.get();
////                    model = models.getModelManager().getModel(new ModelIdentifier(
//                            /*? if >= 1.21.1 {*/ Identifier.fromNamespaceAndPath(/*?}*/
//                            overId.getNamespace(), overId.getPath()
//                            /*? if >= 1.21.1 {*/)/*?}*/
//                            , "standalone"));

                return Optional.ofNullable(model);
            }
        }
        return Optional.empty();
    }

}
