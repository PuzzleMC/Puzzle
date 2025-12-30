package net.puzzlemc.predicates.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import org.jetbrains.annotations.NotNull;

public record PredicateModel(@NotNull BlockStateModel raw) {
    public static final PredicateModel MISSING = new PredicateModel(Minecraft.getInstance().getModelManager().getMissingBlockStateModel());
}
