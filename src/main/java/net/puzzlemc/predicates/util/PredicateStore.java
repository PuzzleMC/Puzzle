package net.puzzlemc.predicates.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

//? fabric {
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
//?} else {
/*import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
*///?}

import java.util.HashMap;
import java.util.Map;

public class PredicateStore {
    //? if fabric {
    public static Map<Identifier, ExtraModelKey<@NotNull BlockStateModel>> predicates = new HashMap<>();
    //?} else if neoforge {
    /*public static Map<Identifier, StandaloneModelKey<BlockStateModel>> predicates = new HashMap<>();
    *///?}

    @Unique
    public static PredicateModel reallyGetModel(Identifier modelId) {
        //? fabric {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getModel(predicates.getOrDefault(modelId, null));
        //?} else {
        /*BlockStateModel model = Minecraft.getInstance().getModelManager().getStandaloneModel(predicates.getOrDefault(modelId, null));
        *///?}
        return model != null ? new PredicateModel(model) : PredicateModel.MISSING;
    }
}
