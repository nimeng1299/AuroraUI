package neuvillette.AuroraUI.api.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import neuvillette.AuroraUI.api.Rect;
import neuvillette.AuroraUI.api.Theme;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class BaseWidget<T extends BaseWidget<T>> extends AbstractWidget {

    private Theme theme;
    private List<AbstractWidget> children = new ArrayList<>();

    private int padding_left = 0;
    private int padding_right = 0;
    private int padding_top = 0;
    private int padding_bottom = 0;

    public BaseWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public BaseWidget(Rect rect, Component message) {
        super(rect.x1(), rect.y1(), rect.width(), rect.height(), message);
    }

    public BaseWidget(BaseWidget parent, Rect rect, Component message) {
        this(rect, message);
        theme = parent.theme;
    }

    public T setTheme(Theme theme) {
        this.theme = theme;
        return (T)this;
    }

    public T setPadding(int size) {
        return setPadding(size, size);
    }

    public T setPadding(int left_right, int top_bottom) {
        return setPadding(left_right, top_bottom, left_right, top_bottom);
    }

    public T setPadding(int left, int top, int right, int bottom) {
        this.padding_left = left;
        this.padding_top = top;
        this.padding_right = right;
        this.padding_bottom = bottom;
        return (T)this;
    }

    /**
     * @return 返回一个四项的数据，分别为左、上、右、下
     */
    public List<Integer> getPadding(){
        return Arrays.asList(padding_left, padding_top, padding_right, padding_bottom);
    }

    public Theme getTheme() {
        return theme;
    }

    public List<AbstractWidget> getChildren() {
        return children;
    }

    public T addChild(Function<BaseWidget<T>, AbstractWidget> child_build){
        getChildren().add(child_build.apply(this));
        return (T)this;
    }

    /**
     * 返回子控件可用的大小
     */
    public Rect getRect() {
        return Rect.from_xywh(getX(), getY(), getWidth(), getHeight()).padding(padding_left, padding_top, padding_right, padding_bottom);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        for (AbstractWidget child : children) {
            child.render(guiGraphics, i, i1, v);
        }
    }
}
