package net.puzzlemc.predicates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import net.puzzlemc.predicates.client.PuzzlePredicates;
import net.puzzlemc.predicates.common.ContextIdentifiers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Shadow
    protected abstract void renderModelLists(BakedModel arg, ItemStack arg2, int j, int k, PoseStack arg3, VertexConsumer arg4);

    @Unique private Level world;
    @Unique private Entity entity;

    @Inject(method = "getModel", at = @At(value = "HEAD"))
    private void getHeldItemModelVariableStealerLol(ItemStack stack, Level world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        this.world = world;
        this.entity = entity;
    }


    @Redirect(method = "getModel", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;"))
    private BakedModel getHeldItemModelMixin(ItemModelShaper itemModels, ItemStack stack) {
        if (world == null || entity == null) return itemModels.getItemModel(stack);

        BlockPos targetPos = entity.blockPosition();
        if (entity instanceof Player player) {
            HitResult hit = player.pick(8, Minecraft.getInstance().getDeltaFrameTime(), false);
            if (hit instanceof BlockHitResult blockHitResult) {
                targetPos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
            }
        }

        Optional<ResourceLocation> identifier = MBPData.meetsPredicate(world, targetPos, Block.byItem(stack.getItem()).defaultBlockState(), ContextIdentifiers.ITEM_HELD);

        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(itemModels.getModelManager());
            return access.reallyGetModel(identifier.get());
        }

        world = null;
        entity = null;
        return itemModels.getItemModel(stack);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"
            )
    )
    private void renderBakedModelMixin(ItemRenderer instance, BakedModel model, ItemStack stack, int light, int overlay, PoseStack matrices, VertexConsumer vertices) {
        if (world == null) world = Minecraft.getInstance().level;
        if (entity == null) entity = PuzzlePredicates.currentEntity;
        if (entity == null) entity = Minecraft.getInstance().player;

        if (entity != null) {
            BlockPos targetPos = entity.blockPosition();
            if (entity instanceof Player player) {
                HitResult hit = player.pick(8, Minecraft.getInstance().getDeltaFrameTime(), false);
                if (hit instanceof BlockHitResult blockHitResult) {
                    targetPos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
                }
            }

            Optional<ResourceLocation> identifier = MBPData.meetsPredicate(world, targetPos, Block.byItem(stack.getItem()).defaultBlockState(), ContextIdentifiers.ITEM);
            if (identifier.isPresent()) {
                BakedModelManagerAccess access = BakedModelManagerAccess.of(itemModelShaper.getModelManager());
                model = access.reallyGetModel(identifier.get());
            }
            PuzzlePredicates.currentEntity = null;
        }

        this.renderModelLists(model, stack, light, overlay, matrices, vertices);
    }
}
