package net.puzzlemc.predicates.util;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
//? if > 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockStateModel;
//?} else {
/*import net.minecraft.client.resources.model.BakedModel;
*///?}

/**
 * This class is meant to abstract the changes to models within vanilla Minecraft by wrapping the raw models
 * @param raw the vanilla model
 */
public record PredicateModel(@NotNull /*? if > 1.21.4 {*/ BlockStateModel /*?} else {*/ /*BakedModel*//*?}*/ raw) {
    public static final PredicateModel MISSING = new PredicateModel(Minecraft.getInstance().getModelManager()./*? if > 1.21.4 {*/ getMissingBlockStateModel() /*?} else {*/ /*getMissingModel() *//*?}*/);
}
