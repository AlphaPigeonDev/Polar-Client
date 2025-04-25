package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoReopenAH {

    boolean sentMessage = false;

    @SubscribeEvent
    public void drawScreenEvent(GuiScreenEvent.DrawScreenEvent event) {
        if (Main.notNull && sentMessage) {
            try {
                IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

                String chestName = chest.getDisplayName().getUnformattedText();

                if (AIOMVigilanceConfig.autoReopenAh) {
                    if (chestName.contains("Co-op Auction House")) {
                        if (chest.getStackInSlot(11) != null && chest.getStackInSlot(11).getItem() == Item.getItemFromBlock(Blocks.gold_block)) {
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
                            sentMessage = false;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void clientChatReceived(ClientChatReceivedEvent e) {
        if (AIOMVigilanceConfig.autoReopenAh) {
            String message = Utils.stripColor(e.message.getUnformattedText());

            if (message.contains("You purchased") || message.contains("Escrow refunded")) {
                Main.mcPlayer.sendChatMessage("/ah");
                sentMessage = true;
            }
        }
    }
}
