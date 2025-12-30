package net.puzzlemc.models;

//? if < 1.21.11 {
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public interface MultiAxisRotation {
    void puzzle$setMultiAxisRotation(Vector3f rotation);
    @Nullable Vector3f puzzle$getMultiAxisRotation();
}
//?}