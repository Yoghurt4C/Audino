package mods.audino;

import mods.audino.handlers.ItemLinkHandler;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AudinoBase {
    public static Logger L = LogManager.getLogger("Audino");
    public static final String MODID = "audino";
    public static ItemLinkHandler LH;

    public static boolean isChatKeyPressed(int key, int scan) {
        return MinecraftClient.getInstance().options.chatKey.matchesKey(key, scan);
    }
}
