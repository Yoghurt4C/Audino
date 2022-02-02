package mods.audino.forge;

import mods.audino.AudinoBase;
import mods.audino.Config;
import mods.audino.forge.network.MessageHandlerFML;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static mods.audino.AudinoBase.MODID;

@Mod(MODID)
public class AudinoFML {
    private static final String V = "1";
    public static SimpleChannel NW;

    public AudinoFML() {
        Config.tryInit();
        if (Config.enableLinking()) {
            NW = NetworkRegistry.newSimpleChannel(new Identifier(MODID, "nw"), () -> V, V::equals, V::equals);
            NW.registerMessage(0, MessageHandlerFML.class, MessageHandlerFML::toBytes, MessageHandlerFML::fromBytes, MessageHandlerFML::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
            AudinoBase.LH = stack -> NW.sendToServer(new MessageHandlerFML(stack));
        }
    }
}
