package mods.audino.mixins;

import mods.audino.AudinoBase;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class ItemLinkMixin extends Screen {
    @Shadow
    @Nullable
    protected Slot focusedSlot;

    protected ItemLinkMixin(Text title) {
        super(title);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void sendIt(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> ctx) {
        if (Screen.hasControlDown() && AudinoBase.isChatKeyPressed(keyCode, scanCode)) {
            if (focusedSlot != null && focusedSlot.hasStack()) {
                AudinoBase.LH.send(focusedSlot.getStack());
                ctx.setReturnValue(true);
            }
        }
    }
}
