package net.puzzlemc.predicates.accessor;


import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

public interface BakedModelManagerAccess {
    BakedModel reallyGetModel(ResourceLocation model);

    static BakedModelManagerAccess of(ModelManager manager) {
        return (BakedModelManagerAccess) manager;
    }
}
