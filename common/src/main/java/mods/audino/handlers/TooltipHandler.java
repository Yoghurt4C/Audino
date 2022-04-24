package mods.audino.handlers;

import mods.audino.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tag.TagKey;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TooltipHandler {
    final static String GRAY = "§7", DGRAY = "§8", BLUE = "§9", YELLOW = "§e", DGREEN = "§2", RED = "§c";
    private static final String[] COLORS = {BLUE, DGREEN, YELLOW, RED};

    public static void appendNbt(ItemStack stack, TooltipContext context, List<Text> tooltip) {
        if (Config.showNBT() && stack.hasNbt()) {
            if (context.isAdvanced())
                tooltip.removeIf(text -> text instanceof TranslatableText && ((TranslatableText) text).getKey().equals("item.nbt_tags"));
            final NbtCompound tag = stack.getNbt();
            if (Screen.hasControlDown()) {
                String[] nbt = WordUtils.wrap("§7NBT: " + format(new StringBuilder(), tag, 0), Config.getWrapAmount(), "\n", false).split("\\n");
                for (String s : nbt) {
                    tooltip.add(new LiteralText(s));
                }
            } else {
                final String hold = MinecraftClient.IS_SYSTEM_MAC ? "§7NBT: §8(CMD)" : "§7NBT: §8(CTRL)";
                tooltip.add(new LiteralText(hold));
            }
        }

        if (Config.showOD()) {
            Optional<RegistryKey<Item>> key = Registry.ITEM.getKey(stack.getItem());
            if (key.isPresent()) {
                Optional<RegistryEntry<Item>> entry = Registry.ITEM.getEntry(key.get());
                if (entry.isPresent()) {
                    final Collection<TagKey<Item>> tags = entry.get().streamTags().toList();
                    if (tags.size() == 0) return;

                    if (Screen.hasAltDown()) {
                        tooltip.add(new TranslatableText("audino.text.tags", "").formatted(Formatting.GRAY));
                        for (TagKey<Item> k : tags) {
                            tooltip.add(new LiteralText(" #" + k.id().toString()).formatted(Formatting.DARK_GRAY));
                        }
                    } else {
                        tooltip.add(new TranslatableText("audino.text.tags", "").formatted(Formatting.GRAY).append(new LiteralText("§8(ALT)")));
                    }
                }
            }
        }
    }

    private static StringBuilder format(StringBuilder builder, NbtElement nbt, int level) {
        if (nbt == null) {
            return builder.append(DGRAY).append("null");
        }
        switch (nbt.getType()) {
            case 0:
                return builder.append(DGRAY).append("null");
            case 9: {
                NbtList list = (NbtList) nbt;
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
                NbtCompound map = (NbtCompound) nbt;
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
                NbtByteArray list = (NbtByteArray) nbt;
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
                NbtIntArray list = (NbtIntArray) nbt;
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
