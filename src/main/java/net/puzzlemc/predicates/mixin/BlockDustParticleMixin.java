package net.puzzlemc.predicates.mixin;

import net.minecraft.client.particle.TerrainParticle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TerrainParticle.class)
public abstract class BlockDustParticleMixin {//extends TextureSheetParticle {

//    protected BlockDustParticleMixin(ClientLevel clientWorld, double d, double e, double f) {
//        super(clientWorld, d, e, f);
//    }

//    @Inject(at = @At(value = "TAIL"), method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V")
//    public void init(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos blockPos, CallbackInfo ci) {
//        Optional<Identifier> identifier = MBPData.meetsPredicate(world, blockPos, state, ContextIDs.FALLING_BLOCK);
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
