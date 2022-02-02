package mods.audino.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface ItemLinkHandler {
    static Text getLinkMessage(PlayerEntity player, ItemStack stack) {
        MutableText text = new LiteralText("<");
        text.append(player.getDisplayName()).append("> ");
        if (stack.isStackable()) {
            text.append(stack.getCount() + "x ");
        }
        text.append(stack.toHoverableText());
        return text;
    }

    void send(ItemStack stack);
}
