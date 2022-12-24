package net.puzzlemc.models.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.util.JsonHelper;
import net.puzzlemc.core.config.PuzzleConfig;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelElement.Deserializer.class)
public abstract class MixinModelElementDeserializer {
    @Shadow protected abstract Vector3f deserializeVec3f(JsonObject object, String name);

    @Inject(at = @At("HEAD"),method = "deserializeRotationAngle", cancellable = true)
    private void puzzle$deserializeRotationAngle(JsonObject object, CallbackInfoReturnable<Float> cir) {
        if (PuzzleConfig.unlimitedRotations) {
            float angle = JsonHelper.getFloat(object, "angle");
            cir.setReturnValue(angle);
        }
    }
    @Inject(at = @At("HEAD"),method = "deserializeTo", cancellable = true)
    private void puzzle$deserializeTo(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vector3f vec3f = this.deserializeVec3f(object, "to");
            if (!(vec3f.x < -32.0F) && !(vec3f.y < -32.0F) && !(vec3f.z < -32.0F) && !(vec3f.x > 48.0F) && !(vec3f.y > 48.0F) && !(vec3f.z > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vec3f);
            }
        }
    }
    @Inject(at = @At("HEAD"),method = "deserializeFrom", cancellable = true)
    private void puzzle$deserializeFrom(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vector3f vec3f = this.deserializeVec3f(object, "from");
            if (!(vec3f.x < -32.0F) && !(vec3f.y < -32.0F) && !(vec3f.z < -32.0F) && !(vec3f.x > 48.0F) && !(vec3f.y > 48.0F) && !(vec3f.z > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vec3f);
            }
        }
    }
}
