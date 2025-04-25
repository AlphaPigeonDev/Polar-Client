package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class PathFind {
    public static boolean clear = false;
    public static Vec3 destinationGlobal;
    public Vec3 pos;
    public Vec3 recentPoint = null;
    public List<Vec3> badPoints = new ArrayList<>();
    public int attempts = 0;
    public LinkedList<Vec3> pathPoints = new LinkedList<>();
    public static boolean globalCollision = true;
    public static List<Integer> optimizeBadIndex = new ArrayList<>();
    public static boolean optimizedFully = false;
    public static long findPathStart;
    public LinkedList<Vec3> finalPath = new LinkedList<>();
    public static boolean followPath = false;
    public ArrayList<Block> passThroughBlockList = new ArrayList<>();
    public boolean sendDoneMessage = false;
    public boolean sendFailMessage = false;
    public boolean inThread = false;
    public static boolean forwardPressed = false;

    public static int xDir = 0;
    public static int zDir = 0;

    public static void pathFind(Vec3 destination, Boolean collision) {
        destinationGlobal = destination;

        globalCollision = collision;

        findPathStart = Utils.currentTimeMillis();

        followPath = false;

        System.out.println("Pathfinding: " + destination);
    }

    public static void clear() {
        destinationGlobal = null;
        BlockRendering.renderMap.clear();
        optimizedFully = false;
        optimizeBadIndex.clear();
        followPath = false;
        clear = true;
        SetPlayerLook.toggled = false;
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        xDir = 0;
        zDir = 0;
    }

    @SubscribeEvent
    public void findPath(TickEvent.ClientTickEvent event) {
        if (Main.notNull) {
            if (pos == null) {
                BlockPos blockPos = new BlockPos(Main.mcPlayer.posX, Main.mcPlayer.posY, Main.mcPlayer.posZ);
                pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
            if (pathPoints.isEmpty()) {
                pathPoints.add(pos);
            }
            if (clear) {
                pathPoints.clear();
                finalPath.clear();
                badPoints.clear();
                sendDoneMessage = false;
                sendFailMessage = false;
                pos = null;
                clear = false;
                return;
            }
            if (passThroughBlockList.isEmpty()) {
                passThroughBlockList.add(Blocks.acacia_fence);
                passThroughBlockList.add(Blocks.activator_rail);
                passThroughBlockList.add(Blocks.air);
                passThroughBlockList.add(Blocks.birch_fence_gate);
                passThroughBlockList.add(Blocks.brown_mushroom);
                passThroughBlockList.add(Blocks.carpet);
                passThroughBlockList.add(Blocks.carrots);
                passThroughBlockList.add(Blocks.dark_oak_fence_gate);
                passThroughBlockList.add(Blocks.deadbush);
                passThroughBlockList.add(Blocks.detector_rail);
                passThroughBlockList.add(Blocks.fire);
                passThroughBlockList.add(Blocks.golden_rail);
                passThroughBlockList.add(Blocks.heavy_weighted_pressure_plate);
                passThroughBlockList.add(Blocks.iron_trapdoor);
                passThroughBlockList.add(Blocks.jungle_fence_gate);
                passThroughBlockList.add(Blocks.lever);
                passThroughBlockList.add(Blocks.melon_stem);
                passThroughBlockList.add(Blocks.nether_wart);
                passThroughBlockList.add(Blocks.potatoes);
                passThroughBlockList.add(Blocks.powered_comparator);
                passThroughBlockList.add(Blocks.pumpkin_stem);
                passThroughBlockList.add(Blocks.rail);
                passThroughBlockList.add(Blocks.red_flower);
                passThroughBlockList.add(Blocks.red_mushroom);
                passThroughBlockList.add(Blocks.redstone_torch);
                passThroughBlockList.add(Blocks.redstone_wire);
                passThroughBlockList.add(Blocks.reeds);
                passThroughBlockList.add(Blocks.sapling);
                passThroughBlockList.add(Blocks.snow_layer);
                passThroughBlockList.add(Blocks.spruce_fence_gate);
                passThroughBlockList.add(Blocks.standing_sign);
                passThroughBlockList.add(Blocks.stone_pressure_plate);
                passThroughBlockList.add(Blocks.tallgrass);
                passThroughBlockList.add(Blocks.torch);
                passThroughBlockList.add(Blocks.trapdoor);
                passThroughBlockList.add(Blocks.tripwire);
                passThroughBlockList.add(Blocks.tripwire_hook);
                passThroughBlockList.add(Blocks.unlit_redstone_torch);
                passThroughBlockList.add(Blocks.unpowered_comparator);
                passThroughBlockList.add(Blocks.vine);
                passThroughBlockList.add(Blocks.wall_sign);
                passThroughBlockList.add(Blocks.water);
                passThroughBlockList.add(Blocks.waterlily);
                passThroughBlockList.add(Blocks.web);
                passThroughBlockList.add(Blocks.wheat);
                passThroughBlockList.add(Blocks.wooden_button);
                passThroughBlockList.add(Blocks.wooden_pressure_plate);
                passThroughBlockList.add(Blocks.yellow_flower);
            }
            if (destinationGlobal != null && !followPath) {
                if (sendDoneMessage) {
                    Main.sendMarkedChatMessage("Path found in " + (Utils.currentTimeMillis() - findPathStart) + " Milliseconds. " + finalPath.size() + " Blocks!");
                    followPath = true;

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);

                    double setX;
                    double setY;
                    double setZ;
                    if (Main.mcPlayer.getPositionVector().xCoord > 0) {
                        setX = Math.floor(Main.mcPlayer.getPositionVector().xCoord) + 0.45;
                    } else {
                        setX = Math.ceil(Main.mcPlayer.getPositionVector().xCoord) - 0.45;
                    }
                    setY = Main.mcPlayer.getPositionVector().yCoord;
                    if (Main.mcPlayer.getPositionVector().zCoord > 0) {
                        setZ = Math.floor(Main.mcPlayer.getPositionVector().zCoord) + 0.45;
                    } else {
                        setZ = Math.ceil(Main.mcPlayer.getPositionVector().zCoord) - 0.45;
                    }

                    Main.mcPlayer.setPosition(setX, setY, setZ);
                    return;
                }
                if (sendFailMessage) {
                    Main.sendMarkedChatMessage("Pathfinding Stopped! No Path Found.");
                    clear();
                    return;
                }
                if (pathPoints.isEmpty()) {
                    pathPoints.add(pos);
                }
                if (finalPath.isEmpty()) {
                    finalPath.add(pos);
                }
                if (!inThread) {
                    new Thread(() -> {
                        inThread = true;
                        for (int x = 1; x <= 100; x++) {
                            if (!clear) {
                                try {
                                    if (Utils.vec3Equals(getNextVec(), destinationGlobal)) {
                                        if (Utils.vec3NotContains(finalPath, destinationGlobal)) {
                                            finalPath.add(destinationGlobal);
                                            finalPath.add(destinationGlobal);
                                        }
                                        if (optimizedFully) {
                                            sendDoneMessage = true;
                                            inThread = false;
                                            return;
                                        } else {
                                            optimizePath(finalPath);
                                        }
                                    } else {
                                        for (int i = 1; i <= 6; i++) {
                                            if (pathPoints.size() == 0) {
                                                pathPoints.add(pos);
                                            }
                                            Vec3 lastPoint = getNextVec();
                                            Vec3 point = getNextVec();

                                            switch (i) {
                                                case 1:
                                                    point = point.addVector(0, 0, 1);
                                                    break;
                                                case 2:
                                                    point = point.addVector(1, 0, 0);
                                                    break;
                                                case 3:
                                                    point = point.addVector(0, 0, -1);
                                                    break;
                                                case 4:
                                                    point = point.addVector(0, -1, 0);
                                                    break;
                                                case 5:
                                                    point = point.addVector(-1, 0, 0);
                                                    break;
                                                case 6:
                                                    point = point.addVector(0, 1, 0);
                                                    break;
                                            }
                                            if ((blockIsPassable(Main.mcWorld.getBlockState(new BlockPos(lastPoint.addVector(0, -1, 0))).getBlock()))) {
                                                if ((blockIsPassable(Main.mcWorld.getBlockState(new BlockPos(point)).getBlock()))
                                                        && (blockIsPassable(Main.mcWorld.getBlockState(new BlockPos(point.addVector(0, 1, 0))).getBlock()))
                                                        && (!blockIsPassable(Main.mcWorld.getBlockState(new BlockPos(point.addVector(0, -1, 0))).getBlock()) || i == 4)
                                                        && Utils.vec3NotContains(badPoints, point)
                                                        && Utils.vec3NotContains(pathPoints, point)
                                                        && Utils.vec3NotContains(finalPath, point)) {
                                                    pathPoints.add(point);
                                                }
                                            } else if ((blockIsPassable(Main.mcWorld.getBlockState(new BlockPos(point)).getBlock()) || !globalCollision)
                                                    && (blockIsPassable(Main.mcWorld.getBlockState(new BlockPos(point.addVector(0, 1, 0))).getBlock()) || !globalCollision)
                                                    && Utils.vec3NotContains(badPoints, point)
                                                    && Utils.vec3NotContains(pathPoints, point)
                                                    && Utils.vec3NotContains(finalPath, point)) {
                                                pathPoints.add(point);
                                            }
                                        }
                                        if (pathPoints.isEmpty()) {
                                            pathPoints.add(pos);
                                        }
                                        if (pathPoints.size() >= 2
                                                && getNextVec() != null) {
                                            finalPath.add(getNextVec());
                                            pathPoints.remove(0);

                                            pathPoints = sortPathPoints(pathPoints);

                                            if (!pathPoints.isEmpty()) {
                                                if (Utils.vec3Equals(getNextVec(), recentPoint)) {
                                                    attempts++;
                                                } else {
                                                    attempts = 0;
                                                }
                                                if (attempts >= 3) {
                                                    if (Utils.vec3NotContains(badPoints, getNextVec())) {
                                                        badPoints.add(getNextVec());
                                                        attempts = 0;
                                                    }
                                                }
                                                recentPoint = getNextVec();

                                                BlockRendering.renderMap.clear();
                                                BlockRendering.renderMap.put(new BlockPos(recentPoint), Color.RED);
                                            } else {
                                                sendFailMessage = true;
                                            }

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                break;
                            }
                        }
                        inThread = false;
                    }).start();
                }
            } else if (followPath) {
                if (finalPath.size() >= 2) {
                    Vec3 currentPos = finalPath.get(0);
                    Vec3 nextPos;
                    Vec3 thirdPos = null;
                    nextPos = finalPath.get(1);
                    if (Utils.vec3Equals(currentPos, nextPos)) {
                        finalPath.remove(0);
                        return;
                    }
                    if (finalPath.size() >= 3) {
                        thirdPos = finalPath.get(2);
                        if (Utils.vec3Equals(nextPos, thirdPos)) {
                            finalPath.remove(1);
                            return;
                        }
                    }

                    BlockRendering.renderMap.put(new BlockPos(currentPos), Color.YELLOW);

                    SetPlayerLook.pitch = 0;
                    SetPlayerLook.yaw = getNextLookVec(currentPos, nextPos, thirdPos);
                    SetPlayerLook.toggled = true;

                    //KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);

                    if (currentPos.yCoord < nextPos.yCoord) {
                        finalPath.remove(0);
                    }
                    if (currentPos.yCoord > nextPos.yCoord) {
                        finalPath.remove(1);
                    }

                    //System.out.println(nextPos.addVector(0.5, 0, 0.5));

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);

                    if (SetPlayerLook.isLookCorrect()) {

                        if (xDir == 0 && (SetPlayerLook.yaw == 90 || SetPlayerLook.yaw == -90)) {
                            if (Main.mcPlayer.getPositionVector().xCoord < nextPos.addVector(0.5, 0, 0.5).xCoord) {
                                xDir = 1;
                            } else {
                                xDir = 2;
                            }
                        }

                        if (zDir == 0 && (SetPlayerLook.yaw == 180 || SetPlayerLook.yaw == 0)) {
                            if (Main.mcPlayer.getPositionVector().zCoord < nextPos.addVector(0.5, 0, 0.5).zCoord) {
                                zDir = 1;
                            } else {
                                zDir = 2;
                            }
                        }

                        System.out.println(xDir + " | " + zDir);

                        if (xDir == 1) {
                            if (Main.mcPlayer.getPositionVector().xCoord >= nextPos.addVector(0.5, 0, 0.5).xCoord) {
                                goToNextBlock();
                                xDir = 0;
                                return;
                            }
                        } else if (xDir == 2) {
                            if (Main.mcPlayer.getPositionVector().xCoord <= nextPos.addVector(0.5, 0, 0.5).xCoord) {
                                goToNextBlock();
                                xDir = 0;
                                return;
                            }
                        }
                        if (zDir == 1) {
                            if (Main.mcPlayer.getPositionVector().zCoord >= nextPos.addVector(0.5, 0, 0.5).zCoord) {
                                goToNextBlock();
                                zDir = 0;
                                return;
                            }
                        } else if (zDir == 2) {
                            if (Main.mcPlayer.getPositionVector().zCoord <= nextPos.addVector(0.5, 0, 0.5).zCoord) {
                                goToNextBlock();
                                zDir = 0;
                                return;
                            }
                        }
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), (currentPos.yCoord > nextPos.yCoord && Main.mcPlayer.getPositionVector().yCoord > nextPos.yCoord) || Main.mcPlayer.getPositionVector().yCoord == nextPos.yCoord);

                        if (currentPos.yCoord <= nextPos.yCoord && Main.mcPlayer.getPositionVector().yCoord < nextPos.yCoord && String.valueOf(Main.mcPlayer.getPositionVector().yCoord).contains(".0")) {
                            if (Main.mcPlayer.posY % 1 == 0) {
                                Main.mcPlayer.jump();
                            }
                        }
                        /*if (((Main.mcPlayer.getPositionVector().xCoord >= 0 && Main.mcPlayer.getPositionVector().xCoord >= nextPos.addVector(0.5, 0, 0.5).xCoord)
                                || (Main.mcPlayer.getPositionVector().xCoord < 0 && Main.mcPlayer.getPositionVector().xCoord >= nextPos.addVector(0.5, 0, 0.5).xCoord))
                                && ((Main.mcPlayer.getPositionVector().zCoord >= 0 && Main.mcPlayer.getPositionVector().zCoord <= nextPos.addVector(0.5, 0, 0.5).zCoord)
                                || (Main.mcPlayer.getPositionVector().zCoord < 0 && Main.mcPlayer.getPositionVector().zCoord <= nextPos.addVector(0.5, 0, 0.5).zCoord))
                                && Math.floor(Main.mcPlayer.getPositionVector().yCoord) == nextPos.yCoord) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);

                            finalPath.remove(0);
                        } else {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                            if (currentPos.yCoord <= nextPos.yCoord && Main.mcPlayer.getPositionVector().yCoord < nextPos.yCoord && String.valueOf(Main.mcPlayer.getPositionVector().yCoord).contains(".0")) {
                                Main.mcPlayer.jump();
                            }*/
                    }
                } else {
                    Main.sendMarkedChatMessage("Followed Path!");
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                    SetPlayerLook.toggled = false;
                    followPath = false;
                    clear();
                }
            }
        }
    }

    private void goToNextBlock() {
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);

        finalPath.remove(0);
    }

    private int getNextLookVec(Vec3 vec1, Vec3 vec2, Vec3 vec3) {
        int returnInt = 0;

        if (vec1.xCoord > vec2.xCoord) {
            returnInt = 90;
        } else if (vec1.xCoord < vec2.xCoord) {
            returnInt = -90;
        } else if (vec1.zCoord > vec2.zCoord) {
            returnInt = 180;
        } else if (vec1.zCoord < vec2.zCoord) {
            //returnInt = 0;
        } else if (vec1.yCoord < vec2.yCoord) {
            System.out.println("Positive Vertical! " + vec1 + " " + vec3);
            if (vec3 != null) {
                returnInt = getNextLookVec(vec1, vec3, null);
            } else {
                System.out.println("Vertical Error! Vec3 Null!");
            }
        } else if (vec1.yCoord > vec2.yCoord) {
            System.out.println("Negative Vertical! " + vec1 + " " + vec3);
            if (vec3 != null) {
                returnInt = getNextLookVec(vec1, vec3, null);
            } else {
                System.out.println("Vertical Error! Vec3 Null!");
            }
        } else {
            System.out.println("Error! " + vec1 + " " + vec2);
        }

        return returnInt;
    }

    private Vec3 getNextVec() {
        try {
            if (pathPoints.get(0) != null) {
                return pathPoints.get(0);
            }
        } catch (Exception ignored) {
        }
        return pos;
    }

    private boolean blockIsPassable(Block block) {
        for (Block b : passThroughBlockList) {
            if (b.toString().equals(block.toString())) {
                return true;
            }
        }
        return false;
    }

    private void optimizePath(List<Vec3> path) {
        List<Vec3> optimizedPath = new ArrayList<>();
        for (int i = 0; i <= path.size() - 1; i++) {
            Vec3 pathPoint = path.get(i);

            if (i == path.size() - 1) {
                optimizedPath.add(pathPoint);
                break;
            }

            int touchingPoint = 0;
            for (int x = i + 1; x <= path.size() - 1; x++) {
                if (vec3Touching(pathPoint, path.get(x))
                        && !optimizeBadIndex.contains(x)) {
                    touchingPoint = x;
                }
            }
            if (touchingPoint != 0) {
                optimizedPath.add(pathPoint);
                optimizedPath.add(path.get(touchingPoint));
                i = touchingPoint - 1;
            } else {
                optimizeBadIndex.add(i);
                return;
            }
        }
        BlockRendering.renderMap.clear();
        optimizedPath.forEach(vec3 -> BlockRendering.renderMap.put(new BlockPos(vec3), Color.BLUE));
        BlockRendering.renderMap.put(new BlockPos(pos), Color.WHITE);
        BlockRendering.renderMap.put(new BlockPos(destinationGlobal), Color.BLACK);

        finalPath = new LinkedList<>(optimizedPath);
        finalPath = new LinkedList<>(optimizedPath);

        optimizedFully = true;
    }

    private boolean vec3Touching(Vec3 vec1, Vec3 vec2) {
        try {
            Vec3 distanceVec = vec1.subtract(vec2);
            double blockRange = Math.abs(distanceVec.xCoord) + Math.abs(distanceVec.yCoord) + Math.abs(distanceVec.zCoord);

            return blockRange == 1.0;
        } catch (Exception ignored) {
        }
        return false;
    }

    private LinkedList<Vec3> sortPathPoints(List<Vec3> pp) {
        LinkedList<Vec3> newList = new LinkedList<>(pp);
        HashMap<Vec3, Double> hashmap = new LinkedHashMap<>();
        for (Vec3 vec3 : newList) {
            if (Utils.distanceBetweenPoints(destinationGlobal, vec3) != null) {
                hashmap.put(vec3, Utils.distanceBetweenPoints(destinationGlobal, vec3));
            }
        }
        List<Map.Entry<Vec3, Double>> list = new LinkedList<>(hashmap.entrySet());
        list.sort(Map.Entry.comparingByValue());
        newList.clear();
        for (Map.Entry<Vec3, Double> entry : list) {
            if (Utils.vec3NotContains(newList, entry.getKey()) && Utils.vec3NotContains(badPoints, entry.getKey())) {
                newList.add(entry.getKey());
            }
        }
        return newList;
    }
}