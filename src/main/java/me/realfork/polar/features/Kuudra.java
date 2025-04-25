package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class Kuudra {

    private int tick = 0;
    private boolean isReady = false;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (Main.notNull && Utils.getWorld().contains("Instanced")) {
            if (tick >= 100) {
                tick = 0;

                if (!isReady && AIOMVigilanceConfig.autoReady) {
                    Utils.openNpc("Elle");
                    Utils.openNpc("Animator");
                }
            }
            tick++;

            if (AIOMVigilanceConfig.autoAim) {
                Entity entityToLookAt = null;

                List<Entity> entityList = Main.mcWorld.loadedEntityList;

                for (Entity entity : entityList) {
                    if (entity instanceof EntityWither && (entityToLookAt == null || Utils.distanceBetweenPoints(Main.mcPlayer.getPositionVector(), entity.getPositionVector()) < Utils.distanceBetweenPoints(Main.mcPlayer.getPositionVector(), entityToLookAt.getPositionVector()))) {
                        entityToLookAt = entity;
                    }
                }
                if (entityToLookAt != null) {
                    Utils.autoAim(entityToLookAt.getPositionVector());
                }
            }
        }
    }

    @SubscribeEvent
    public void insideKuudraGui(GuiScreenEvent event) {
        if (event.gui instanceof GuiChest && AIOMVigilanceConfig.autoReady) {
            IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();


            if (chest.getName().equals("Ready Up")) {
                for (Slot slot : ((GuiChest) event.gui).inventorySlots.inventorySlots) {
                    ItemStack item = chest.getStackInSlot(slot.slotNumber);
                    if (item != null && item.getDisplayName().contains(Main.mcPlayer.getName())) {
                        if ((chest.getStackInSlot(slot.slotNumber - 9).getDisplayName().contains("Not Ready"))) {
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, slot.slotNumber, 0, 0, Main.mcPlayer);
                            isReady = true;
                        } else {
                            Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 40, 0, 0, Main.mcPlayer);
                        }
                    }
                }
            }
        }
    }
}
