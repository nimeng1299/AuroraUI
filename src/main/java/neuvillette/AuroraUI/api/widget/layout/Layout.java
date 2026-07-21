package neuvillette.AuroraUI.api.widget.layout;

import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import neuvillette.AuroraUI.api.Rect;
import neuvillette.AuroraUI.api.widget.BaseWidget;

import java.util.*;

public class Layout extends BaseWidget<Layout> {

    private List<Constraint> constraints;
    private Direction direction = Direction.HORIZONTAL;

    private int margin = 0;

    private final List<Rect> splits = new ArrayList<>();

    public Layout(BaseWidget<?> parent, Component message) {
        super(parent, message);
    }

    public Layout(Rect rect, Component message) {
        super(rect, message);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public Layout Constraints(Constraint... constraints) {
        this.constraints = Arrays.asList(constraints);
        return this;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * 设置子项的排列顺序<br>
     * 默认为{@code Direction.HORIZONTAL}<br>
     * 另请参阅 {@link Direction}
     */
    public Layout Direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * 设置子项之间的间隔
     */
    public Layout setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public int getMargin() {
        return margin;
    }

    /**
     * 计算所有子项的占比<br>
     * 只需在构建时计算一次<br>
     * 用{@code Area(int i)}来获取每个子项所有的大小
     */
    public Layout Split(){
        splits.clear();
        if(getConstraints().isEmpty()){
            return this;
        }

        int len; //子项总可用的长度
        if(direction == Direction.HORIZONTAL){
            len = getWidth();
        }
        else{
            len = getHeight();
        }

        if(len <= 0){
            return this;
        }

        List<Integer> s = new ArrayList<>(); // 每项占的长度
        // 先便利一遍，用来占位
        for(Constraint constraint : getConstraints()){
            s.add(0);
        }

        // 先减去所有的margin长度
        len = len - (constraints.size() - 1) * getMargin();


        // 先计算固定长度的子项
        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(i);
            if(constraint.isLength()){
                int l = constraint.getSize();
                if(l > len){
                    s.set(i, len);
                    len = 0;
                }else{
                    s.set(i, l);
                    len = len - l;
                }
            }
        }

        int allFill = 0;    // 所有Fill的占比，用来计算比例
        int fillCount = 0;  // 还没计算的Fill个数，最后一项直接用掉剩余空间，防止有空间剩余
        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(i);
            if(constraint.isFill()){
                int l = constraint.getSize();
                allFill += l;
                fillCount += 1;
            }
        }

        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(i);
            if(constraint.isFill()){
                if(fillCount == 1){
                    s.set(i, len);
                    len = 0;
                }
                else{
                    int l = constraint.getSize();
                    int rlen = len * l / allFill;
                    s.set(i, rlen);
                    len = len - rlen;
                }
                fillCount -= 1;
            }
        }

        // 计算所有子项的区域
        int _x = getFillRect().x1();
        int _y = getFillRect().y1();
        for (int size : s){
            if(direction == Direction.HORIZONTAL){
                splits.add(Rect.from_xywh(_x, _y, size, getFillRect().height()));
                _x += (size + margin);
            }else{
                splits.add(Rect.from_xywh(_x, _y, getFillRect().width(), size));
                _y += (size + margin);
            }
        }
        return this;
    }

    /**
     * 获取某个子项所占有的区域<br>
     * 如果没找到就会返回null<br>
     * 子项的顺序是按照{@code Constraints()}传入的参数顺序来的
     */
    public Rect Area(int i){
        if (i >= splits.size() || i < 0){
            return null;
        }
        return splits.get(i);
    }

}
