package net.puzzlemc.gui.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.PuzzleGui;
import net.puzzlemc.gui.screen.widget.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.List;
import java.util.Objects;

public class PuzzleOptionsScreen extends Screen {
    public PuzzleOptionListWidget list;
    public List<Text> tooltip = null;
    public TabManager tabManager = new TabManager(a -> {}, a -> {});
    public Tab prevTab;
    public TabNavigationWidget tabNavigation;
    public static Text graphicsTab = Text.translatable("puzzle.page.graphics");
    public static Text miscTab = Text.translatable("puzzle.page.misc");
    public static Text performanceTab = Text.translatable("puzzle.page.performance");
    public static Text resourcesTab = Text.translatable("puzzle.page.resources");

    public PuzzleOptionsScreen(Screen parent) {
        super(Text.translatable("puzzle.screen.title"));
        this.parent = parent;
    }
    private final Screen parent;

    @Override
    protected void init() {
        if (!PuzzleGui.lateInitDone) PuzzleGui.lateInit();

        List<Tab> tabs = Lists.newArrayList();
        if (!PuzzleApi.GRAPHICS_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(graphicsTab));
        if (!PuzzleApi.RESOURCE_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(resourcesTab));
        if (!PuzzleApi.PERFORMANCE_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(performanceTab));
        if (!PuzzleApi.MISC_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(miscTab));

        tabNavigation = TabNavigationWidget.builder(tabManager, this.width).tabs(tabs.toArray(new Tab[0])).build();
        tabNavigation.selectTab(0, false);
        tabNavigation.init();
        prevTab = tabManager.getCurrentTab();

        this.list = new PuzzleOptionListWidget(this.client, this.width, this.height - 57, 24, 25);
        fillList();

        if (tabs.size() > 1) {
            this.addDrawableChild(tabNavigation);
            list.renderHeaderSeparator = false;
        }

        this.addDrawableChild(list);

        super.init();
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> Objects.requireNonNull(client).setScreen(parent)).dimensions(this.width / 2 - 100, this.height - 26, 200, 20).build());
    }
    private void fillList() {
        List<PuzzleWidget> options = List.of();
        if (tabManager.getCurrentTab() == null) return;
        else {
            Text title = tabManager.getCurrentTab().getTitle();
            if (title.equals(graphicsTab)) options = PuzzleApi.GRAPHICS_OPTIONS;
            else if (title.equals(miscTab)) options = PuzzleApi.MISC_OPTIONS;
            else if (title.equals(performanceTab))
                options = PuzzleApi.PERFORMANCE_OPTIONS;
            else if (title.equals(resourcesTab)) options = PuzzleApi.RESOURCE_OPTIONS;
        }
        list.addAll(options);
    }
    @Override
    public boolean keyPressed(KeyInput input) {
        return this.tabNavigation.keyPressed(input) || super.keyPressed(input);
    }
    @Override
    public void tick() {
        super.tick();
        if (prevTab != null && prevTab != tabManager.getCurrentTab()) {
            prevTab = tabManager.getCurrentTab();
            this.list.clear();
            fillList();
            list.setScrollY(0);
        }
    }
}
