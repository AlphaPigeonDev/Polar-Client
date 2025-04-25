package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CropAura {
    public static boolean toggled = false;

    ArrayList<BlockPos> toPlace = new ArrayList<>();

    BlockPos bp = null;

    @SubscribeEvent
    public void playerTick(@NotNull TickEvent.ClientTickEvent event) {
        if (toggled && Main.mcPlayer != null && Main.mcWorld != null) {
            for (int i = 0; i <= AIOMVigilanceConfig.cropAuraBPS; i++) {
                if (toPlace.size() != 0) {
                    BlockPos blockPos = toPlace.get(0);
                    BlockPos playerPos = Main.mcPlayer.getPosition();
                    Vec3i vec3i = new Vec3i(3, 1, 3);
                    ArrayList<BlockPos> blocks = new ArrayList<>();
                    for (BlockPos bp : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blocks.add(bp);
                    }

                    if (!blocks.contains(new BlockPos(blockPos.getX() - 0.5, blockPos.getY(), blockPos.getZ() - 0.5)) || !blocks.contains(new BlockPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5))) {
                        toPlace.remove(blockPos);
                    }
                    if (toPlace.size() != 0) {
                        bp = new BlockPos(toPlace.get(0));
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), bp, EnumFacing.UP, Main.mc.objectMouseOver.hitVec);
                        toPlace.remove(0);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void pickBlocks(@NotNull RenderWorldLastEvent event) {
        if (toggled && Main.mcPlayer != null && Main.mcWorld != null) {
            BlockPos playerPos = Main.mcPlayer.getPosition().add(0, -1, 0);
            Vec3i vec3i = new Vec3i(3, 0, 3);
            ArrayList<Vec3> blocks = new ArrayList<>();
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Main.mcWorld.getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.air) {
                    continue;
                }
                blocks.add(new Vec3((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5));
            }

            for (Vec3 block : blocks) {
                Block b = Main.mcWorld.getBlockState(new BlockPos(block)).getBlock();
                if (b == Blocks.dirt && !toPlace.contains(new BlockPos(block))) {
                    toPlace.add(new BlockPos(block));
                }
            }
        }
    }
}
