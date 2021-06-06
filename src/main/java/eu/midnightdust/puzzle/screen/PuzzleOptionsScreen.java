package eu.midnightdust.puzzle.screen;

import eu.midnightdust.puzzle.screen.page.GraphicsPage;
import eu.midnightdust.puzzle.screen.page.MiscPage;
import eu.midnightdust.puzzle.screen.page.PerformancePage;
import eu.midnightdust.puzzle.screen.page.TexturesPage;
import net.coderbot.iris.gui.screen.ShaderPackScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

public class PuzzleOptionsScreen extends Screen {

    public PuzzleOptionsScreen(Screen parent) {
        super(new TranslatableText("puzzle.screen.title"));
        this.parent = parent;
    }
    private final Screen parent;

    @Override
    protected void init() {
        super.init();
        GraphicsPage graphicsPage = new GraphicsPage(this);
        MiscPage miscPage = new MiscPage(this);
        PerformancePage performancePage = new PerformancePage(this);
        TexturesPage texturesPage = new TexturesPage(this);

        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, graphicsPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).openScreen(graphicsPage)));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, texturesPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).openScreen(texturesPage)));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, performancePage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).openScreen(performancePage)));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, miscPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).openScreen(miscPage)));
        if (FabricLoader.getInstance().isModLoaded("iris")) {
            try {
                ShaderPackScreen shaderPackPage = new ShaderPackScreen(this);
                this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, shaderPackPage.getTitle().copy().append("..."), (button) -> Objects.requireNonNull(client).openScreen(shaderPackPage)));
            }
            catch (Exception | Error ignored) {
            }
        }
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, ScreenTexts.DONE, (button) -> Objects.requireNonNull(client).openScreen(parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width/2, 15, 0xFFFFFF);
    }
}
