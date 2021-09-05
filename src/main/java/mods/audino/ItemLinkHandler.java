package mods.audino;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import mods.audino.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ItemLinkHandler {

    private boolean held = false;
    private final String[] theSlot = {"theSlot", "field_147006_u", "u"};

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            Minecraft mc = FMLClientHandler.instance().getClient();
            if (mc.theWorld != null) {
                if (mc.currentScreen != null) {
                    synchronized (this) {
                        if (mc.currentScreen instanceof GuiContainer) {
                            if (GuiContainer.isCtrlKeyDown() && Audino.isChatKeyPressed()) {
                                if (held) return;
                                Slot slot = ReflectionHelper.getPrivateValue(GuiContainer.class, (GuiContainer) mc.currentScreen, theSlot);
                                if (slot != null && slot.getHasStack()) {
                                    ItemLinkHandler.sendItem(slot.getStack());
                                }
                                held = true;
                            } else {
                                held = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void sendItem(ItemStack stack) {
        Audino.NW.sendToServer(new MessageHandler.Message(stack));
    }
}
