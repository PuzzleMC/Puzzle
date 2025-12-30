package net.puzzlemc.predicates.common;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;

import java.util.Optional;

public class BlockRendering {

    public static BakedModel tryModelOverride(BlockModelShaper models, BlockAndTintGetter world, BlockState state, BlockPos pos, ResourceLocation renderContext) {
        RenderShape blockRenderType = state.getRenderShape();
        if (blockRenderType == RenderShape.MODEL) {
            Optional<ResourceLocation> override = MBPData.meetsPredicate(world, pos, state, renderContext);
            if (override.isPresent()) {
                BakedModel model;
                BakedModelManagerAccess manager = ((BakedModelManagerAccess) models.getModelManager());
                model = manager.reallyGetModel(override.get());
                if (model == models.getModelManager().getMissingModel()) {
                    var overId = override.get();
                    model = models.getModelManager().getModel(new ModelResourceLocation(overId.getNamespace(), overId.getPath(), ""));
                }
                return model;
            }
        }
        return null;
    }

}
