package me.jacktym.aiomacro;

import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BeaconRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ParticleHandler implements IWorldAccess {

    public static HashMap<BlockPos, Integer> startParticles = new HashMap<>();
    public static HashMap<BlockPos, Integer> mobParticles = new HashMap<>();
    public static HashMap<BlockPos, Integer> treasureParticles = new HashMap<>();
    public static HashMap<BlockPos, Integer> burrowParticles = new HashMap<>();
    public static HashMap<BlockPos, Integer> finishedParticles = new HashMap<>();
    public static float firstPitch = 0;

    public void onEntityAdded(Entity entityIn) {
    }

    public void onEntityRemoved(Entity entityIn) {
    }

    public static long lastDing = 0;
    public static float lastDingPitch = 0;
    public static int dingIndex = 0;
    public static ArrayList<Float> dingSlope = new ArrayList<>();
    public static BlockPos lastParticlePoint;
    public static BlockPos lastParticlePoint2;
    public static BlockPos lastSoundPoint;
    public static BlockPos firstParticlePoint;
    public static BlockPos particlePoint;
    public static double distance;
    public static BlockPos guessPoint;

    public static BlockPos guessForPoint() {
        double lineDist = Math.sqrt(((lastParticlePoint2.getX() - particlePoint.getX()) * (lastParticlePoint2.getX() - particlePoint.getX())) + ((lastParticlePoint2.getY() - particlePoint.getY()) * (lastParticlePoint2.getY() - particlePoint.getY())) + ((lastParticlePoint2.getZ() - particlePoint.getZ()) * (lastParticlePoint2.getZ() - particlePoint.getZ())));
        ArrayList<Double> changes = new ArrayList<>();
        changes.add((particlePoint.getX() - lastParticlePoint2.getX()) / lineDist);
        changes.add((particlePoint.getY() - lastParticlePoint2.getY()) / lineDist);
        changes.add((particlePoint.getZ() - lastParticlePoint2.getZ()) / lineDist);

        return new BlockPos(lastSoundPoint.getX() + changes.get(0) * distance, lastSoundPoint.getY() + changes.get(1) * distance, lastSoundPoint.getZ() + changes.get(2) * distance);
    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_180442_15_) {
        if (AIOMVigilanceConfig.waypointsOn) {
            //System.out.println(particleID + " " + ignoreRange + " " + xCoord + " " + yCoord + " " + zCoord + " " + xOffset + " " + yOffset + " " + zOffset + " " + Arrays.toString(p_180442_15_));

            BlockPos bp = new BlockPos(xCoord, yCoord - 1, zCoord);

            if (particleID == 10) {
                //System.out.println("Possible Start Burrow! " + bp);

                if (!startParticles.containsKey(bp)) {
                    startParticles.put(bp, 1);
                } else {
                    startParticles.put(bp, startParticles.get(bp) + 1);
                }
            }
            if (particleID == 9) {
                //System.out.println("Possible Mob Burrow! " + bp);

                if (!mobParticles.containsKey(bp)) {
                    mobParticles.put(bp, 1);
                } else {
                    mobParticles.put(bp, mobParticles.get(bp) + 1);
                }
            }
            if (particleID == 19) {
                //System.out.println("Possible Treasure Burrow! " + bp);

                if (!treasureParticles.containsKey(bp)) {
                    treasureParticles.put(bp, 1);
                } else {
                    treasureParticles.put(bp, treasureParticles.get(bp) + 1);
                }
            }
            if (particleID == 28) {
                //System.out.println("Possible Burrow! " + bp);

                if (!burrowParticles.containsKey(bp)) {
                    burrowParticles.put(bp, 1);
                } else {
                    burrowParticles.put(bp, burrowParticles.get(bp) + 1);
                }
            }

            if (particleID == 30) {
                //System.out.println("DUG BURROW");
                //System.out.println("Possible Direction! " + bp + " " + new Color((int) (255*Math.abs(xOffset)), (int) (255*Math.abs(yOffset)), (int) (255*Math.abs(zOffset))));
                if (!finishedParticles.containsKey(bp)) {
                    finishedParticles.put(bp, 1);
                } else {
                    finishedParticles.put(bp, finishedParticles.get(bp) + 1);
                }
            }

            if (AIOMVigilanceConfig.guessWaypointsOn) {
                if (particleID == 3) {
                    //System.out.println("Possible Path! " + bp);

                    if (lastSoundPoint != null && Math.abs(xCoord - lastSoundPoint.getX()) < 2 && Math.abs(yCoord - lastSoundPoint.getY()) < 0.5 && Math.abs(zCoord - lastSoundPoint.getZ()) < 2) {
                        if (lastParticlePoint == null) {
                            firstParticlePoint = new BlockPos(xCoord, yCoord, zCoord);
                        }
                        lastParticlePoint2 = lastParticlePoint;
                        lastParticlePoint = particlePoint;
                        particlePoint = new BlockPos(xCoord, yCoord, zCoord);

                        if (lastParticlePoint2 != null && firstParticlePoint != null && distance != 0 && lastSoundPoint != null) {
                            guessPoint = guessForPoint();
                            BeaconRendering.beaconData.remove("Guess");
                            Utils.renderBeacon(guessPoint, Color.cyan, "Guess");
                        }
                    }
                }
            }
        }
    }

    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
    }

    public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float f) {
    }

    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    public void markBlockForUpdate(BlockPos pos) {
    }

    public void notifyLightSet(BlockPos pos) {
    }

    public void playRecord(String recordName, BlockPos blockPosIn) {
    }

    public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int data) {
    }

    public void broadcastSound(int soundID, BlockPos pos, int data) {
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
    }
}
