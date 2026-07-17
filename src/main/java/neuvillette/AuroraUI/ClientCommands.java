package neuvillette.AuroraUI;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import neuvillette.AuroraUI.gallery.screen.GalleryScreen;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientCommands {
    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
            Commands.literal("aurora")
                .executes(context -> {
                    GalleryScreen.create();
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
