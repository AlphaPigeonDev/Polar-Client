package me.jacktym.aiomacro.keybinds;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.ParticleHandler;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.features.AutoHotBar;
import me.jacktym.aiomacro.features.MacroHandler;
import me.jacktym.aiomacro.features.SetPlayerLook;
import me.jacktym.aiomacro.proxy.ClientProxy;
import me.jacktym.aiomacro.rendering.LineRendering;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.jetbrains.annotations.NotNull;

public class ModInputHandler {

    @SubscribeEvent
    public void onKeyInput(@NotNull KeyInputEvent event) {
        KeyBinding[] keyBindings = ClientProxy.keyBindings;

        if (keyBindings[0].isPressed()) {
            MacroHandler.toggleMacro();
        }
        if (keyBindings[1].isPressed()) {
            Main.gui = AIOMVigilanceConfig.INSTANCE.gui();
        }
        if (keyBindings[2].isPressed()) {
            if (AIOMVigilanceConfig.hotBarProfileOne.equals("") || !AutoHotBar.hotBars.containsKey(AIOMVigilanceConfig.hotBarProfileOne)) {
                Main.sendMarkedChatMessage("Error loading HotBar profile! Please set your name in /aiom");
            } else {
                AutoHotBar.hotBarSolution = AutoHotBar.findHotBarSolution(AutoHotBar.hotBars.get(AIOMVigilanceConfig.hotBarProfileOne));
                AutoHotBar.swapHotBar = true;
            }
        }
        if (keyBindings[3].isPressed()) {
            if (AIOMVigilanceConfig.hotBarProfileTwo.equals("") || !AutoHotBar.hotBars.containsKey(AIOMVigilanceConfig.hotBarProfileTwo)) {
                Main.sendMarkedChatMessage("Error loading HotBar profile! Please set your name in /aiom");
            } else {
                AutoHotBar.hotBarSolution = AutoHotBar.findHotBarSolution(AutoHotBar.hotBars.get(AIOMVigilanceConfig.hotBarProfileTwo));
                AutoHotBar.swapHotBar = true;
            }
        }
        if (keyBindings[4].isPressed()) {
            if (AIOMVigilanceConfig.hotBarProfileThree.equals("") || !AutoHotBar.hotBars.containsKey(AIOMVigilanceConfig.hotBarProfileThree)) {
                Main.sendMarkedChatMessage("Error loading HotBar profile! Please set your name in /aiom");
            } else {
                AutoHotBar.hotBarSolution = AutoHotBar.findHotBarSolution(AutoHotBar.hotBars.get(AIOMVigilanceConfig.hotBarProfileThree));
                AutoHotBar.swapHotBar = true;
            }
        }
        if (keyBindings[5].isPressed()) {
            if (!AIOMVigilanceConfig.vClipKeyBindAmount.equals("")) {
                Main.mcPlayer.setPosition(Main.mcPlayer.posX, Main.mcPlayer.posY + Double.parseDouble(AIOMVigilanceConfig.vClipKeyBindAmount), Main.mcPlayer.posZ);
            }
        }
        if (keyBindings[6].isPressed()) {
            if (!AIOMVigilanceConfig.vClipKeyBindAmount.equals("")) {
                if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= -135 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= -45) {
                    Main.mcPlayer.setPosition(Main.mcPlayer.posX + Double.parseDouble(AIOMVigilanceConfig.hClipKeyBindAmount), Main.mcPlayer.posY, Main.mcPlayer.posZ);
                } else if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= -45 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= 45) {
                    Main.mcPlayer.setPosition(Main.mcPlayer.posX, Main.mcPlayer.posY, Main.mcPlayer.posZ + Double.parseDouble(AIOMVigilanceConfig.hClipKeyBindAmount));
                } else if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= 45 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= 135) {
                    Main.mcPlayer.setPosition(Main.mcPlayer.posX - Double.parseDouble(AIOMVigilanceConfig.hClipKeyBindAmount), Main.mcPlayer.posY, Main.mcPlayer.posZ);
                } else if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= 135 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= -135) {
                    Main.mcPlayer.setPosition(Main.mcPlayer.posX, Main.mcPlayer.posY, Main.mcPlayer.posZ - Double.parseDouble(AIOMVigilanceConfig.hClipKeyBindAmount));
                }
            }
        }
        if (keyBindings[7].isPressed()) {
            Main.sendMarkedChatMessage("Refreshed Diana Waypoints!");
            Main.mcWorld.removeWorldAccess(new ParticleHandler());
        }
        if (keyBindings[8].isPressed()) {
            AIOMVigilanceConfig.cancelPackets = !AIOMVigilanceConfig.cancelPackets;
            if (AIOMVigilanceConfig.cancelPackets) {
                Main.sendMarkedChatMessage("Path Recording Enabled");
            } else {
                Main.sendMarkedChatMessage("Path Recording Disabled");
            }
        }
        if (keyBindings[9].isPressed()) {
            LineRendering.recording = !LineRendering.recording;
            if (LineRendering.recording) {
                Main.sendMarkedChatMessage("Path Recording Enabled");
            } else {
                Main.sendMarkedChatMessage("Path Recording Disabled");
            }
        }
    }
}
