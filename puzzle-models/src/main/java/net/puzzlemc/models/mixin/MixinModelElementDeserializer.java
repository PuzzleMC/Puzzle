package net.puzzlemc.models.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3f;
import net.puzzlemc.core.config.PuzzleConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelElement.Deserializer.class)
public abstract class MixinModelElementDeserializer {
    @Shadow protected abstract Vec3f deserializeVec3f(JsonObject object, String name);

    @Inject(at = @At("HEAD"),method = "deserializeRotationAngle", cancellable = true)
    private void puzzle$deserializeRotationAngle(JsonObject object, CallbackInfoReturnable<Float> cir) {
        if (PuzzleConfig.unlimitedRotations) {
            float angle = JsonHelper.getFloat(object, "angle");
            cir.setReturnValue(angle);
        }
    }
    @Inject(at = @At("HEAD"),method = "deserializeTo", cancellable = true)
    private void puzzle$deserializeTo(JsonObject object, CallbackInfoReturnable<Vec3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vec3f vec3f = this.deserializeVec3f(object, "to");
            if (!(vec3f.getX() < -32.0F) && !(vec3f.getY() < -32.0F) && !(vec3f.getZ() < -32.0F) && !(vec3f.getX() > 48.0F) && !(vec3f.getY() > 48.0F) && !(vec3f.getZ() > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vec3f);
            }
        }
    }
    @Inject(at = @At("HEAD"),method = "deserializeFrom", cancellable = true)
    private void puzzle$deserializeFrom(JsonObject object, CallbackInfoReturnable<Vec3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vec3f vec3f = this.deserializeVec3f(object, "from");
            if (!(vec3f.getX() < -32.0F) && !(vec3f.getY() < -32.0F) && !(vec3f.getZ() < -32.0F) && !(vec3f.getX() > 48.0F) && !(vec3f.getY() > 48.0F) && !(vec3f.getZ() > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vec3f);
            }
        }
    }
}
