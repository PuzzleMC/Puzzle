package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import net.puzzlemc.predicates.common.ContextIDs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PistonHeadRenderer.class)
public class PistonBlockEntityRendererMixin {

//    @Unique private BlockPos tempBlockPos;
//    @Unique private Level tempWorld;
//
//    @Inject(at = @At("HEAD"), method = "renderBlock")
//    public void renderModel(BlockPos pos, BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, Level world, boolean cull, int overlay, CallbackInfo ci) {
//        this.tempBlockPos = pos;
//        this.tempWorld = world;
//    }

//    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"), method = "renderBlock")
//    public BakedModel renderModel(BlockRenderDispatcher instance, BlockState state) {
//        Optional<Identifier> identifier = MBPData.meetsPredicate(tempWorld, tempBlockPos, state, ContextIDs.PISTON_PUSHING);
//
//        Minecraft client = Minecraft.getInstance();
//        if (identifier.isPresent()) {
//            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getModelManager());
//            return access.reallyGetModel(identifier.get());
//        } else {
//            return client.getBlockRenderer().getBlockModelShaper().getBlockModel(state);
//        }
//    }

}
