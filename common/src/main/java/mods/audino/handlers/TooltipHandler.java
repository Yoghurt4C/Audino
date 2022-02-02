package mods.audino.handlers;

import mods.audino.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collection;
import java.util.List;

public class TooltipHandler {
    final static String GRAY = "§7", DGRAY = "§8", BLUE = "§9", YELLOW = "§e", DGREEN = "§2", RED = "§c";
    private static final String[] COLORS = {BLUE, DGREEN, YELLOW, RED};

    public static void appendNbt(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        if (Config.showNBT() && stack.hasTag()) {
            if (context.isAdvanced())
                tooltip.removeIf(text -> text instanceof TranslatableText && ((TranslatableText) text).getKey().equals("item.nbt_tags"));
            final CompoundTag tag = stack.getTag();
            if (Screen.hasControlDown()) {
                String formatted = format(new StringBuilder(), tag, 0).toString();
                String[] nbt = WordUtils.wrap("§7NBT: " + formatted, Config.getWrapAmount(), "\n", false).split("\\n");
                for (String s : nbt) {
                    tooltip.add(new LiteralText(s));
                }
            } else {
                final String hold = MinecraftClient.IS_SYSTEM_MAC ? "§7NBT: §8(CMD)" : "§7NBT: §8(CTRL)";
                tooltip.add(new LiteralText(hold));
            }
        }

        if (Config.showOD()) {
            final Collection<Identifier> tags = ItemTags.getTagGroup().getTagsFor(stack.getItem());
            if (tags.size() == 0) return;


            if (Screen.hasAltDown()) {
                tooltip.add(new TranslatableText("audino.text.tags", "").formatted(Formatting.GRAY));
                for (Identifier id : tags) {
                    tooltip.add(new LiteralText(" #" + id.toString()).formatted(Formatting.DARK_GRAY));
                }
            } else {
                tooltip.add(new TranslatableText("audino.text.tags", "").formatted(Formatting.GRAY).append(new LiteralText("§8(ALT)")));
            }
        }
    }

    private static StringBuilder format(StringBuilder builder, Tag nbt, int level) {
        if (nbt == null) {
            return builder.append(DGRAY).append("null");
        }
        switch (nbt.getType()) {
            case 0:
                return builder.append(DGRAY).append("null");
            case 9: {
                ListTag list = (ListTag) nbt;
                builder.append(COLORS[level % COLORS.length]).append('[');

                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    format(builder, list.get(i), level + 1);
                }

                return builder.append(COLORS[level % COLORS.length]).append(']');
            }
            case 10: {
                CompoundTag map = (CompoundTag) nbt;
                builder.append(COLORS[level % COLORS.length]).append('{');

                boolean first = true;

                for (String key : map.getKeys()) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    builder.append(DGRAY).append(key).append(':').append(' ');
                    format(builder, map.get(key), level + 1);
                }

                return builder.append(COLORS[level % COLORS.length]).append('}');
            }
            case 7: {
                ByteArrayTag list = (ByteArrayTag) nbt;
                builder.append(COLORS[level % COLORS.length]).append('[');

                for (int i = 0; i < list.getByteArray().length; i++) {
                    if (i > 0) {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    builder.append(DGRAY).append(list.getByteArray()[i]);
                }

                return builder.append(COLORS[level % COLORS.length]).append(']');
            }
            case 11: {
                IntArrayTag list = (IntArrayTag) nbt;
                builder.append(COLORS[level % COLORS.length]).append('[');

                for (int i = 0; i < list.getIntArray().length; i++) {
                    if (i > 0) {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    builder.append(GRAY).append(list.getIntArray()[i]);
                }

                return builder.append(COLORS[level % COLORS.length]).append(']');
            }
            default:
                return builder.append(GRAY).append(nbt);
        }
    }
}
