package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Scatha {

    public static int mainTicks = 0;

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent e) {
        if (Main.notNull && AIOMVigilanceConfig.macroType == 7 && MacroHandler.isMacroOn) {
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

            BlockPos playerPos = new BlockPos(Main.mcPlayer.getPositionVector());

            SetPlayerLook.setDefault();
            SetPlayerLook.pitch = 15;
            SetPlayerLook.toggled = true;

            switch (AIOMVigilanceConfig.yaw) {
                case 0:
                    if (Main.mcWorld.getBlockState(playerPos.add(0, 0, 2)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(0, 0, 2)), EnumFacing.SOUTH));
                    } else if (Main.mcWorld.getBlockState(playerPos.add(0, 1, 2)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(0, 1, 2)), EnumFacing.SOUTH));
                    }
                    break;
                case 90:
                    if (Main.mcWorld.getBlockState(playerPos.add(-2, 0, 0)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(-2, 0, 0)), EnumFacing.SOUTH));
                    } else if (Main.mcWorld.getBlockState(playerPos.add(-2, 1, 0)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(-2, 1, 0)), EnumFacing.SOUTH));
                    }
                    break;
                case 180:
                    if (Main.mcWorld.getBlockState(playerPos.add(0, 0, -2)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(0, 0, -2)), EnumFacing.SOUTH));
                    } else if (Main.mcWorld.getBlockState(playerPos.add(0, 1, -2)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(0, 1, -2)), EnumFacing.SOUTH));
                    }
                    break;
                case -90:
                    if (Main.mcWorld.getBlockState(playerPos.add(2, 0, 0)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(2, 0, 0)), EnumFacing.SOUTH));
                    } else if (Main.mcWorld.getBlockState(playerPos.add(2, 1, 0)).getBlock() != Blocks.air) {
                        Main.mcPlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(playerPos.add(2, 1, 0)), EnumFacing.SOUTH));
                    }
                    break;
            }
        }
    }
}
