package me.jacktym.aiomacro.mixins;

import me.jacktym.aiomacro.features.AHReSkin;
import me.jacktym.aiomacro.features.AHSearchExtension;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiChestMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (AHSearchExtension.auctionHouseOpen) {
            AHSearchExtension.mouseClick(mouseX, mouseY);
        } else if (AHReSkin.auctionHouseOpen) {
            ci.cancel();
            AHReSkin.mouseClick();
        }
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    public void keyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        if (AHSearchExtension.auctionHouseOpen) {
            AHSearchExtension.keyTyped(typedChar, keyCode);
            if ((AHSearchExtension.nameSelected || AHSearchExtension.loreSelected || AHReSkin.searchSelected) && String.valueOf(typedChar).equalsIgnoreCase("e")) {
                ci.cancel();
            }
        } else if (AHReSkin.auctionHouseOpen) {
            AHReSkin.keyTyped(typedChar, keyCode);
            if ((AHReSkin.nameSelected || AHReSkin.loreSelected || AHReSkin.searchSelected) && String.valueOf(typedChar).equalsIgnoreCase("e")) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (AHReSkin.auctionHouseOpen) {
            ci.cancel();

            AHReSkin.mouseX = mouseX;
            AHReSkin.mouseY = mouseY;
        }
    }

    @Inject(method = "drawSlot", at = @At("RETURN"), cancellable = true)
    public void drawSlot(Slot slotIn, CallbackInfo ci) {
        if (AHSearchExtension.auctionHouseOpen) {
            if (AHReSkin.highlightStack(slotIn.inventory.getDisplayName().getUnformattedText(), AHSearchExtension.stars, AHSearchExtension.nameSearch, AHSearchExtension.loreSearch, slotIn)) {
                AHReSkin.drawGradientRect(slotIn.xDisplayPosition, slotIn.yDisplayPosition, slotIn.xDisplayPosition + 16, slotIn.yDisplayPosition + 16, 0.5f);
            }
        }

        if (AHReSkin.auctionHouseOpen) {
            if (slotIn.slotNumber < 11 || slotIn.slotNumber > 43) {
                ci.cancel();
            }
        }
    }
}
