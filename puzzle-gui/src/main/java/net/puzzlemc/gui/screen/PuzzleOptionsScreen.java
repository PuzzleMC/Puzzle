package net.puzzlemc.gui.screen;

import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.puzzlemc.gui.PuzzleClient;
import net.puzzlemc.gui.screen.page.GraphicsPage;
import net.puzzlemc.gui.screen.page.MiscPage;
import net.puzzlemc.gui.screen.page.PerformancePage;
import net.puzzlemc.gui.screen.page.ResourcesPage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Objects;

public class PuzzleOptionsScreen extends Screen {

    public PuzzleOptionsScreen(Screen parent) {
        super(Text.translatable("puzzle.screen.title"));
        this.parent = parent;
    }
    private final Screen parent;

    @Override
    protected void init() {
        super.init();
        if (!PuzzleClient.lateInitDone) PuzzleClient.lateInit();
        GraphicsPage graphicsPage = new GraphicsPage(this);
        MiscPage miscPage = new MiscPage(this);
        PerformancePage performancePage = new PerformancePage(this);
        ResourcesPage resourcesPage = new ResourcesPage(this);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, graphicsPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).setScreen(graphicsPage)));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, resourcesPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).setScreen(resourcesPage)));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, performancePage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).setScreen(performancePage)));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, miscPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).setScreen(miscPage)));
//        if (FabricLoader.getInstance().isModLoaded("iris")) {
//            this.addDrawableChild(IrisButton.getButton(this.width / 2 - 155, this.height / 6 + 96 - 6, 310, 20, this, client));
//        }
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, ScreenTexts.DONE, (button) -> Objects.requireNonNull(client).setScreen(parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width/2, 15, 0xFFFFFF);
    }
}
