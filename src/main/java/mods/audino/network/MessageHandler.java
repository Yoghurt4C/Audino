package mods.audino.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import static mods.audino.Audino.MODID;

public class MessageHandler implements ServerPlayNetworking.PlayChannelHandler {
    public static Identifier ITEM_LINK = new Identifier(MODID, "item_link");

    public static Text getLinkMessage(PlayerEntity player, ItemStack stack) {
        MutableText text = new LiteralText("<");
        text.append(player.getDisplayName().asString()).append("> ");
        if (stack.isStackable()) {
            text.append(stack.getCount() + "x ");
        }
        text.append(stack.toHoverableText());
        return text;
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        final ItemStack stack = buf.readItemStack();
        final Text link = getLinkMessage(player, stack);
        server.getPlayerManager().broadcastChatMessage(link, MessageType.CHAT, Util.NIL_UUID);
    }

    public static void send(ItemStack stack) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeItemStack(stack);
        ClientPlayNetworking.send(ITEM_LINK, buf);
    }
}
