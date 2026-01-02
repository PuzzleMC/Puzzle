package net.puzzlemc.predicates.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Unique;

//? fabric && > 1.21.4 {
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
//?} else if neoforge && > 1.21.4 {
/*import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
*///?}

//? if > 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockStateModel;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
//?} else {
/*import net.minecraft.client.resources.model.BakedModel;
*///?}

//? if < 1.21.4 {
/*import net.minecraft.client.resources.model.ModelIdentifier;
*///?}

public class PredicateStore {
    //? if fabric && > 1.21.4 {
    public static Map<Identifier, ExtraModelKey<@NotNull BlockStateModel>> predicates = new HashMap<>();
    //?} else if neoforge && > 1.21.4 {
    /*public static Map<Identifier, StandaloneModelKey<BlockStateModel>> predicates = new HashMap<>();
    *///?}

    @Unique
    public static PredicateModel reallyGetModel(Identifier modelId) {
        //? if fabric && > 1.21.4 {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getModel(predicates.getOrDefault(modelId, null));
        //?} else if fabric {
        /*BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelId);
        *///?} else if > 1.21.4 {
        /*BlockStateModel model = Minecraft.getInstance().getModelManager().getStandaloneModel(predicates.getOrDefault(modelId, null));
        *///?} else if > 1.21.1 {
        //BakedModel model = Minecraft.getInstance().getModelManager().getStandaloneModel(modelId);
        //?} else {
        /*BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ModelIdentifier(modelId, "standalone"));
        *///?}
        return model != null ? new PredicateModel(model) : PredicateModel.MISSING;
    }
}
