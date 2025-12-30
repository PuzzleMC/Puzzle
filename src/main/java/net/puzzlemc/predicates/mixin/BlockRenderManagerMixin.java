package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import net.puzzlemc.predicates.accessor.BlockRenderManagerAccess;
import net.puzzlemc.predicates.common.BlockRendering;
import net.puzzlemc.predicates.common.ContextIdentifiers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = BlockRenderDispatcher.class, priority = 2000)
public class BlockRenderManagerMixin implements BlockRenderManagerAccess {

    @Shadow @Final private BlockColors blockColors;
    @Shadow
    @Final
    private BlockModelShaper blockModelShaper;
    @Shadow
    @Final
    private ModelBlockRenderer modelRenderer;
    @Unique @Nullable private Entity figura$contextEntity;

    @Inject(at = @At("HEAD"), method = "renderBatched", cancellable = true)
    public void renderBlock(BlockState state, BlockPos pos, BlockAndTintGetter world, PoseStack matrices, VertexConsumer vertexConsumer, boolean cull, RandomSource random, CallbackInfo ci) {
        RenderShape blockRenderType = state.getRenderShape();
        if (blockRenderType == RenderShape.MODEL) {
            BakedModel newModel = BlockRendering.tryModelOverride(this.blockModelShaper, world, state, pos, ContextIdentifiers.MISC);
            if (newModel != null) {
                this.modelRenderer.tesselateBlock(world, newModel, state, pos, matrices, vertexConsumer, cull, random, state.getSeed(pos), OverlayTexture.NO_OVERLAY);
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "renderSingleBlock", cancellable = true)
    public void renderBlockAsEntity(BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (state.getRenderShape() == RenderShape.MODEL) {
            BlockPos pos = figura$contextEntity == null ? BlockPos.ZERO : figura$contextEntity.getOnPos();
            figura$contextEntity = null;
            Optional<ResourceLocation> id = MBPData.meetsPredicate(Minecraft.getInstance().level, pos, state, ContextIdentifiers.ENTITY);
            if (id.isEmpty()) return;

            BakedModel bakedModel = ((BakedModelManagerAccess) this.blockModelShaper.getModelManager()).reallyGetModel(id.get());
            int i = this.blockColors.getColor(state, null, null, 0);
            float f = (float) (i >> 16 & 0xFF) / 255.0F;
            float g = (float) (i >> 8 & 0xFF) / 255.0F;
            float h = (float) (i & 0xFF) / 255.0F;
            this.modelRenderer
                    .renderModel(
                            matrices.last(),
                            vertexConsumers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)),
                            state,
                            bakedModel,
                            f,
                            g,
                            h,
                            light,
                            overlay
                    );
            ci.cancel();
        }
    }

    @Override
    public void moreBlockPredicates$setContextEntity(Entity entity) {
        figura$contextEntity = entity;
    }
}
