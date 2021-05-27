package mods.audino.proxy;

import mods.audino.Config;
import mods.audino.TooltipHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements Proxy {
    @Override
    public void preInit() {
        Config.tryInit();
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public void registerRenderInformation() {

    }
}
