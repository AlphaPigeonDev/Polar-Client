package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FastBreak {

    ArrayList<BlockPos> toBreak = new ArrayList<>();

    @SubscribeEvent
    public void playerTick(@NotNull TickEvent.ClientTickEvent event) {
        if (AIOMVigilanceConfig.fastBreak && Main.notNull && Main.mc.objectMouseOver != null) {
            for (int i = 0; i <= AIOMVigilanceConfig.fastBreakBPS; i++) {
                if (toBreak.size() != 0) {
                    Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(toBreak.get(0)), EnumFacing.DOWN));

                    toBreak.remove(0);
                }
            }
        }
    }

    @SubscribeEvent
    public void pickBlocks(@NotNull RenderWorldLastEvent event) {
        if (AIOMVigilanceConfig.fastBreak && Main.notNull) {
            BlockPos playerPos = Main.mcPlayer.getPosition().add(0, 1, 0);
            Vec3i vec3i = new Vec3i(5, 1, 5);
            ArrayList<Vec3> blocks = new ArrayList<>();
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Main.mcWorld.getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.air) {
                    continue;
                }
                blocks.add(new Vec3((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5));
            }

            if (AIOMVigilanceConfig.yaw == 90) {
                // -x

                for (Vec3 block : blocks) {
                    Block b = Main.mcWorld.getBlockState(new BlockPos(block)).getBlock();
                    if (b == Blocks.nether_wart || b == Blocks.carrots || b == Blocks.potatoes) {
                        if (block.zCoord - .5 == playerPos.getZ() && block.xCoord - .5 >= playerPos.getX() - 4 && block.xCoord <= playerPos.getX() && !toBreak.contains(new BlockPos(block))) {
                            toBreak.add(new BlockPos(block));
                        }
                    }
                }

            }
            if (AIOMVigilanceConfig.yaw == 180) {
                // -z
                for (Vec3 block : blocks) {
                    Block b = Main.mcWorld.getBlockState(new BlockPos(block)).getBlock();
                    if (b == Blocks.nether_wart || b == Blocks.carrots || b == Blocks.potatoes) {
                        if (block.xCoord - .5 == playerPos.getX() && block.zCoord - .5 >= playerPos.getZ() - 4 && block.zCoord <= playerPos.getZ() && !toBreak.contains(new BlockPos(block))) {
                            toBreak.add(new BlockPos(block));
                        }
                    }

                }
            }
            if (AIOMVigilanceConfig.yaw == -90) {
                // +x

                for (Vec3 block : blocks) {
                    Block b = Main.mcWorld.getBlockState(new BlockPos(block)).getBlock();
                    if (b == Blocks.nether_wart || b == Blocks.carrots || b == Blocks.potatoes) {
                        if (block.zCoord - .5 == playerPos.getZ() && block.xCoord - .5 <= playerPos.getX() + 4 && block.xCoord >= playerPos.getX() && !toBreak.contains(new BlockPos(block))) {
                            toBreak.add(new BlockPos(block));
                        }
                    }

                }
            }
            if (AIOMVigilanceConfig.yaw == 0) {
                // +z

                for (Vec3 block : blocks) {
                    Block b = Main.mcWorld.getBlockState(new BlockPos(block)).getBlock();
                    if (b == Blocks.nether_wart || b == Blocks.carrots || b == Blocks.potatoes) {
                        if (block.xCoord - .5 == playerPos.getX() && block.zCoord - .5 <= playerPos.getZ() + 4 && block.zCoord >= playerPos.getZ() && !toBreak.contains(new BlockPos(block))) {
                            toBreak.add(new BlockPos(block));
                        }
                    }
                }
            }
        }
    }
}

