package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoGodPot {
    private boolean ahOpened = false;

    private boolean boughtGodPotion = false;
    private boolean claimedGodPotion = false;


    public static int openAhTicks = 0;
    public static int useGodPotionTicks = 0;

    public static HashMap<Integer, Integer> temp;
    public static int ahTicks = 0;

    @SubscribeEvent
    public void triggerAutoGodPot(TickEvent.ClientTickEvent event) {
        if (Main.mcPlayer != null && Main.mcWorld != null) {
            if (AIOMVigilanceConfig.autoGodPotion && FarmingHUD.getGodPotionTime().equals("")) {
                if (openAhTicks >= 400) {
                    openAhTicks = 0;
                    Main.mcPlayer.sendChatMessage("/ah");

                    ahOpened = true;

                    boughtGodPotion = false;
                    claimedGodPotion = false;
                }
                openAhTicks++;
            }

            if (claimedGodPotion) {
                if (useGodPotionTicks >= 20) {
                    useGodPotionTicks = 0;
                    if (Main.mcPlayer.inventory.mainInventory[0] != null) {
                        if (Main.mcPlayer.inventory.mainInventory[0].getDisplayName().contains("God Potion")) {
                            Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getStackInSlot(0));
                        } else {

                            List<String> hotBar = new ArrayList<>();

                            hotBar.add("§cGod Potion");

                            AutoHotBar.hotBarSolution = AutoHotBar.findHotBarSolution(hotBar);
                            temp = AutoHotBar.hotBarSolution;
                            AutoHotBar.swapHotBar = true;
                        }
                    }
                }
                useGodPotionTicks++;
            }
        }
    }

    @SubscribeEvent
    public void ahOpenEvent(GuiScreenEvent event) {
        try {
            if (ahOpened && !claimedGodPotion) {
                if (ahTicks >= 20) {
                    ahTicks = 0;
                    if (event.gui instanceof GuiContainer) {
                        IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

                        String chestName = chest.getDisplayName().getUnformattedText();

                        if (!boughtGodPotion) {
                            if (chestName.equals("Co-op Auction House")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                            } else if (chestName.contains("Auctions: ") && !chestName.contains("God Potion")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 47, 0, 0, Main.mcPlayer);
                            } else if (chestName.startsWith("Auctions Browser")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 48, 0, 0, Main.mcPlayer);
                            } else if (chestName.equals("Auctions: \"God Potion\"")) {
                                if (chest.getStackInSlot(52) != null) {
                                    String BINOnly = chest.getStackInSlot(52).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(2).toString();

                                    if (BINOnly.contains("▶")) {
                                        if (chest.getStackInSlot(50) != null) {
                                            String LowestPrice = chest.getStackInSlot(50).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(2).toString();

                                            if (LowestPrice.contains("▶")) {
                                                if (chest.getStackInSlot(41) != null) {
                                                    if (chest.getStackInSlot(41).getDisplayName().contains("God Potion")) {
                                                        Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 41, 0, 0, Main.mcPlayer);
                                                    }
                                                }
                                            } else {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 50, 0, 0, Main.mcPlayer);
                                            }
                                        }
                                    } else {
                                        Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 52, 0, 0, Main.mcPlayer);
                                    }
                                }
                            } else if (chestName.equals("BIN Auction View")) {
                                if (chest.getStackInSlot(13) != null) {
                                    if (chest.getStackInSlot(13).getDisplayName().contains("God Potion")) {
                                        Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                                    }
                                }
                            } else if (chestName.equals("Confirm Purchase")) {
                                if (chest.getStackInSlot(13) != null) {

                                    String GodPotionLore = chest.getStackInSlot(13).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(1).toString();

                                    if (GodPotionLore.contains("God Potion")) {
                                        Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                                        ahOpened = false;
                                    }
                                }
                            }
                        } else {
                            switch (chestName) {
                                case "Co-op Auction House":
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 13, 0, 0, Main.mcPlayer);
                                    break;
                                case "Your Bids":
                                    for (int i = 0; i <= 45; i++) {
                                        if (chest.getStackInSlot(i) != null) {
                                            if (chest.getStackInSlot(i).getDisplayName().contains("God Potion")) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, i, 0, 0, Main.mcPlayer);
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                case "BIN Auction View":
                                    if (chest.getStackInSlot(13) != null) {
                                        if (chest.getStackInSlot(13).getDisplayName().contains("God Potion")) {
                                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                                            ahOpened = false;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    if (event.gui instanceof GuiEditSign) {
                        Field sign = GuiEditSign.class.getDeclaredField("tileSign");

                        sign.setAccessible(true);

                        GuiEditSign ges = (GuiEditSign) event.gui;
                        TileEntitySign tileSign;
                        tileSign = (TileEntitySign) sign.get(ges);
                        tileSign.signText[0] = new ChatComponentText("God Potion");
                    }
                }
                ahTicks++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void clientChatReceiveEvent(ClientChatReceivedEvent e) {
        if (AIOMVigilanceConfig.autoGodPotion && FarmingHUD.getGodPotionTime().equals("")) {
            String strippedMessage = Utils.stripColor(e.message.getUnformattedText());

            if (strippedMessage.contains("You purchased God Potion for")) {
                boughtGodPotion = true;

                Main.mcPlayer.sendChatMessage("/ah");
                ahOpened = true;
            } else if (strippedMessage.contains("You claimed God Potion from")) {
                claimedGodPotion = true;
            } else if (strippedMessage.contains("The God Potion grants you powers for")) {
                AutoHotBar.hotBarSolution = temp;
                AutoHotBar.swapHotBar = true;
            }
        }
    }
}