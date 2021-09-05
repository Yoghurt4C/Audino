package mods.audino.proxy;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import mods.audino.Audino;
import mods.audino.Config;
import mods.audino.network.MessageHandler;

public class CommonProxy implements Proxy {
    @Override
    public void preInit() {
        Config.tryInit();
    }

    @Override
    public void init() {
        if (Config.enableLinking()) {
            Audino.NW = NetworkRegistry.INSTANCE.newSimpleChannel(Audino.MODID);
            Audino.NW.registerMessage(MessageHandler.class, MessageHandler.Message.class, 1, Side.SERVER);
        }
    }

    @Override
    public void postInit() {

    }

    @Override
    public void registerRenderInformation() {

    }
}
