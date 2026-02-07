package net.puzzlemc.predicates.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.common.ContextIDs;
import net.puzzlemc.predicates.util.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

//? if < 1.21.10 {
/*import net.minecraft.client.particle.TextureSheetParticle;
*///?} else {
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//?}

@Mixin(TerrainParticle.class)
public abstract class BlockDustParticleMixin extends /*? if < 1.21.10 {*/  /*TextureSheetParticle  *//*?} else {*/ SingleQuadParticle /*?}*/ {
    //? if >= 1.21.10 {
    protected BlockDustParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {super(clientLevel, d, e, f, textureAtlasSprite);}
    //?} else {
    /*protected BlockDustParticleMixin(ClientLevel clientWorld, double d, double e, double f) {super(clientWorld, d, e, f);}
    *///?}

    @Inject(at = @At(value = "TAIL"), method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V")
    public void init(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci) {
        Optional<ModelData> override = MBPData.meetsPredicate(world, blockPos, state, ContextIDs.FALLING_BLOCK);

        override.ifPresent(modelData -> this.setSprite(modelData.getOverrideModel().raw()./*? if < 1.21.5 {*/ /*getParticleIcon() *//*?} else {*/ particleIcon() /*?}*/));
    }
}
