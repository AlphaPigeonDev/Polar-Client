package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class CryptESP {

    public static List<Vec3> renderList = new ArrayList<>();

    public static List<Vec3> loadedBlocks = new ArrayList<>();

    public static boolean inThread = false;

    @SubscribeEvent
    public void setRenderList(RenderWorldLastEvent e) {
        if (Main.mcPlayer != null && Main.mcWorld != null && AIOMVigilanceConfig.cryptESPOn && !inThread) {
            new Thread(() -> {
                inThread = true;
                if (loadedBlocks.isEmpty()) {
                    loadedBlocks = Nuker.pickBlocks(new Vec3i(100, 100, 100));
                } else {
                    renderList.removeIf(loadedBlock -> Main.mcWorld.getBlockState(new BlockPos(loadedBlock)).getBlock() != Blocks.stone_slab);
                    loadedBlocks.removeIf(loadedBlock -> Main.mcWorld.getBlockState(new BlockPos(loadedBlock)).getBlock() != Blocks.stone_slab);
                    if (!loadedBlocks.isEmpty()) {
                        Vec3 loadedBlock = loadedBlocks.get(0);
                        loadedBlocks.remove(loadedBlock);
                        if (Main.mcWorld.getBlockState(new BlockPos(loadedBlock)).getBlock() == Blocks.stone_slab) {
                            if (Main.mcWorld.getBlockState(new BlockPos(loadedBlock.addVector(0, -1, 0))).getBlock() == Blocks.stone_brick_stairs && Utils.vec3NotContains(renderList, loadedBlock)) {
                                renderList.add(loadedBlock);
                            }
                        }
                    }
                }
                inThread = false;
            }).start();
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.ClientTickEvent e) {
        if (Main.mcPlayer != null && Main.mcWorld != null && AIOMVigilanceConfig.cryptESPOn) {
            BlockRendering.renderMap.clear();
            if (!renderList.isEmpty()) {
                renderList.forEach(vec3 -> BlockRendering.renderMap.putIfAbsent(new BlockPos(vec3), AIOMVigilanceConfig.dungeonESPColor));
            }
        }
    }
}
