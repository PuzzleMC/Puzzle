package eu.midnightdust.puzzle.screen.page;

import eu.midnightdust.puzzle.screen.widget.PuzzleOptionListWidget;
import eu.midnightdust.puzzle.screen.widget.PuzzleWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Objects;

public abstract class AbstractPuzzleOptionsPage extends Screen {
    private PuzzleOptionListWidget list;
    private final List<PuzzleWidget> options;

    public AbstractPuzzleOptionsPage(Screen parent, TranslatableText title, List<PuzzleWidget> options) {
        super(title);
        this.parent = parent;
        this.options = options;
    }
    private final Screen parent;

    @Override
    protected void init() {
        this.list = new PuzzleOptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        list.addAll(options);
        this.children.add(this.list);

        super.init();

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 28, 200, 20, ScreenTexts.DONE, (button) -> {
            Objects.requireNonNull(client).openScreen(parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, textRenderer, title, width/2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
