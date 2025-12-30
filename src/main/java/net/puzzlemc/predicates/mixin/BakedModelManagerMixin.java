package net.puzzlemc.predicates.mixin;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.puzzlemc.predicates.accessor.BakedModelManagerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(ModelManager.class)
public abstract class BakedModelManagerMixin implements BakedModelManagerAccess {

//    @Shadow public abstract BakedModel getMissingModel();
//
//    @Shadow
//    private Map<ModelIdentifier, BakedModel> /*? if < 1.21.4 {*/ /*bakedRegistry*/ /*?} else {*/ bakedBlockStateModels /*?}*/;
//
//    @Shadow
//    @Final
//    private BlockModelShaper blockModelShaper;
//
//    @Override @Unique
//    public BakedModel reallyGetModel(Identifier model) {
//        return /*? if < 1.21.4 {*/ /*bakedRegistry*/ /*?} else {*/ bakedBlockStateModels /*?}*/.getOrDefault(new ModelIdentifier(model, "standalone"), this.getMissingModel());
//        blockModelShaper.
//    }

}
