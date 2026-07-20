package neuvillette.AuroraUI.api;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import neuvillette.AuroraUI.api.color.RGBAColor;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Canvas {

    public static final  RenderType RENDER_TRIANGLE_FAN = RenderType.create(
            "aurora_triangle_fan",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLE_FAN,      // ← 关键：三角形模式
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setShaderState(RenderStateShard.RENDERTYPE_GUI_SHADER)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public static final  RenderType RENDER_QUADS = RenderType.create(
            "aurora_quads",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,      // ← 关键：三角形模式
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setShaderState(RenderStateShard.RENDERTYPE_GUI_SHADER)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );


    private static final int NUM_POINTS = 72;
    private static final double ANGLE_STEP = 2 * Math.PI / NUM_POINTS;

    // 预计算所有角度的 cos/sin（0°, 5°, 10°, …）
    private static final double[] COS_TABLE = new double[NUM_POINTS];
    private static final double[] SIN_TABLE = new double[NUM_POINTS];

    static {
        for (int i = 0; i < NUM_POINTS; i++) {
            double angle = i * ANGLE_STEP;
            COS_TABLE[i] = Math.cos(angle);
            SIN_TABLE[i] = Math.sin(angle);
        }
    }

    private static void draw(GuiGraphics guiGraphics, RenderType renderType, BiConsumer<VertexConsumer, Matrix4f> render){
        VertexConsumer consumer = guiGraphics.bufferSource().getBuffer(renderType);
        Matrix4f mat = guiGraphics.pose().last().pose();
        render.accept(consumer, mat);
        guiGraphics.flush();
    }

    /**
     * 绘制一个长方形
     *
     * @param rect              矩形大小
     * @param fillColor         底色
     * @param outlineColor      边框颜色
     * @param borderSize        边框大小（向内收缩）
     */
    public static void drawRectangle(GuiGraphics guiGraphics, Rect rect, RGBAColor fillColor, RGBAColor outlineColor, int borderSize) {
        if (borderSize <= 0) {
            drawFillRectangle(guiGraphics, rect,fillColor);
        }else{
            drawFillRectangle(guiGraphics, rect, outlineColor);

            Rect r1 = Rect.from_x1y1x2y2(rect.x1() + borderSize, rect.y1() + borderSize, rect.x2() - borderSize, rect.y2() - borderSize);
            drawFillRectangle(guiGraphics, r1, fillColor);
        }

    }

    /**
     * 画一个填充矩形
     *
     * @param rect      矩形大小
     * @param color     填充颜色
     */
    public static void drawFillRectangle(GuiGraphics guiGraphics, Rect rect, RGBAColor color) {
        draw(guiGraphics, RENDER_QUADS, (consumer, mat) -> {
            consumer.addVertex(mat, rect.x1(), rect.y1(), 0).setColor(color.red(), color.green(), color.blue(), color.alpha());
            consumer.addVertex(mat, rect.x1(), rect.y2(), 0).setColor(color.red(), color.green(), color.blue(), color.alpha());
            consumer.addVertex(mat, rect.x2(), rect.y2(), 0).setColor(color.red(), color.green(), color.blue(), color.alpha());
            consumer.addVertex(mat, rect.x2(), rect.y1(), 0).setColor(color.red(), color.green(), color.blue(), color.alpha());
        });

    }

    /**
     * 绘制一个实心圆角矩形（带边框）—— 用已有函数组合
     * <p>
     * 将圆角矩形拆解为 3 个矩形填充（中、左、右）+
     * 4 个四分之一圆角扇形（drawArc），
     * 以及 4 条直边框，避免逐像素扫描的性能问题。
     *
     * @param guiGraphics  GuiGraphics 用于渲染
     * @param rect         矩形大小
     * @param fillColor    底色
     * @param outlineColor 边框颜色
     * @param borderSize   边框厚度
     * @param radius       圆角半径
     */
    public static void drawRadiusRectangle(GuiGraphics guiGraphics, Rect rect, RGBAColor fillColor, RGBAColor outlineColor, int borderSize, int radius) {
        int x1 = rect.x1(), y1 = rect.y1(), x2 = rect.x2(), y2 = rect.y2();
        if (radius <= 0 || x2 <= x1 || y2 <= y1) {
            drawRectangle(guiGraphics, rect, outlineColor, fillColor, borderSize);
            return;
        }

        // 圆角圆心坐标，左上右上左下右下
        int r1_x = rect.x1() + radius, r1_y = rect.y1() + radius;
        int r2_x = rect.x2() - radius, r2_y = rect.y1() + radius;
        int r3_x = rect.x1() + radius, r3_y = rect.y2() - radius;
        int r4_x = rect.x2() - radius, r4_y = rect.y2() - radius;

        drawRightAngleArc(guiGraphics, r1_x, r1_y, radius, fillColor, outlineColor, borderSize, 2);
        drawRightAngleArc(guiGraphics, r2_x, r2_y, radius, fillColor, outlineColor, borderSize, 1);
        drawRightAngleArc(guiGraphics, r3_x, r3_y, radius, fillColor, outlineColor, borderSize, 3);
        drawRightAngleArc(guiGraphics, r4_x, r4_y, radius, fillColor, outlineColor, borderSize, 4);

        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(rect.x1(), rect.y1() + radius, r3_x, r3_y), outlineColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(rect.x1() + borderSize, rect.y1() + radius, r3_x, r3_y), fillColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(rect.x1() + radius, rect.y1(), r2_x, r2_y), outlineColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(rect.x1() + radius, rect.y1() + borderSize, r2_x, r2_y), fillColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(r2_x, r2_y, rect.x2(), rect.y2() - radius), outlineColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(r2_x, r2_y, rect.x2() - borderSize, rect.y2() - radius), fillColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(r3_x, r3_y, rect.x2() - radius, rect.y2()), outlineColor);
        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(r3_x, r3_y, rect.x2() - radius, rect.y2() - borderSize), fillColor);

        drawFillRectangle(guiGraphics, Rect.from_x1y1x2y2(r1_x, r1_y, r4_x, r4_y), fillColor);
    }

    /**
     * 绘制一个实心圆形（带边框）
     *
     * @param guiGraphics  GuiGraphics 用于渲染
     * @param cx           圆心 x 坐标
     * @param cy           圆心 y 坐标
     * @param r            半径
     * @param fillColor    内部填充颜色（ARGB）
     * @param outlineColor 边框颜色（ARGB）
     * @param borderSize   边框厚度
     */
    public static void drawCircle(GuiGraphics guiGraphics, int cx, int cy, int r,
                                  RGBAColor fillColor, RGBAColor outlineColor, int borderSize) {
        if (borderSize <= 0) {
            drawFillCircle(guiGraphics, cx, cy, r, fillColor);
        }else if(r <= borderSize) {
            drawFillCircle(guiGraphics, cx, cy, r, outlineColor);
        }else{
            drawFillCircle(guiGraphics, cx, cy, r, outlineColor);
            drawFillCircle(guiGraphics, cx, cy, r - borderSize, fillColor);
        }

    }

    /**
     * 画一个实心的圆
     *
     * @param cx        圆心坐标x
     * @param cy        圆心坐标y
     * @param r         半径
     * @param color     填充颜色
     */
    public static void drawFillCircle(GuiGraphics guiGraphics, int cx, int cy, int r, RGBAColor color){
        draw(guiGraphics, RENDER_TRIANGLE_FAN, (consumer, mat) -> {
            consumer.addVertex(mat, cx, cy, 0).setColor(color.red(), color.green(), color.blue(), color.alpha());
            for (int i = 0; i <= NUM_POINTS; i++) {
                int idx = i % NUM_POINTS;
                double x = cx + r * COS_TABLE[idx];
                double y = cy - r * SIN_TABLE[idx];
                consumer.addVertex(mat, (float) x, (float) y, 0).setColor(color.red(), color.green(), color.blue(), color.alpha());
            }
        });
    }

    /**
     * 画一个 90° 圆弧（带边框）
     *
     * @param cx            圆心 x
     * @param cy            圆心 y
     * @param r             半径
     * @param fillColor     内部填充颜色（ARGB）
     * @param outlineColor  边框颜色（ARGB）
     * @param quadrant      象限 1-4
     *                      <br>1 = 右上（right → top）
     *                      <br>2 = 左上（top → left）
     *                      <br>3 = 左下（left → bottom）
     *                      <br>4 = 右下（bottom → right）
     */
    public static void drawRightAngleArc(GuiGraphics guiGraphics, int cx, int cy, int r,
                                         RGBAColor fillColor, RGBAColor outlineColor, int borderSize, int quadrant) {
        if (quadrant <= 0 || quadrant > 4 || r <= 0) return;
        if (borderSize <= 0) {
            drawFillRightAngleArc(guiGraphics, cx, cy, r, fillColor, quadrant);
        }else if(r <= borderSize) {
            drawFillRightAngleArc(guiGraphics, cx, cy, r, outlineColor, quadrant);
        }else{
            drawFillRightAngleArc(guiGraphics, cx, cy, r, outlineColor, quadrant);
            drawFillRightAngleArc(guiGraphics, cx, cy, r - borderSize, fillColor, quadrant);
        }

    }

    /**
     * 画一个 90° 填充圆弧（扇形）
     *
     * @param cx       圆心 x
     * @param cy       圆心 y
     * @param r        半径
     * @param color    填充颜色
     * @param quadrant 象限 1-4
     *                 <br>1 = 右上（right → top）
     *                 <br>2 = 左上（top → left）
     *                 <br>3 = 左下（left → bottom）
     *                 <br>4 = 右下（bottom → right）
     */
    public static void drawFillRightAngleArc(GuiGraphics guiGraphics, int cx, int cy, int r, RGBAColor color, int quadrant) {
        if (quadrant <= 0 || quadrant > 4 || r <= 0) return;

        int ptsPerQuadrant = NUM_POINTS / 4;               // 72/4 = 18
        int startIdx = (quadrant - 1) * ptsPerQuadrant;    // 0, 18, 36, 54
        int endIdx   = quadrant * ptsPerQuadrant;          // 18, 36, 54, 72

        draw(guiGraphics, RENDER_TRIANGLE_FAN, (consumer, mat) -> {
            // 圆心（扇形枢纽）
            consumer.addVertex(mat, cx, cy, 0)
                    .setColor(color.red(), color.green(), color.blue(), color.alpha());
            // 弧上顶点（含起止边界，共 19 个点 = 18 段 + 闭合）
            for (int i = startIdx; i <= endIdx; i++) {
                int idx = i % NUM_POINTS;
                double x = cx + r * COS_TABLE[idx];
                double y = cy - r * SIN_TABLE[idx];
                consumer.addVertex(mat, (float) x, (float) y, 0)
                        .setColor(color.red(), color.green(), color.blue(), color.alpha());
            }
        });
    }


}
