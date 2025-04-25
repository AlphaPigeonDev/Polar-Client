package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BookCombine {
    public int ticks = 0;

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (Main.notNull && AIOMVigilanceConfig.autoCombineBooks) {
            ticks++;
            if (ticks >= 20) {
                ticks = 0;
                GuiScreen screen = Main.mc.currentScreen;
                if (screen instanceof GuiChest) {
                    IInventory chest = ((ContainerChest) (((GuiChest) screen).inventorySlots)).getLowerChestInventory();

                    String chestName = chest.getDisplayName().getUnformattedText();

                    if (chestName.contains("Anvil")) {

                        if (chest.getStackInSlot(13) != null && chest.getStackInSlot(13).getDisplayName() != null && chest.getStackInSlot(13).getItem() != Item.getItemFromBlock(Blocks.barrier)) {
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 22, 0, 0, Main.mcPlayer);
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 13, 0, 1, Main.mcPlayer);
                        } else {
                            for (int i = 0; i < 36; i++) {
                                ItemStack item = Main.mcPlayer.inventory.mainInventory[i];

                                int index = i-8 + 53;

                                if (i <= 8) {
                                    index += 36;
                                }

                                if (chest.getStackInSlot(29) != null && chest.getStackInSlot(29).getDisplayName() != null && chest.getStackInSlot(29).getItem() == Items.enchanted_book) {
                                    item = chest.getStackInSlot(29);
                                    index = 99;
                                }

                                if (item != null && item.getDisplayName() != null) {
                                    if (item.getDisplayName().contains("Enchanted Book")) {
                                        for (int x = 0; x < 36; x++) {
                                            if (i != x) {
                                                ItemStack item2 = Main.mcPlayer.inventory.mainInventory[x];
                                                if (item2 != null && item2.getDisplayName() != null) {
                                                    if (item2.getDisplayName().contains("Enchanted Book")) {
                                                        if (item.getTagCompound().getCompoundTag("display").getTagList("Lore", 8).equals(item2.getTagCompound().getCompoundTag("display").getTagList("Lore", 8))) {
                                                            int index2 = x-8 + 53;

                                                            if (x <= 8) {
                                                                index2 += 36;
                                                            }

                                                            if (index != 99) {
                                                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, index, 0, 1, Main.mcPlayer);
                                                            }
                                                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, index2, 0, 1, Main.mcPlayer);
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
