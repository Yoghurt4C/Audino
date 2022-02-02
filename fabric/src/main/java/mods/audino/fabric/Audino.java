package mods.audino.fabric;

import mods.audino.AudinoBase;
import mods.audino.Config;
import mods.audino.fabric.network.MessageHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import static mods.audino.fabric.network.MessageHandler.ITEM_LINK;

public class Audino implements ModInitializer {
    @Override
    public void onInitialize() {
        Config.tryInit();
        if (Config.enableLinking()) {
            ServerPlayNetworking.registerGlobalReceiver(ITEM_LINK, new MessageHandler());
            AudinoBase.LH = MessageHandler::send;
        }
    }
}
