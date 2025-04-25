package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.ParticleHandler;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BeaconRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DianaWaypoints {

    public int tickCount = 0;
    BlockPos guessPoint = null;
    ArrayList<BlockPos> unknownBurrows = new ArrayList<>();
    static HashMap<BlockPos, String> points = new HashMap<>();
    boolean worldAccessAdded = false;
    ArrayList<BlockPos> clickedBurials = new ArrayList<>();

    @SubscribeEvent
    public void renderTicks(RenderWorldLastEvent event) {
        if (AIOMVigilanceConfig.waypointsOn) {
            if (guessPoint != null && AIOMVigilanceConfig.guessWaypointsOn) {
                drawWaypoint(guessPoint, "Guess");
            }

            for (Map.Entry<BlockPos, String> point : points.entrySet()) {
                if (!clickedBurials.contains(point.getKey())) {
                    drawWaypoint(point.getKey(), point.getValue());
                }
            }
        }
    }

    @SubscribeEvent
    public void soundSetupEvent(SoundEvent.SoundSourceEvent event) {
        String soundName = event.sound.getSoundLocation().toString();
        double x = event.sound.getXPosF();
        double y = event.sound.getYPosF();
        double z = event.sound.getZPosF();
        float pitch = event.sound.getPitch();

        if (AIOMVigilanceConfig.guessWaypointsOn) {
            if (soundName.equals("minecraft:note.harp")) {
                if (ParticleHandler.lastDing == 0) {
                    ParticleHandler.firstPitch = pitch;
                }
                ParticleHandler.lastDing = Utils.currentTimeMillis();
                if (pitch < ParticleHandler.lastDingPitch) {
                    ParticleHandler.firstPitch = pitch;
                    ParticleHandler.dingIndex = 0;
                    ParticleHandler.dingSlope = new ArrayList<>();
                    ParticleHandler.lastDingPitch = pitch;
                    ParticleHandler.lastParticlePoint = null;
                    ParticleHandler.lastParticlePoint2 = null;
                    ParticleHandler.lastSoundPoint = null;
                    ParticleHandler.firstParticlePoint = null;
                }
                if (ParticleHandler.lastDingPitch == 0) {
                    ParticleHandler.lastDingPitch = pitch;
                    ParticleHandler.lastParticlePoint = null;
                    ParticleHandler.lastParticlePoint2 = null;
                    ParticleHandler.lastSoundPoint = null;
                    ParticleHandler.firstParticlePoint = null;
                    return;
                }
                ParticleHandler.dingIndex++;
                if (ParticleHandler.dingIndex > 1) {
                    ParticleHandler.dingSlope.add(pitch - ParticleHandler.lastDingPitch);
                }
                if (ParticleHandler.dingSlope.size() > 15) {
                    ParticleHandler.dingSlope.remove(0);
                }
                float slope = ParticleHandler.dingSlope.stream().reduce(0f, Float::sum) / ParticleHandler.dingSlope.size();

                ParticleHandler.lastSoundPoint = new BlockPos(x, y, z);
                ParticleHandler.lastDingPitch = pitch;

                if (ParticleHandler.lastParticlePoint != null && ParticleHandler.particlePoint != null && ParticleHandler.firstParticlePoint != null) {
                    ParticleHandler.distance = Math.E / slope - Math.sqrt(((ParticleHandler.firstParticlePoint.getX() - x) * (ParticleHandler.firstParticlePoint.getX() - x)) + ((ParticleHandler.firstParticlePoint.getY() - y) * (ParticleHandler.firstParticlePoint.getY() - y)) + ((ParticleHandler.firstParticlePoint.getZ() - z) * (ParticleHandler.firstParticlePoint.getZ() - z)));
                    guessPoint = ParticleHandler.guessForPoint();
                    BeaconRendering.beaconData.remove("Guess");
                    Utils.renderBeacon(guessPoint, Color.cyan, "Guess");
                }
            }
        }
    }

    @SubscribeEvent
    public void handleWorldAccess(TickEvent.ClientTickEvent event) {
        if (tickCount >= 20) {
            tickCount = 0;
            ParticleHandler.startParticles.clear();
            ParticleHandler.treasureParticles.clear();
            ParticleHandler.mobParticles.clear();
            ParticleHandler.burrowParticles.clear();
            ParticleHandler.finishedParticles.clear();
        }
        tickCount++;

        if (Main.notNull) {
            boolean hasAccess = false;

            try {
                final Field worldAccesses = ReflectionHelper.findField(World.class, "field_73021_x", "worldAccesses");

                for (IWorldAccess iWorldAccess : ((List<IWorldAccess>) worldAccesses.get((Main.mcWorld)))) {
                    if (iWorldAccess.getClass().equals(ParticleHandler.class)) {
                        hasAccess = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            worldAccessAdded = hasAccess;

            if (!worldAccessAdded) {
                worldAccessAdded = true;
                points.clear();
                ParticleHandler.startParticles.clear();
                ParticleHandler.treasureParticles.clear();
                ParticleHandler.mobParticles.clear();
                ParticleHandler.burrowParticles.clear();
                ParticleHandler.finishedParticles.clear();
                BeaconRendering.beaconData.clear();
                unknownBurrows.clear();
                clickedBurials.clear();
                System.out.println("Added World Access");
                Main.mcWorld.addWorldAccess(new ParticleHandler());
            }

            for (Map.Entry<BlockPos, Integer> bpSet : ParticleHandler.burrowParticles.entrySet()) {
                if (bpSet.getValue() >= 3) {
                    unknownBurrows.add(bpSet.getKey());
                }
            }

            for (Map.Entry<BlockPos, Integer> bpSet : ParticleHandler.finishedParticles.entrySet()) {
                if (bpSet.getValue() >= 6) {
                    clickedBurials.add(bpSet.getKey());
                    points.remove(bpSet.getKey());
                    BeaconRendering.beaconData.remove(bpSet.getKey().toString());
                }
            }

            for (Map.Entry<BlockPos, Integer> bpSet : ParticleHandler.mobParticles.entrySet()) {
                if (bpSet.getValue() >= 3) {
                    if (unknownBurrows.contains(bpSet.getKey()) && !points.containsKey(bpSet.getKey())) {
                        points.put(bpSet.getKey(), "Mob");
                    }
                }
            }

            for (Map.Entry<BlockPos, Integer> bpSet : ParticleHandler.treasureParticles.entrySet()) {
                if (bpSet.getValue() >= 3) {
                    if (unknownBurrows.contains(bpSet.getKey()) && !points.containsKey(bpSet.getKey())) {
                        points.put(bpSet.getKey(), "Treasure");
                    }
                }
            }

            for (Map.Entry<BlockPos, Integer> bpSet : ParticleHandler.startParticles.entrySet()) {
                if (bpSet.getValue() >= 3) {
                    if (unknownBurrows.contains(bpSet.getKey()) && !points.containsKey(bpSet.getKey())) {
                        points.put(bpSet.getKey(), "Start");
                    }
                }
            }
        } else {
            if (worldAccessAdded) {
                System.out.println("Removed World Access");
                worldAccessAdded = false;
            }
        }
    }

    public static int getSpadeIndex() {
        int spadeIndex = Main.mcPlayer.inventory.currentItem;
        for (int i = 0; i <= 9; i++) {
            if (Main.mcPlayer.inventory.mainInventory[i] != null && Main.mcPlayer.inventory.mainInventory[i].getDisplayName() != null && Main.mcPlayer.inventory.mainInventory[i].getDisplayName().contains("Ancestral Spade")) {
                spadeIndex = i;
                break;
            }
        }
        return spadeIndex;
    }

    @SubscribeEvent
    public void clientChatReceivedEvent(ClientChatReceivedEvent event) {
        String strippedMessage = Utils.stripColor(event.message.getUnformattedText());

        if (strippedMessage.contains("Griffin burrow") || strippedMessage.contains("Griffin Burrow")) {
            for (Map.Entry<BlockPos, String> point : points.entrySet()) {
                if (point.getValue().equals("Mob") || point.getValue().equals("Treasure")) {
                    clickedBurials.add(point.getKey());
                }
            }
            points.values().remove("Mob");
            points.values().remove("Treasure");
            points.values().remove("Guess");
            BeaconRendering.beaconData.remove("Mob");
            BeaconRendering.beaconData.remove("Treasure");
            BeaconRendering.beaconData.remove("Guess");
            if (AIOMVigilanceConfig.useEchoAfterBurrow) {
                Main.sendMarkedChatMessage("Using Echo!");
                int currentItem = Main.mcPlayer.inventory.currentItem;
                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getSpadeIndex()));
                Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getStackInSlot(getSpadeIndex()));
                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentItem));
            }
        }
    }

    public void drawWaypoint(BlockPos waypoint, String pointName) {
        switch (pointName) {
            case "Start":
                Utils.renderBeacon(waypoint, Color.GREEN, waypoint.toString());
                break;
            case "Treasure":
                Utils.renderBeacon(waypoint, Color.YELLOW, "Treasure");
                break;
            case "Mob":
                Utils.renderBeacon(waypoint, Color.RED, "Mob");
                break;
        }
    }
}
