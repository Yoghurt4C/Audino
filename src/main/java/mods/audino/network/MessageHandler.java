package mods.audino.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.io.IOException;

public class MessageHandler implements IMessageHandler<MessageHandler.Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        final ItemStack stack = message.stack;
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final IChatComponent link = getLinkMessage(player, stack);
        for (Object p : player.getServerForPlayer().playerEntities) {
            ((EntityPlayer) p).addChatComponentMessage(link);
        }
        return null;
    }

    public static IChatComponent getLinkMessage(EntityPlayer player, ItemStack stack) {
        ChatComponentText text;
        StringBuilder b = new StringBuilder();
        b.append("<").append(player.getDisplayName()).append("> ");
        if (stack.isStackable()) { b.append(stack.stackSize).append("x "); }
        text = new ChatComponentText(b.toString());
        text.appendSibling(stack.func_151000_E());
        return text;
    }

    public static class Message implements IMessage {
        ItemStack stack;

        public Message() { }

        public Message(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void fromBytes(ByteBuf bytes) {
            try {
                PacketBuffer buf = new PacketBuffer(bytes);
                this.stack = buf.readItemStackFromBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void toBytes(ByteBuf bytes) {
            try {
                PacketBuffer buf = new PacketBuffer(bytes);
                buf.writeItemStackToBuffer(stack);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
