package neuvillette.AuroraUI.gallery.screen;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import neuvillette.AuroraUI.AuroraUI;
import neuvillette.AuroraUI.api.Rect;
import neuvillette.AuroraUI.api.Theme;
import neuvillette.AuroraUI.api.color.Colors;
import neuvillette.AuroraUI.api.screen.AuroraScreen;
import neuvillette.AuroraUI.api.widget.Button;
import neuvillette.AuroraUI.api.widget.Frame;
import neuvillette.AuroraUI.api.widget.layout.Constraint;
import neuvillette.AuroraUI.api.widget.layout.Layout;
import org.jetbrains.annotations.NotNull;

public class GalleryScreen extends AuroraScreen {
    protected GalleryScreen(Component title) {
        super(title);
    }

    public static void create() {
        Minecraft.getInstance().setScreen(new GalleryScreen(Component.literal("Aurora UI")));
    }

    @Override
    public boolean shouldCloseOnEsc() { return true; }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public void render(@NotNull GuiGraphics gg, int mx, int my, float pt) {
        super.render(gg, mx, my, pt);

        Window win = Minecraft.getInstance().getWindow();
        int guiW = win.getGuiScaledWidth();
        int guiH = win.getGuiScaledHeight();

        int x = (int) ((guiW - guiW * 0.8) / 2);
        int y = (int) ((guiH - guiH * 0.8) / 2);
        int w = (int) (guiW * 0.8);
        int h = (int) (guiH * 0.8);

        Rect rect = Rect.from_xywh(x, y, w, h);



        Frame frame = newFrame(rect, Component.literal("Frame"));

        Layout layout = new Layout(frame, Component.literal("Frames"))
                .setTheme(new Theme())
                .setMargin(5)
                .Constraints(Constraint.Length(100), Constraint.Fill(1))
                .Split();

        Button button = new Button(layout.Area(0), Component.literal("Button"))
                .setTheme(new Theme())
                .setOnClick((button1) -> {
                    var mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        mc.gui.getChat().addMessage(Component.literal("§a这是绿色系统消息"));
                    }
                });
        layout.addChild(button);
        layout.addChild(newFrame(layout.Area(1), Component.literal("layout2")));

        frame.addChild(layout);


        addRenderableWidget(frame);
    }

    private Frame newFrame(Rect rect,  Component message) {
        return new Frame(rect, message)
            .setTheme(new Theme())
            .setPadding(5);
    }
}
