package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class DungeonDoorAura {

    public static Vec3 centerWitherDoor;

    public static List<Vec3> loadedBlocks = new ArrayList<>();

    @SubscribeEvent
    public void findDungeonDoors(RenderWorldLastEvent e) {
        if (Main.mcPlayer != null && Main.mcWorld != null && AIOMVigilanceConfig.dungeonDoorAura) {
            if (loadedBlocks.isEmpty()) {
                loadedBlocks = Nuker.pickBlocks(new Vec3i(6, 6, 6));
            } else {
                if (centerWitherDoor != null && (Main.mcWorld.getBlockState(new BlockPos(centerWitherDoor)).getBlock() != Blocks.coal_block || Utils.distanceBetweenPoints(Main.mcPlayer.getPositionVector(), centerWitherDoor) >= 10)) {
                    centerWitherDoor = null;
                }
                loadedBlocks.removeIf(loadedBlock -> Main.mcWorld.getBlockState(new BlockPos(loadedBlock)).getBlock() != Blocks.coal_block);
                if (!loadedBlocks.isEmpty()) {
                    Vec3 loadedBlock = loadedBlocks.get(0);
                    loadedBlocks.remove(loadedBlock);
                    if (Main.mcWorld.getBlockState(new BlockPos(loadedBlock)).getBlock() == Blocks.coal_block) {
                        if (Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(-1, 0, 0))).getBlock() == Blocks.coal_block
                                && Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(1, 0, 0))).getBlock() == Blocks.coal_block
                                && Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(0, -1, 0))).getBlock() == Blocks.coal_block
                                && Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(0, 1, 0))).getBlock() == Blocks.coal_block
                                && Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(0, 2, 0))).getBlock() == Blocks.coal_block
                                && Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(0, 0, -1))).getBlock() == Blocks.coal_block
                                && Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(0, 0, 1))).getBlock() == Blocks.coal_block && !Utils.vec3Equals(centerWitherDoor, loadedBlock)) {
                            centerWitherDoor = loadedBlock;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void ClientTIckEvent(TickEvent.ClientTickEvent e) {
        if (Main.mcPlayer != null && Main.mcWorld != null && AIOMVigilanceConfig.dungeonDoorAura) {
            BlockRendering.renderMap.clear();
            if (centerWitherDoor != null) {
                BlockRendering.renderMap.put(new BlockPos(centerWitherDoor), AIOMVigilanceConfig.dungeonESPColor);

                Main.mc.playerController.clickBlock(new BlockPos(centerWitherDoor), EnumFacing.DOWN);
            }
        }
    }
}
