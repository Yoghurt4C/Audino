package mods.audino.handlers;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class NbtCommandHandler {

    protected static int dumpItemStackNbt(ItemStack stack, ClientPlayerEntity player) {
        final MutableText text = new LiteralText("").append(new LiteralText("[Audino] ").formatted(Formatting.BLUE));
        if (stack.isEmpty()) {
            player.sendMessage(text.append(new TranslatableText("audino.text.handempty")), false);
            return SINGLE_SUCCESS;
        }
        if (!stack.hasTag()) {
            player.sendMessage(text.append(new TranslatableText("audino.text.handnonbt")), false);
            return SINGLE_SUCCESS;
        }
        player.sendMessage(text.append(getItemText(stack)), false);
        return SINGLE_SUCCESS;
    }

    private static Text getItemText(ItemStack stack) {
        MutableText text = new LiteralText("").append(stack.getName());
        if (stack.hasTag()) {
            text.append(new LiteralText(" ")
                    .append(new TranslatableText("audino.text.clicktocopy").formatted(Formatting.UNDERLINE, Formatting.ITALIC).styled(
                            style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, stack.getTag().toString())))));
        }
        return text;
    }

    protected static int dumpBlockNBT(BlockPos pos, ClientPlayerEntity player) {
        final World world = player.world;
        final ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock(), 1);
        final BlockEntity entity = world.getBlockEntity(pos);
        stack.setTag(entity != null ? entity.toTag(new CompoundTag()) : new CompoundTag());
        player.sendMessage(new LiteralText("").append(new LiteralText("[Audino] ").formatted(Formatting.BLUE)).
                append(getItemText(stack)), false);
        return SINGLE_SUCCESS;
    }
}