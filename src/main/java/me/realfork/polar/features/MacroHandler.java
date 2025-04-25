package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MacroHandler {

    public static long macroStartMillis;

    public static boolean isMacroOn = false;
    public static boolean isNetherWart = false;
    public static boolean isSugarCane = false;
    public static boolean isCocoaBean = false;
    public static boolean isOneRow = false;

    public static void toggleMacro() {
        AuctionFlipper.uuids.clear();
        AuctionFlipper.auctionUUIDToBuy = "";
        AuctionFlipper.itemBeingBought = null;
        AuctionFlipper.needToList = false;
        AuctionFlipper.needToClaim = false;
        AuctionFlipper.sentWaitingForFlips = false;
        AuctionFlipper.sentListingFlip = false;
        AuctionFlipper.sentBuyingFlip = false;
        AuctionFlipper.sentClaimingFlip = false;

        if (!isMacroOn && Main.notNull) {
            isNetherWart = false;
            isSugarCane = false;
            isCocoaBean = false;
            isOneRow = false;
            isMacroOn = true;
            macroStartMillis = Utils.currentTimeMillis();

            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);

            System.out.println(AIOMVigilanceConfig.macroType);

            if (AIOMVigilanceConfig.macroType == 0) {
                NetherWart.left = true;
                NetherWart.forward = false;
                isNetherWart = true;
                SetPlayerLook.setDefault();
                SetPlayerLook.toggled = true;
                if (AIOMVigilanceConfig.ungrab) {
                    Utils.unGrab();
                }
            } else if (AIOMVigilanceConfig.macroType == 1) {
                SugarCane.left = true;
                isSugarCane = true;
                SetPlayerLook.setDefault();
                SetPlayerLook.toggled = true;
                if (AIOMVigilanceConfig.ungrab) {
                    Utils.unGrab();
                }
            } else if (AIOMVigilanceConfig.macroType == 6) {
                MinionAura.recentClickedEntity = null;
                MinionAura.claimedMinions.clear();
                MinionAura.inMinion = false;
            } else if (AIOMVigilanceConfig.macroType == 10) {
                SugarCane.left = true;
                isCocoaBean = true;
                SetPlayerLook.setDefault();
                SetPlayerLook.toggled = true;
                if (AIOMVigilanceConfig.ungrab) {
                    Utils.unGrab();
                }
            } else if (AIOMVigilanceConfig.macroType == 11) {
                OneRow.forwards = true;
                isOneRow = true;
                SetPlayerLook.setDefault();
                SetPlayerLook.toggled = true;
                if (AIOMVigilanceConfig.ungrab) {
                    Utils.unGrab();
                }
            }
            Main.sendMarkedChatMessage("Macro Enabled!");
        } else if (isMacroOn) {
            Main.sendMarkedChatMessage("Macro Disabled!");
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);

            CarpentryMacro.claimDiamonds = true;
            CarpentryMacro.craftDiamonds = false;
            CarpentryMacro.putInBackPack = false;

            BlockRendering.renderMap.clear();

            isNetherWart = false;
            isSugarCane = false;

            SetPlayerLook.toggled = false;

            isMacroOn = false;

            if (AIOMVigilanceConfig.ungrab) {
                Utils.reGrab();
            }

            BazaarFlipper.currentManagedStack  = null;
            BazaarFlipper.manageOrderPhase = 0;
            BazaarFlipper.manageOrders = false;
            BazaarFlipper.doOrders = false;
            BazaarFlipper.buyFills = new ArrayList<>();
            BazaarFlipper.sellFills = new ArrayList<>();
            BazaarFlipper.buyOrders = new HashMap<>();
            BazaarFlipper.sellOrders = new HashMap<>();
            BazaarFlipper.buyCuts = new ArrayList<>();
            BazaarFlipper.sellCuts = new ArrayList<>();
            BazaarFlipper.buyClaims = new ArrayList<>();
            BazaarFlipper.sellClaims = new ArrayList<>();
            BazaarFlipper.key = "";
            BazaarFlipper.orderNum = 0;
            BazaarFlipper.coolDownWaitTime = 0;
            BazaarFlipper.sentWaitMessage = false;
        }
    }
}