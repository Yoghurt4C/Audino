package mods.audino.forge.network;

import mods.audino.handlers.ItemLinkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageHandlerFML {
    private final ItemStack stack;

    public static void onMessage(MessageHandlerFML message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            final ItemStack stack = message.stack;
            final ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            final Text link = ItemLinkHandler.getLinkMessage(player, stack);
            player.getServer().getPlayerManager().broadcastChatMessage(link, MessageType.CHAT, Util.NIL_UUID);
        });
        ctx.get().setPacketHandled(true);
    }

    public MessageHandlerFML(ItemStack stack) {
        this.stack = stack;
    }

    public static MessageHandlerFML fromBytes(PacketByteBuf buf) {
        return new MessageHandlerFML(buf.readItemStack());
    }

    public void toBytes(PacketByteBuf buf) {
        buf.writeItemStack(stack);
    }
}
