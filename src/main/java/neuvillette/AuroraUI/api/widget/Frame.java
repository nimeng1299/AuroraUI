package neuvillette.AuroraUI.api.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import neuvillette.AuroraUI.api.Canvas;
import neuvillette.AuroraUI.api.Rect;
import org.jetbrains.annotations.NotNull;

public class Frame extends BaseWidget<Frame> {

    public int border_alpha = 255;
    public int bg_alpha = 255;

    public Frame(Rect rect, Component message) {
        super(rect, message);
    }

    public Frame(BaseWidget<?> parent, Component message) {
        super(parent, message);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        var rect = getFillRect();


        Canvas.drawRadiusRectangle(guiGraphics, rect, getTheme().color_base_100.toRGBAColor(border_alpha), getTheme().color_base_300.toRGBAColor(bg_alpha), getTheme().border, getTheme().radius_box);

        super.renderWidget(guiGraphics, i, i1, v);
    }


    public Frame setBorderAlpha(int border_alpha) {
        this.border_alpha = border_alpha;
        return this;
    }

    public Frame setBgAlpha(int bg_alpha) {
        this.bg_alpha = bg_alpha;
        return this;
    }

    @Override
    public Rect getRect() {
        return Rect.from_xywh(getX(), getY(), getWidth(), getHeight()).padding(getPadding()).padding(getTheme().border);
    }
}
