package me.jacktym.aiomacro.features;

import com.google.common.collect.EvictingQueue;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TPS {

    @SubscribeEvent
    public void onClientConnectedToServer(PlayerEvent.PlayerLoggedInEvent event) {
        serverTPS.clear();
        systemTime1 = 0;
        systemTime2 = 0;
        serverTime = 0;
    }

    public static EvictingQueue<Float> serverTPS = EvictingQueue.create(3);
    private static long systemTime1 = 0;
    private static long systemTime2 = 0;
    private static long serverTime = 0;

    public static void onServerTick(S03PacketTimeUpdate packet) {
        if (systemTime1 == 0) {
            systemTime1 = System.currentTimeMillis();
            serverTime = packet.getTotalWorldTime();
        } else {
            long newSystemTime = System.currentTimeMillis();
            long newServerTime = packet.getTotalWorldTime();
            serverTPS.add((((float) (newServerTime - serverTime)) / (((float) (newSystemTime - systemTime1)) / 50.0f)) * 20.0f);
            systemTime1 = newSystemTime;
            serverTime = newServerTime;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            systemTime2 = System.currentTimeMillis();
        }
    }

    public static float calculateServerTPS() {
        float sum = 0.0f;
        for (Float f : serverTPS) {
            sum += f;
        }
        float returnValue = sum / (float) serverTPS.size();

        if (returnValue > 20.0F) {
            returnValue = 20.0F;
        }
        if (returnValue < 0.0F) {
            returnValue = 0.0F;
        }

        return returnValue;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (Main.notNull && AIOMVigilanceConfig.tpsViewer) {
            FontRenderer fontRendererObj = Main.mc.fontRendererObj;

            fontRendererObj.drawString(String.format("TPS: %.1f", calculateServerTPS()), AIOMVigilanceConfig.tpsViewerX, AIOMVigilanceConfig.tpsViewerY, AIOMVigilanceConfig.tpsViewerColor.getRGB());
        }
    }
}
