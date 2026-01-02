package net.puzzlemc.predicates.util;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
//? if > 1.21.4 {
import net.minecraft.client.renderer.block.model.BlockStateModel;
//?} else {
/*import net.minecraft.client.resources.model.BakedModel;
*///?}

public record PredicateModel(@NotNull /*? if > 1.21.4 {*/ BlockStateModel /*?} else {*/ /*BakedModel*//*?}*/ raw) {
    public static final PredicateModel MISSING = new PredicateModel(Minecraft.getInstance().getModelManager()./*? if > 1.21.4 {*/ getMissingBlockStateModel() /*?} else {*/ /*getMissingModel() *//*?}*/);
}
