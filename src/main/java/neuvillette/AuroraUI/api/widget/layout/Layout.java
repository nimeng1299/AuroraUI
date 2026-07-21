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

    private List<Rect> splits = new ArrayList<>();

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

    public Layout Direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public Layout setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public int getMargin() {
        return margin;
    }

    public Layout Split(){
        splits.clear();
        if(getConstraints().isEmpty()){
            return this;
        }

        int len;
        if(direction == Direction.HORIZONTAL){
            len = getWidth();
        }
        else{
            len = getHeight();
        }

        if(len <= 0){
            return this;
        }

        List<Integer> s = new ArrayList<>();
        for(Constraint constraint : getConstraints()){
            s.add(0);
        }

        len = len - (constraints.size() - 1) * getMargin();

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

        int allFill = 0;
        int fillCount = 0;
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

    public Rect Area(int i){
        if (i >= splits.size() || i < 0){
            return null;
        }
        return splits.get(i);
    }

}
