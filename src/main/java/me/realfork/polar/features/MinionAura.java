package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MinionAura {

    public static List<Entity> claimedMinions = new ArrayList<>();
    public static Entity recentClickedEntity;

    public static int checkTick = 0;
    public static int claimTick = 0;
    public static boolean inMinion = false;
    List<Entity> claimList = new ArrayList<>();

    public List<Entity> getMinions() {
        if (Main.notNull) {
            List<Entity> entityList = Main.mcWorld.loadedEntityList;
            List<Entity> minionsToClaim = new ArrayList<>();

            if (!entityList.isEmpty()) {
                for (Entity entity : entityList) {
                    if (entity instanceof EntityArmorStand) {
                        ItemStack chestStack = ((EntityArmorStand) entity).getCurrentArmor(1);

                        if (chestStack != null) {
                            if (chestStack.getDisplayName().equals("Leather Pants") && !minionsToClaim.contains(entity) && !claimedMinions.contains(entity)) {
                                minionsToClaim.add(entity);
                            }
                        }
                    }
                }

                return minionsToClaim;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void openTick(TickEvent.ClientTickEvent e) {
        if (Main.notNull && !inMinion && AIOMVigilanceConfig.macroType == 6 && MacroHandler.isMacroOn) {

            if (claimList.isEmpty()) {
                claimList = getMinions();
            }

            claimList.removeAll(claimedMinions);

            try {
                BlockRendering.renderMap.clear();
                claimedMinions.forEach(entity -> BlockRendering.renderMap.put(entity.getPosition(), Color.RED));

                claimList.forEach(entity -> BlockRendering.renderMap.putIfAbsent(entity.getPosition(), Color.GRAY));

                claimList.removeIf(entity -> Utils.distanceBetweenPoints(Main.mcPlayer.getPositionVector(), entity.getPositionVector()) > 6);

                claimList.forEach(entity -> BlockRendering.renderMap.put(entity.getPosition(), Color.GREEN));
            } catch (Exception ignored) {
            }

            if (!claimList.isEmpty()) {
                if (checkTick >= 20) {
                    checkTick = 0;

                    Entity entity = claimList.get(0);

                    if (!inMinion) {
                        Utils.useEntity(entity);
                        recentClickedEntity = entity;
                    }
                }
                checkTick++;
            }
        }
    }


    @SubscribeEvent
    public void guiScreenEvent(GuiScreenEvent event) {
        if (event.gui instanceof GuiChest && AIOMVigilanceConfig.macroType == 6 && MacroHandler.isMacroOn) {
            if (claimTick >= 100) {
                claimTick = 0;
                IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

                String chestName = chest.getDisplayName().getUnformattedText();

                if (chestName.contains("Minion")) {
                    inMinion = true;
                    if (recentClickedEntity != null) {
                        if (chest.getStackInSlot(21) == null || chest.getStackInSlot(21).getItem() == null || chest.getStackInSlot(21).getItem() == Item.getItemFromBlock(Blocks.air)) {
                            if (chest.getStackInSlot(28) != null && chest.getStackInSlot(28).getItem() == Item.getItemFromBlock(Blocks.hopper) && !Utils.stripColor(chest.getStackInSlot(28).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(9).toString()).split("Items Sold: ")[1].equals("0\"")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 28, 0, 0, Main.mcPlayer);
                            } else {
                                Main.mcPlayer.closeScreen();
                                claimedMinions.add(recentClickedEntity);
                                recentClickedEntity = null;
                                inMinion = false;
                            }
                        } else if (chest.getStackInSlot(48) != null && chest.getStackInSlot(48).getDisplayName() != null) {
                            if (Utils.stripColor(chest.getStackInSlot(48).getDisplayName()).equals("Collect All")) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 48, 0, 0, Main.mcPlayer);
                            }
                        }
                    }
                }
            }
            claimTick++;
        }
    }
}
