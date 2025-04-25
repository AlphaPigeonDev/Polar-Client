package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class OneRow {

    public static boolean forwards = true;
    public static Vec3 endRow = null;

    private static long currentTimeMillisSetSpawn = Utils.currentTimeMillis();

    private static long currentTimeMillisSwitch = Utils.currentTimeMillis();

    @SubscribeEvent
    public void farmRow(@NotNull TickEvent.ClientTickEvent event) {
        if (MacroHandler.isMacroOn && MacroHandler.isOneRow) {
            if (endRow != null) {
                System.out.println(Math.abs(endRow.xCoord - Main.mcPlayer.getPositionVector().xCoord));
                if (Math.abs(endRow.xCoord - Main.mcPlayer.getPositionVector().xCoord) >= 0.8f
                || Math.abs(endRow.zCoord - Main.mcPlayer.getPositionVector().zCoord) >= 0.8f) {
                    endRow = null;
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
                    forwards = !forwards;
                } else {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), forwards);
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), !forwards);
                if (Main.mcPlayer.motionX <= 0.001 && Main.mcPlayer.motionX >= -0.001 && Main.mcPlayer.motionZ <= 0.001 && Main.mcPlayer.motionZ >= -0.001 && System.currentTimeMillis() - MacroHandler.macroStartMillis >= 5000) {
                    int randomDelay = AIOMVigilanceConfig.INSTANCE.getRandomDelay();
                    if (AIOMVigilanceConfig.setSpawnFailsafe && Utils.currentTimeMillis() - currentTimeMillisSetSpawn >= randomDelay) {
                        Main.mcPlayer.sendChatMessage("/setspawn");
                        currentTimeMillisSetSpawn = Utils.currentTimeMillis();
                    }
                    System.out.println(Utils.currentTimeMillis() - currentTimeMillisSwitch <= randomDelay);
                    if (Utils.currentTimeMillis() - currentTimeMillisSwitch >= randomDelay) {
                        endRow = Main.mcPlayer.getPositionVector();
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
                        currentTimeMillisSwitch = Utils.currentTimeMillis();
                    }
                    if (Utils.currentTimeMillis() - currentTimeMillisSwitch <= (randomDelay) && Utils.currentTimeMillis() - currentTimeMillisSwitch >= 200 && !AntiStuck.antistuckOn && AIOMVigilanceConfig.antiStuckFailsafe) {
                        AntiStuck.antistuckOn = true;
                    }
                }
            }
        }
    }
}
