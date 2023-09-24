package net.puzzlemc.gui.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleApi;
import net.puzzlemc.gui.PuzzleClient;
import net.puzzlemc.gui.screen.widget.PuzzleOptionListWidget;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
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
        if (!PuzzleClient.lateInitDone) PuzzleClient.lateInit();

        List<Tab> tabs = Lists.newArrayList();
        if (!PuzzleApi.GRAPHICS_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(graphicsTab));
        if (!PuzzleApi.RESOURCE_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(resourcesTab));
        if (!PuzzleApi.PERFORMANCE_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(performanceTab));
        if (!PuzzleApi.MISC_OPTIONS.isEmpty()) tabs.add(new GridScreenTab(miscTab));

        tabNavigation = TabNavigationWidget.builder(tabManager, this.width).tabs(tabs.toArray(new Tab[0])).build();
        if (tabs.size() > 1) this.addDrawableChild(tabNavigation);
        tabNavigation.selectTab(0, false);
        tabNavigation.init();
        prevTab = tabManager.getCurrentTab();
        this.addDrawableChild(tabNavigation);
        this.list = new PuzzleOptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        fillList();
        this.addSelectableChild(this.list);

        super.init();
        if (client != null && client.world != null) this.list.setRenderBackground(false);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> Objects.requireNonNull(client).setScreen(parent)).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }
    private void fillList() {
        List<PuzzleWidget> options = List.of();
        if (tabManager.getCurrentTab() == null) return;
        else if (tabManager.getCurrentTab().getTitle().equals(graphicsTab)) options = PuzzleApi.GRAPHICS_OPTIONS;
        else if (tabManager.getCurrentTab().getTitle().equals(miscTab)) options = PuzzleApi.MISC_OPTIONS;
        else if (tabManager.getCurrentTab().getTitle().equals(performanceTab)) options = PuzzleApi.PERFORMANCE_OPTIONS;
        else if (tabManager.getCurrentTab().getTitle().equals(resourcesTab)) options = PuzzleApi.RESOURCE_OPTIONS;
        list.addAll(options);
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.tabNavigation.trySwitchTabsWithKey(keyCode)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        this.list.render(context, mouseX, mouseY, delta);

        if (tooltip != null) {
            if (this.list.getFocused() != null && (this.list.getHoveredEntry() == null || this.list.getHoveredEntry().button == null || !this.list.getHoveredEntry().button.isMouseOver(mouseX, mouseY))) {
                context.drawTooltip(textRenderer, tooltip, this.list.getFocused().getX(), this.list.getFocused().getY() + (this.list.getFocused().getHeight() * 2));
            }
            else context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
        }
        tooltip = null;
    }
}
