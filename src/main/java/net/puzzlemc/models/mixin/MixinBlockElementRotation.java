package net.puzzlemc.models.mixin;

//? if < 1.21.11 {
/*import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.puzzlemc.models.MultiAxisRotation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockElementRotation.class)
public class MixinBlockElementRotation implements MultiAxisRotation {
    @Nullable
    @Unique
    public Vector3f multiAxisRotation = null;

    @Override
    @Unique
    public void puzzle$setMultiAxisRotation(Vector3f rotation) {
        this.multiAxisRotation = rotation;
    }

    @Override
    @Unique
    public @Nullable Vector3f puzzle$getMultiAxisRotation() {
        return this.multiAxisRotation;
    }
}
*///?} else {
import org.spongepowered.asm.mixin.Mixin;
import eu.midnightdust.core.MidnightLib;

@Mixin(MidnightLib.class)
public abstract class MixinBlockElementRotation {}
//?}
