package net.puzzlemc.predicates.util;

import com.google.gson.JsonObject;
import com.mojang.math.OctahedralGroup;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ModelData {
    private final int xRot, yRot, zRot;
    private final boolean uvLock;
    private final Identifier modelLocation;
    public final ExtraModelKey<@NotNull BlockStateModel> modelKey;

    public ModelData(int xRot, int yRot, int zRot, boolean uvLock, String applyId) {
        this(xRot, yRot, zRot, uvLock, Identifier.fromNamespaceAndPath(applyId.split(":")[0], "block/" + applyId.split(":")[1]));
    }

    public ModelData(int xRot, int yRot, int zRot, boolean uvLock, Identifier modelLocation) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
        this.uvLock = uvLock;
        this.modelLocation = modelLocation;
        this.modelKey = ExtraModelKey.create(appendedIdentifier()::toString);
    }

    public static ModelData none(String applyId) {
        return new ModelData(0, 0, 0, false, applyId);
    }

    public static ModelData parse(JsonObject json, String applyId) {
        int x = 0;
        if (json.has("x"))
            x = json.get("x").getAsInt();
        int y = 0;
        if (json.has("y"))
            y = json.get("y").getAsInt();
        int z = 0;
        if (json.has("z"))
            z = json.get("z").getAsInt();
        boolean uvLock = false;
        if (json.has("uvlock"))
            uvLock = json.get("uvlock").getAsBoolean();

        return new ModelData(x, y, z, uvLock, applyId);
    }

    public ModelState asVanilla() {
        OctahedralGroup group = OctahedralGroup.IDENTITY;
        switch (xRot) {
            case 90 -> group = OctahedralGroup.BLOCK_ROT_X_90;
            case 180 -> group = OctahedralGroup.BLOCK_ROT_X_180;
            case 270 -> group = OctahedralGroup.BLOCK_ROT_X_270;
        }
        switch (yRot) {
            case 90 -> group = OctahedralGroup.BLOCK_ROT_Y_90;
            case 180 -> group = OctahedralGroup.BLOCK_ROT_Y_180;
            case 270 -> group = OctahedralGroup.BLOCK_ROT_Y_270;
        }
        switch (zRot) {
            case 90 -> group = OctahedralGroup.BLOCK_ROT_Z_90;
            case 180 -> group = OctahedralGroup.BLOCK_ROT_Z_180;
            case 270 -> group = OctahedralGroup.BLOCK_ROT_Z_270;
        }
        return uvLock ? BlockModelRotation.get(group).withUvLock() : BlockModelRotation.get(group);
    }

    public Identifier appendedIdentifier() {
        return Identifier.fromNamespaceAndPath(modelLocation.getNamespace(), "%s/x%s_y%s_z%s_uv%s".formatted(modelLocation.getPath(), xRot, yRot, zRot, uvLock));
    }

    public Identifier modelLocation() {
        return modelLocation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ModelData) obj;
        return this.xRot == that.xRot &&
                this.yRot == that.yRot &&
                this.zRot == that.zRot &&
                this.uvLock == that.uvLock &&
                Objects.equals(this.modelLocation, that.modelLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xRot, yRot, zRot, uvLock, modelLocation);
    }

    @Override
    public String toString() {
        return "ModelData[" +
                "xRot=" + xRot + ", " +
                "yRot=" + yRot + ", " +
                "zRot=" + zRot + ", " +
                "uvLock=" + uvLock + ", " +
                "modelLocation=" + modelLocation + ']';
    }

    public PredicateModel getOverrideModel() {
        //? if fabric && > 1.21.4 {
        var model = Minecraft.getInstance().getModelManager().getModel(this.modelKey);
        //?} else if fabric {
        /*var model = Minecraft.getInstance().getModelManager().getModel(modelId);
         *///?} else if > 1.21.4 {
        /*var model = Minecraft.getInstance().getModelManager().getStandaloneModel(predicates.getOrDefault(modelId, null));
         *///?} else if > 1.21.1 {
        //var model = Minecraft.getInstance().getModelManager().getStandaloneModel(modelId);
        //?} else {
        /*var model = Minecraft.getInstance().getModelManager().getModel(new ModelIdentifier(modelId, "standalone"));
         *///?}
        return model != null ? new PredicateModel(model) : PredicateModel.MISSING;
    }
}
