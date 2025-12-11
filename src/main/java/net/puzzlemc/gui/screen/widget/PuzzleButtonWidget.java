package net.puzzlemc.gui.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class PuzzleButtonWidget extends Button {
    private final PuzzleWidget.TextAction title;

    public PuzzleButtonWidget(int x, int y, int width, int height, PuzzleWidget.TextAction title, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Supplier::get);
        this.title = title;
    }
    @Override
    //? if < 1.21.11 {
    /*public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
    *///?} else {
    public void renderContents(GuiGraphics context, int mouseX, int mouseY, float delta) {
    //?}
        try { title.setTitle(this);
        } catch (Exception e) {e.fillInStackTrace(); this.visible = false;}

        //? if < 1.21.11 {
        /*super.renderWidget(context, mouseX, mouseY, delta);*/
        //?} else {
        this.renderDefaultSprite(context);
        this.renderDefaultLabel(context.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
        //?}
    }
}
