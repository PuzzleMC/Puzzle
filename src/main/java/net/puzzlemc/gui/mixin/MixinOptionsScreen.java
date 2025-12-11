package net.puzzlemc.gui.mixin;

import eu.midnightdust.core.config.MidnightLibConfig;
import eu.midnightdust.lib.util.PlatformFunctions;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.puzzlemc.core.config.PuzzleConfig;
import net.puzzlemc.gui.screen.PuzzleOptionsScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

//? if >= 1.21 {
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.puzzlemc.gui.PuzzleGui;
//?} else {
/*import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.components.TextAndImageButton;
import net.minecraft.resources.Identifier;

import static net.puzzlemc.core.PuzzleCore.MOD_ID;
*///?}

@Mixin(OptionsScreen.class)
public abstract class MixinOptionsScreen extends Screen {
    private MixinOptionsScreen(Component title) {super(title);}

    //? if >= 1.21 {
    @Shadow @Final private HeaderAndFooterLayout layout;
    @Unique
    SpriteIconButton puzzle$button = SpriteIconButton.builder(Component.translatable("puzzle.screen.title"), (buttonWidget) ->
                    (Objects.requireNonNull(this.minecraft)).setScreen(new PuzzleOptionsScreen(this)), true)
            .size(20, 20).sprite(PuzzleGui.PUZZLE_BUTTON, 20, 20).build();

    @Inject(at = @At("HEAD"), method = "init")
    public void puzzle$onInit(CallbackInfo ci) {
        if (PuzzleConfig.enablePuzzleButton) {
            this.puzzle$setButtonPos();
            this.addRenderableWidget(puzzle$button);
        }
    }

    @Inject(at = @At("TAIL"), method = "repositionElements")
    public void puzzle$onResize(CallbackInfo ci) {
        if (PuzzleConfig.enablePuzzleButton) this.puzzle$setButtonPos();
    }

    @Unique
    public void puzzle$setButtonPos() {
        int i = 0;
        if (PlatformFunctions.isModLoaded("lod")) {
            i = i + 358;
            if (MidnightLibConfig.config_screen_list.equals(MidnightLibConfig.ConfigButton.FALSE)) i = i - 25;
        }
        puzzle$button.setPosition(this.width / 2 - 178 + i, layout.getY() + layout.getFooterHeight() - 4);
    }
    //?} else {
    /*@Unique TextAndImageButton puzzle$button = TextAndImageButton.builder(Component.translatable("midnightlib.overview.title"), Identifier.fromNamespaceAndPath(MOD_ID, "icon/button.png"),
            button -> Objects.requireNonNull(minecraft).setScreen(new PuzzleOptionsScreen(this))).textureSize(19, 19).usedTextureSize(16, 16).offset(-2, 0).build();

    @Inject(at = @At("HEAD"), method = "init")
    private void midnightlib$init(CallbackInfo ci) {
        if (PuzzleConfig.enablePuzzleButton) {
            puzzle$button.setWidth(20);
            int i = 0;
            if (PlatformFunctions.isModLoaded("lod")) {
                i = i + 358;
                if (MidnightLibConfig.config_screen_list.equals(MidnightLibConfig.ConfigButton.FALSE)) i = i - 25;
            }
            puzzle$button.setPosition(this.width / 2 - 178 + i, this.height / 6 - 12);
            this.addRenderableWidget(puzzle$button);
        }
    }
    *///?}
}
