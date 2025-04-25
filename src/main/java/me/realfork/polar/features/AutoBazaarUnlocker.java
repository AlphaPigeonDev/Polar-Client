package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class AutoBazaarUnlocker {

    public static boolean autoWheatOn = false;

    public static int phase = 0;

    public static double startY = 0;

    public static boolean showBlockPos = false;

    private static int tick = 0;

    private static int command = 0;

    int privateLobbyTick = 0;

    @SubscribeEvent
    public void autoWheat(@NotNull TickEvent.ClientTickEvent event) {
        if (autoWheatOn && Main.mcPlayer != null && Main.mcWorld != null) {
            BlockPos bp = new BlockPos(Main.mcPlayer.posX, Main.mcPlayer.posY - 1, Main.mcPlayer.posZ);

            if (showBlockPos) {
                Main.sendChatMessage(bp + "");
            }


            if (phase == 0) {
                if (bp.equals(new BlockPos(-3, 69, -70))) {
                    phase = 1;
                }
            } else if (phase == 1) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(-3, 69, -91))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);

                        phase = 2;
                    }
                }
            } else if (phase == 2) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(20, 69, -91))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);

                        phase = 3;
                    }
                }
            } else if (phase == 3) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(20, 68, -121))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);

                        phase = 4;
                    }
                }
            } else if (phase == 4) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(43, 69, -121))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);

                        phase = 7;
                    }
                }
            } else if (phase == 7) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(43, 69, -127))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        startY = Main.mcPlayer.posY;
                        phase = 8;
                    }
                }
            } else if (phase == 8) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(45, 69, -127))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        startY = Main.mcPlayer.posY;
                        phase = 9;
                    }
                }
            } else if (phase == 9) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                    if (bp.equals(new BlockPos(45, 69, -130))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 10;
                    }
                }
            } else if (phase == 10) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(45, 69, -129))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 11;
                    }
                }
            } else if (phase == 11) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(51, 69, -129))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        startY = Main.mcPlayer.posY;
                        phase = 12;
                    }
                }
            } else if (phase == 12) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                    if (bp.equals(new BlockPos(52, 70, -129))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 13;
                    }
                }
            } else if (phase == 13) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(64, 70, -129))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        startY = Main.mcPlayer.posY;
                        phase = 14;
                    }
                }
            } else if (phase == 14) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(64, 70, -134))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 15;
                    }
                }
            } else if (phase == 15) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(64, 70, -150))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        startY = Main.mcPlayer.posY;
                        phase = 16;
                    }
                }
            } else if (phase == 16) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }

                    if (bp.equals(new BlockPos(64, 71, -151))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 17;
                    }
                }
            } else if (phase == 17) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(64, 71, -157))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 18;
                    }
                }
            } else if (phase == 18) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(72, 71, -157))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 19;
                    }
                }
            } else if (phase == 19) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(72, 71, -161))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 20;
                    }
                }
            } else if (phase == 20) {
                SetPlayerLook.yaw = 180;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(72, 71, -182))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 21;
                    }
                }
            } else if (phase == 21) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(59, 71, -182))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 22;
                    }
                }
            } else if (phase == 22) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(26, 70, -182))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 23;
                    }
                }
            } else if (phase == 23) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(26, 70, -169))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        startY = Main.mcPlayer.posY;
                        phase = 24;
                    }
                }
            } else if (phase == 24) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (Main.mcPlayer.posY == startY) {
                        Main.mcPlayer.jump();
                    }

                    if (bp.equals(new BlockPos(26, 71, -168))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 25;
                    }
                }
            } else if (phase == 25) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(26, 71, -159))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 26;
                    }
                }
            } else if (phase == 26) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(29, 71, -159))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 27;
                    }
                }
            } else if (phase == 27) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(29, 69, -142))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 28;
                    }
                }
            } else if (phase == 28) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(23, 69, -142))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 29;
                    }
                }
            } else if (phase == 29) {
                SetPlayerLook.yaw = -90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(29, 69, -142))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 30;
                    }
                }
            } else if (phase == 30) {
                SetPlayerLook.yaw = 0;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(29, 69, -130))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        phase = 31;
                    }
                }
            } else if (phase == 31) {
                SetPlayerLook.yaw = 90;
                SetPlayerLook.pitch = 0;
                SetPlayerLook.toggled = true;

                if (SetPlayerLook.isLookCorrect()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), true);

                    if (bp.equals(new BlockPos(22, 69, -130))) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                        SetPlayerLook.toggled = false;

                        if (AIOMVigilanceConfig.autoWheatPrivate) {
                            Main.mcPlayer.sendChatMessage("/hub");
                        }
                        phase = 32;
                    }
                }
            } else if (phase == 32) {
                if (AIOMVigilanceConfig.autoWheatPrivate) {
                    Utils.openNpc("Hub Selector");
                } else {
                    if (tick >= (100)) {
                        command++;
                        if (command == 1) {
                            Main.mcPlayer.sendChatMessage("/is");
                        } else if (command == 2) {
                            Main.mcPlayer.sendChatMessage("/hub");
                        } else if (command == 4) {
                            command = 0;
                            phase = 0;
                        }
                        tick = 0;
                    }

                    tick++;
                }
            }
        }
    }

    @SubscribeEvent
    public void privateLobbySwitcher(@NotNull GuiScreenEvent event) {
        if (event.gui instanceof GuiChest && AIOMVigilanceConfig.autoWheatPrivate && autoWheatOn) {
            if (privateLobbyTick >= 20) {
                IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

                String chestName = chest.getDisplayName().getUnformattedText();

                if (Utils.stripColor(chestName).equals("SkyBlock Hub Selector") && ((GuiChest) event.gui).inventorySlots.inventorySlots.get(50).getStack() != null && ((GuiChest) event.gui).inventorySlots.inventorySlots.get(50).getStack().getItem() == Items.compass) {
                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 50, 1, 0, Main.mcPlayer);
                }
            }
            privateLobbyTick++;
        }
    }
}
