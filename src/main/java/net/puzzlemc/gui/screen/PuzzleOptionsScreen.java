package net.puzzlemc.gui.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.PuzzleGui;
import net.puzzlemc.gui.screen.widget.*;
//? if >= 1.21.9
/*import net.minecraft.client.input.KeyEvent;*/


import java.util.List;
import java.util.Objects;

import static net.minecraft.network.chat.CommonComponents.GUI_DONE;

public class PuzzleOptionsScreen extends Screen {
    public PuzzleOptionListWidget list;
    public List<Component> tooltip = null;
    public TabManager tabManager = new TabManager(a -> {}, a -> {});
    public Tab prevTab;
    public TabNavigationBar tabNavigation;
    public static Component graphicsTab = Component.translatable("puzzle.page.graphics");
    public static Component miscTab = Component.translatable("puzzle.page.misc");
    public static Component performanceTab = Component.translatable("puzzle.page.performance");
    public static Component resourcesTab = Component.translatable("puzzle.page.resources");

    public PuzzleOptionsScreen(Screen parent) {
        super(Component.translatable("puzzle.screen.title"));
        this.parent = parent;
    }
    private final Screen parent;

    @Override
    protected void init() {
        if (!PuzzleGui.lateInitDone) PuzzleGui.lateInit();

        List<Tab> tabs = Lists.newArrayList();
        if (!PuzzleApi.GRAPHICS_OPTIONS.isEmpty()) tabs.add(new GridLayoutTab(graphicsTab));
        if (!PuzzleApi.RESOURCE_OPTIONS.isEmpty()) tabs.add(new GridLayoutTab(resourcesTab));
        if (!PuzzleApi.PERFORMANCE_OPTIONS.isEmpty()) tabs.add(new GridLayoutTab(performanceTab));
        if (!PuzzleApi.MISC_OPTIONS.isEmpty()) tabs.add(new GridLayoutTab(miscTab));

        tabNavigation = TabNavigationBar.builder(tabManager, this.width).addTabs(tabs.toArray(new Tab[0])).build();
        tabNavigation.selectTab(0, false);
        tabNavigation.arrangeElements();
        prevTab = tabManager.getCurrentTab();

        this.list = new PuzzleOptionListWidget(this.minecraft, this.width, this.height - 57, 24, 25);
        fillList();

        if (tabs.size() > 1) {
            this.addRenderableWidget(tabNavigation);
            list.renderHeaderSeparator = false;
        }

        this.addWidget(list);

        super.init();
        this.addRenderableWidget(Button.builder(GUI_DONE, (button) -> Objects.requireNonNull(minecraft).setScreen(parent)).bounds(this.width / 2 - 100, this.height - 26, 200, 20).build());
    }
    private void fillList() {
        List<PuzzleWidget> options = List.of();
        if (tabManager.getCurrentTab() == null) return;
        else {
            Component title = tabManager.getCurrentTab().getTabTitle();
            if (title.equals(graphicsTab)) options = PuzzleApi.GRAPHICS_OPTIONS;
            else if (title.equals(miscTab)) options = PuzzleApi.MISC_OPTIONS;
            else if (title.equals(performanceTab))
                options = PuzzleApi.PERFORMANCE_OPTIONS;
            else if (title.equals(resourcesTab)) options = PuzzleApi.RESOURCE_OPTIONS;
        }
        list.addAll(options);
    }
    @Override
    public void tick() {
        super.tick();
        if (prevTab != null && prevTab != tabManager.getCurrentTab()) {
            prevTab = tabManager.getCurrentTab();
            this.list.clear();
            fillList();
            list.setScrollAmount(0);
        }
    }

    @Override
    //? if >= 1.21.9 {
    /*public boolean keyPressed(KeyEvent input) {
        return this.tabNavigation.keyPressed(input) || super.keyPressed(input);
    }
    *///?} else {
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        return this.tabNavigation.keyPressed(key) || super.keyPressed(key, scanCode, modifiers);
    }
    //?}

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        //? if >= 1.21 {
        /*super.render(context, mouseX, mouseY, delta);
        *///?} else {
        super.renderBackground(context);
         //?}
        this.list.render(context, mouseX, mouseY, delta);
        //? if < 1.21
        super.render(context, mouseX, mouseY, delta);
    }
}
