package mods.audino;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TooltipHandler {
    final static String GRAY = "§7", DGRAY = "§8", BLUE = "§9", YELLOW = "§e", DGREEN = "§2", RED = "§c";
    private static final String[] COLORS = {BLUE, DGREEN, YELLOW, RED};

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        appendNbt(event.itemStack, event.toolTip);
    }

    public static void appendNbt(final ItemStack stack, final List<String> tooltip) {
        if (Config.showNBT() && stack.hasTagCompound()) {
            final NBTTagCompound tag = stack.getTagCompound();
            if (GuiContainer.isCtrlKeyDown()) {
                String[] nbt = WordUtils.wrap("§7NBT: " + format(new StringBuilder(), tag, 0), Config.getWrapAmount(), "\n", false).split("\\n");
                tooltip.addAll(Arrays.asList(nbt));
            } else {
                final String hold = Minecraft.isRunningOnMac ? "§7NBT: §8(CMD)" : "§7NBT: §8(CTRL)";
                tooltip.add(hold);
            }
        }

        if (Config.showOD()) {
            final int[] ids = OreDictionary.getOreIDs(stack);
            if (ids.length == 0) return;

            List<String> od = new ArrayList<>();
            for (int id : ids) {
                od.add(OreDictionary.getOreName(id));


            }
            if (Audino.isAltPressed()) {
                tooltip.add("§7OD: ");
                for (String s : od) {
                    tooltip.add("§8#" + s);
                }
            } else {
                tooltip.add("§7OD: §8(ALT)");
            }
        }
    }

    private static StringBuilder format(StringBuilder builder, NBTBase nbt, int level) {
        if (nbt == null) {
            return builder.append(DGRAY).append("null");
        }

        switch (nbt.getId()) {
            case Constants.NBT.TAG_END:
                return builder.append(DGRAY).append("null");
            case Constants.NBT.TAG_LIST: {
                NBTTagList list = (NBTTagList) nbt;
                builder.append(COLORS[level % COLORS.length]).append('[');

                for (int i = 0; i < list.tagCount(); i++) {
                    if (i > 0) {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    format(builder, list.getCompoundTagAt(i), level + 1);
                }

                return builder.append(COLORS[level % COLORS.length]).append(']');
            }
            case Constants.NBT.TAG_COMPOUND: {
                NBTTagCompound map = (NBTTagCompound) nbt;
                builder.append(COLORS[level % COLORS.length]).append('{');

                boolean first = true;

                for (Object key : map.func_150296_c()) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    builder.append(DGRAY).append(key).append(':').append(' ');
                    format(builder, map.getTag((String) key), level + 1);
                }

                return builder.append(COLORS[level % COLORS.length]).append('}');
            }
            case Constants.NBT.TAG_BYTE_ARRAY: {
                NBTTagByteArray list = (NBTTagByteArray) nbt;
                builder.append(COLORS[level % COLORS.length]).append('[');

                for (int i = 0; i < list.func_150292_c().length; i++) {
                    if (i > 0) {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    builder.append(DGRAY).append(list.func_150292_c()[i]);
                }

                return builder.append(COLORS[level % COLORS.length]).append(']');
            }
            case Constants.NBT.TAG_INT_ARRAY: {
                NBTTagIntArray list = (NBTTagIntArray) nbt;
                builder.append(COLORS[level % COLORS.length]).append('[');

                for (int i = 0; i < list.func_150302_c().length; i++) {
                    if (i > 0) {
                        builder.append(DGRAY).append(',').append(' ');
                    }

                    builder.append(GRAY).append(list.func_150302_c()[i]);
                }

                return builder.append(COLORS[level % COLORS.length]).append(']');
            }
            default:
                return builder.append(GRAY).append(nbt.toString());
        }
    }
}
