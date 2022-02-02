package mods.audino.fabric.handlers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import mods.audino.handlers.NbtCommandHandler;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class NbtCommandFabric extends NbtCommandHandler {
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
}
