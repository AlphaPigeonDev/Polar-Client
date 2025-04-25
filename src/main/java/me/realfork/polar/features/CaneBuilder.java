package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class CaneBuilder {

    public static long phaseStartMillis = 0;

    public static long phaseMillis = 0;

    public static boolean caneBuilderOn = false;

    public static int phase = 0;

    public static double startX = 0;

    public static double startY = 0;

    public static double startZ = 0;

    public static boolean west = false;

    public static int ticks = 0;

    public static boolean left = true;

    public static int blocks = 0;

    @SubscribeEvent
    public void caneBuilder(@NotNull TickEvent.ClientTickEvent event) {
        if (caneBuilderOn) {
            BlockPos bp = new BlockPos(Main.mcPlayer.posX, Main.mcPlayer.posY - 1, Main.mcPlayer.posZ);
            //Phase to set correct pitch and yaw values to prepare for placing the first row of dirt
            if (phase == 0) {
                //Checks if x and y values are equal to 79
                if (Main.mcPlayer.posX >= 79.5 && Main.mcPlayer.posZ >= 79.5) {
                    //Sneaks and sets pitch and yaw for proper placement
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                    SetPlayerLook.yaw = -90;
                    SetPlayerLook.pitch = 80;
                    SetPlayerLook.toggled = true;
                    //If pitch and yaw are correct
                    if (SetPlayerLook.isLookCorrect()) {
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 1;
                    }
                } else {
                    Main.sendMarkedChatMessage("Incorrect X and Z values. Required: 79.5,79.5. Given: " + Main.mcPlayer.posX + "," + Main.mcPlayer.posZ);
                }
                //Phase to begin placing the first row of dirt, places from (80,y,80) to (0,y,80)
            } else if (phase == 1) {
                //Checks if player has reached the end of the row
                if (Main.mcPlayer.posX >= -79.5) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                    //Changes the current item to the first slot (Dirt Wand)
                    Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                    Main.mcPlayer.inventory.currentItem = 0;
                    //Checks if player has reached the end of the block
                    if ((int) ((Main.mcPlayer.posX - bp.getX()) * 10) == 7) {
                        //Places a block
                        Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                    }
                }
                if (Main.mcPlayer.posX <= -79.5) {
                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 2;
                }
            } else if (phase == 2) {
                //Checks if player has reached the end of the row
                if (Main.mcPlayer.posZ >= -79.5) {
                    SetPlayerLook.yaw = 0;
                    SetPlayerLook.pitch = 80;
                    SetPlayerLook.toggled = true;

                    if (SetPlayerLook.isLookCorrect()) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        //Changes the current item to the first slot (Builder's Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
                        Main.mcPlayer.inventory.currentItem = 1;
                        //Checks if player has reached the end of the block
                        if (Math.abs((int) ((Main.mcPlayer.posZ - bp.getZ()) * 10)) == 7) {
                            //Places a block
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                        }
                    }
                    startY = Main.mcPlayer.posY;
                } else {
                    //Looks down at the block under you
                    SetPlayerLook.yaw = 0;
                    SetPlayerLook.pitch = 90;
                    SetPlayerLook.toggled = true;

                    if (SetPlayerLook.isLookCorrect()) {
                        //Jumps and places a block under you
                        if (Main.mcPlayer.posY == startY) {
                            Main.mcPlayer.jump();
                        }
                        //If the player is high enough to place a block, place it
                        if (Main.mcPlayer.posY >= startY + 1.2) {
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                            phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                            if (phaseMillis < 60000) {
                                int seconds = (int) phaseMillis / 1000;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                            } else {
                                int minutes = (int) Math.floor(phaseMillis / 60000D);
                                int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                            }
                            phaseStartMillis = Utils.currentTimeMillis();
                            phase = 3;
                        }
                    }
                }
            } else if (phase == 3) {
                //Sets pitch and yaw for proper placement
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 80;
                SetPlayerLook.toggled = true;
                //If pitch and yaw are correct
                if (SetPlayerLook.isLookCorrect()) {
                    if (Main.mcPlayer.posX <= 79.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        //Changes the current item to the first slot (Dirt Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                        Main.mcPlayer.inventory.currentItem = 0;

                        //Checks if player has reached the end of the block
                        if (Math.abs((int) (((bp.getX() + 1) - Main.mcPlayer.posX) * 10)) == 7) {
                            //Places a block
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                        }
                    } else {
                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 4;
                    }
                }
            } else if (phase == 4) {
                //Checks if player has reached the end of the row
                if (Main.mcPlayer.posZ <= 79.5) {
                    SetPlayerLook.yaw = 180;
                    SetPlayerLook.pitch = 80;
                    SetPlayerLook.toggled = true;

                    if (SetPlayerLook.isLookCorrect()) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        //Changes the current item to the Second slot (Builder's Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
                        Main.mcPlayer.inventory.currentItem = 1;
                        //Checks if player has reached the end of the block
                        if (Math.abs((int) (((bp.getZ() + 1) - Main.mcPlayer.posZ) * 10)) == 7) {
                            //Places a block
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                        }
                    }
                }
                if (Main.mcPlayer.posZ >= 79.5) {
                    startY = Main.mcPlayer.posY;

                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 5;
                }
            } else if (phase == 5) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                //Changes the current item to the third slot (Shovel)
                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(2));
                Main.mcPlayer.inventory.currentItem = 2;

                if (SetPlayerLook.isLookCorrect()) {

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);

                    if (Main.mcPlayer.posX >= 77.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    } else if (Main.mcPlayer.posZ >= 78.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    } else if (Main.mcPlayer.posX <= 77.5 && Main.mcPlayer.posZ <= 78.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);

                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bp, EnumFacing.DOWN));

                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                        startY = Main.mcPlayer.posY;

                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 6;
                    }
                }
            } else if (phase == 6) {
                if (west) {
                    SetPlayerLook.yaw = 90;
                } else {
                    SetPlayerLook.yaw = -90;
                }
                SetPlayerLook.pitch = 40;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {

                    BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();

                    Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));

                    phase = 7;
                }
            } else if (phase == 7) {
                if (west) {
                    SetPlayerLook.yaw = 90;
                } else {
                    SetPlayerLook.yaw = -90;
                }
                SetPlayerLook.pitch = 15;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {

                    if ((west && Main.mcPlayer.posX >= -77.3) || (!west && Main.mcPlayer.posX <= 77.3)) {

                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                        if ((west && Main.mcPlayer.posX <= -76) || (!west && Main.mcPlayer.posX >= 76)) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        }

                        BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();

                        if (lookingAt.getX() != 78 && lookingAt.getX() != 79 && lookingAt.getX() != -79 && lookingAt.getX() != -80 && lookingAt.getY() == bp.getY() + 1 && Math.abs(lookingAt.getX() - bp.getX()) <= 4) {
                            if (west) {
                                Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));
                            } else {
                                Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.EAST));
                            }
                        }
                    } else {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                        startZ = bp.getZ();
                        startY = Main.mcPlayer.posY;

                        if (bp.getZ() <= -77) {
                            phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                            if (phaseMillis < 60000) {
                                int seconds = (int) phaseMillis / 1000;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                            } else {
                                int minutes = (int) Math.floor(phaseMillis / 60000D);
                                int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                                Main.sendMarkedChatMessage("Finished Phase 8-10 In " + minutes + " Minutes and " + seconds + " Seconds");
                            }
                            phaseStartMillis = Utils.currentTimeMillis();
                            phase = 9;
                        } else {
                            phase = 8;
                        }
                    }
                }
            } else if (phase == 8) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);

                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }

                    if (Main.mcPlayer.posZ <= startZ - 2.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bp, EnumFacing.DOWN));

                        west = !west;

                        phase = 6;
                    }
                }
            } else if (phase == 9) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);

                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }

                    if (Main.mcPlayer.posZ <= -79.5 && Main.mcPlayer.posX <= -79.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 10;
                    }
                }
            }
            if (phase == 10) {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);

                if (Main.mcPlayer.posX >= -77.9) {
                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 11;

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                }
            } else if (phase == 11) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 40;
                SetPlayerLook.toggled = true;

                //Changes the current item to the fourth slot (Prismapump)
                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(3));
                Main.mcPlayer.inventory.currentItem = 3;

                if (SetPlayerLook.isLookCorrect()) {
                    //Places a block
                    Main.mc.playerController.onPlayerRightClick(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem(), new BlockPos(bp.getX(), bp.getY(), bp.getZ() + 2), Main.mc.objectMouseOver.sideHit, Main.mc.objectMouseOver.hitVec);

                    phase = 12;
                }
            } else if (phase == 12) {
                SetPlayerLook.yaw = -30;
                SetPlayerLook.pitch = 40;
                SetPlayerLook.toggled = true;

                //Changes the current item to the fifth slot (Magical Water Bucket)
                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(4));
                Main.mcPlayer.inventory.currentItem = 4;

                if (SetPlayerLook.isLookCorrect()) {
                    //Uses the water
                    Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());

                    phase = 13;
                }
            } else if (phase == 13) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 40;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {

                    //Changes the current item to the sixth slot (Pickaxe)
                    Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(5));
                    Main.mcPlayer.inventory.currentItem = 5;

                    //Breaks the prismapump
                    Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(bp.getX(), bp.getY(), bp.getZ() + 2), EnumFacing.DOWN));

                    if (ticks >= 20) {

                        //Changes the current item to the fifth slot (Magical Water Bucket)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(4));
                        Main.mcPlayer.inventory.currentItem = 4;

                        //Uses the water
                        Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());

                        ticks = 0;

                        startZ = bp.getZ();

                        if (bp.getZ() >= 76) {
                            phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                            if (phaseMillis < 60000) {
                                int seconds = (int) phaseMillis / 1000;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                            } else {
                                int minutes = (int) Math.floor(phaseMillis / 60000D);
                                int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                            }
                            phaseStartMillis = Utils.currentTimeMillis();
                            phase = 15;
                        } else {
                            phase = 14;
                        }
                    }

                    ticks++;
                }
            } else if (phase == 14) {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);

                if (Main.mcPlayer.posZ >= startZ + 3.5) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                    phase = 11;
                }
            } else if (phase == 15) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);

                    if (Main.mcPlayer.posX <= -79.5 && Main.mcPlayer.posZ >= 79.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                        startY = Main.mcPlayer.posY;

                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 16;
                    }
                }
            } else if (phase == 16) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {

                    //Jumps and places a block under you
                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }
                    //If the player is high enough to place a block, place it
                    if (Main.mcPlayer.posY >= startY + 1.2) {
                        //Changes the current item to the first slot (Infi-Dirt Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                        Main.mcPlayer.inventory.currentItem = 0;

                        Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 17;
                    }


                }
            } else if (phase == 17) {
                //Checks if player has reached the end of the row
                if (Main.mcPlayer.posX <= 79.5) {
                    SetPlayerLook.yaw = 90;
                    SetPlayerLook.pitch = 80;
                    SetPlayerLook.toggled = true;

                    if (SetPlayerLook.isLookCorrect()) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        //Changes the current item to the first slot (Dirt Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                        Main.mcPlayer.inventory.currentItem = 0;

                        //Checks if player has reached the end of the block
                        if (Math.abs((int) (((bp.getX() + 1) - Main.mcPlayer.posX) * 10)) == 7) {
                            //Places a block
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                        }
                    }
                } else {
                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 18;
                }
            } else if (phase == 18) {
                //Checks if player has reached the end of the row
                if (Main.mcPlayer.posZ >= -79.5) {
                    SetPlayerLook.yaw = 0;
                    SetPlayerLook.pitch = 80;
                    SetPlayerLook.toggled = true;

                    if (SetPlayerLook.isLookCorrect()) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        //Changes the current item to the first slot (Builder's Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
                        Main.mcPlayer.inventory.currentItem = 1;

                        //Checks if player has reached the end of the block
                        if (Math.abs((int) (((bp.getZ()) - Main.mcPlayer.posZ) * 10)) == 7) {
                            //Places a block
                            Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());
                        }
                    }
                } else {
                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 19;
                }
            } else if (phase == 19) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);

                    if (Main.mcPlayer.posZ >= -78.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);

                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                        if (Main.mcPlayer.posX <= 78.5) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);

                            //Changes the current item to the third slot (Shovel)
                            Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(2));
                            Main.mcPlayer.inventory.currentItem = 2;

                            Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bp, EnumFacing.DOWN));

                            phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                            if (phaseMillis < 60000) {
                                int seconds = (int) phaseMillis / 1000;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                            } else {
                                int minutes = (int) Math.floor(phaseMillis / 60000D);
                                int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                            }
                            phaseStartMillis = Utils.currentTimeMillis();
                            phase = 20;
                        }
                    }
                }
            } else if (phase == 20) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 40;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {

                    BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();

                    Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));

                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 21;
                }
            } else if (phase == 21) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 15;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    if (west && Main.mcPlayer.posX >= -78.5) {

                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                        BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();

                        if (lookingAt.getX() != 79 && lookingAt.getX() != -80 && lookingAt.getY() == bp.getY() + 1 && Math.abs(lookingAt.getX() - bp.getX()) <= 4) {
                            Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));
                        }
                    } else {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                        left = true;

                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        phase = 22;
                    }
                }
            } else if (phase == 22) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 20;
                SetPlayerLook.toggled = true;

                //Changes the current item to the third slot (Shovel)
                Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(2));
                Main.mcPlayer.inventory.currentItem = 2;

                if (SetPlayerLook.isLookCorrect()) {
                    BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();

                    if (lookingAt.getZ() == bp.getZ() + 2) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));
                    }
                    lookingAt = Main.mc.objectMouseOver.getBlockPos();

                    if (lookingAt.getZ() == bp.getZ() + 3) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));
                    }

                    if (left) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);

                        if (Main.mcPlayer.posX >= 78.65) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                            phase = 23;
                        }
                    } else {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);

                        if (Main.mcPlayer.posX <= -78.65) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                            phase = 23;
                        }
                    }
                }
            } else if (phase == 23) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 70;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {

                    startZ = Main.mcPlayer.posZ;

                    BlockPos lookingAt = Main.mc.objectMouseOver.getBlockPos();

                    if (lookingAt.getZ() - 1 == bp.getZ() && lookingAt.getY() - 1 == bp.getY()) {

                        //Changes the current item to the third slot (Shovel)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(2));
                        Main.mcPlayer.inventory.currentItem = 2;

                        //Breaks the block
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, lookingAt, EnumFacing.WEST));

                        phase = 24;
                    }
                }
            } else if (phase == 24) {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                if (Main.mcPlayer.posZ - startZ >= 2.9) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                    left = !left;
                    phase = 22;
                }
                if (Main.mcPlayer.posZ >= 79.5) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);

                    phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                    if (phaseMillis < 60000) {
                        int seconds = (int) phaseMillis / 1000;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                    } else {
                        int minutes = (int) Math.floor(phaseMillis / 60000D);
                        int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                        Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + minutes + " Minutes and " + seconds + " Seconds");
                    }
                    phaseStartMillis = Utils.currentTimeMillis();
                    phase = 25;
                }
            } else if (phase == 25) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (Main.mcPlayer.posX <= -78.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        west = false;
                        phase = 26;
                    }
                }
            } else if (phase == 26) {
                if (west) {
                    SetPlayerLook.yaw = 90;
                } else {
                    SetPlayerLook.yaw = -90;
                }
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                CropAura.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if ((Main.mcPlayer.posX <= -78.5 && west) || (Main.mcPlayer.posX >= 78.5 && !west)) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        west = !west;
                        startZ = Main.mcPlayer.posZ;
                        phase = 27;
                    }
                }
            } else if (phase == 27) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);
                    if (Main.mcPlayer.posZ - startZ <= -2.9) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        phase = 26;
                    }
                    if (Main.mcPlayer.posZ <= -78.5) {
                        phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                        if (phaseMillis < 60000) {
                            int seconds = (int) phaseMillis / 1000;
                            Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                        } else {
                            int minutes = (int) Math.floor(phaseMillis / 60000D);
                            int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                            Main.sendMarkedChatMessage("Finished Phase 25-27 In " + minutes + " Minutes and " + seconds + " Seconds");
                        }
                        phaseStartMillis = Utils.currentTimeMillis();
                        startY = Main.mcPlayer.posY;
                        phase = 28;
                    }
                }
            } else if (phase == 28) {
                if (AIOMVigilanceConfig.caneBuilderLayers == 1) {
                    CaneBuilder.phase = 0;

                    CaneBuilder.caneBuilderOn = false;

                    SetPlayerLook.toggled = false;

                    CropAura.toggled = false;
                } else {
                    SetPlayerLook.yaw = 0;
                    SetPlayerLook.pitch = 0;
                    SetPlayerLook.toggled = true;

                    if (SetPlayerLook.isLookCorrect()) {
                        if (Main.mcPlayer.posX >= 79.5) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);
                        } else {
                            //Jumps
                            if (Main.mcPlayer.posY == startY) {
                                Main.mcPlayer.jump();
                            }
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
                        }
                        if (Main.mcPlayer.posZ >= 79.5) {
                            phaseMillis = Utils.currentTimeMillis() - phaseStartMillis;

                            if (phaseMillis < 60000) {
                                int seconds = (int) phaseMillis / 1000;
                                Main.sendMarkedChatMessage("Finished Phase " + phase + " In " + seconds + " Seconds");
                            } else {
                                int minutes = (int) Math.floor(phaseMillis / 60000D);
                                int seconds = (int) (phaseMillis / 1000) - 60 * minutes;
                                Main.sendMarkedChatMessage("Finished Phase 25-27 In " + minutes + " Minutes and " + seconds + " Seconds");
                            }
                            phaseStartMillis = Utils.currentTimeMillis();
                            startY = Main.mcPlayer.posY;
                            phase = 29;
                        }
                    }
                }
            } else if (phase == 29) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 90;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    //Jumps and places a block under you
                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }
                    //If the player is high enough to place a block, place it
                    if (Main.mcPlayer.posY >= startY + 1.2) {
                        //Changes the current item to the first slot (Infi-Dirt Wand)
                        Main.mcPlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
                        Main.mcPlayer.inventory.currentItem = 0;

                        Main.mc.playerController.sendUseItem(Main.mcPlayer, Main.mcWorld, Main.mcPlayer.inventory.getCurrentItem());

                        startY = Main.mcPlayer.posY;

                        blocks++;
                    }
                }
                if (blocks == 3) {
                    phase = 0;
                }
            }
        }
    }
}

