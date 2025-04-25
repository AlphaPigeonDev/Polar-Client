package me.jacktym.aiomacro.features;


import gg.essential.universal.UResolution;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.NGGlobal;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class AHSearchExtension {

    public static String nameSearch = "";
    public static String loreSearch = "";

    public static int stars = 0;

    public static boolean nameSelected = false;
    public static boolean loreSelected = false;

    public static boolean auctionHouseOpen = false;

    private static boolean controlDown = false;

    private static int cornerX = 0;
    private static int cornerY = 0;

    @SubscribeEvent
    public void renderGUIScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof GuiChest && AIOMVigilanceConfig.ahSearchExtension) {
            IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

            String chestName = chest.getDisplayName().getUnformattedText();

            auctionHouseOpen = AIOMVigilanceConfig.ahSearchExtension && (chestName.equals("Auctions Browser") || chestName.contains("Auctions: "));

            if (chestName.equals("Auctions Browser") || chestName.contains("Auctions: ")) {
                auctionHouseOpen = true;

                FontRenderer fontRendererObj = Main.mc.fontRendererObj;

                boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
                boolean light = GL11.glGetBoolean(GL11.GL_LIGHTING);

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glColor4f(1, 1, 1, 1f);

                int textureX = 250;
                int textureY = 124;

                cornerX = (UResolution.getScaledWidth() - 176) / 2 - textureX - 30;
                cornerY = (((UResolution.getScaledHeight() - 166) / 2) + ((chest.getSizeInventory() / 9 - 2) * 18 + 17) / 2);

                Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/AHExtension.png"));
                Gui.drawModalRectWithCustomSizedTexture(cornerX, cornerY, 0, 0, textureX, textureY, 250, 124);

                for (int i = 1; i <= 10; i++) {
                    if (stars < i) {
                        Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/DarkenedStarAHExtension.png"));

                        int xPos = 0;

                        switch (i) {
                            case 1:
                                xPos = cornerX + 71;
                                break;
                            case 2:
                                xPos = cornerX + 85;
                                break;
                            case 3:
                                xPos = cornerX + 100;
                                break;
                            case 4:
                                xPos = cornerX + 114;
                                break;
                            case 5:
                                xPos = cornerX + 129;
                                break;
                            case 6:
                                xPos = cornerX + 143;
                                break;
                            case 7:
                                xPos = cornerX + 157;
                                break;
                            case 8:
                                xPos = cornerX + 172;
                                break;
                            case 9:
                                xPos = cornerX + 186;
                                break;
                            case 10:
                                xPos = cornerX + 201;
                                break;
                        }

                        Gui.drawModalRectWithCustomSizedTexture(xPos, cornerY+24, 0, 0, 15, 15, 15, 15);
                    }
                }

                fontRendererObj.drawString(nameSearch, cornerX+74, cornerY+58, 0xFFFFFF, true);
                fontRendererObj.drawString(loreSearch, cornerX+74, cornerY+88, 0xFFFFFF, true);

                if (!blend) {
                    GL11.glDisable(GL11.GL_BLEND);
                }
                if (light) {
                    GL11.glEnable(GL11.GL_LIGHTING);
                }
            }
        } else {
            auctionHouseOpen = false;
        }
    }

    public static void mouseClick(int mouseX, int mouseY) {
        if (mouseY > cornerY + 26 && mouseY < cornerY + 36) {
            if (mouseX > cornerX + 71 && mouseX < cornerX + 81) {
                if (stars == 1) {
                    stars = 0;
                } else {
                    stars = 1;
                }
            } else if (mouseX > cornerX + 86 && mouseX < cornerX + 98) {
                if (stars == 2) {
                    stars = 0;
                } else {
                    stars = 2;
                }
            } else if (mouseX > cornerX + 100 && mouseX < cornerX + 112) {
                if (stars == 3) {
                    stars = 0;
                } else {
                    stars = 3;
                }
            } else if (mouseX > cornerX + 114 && mouseX < cornerX + 126) {
                if (stars == 4) {
                    stars = 0;
                } else {
                    stars = 4;
                }
            } else if (mouseX > cornerX + 129 && mouseX < cornerX + 141) {
                if (stars == 5) {
                    stars = 0;
                } else {
                    stars = 5;
                }
            } else if (mouseX > cornerX + 144 && mouseX < cornerX + 155) {
                if (stars == 6) {
                    stars = 0;
                } else {
                    stars = 6;
                }
            } else if (mouseX > cornerX + 158 && mouseX < cornerX + 170) {
                if (stars == 7) {
                    stars = 0;
                } else {
                    stars = 7;
                }
            } else if (mouseX > cornerX + 172 && mouseX < cornerX + 186) {
                if (stars == 8) {
                    stars = 0;
                } else {
                    stars = 8;
                }
            } else if (mouseX > cornerX + 186 && mouseX < cornerX + 198) {
                if (stars == 9) {
                    stars = 0;
                } else {
                    stars = 9;
                }
            } else if (mouseX > cornerX + 201 && mouseX < cornerX + 214) {
                if (stars == 10) {
                    stars = 0;
                } else {
                    stars = 10;
                }
            }
        }

        if (mouseX > cornerX + 72 && mouseX < cornerX + 215 && mouseY > cornerY + 55 && mouseY < cornerY + 69) {
            nameSelected = true;
            return;
        }
        nameSelected = false;

        if (mouseX > cornerX + 72 && mouseX < cornerX + 215 && mouseY > cornerY + 85 && mouseY < cornerY + 100) {
            loreSelected = true;
            return;
        }
        loreSelected = false;
    }

    public static void keyTyped(char typedChar, int keyCode) {
        if (nameSelected || loreSelected) {
            if (keyCode == 14) {
                try {
                    if (nameSelected) {
                        nameSearch = nameSearch.replaceAll(".$", "");
                    } else {
                        loreSearch = loreSearch.replaceAll(".$", "");
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!controlDown) {
                if (nameSelected) {
                    if (keyCode == 57) {
                        nameSearch = nameSearch + " ";
                    }
                    nameSearch = nameSearch + String.valueOf(typedChar).replaceAll("[^/da-zA-Z]", "");
                } else {
                    if (keyCode == 57) {
                        loreSearch = loreSearch + " ";
                    }
                    loreSearch = loreSearch + String.valueOf(typedChar).replaceAll("[^/da-zA-Z]", "");
                }
            } else {
                if (String.valueOf(typedChar).equals("v")) {
                    try {
                        String clipboardData = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                        if (nameSelected) {
                            nameSearch = nameSearch + String.valueOf(clipboardData).replaceAll("[^/da-zA-Z]", "");
                        } else {
                            loreSearch = loreSearch + String.valueOf(clipboardData).replaceAll("[^/da-zA-Z]", "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        controlDown = keyCode == 29;
    }
}
