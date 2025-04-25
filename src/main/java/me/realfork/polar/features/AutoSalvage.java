package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoSalvage {

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent e) {
        if (Main.notNull && AIOMVigilanceConfig.autoSalvage) {
            GuiScreen screen = Main.mc.currentScreen;

            if (screen instanceof GuiChest) {
                IInventory chest = ((ContainerChest) (((GuiChest) screen).inventorySlots)).getLowerChestInventory();

                String chestName = chest.getDisplayName().getUnformattedText();

                if (chestName.equals("Salvage Items")) {

                }
            }
        }
    }
}
