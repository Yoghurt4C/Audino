package mods.audino;

import mods.audino.handlers.NbtCommandHandler;
import mods.audino.handlers.TooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class AudinoClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(TooltipHandler::appendNbt);
        ClientCommandManager.DISPATCHER.register(NbtCommandHandler.register());
    }
}
