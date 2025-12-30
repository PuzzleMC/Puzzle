package net.puzzlemc.models.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.puzzlemc.core.config.PuzzleConfig;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if < 1.21.11 {
import net.minecraft.util.GsonHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.core.Direction;
import net.puzzlemc.models.MultiAxisRotation;
//?}

@Mixin(BlockElement.Deserializer.class)
public abstract class MixinModelElementDeserializer {
    //? if < 1.21.11 {
    @Shadow protected abstract Vector3f getVector3f(JsonObject jsonObject, String string);

    @Inject(method = "getRotation", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;mul(F)Lorg/joml/Vector3f;", shift = At.Shift.AFTER), cancellable = true)
    private void getRotation(JsonObject jsonObject, CallbackInfoReturnable<BlockElementRotation> cir, @Local Vector3f vector3f) {
        JsonObject rotationData = GsonHelper.getAsJsonObject(jsonObject, "rotation");
        if (PuzzleConfig.unlimitedRotations && !rotationData.has("angle") && !rotationData.has("axis")) {
            if (!rotationData.has("x") && !rotationData.has("y") && !rotationData.has("z")) {
                throw new JsonParseException("Missing rotation value, expected either 'axis' and 'angle' or 'x', 'y' and 'z'");
            }

            float xRot = GsonHelper.getAsFloat(rotationData, "x", 0.0F);
            float yRot = GsonHelper.getAsFloat(rotationData, "y", 0.0F);
            float zRot = GsonHelper.getAsFloat(rotationData, "z", 0.0F);

            boolean shouldRescale = GsonHelper.getAsBoolean(rotationData, "rescale", false);
            BlockElementRotation rotation = new BlockElementRotation(vector3f, Direction.Axis.Z, 0.0F, shouldRescale);
            ((MultiAxisRotation) (Object) rotation).puzzle$setMultiAxisRotation(new Vector3f(xRot, yRot, zRot)); // Adds the per-axis rotation values to the BlockElementRotation object
            cir.setReturnValue(rotation);
        }
    }

    @Inject(at = @At("HEAD"),method = "getAngle", cancellable = true)
    private void puzzle$deserializeRotationAngle(JsonObject object, CallbackInfoReturnable<Float> cir) {
        if (PuzzleConfig.unlimitedRotations) {
            float angle = GsonHelper.getAsFloat(object, "angle");
            cir.setReturnValue(angle);
        }
    }
    @Inject(at = @At("HEAD"),method = "getTo", cancellable = true)
    private void puzzle$deserializeTo(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vector3f vec3f = this.getVector3f(object, "to");
            if (!(vec3f.x < -32.0F) && !(vec3f.y < -32.0F) && !(vec3f.z < -32.0F) && !(vec3f.x > 48.0F) && !(vec3f.y > 48.0F) && !(vec3f.z > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vec3f);
            }
        }
    }
    @Inject(at = @At("HEAD"),method = "getFrom", cancellable = true)
    private void puzzle$deserializeFrom(JsonObject object, CallbackInfoReturnable<Vector3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vector3f vec3f = this.getVector3f(object, "from");
            if (!(vec3f.x < -32.0F) && !(vec3f.y < -32.0F) && !(vec3f.z < -32.0F) && !(vec3f.x > 48.0F) && !(vec3f.y > 48.0F) && !(vec3f.z > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vec3f);
            }
        }
    }
    //?} else {
    /*@Shadow
    private static Vector3f getVector3f(JsonObject jsonObject, String string) {
        throw new RuntimeException("MixinModelElementDeserializer from Puzzle could not be loaded properly");
    }

    @Inject(at = @At("HEAD"),method = "getPosition", cancellable = true)
    private static void puzzle$deserializePos(JsonObject object, String string, CallbackInfoReturnable<Vector3f> cir) {
        if (PuzzleConfig.biggerModels) {
            Vector3f vec3f = getVector3f(object, string);
            if (!(vec3f.x < -32.0F) && !(vec3f.y < -32.0F) && !(vec3f.z < -32.0F) && !(vec3f.x > 48.0F) && !(vec3f.y > 48.0F) && !(vec3f.z > 48.0F)) {
                cir.setReturnValue(vec3f);
            } else {
                throw new JsonParseException("'%s' specifier exceeds the allowed boundaries: %s".formatted(string, vec3f));
            }
        }
    }
    *///?}
}
