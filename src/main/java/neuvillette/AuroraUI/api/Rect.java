package neuvillette.AuroraUI.api;

import java.util.List;

public record Rect(int x1, int y1, int x2, int y2) {
    public static Rect from_x1y1x2y2(int x1, int y1, int x2, int y2){
        if (x1 > x2){
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        return new Rect(x1, y1, x2, y2);
    }

    public static Rect from_xywh(int x, int y, int w, int h){
        return new Rect(x, y, x + w, y + h);
    }

    public int width(){
        return x2 - x1;
    }

    public int height(){
        return y2 - y1;
    }

    public int[] to_x1y1x2y2(){
        return new int[]{x1, y1, x2, y2};
    }

    public int[] to_xywh(){
        return new int[]{x1, y1, width(), height()};
    }

    public boolean contains(int x, int y){
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    public boolean contains(double x, double y){
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    /**
     * @param padding_list 输入一个4个方向的int数值，顺序为左、上、右、下
     */
    public Rect padding(List<Integer> padding_list){
        if (padding_list.size() != 4){
            return this;
        }
        return padding(padding_list.get(0), padding_list.get(1), padding_list.get(2), padding_list.get(3));
    }

    public Rect padding(int padding_count){
        return padding(padding_count, padding_count);
    }

    public Rect padding(int top_bottom, int left_right){
        return padding(left_right, top_bottom, left_right, top_bottom);
    }

    public Rect padding(int left, int top, int right, int bottom){
        return Rect.from_x1y1x2y2(x1()  + left, y1() +  top, x2() - right,y2() - bottom);
    }

    public int midX(){
        return x1 + width()/2;
    }
    public int midY(){
        return y1 + height()/2;
    }
}
