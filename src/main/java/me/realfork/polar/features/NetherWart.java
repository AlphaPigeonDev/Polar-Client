package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class NetherWart {

    public static boolean left = true;
    public static boolean forward = false;

    private static long currentTimeMillisSetSpawn = Utils.currentTimeMillis();

    private static long currentTimeMillisSwitch = Utils.currentTimeMillis();

    @SubscribeEvent
    public void farmRow(@NotNull TickEvent.ClientTickEvent event) {
        if (MacroHandler.isMacroOn && MacroHandler.isNetherWart) {
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), left);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), !left);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), forward);
            if (Main.mcPlayer.motionX <= 0.001 && Main.mcPlayer.motionX >= -0.001 && Main.mcPlayer.motionZ <= 0.001 && Main.mcPlayer.motionZ >= -0.001) {
                int randomDelay = AIOMVigilanceConfig.INSTANCE.getRandomDelay();
                if (AIOMVigilanceConfig.setSpawnFailsafe && Utils.currentTimeMillis() - currentTimeMillisSetSpawn >= randomDelay) {
                    Main.mcPlayer.sendChatMessage("/setspawn");
                    currentTimeMillisSetSpawn = Utils.currentTimeMillis();
                }
                if (Utils.currentTimeMillis() - currentTimeMillisSwitch >= randomDelay) {
                    forward = !forward;
                    left = !left;
                    currentTimeMillisSwitch = Utils.currentTimeMillis();
                }
                if (Utils.currentTimeMillis() - currentTimeMillisSwitch <= (randomDelay) && Utils.currentTimeMillis() - currentTimeMillisSwitch >= 200 && !AntiStuck.antistuckOn && AIOMVigilanceConfig.antiStuckFailsafe) {
                    AntiStuck.antistuckOn = true;
                }
            }
            SetPlayerLook.flip180Check();
        }
    }
}