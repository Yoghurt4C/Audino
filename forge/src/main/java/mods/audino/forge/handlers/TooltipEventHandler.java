package mods.audino.forge.handlers;

import mods.audino.AudinoBase;
import mods.audino.handlers.TooltipHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AudinoBase.MODID, value = Dist.CLIENT)
public class TooltipEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void handleTooltip(ItemTooltipEvent event) {
        TooltipHandler.appendNbt(event.getItemStack(), event.getFlags(), event.getToolTip());
    }
}
