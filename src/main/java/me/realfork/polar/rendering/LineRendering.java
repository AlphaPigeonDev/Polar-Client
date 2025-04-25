package me.jacktym.aiomacro.rendering;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LineRendering {

    public static Vec3 lastPos;
    public static HashMap<Integer, Boolean> glCapMap = new HashMap<>();
    public static HashMap<Vec3, Vec3> currentRenderPoints = new HashMap<>();

    public static boolean recording = false;

    public static Vec3 origin = null;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent e) {
        if (Main.notNull && AIOMVigilanceConfig.renderingEnabled && recording) {
            if (origin != null) {
                if (lastPos != null) {
                    if (Utils.distanceBetweenPoints(lastPos, Main.mcPlayer.getPositionVector()) >= 1F / AIOMVigilanceConfig.pointsPerBlock) {
                        System.out.println(origin.subtract(lastPos) + " " + origin.subtract(Main.mcPlayer.getPositionVector()));
                        currentRenderPoints.put(lastPos, Main.mcPlayer.getPositionVector());
                        lastPos = Main.mcPlayer.getPositionVector();
                    }
                } else {
                    lastPos = Main.mcPlayer.getPositionVector();
                }
            } else {
                origin = Main.mcPlayer.getPositionVector();
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent e) {
        if (Main.notNull && AIOMVigilanceConfig.renderingEnabled && origin != null) {
            for (Map.Entry<Vec3, Vec3> entry : currentRenderPoints.entrySet()) {

                Color color = Color.GREEN;

                RenderManager renderManager = Main.mc.getRenderManager();
                double xBp = entry.getValue().add(origin).xCoord - renderManager.viewerPosX;
                double yBp = entry.getValue().add(origin).yCoord - renderManager.viewerPosY;
                double zBp = entry.getValue().add(origin).zCoord - renderManager.viewerPosZ;
                double x = entry.getKey().add(origin).xCoord - renderManager.viewerPosX;
                double y = entry.getKey().add(origin).yCoord - renderManager.viewerPosY;
                double z = entry.getKey().add(origin).zCoord - renderManager.viewerPosZ;
                int width = 5;
                GL11.glBlendFunc(770, 771);

                glCapMap.put(3042, GL11.glGetBoolean(3042));
                GL11.glEnable(3042);

                glCapMap.put(3553, GL11.glGetBoolean(3553));
                GL11.glDisable(3553);
                glCapMap.put(2929, GL11.glGetBoolean(2929));
                GL11.glDisable(2929);

                GL11.glDepthMask(false);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() != 255 ? color.getAlpha() / 255.0f : 26 / 255.0f);
                GL11.glLineWidth(width);
                glCapMap.put(2848, GL11.glGetBoolean(2848));
                GL11.glEnable(2848);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() != 255 ? color.getAlpha() : 26);

                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(3, DefaultVertexFormats.POSITION);

                worldrenderer.pos(xBp, yBp, zBp).endVertex();
                worldrenderer.pos(x, y, z).endVertex();
                tessellator.draw();

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                for (Map.Entry<Integer, Boolean> set : glCapMap.entrySet()) {
                    if (set.getValue()) {
                        GL11.glEnable(set.getKey());
                    } else {
                        GL11.glDisable(set.getKey());
                    }
                }
            }
        }
    }
}
