/*
package mods.audino.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import mods.audino.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.List;

public class LinkedItemIconHandler {
    private static int chatX, chatY;
    static ItemStack invalid = new ItemStack(Blocks.bedrock);

    public static IChatComponent createStackComponent(IChatComponent component) {
        if (Config.fancyItemLinks()) {
            IChatComponent out = new ChatComponentText("   ");
            out.setChatStyle(component.getChatStyle().createDeepCopy());
            return out.appendSibling(component);
        }
        return component;
    }

    @SubscribeEvent
    public void getChatPos(RenderGameOverlayEvent.Chat event) {
        chatX = event.posX;
        chatY = event.posY;
    }

    @SubscribeEvent
    public void renderSymbols(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiIngame gameGui = mc.ingameGUI;
        GuiNewChat chatGui = gameGui.getChatGUI();
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            int updateCounter = gameGui.getUpdateCounter();
            List<ChatLine> lines = ReflectionHelper.getPrivateValue(GuiNewChat.class, chatGui, "chatLines");
            //ClientReflectiveAccessor.getChatDrawnLines(chatGui);
            int shift = ReflectionHelper.getPrivateValue(GuiNewChat.class, chatGui, "field_146250_j");
            //ClientReflectiveAccessor.getScrollPos(chatGui);

            int idx = shift;

            while (idx < lines.size() && (idx - shift) < chatGui.func_146232_i()) {
                ChatLine line = lines.get(idx);
                String before = "";

                String currentText = line.func_151461_a().getUnformattedTextForChat();
                if (currentText != null && currentText.startsWith("   "))
                    render(mc, chatGui, updateCounter, before, line, idx - shift, line.func_151461_a());
                before += currentText;

                for (Object obj : line.func_151461_a().getSiblings()) {
                    IChatComponent sibling = (IChatComponent) obj;
                    currentText = sibling.getUnformattedTextForChat();
                    if (currentText != null && currentText.startsWith("   "))
                        render(mc, chatGui, updateCounter, before, line, idx - shift, (IChatComponent) sibling.getSiblings().get(0));
                    before += currentText;
                }

                idx++;
            }
        }
    }
    
    private static void render(Minecraft mc, GuiNewChat chatGui, int updateCounter, String before, ChatLine line, int lineHeight, IChatComponent component) {
        ChatStyle style = component.getChatStyle();
        HoverEvent hoverEvent = style.getChatHoverEvent();
        if (hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_ITEM) {
            ItemStack stack = invalid;

            try {
                NBTTagCompound textValue = (NBTTagCompound) JsonToNBT.func_150315_a(hoverEvent.getValue().getUnformattedText());
                stack = ItemStack.loadItemStackFromNBT(textValue);
            } catch (NBTException ignored) {
                // NO-OP
            }

            int timeSinceCreation = updateCounter - line.getUpdatedCounter();
            if (chatGui.getChatOpen()) timeSinceCreation = 0;

            if (timeSinceCreation < 200) {
                float chatOpacity = mc.gameSettings.chatOpacity * 0.9f + 0.1f;
                float fadeOut = MathHelper.clamp_float((1 - timeSinceCreation / 200f) * 10, 0, 1);
                float alpha = fadeOut * fadeOut * chatOpacity;

                int x = chatX + 3 + mc.fontRenderer.getStringWidth(before);
                int y = chatY - mc.fontRenderer.FONT_HEIGHT * lineHeight;

                if (alpha > 0) {
                    RenderHelper.enableGUIStandardItemLighting();
                    ALPHA_VALUE = ((int) (alpha * 255) << 24);

                    RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x, y);

                    ALPHA_VALUE = -1;
                    RenderHelper.disableStandardItemLighting();
                }
            }
        }
    }

    public static int transformColor(int src) {
        if (ALPHA_VALUE == -1)
            return src;
        return (src & RGB_MASK) | ALPHA_VALUE;
    }

    public static final int RGB_MASK = 0x00FFFFFF;
    private static int ALPHA_VALUE = -1;

    /*
    private static void renderItemIntoGUI(Minecraft mc, RenderItem render, ItemStack stack, int x, int y) {
        renderItemModelIntoGUI(mc, render, stack, x, y, render.getItemModelWithOverrides(stack, null, null));
    }

    @SideOnly(Side.CLIENT)
    private static void renderItemModelIntoGUI(Minecraft mc, RenderItem render, ItemStack stack, int x, int y, IBakedModel model) {
        TextureManager textureManager = mc.getTextureManager();

        GlStateManager.pushMatrix();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(-4, -4, -4);
        ClientReflectiveAccessor.setupGuiTransform(render, x, y, model.isGui3d());
        GlStateManager.scale(0.65, 0.65, 0.65);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
        render.renderItem(stack, model);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }
}
     */
