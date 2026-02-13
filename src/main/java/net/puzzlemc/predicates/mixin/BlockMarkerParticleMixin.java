package net.puzzlemc.predicates.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BlockMarker;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.util.ConditionCheck;
import net.puzzlemc.predicates.common.ContextIDs;
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

@Mixin(BlockMarker.class)
public abstract class BlockMarkerParticleMixin extends /*? if < 1.21.10 {*/  /*TextureSheetParticle  *//*?} else {*/ SingleQuadParticle /*?}*/ {
    //? if >= 1.21.10 {
    protected BlockMarkerParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {super(clientLevel, d, e, f, textureAtlasSprite);}
    //?} else {
    /*protected BlockMarkerParticleMixin(ClientLevel clientWorld, double d, double e, double f) {super(clientWorld, d, e, f);}
    *///?}

    @Inject(at = @At(value = "TAIL"), method = "<init>")
    public void init(ClientLevel world, double x, double y, double z, BlockState state, CallbackInfo ci) {
        Optional<BlockStateModel> override = ConditionCheck.meetsPredicate(world, new BlockPos((int)x, (int)y, (int)z), state, ContextIDs.MARKER_PARTICLE);

        override.ifPresent(modelData -> this.setSprite(modelData./*? if < 1.21.5 {*/ /*getParticleIcon() *//*?} else {*/ particleIcon() /*?}*/));
    }
}
