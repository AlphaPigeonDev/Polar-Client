package me.jacktym.aiomacro.gui;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class TPSViewerGui extends GuiScreen {

    public TPSViewerGui() {
        super();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        super.drawCenteredString(Main.mc.fontRendererObj, "Editing HUD Position", Main.mc.displayWidth / 4, Main.mc.displayHeight / 50, Color.BLACK.getRGB());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        AIOMVigilanceConfig.tpsViewerX = mouseX;
        AIOMVigilanceConfig.tpsViewerY = mouseY;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        AIOMVigilanceConfig.tpsViewerX = mouseX;
        AIOMVigilanceConfig.tpsViewerY = mouseY;
    }
}
