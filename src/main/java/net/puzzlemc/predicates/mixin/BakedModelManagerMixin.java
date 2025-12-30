package net.puzzlemc.predicates.mixin;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(ModelManager.class)
public abstract class BakedModelManagerMixin implements BakedModelManagerAccess {

    @Shadow public abstract BakedModel getMissingModel();

    @Shadow private Map<ModelResourceLocation, BakedModel> bakedRegistry;

    @Override @Unique
    public BakedModel reallyGetModel(ResourceLocation model) {
        return bakedRegistry.getOrDefault(model, this.getMissingModel());
    }

}
