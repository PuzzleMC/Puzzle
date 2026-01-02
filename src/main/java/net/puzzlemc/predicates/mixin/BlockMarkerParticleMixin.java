package net.puzzlemc.predicates.mixin;

import net.minecraft.client.particle.BlockMarker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockMarker.class)
public abstract class BlockMarkerParticleMixin {//extends TextureSheetParticle {

//    protected BlockMarkerParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
//        super(clientWorld, d, e, f);
//    }

//    @Inject(at = @At(value = "TAIL"), method = "<init>")
//    public void init(ClientLevel world, double x, double y, double z, BlockState state, CallbackInfo ci) {
//        Optional<Identifier> identifier = MBPData.meetsPredicate(world, new BlockPos((int)x, (int)y, (int)z), state, ContextIDs.MARKER_PARTICLE);
//
//        Minecraft client = Minecraft.getInstance();
//        if (identifier.isPresent()) {
//            BakedModelManagerAccess access = BakedModelManagerAccess.of(client.getModelManager());
//            setSprite(access.reallyGetModel(identifier.get()).getParticleIcon());
//        } else {
//            this.setSprite(client.getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
//        }
//    }
}
