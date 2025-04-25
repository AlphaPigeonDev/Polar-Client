package me.jacktym.aiomacro.mixins;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.commands.AIOM;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class FontRendererMixin {
    @ModifyVariable(method = "renderString", at = @At(value = "HEAD"), argsOnly = true)
    private String replaceUsername(String text) {
        String returnText = text;
        try {
            if (Main.notNull) {
                if (AIOMVigilanceConfig.usernameHider && !Main.mcPlayer.getGameProfile().getName().equals("")) {
                    returnText = returnText.replace(Main.mcPlayer.getGameProfile().getName(), AIOMVigilanceConfig.usernameReplacement);
                }
                if (!AIOM.fragBotNameCache.equals("")) {
                    returnText = returnText.replace(AIOM.fragBotNameCache, "Frag Bot");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnText;
    }
}