package mods.audino;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import mods.audino.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import static mods.audino.Audino.MODID;

@Mod(modid = MODID, name = "Audino", version = "${version}")
public class Audino {
    public static Logger L = LogManager.getLogger("Audino");
    public static final String MODID = "audino";
    public static SimpleNetworkWrapper NW;

    @SidedProxy(clientSide = "mods.audino.proxy.ClientProxy", serverSide = "mods.audino.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) { PROXY.init(); }

    public static boolean isAltPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    public static boolean isChatKeyPressed() {
        return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindChat.getKeyCode());
    }
}
