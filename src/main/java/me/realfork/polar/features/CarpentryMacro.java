package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CarpentryMacro {

    public static boolean claimDiamonds = true;
    public static boolean craftDiamonds = false;
    public static boolean putInBackPack = false;

    public static int ticks = 0;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent e) {
        if (Main.notNull && MacroHandler.isMacroOn && AIOMVigilanceConfig.macroType == 8) {
            ticks++;
            if (ticks >= AIOMVigilanceConfig.carpentryMacroSpeed) {
                ticks = 0;
                GuiScreen screen = Main.mc.currentScreen;

                if (screen == null) {
                    if (claimDiamonds) {
                        Main.sendMarkedChatMessage("Opening Bazaar!");
                        Main.mcPlayer.sendChatMessage("/bz");
                    }
                    if (craftDiamonds || putInBackPack) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(8));
                        Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getStackInSlot(8));
                    }
                } else {
                    if (screen instanceof GuiChest) {
                        IInventory chest = ((ContainerChest) (((GuiChest) screen).inventorySlots)).getLowerChestInventory();

                        String chestName = chest.getDisplayName().getUnformattedText();

                        if (claimDiamonds) {
                            if (chestName.startsWith("Bazaar")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 50, 0, 0, Main.mcPlayer);
                            } else if (chestName.contains("Co-op Bazaar Orders")) {
                                for (int z = 1; z < 3; z++) {
                                    for (int i = 0; i < 7; i++) {
                                        if (chest.getStackInSlot(i + 19) != null && chest.getStackInSlot(i + 19).getDisplayName() != null) {
                                            if (chest.getStackInSlot(i + 19).getDisplayName().contains("Enchanted Diamond")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, i + 19, 0, 0, Main.mcPlayer);
                                                break;
                                            }
                                        }
                                    }
                                }
                                claimDiamonds = false;
                                craftDiamonds = true;
                                Main.mcPlayer.closeScreen();
                                craftDiamonds = true;
                            }
                        }
                        if (craftDiamonds || putInBackPack) {
                            if (chestName.startsWith("Bazaar")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 49, 0, 0, Main.mcPlayer);
                            } else if (chestName.contains("Co-Op Bazaar Orders")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                            }

                            if (chestName.contains("SkyBlock Menu")) {
                                if (craftDiamonds) {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                                }
                                if (putInBackPack) {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 25, 0, 0, Main.mcPlayer);
                                }
                            }
                            if (chestName.contains("Craft Item")) {
                                if (craftDiamonds) {
                                    if (chest.getStackInSlot(16) != null && chest.getStackInSlot(16).getDisplayName() != null && chest.getStackInSlot(25) != null && chest.getStackInSlot(25).getDisplayName() != null && chest.getStackInSlot(34) != null && chest.getStackInSlot(34).getDisplayName() != null) {
                                        boolean shiftClick = false;
                                        for (int x = 0; x < 36; x++) {
                                            if (Main.mcPlayer.inventory.mainInventory[x] == null) {
                                                shiftClick = true;
                                            }
                                        }
                                        if (shiftClick) {
                                            if (chest.getStackInSlot(16).getDisplayName().contains("Enchanted Diamond Block")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 16, 0, 1, Main.mcPlayer);
                                            } else if (chest.getStackInSlot(25).getDisplayName().contains("Enchanted Diamond Block")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 25, 0, 1, Main.mcPlayer);
                                            } else if (chest.getStackInSlot(34).getDisplayName().contains("Enchanted Diamond Block")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 34, 0, 1, Main.mcPlayer);
                                            } else {
                                                craftDiamonds = false;
                                                putInBackPack = true;
                                            }
                                        } else {
                                            if (chest.getStackInSlot(16).getDisplayName().contains("Enchanted Diamond Block")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 16, 0, 0, Main.mcPlayer);
                                            } else if (chest.getStackInSlot(25).getDisplayName().contains("Enchanted Diamond Block")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 25, 0, 0, Main.mcPlayer);
                                            } else if (chest.getStackInSlot(34).getDisplayName().contains("Enchanted Diamond Block")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 34, 0, 0, Main.mcPlayer);
                                            } else {
                                                craftDiamonds = false;
                                                putInBackPack = true;
                                            }
                                        }
                                    }
                                }
                                if (putInBackPack) {
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 49, 0, 0, Main.mcPlayer);
                                }
                            }
                        }
                        if (chestName.contains("Storage")) {
                            if (putInBackPack) {
                                int index = 26 + AIOMVigilanceConfig.carpentryMacroSlot;

                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, index, 0, 0, Main.mcPlayer);
                            }
                            if (claimDiamonds || craftDiamonds) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 49, 0, 0, Main.mcPlayer);
                            }
                        }
                        if (chestName.contains("Backpack")) {
                            if (putInBackPack) {
                                for (int i = 0; i < 36; i++) {
                                    if (Main.mcPlayer.inventory.mainInventory[i] != null && Main.mcPlayer.inventory.mainInventory[i].getDisplayName() != null) {
                                        if (Main.mcPlayer.inventory.mainInventory[i].getDisplayName().contains("Enchanted Diamond Block")) {
                                            int index = i;

                                            if (index <= 9) {
                                                index += 36;
                                            }
                                            if (chestName.contains("Small")) {
                                                index += 9;
                                            }
                                            if (chestName.contains("Medium")) {
                                                index += 18;
                                            }
                                            if (chestName.contains("Large")) {
                                                index += 27;
                                            }
                                            if (chestName.contains("Greater")) {
                                                index += 36;
                                            }
                                            if (chestName.contains("Jumbo")) {
                                                index += 45;
                                            }
                                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, index, 0, 1, Main.mcPlayer);
                                        }
                                    }
                                }
                                putInBackPack = false;
                                claimDiamonds = true;
                                Main.mcPlayer.closeScreen();
                            }
                            if (claimDiamonds || craftDiamonds) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 0, 0, 0, Main.mcPlayer);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void chatEvent(ClientChatReceivedEvent event) {
        String message = Utils.stripColor(event.message.getUnformattedText());

        if (message.contains("Claimed") && message.contains("Enchanted Diamond")) {
            claimDiamonds = false;
            craftDiamonds = true;
            Main.mcPlayer.closeScreen();
            craftDiamonds = true;
        }
    }
}
