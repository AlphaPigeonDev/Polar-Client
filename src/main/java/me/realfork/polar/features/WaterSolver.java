package me.jacktym.aiomacro.features;


import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class WaterSolver {

    private boolean prevInWaterRoom = false;
    private boolean inWaterRoom = false;
    private List<String> purpleLeverOrder = new ArrayList<>();
    private List<String> orangeLeverOrder = new ArrayList<>();
    private List<String> blueLeverOrder = new ArrayList<>();
    private List<String> greenLeverOrder = new ArrayList<>();
    private List<String> redLeverOrder = new ArrayList<>();

    private final Map<String, List<String>> orders = new LinkedHashMap<>();

    private boolean purpleOpen;
    private boolean orangeOpen;
    private boolean blueOpen;
    private boolean greenOpen;
    private boolean redOpen;


    private BlockPos purpleGateCheck;
    private BlockPos orangeGateCheck;
    private BlockPos blueGateCheck;
    private BlockPos greenGateCheck;
    private BlockPos redGateCheck;

    public int tick = 0;



    public int tick1 = -1;
    private boolean sentMessage1 = false;
    private boolean sentMessage2 = false;

    private BlockPos quartzLever;
    private BlockPos diamondLever;
    private BlockPos goldLever;
    private BlockPos emeraldLever;
    private BlockPos coalLever;
    private BlockPos clayLever;
    private BlockPos waterLever;


    private boolean quartzLeverActive = false;
    private boolean diamondLeverActive = false;
    private boolean goldLeverActive = false;
    private boolean emeraldLeverActive = false;
    private boolean coalLeverActive = false;
    private boolean clayLeverActive = false;

    private boolean waterLeverActive = false;


    public int tick2 = 0;

    private boolean sendMessage01 = false;
    private boolean doRender1 = false;

    private final boolean sentMessage = false;
    public int tick3 = 0;
    private boolean toggleLever = false;
    private boolean sendMessage0 = false;
    private boolean doRender = false;


    private final Map<String, Boolean> purpleLeverCheck = new HashMap<>();
    private final Map<String, Boolean> orangeLeverCheck = new HashMap<>();
    private final Map<String, Boolean> blueLeverCheck = new HashMap<>();
    private final Map<String, Boolean> greenLeverCheck = new HashMap<>();
    private final Map<String, Boolean> redLeverCheck = new HashMap<>();

    private String currentGate = "";

    private boolean sendMessage3 = false;


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void resetVars(ClientChatReceivedEvent e) {
        String strippedMessage = Utils.stripColor(e.message.getUnformattedText());

        if (strippedMessage.contains("Here, I found this map when I first entered the dungeon.")) {
            BlockRendering.renderMap.clear();

            MinecraftForge.EVENT_BUS.unregister(this);
            MinecraftForge.EVENT_BUS.register(new WaterSolver());
        }

    }
    @SubscribeEvent
    public final void messageHandler(TickEvent.ClientTickEvent event) {
        if (!sentMessage1) {
            if (inWaterRoom) {
                Main.sendMarkedChatMessage("Detected Water Room! Please keep move around to fully render all the gates!");
                sentMessage1 = true;
            }
        }
        if (!sentMessage2) {
            if (purpleOpen && orangeOpen && blueOpen && greenOpen && redOpen) {
                Main.sendMarkedChatMessage("Water Puzzle Complete!");
                sentMessage2 = true;
            }
        }

    }
    @SubscribeEvent
    public void coolDownManager(TickEvent.ClientTickEvent event) {

        if (tick3 >= 10) {

            tick3 = 0;
        }
        toggleLever = tick3 == 1;
        tick3++;

    }


    @SubscribeEvent
    public void waterLogic(TickEvent.ClientTickEvent event) {

        if (tick >= 2) {
            if (event.phase != TickEvent.Phase.START) return;

            EntityPlayerSP player = Main.mcPlayer;
            World world = Main.mcWorld;
            if (AIOMVigilanceConfig.waterToggled && world != null && player != null) {
                new Thread(() -> {
                    prevInWaterRoom = inWaterRoom;
                    inWaterRoom = false;
                    boolean foundPiston = false;
                    boolean done = false;
                    if(purpleGateCheck != null && orangeGateCheck != null && blueGateCheck != null && greenGateCheck != null && redGateCheck != null) {
                        doRender = true;

                        if(!sendMessage0){
                            Main.sendMarkedChatMessage("Successfully rendered all gates! Solving puzzle now...");
                            sendMessage0 = true;
                        }
                    }
                    if(doRender) {
                        purpleOpen = (world.getBlockState(purpleGateCheck.add(0, -1, 0)).getBlock() == Blocks.sticky_piston) || (world.getBlockState(purpleGateCheck).getBlock() == Blocks.air);
                        orangeOpen = (world.getBlockState(orangeGateCheck.add(0, -1, 0)).getBlock() == Blocks.sticky_piston) || (world.getBlockState(orangeGateCheck).getBlock() == Blocks.air);
                        blueOpen = (world.getBlockState(blueGateCheck.add(0, -1, 0)).getBlock() == Blocks.sticky_piston) || (world.getBlockState(blueGateCheck).getBlock() == Blocks.air);
                        greenOpen = (world.getBlockState(greenGateCheck.add(0, -1, 0)).getBlock() == Blocks.sticky_piston) || (world.getBlockState(greenGateCheck).getBlock() == Blocks.air);
                        redOpen = (world.getBlockState(redGateCheck.add(0, -1, 0)).getBlock() == Blocks.sticky_piston) || (world.getBlockState(redGateCheck).getBlock() == Blocks.air);
                        foundPiston = true;

                    }
                    else if(!doRender){
                        for (int x = (int) (player.posX - 4); x <= player.posX + 4; x++) {
                            for (int z = (int) (player.posZ - 4); z <= player.posZ + 4; z++) {
                                BlockPos blockPos = new BlockPos(x, 54, z);
                                if (world.getBlockState(blockPos).getBlock() == Blocks.sticky_piston) {
                                    if (world.getBlockState(blockPos.add(0, 1, 0)).getProperties().containsValue(EnumDyeColor.PURPLE)) {
                                        purpleOpen = true;
                                        purpleGateCheck = blockPos.add(0, 1, 0);
                                    } else if (world.getBlockState(blockPos.add(0, 1, 0)).getProperties().containsValue(EnumDyeColor.ORANGE)) {
                                        orangeOpen = true;
                                        orangeGateCheck = blockPos.add(0, 1, 0);
                                    } else if (world.getBlockState(blockPos.add(0, 1, 0)).getProperties().containsValue(EnumDyeColor.BLUE)) {
                                        blueOpen = true;
                                        blueGateCheck = blockPos.add(0, 1, 0);
                                    } else if (world.getBlockState(blockPos.add(0, 1, 0)).getProperties().containsValue(EnumDyeColor.LIME)) {
                                        greenOpen = true;
                                        greenGateCheck = blockPos.add(0, 1, 0);
                                    } else if (world.getBlockState(blockPos.add(0, 1, 0)).getProperties().containsValue(EnumDyeColor.RED)) {
                                        redOpen = true;
                                        redGateCheck = blockPos.add(0, 1, 0);
                                    }

                                    //handle checking the non-opened gates

                                    else if (world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.piston_head) {
                                        if (world.getBlockState(blockPos.add(0, 2, 0)).getProperties().containsValue(EnumDyeColor.PURPLE)) {
                                            purpleOpen = false;
                                            purpleGateCheck = blockPos.add(0, 2, 0);
                                        } else if (world.getBlockState(blockPos.add(0, 2, 0)).getProperties().containsValue(EnumDyeColor.ORANGE)) {
                                            orangeOpen = false;
                                            orangeGateCheck = blockPos.add(0, 2, 0);
                                        } else if (world.getBlockState(blockPos.add(0, 2, 0)).getProperties().containsValue(EnumDyeColor.BLUE)) {
                                            blueOpen = false;
                                            blueGateCheck = blockPos.add(0, 2, 0);
                                        } else if (world.getBlockState(blockPos.add(0, 2, 0)).getProperties().containsValue(EnumDyeColor.LIME)) {
                                            greenOpen = false;
                                            greenGateCheck = blockPos.add(0, 2, 0);
                                        } else if (world.getBlockState(blockPos.add(0, 2, 0)).getProperties().containsValue(EnumDyeColor.RED)) {
                                            redOpen = false;
                                            redGateCheck = blockPos.add(0, 2, 0);
                                        }
                                    }
                                    foundPiston = true;
                                    break;
                                }
                            }
                            if (foundPiston) break;
                        }

                    }

                    if (foundPiston) {
                        for (int x = (int) (player.posX - 25); x <= player.posX + 25; x++) {
                            for (int z = (int) (player.posZ - 25); z <= player.posZ + 25; z++) {
                                BlockPos blockPos = new BlockPos(x, 82, z);
                                if (world.getBlockState(blockPos).getBlock() == Blocks.piston_head) {
                                    inWaterRoom = true;
                                    if (!prevInWaterRoom) {
                                        boolean foundGold = false;
                                        boolean foundClay = false;
                                        boolean foundEmerald = false;
                                        boolean foundQuartz = false;
                                        boolean foundDiamond = false;


                                        BlockPos scan1 = new BlockPos(x + 1, 78, z + 1);
                                        BlockPos scan2 = new BlockPos(x - 1, 77, z - 1);
                                        Iterable<BlockPos> blocks = BlockPos.getAllInBox(scan1, scan2);
                                        for (BlockPos puzzleBlockPos : blocks) {
                                            Block block = world.getBlockState(puzzleBlockPos).getBlock();
                                            if (block == Blocks.gold_block) {
                                                foundGold = true;
                                            } else if (block == Blocks.hardened_clay) {
                                                foundClay = true;
                                            } else if (block == Blocks.emerald_block) {
                                                foundEmerald = true;
                                            } else if (block == Blocks.quartz_block) {
                                                foundQuartz = true;
                                            } else if (block == Blocks.diamond_block) {
                                                foundDiamond = true;
                                            }
                                        }

                                        int variant = 0;
                                        if (foundGold && foundClay) {
                                            variant = 1;
                                        } else if (foundEmerald && foundQuartz) {
                                            variant = 2;
                                        } else if (foundQuartz && foundDiamond) {
                                            variant = 3;
                                        } else if (foundGold && foundQuartz) {
                                            variant = 4;
                                        }

                                        // Return solution
                                        String purple;
                                        String orange;
                                        String blue;
                                        String green;
                                        String red;
                                        switch (variant) {
                                            case 1:
                                                purple = "Quartz, Gold, Diamond, Clay";
                                                orange = "Gold, Coal, Emerald";
                                                blue = "Quartz, Gold, Emerald, Clay";
                                                green = "Emerald";
                                                red = "None";
                                                break;
                                            case 2:
                                                purple = "Coal";
                                                orange = "Quartz, Gold, Emerald, Clay";
                                                blue = "Quartz, Diamond, Emerald";
                                                green = "Quartz, Emerald";
                                                red = "Quartz, Coal, Emerald";
                                                break;
                                            case 3:
                                                purple = "Quartz, Gold, Diamond";
                                                orange = "Emerald";
                                                blue = "Quartz, Diamond";
                                                green = "None";
                                                red = "Gold, Emerald";
                                                break;
                                            case 4:
                                                purple = "Quartz, Gold, Emerald, Clay";
                                                orange = "Gold, Coal";
                                                blue = "Quartz, Gold, Coal, Emerald, Clay";
                                                green = "Gold, Emerald";
                                                red = "Gold, Diamond, Emerald, Clay";
                                                break;
                                            default:
                                                purple = orange = blue = green = red = "";
                                                break;
                                        }
                                        purpleLeverOrder = Arrays.asList(purple.replace(" ", "").split(","));
                                        orangeLeverOrder = Arrays.asList(orange.replace(" ", "").split(","));
                                        blueLeverOrder = Arrays.asList(blue.replace(" ", "").split(","));
                                        greenLeverOrder = Arrays.asList(green.replace(" ", "").split(","));
                                        redLeverOrder = Arrays.asList(red.replace(" ", "").split(","));

                                        if (orders.isEmpty()) {
                                            orders.put("Purple", purpleLeverOrder);
                                            orders.put("Orange", orangeLeverOrder);
                                            orders.put("Blue", blueLeverOrder);
                                            orders.put("Green", greenLeverOrder);
                                            orders.put("Red", redLeverOrder);
                                        }


                                        done = true;
                                        break;
                                    }
                                }
                            }
                            if (done) break;
                        }
                    }
                }).start();
            }
            tick = 0;
        }
        tick++;


    }

    @SubscribeEvent
    public void leverHandler(TickEvent.ClientTickEvent event) {
        if (tick2 >= 2) {
            if (event.phase != TickEvent.Phase.START) return;

            EntityPlayerSP player = Main.mcPlayer;
            World world = Main.mcWorld;
            if (world != null && player != null && inWaterRoom) {
                BlockRendering.renderMap.clear();
                new Thread(() -> {
                    if(quartzLever != null && diamondLever != null && goldLever != null && emeraldLever != null && coalLever != null && clayLever != null) {
                        doRender1 = true;

                        if(!sendMessage01){
                            Main.sendMarkedChatMessage("Successfully rendered all levers! Attempting to render all gates...");
                            sendMessage01 = true;
                        }
                    }
                    if(doRender1) {
                        if (world.getBlockState(quartzLever).getProperties().containsValue(false)) {
                            BlockRendering.renderMap.put(quartzLever, Color.WHITE);
                            quartzLeverActive = false;
                        } else {
                            BlockRendering.renderMap.put(quartzLever, Color.RED);
                            quartzLeverActive = true;
                        }
                        if (world.getBlockState(diamondLever).getProperties().containsValue(false)) {
                            BlockRendering.renderMap.put(diamondLever, Color.CYAN);
                            diamondLeverActive = false;
                        } else {
                            BlockRendering.renderMap.put(diamondLever, Color.RED);
                            diamondLeverActive = true;
                        }
                        if (world.getBlockState(goldLever).getProperties().containsValue(false)) {
                            BlockRendering.renderMap.put(goldLever, Color.YELLOW);
                            goldLeverActive = false;
                        } else {
                            BlockRendering.renderMap.put(goldLever, Color.RED);
                            goldLeverActive = true;
                        }
                        if (world.getBlockState(emeraldLever).getProperties().containsValue(false)) {
                            BlockRendering.renderMap.put(emeraldLever, Color.GREEN);
                            emeraldLeverActive = false;
                        } else {
                            BlockRendering.renderMap.put(emeraldLever, Color.RED);
                            emeraldLeverActive = true;
                        }
                        if (world.getBlockState(coalLever).getProperties().containsValue(false)) {
                            BlockRendering.renderMap.put(coalLever, Color.BLACK);
                            coalLeverActive = false;
                        } else {
                            BlockRendering.renderMap.put(coalLever, Color.RED);
                            coalLeverActive = true;
                        }
                        if (world.getBlockState(clayLever).getProperties().containsValue(false)) {
                            BlockRendering.renderMap.put(clayLever, Color.PINK);
                            clayLeverActive = false;
                        } else {
                            BlockRendering.renderMap.put(clayLever, Color.RED);
                            clayLeverActive = true;
                        }
                        if (world.getBlockState(waterLever).getBlock() == Blocks.lever) {
                            if (world.getBlockState(waterLever).getProperties().containsValue(false)) {
                                BlockRendering.renderMap.put(waterLever, Color.GRAY);
                                waterLeverActive = false;
                            } else {
                                BlockRendering.renderMap.put(waterLever, Color.RED);
                                waterLeverActive = true;
                            }
                        }
                    }

                    if(!doRender1) {
                        for (int x = (int) (player.posX - 20); x <= player.posX + 20; x++) {
                            for (int z = (int) (player.posZ - 20); z <= player.posZ + 20; z++) {
                                BlockPos blockPos = new BlockPos(x, 61, z);
                                BlockPos blockPos1 = new BlockPos(x, 60, z);
                                if (world.getBlockState(blockPos).getBlock() == Blocks.quartz_block) {

                                    Block pos1 = world.getBlockState(blockPos.add(-1, 0, 0)).getBlock();
                                    Block pos2 = world.getBlockState(blockPos.add(1, 0, 0)).getBlock();
                                    Block pos3 = world.getBlockState(blockPos.add(0, 0, -1)).getBlock();
                                    Block pos4 = world.getBlockState(blockPos.add(0, 0, 1)).getBlock();

                                    if (pos1 == Blocks.lever) {
                                        quartzLever = blockPos.add(-1, 0, 0);
                                        if (world.getBlockState(blockPos.add(-1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.WHITE);
                                            quartzLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.RED);
                                            quartzLeverActive = true;

                                        }
                                    }
                                    if (pos2 == Blocks.lever) {
                                        quartzLever = blockPos.add(1, 0, 0);
                                        if (world.getBlockState(blockPos.add(1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.WHITE);
                                            quartzLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.RED);
                                            quartzLeverActive = true;
                                        }
                                    }
                                    if (pos3 == Blocks.lever) {
                                        quartzLever = blockPos.add(0, 0, -1);
                                        if (world.getBlockState(blockPos.add(0, 0, -1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.WHITE);
                                            quartzLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.RED);
                                            quartzLeverActive = true;
                                        }
                                    }
                                    if (pos4 == Blocks.lever) {
                                        quartzLever = blockPos.add(0, 0, 1);
                                        if (world.getBlockState(blockPos.add(0, 0, 1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.WHITE);
                                            quartzLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.RED);
                                            quartzLeverActive = true;
                                        }
                                    }
                                }
                                if (world.getBlockState(blockPos).getBlock() == Blocks.diamond_block) {

                                    Block pos1 = world.getBlockState(blockPos.add(-1, 0, 0)).getBlock();
                                    Block pos2 = world.getBlockState(blockPos.add(1, 0, 0)).getBlock();
                                    Block pos3 = world.getBlockState(blockPos.add(0, 0, -1)).getBlock();
                                    Block pos4 = world.getBlockState(blockPos.add(0, 0, 1)).getBlock();

                                    if (pos1 == Blocks.lever) {
                                        diamondLever = blockPos.add(-1, 0, 0);
                                        if (world.getBlockState(blockPos.add(-1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.CYAN);
                                            diamondLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.RED);
                                            diamondLeverActive = true;

                                        }
                                    }
                                    if (pos2 == Blocks.lever) {
                                        diamondLever = blockPos.add(1, 0, 0);
                                        if (world.getBlockState(blockPos.add(1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.CYAN);
                                            diamondLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.RED);
                                            diamondLeverActive = true;
                                        }
                                    }
                                    if (pos3 == Blocks.lever) {
                                        diamondLever = blockPos.add(0, 0, -1);
                                        if (world.getBlockState(blockPos.add(0, 0, -1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.CYAN);
                                            diamondLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.RED);
                                            diamondLeverActive = true;
                                        }
                                    }
                                    if (pos4 == Blocks.lever) {
                                        diamondLever = blockPos.add(0, 0, 1);
                                        if (world.getBlockState(blockPos.add(0, 0, 1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.CYAN);
                                            diamondLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.RED);
                                            diamondLeverActive = true;
                                        }
                                    }
                                }
                                if (world.getBlockState(blockPos).getBlock() == Blocks.gold_block) {

                                    Block pos1 = world.getBlockState(blockPos.add(-1, 0, 0)).getBlock();
                                    Block pos2 = world.getBlockState(blockPos.add(1, 0, 0)).getBlock();
                                    Block pos3 = world.getBlockState(blockPos.add(0, 0, -1)).getBlock();
                                    Block pos4 = world.getBlockState(blockPos.add(0, 0, 1)).getBlock();

                                    if (pos1 == Blocks.lever) {
                                        goldLever = blockPos.add(-1, 0, 0);
                                        if (world.getBlockState(blockPos.add(-1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.YELLOW);
                                            goldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.RED);
                                            goldLeverActive = true;

                                        }
                                    }
                                    if (pos2 == Blocks.lever) {
                                        goldLever = blockPos.add(1, 0, 0);
                                        if (world.getBlockState(blockPos.add(1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.YELLOW);
                                            goldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.RED);
                                            goldLeverActive = true;
                                        }
                                    }
                                    if (pos3 == Blocks.lever) {
                                        goldLever = blockPos.add(0, 0, -1);
                                        if (world.getBlockState(blockPos.add(0, 0, -1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.YELLOW);
                                            goldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.RED);
                                            goldLeverActive = true;
                                        }
                                    }
                                    if (pos4 == Blocks.lever) {
                                        goldLever = blockPos.add(0, 0, 1);
                                        if (world.getBlockState(blockPos.add(0, 0, 1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.YELLOW);
                                            goldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.RED);
                                            goldLeverActive = true;
                                        }
                                    }

                                }
                                if (world.getBlockState(blockPos).getBlock() == Blocks.emerald_block) {

                                    Block pos1 = world.getBlockState(blockPos.add(-1, 0, 0)).getBlock();
                                    Block pos2 = world.getBlockState(blockPos.add(1, 0, 0)).getBlock();
                                    Block pos3 = world.getBlockState(blockPos.add(0, 0, -1)).getBlock();
                                    Block pos4 = world.getBlockState(blockPos.add(0, 0, 1)).getBlock();

                                    if (pos1 == Blocks.lever) {
                                        emeraldLever = blockPos.add(-1, 0, 0);
                                        if (world.getBlockState(blockPos.add(-1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.GREEN);
                                            emeraldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.RED);
                                            emeraldLeverActive = true;

                                        }
                                    }
                                    if (pos2 == Blocks.lever) {
                                        emeraldLever = blockPos.add(1, 0, 0);
                                        if (world.getBlockState(blockPos.add(1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.GREEN);
                                            emeraldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.RED);
                                            emeraldLeverActive = true;
                                        }
                                    }
                                    if (pos3 == Blocks.lever) {
                                        emeraldLever = blockPos.add(0, 0, -1);
                                        if (world.getBlockState(blockPos.add(0, 0, -1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.GREEN);
                                            emeraldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.RED);
                                            emeraldLeverActive = true;
                                        }
                                    }
                                    if (pos4 == Blocks.lever) {
                                        emeraldLever = blockPos.add(0, 0, 1);
                                        if (world.getBlockState(blockPos.add(0, 0, 1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.GREEN);
                                            emeraldLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.RED);
                                            emeraldLeverActive = true;
                                        }
                                    }

                                }
                                if (world.getBlockState(blockPos).getBlock() == Blocks.coal_block) {

                                    Block pos1 = world.getBlockState(blockPos.add(-1, 0, 0)).getBlock();
                                    Block pos2 = world.getBlockState(blockPos.add(1, 0, 0)).getBlock();
                                    Block pos3 = world.getBlockState(blockPos.add(0, 0, -1)).getBlock();
                                    Block pos4 = world.getBlockState(blockPos.add(0, 0, 1)).getBlock();

                                    if (pos1 == Blocks.lever) {
                                        coalLever = blockPos.add(-1, 0, 0);
                                        if (world.getBlockState(blockPos.add(-1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.BLACK);
                                            coalLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.RED);
                                            coalLeverActive = true;

                                        }
                                    }
                                    if (pos2 == Blocks.lever) {
                                        coalLever = blockPos.add(1, 0, 0);
                                        if (world.getBlockState(blockPos.add(1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.BLACK);
                                            coalLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.RED);
                                            coalLeverActive = true;
                                        }
                                    }
                                    if (pos3 == Blocks.lever) {
                                        coalLever = blockPos.add(0, 0, -1);
                                        if (world.getBlockState(blockPos.add(0, 0, -1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.BLACK);
                                            coalLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.RED);
                                            coalLeverActive = true;
                                        }
                                    }
                                    if (pos4 == Blocks.lever) {
                                        coalLever = blockPos.add(0, 0, 1);
                                        if (world.getBlockState(blockPos.add(0, 0, 1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.BLACK);
                                            coalLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.RED);
                                            coalLeverActive = true;
                                        }
                                    }

                                }
                                if (world.getBlockState(blockPos).getBlock() == Blocks.hardened_clay) {

                                    Block pos1 = world.getBlockState(blockPos.add(-1, 0, 0)).getBlock();
                                    Block pos2 = world.getBlockState(blockPos.add(1, 0, 0)).getBlock();
                                    Block pos3 = world.getBlockState(blockPos.add(0, 0, -1)).getBlock();
                                    Block pos4 = world.getBlockState(blockPos.add(0, 0, 1)).getBlock();

                                    if (pos1 == Blocks.lever) {
                                        clayLever = blockPos.add(-1, 0, 0);
                                        if (world.getBlockState(blockPos.add(-1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.PINK);
                                            clayLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(-1, 0, 0), Color.RED);
                                            clayLeverActive = true;

                                        }
                                    }
                                    if (pos2 == Blocks.lever) {
                                        clayLever = blockPos.add(1, 0, 0);
                                        if (world.getBlockState(blockPos.add(1, 0, 0)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.PINK);
                                            clayLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(1, 0, 0), Color.RED);
                                            clayLeverActive = true;
                                        }
                                    }
                                    if (pos3 == Blocks.lever) {
                                        clayLever = blockPos.add(0, 0, -1);
                                        if (world.getBlockState(blockPos.add(0, 0, -1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.PINK);
                                            clayLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, -1), Color.RED);
                                            clayLeverActive = true;
                                        }
                                    }
                                    if (pos4 == Blocks.lever) {
                                        clayLever = blockPos.add(0, 0, 1);
                                        if (world.getBlockState(blockPos.add(0, 0, 1)).getProperties().containsValue(false)) {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.PINK);
                                            clayLeverActive = false;
                                        } else {
                                            BlockRendering.renderMap.put(blockPos.add(0, 0, 1), Color.RED);
                                            clayLeverActive = true;
                                        }
                                    }

                                }
                                if (world.getBlockState(blockPos1).getBlock() == Blocks.lever) {
                                    waterLever = blockPos1;
                                    if (world.getBlockState(blockPos1).getProperties().containsValue(false)) {
                                        BlockRendering.renderMap.put(blockPos1, Color.GRAY);
                                        waterLeverActive = false;
                                    } else {
                                        BlockRendering.renderMap.put(blockPos1, Color.RED);
                                        waterLeverActive = true;
                                    }
                                }
                            }
                        }
                    }

                }).start();
            }


            tick2 = 0;
        }
        tick2++;

    }

    private int counter = 0;

    @SubscribeEvent
    public void solver(TickEvent.ClientTickEvent event) {


        EntityPlayerSP player = Main.mcPlayer;
        WorldClient world = Main.mcWorld;
        if(world != null && player != null && inWaterRoom){


            if(tick1 >= 20) {
                if(toggleLever){
                    if(waterLeverActive){
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), waterLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                        counter++;
                    }
                }
            }
            if(tick1 >= 0 && tick1 < 20) {
                if(toggleLever){
                    if(!waterLeverActive){
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), waterLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }

                }
            }
        }


        if (world != null && player != null && inWaterRoom) {
            if (currentGate.equals("Purple")) {

                int purpleCount = orders.get("Purple").size();
                if (!purpleLeverCheck.containsValue(false) && !purpleLeverCheck.isEmpty() && purpleLeverCheck.size() == purpleCount) {
                    if (!sendMessage3) {
                        Main.sendMarkedChatMessage("All levers for purple have been flicked! ");
                        sendMessage3 = true;
                    }
                    //toggle water
                    if(!(counter == AIOMVigilanceConfig.waterDuration)) {
                        tick1++;
                        if (tick1 >= 20 && !waterLeverActive) {
                            tick1 = -1;
                        }
                    }

                    //check for gate completion
                    if (purpleOpen) {
                        if(clearLevers()) {
                            currentGate = "";
                            purpleLeverCheck.clear();
                            sendMessage3 = false;
                            counter = 0;
                            Main.sendMarkedChatMessage("Purple has been solved!");
                        }
                    }


                } else {
                    orders.get("Purple").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                }
            }
            else if (currentGate.equals("Orange")) {

                int orangeCount = orders.get("Orange").size();
                if (!orangeLeverCheck.containsValue(false) && !orangeLeverCheck.isEmpty() && orangeLeverCheck.size() == orangeCount) {
                    if (!sendMessage3) {
                        Main.sendMarkedChatMessage("All levers for orange have been flicked!");
                        sendMessage3 = true;
                    }
                    //toggle water
                    if(!(counter == AIOMVigilanceConfig.waterDuration)) {
                        tick1++;
                        if (tick1 >= 20 && !waterLeverActive) {
                            tick1 = -1;
                        }
                    }

                    //check for gate completion
                    if (orangeOpen) {
                        if(clearLevers()) {
                            currentGate = "";
                            orangeLeverCheck.clear();
                            sendMessage3 = false;
                            counter = 0;
                            Main.sendMarkedChatMessage("Orange has been solved!");

                        }
                    }

                } else {
                    orders.get("Orange").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                }
            }
            else if (currentGate.equals("Blue")) {

                int blueCount = orders.get("Blue").size();
                if (!blueLeverCheck.containsValue(false) && !blueLeverCheck.isEmpty() && blueLeverCheck.size() == blueCount) {
                    if (!sendMessage3) {
                        Main.sendMarkedChatMessage("All levers for blue have been flicked!");
                        sendMessage3 = true;
                    }
                    //toggle water
                    if(!(counter == AIOMVigilanceConfig.waterDuration)) {
                        tick1++;
                        if (tick1 >= 20 && !waterLeverActive) {
                            tick1 = -1;
                        }
                    }

                    //check for gate completion
                    if (blueOpen) {
                        if(clearLevers()) {
                            currentGate = "";
                            blueLeverCheck.clear();
                            sendMessage3 = false;
                            counter = 0;
                            Main.sendMarkedChatMessage("Blue has been solved!");

                        }
                    }

                } else {
                    orders.get("Blue").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                }
            }

            else if (currentGate.equals("Green")) {

                int greenCount = orders.get("Green").size();
                if (!greenLeverCheck.containsValue(false) && !greenLeverCheck.isEmpty() && greenLeverCheck.size() == greenCount) {
                    if (!sendMessage3) {
                        Main.sendMarkedChatMessage("All levers for green have been flicked!");
                        sendMessage3 = true;
                    }
                    //toggle water
                    if(!(counter == AIOMVigilanceConfig.waterDuration)) {
                        tick1++;
                        if (tick1 >= 20 && !waterLeverActive) {
                            tick1 = -1;
                        }
                    }

                    //check for gate completion
                    if (greenOpen) {
                        if(clearLevers()) {
                            currentGate = "";
                            greenLeverCheck.clear();
                            sendMessage3 = false;
                            counter = 0;
                            Main.sendMarkedChatMessage("Green has been solved!");

                        }
                    }
                } else {
                    orders.get("Green").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                }
            }
            else if (currentGate.equals("Red")) {

                int redCount = orders.get("Red").size();
                if (!redLeverCheck.containsValue(false) && !redLeverCheck.isEmpty() && redLeverCheck.size() == redCount) {
                    if (!sendMessage3) {
                        Main.sendMarkedChatMessage("All levers for red have been flicked!");
                        sendMessage3 = true;
                    }
                    //toggle water
                    if(!(counter == AIOMVigilanceConfig.waterDuration)) {
                        tick1++;
                        if (tick1 >= 20 && !waterLeverActive) {
                            tick1 = -1;
                        }
                    }

                    //check for gate completion
                    if (redOpen) {
                        if(clearLevers()) {
                            currentGate = "";
                            redLeverCheck.clear();
                            sendMessage3 = false;
                            counter = 0;
                            Main.sendMarkedChatMessage("Red has been solved!");

                        }
                    }
                } else {
                    orders.get("Red").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                }
            }

            else if (currentGate.equals("")) {

                if(doRender) {
                    try{
                        if (!purpleOpen) {
                            currentGate = "Purple";
                            purpleLeverCheck.clear();
                            orders.get("Purple").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                        }
                        else if (!orangeOpen) {
                            currentGate = "Orange";
                            orangeLeverCheck.clear();
                            orders.get("Orange").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                        }
                        else if (!blueOpen) {
                            currentGate = "Blue";
                            blueLeverCheck.clear();
                            orders.get("Blue").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                        }
                        else if (!greenOpen) {
                            currentGate = "Green";
                            greenLeverCheck.clear();
                            orders.get("Green").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                        }
                        else if (!redOpen) {
                            currentGate = "Red";
                            redLeverCheck.clear();
                            orders.get("Red").forEach(lever -> this.sendLeverPackets(lever, currentGate));
                        }
                    } catch (NullPointerException ignored) {}
                }
            }
        }
    }

    private boolean clearLevers(){
        if(toggleLever) {

            if(quartzLeverActive){
                Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), quartzLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
            }
            if(diamondLeverActive){
                Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), diamondLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
            }
            if(goldLeverActive){
                Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), goldLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
            }
            if(emeraldLeverActive){
                Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), emeraldLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
            }
            if(coalLeverActive){
                Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), coalLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
            }
            if(clayLeverActive){
                Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), clayLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
            }
            if(!quartzLeverActive && !diamondLeverActive && !goldLeverActive && !emeraldLeverActive && !coalLeverActive && !clayLeverActive) {
                Main.sendMarkedChatMessage("All levers have been reset!");
                return true;
            } else return false;
        }
        return false;
    }
    private void sendLeverPackets(String lever, String gate) {
        if (toggleLever) {
            switch (lever) {
                case "None":
                    switch (gate) {
                        case "Purple":
                            purpleLeverCheck.put("None", true);
                            break;
                        case "Orange":
                            orangeLeverCheck.put("None", true);
                            break;
                        case "Blue":
                            blueLeverCheck.put("None", true);
                            break;
                        case "Green":
                            greenLeverCheck.put("None", true);
                            break;
                        case "Red":
                            redLeverCheck.put("None", true);
                            break;
                    }
                    break;

                case "Quartz":
                    if (!quartzLeverActive) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), quartzLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }
                    switch (gate) {
                        case "Purple":
                            if (!purpleLeverCheck.containsKey("Quartz") && quartzLeverActive) {
                                purpleLeverCheck.put("Quartz", quartzLeverActive);
                            }
                            break;
                        case "Orange":
                            if (!orangeLeverCheck.containsKey("Quartz") && quartzLeverActive) {
                                orangeLeverCheck.put("Quartz", quartzLeverActive);
                            }
                            break;
                        case "Blue":
                            if (!blueLeverCheck.containsKey("Quartz") && quartzLeverActive) {
                                blueLeverCheck.put("Quartz", quartzLeverActive);
                            }
                            break;
                        case "Green":
                            if (!greenLeverCheck.containsKey("Quartz") && quartzLeverActive) {
                                greenLeverCheck.put("Quartz", quartzLeverActive);
                            }
                            break;
                        case "Red":
                            if (!redLeverCheck.containsKey("Quartz") && quartzLeverActive) {
                                redLeverCheck.put("Quartz", quartzLeverActive);
                            }
                            break;
                    }

                    break;
                case "Diamond":
                    if (!diamondLeverActive) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), diamondLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }
                    switch (gate) {
                        case "Purple":
                            if (!purpleLeverCheck.containsKey("Diamond") && diamondLeverActive) {
                                purpleLeverCheck.put("Diamond", diamondLeverActive);
                            }
                            break;
                        case "Orange":
                            if (!orangeLeverCheck.containsKey("Diamond") && diamondLeverActive) {
                                orangeLeverCheck.put("Diamond", diamondLeverActive);
                            }
                            break;
                        case "Blue":
                            if (!blueLeverCheck.containsKey("Diamond") && diamondLeverActive) {
                                blueLeverCheck.put("Diamond", diamondLeverActive);
                            }
                            break;
                        case "Green":
                            if (!greenLeverCheck.containsKey("Diamond") && diamondLeverActive) {
                                greenLeverCheck.put("Diamond", diamondLeverActive);
                            }
                            break;
                        case "Red":
                            if (!redLeverCheck.containsKey("Diamond") && diamondLeverActive) {
                                redLeverCheck.put("Diamond", diamondLeverActive);
                            }
                            break;
                    }

                    break;
                case "Gold":
                    if (!goldLeverActive) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), goldLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }
                    switch (gate) {
                        case "Purple":
                            if (!purpleLeverCheck.containsKey("Gold") && goldLeverActive) {
                                purpleLeverCheck.put("Gold", goldLeverActive);
                            }
                            break;
                        case "Orange":
                            if (!orangeLeverCheck.containsKey("Gold") && goldLeverActive) {
                                orangeLeverCheck.put("Gold", goldLeverActive);
                            }
                            break;
                        case "Blue":
                            if (!blueLeverCheck.containsKey("Gold") && goldLeverActive) {
                                blueLeverCheck.put("Gold", goldLeverActive);
                            }
                            break;
                        case "Green":
                            if (!greenLeverCheck.containsKey("Gold") && goldLeverActive) {
                                greenLeverCheck.put("Gold", goldLeverActive);
                            }
                            break;
                        case "Red":
                            if (!redLeverCheck.containsKey("Gold") && goldLeverActive) {
                                redLeverCheck.put("Gold", goldLeverActive);
                            }
                            break;
                    }

                    break;
                case "Emerald":
                    if (!emeraldLeverActive) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), emeraldLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }
                    switch (gate) {
                        case "Purple":
                            if (!purpleLeverCheck.containsKey("Emerald") && emeraldLeverActive) {
                                purpleLeverCheck.put("Emerald", emeraldLeverActive);
                            }
                            break;
                        case "Orange":
                            if (!orangeLeverCheck.containsKey("Emerald") && emeraldLeverActive) {
                                orangeLeverCheck.put("Emerald", emeraldLeverActive);
                            }
                            break;
                        case "Blue":
                            if (!blueLeverCheck.containsKey("Emerald") && emeraldLeverActive) {
                                blueLeverCheck.put("Emerald", emeraldLeverActive);
                            }
                            break;
                        case "Green":
                            if (!greenLeverCheck.containsKey("Emerald") && emeraldLeverActive) {
                                greenLeverCheck.put("Emerald", emeraldLeverActive);
                            }
                            break;
                        case "Red":
                            if (!redLeverCheck.containsKey("Emerald") && emeraldLeverActive) {
                                redLeverCheck.put("Emerald", emeraldLeverActive);
                            }
                            break;
                    }

                    break;
                case "Coal":
                    if (!coalLeverActive) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), coalLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }
                    switch (gate) {
                        case "Purple":
                            if (!purpleLeverCheck.containsKey("Coal") && coalLeverActive) {
                                purpleLeverCheck.put("Coal", coalLeverActive);
                            }
                            break;
                        case "Orange":
                            if (!orangeLeverCheck.containsKey("Coal") && coalLeverActive) {
                                orangeLeverCheck.put("Coal", coalLeverActive);
                            }
                            break;
                        case "Blue":
                            if (!blueLeverCheck.containsKey("Coal") && coalLeverActive) {
                                blueLeverCheck.put("Coal", coalLeverActive);
                            }
                            break;
                        case "Green":
                            if (!greenLeverCheck.containsKey("Coal") && coalLeverActive) {
                                greenLeverCheck.put("Coal", coalLeverActive);
                            }
                            break;
                        case "Red":
                            if (!redLeverCheck.containsKey("Coal") && coalLeverActive) {
                                redLeverCheck.put("Coal", coalLeverActive);
                            }
                            break;
                    }

                    break;
                case "Clay":
                    if (!clayLeverActive) {
                        Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), clayLever, EnumFacing.fromAngle(Main.mcPlayer.rotationYaw), new Vec3(0.0D, 0.0D, 0.00D));
                    }
                    switch (gate) {
                        case "Purple":
                            if (!purpleLeverCheck.containsKey("Clay") && clayLeverActive) {
                                purpleLeverCheck.put("Clay", clayLeverActive);
                            }
                            break;
                        case "Orange":
                            if (!orangeLeverCheck.containsKey("Clay") && clayLeverActive) {
                                orangeLeverCheck.put("Clay", clayLeverActive);
                            }
                            break;
                        case "Blue":
                            if (!blueLeverCheck.containsKey("Clay") && clayLeverActive) {
                                blueLeverCheck.put("Clay", clayLeverActive);
                            }
                            break;
                        case "Green":
                            if (!greenLeverCheck.containsKey("Clay") && clayLeverActive) {
                                greenLeverCheck.put("Clay", clayLeverActive);
                            }
                            break;
                        case "Red":
                            if (!redLeverCheck.containsKey("Clay") && clayLeverActive) {
                                redLeverCheck.put("Clay", clayLeverActive);
                            }
                            break;
                    }

                    break;
            }
        }
    }
}
