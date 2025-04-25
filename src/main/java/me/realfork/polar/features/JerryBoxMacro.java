package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class JerryBoxMacro {
    public static int ticks = 0;

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent e) {
        if (Main.notNull && AIOMVigilanceConfig.autoJerryBox) {
            ticks++;
            if (ticks >= 5) {
                ticks = 0;

                GuiScreen screen = Main.mc.currentScreen;
                if (screen instanceof GuiChest) {
                    IInventory chest = ((ContainerChest) (((GuiChest) screen).inventorySlots)).getLowerChestInventory();

                    String chestName = chest.getDisplayName().getUnformattedText();

                    if (chestName.contains("Open a Jerry Box")) {
                        Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 22, 0, 0, Main.mcPlayer);
                        Main.mcPlayer.closeScreen();
                    }
                } else if (screen == null) {
                    for (int i = 0; i < 8; i++) {
                        if (Main.mcPlayer.inventory.mainInventory[i] != null && Main.mcPlayer.inventory.mainInventory[i].getDisplayName() != null) {
                            if (Main.mcPlayer.inventory.mainInventory[i].getDisplayName().contains("Jerry Box")) {
                                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                                Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getStackInSlot(i));
                            }
                        }
                    }
                }
            }
        }
    }
}
