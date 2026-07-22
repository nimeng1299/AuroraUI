package neuvillette.AuroraUI.api.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import neuvillette.AuroraUI.api.Canvas;
import neuvillette.AuroraUI.api.Rect;
import neuvillette.AuroraUI.api.Theme;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Button extends BaseWidget<Button>{
    private boolean is_mouse_left_pressed = false;
    private Consumer<Button> onClick;

    private Theme.Size size =  Theme.Size.MD;

    public Button(BaseWidget<?> parent, Component message) {
        super(parent, message);
    }

    public Button(Rect rect, Component message) {
        super(rect, message);
    }

    @Override
    public void onPress() {
        onClick.accept(this);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public Button setOnClick(Consumer<Button> onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(getRealRect().contains(mouseX, mouseY)){
            is_mouse_left_pressed = true;
            onPress();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        is_mouse_left_pressed = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * 获取按钮真正的渲染矩形
     */
    public Rect getRealRect(){
        var rect = getFillRect();

        var h =  Theme.getSize(getTheme().size_field, size);
        return Rect.from_xywh(rect.x1(),  rect.midY() - h / 2, rect.width(), h);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        Canvas.drawRadiusRectangle(guiGraphics, getRealRect(), getTheme().color_base_200.toRGBAColor(255), getTheme().color_base_300.toRGBAColor(255),0, getTheme().radius_box);

        super.renderWidget(guiGraphics, i, i1, v);
    }

    public Theme.Size getSize() {
        return size;
    }

    public Button setSize(Theme.Size size) {
        this.size = size;
        return this;
    }
}
