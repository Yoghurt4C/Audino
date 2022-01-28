package mods.audino.handlers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class NbtCommandHandler {
    public static LiteralArgumentBuilder<FabricClientCommandSource> register() {
        return literal("nbt")
                .then(literal("block").executes(context -> {
                    final ClientPlayerEntity player = context.getSource().getPlayer();
                    final HitResult result = player.raycast(3, MinecraftClient.getInstance().getTickDelta(), false);
                    BlockPos pos;
                    if (result.getType() == HitResult.Type.BLOCK) pos = ((BlockHitResult) result).getBlockPos();
                    else pos = player.getBlockPos().down();
                    return dumpBlockNBT(pos, player);
                })).then(literal("hand").executes(context -> {
                    final ClientPlayerEntity player = context.getSource().getPlayer();
                    final ItemStack hand = player.getStackInHand(Hand.MAIN_HAND);
                    final ItemStack off_hand = player.getStackInHand(Hand.OFF_HAND);
                    if (hand.isEmpty() && !off_hand.isEmpty()) {
                        return dumpItemStackNbt(off_hand, player);
                    }
                    return dumpItemStackNbt(hand, player);
                }).then(literal("main").executes(context -> {
                    final ClientPlayerEntity player = context.getSource().getPlayer();
                    final ItemStack hand = player.getStackInHand(Hand.MAIN_HAND);
                    return dumpItemStackNbt(hand, player);
                })).then(literal("off").executes(context -> {
                    final ClientPlayerEntity player = context.getSource().getPlayer();
                    final ItemStack hand = player.getStackInHand(Hand.OFF_HAND);
                    return dumpItemStackNbt(hand, player);
                })));
    }

    private static int dumpItemStackNbt(ItemStack stack, ClientPlayerEntity player) {
        final MutableText text = new LiteralText("").append(new LiteralText("[Audino] ").formatted(Formatting.BLUE));
        if (stack.isEmpty()) {
            player.sendMessage(text.append(new TranslatableText("audino.text.handempty")), false);
            return SINGLE_SUCCESS;
        }
        if (!stack.hasNbt()) {
            player.sendMessage(text.append(new TranslatableText("audino.text.handnonbt")), false);
            return SINGLE_SUCCESS;
        }
        player.sendMessage(text.append(getItemText(stack, true)), false);
        return SINGLE_SUCCESS;
    }

    private static Text getItemText(ItemStack stack, boolean copyText) {
        MutableText text = new LiteralText("").append(stack.getName());
        if (copyText && stack.hasNbt()) {
            text.append(new LiteralText(" ")
                    .append(new TranslatableText("audino.text.clicktocopy").formatted(Formatting.UNDERLINE, Formatting.ITALIC).styled(
                            style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, stack.getNbt().toString())))));
        }
        return text;
    }

    private static int dumpBlockNBT(BlockPos pos, ClientPlayerEntity player) {
        final World world = player.world;
        final ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock(), 1);
        final BlockEntity entity = world.getBlockEntity(pos);
        stack.setNbt(entity != null ? entity.createNbt() : new NbtCompound());
        player.sendMessage(new LiteralText("").append(new LiteralText("[Audino] ").formatted(Formatting.BLUE)).
                append(getItemText(stack, true)), false);
        return SINGLE_SUCCESS;
    }
}