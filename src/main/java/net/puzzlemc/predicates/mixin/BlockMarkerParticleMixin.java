package net.puzzlemc.predicates.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BlockMarker;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.puzzlemc.predicates.MBPData;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import net.puzzlemc.predicates.common.ContextIdentifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BlockMarker.class)
public abstract class BlockMarkerParticleMixin extends TextureSheetParticle {

    protected BlockMarkerParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);

    }

    @Inject(at = @At(value = "TAIL"), method = "<init>")
    public void init(ClientLevel world, double x, double y, double z, BlockState state, CallbackInfo ci) {
        Optional<ResourceLocation> identifier = MBPData.meetsPredicate(world, new BlockPos((int)x, (int)y, (int)z), state, ContextIdentifiers.MARKER_PARTICLE);

        Minecraft client = Minecraft.getInstance();
        if (identifier.isPresent()) {
            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getModelManager());
            setSprite(access.reallyGetModel(identifier.get()).getParticleIcon());
        } else {
            this.setSprite(client.getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        }
    }
}
