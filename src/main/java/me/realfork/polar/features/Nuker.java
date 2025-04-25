package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
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

import java.awt.*;
import java.util.List;
import java.util.*;

public class Nuker {
    ArrayList<BlockPos> toBreak = new ArrayList<>();
    BlockPos bp = null;

    public static HashMap<BlockPos, Double>
    sortByValue(HashMap<BlockPos, Double> hm) {
        List<Map.Entry<BlockPos, Double>> list
                = new LinkedList<>(
                hm.entrySet());

        list.sort(Map.Entry.comparingByValue());

        HashMap<BlockPos, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<BlockPos, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    @SubscribeEvent
    public void playerTick(@NotNull TickEvent.ClientTickEvent event) {
        if (((MacroHandler.isMacroOn && AIOMVigilanceConfig.macroType == 2) || AutoBazaarUnlocker.autoWheatOn) && Main.notNull) {
            BlockRendering.renderMap.clear();

            ArrayList<BlockPos> localToBreak = toBreak;

            for (int i = 0; i <= AIOMVigilanceConfig.nukerBPS; i++) {
                if (AIOMVigilanceConfig.stayOnYLevel) {
                    localToBreak.removeIf(blockPos -> blockPos.getY() < Main.mcPlayer.posY);
                }
                localToBreak.removeIf(blockPos -> Utils.distanceBetweenPoints(new Vec3(blockPos), Main.mcPlayer.getPositionVector()) >= 4);
                localToBreak.removeIf(blockPos -> Main.mcWorld.getBlockState(blockPos).getBlock() == Blocks.air);

                HashMap<BlockPos, Double> toBreakMap = new HashMap<>();
                HashMap<BlockPos, Double> finalToBreakMap = toBreakMap;
                localToBreak.forEach(block -> finalToBreakMap.put(block, Utils.distanceBetweenPoints(new Vec3(block), Main.mcPlayer.getPositionVector())));
                toBreakMap = sortByValue(toBreakMap);

                localToBreak.clear();
                localToBreak.addAll(toBreakMap.keySet());

                toBreak = localToBreak;

                if (localToBreak.size() != 0) {
                    bp = new BlockPos(localToBreak.get(0));
                    if (AIOMVigilanceConfig.nukerBlock == 5) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), new BlockPos(toBreak.get(0)).add(0, 0, 0), EnumFacing.UP, Main.mc.objectMouseOver.hitVec);
                    } else {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(toBreak.get(0)), EnumFacing.DOWN));
                    }
                    localToBreak.remove(0);
                }
            }
        }

        if (!MacroHandler.isMacroOn && (!toBreak.isEmpty() || bp != null)) {
            toBreak.clear();
            bp = null;
        }
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        if (((MacroHandler.isMacroOn && AIOMVigilanceConfig.macroType == 2) || AutoBazaarUnlocker.autoWheatOn) && Main.notNull && bp != null && toBreak.size() != 0) {
            Color color = AIOMVigilanceConfig.nukerColor;
            BlockRendering.renderMap.put(bp, color);
        }
    }

    @SubscribeEvent
    public void pickBlocks(@NotNull RenderWorldLastEvent event) {
        if (((MacroHandler.isMacroOn && AIOMVigilanceConfig.macroType == 2) || AutoBazaarUnlocker.autoWheatOn) && Main.notNull) {
            ArrayList<Vec3> blocks = new ArrayList<>();

            switch (AIOMVigilanceConfig.nukerBlock) {
                case 0:
                case 1:
                case 3:
                case 5:
                    blocks = pickBlocks(new Vec3i(3, 1, 3));
                    break;
                case 2:
                case 6:
                    blocks = pickBlocks(new Vec3i(5, 5, 5));
                    break;
                case 4:
                    blocks = pickBlocks(new Vec3i(3, 2, 3));
                    break;
            }

            if (AutoBazaarUnlocker.autoWheatOn) {
                blocks = pickBlocks(new Vec3i(3, 1, 3));
            }

            for (Vec3 block : blocks) {
                Block b = Main.mcWorld.getBlockState(new BlockPos(block)).getBlock();
                if (AIOMVigilanceConfig.nukerBlock == 0) {
                    if (b == Blocks.mycelium && !toBreak.contains(new BlockPos(block))) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AIOMVigilanceConfig.nukerBlock == 1) {
                    if (b == Blocks.sand && !toBreak.contains(new BlockPos(block)) && Main.mcWorld.getBlockState(new BlockPos(block)).getBlock().getMetaFromState(Main.mcWorld.getBlockState(new BlockPos(block))) == 1) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AIOMVigilanceConfig.nukerBlock == 2) {
                    if ((b == Blocks.log || b == Blocks.log2) && !toBreak.contains(new BlockPos(block))) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AIOMVigilanceConfig.nukerBlock == 3) {
                    if ((b == Blocks.nether_wart || b == Blocks.potatoes || b == Blocks.carrots || b == Blocks.wheat || b == Blocks.cocoa || b == Blocks.pumpkin || b == Blocks.melon_block) && Main.mcWorld.getBlockState(new BlockPos(block)).getBlock().getMetaFromState(Main.mcWorld.getBlockState(new BlockPos(block))) >= 1 && !toBreak.contains(new BlockPos(block))) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AIOMVigilanceConfig.nukerBlock == 4) {
                    if ((b == Blocks.reeds || b == Blocks.cactus) && !toBreak.contains(new BlockPos(block)) && block.yCoord >= Main.mcPlayer.posY + 1) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AutoBazaarUnlocker.autoWheatOn) {
                    if (b == Blocks.wheat && !toBreak.contains(new BlockPos(block))) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AIOMVigilanceConfig.nukerBlock == 5) {
                    if ((b == Blocks.grass || b == Blocks.dirt) && !toBreak.contains(new BlockPos(block)) && Main.mcWorld.getBlockState(new BlockPos(block).add(0, 1, 0)).getBlock() == Blocks.air) {
                        toBreak.add(new BlockPos(block));
                    }
                }
                if (AIOMVigilanceConfig.nukerBlock == 6) {
                    try {
                        if (Block.getBlockFromName(AIOMVigilanceConfig.customNukerBlock.toLowerCase()) != null) {
                            if (b == Block.getBlockFromName(AIOMVigilanceConfig.customNukerBlock.toLowerCase()) && !toBreak.contains(new BlockPos(block))) {
                                toBreak.add(new BlockPos(block));
                            }
                        } else {
                            System.out.println("[AIOM] Error With Custom Nuker! " + AIOMVigilanceConfig.customNukerBlock + " Not Found In Block List!");
                        }
                    } catch (NullPointerException e) {
                        System.out.println("[AIOM] Error With Custom Nuker! " + AIOMVigilanceConfig.customNukerBlock + " Not Found In Block List!");
                    }
                }
            }
        }
    }

    public static ArrayList<Vec3> pickBlocks(Vec3i vec3i) {
        BlockPos playerPos = Main.mcPlayer.getPosition();
        ArrayList<Vec3> blocks = new ArrayList<>();
        for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
            IBlockState blockState = Main.mcWorld.getBlockState(blockPos);
            if (blockState.getBlock() == Blocks.air) {
                continue;
            }
            blocks.add(new Vec3((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5));
        }
        return blocks;
    }
}
