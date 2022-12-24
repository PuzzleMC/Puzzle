package net.puzzlemc.gui.screen.page;

import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.puzzlemc.gui.screen.widget.PuzzleOptionListWidget;
import net.puzzlemc.gui.screen.widget.PuzzleWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.Objects;

public abstract class AbstractPuzzleOptionsPage extends Screen {
    public PuzzleOptionListWidget list;
    private final List<PuzzleWidget> options;
    public List<Text> tooltip = null;

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

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> Objects.requireNonNull(client).setScreen(parent)).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (client != null && client.world != null) this.list.setRenderBackground(false);
        this.list.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, textRenderer, title, width/2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        if (tooltip != null) {
            if (this.list.getFocused() != null && (this.list.getHoveredEntry() == null || this.list.getHoveredEntry().button == null || !this.list.getHoveredEntry().button.isMouseOver(mouseX, mouseY))) {
                renderTooltip(matrices, tooltip, this.list.getFocused().getX() + this.list.getFocused().getWidth(), this.list.getFocused().getY() + (this.list.getFocused().getHeight() * 2));
            }
            else renderTooltip(matrices, tooltip, mouseX, mouseY);
        }
        tooltip = null;
    }
}
