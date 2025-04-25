package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RuneCombine {

    public static int ticks = 0;

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (Main.notNull && AIOMVigilanceConfig.autoCombineRunes) {
            ticks++;
            if (ticks >= 20) {
                ticks = 0;
                GuiScreen screen = Main.mc.currentScreen;
                if (screen instanceof GuiChest) {
                    IInventory chest = ((ContainerChest) (((GuiChest) screen).inventorySlots)).getLowerChestInventory();

                    String chestName = chest.getDisplayName().getUnformattedText();

                    if (chestName.contains("Runic Pedestal")) {
                        if (chest.getStackInSlot(31) != null && chest.getStackInSlot(31).getDisplayName() != null && chest.getStackInSlot(31).getItem() != Item.getItemFromBlock(Blocks.barrier)) {
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 13, 0, 0, Main.mcPlayer);
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 1, Main.mcPlayer);
                        } else {
                            for (int i = 0; i < 36; i++) {
                                ItemStack item = Main.mcPlayer.inventory.mainInventory[i];

                                int index = i - 8 + 53;

                                if (i <= 8) {
                                    index += 36;
                                }

                                if (chest.getStackInSlot(19) != null && chest.getStackInSlot(19).getDisplayName() != null) {
                                    item = chest.getStackInSlot(19);
                                    index = 99;
                                }

                                if (item != null && item.getDisplayName() != null) {
                                    if (item.getDisplayName().contains("Rune") && item.stackSize >= 2) {
                                        for (int x = 0; x < 36; x++) {
                                            ItemStack item2 = Main.mcPlayer.inventory.mainInventory[x];
                                            if (item2 != null && item2.getDisplayName() != null) {
                                                if (item2.getDisplayName().equals(item.getDisplayName())) {
                                                    int index2 = x - 8 + 53;

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
