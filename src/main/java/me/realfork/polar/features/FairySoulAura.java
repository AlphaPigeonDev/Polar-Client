package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class FairySoulAura {

    public static List<Entity> clickedSouls = new ArrayList<>();
    public static List<Entity> loadedSouls = new ArrayList<>();
    public static Entity recentClickAttempt;

    public static long lastClick = Utils.currentTimeMillis();

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (Main.mcWorld != null && Main.mcPlayer != null && AIOMVigilanceConfig.macroType == 4 && MacroHandler.isMacroOn) {
            List<Entity> entityList = Main.mcWorld.loadedEntityList;

            for (Entity entity : entityList) {
                if (entity instanceof EntityArmorStand && !clickedSouls.contains(entity)) {
                    ItemStack headStack = ((EntityArmorStand) entity).getCurrentArmor(3);

                    if (headStack != null) {
                        NBTTagCompound nbtTagCompound = headStack.getSubCompound("SkullOwner", false);
                        if (nbtTagCompound != null && nbtTagCompound.toString().contains("Id:") && nbtTagCompound.getString("Id").contains("57a4c8dc-9b8e-3d41-80da-a608901a6147") && !loadedSouls.contains(entity)) {
                            loadedSouls.add(entity);
                        }
                    }
                }
            }
            for (Entity entity : loadedSouls) {
                AxisAlignedBB aabb = new AxisAlignedBB(new BlockPos(entity.getPositionVector().addVector(-4, -4, -4)), new BlockPos(entity.getPositionVector().addVector(4, 4, 4)));
                if (Main.mcWorld.getEntitiesWithinAABB(EntityPlayer.class, aabb).contains(Main.mcPlayer) && Utils.currentTimeMillis() - lastClick >= 1000 && !clickedSouls.contains(entity)) {
                    Utils.useEntity(entity);
                    recentClickAttempt = entity;
                    lastClick = Utils.currentTimeMillis();
                }
            }
        }
    }

    @SubscribeEvent
    public void chatEvent(ClientChatReceivedEvent event) {
        if (Utils.stripColor(event.message.getUnformattedText()).contains("You have already found that Fairy Soul!") || Utils.stripColor(event.message.getUnformattedText()).contains("You found a Fairy Soul!")) {
            clickedSouls.add(recentClickAttempt);
        }
    }
}
