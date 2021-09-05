package mods.audino.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import mods.audino.Config;
import mods.audino.ItemLinkHandler;
import mods.audino.TooltipHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }

    @Override
    public void init() {
        super.init();
        if (Config.enableLinking()) {
            FMLCommonHandler.instance().bus().register(new ItemLinkHandler());
        }
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public void registerRenderInformation() {
        super.registerRenderInformation();
    }
}
