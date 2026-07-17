package neuvillette.AuroraUI.api.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import neuvillette.AuroraUI.api.Theme;

public class AuroraScreen extends Screen {
    public Theme theme = new Theme();
    protected AuroraScreen(Component title) {
        super(title);
    }

    protected AuroraScreen(Component title, Theme theme) {
        super(title);
        this.theme = theme;
    }

    public static void create(Component title, Theme theme) {
        Minecraft.getInstance().setScreen(new  AuroraScreen(title,  theme));
    }

    public static void create(Theme theme) {
        create(Component.literal("Aurora UI"), theme);
    }

    public static void create(Component title) {
        Minecraft.getInstance().setScreen(new  AuroraScreen(title,  new Theme()));
    }

    public static void create() {
        create(Component.literal("Aurora UI"));
    }
}
