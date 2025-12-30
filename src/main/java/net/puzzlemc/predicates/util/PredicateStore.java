package net.puzzlemc.predicates.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Unique;

//? fabric {
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
//?} else {
/*import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
*///?}

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PredicateStore {
    //? if fabric {
    public static Map<Identifier, ExtraModelKey<BlockStateModel>> predicates = new HashMap<>();
    //?} else if neoforge {
    /*public static Map<Identifier, StandaloneModelKey<BlockStateModel>> predicates = new HashMap<>();
    *///?}

    @Unique
    public static PredicateModel reallyGetModel(Identifier modelId) {
        //? fabric {
        return new PredicateModel(Objects.requireNonNull(Minecraft.getInstance().getModelManager().getModel(predicates.getOrDefault(modelId, null))));
        //?} else {
        /*BlockStateModel model = Minecraft.getInstance().getModelManager().getStandaloneModel(predicates.getOrDefault(modelId, null));
        System.out.println(model);
        return model != null ? new PredicateModel(model) : PredicateModel.MISSING;
        *///?}
        //return predicates.getOrDefault(modelId, PredicateModel.MISSING);
    }
}
