package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoF7Callouts {
    public static boolean sentPhase1Message = false;
    public static boolean sentPhase2Message = false;
    public static boolean sentPhase3Message = false;
    public static boolean sentPhase4Message = false;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void ClientChatReceived(ClientChatReceivedEvent e) {
        String strippedMessage = Utils.stripColor(e.message.getUnformattedText());

        if (strippedMessage.contains("WELL WELL WELL LOOK WHO’S HERE")) {
            if (!sentPhase1Message && !AIOMVigilanceConfig.autoF7CalloutPhase1.equals("")) {
                Main.mcPlayer.sendChatMessage(AIOMVigilanceConfig.autoF7CalloutPhase1);
                sentPhase1Message = true;
            }
        } else if (strippedMessage.contains("Pathetic Maxor, just like expected")) {
            if (!sentPhase2Message && !AIOMVigilanceConfig.autoF7CalloutPhase2.equals("")) {
                Main.mcPlayer.sendChatMessage(AIOMVigilanceConfig.autoF7CalloutPhase2);
                sentPhase2Message = true;
            }
        } else if (strippedMessage.contains("At least my son died by your hands")) {
            if (!sentPhase3Message && !AIOMVigilanceConfig.autoF7CalloutPhase3.equals("")) {
                Main.mcPlayer.sendChatMessage(AIOMVigilanceConfig.autoF7CalloutPhase3);
                sentPhase3Message = true;
            }
        } else if (strippedMessage.contains("Necron, forgive me")) {
            if (!sentPhase4Message && !AIOMVigilanceConfig.autoF7CalloutPhase4.equals("")) {
                Main.mcPlayer.sendChatMessage(AIOMVigilanceConfig.autoF7CalloutPhase4);
                sentPhase4Message = true;
            }
        } else if (strippedMessage.contains("Here, I found this map when I first entered the dungeon.")) {
            sentPhase1Message = false;
            sentPhase2Message = false;
            sentPhase3Message = false;
            sentPhase4Message = false;
        }
    }
}
