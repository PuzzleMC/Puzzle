package net.puzzlemc.predicates.util;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
//? if fabric && > 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
//?} else if neoforge && > 1.21.4 {
/*import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
*///?} else if neoforge {
/*import net.minecraft.client.resources.model.ModelIdentifier;
*///?}

//? if > 1.21.10 {
import com.mojang.math.OctahedralGroup;
//?} else if > 1.21.4 {
/*import com.mojang.math.Quadrant;
import com.mojang.serialization.JavaOps;
*///?}
import java.util.HashMap;
import java.util.Objects;

/**
 * This class contains all the relevant data to build and retrieve each unique model
 */
public final class ModelData {
    private final int xRot, yRot, zRot;
    private final boolean uvLock;
    private final Identifier modelLocation;
    private final Identifier distinctModelId;
    //? if > 1.21.4 {
    public final /*? fabric {*/ ExtraModelKey /*?} else {*/ /*StandaloneModelKey *//*?}*/<@NotNull BlockStateModel> modelKey;
    //?}

    private ModelData(int xRot, int yRot, int zRot, boolean uvLock, String applyId) {
        this(xRot, yRot, zRot, uvLock, Identifier.fromNamespaceAndPath(applyId.split(":")[0], "block/" + applyId.split(":")[1]));
    }

    private ModelData(int xRot, int yRot, int zRot, boolean uvLock, Identifier modelLocation) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
        this.uvLock = uvLock;
        this.modelLocation = modelLocation;
        this.distinctModelId = Identifier.fromNamespaceAndPath(modelLocation.getNamespace(), "%s/x%s_y%s_z%s_uv%s".formatted(modelLocation.getPath(), xRot, yRot, zRot, uvLock));
        //? if fabric && > 1.21.4 {
        this.modelKey = ExtraModelKey.create(distinctModelId::toString);
        //?} else if neoforge && > 1.21.4 {
        /*modelKey = new StandaloneModelKey<>(/^? if < 1.21.6 {^/ distinctModelId /^?} else {^/ /^distinctModelId::toString ^//^?}^/
        );
        *///?}
    }

    public static ModelData basic(String applyId) {
        return checkCache(new ModelData(0, 0, 0, false, applyId));
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

        return checkCache(new ModelData(x, y, z, uvLock, applyId));
    }

    private static final HashMap<Integer, ModelData> existingInstances = new HashMap<>();

    /**
     * In case a ModelData instance for this model state has already been created, return it.
     * Otherwise, save the new instance to the cache.
     */
    private static ModelData checkCache(ModelData candidate) {
        int hash = candidate.hashCode();
        if (existingInstances.containsKey(hash))
            candidate = existingInstances.get(hash);
        else
            existingInstances.put(hash, candidate);
        return candidate;
    }

    /**
     * The cache is needed to ensure that if the same model is requested twice, both will share the same ModelData instance (along with the key).
     * Once model loading has completed, we can clear this cache.
     */
    public static void clearCache() {
        existingInstances.clear();
    }

    public ModelState asVanilla() {
        //? if > 1.21.10 {
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
        //?} else if > 1.21.4 {
        /*Quadrant xQuad = Quadrant.CODEC.parse(JavaOps.INSTANCE, xRot).mapOrElse(q -> q, e -> Quadrant.R0);
        Quadrant yQuad = Quadrant.CODEC.parse(JavaOps.INSTANCE, yRot).mapOrElse(q -> q, e -> Quadrant.R0);
        BlockModelRotation rotation = BlockModelRotation.by(xQuad, yQuad);
        return uvLock ? rotation.withUvLock() : rotation;
        *///?} else {
        /*return BlockModelRotation.by(xRot, yRot);
        *///?}
    }

    public Identifier modelLocation() {
        return modelLocation;
    }
    public Identifier distinctModelId() {
        return distinctModelId;
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
        /*var model = Minecraft.getInstance().getModelManager().getModel(modelLocation());
         *///?} else if > 1.21.4 {
        /*var model = Minecraft.getInstance().getModelManager().getStandaloneModel(this.modelKey);
         *///?} else if > 1.21.1 {
        /*var model = Minecraft.getInstance().getModelManager().getStandaloneModel(modelLocation());
        *///?} else {
        /*var model = Minecraft.getInstance().getModelManager().getModel(new ModelIdentifier(distinctModelId(), "standalone"));
         *///?}
        return model != null ? new PredicateModel(model) : PredicateModel.MISSING;
    }
}
