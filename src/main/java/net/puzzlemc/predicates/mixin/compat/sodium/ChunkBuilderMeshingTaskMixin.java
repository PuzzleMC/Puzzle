package net.puzzlemc.predicates.mixin.compat.sodium;

//import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
//import me.jellysquid.mods.sodium.client.world.WorldSlice;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.block.state.BlockState;
//import net.puzzlemc.predicates.common.BlockRendering;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//
//@Mixin(value = ChunkBuilderMeshingTask.class)
//public class ChunkBuilderMeshingTaskMixin {
//
//    @Unique private int x = 0;
//    @Unique private int y = 0;
//    @Unique private int z = 0;
//
//    @Redirect(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/world/WorldSlice;getBlockState(III)Lnet/minecraft/block/BlockState;"))
//    public BlockState getBlockStateRedirect(WorldSlice worldSlice, int x, int y, int z) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        return worldSlice.getBlockState(x,y,z);
//    }
//
//    @Redirect(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModels;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
//    public BakedModel getModelRedirect(BlockModels models, BlockState state) {
//        BakedModel newModel = BlockRendering.tryModelOverride(models, Minecraft.getInstance().level, state, new BlockPos(x, y, z), ContextIDs.CHUNK_MESH);
//        if (newModel != null)
//            return newModel;
//
//        // If failed return original method call
//        return models.getModel(state);
//    }
//}

import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public class ChunkBuilderMeshingTaskMixin {
}