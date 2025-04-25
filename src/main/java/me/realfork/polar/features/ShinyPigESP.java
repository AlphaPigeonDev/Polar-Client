package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShinyPigESP {
    List<BlockPos> yourPigs = new ArrayList<>();
    List<Entity> yourPigArmorstands = new ArrayList<>();
    List<BlockPos> allPigs = new ArrayList<>();
    List<Entity> allPigArmorstands = new ArrayList<>();

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (Main.notNull && AIOMVigilanceConfig.macroType == 5 && MacroHandler.isMacroOn) {
            yourPigArmorstands.clear();
            yourPigs.clear();
            allPigs.clear();
            allPigArmorstands.clear();
            BlockRendering.renderMap.clear();
            List<Entity> entityList = Main.mcWorld.loadedEntityList;

            for (Entity entity : entityList) {
                if (entity instanceof EntityArmorStand) {
                    if (Utils.stripColor(entity.getDisplayName().getUnformattedText()).equals(Main.mcPlayer.getName()) && !yourPigArmorstands.contains(entity)) {
                        yourPigArmorstands.add(entity);
                    } else if (Utils.stripColor(entity.getDisplayName().getUnformattedText()).contains("SHINY PIG") && !allPigArmorstands.contains(entity)) {
                        allPigArmorstands.add(entity);
                    }
                }
                if (entity instanceof EntityPig) {
                    if (!yourPigArmorstands.isEmpty()) {
                        for (Entity armorStand : yourPigArmorstands) {
                            if (!yourPigs.contains(new BlockPos(armorStand.getPositionVector()))) {
                                yourPigs.add(new BlockPos(armorStand.getPositionVector()));
                                allPigs.remove(new BlockPos(armorStand.getPositionVector()));
                            }
                        }
                    }
                    if (!allPigArmorstands.isEmpty()) {
                        for (Entity armorStand : allPigArmorstands) {
                            if (allPigs != null && !allPigs.contains(new BlockPos(armorStand.getPositionVector())) && !yourPigs.contains(new BlockPos(armorStand.getPositionVector()))) {
                                allPigs.add(new BlockPos(armorStand.getPositionVector()));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (AIOMVigilanceConfig.macroType == 5 && MacroHandler.isMacroOn) {
            for (BlockPos bp : yourPigs) {
                BlockRendering.renderMap.put(bp, Color.RED);
            }
            for (BlockPos bp : allPigs) {
                if (!BlockRendering.renderMap.containsKey(bp.add(0, -1, 0))) {
                    BlockRendering.renderMap.put(bp.add(0, -1, 0), Color.BLUE);
                }
            }
        }
    }
}
