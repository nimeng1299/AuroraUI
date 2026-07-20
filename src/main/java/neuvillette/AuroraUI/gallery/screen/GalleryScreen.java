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
import neuvillette.AuroraUI.api.widget.Frame;
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



        Frame frame = new Frame(rect, Component.literal("Frame"))
                .setTheme(new Theme())
                .setPadding(5)
                .addChild((baseWidget) ->
                        new Frame(baseWidget.getRect(), Component.literal("Frames"))
                            .setTheme(new Theme())
                            .setPadding(5)
                );
        addRenderableWidget(frame);
    }
}
