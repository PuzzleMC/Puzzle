package net.puzzlemc.gui.screen.page;

import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.puzzlemc.gui.screen.widget.PuzzleOptionListWidget;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.Objects;

public abstract class AbstractPuzzleOptionsPage extends Screen {
    private PuzzleOptionListWidget list;
    private final List<PuzzleWidget> options;

    public AbstractPuzzleOptionsPage(Screen parent, Text title, List<PuzzleWidget> options) {
        super(title);
        this.parent = parent;
        this.options = options;
    }
    private final Screen parent;

    @Override
    protected void init() {
        this.list = new PuzzleOptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        list.addAll(options);
        this.addSelectableChild(this.list);

        super.init();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 28, 200, 20, ScreenTexts.DONE, (button) -> Objects.requireNonNull(client).setScreen(parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (client != null && client.world != null) this.list.setRenderBackground(false);
        this.list.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, textRenderer, title, width/2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
