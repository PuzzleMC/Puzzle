package net.puzzlemc.models.mixin;

//? if < 1.21.11 {
/*import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.core.Direction;
import net.puzzlemc.models.MultiAxisRotation;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FaceBakery.class)
public abstract class MixinFaceBakery {
    @Inject(method = "applyElementRotation", at = @At("HEAD"), cancellable = true)
    private /^? if >= 1.21.4 {^/ static /^?}^/ void puzzle$applyMultiAxisRotation(Vector3f vector3f, BlockElementRotation rotationInfo, CallbackInfo ci) {
       //noinspection ConstantValue
       if (rotationInfo != null && ((MultiAxisRotation) (Object) rotationInfo).puzzle$getMultiAxisRotation() != null) {
           puzzle$rotateVertexBy(vector3f, rotationInfo.origin(), puzzle$calcRotationMatrix(rotationInfo));
           ci.cancel();
       }
    }

    @Unique
    private static Matrix4f puzzle$calcRotationMatrix(BlockElementRotation arg) {
        Matrix4f matrix4f = puzzle$getTransformation(arg);
        if (arg.rescale() && !puzzle$isIdentityMatrix(matrix4f))
            matrix4f.scale(puzzle$calcRescale(matrix4f));

        return matrix4f;
    }

    @Unique
    private static Vector3fc puzzle$calcRescale(Matrix4fc modelMatrix) {
        Vector3f vector3f = new Vector3f();
        float scaleX = puzzle$scaleFactorForAxis(modelMatrix, Direction.Axis.X, vector3f);
        float scaleY = puzzle$scaleFactorForAxis(modelMatrix, Direction.Axis.Y, vector3f);
        float scaleZ = puzzle$scaleFactorForAxis(modelMatrix, Direction.Axis.Z, vector3f);
        return vector3f.set(scaleX, scaleY, scaleZ);
    }

    @Unique
    private static Vector3f puzzle$getUnitVec3f(Direction.Axis axis) {
        Direction dir = switch (axis) {
            case X -> Direction.EAST;
            case Y -> Direction.UP;
            case Z -> Direction.SOUTH;
        };
        return dir.step();
    }

    @Unique
    private static float puzzle$scaleFactorForAxis(Matrix4fc modelMatrix, Direction.Axis axis, Vector3f vector3f) {
        Vector3f scaleVector = modelMatrix.transformDirection(vector3f.set(puzzle$getUnitVec3f(axis)));
        float maxDimension = Math.max(Math.max(Math.abs(scaleVector.x), Math.abs(scaleVector.y)), Math.abs(scaleVector.z));
        return 1.0F / maxDimension;
    }

    @Unique
    private static Matrix4f puzzle$getTransformation(BlockElementRotation rotation) {
        Vector3f perAxisRotation = ((MultiAxisRotation)(Object)rotation).puzzle$getMultiAxisRotation();
        assert perAxisRotation != null;
        return (new Matrix4f()).rotationZYX(perAxisRotation.z * ((float)Math.PI / 180F),
                perAxisRotation.y * ((float)Math.PI / 180F),
                perAxisRotation.x * ((float)Math.PI / 180F));
    }

    @Unique
    private static void puzzle$rotateVertexBy(Vector3f vertexPos, Vector3fc rotationOrigin, Matrix4fc rotationMatrix) {
        vertexPos.sub(rotationOrigin);
        rotationMatrix.transformPosition(vertexPos);
        vertexPos.add(rotationOrigin);
    }

    @Unique
    private static boolean puzzle$isIdentityMatrix(Matrix4fc matrix) {
        if ((matrix.properties() & 4) != 0)
            return true;

        if (matrix instanceof Matrix4f matrix4f) {
            matrix4f.determineProperties();
            return (matrix4f.properties() & 4) != 0;
        }
        return false;
    }
}
*///?} else {
import org.spongepowered.asm.mixin.Mixin;
import eu.midnightdust.core.MidnightLib;

@Mixin(MidnightLib.class)
public abstract class MixinFaceBakery {}
//?}
