package me.jacktym.aiomacro.features;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class AuctionFlipper {
    public static String auctionUUIDToBuy = "";

    public static List<String> uuids = new ArrayList<>();

    public static boolean needToClaim = false;
    public static boolean needToList = false;

    public static ItemStack itemBeingBought = null;

    public static String itemPrice = null;

    public static int ticks = 20;
    public static int claimRelistTicks = 0;

    public static boolean sentWaitingForFlips = false;
    public static boolean sentBuyingFlip = false;
    public static boolean sentClaimingFlip = false;
    public static boolean sentListingFlip = false;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (Main.notNull && MacroHandler.isMacroOn && AIOMVigilanceConfig.macroType == 9 && Main.mcWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null && Main.mcWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null && Main.mcWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().contains("SKYBLOCK")) {
            if (!auctionUUIDToBuy.equals("")) {
                if (Main.mc.currentScreen == null) {
                    if (ticks >= 20) {
                        Main.mcPlayer.sendChatMessage("/viewauction " + auctionUUIDToBuy);
                        ticks = 0;
                    } else {
                        ticks++;
                    }
                } else if (Main.mc.currentScreen instanceof GuiChest) {
                    IInventory chest = ((ContainerChest) (((GuiChest) Main.mc.currentScreen).inventorySlots)).getLowerChestInventory();

                    String chestName = chest.getDisplayName().getUnformattedText();

                    switch (chestName) {
                        case "BIN Auction View":
                            itemBeingBought = chest.getStackInSlot(13);
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                        case "Confirm Purchase":
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                    }
                }
            } else if (!AIOMVigilanceConfig.claimRelist || (!needToClaim && !needToList) || itemBeingBought == null || itemPrice.equals("")) {
                ticks = 20;
                grabNextFlip();
            } else {
                claimRelistTicks++;
                if (claimRelistTicks >= AIOMVigilanceConfig.claimRelistTickSpeed) {
                    claimRelistTicks = 0;
                    if (needToClaim) {

                        if (AIOMVigilanceConfig.sendStatusMessages && !sentClaimingFlip) {
                            Main.sendMarkedChatMessage("Claiming Item!");
                            sentClaimingFlip = true;
                        }

                        if (Main.mc.currentScreen == null) {
                            Main.mcPlayer.sendChatMessage("/viewauction " + auctionUUIDToBuy);
                        } else if (Main.mc.currentScreen instanceof GuiChest) {
                            IInventory chest = ((ContainerChest) (((GuiChest) Main.mc.currentScreen).inventorySlots)).getLowerChestInventory();

                            String chestName = chest.getDisplayName().getUnformattedText();

                            if (chestName.equals("BIN Auction View")) {
                                itemBeingBought = chest.getStackInSlot(13);
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                            }
                        }
                    } else {

                        if (AIOMVigilanceConfig.sendStatusMessages && !sentListingFlip) {
                            Main.sendMarkedChatMessage("Listing Item!");
                            sentClaimingFlip = true;
                        }

                        if (Main.mc.currentScreen == null) {
                            Main.mcPlayer.sendChatMessage("/ah");
                        } else if (Main.mc.currentScreen instanceof GuiChest) {
                            IInventory chest = ((ContainerChest) (((GuiChest) Main.mc.currentScreen).inventorySlots)).getLowerChestInventory();

                            String chestName = chest.getDisplayName().getUnformattedText();

                            switch (chestName) {
                                case "Co-op Auction House":
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 15, 0, 0, Main.mcPlayer);
                                case "Manage Auctions":
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 24, 0, 0, Main.mcPlayer);
                                case "Create Auction":
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 48, 0, 0, Main.mcPlayer);
                                case "Create BIN Auction":
                                    if (chest.getStackInSlot(13) != null && chest.getStackInSlot(13).getItem() == Item.getItemFromBlock(Blocks.stone_button)) {
                                        for (int i = 0; i < 36; i++) {
                                            ItemStack stack = Main.mcPlayer.inventory.mainInventory[i];

                                            if (stack != null && Utils.stripColor(stack.getDisplayName()).equals(Utils.stripColor(itemBeingBought.getDisplayName()))) {
                                                int index = i - 8 + 53;

                                                if (i <= 8) {
                                                    index += 36;
                                                }
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, index, 0, 0, Main.mcPlayer);
                                            }
                                        }
                                    } else {
                                        if (chest.getStackInSlot(31) != null) {
                                            if (!Utils.stripColor(chest.getStackInSlot(31).getDisplayName()).replace(",", "").contains(itemPrice)) {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
                                            } else {
                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 29, 0, 0, Main.mcPlayer);
                                            }
                                        }
                                    }
                                case "Confirm BIN Auction":
                                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                            }
                        } else if (Main.mc.currentScreen instanceof GuiEditSign) {
                            try {
                                TileEntitySign currentSign = BazaarFlipper.currentSign;
                                if (currentSign != null) {
                                    currentSign.signText[0] = new ChatComponentText(itemPrice);

                                    if (currentSign.signText[0].equals(new ChatComponentText(itemPrice))) {
                                        Main.mc.currentScreen.onGuiClosed();
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void chatMessage(ClientChatReceivedEvent event) {
        String message = Utils.stripColor(event.message.getUnformattedText());

        if (message.contains("You purchased") || message.contains("This auction wasn't found") || message.contains("Escrow Refunded") || message.contains("There was an error with the auction house!")) {
            if (message.contains("You purchased")) {
                needToClaim = true;
                needToList = false;
            } else if (!needToClaim) {
                auctionUUIDToBuy = "";
            }

            if (!AIOMVigilanceConfig.claimRelist) {
                sentWaitingForFlips = false;
                sentBuyingFlip = false;
                auctionUUIDToBuy = "";
            }
        }
        if (message.contains("You claimed") || message.contains("ALREADY_CLAIMED")) {
            needToClaim = false;
            needToList = true;
        }
        if (message.contains("BIN Auction started for")) {
            needToList = false;
            itemBeingBought = null;

            auctionUUIDToBuy = "";

            Main.mcPlayer.closeScreen();
        }
    }

    public void grabNextFlip() {
        ticks++;
        if (ticks >= 20) {
            ticks = 0;
            if (AIOMVigilanceConfig.sendStatusMessages && !sentWaitingForFlips) {
                Main.sendMarkedChatMessage("Waiting For Flips!");
                sentWaitingForFlips = true;
            }

            new Thread(() -> {
                try {
                    String flipsString = Utils.getFromServer("http://" + AIOMVigilanceConfig.ahFlipperServer + "/items?max_price=" + AIOMVigilanceConfig.maxPriceAH + "&min_profit=" + AIOMVigilanceConfig.minProfit + "&manipulation=" + AIOMVigilanceConfig.manipulation + "&pet_skins=" + AIOMVigilanceConfig.petSkins);

                    JsonElement jsonElement = new JsonParser().parse(flipsString);
                    JsonArray jsonArray = jsonElement.getAsJsonArray();

                    for (JsonElement flipElement : jsonArray) {
                        JsonObject flip = flipElement.getAsJsonObject();
                        if (!uuids.contains(flip.get("UUID").getAsString()) && auctionUUIDToBuy.equals("")) {
                            uuids.add(flip.get("UUID").getAsString());
                            auctionUUIDToBuy = flip.get("UUID").getAsString();

                            itemPrice = flip.get("LowestBIN").getAsString();

                            if (AIOMVigilanceConfig.sendStatusMessages && !sentBuyingFlip) {
                                Main.sendMarkedChatMessage("Flip Found! Buying " + flip.get("ItemName").getAsString() + " Worth " + itemPrice);
                                sentBuyingFlip = true;
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Main.sendMarkedChatMessage("Flip API May Be Down! Are you hosting the server yourself? (#self-hosting-auction-flip-guide in the discord)");
                    MacroHandler.isMacroOn = false;
                }
            }).start();
        }
    }
}
