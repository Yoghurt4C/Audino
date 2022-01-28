package mods.audino;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mods.audino.network.MessageHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mods.audino.network.MessageHandler.ITEM_LINK;

public class Audino implements ModInitializer {
    public static Logger L = LogManager.getLogger("Audino");
    public static final String MODID = "audino";
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean isChatKeyPressed(int key, int scan) {
        return MinecraftClient.getInstance().options.keyChat.matchesKey(key, scan);
    }

    @Override
    public void onInitialize() {
        Config.tryInit();
        if (Config.enableLinking()) {
            ServerPlayNetworking.registerGlobalReceiver(ITEM_LINK, new MessageHandler());
        }
    }
}
