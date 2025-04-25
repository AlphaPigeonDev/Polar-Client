package me.jacktym.aiomacro.rendering;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.features.HideSummons;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Renderer {

    private static HashMap<Entity, Color> highlightedEntities = new HashMap<>();

    @SubscribeEvent
    public void renderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (AIOMVigilanceConfig.renderingEnabled && (AIOMVigilanceConfig.dungeonMobESP || AIOMVigilanceConfig.showHiddenMobs || AIOMVigilanceConfig.miningESP || AIOMVigilanceConfig.slayerBossESP || AIOMVigilanceConfig.hideSummons)) {
            Entity entity = event.entity;
            if (Utils.getWorld().contains("Catacombs")) {
                if (AIOMVigilanceConfig.dungeonMobESP) {
                    if (entity instanceof EntityBat) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                    if (entity instanceof EntityEnderman) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                    if (entity instanceof EntityBat) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                    ArrayList<String> nameList = new ArrayList<>();
                    nameList.add("Shadow Assassin");
                    nameList.add("Lost Adventurer");
                    nameList.add("Diamond Guy");
                    if (nameList.contains(entity.getName())) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                }
            }
            if (AIOMVigilanceConfig.showHiddenMobs) {
                if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityArmorStand)) {
                    entity.setInvisible(false);
                }
            }
            if (Utils.getWorld().equals("Crystal Hollows")) {
                if (AIOMVigilanceConfig.miningESP) {
                    if (entity instanceof EntitySilverfish) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                    if (entity instanceof EntityMagmaCube) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                    ArrayList<String> nameList = new ArrayList<>();
                    nameList.add("Corleone");
                    nameList.add("Team Treasurite");
                    nameList.add("Sebastian");
                    nameList.add("Wendy");
                    nameList.add("Viper");
                    nameList.add("Goblin");
                    nameList.add("Sludge");
                    nameList.add("Butterfly");
                    if (nameList.contains(entity.getName())) {
                        highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                    }
                }
            }
            if (AIOMVigilanceConfig.slayerBossESP) {
                ArrayList<String> nameList = new ArrayList<>();
                nameList.add("Revenant Sycophant");
                nameList.add("Revenant Champion");
                nameList.add("Deformed Revenant");
                nameList.add("Atoned Champion");
                nameList.add("Atoned Revenant");
                nameList.add("Tarantula Vermin");
                nameList.add("Tarantula Beast");
                nameList.add("Mutant Tarantula");
                nameList.add("Pack Enforcer");
                nameList.add("Sven Follower");
                nameList.add("Sven Alpha");
                nameList.add("Voidling Devotee");
                nameList.add("Voidling Radical");
                nameList.add("Voidcrazed Maniac");
                nameList.add("Revenant Horror");
                nameList.add("Tarantula Broodfather");
                nameList.add("Sven Packmaster");
                nameList.add("Voidgloom Seraph");
                nameList.add("Inferno Demonlord");
                if (nameList.contains(entity.getName())) {
                    highlightedEntities.put(event.entity, AIOMVigilanceConfig.ESPColor);
                }
            }
            if (AIOMVigilanceConfig.hideSummons) {
                if (HideSummons.isEntityASummon(entity) && Utils.distanceBetweenPoints(Main.mcPlayer.getPositionVector(), entity.getPositionVector()) <= 10) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderEntityModel(me.jacktym.aiomacro.rendering.RenderEntityModelEvent event) {
        if (AIOMVigilanceConfig.dungeonMobESP && !highlightedEntities.isEmpty() && highlightedEntities.containsKey(event.entity) && AIOMVigilanceConfig.renderingEnabled) {
            me.jacktym.aiomacro.rendering.EntityRendering.outlineEntity(event, highlightedEntities.get(event.entity));
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (Main.notNull && AIOMVigilanceConfig.miningESP && Utils.getWorld().contains("Dwarven Mines") && AIOMVigilanceConfig.renderingEnabled) {
            BlockPos playerPos = Main.mcPlayer.getPosition();
            ArrayList<Vec3> blocks = new ArrayList<>();
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(new Vec3i(50, 50, 50)), playerPos.subtract(new Vec3i(50, 50, 50)))) {
                IBlockState blockState = Main.mcWorld.getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.air) {
                    continue;
                }
                blocks.add(new Vec3((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5));
            }

            for (Vec3 block : blocks) {
                if (Main.mcWorld.getBlockState(new BlockPos(block)).getBlock() == Blocks.dragon_egg) {
                    BlockRendering.renderMap.put(new BlockPos(block), Color.pink);
                }
            }
        }
    }

    @SubscribeEvent
    public void renderGui(GuiScreenEvent.DrawScreenEvent event) {
        if (Main.notNull && event.gui instanceof GuiChest && AIOMVigilanceConfig.autoCloseChest && AIOMVigilanceConfig.renderingEnabled) {
            if (Utils.getTabList().contains("Catacombs")) {
                Main.mcPlayer.closeScreen();
            }
        }
    }
}
