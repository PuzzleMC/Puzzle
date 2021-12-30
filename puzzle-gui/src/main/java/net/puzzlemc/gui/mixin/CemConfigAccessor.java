package net.puzzlemc.gui.mixin;

import net.dorianpb.cem.internal.config.CemConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CemConfig.class, remap = false)
public interface CemConfigAccessor {
    @Accessor("use_optifine_folder")
    void setUseOptifineFolder(boolean value);

    @Accessor("use_new_model_creation_fix")
    void setUseModelCreationFix(boolean value);

    @Accessor("use_old_animations")
    void setUseOldAnimations(boolean value);
}
