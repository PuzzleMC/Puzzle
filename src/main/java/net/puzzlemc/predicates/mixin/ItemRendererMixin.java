package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.PuzzlePredicates;
import net.puzzlemc.predicates.common.ContextIDs;
import net.puzzlemc.predicates.util.PredicateModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

//? if < 1.21.5 {
//import net.minecraft.client.resources.model.BakedModel;
//?}

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    //? if < 1.21.4 {
//    @Unique private Level world;
//    @Unique private Entity entity;
//
//    @Shadow protected abstract void renderModelLists(BakedModel arg, ItemStack arg2, int j, int k, PoseStack arg3, VertexConsumer arg4);
//
//    @Inject(method = "getModel", at = @At(value = "HEAD"))
//    private void getVariablesForHeldItemModelRender(ItemStack stack, Level world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
//        this.world = world;
//        this.entity = entity;
//    }
    //?} else {
    //?}

    //? if < 1.21.4 {
//    @Redirect(method = "getModel", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;"))
//    private BakedModel getHeldItemModelMixin(ItemModelShaper itemModels, ItemStack stack) {
//        if (world == null || entity == null) return itemModels.getItemModel(stack);
//
//        BlockPos targetPos = entity.blockPosition();
//        if (entity instanceof Player player) {
//            HitResult hit = player.pick(8, Minecraft.getInstance().getTimer().getGameTimeDeltaTicks(), false);
//            if (hit instanceof BlockHitResult blockHitResult) {
//                targetPos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
//            }
//        }
//
//        Optional<Identifier> identifier = MBPData.meetsPredicate(world, targetPos, Block.byItem(stack.getItem()).defaultBlockState(), ContextIDs.ITEM_HELD);
//
//        if (identifier.isPresent()) {
//            return PredicateStore.reallyGetModel(identifier.get()).raw();
//        }
//
//        world = null;
//        entity = null;
//        return itemModels.getItemModel(stack);
//    }
    //?}

    //? if < 1.21.4 {
    //@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"))
    //private void renderBakedModelMixin(ItemRenderer instance, BakedModel model, ItemStack stack, int light, int overlay, PoseStack matrices, VertexConsumer vertices) {
//        if (world == null) world = Minecraft.getInstance().level;
//        if (PuzzlePredicates.contextPos == null) entity = Minecraft.getInstance().player;
//
//        BlockPos targetPos = entity != null ? entity.blockPosition() : PuzzlePredicates.contextPos;
//        if (entity instanceof Player player) {
//            HitResult hit = player.pick(8, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks(), false);
//            if (hit instanceof BlockHitResult blockHitResult) {
//                targetPos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
//            }
//        }
//
//        Optional<Identifier> identifier = MBPData.meetsPredicate(world, targetPos, Block.byItem(stack.getItem()).defaultBlockState(), ContextIDs.ITEM);
//        if (identifier.isPresent()) {
//            model = PredicateStore.reallyGetModel(identifier.get());
//        }
//        PuzzlePredicates.contextPos = null;
//
//        this.renderModelLists(model, stack, light, overlay, matrices, vertices);
//    }
    //?}
}
