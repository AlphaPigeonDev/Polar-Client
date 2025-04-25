package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoCookie {

    public static int openBzTicks = 0;
    public static int useCookieTicks = 0;
    public static HashMap<Integer, Integer> temp;
    public static int bzTicks = 0;
    private boolean bzOpened = false;
    private boolean boughtCookie = false;

    @SubscribeEvent
    public void triggerAutoGodPot(TickEvent.ClientTickEvent event) {
        if (Main.mcPlayer != null && Main.mcWorld != null) {
            if (AIOMVigilanceConfig.autoCookie && FarmingHUD.getBoosterCookieTime().contains("1h")) {
                if (openBzTicks >= 400) {
                    openBzTicks = 0;
                    Main.mcPlayer.sendChatMessage("/bz");

                    bzOpened = true;

                    boughtCookie = false;
                }
                openBzTicks++;
            }

            if (boughtCookie) {
                if (useCookieTicks >= 20) {
                    useCookieTicks = 0;
                    if (Main.mcPlayer.inventory.mainInventory[0] != null) {
                        if (Main.mcPlayer.inventory.mainInventory[0].getDisplayName().contains("Booster Cookie")) {
                            Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getStackInSlot(0));
                        } else {

                            List<String> hotBar = new ArrayList<>();

                            hotBar.add("§6Booster Cookie");

                            AutoHotBar.hotBarSolution = AutoHotBar.findHotBarSolution(hotBar);
                            temp = AutoHotBar.hotBarSolution;
                            AutoHotBar.swapHotBar = true;
                        }
                    }
                }
                useCookieTicks++;
            }
        }
    }

    @SubscribeEvent
    public void ahOpenEvent(GuiScreenEvent event) {
        try {
            if (AIOMVigilanceConfig.autoCookie && bzOpened && FarmingHUD.getBoosterCookieTime().equals("")) {
                if (bzTicks >= 200) {
                    bzTicks = 0;
                    if (event.gui instanceof GuiContainer) {
                        IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

                        String chestName = chest.getDisplayName().getUnformattedText();

                        if (!boughtCookie) {
                            if (chestName.contains("Bazaar")) {
                                if (chestName.contains("Oddities")) {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                                } else {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 35, 0, 0, Main.mcPlayer);
                                }
                            } else if (chestName.contains("Booster Cookie")) {
                                if (chestName.contains("Oddities")) {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 10, 0, 0, Main.mcPlayer);
                                } else if (chestName.contains("Instant Buy")) {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 10, 0, 0, Main.mcPlayer);
                                }
                            }
                        } else {
                            if (chestName.equals("Consume Booster Cookie?")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                            }
                        }
                    }
                }
                bzTicks++;
            }
        } catch (Exception ignored) {
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void clientChatReceiveEvent(ClientChatReceivedEvent e) {
        if (AIOMVigilanceConfig.autoCookie && FarmingHUD.getBoosterCookieTime().equals("")) {
            String strippedMessage = Utils.stripColor(e.message.getUnformattedText());

            if (strippedMessage.contains("Bought 1x Booster Cookie for")) {
                boughtCookie = true;
            } else if (strippedMessage.contains("You consumed a Booster Cookie")) {
                AutoHotBar.hotBarSolution = temp;
                AutoHotBar.swapHotBar = true;
            }
        }
    }
}
