package mods.audino;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mods.audino.proxy.Proxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import static mods.audino.Audino.MODID;

@Mod(modid = MODID, name = "Audino", version = "${version}")
public class Audino {
    public static Logger L = LogManager.getLogger("Audino");
    public static final String MODID = "audino";

    @SidedProxy(clientSide = "mods.audino.proxy.ClientProxy")
    public static Proxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit();
    }

    public static boolean isAltPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }
}
