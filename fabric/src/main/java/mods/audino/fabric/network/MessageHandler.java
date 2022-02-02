package mods.audino.fabric.network;

import io.netty.buffer.Unpooled;
import mods.audino.AudinoBase;
import mods.audino.handlers.ItemLinkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class MessageHandler implements ServerPlayNetworking.PlayChannelHandler {
    public static Identifier ITEM_LINK = new Identifier(AudinoBase.MODID, "item_link");

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        final ItemStack stack = buf.readItemStack();
        final Text link = ItemLinkHandler.getLinkMessage(player, stack);
        server.getPlayerManager().broadcast(link, MessageType.CHAT, Util.NIL_UUID);
    }

    public static void send(ItemStack stack) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeItemStack(stack);
        ClientPlayNetworking.send(ITEM_LINK, buf);
    }
}
