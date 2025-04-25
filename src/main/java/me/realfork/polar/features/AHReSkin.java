package me.jacktym.aiomacro.features;


import gg.essential.universal.UResolution;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.NGGlobal;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

public class AHReSkin {

    public static String ahSearch = "";
    public static String nameSearch = "";
    public static String loreSearch = "";

    public static int stars = 0;

    public static boolean searchSelected = false;
    public static boolean nameSelected = false;
    public static boolean loreSelected = false;

    public static int categorySelected = 1;

    public static int sortSelected = 1;
    public static int raritySelected = 1;
    public static int binFilterSelected = 2;

    public static boolean auctionHouseOpen = false;

    private static boolean controlDown = false;

    public static int cornerX = 0;
    public static int cornerY = 0;

    public static int currentPage = 1;
    public static int maxPages = 10;

    public static int mouseX = 0;
    public static int mouseY = 0;

    IInventory lastChest = null;

    int ticks = 0;

    @SubscribeEvent
    public void setConfig(TickEvent.ClientTickEvent e) {
        ticks++;
        if (auctionHouseOpen && ticks >= 5 && Main.mc.currentScreen instanceof GuiChest) {
            ticks = 0;
            String currentSearch = "";
            int currentSort = 0;
            int currentRarity = 0;
            int currentFilter = 0;

            try {
                String chestName = lastChest.getDisplayName().getUnformattedText();
                if (chestName.contains("Auctions: ")) {
                    currentSearch = chestName.split("Auctions: \"")[1].split("\"")[0];
                } else {
                    currentSearch = "";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            for (int i = 1; i <= 4; i++) {
                try {
                    if (lastChest.getStackInSlot(50).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(i).toString().contains("▶")) {
                        currentSort = i;
                    }
                } catch (Exception ignored) {
                }
            }

            for (int i = 1; i <= 10; i++) {
                try {
                    if (lastChest.getStackInSlot(51).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(i).toString().contains("▶")) {
                        currentRarity = i;
                    }
                } catch (Exception ignored) {
                }
            }

            for (int i = 1; i <= 3; i++) {
                try {
                    if (lastChest.getStackInSlot(52).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(i).toString().contains("▶")) {
                        currentFilter = i;
                    }
                } catch (Exception ignored) {
                }
            }

            if (!ahSearch.equals(currentSearch) && !searchSelected) {
                if (ahSearch.equals("")) {
                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 48, 1, 0, Main.mcPlayer);
                } else {
                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 48, 0, 0, Main.mcPlayer);
                }
            } else if (currentSort != sortSelected) {
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 50, 0, 0, Main.mcPlayer);
            } else if (currentRarity != raritySelected) {
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 51, 0, 0, Main.mcPlayer);
            } else if (currentFilter != binFilterSelected) {
                if (currentFilter < binFilterSelected) {
                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 52, 0, 0, Main.mcPlayer);
                } else {
                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 52, 1, 0, Main.mcPlayer);
                }
            }
        }
    }


    @SubscribeEvent
    public void renderGUIScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (AIOMVigilanceConfig.ahReSkin) {
            if (event.gui instanceof GuiEditSign && auctionHouseOpen) {
                try {
                    if (ahSearch.equals("")) {
                        Main.mc.currentScreen.onGuiClosed();
                    } else if (BazaarFlipper.currentSign != null) {
                        BazaarFlipper.currentSign.signText[0] = new ChatComponentText(ahSearch);
                        if (BazaarFlipper.currentSign.signText[0].equals(new ChatComponentText(ahSearch))) {
                            Main.mc.currentScreen.onGuiClosed();
                        }
                    }
                } catch (Exception ignored) {
                }
            }
            if (event.gui instanceof GuiChest) {
                IInventory chest = ((ContainerChest) (((GuiChest) event.gui).inventorySlots)).getLowerChestInventory();

                lastChest = chest;

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

                    int textureX = 913;
                    int textureY = 570;

                    cornerX = (UResolution.getScaledWidth() - textureX) / 2;
                    cornerY = (UResolution.getScaledHeight() - textureY) / 2;

                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/AuctionBrowser.png"));
                    Gui.drawModalRectWithCustomSizedTexture(cornerX, cornerY, 0, 0, textureX, textureY, textureX, textureY);

                    for (int i = 1; i <= 6; i++) {
                        if (categorySelected != i) {
                            int xPos = 0;
                            int yPos = 0;
                            int texX = 0;
                            int texY = 0;
                            switch (i) {
                                case 1:
                                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHWeaponsDarkener.png"));
                                    xPos = cornerX + 36;
                                    yPos = cornerY + 74;
                                    texX = 348;
                                    texY = 90;
                                    break;
                                case 2:
                                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHArmorDarkener.png"));
                                    xPos = cornerX + 163;
                                    yPos = cornerY + 72;
                                    texX = 284;
                                    texY = 100;
                                    break;
                                case 3:
                                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHAccessoriesDarkener.png"));
                                    xPos = cornerX + 268;
                                    yPos = cornerY + 73;
                                    texX = 496;
                                    texY = 96;
                                    break;
                                case 4:
                                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHConsumablesDarkener.png"));
                                    xPos = cornerX + 446;
                                    yPos = cornerY + 75;
                                    texX = 491;
                                    texY = 92;
                                    break;
                                case 5:
                                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHBlocksDarkener.png"));
                                    xPos = cornerX + 621;
                                    yPos = cornerY + 73;
                                    texX = 296;
                                    texY = 96;
                                    break;
                                case 6:
                                    Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHToolsMiscDarkener.png"));
                                    xPos = cornerX + 726;
                                    yPos = cornerY + 72;
                                    texX = 469;
                                    texY = 106;
                                    break;
                            }

                            Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, texX / 3, texY / 3, texX / 3f, texY / 3f);
                        }
                    }

                    for (int i = 1; i <= 10; i++) {
                        if (stars < i) {
                            Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHStarDarkener.png"));

                            int xPos = 0;

                            switch (i) {
                                case 1:
                                    xPos = cornerX + 174;
                                    break;
                                case 2:
                                    xPos = cornerX + 202;
                                    break;
                                case 3:
                                    xPos = cornerX + 229;
                                    break;
                                case 4:
                                    xPos = cornerX + 257;
                                    break;
                                case 5:
                                    xPos = cornerX + 285;
                                    break;
                                case 6:
                                    xPos = cornerX + 312;
                                    break;
                                case 7:
                                    xPos = cornerX + 340;
                                    break;
                                case 8:
                                    xPos = cornerX + 367;
                                    break;
                                case 9:
                                    xPos = cornerX + 395;
                                    break;
                                case 10:
                                    xPos = cornerX + 423;
                                    break;
                            }

                            Gui.drawModalRectWithCustomSizedTexture(xPos, cornerY + 412, 0, 0, 25, 25, 25, 25);
                        }
                    }

                    for (int i = 1; i <= 4; i++) {
                        if (sortSelected != i) {
                            Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHSelectorDarkener.png"));

                            int yPos = 0;
                            switch (i) {
                                case 1:
                                    yPos = cornerY + 391;
                                    break;
                                case 2:
                                    yPos = cornerY + 432;
                                    break;
                                case 3:
                                    yPos = cornerY + 472;
                                    break;
                                case 4:
                                    yPos = cornerY + 511;
                                    break;
                            }
                            Gui.drawModalRectWithCustomSizedTexture(cornerX + 462, yPos, 0, 0, 98, 24, 98, 24);
                        }
                    }

                    for (int i = 1; i <= 10; i++) {
                        if (raritySelected != i) {
                            Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHSelectorDarkener.png"));

                            int yPos = 0;
                            int xPos = 0;
                            switch (i) {
                                case 1:
                                    yPos = cornerY + 392;
                                    xPos = cornerX + 571;
                                    break;
                                case 2:
                                    yPos = cornerY + 422;
                                    xPos = cornerX + 571;
                                    break;
                                case 3:
                                    yPos = cornerY + 452;
                                    xPos = cornerX + 571;
                                    break;
                                case 4:
                                    yPos = cornerY + 482;
                                    xPos = cornerX + 571;
                                    break;
                                case 5:
                                    yPos = cornerY + 512;
                                    xPos = cornerX + 571;
                                    break;
                                case 6:
                                    yPos = cornerY + 392;
                                    xPos = cornerX + 670;
                                    break;
                                case 7:
                                    yPos = cornerY + 422;
                                    xPos = cornerX + 670;
                                    break;
                                case 8:
                                    yPos = cornerY + 452;
                                    xPos = cornerX + 670;
                                    break;
                                case 9:
                                    yPos = cornerY + 482;
                                    xPos = cornerX + 670;
                                    break;
                                case 10:
                                    yPos = cornerY + 512;
                                    xPos = cornerX + 670;
                                    break;
                            }
                            Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, 98, 24, 98, 24);
                        }
                    }

                    for (int i = 1; i <= 3; i++) {
                        if (binFilterSelected != i) {
                            Main.mc.getTextureManager().bindTexture(new ResourceLocation(NGGlobal.MOD_ID, "textures/FullAHSelectorDarkener.png"));

                            int yPos = 0;
                            switch (i) {
                                case 1:
                                    yPos = cornerY + 392;
                                    break;
                                case 2:
                                    yPos = cornerY + 452;
                                    break;
                                case 3:
                                    yPos = cornerY + 512;
                                    break;
                            }
                            Gui.drawModalRectWithCustomSizedTexture(cornerX + 779, yPos, 0, 0, 98, 24, 98, 24);
                        }
                    }

                    GL11.glPushMatrix();
                    GL11.glScalef(2.5F, 2.5F, 2.5F);
                    fontRendererObj.drawString(ahSearch, (cornerX + 178) / 2.5f, (cornerY + 369) / 2.5f, 0xFFFFFF, true);
                    fontRendererObj.drawString(nameSearch, (cornerX + 178) / 2.5f, (cornerY + 462) / 2.5f, 0xFFFFFF, true);
                    fontRendererObj.drawString(loreSearch, (cornerX + 178) / 2.5f, (cornerY + 508) / 2.5f, 0xFFFFFF, true);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(2.5F, 2.5F, 2.5F);
                    String lore = "(1/1)";
                    try {
                        if (chest.getStackInSlot(53) != null && chest.getStackInSlot(53).getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                            lore = chest.getStackInSlot(53).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(0).toString();
                        } else if (chest.getStackInSlot(46) != null && chest.getStackInSlot(46).getItem() != Item.getItemFromBlock(Blocks.stained_glass_pane)) {
                            lore = chest.getStackInSlot(46).getTagCompound().getCompoundTag("display").getTagList("Lore", 8).get(0).toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentPage = Integer.parseInt(lore.split("\\(")[1].split("/")[0]);
                    maxPages = Integer.parseInt(lore.split("/")[1].split("\\)")[0]);
                    fontRendererObj.drawString("Page " + currentPage + " / " + maxPages, (cornerX + 42) / 2.5f, (cornerY + 50) / 2.5f, 0xFFFFFF, true);
                    GL11.glPopMatrix();

                    //Drawing Items

                    for (int x = 1; x <= 24; x++) {
                        int index = 10 + x;
                        if (x >= 7) {
                            index = index + 3;
                        }
                        if (x >= 13) {
                            index = index + 3;
                        }
                        if (x >= 19) {
                            index = index + 3;
                        }
                        Slot slot = ((GuiChest) event.gui).inventorySlots.inventorySlots.get(index);
                        drawSlot(slot, x);
                    }

                    //Drawing tooltip

                    ItemStack stack = null;

                    if (mouseY > AHReSkin.cornerY + 145 && mouseY < AHReSkin.cornerY + 185) {
                        //Row 1
                        if (mouseX > AHReSkin.cornerX + 55 && mouseX < AHReSkin.cornerX + 110) {
                            //Column 1
                            stack = chest.getStackInSlot(11);
                        }
                        if (mouseX > AHReSkin.cornerX + 165 && mouseX < AHReSkin.cornerX + 230) {
                            //Column 2
                            stack = chest.getStackInSlot(12);
                        }
                        if (mouseX > AHReSkin.cornerX + 270 && mouseX < AHReSkin.cornerX + 325) {
                            //Column 3
                            stack = chest.getStackInSlot(13);
                        }
                        if (mouseX > AHReSkin.cornerX + 375 && mouseX < AHReSkin.cornerX + 430) {
                            //Column 4
                            stack = chest.getStackInSlot(14);
                        }
                        if (mouseX > AHReSkin.cornerX + 480 && mouseX < AHReSkin.cornerX + 535) {
                            //Column 5
                            stack = chest.getStackInSlot(15);
                        }
                        if (mouseX > AHReSkin.cornerX + 590 && mouseX < AHReSkin.cornerX + 645) {
                            //Column 6
                            stack = chest.getStackInSlot(16);
                        }
                        if (mouseX > AHReSkin.cornerX + 700 && mouseX < AHReSkin.cornerX + 750) {
                            //Column 7
                            stack = chest.getStackInSlot(20);
                        }
                        if (mouseX > AHReSkin.cornerX + 805 && mouseX < AHReSkin.cornerX + 860) {
                            //Column 8
                            stack = chest.getStackInSlot(21);
                        }
                    }

                    if (mouseY > AHReSkin.cornerY + 205 && mouseY < AHReSkin.cornerY + 255) {
                        //Row 2
                        if (mouseX > AHReSkin.cornerX + 55 && mouseX < AHReSkin.cornerX + 110) {
                            //Column 1
                            stack = chest.getStackInSlot(22);
                        }
                        if (mouseX > AHReSkin.cornerX + 165 && mouseX < AHReSkin.cornerX + 230) {
                            //Column 2
                            stack = chest.getStackInSlot(23);
                        }
                        if (mouseX > AHReSkin.cornerX + 270 && mouseX < AHReSkin.cornerX + 325) {
                            //Column 3
                            stack = chest.getStackInSlot(24);
                        }
                        if (mouseX > AHReSkin.cornerX + 375 && mouseX < AHReSkin.cornerX + 430) {
                            //Column 4
                            stack = chest.getStackInSlot(25);
                        }
                        if (mouseX > AHReSkin.cornerX + 480 && mouseX < AHReSkin.cornerX + 535) {
                            //Column 5
                            stack = chest.getStackInSlot(29);
                        }
                        if (mouseX > AHReSkin.cornerX + 590 && mouseX < AHReSkin.cornerX + 645) {
                            //Column 6
                            stack = chest.getStackInSlot(30);
                        }
                        if (mouseX > AHReSkin.cornerX + 700 && mouseX < AHReSkin.cornerX + 750) {
                            //Column 7
                            stack = chest.getStackInSlot(31);
                        }
                        if (mouseX > AHReSkin.cornerX + 805 && mouseX < AHReSkin.cornerX + 860) {
                            //Column 8
                            stack = chest.getStackInSlot(32);
                        }
                    }

                    if (mouseY > AHReSkin.cornerY + 275 && mouseY < AHReSkin.cornerY + 325) {
                        //Row 3
                        if (mouseX > AHReSkin.cornerX + 55 && mouseX < AHReSkin.cornerX + 110) {
                            //Column 1
                            stack = chest.getStackInSlot(33);
                        }
                        if (mouseX > AHReSkin.cornerX + 165 && mouseX < AHReSkin.cornerX + 230) {
                            //Column 2
                            stack = chest.getStackInSlot(34);
                        }
                        if (mouseX > AHReSkin.cornerX + 270 && mouseX < AHReSkin.cornerX + 325) {
                            //Column 3
                            stack = chest.getStackInSlot(38);
                        }
                        if (mouseX > AHReSkin.cornerX + 375 && mouseX < AHReSkin.cornerX + 430) {
                            //Column 4
                            stack = chest.getStackInSlot(39);
                        }
                        if (mouseX > AHReSkin.cornerX + 480 && mouseX < AHReSkin.cornerX + 535) {
                            //Column 5
                            stack = chest.getStackInSlot(40);
                        }
                        if (mouseX > AHReSkin.cornerX + 590 && mouseX < AHReSkin.cornerX + 645) {
                            //Column 6
                            stack = chest.getStackInSlot(41);
                        }
                        if (mouseX > AHReSkin.cornerX + 700 && mouseX < AHReSkin.cornerX + 750) {
                            //Column 7
                            stack = chest.getStackInSlot(42);
                        }
                        if (mouseX > AHReSkin.cornerX + 805 && mouseX < AHReSkin.cornerX + 860) {
                            //Column 8
                            stack = chest.getStackInSlot(43);
                        }
                    }

                    if (stack != null) {
                        List<String> list = stack.getTooltip(Main.mcPlayer, Main.mc.gameSettings.advancedItemTooltips);

                        for (int i = 0; i < list.size(); ++i) {
                            if (i == 0) {
                                list.set(i, stack.getRarity().rarityColor + list.get(i));
                            } else {
                                list.set(i, EnumChatFormatting.GRAY + list.get(i));
                            }
                        }

                        FontRenderer font = stack.getItem().getFontRenderer(stack);
                        GuiUtils.drawHoveringText(list, mouseX, mouseY, event.gui.width, event.gui.height, -1, font == null ? fontRendererObj : font);
                    }

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
    }

    private void drawSlot(Slot slotIn, int slotNum) {
        float i;
        float j;

        if (slotNum <= 8) {
            j = cornerY + 146;
        } else if (slotNum <= 16) {
            j = cornerY + 216;
        } else {
            j = cornerY + 283.5F;
        }

        switch (slotNum) {
            case 1:
            case 9:
            case 17:
                i = cornerX + 67.5F;
                break;
            case 2:
            case 10:
            case 18:
                i = cornerX + 174.5F;
                break;
            case 3:
            case 11:
            case 19:
                i = cornerX + 281.5F;
                break;
            case 4:
            case 12:
            case 20:
                i = cornerX + 388F;
                break;
            case 5:
            case 13:
            case 21:
                i = cornerX + 494.5F;
                break;
            case 6:
            case 14:
            case 22:
                i = cornerX + 601.5F;
                break;
            case 7:
            case 15:
            case 23:
                i = cornerX + 709.5F;
                break;
            case 8:
            case 16:
            case 24:
                i = cornerX + 816.5F;
                break;
            default:
                i = cornerX;
        }


        ItemStack itemstack = slotIn.getStack();

        if (itemstack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);

            renderItemAndEffectIntoGUI(itemstack, i / 2.0F, j / 2.0F);

            if (highlightStack(lastChest.getDisplayName().getUnformattedText(), stars, nameSearch, loreSearch, slotIn)) {
                drawGradientRect(i / 2.0F, j / 2.0F, i / 2.0F + 16, j / 2.0F + 16, 0.2f);
            }
        }

        GlStateManager.popMatrix();
    }

    public static boolean highlightStack(String chestName, int starsAmount, String nameSearch, String loreSearch, Slot slotIn) {
        if ((!nameSearch.equals("") || !loreSearch.equals("") || starsAmount != 0) && chestName != null && (chestName.equals("Auctions Browser") || chestName.contains("Auctions: "))) {
            StringBuilder stars = new StringBuilder();

            for (int i = 1; i <= 5; i++) {
                if (i <= starsAmount) {
                    stars.append("§6✪");
                }
            }
            switch (starsAmount) {
                case 6:
                    stars.append("§c➊");
                    break;
                case 7:
                    stars.append("§c➋");
                    break;
                case 8:
                    stars.append("§c➌");
                    break;
                case 9:
                    stars.append("§c➍");
                    break;
                case 10:
                    stars.append("§c➎");
                    break;
            }

            try {
                String lore = slotIn.getStack().getTagCompound().getCompoundTag("display").getTagList("Lore", 8).toString();

                return slotIn.getStack().getDisplayName().contains(nameSearch)
                        && lore.contains(loreSearch)
                        && (stars.toString().equals("")
                        || (slotIn.getStack().getDisplayName().split(" ").length >= 1
                        && slotIn.getStack().getDisplayName().split(" ")[slotIn.getStack().getDisplayName().split(" ").length - 1].equals(stars.toString())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, float transparency) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.colorMask(true, true, true, false);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(right, top, 100.0F).color(1.0f, 1.0f, 1.0f, transparency).endVertex();
        worldRenderer.pos(left, top, 100.0F).color(1.0f, 1.0f, 1.0f, transparency).endVertex();
        worldRenderer.pos(left, bottom, 100.0F).color(1.0f, 1.0f, 1.0f, transparency).endVertex();
        worldRenderer.pos(right, bottom, 100.0F).color(1.0f, 1.0f, 1.0f, transparency).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();

        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    public void renderItemAndEffectIntoGUI(final ItemStack stack, float xPosition, float yPosition) {
        if (stack != null && stack.getItem() != null) {
            try {
                this.renderItemIntoGUI(stack, xPosition, yPosition);
            } catch (Throwable var7) {
                CrashReport crashreport = CrashReport.makeCrashReport(var7, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addCrashSectionCallable("Item Type", () -> String.valueOf(stack.getItem()));
                crashreportcategory.addCrashSectionCallable("Item Aux", () -> String.valueOf(stack.getMetadata()));
                crashreportcategory.addCrashSectionCallable("Item NBT", () -> String.valueOf(stack.getTagCompound()));
                crashreportcategory.addCrashSectionCallable("Item Foil", () -> String.valueOf(stack.hasEffect()));
                throw new ReportedException(crashreport);
            }
        }

    }

    public void renderItemIntoGUI(ItemStack stack, float x, float y) {
        IBakedModel ibakedmodel = Main.mc.getRenderItem().getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        Main.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Main.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GUI);
        this.renderItem(stack, ibakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        Main.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Main.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(float xPosition, float yPosition, boolean isGui3d) {
        GlStateManager.translate(xPosition, yPosition, 100.0F + 50.0F);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, -1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        if (isGui3d) {
            GlStateManager.scale(40.0F, 40.0F, 40.0F);
            GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableLighting();
        } else {
            GlStateManager.scale(64.0F, 64.0F, 64.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
        }

    }

    public void renderItem(ItemStack stack, IBakedModel model) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            if (model.isBuiltInRenderer()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                TileEntityItemStackRenderer.instance.renderByItem(stack);
            } else {
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
                this.renderModel(model, stack);
                if (stack.hasEffect()) {
                    this.renderEffect(model);
                }
            }

            GlStateManager.popMatrix();
        }

    }

    private void renderModel(IBakedModel model, ItemStack stack) {
        this.renderModel(model, -1, stack);
    }

    private void renderEffect(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        Main.mc.getTextureManager().bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        Main.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }

    private void renderModel(IBakedModel model) {
        this.renderModel(model, -8372020, null);
    }

    private void renderModel(IBakedModel model, int color, ItemStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.ITEM);
        EnumFacing[] var6 = EnumFacing.values();

        for (EnumFacing enumfacing : var6) {
            this.renderQuads(worldrenderer, model.getFaceQuads(enumfacing), color, stack);
        }

        this.renderQuads(worldrenderer, model.getGeneralQuads(), color, stack);
        tessellator.draw();
    }

    private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
        boolean flag = color == -1 && stack != null;
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = quads.get(i);
            int k = color;
            if (flag && bakedquad.hasTintIndex()) {
                k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                k |= -16777216;
            }

            LightUtil.renderQuadColor(renderer, bakedquad, k);
        }

    }

    public static void mouseClick() {
        if (mouseX > cornerX + 882 && mouseX < cornerX + 907 && mouseY > cornerY + 46 && mouseY < cornerY + 70) {
            //X Button
            Main.mc.thePlayer.closeScreen();
        }

        if (mouseY > cornerY + 78 && mouseY < cornerY + 100) {
            //Category
            if (mouseX > cornerX + 40 && mouseX < cornerX + 149) {
                categorySelected = 1;
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 0, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 172 && mouseX < cornerX + 250) {
                categorySelected = 2;
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 9, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 273 && mouseX < cornerX + 429) {
                categorySelected = 3;
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 18, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 450 && mouseX < cornerX + 607) {
                categorySelected = 4;
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 27, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 627 && mouseX < cornerX + 714) {
                categorySelected = 5;
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 36, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 731 && mouseX < cornerX + 875) {
                categorySelected = 6;
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 45, 0, 0, Main.mcPlayer);
            }
        }

        searchSelected = false;
        nameSelected = false;
        loreSelected = false;
        if (mouseX > cornerX + 175 && mouseX < cornerX + 447) {
            //Type Boxes
            if (mouseY > cornerY + 361 && mouseY < cornerY + 397) {
                searchSelected = true;
            }
            if (mouseY > cornerY + 454 && mouseY < cornerY + 487) {
                nameSelected = true;
            }
            if (mouseY > cornerY + 499 && mouseY < cornerY + 534) {
                loreSelected = true;
            }

            if (mouseY > cornerY + 411 && mouseY < cornerY + 437) {
                //Stars
                if (mouseX > cornerX + 174 && mouseX < cornerX + 197) {
                    if (stars == 1) {
                        stars = 0;
                    } else {
                        stars = 1;
                    }
                }
                if (mouseX > cornerX + 202 && mouseX < cornerX + 226) {
                    if (stars == 2) {
                        stars = 0;
                    } else {
                        stars = 2;
                    }
                }
                if (mouseX > cornerX + 230 && mouseX < cornerX + 253) {
                    if (stars == 3) {
                        stars = 0;
                    } else {
                        stars = 3;
                    }
                }
                if (mouseX > cornerX + 257 && mouseX < cornerX + 282) {
                    if (stars == 4) {
                        stars = 0;
                    } else {
                        stars = 4;
                    }
                }
                if (mouseX > cornerX + 285 && mouseX < cornerX + 309) {
                    if (stars == 5) {
                        stars = 0;
                    } else {
                        stars = 5;
                    }
                }
                if (mouseX > cornerX + 312 && mouseX < cornerX + 337) {
                    if (stars == 6) {
                        stars = 0;
                    } else {
                        stars = 6;
                    }
                }
                if (mouseX > cornerX + 339 && mouseX < cornerX + 365) {
                    if (stars == 7) {
                        stars = 0;
                    } else {
                        stars = 7;
                    }
                }
                if (mouseX > cornerX + 367 && mouseX < cornerX + 392) {
                    if (stars == 8) {
                        stars = 0;
                    } else {
                        stars = 8;
                    }
                }
                if (mouseX > cornerX + 396 && mouseX < cornerX + 419) {
                    if (stars == 9) {
                        stars = 0;
                    } else {
                        stars = 9;
                    }
                }
                if (mouseX > cornerX + 423 && mouseX < cornerX + 447) {
                    if (stars == 10) {
                        stars = 0;
                    } else {
                        stars = 10;
                    }
                }
            }
        }

        if (mouseX > cornerX + 464 && mouseX < cornerX + 560) {
            //Sort
            if (mouseY > cornerY + 394 && mouseY < cornerY + 414) {
                sortSelected = 1;
            }
            if (mouseY > cornerY + 434 && mouseY < cornerY + 454) {
                sortSelected = 2;
            }
            if (mouseY > cornerY + 474 && mouseY < cornerY + 494) {
                sortSelected = 3;
            }
            if (mouseY > cornerY + 515 && mouseY < cornerY + 535) {
                sortSelected = 4;
            }
        }

        if (mouseX > cornerX + 572 && mouseX < cornerX + 667) {
            //Rarity 1-5
            if (mouseY > cornerY + 394 && mouseY < cornerY + 414) {
                raritySelected = 1;
            }
            if (mouseY > cornerY + 424 && mouseY < cornerY + 444) {
                raritySelected = 2;
            }
            if (mouseY > cornerY + 454 && mouseY < cornerY + 474) {
                raritySelected = 3;
            }
            if (mouseY > cornerY + 484 && mouseY < cornerY + 504) {
                raritySelected = 4;
            }
            if (mouseY > cornerY + 514 && mouseY < cornerY + 534) {
                raritySelected = 5;
            }
        }

        if (mouseX > cornerX + 672 && mouseX < cornerX + 767) {
            //Rarity 6-4
            if (mouseY > cornerY + 394 && mouseY < cornerY + 414) {
                raritySelected = 6;
            }
            if (mouseY > cornerY + 424 && mouseY < cornerY + 444) {
                raritySelected = 7;
            }
            if (mouseY > cornerY + 454 && mouseY < cornerY + 474) {
                raritySelected = 8;
            }
            if (mouseY > cornerY + 484 && mouseY < cornerY + 504) {
                raritySelected = 9;
            }
            if (mouseY > cornerY + 514 && mouseY < cornerY + 534) {
                raritySelected = 10;
            }
        }

        if (mouseX > cornerX + 778 && mouseX < cornerX + 877) {
            //BIN Filter
            if (mouseY > cornerY + 394 && mouseY < cornerY + 414) {
                binFilterSelected = 1;
            }
            if (mouseY > cornerY + 457 && mouseY < cornerY + 480) {
                binFilterSelected = 2;
            }
            if (mouseY > cornerY + 514 && mouseY < cornerY + 534) {
                binFilterSelected = 3;
            }
        }

        if (mouseY > cornerY + 208 && mouseY < cornerY + 250) {
            //Arrows
            if (mouseX > cornerX + 13 && mouseX < cornerX + 39) {
                //Left Arrow
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 46, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 877 && mouseX < cornerX + 902) {
                //Right Arrow
                if (currentPage < maxPages) {
                    Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 53, 0, 0, Main.mcPlayer);
                }
            }
        }

        if (mouseY > cornerY + 145 && mouseY < cornerY + 185) {
            //Row 1
            if (mouseX > cornerX + 55 && mouseX < cornerX + 110) {
                //Column 1
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 11, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 165 && mouseX < cornerX + 230) {
                //Column 2
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 12, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 270 && mouseX < cornerX + 325) {
                //Column 3
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 13, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 375 && mouseX < cornerX + 430) {
                //Column 4
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 14, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 480 && mouseX < cornerX + 535) {
                //Column 5
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 15, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 590 && mouseX < cornerX + 645) {
                //Column 6
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 16, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 700 && mouseX < cornerX + 750) {
                //Column 7
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 20, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 805 && mouseX < cornerX + 860) {
                //Column 8
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 21, 0, 0, Main.mcPlayer);
            }
        }

        if (mouseY > cornerY + 205 && mouseY < cornerY + 255) {
            //Row 2
            if (mouseX > cornerX + 55 && mouseX < cornerX + 110) {
                //Column 1
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 22, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 165 && mouseX < cornerX + 230) {
                //Column 2
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 23, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 270 && mouseX < cornerX + 325) {
                //Column 3
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 24, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 375 && mouseX < cornerX + 430) {
                //Column 4
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 25, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 480 && mouseX < cornerX + 535) {
                //Column 5
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 29, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 590 && mouseX < cornerX + 645) {
                //Column 6
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 30, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 700 && mouseX < cornerX + 750) {
                //Column 7
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 31, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 805 && mouseX < cornerX + 860) {
                //Column 8
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 32, 0, 0, Main.mcPlayer);
            }
        }

        if (mouseY > cornerY + 275 && mouseY < cornerY + 325) {
            //Row 3
            if (mouseX > cornerX + 55 && mouseX < cornerX + 110) {
                //Column 1
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 33, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 165 && mouseX < cornerX + 230) {
                //Column 2
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 34, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 270 && mouseX < cornerX + 325) {
                //Column 3
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 38, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 375 && mouseX < cornerX + 430) {
                //Column 4
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 39, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 480 && mouseX < cornerX + 535) {
                //Column 5
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 40, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 590 && mouseX < cornerX + 645) {
                //Column 6
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 41, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 700 && mouseX < cornerX + 750) {
                //Column 7
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 42, 0, 0, Main.mcPlayer);
            }
            if (mouseX > cornerX + 805 && mouseX < cornerX + 860) {
                //Column 8
                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, 43, 0, 0, Main.mcPlayer);
            }
        }
    }

    public static void keyTyped(char typedChar, int keyCode) {
        if (nameSelected || loreSelected || searchSelected) {
            if (keyCode == 14) {
                try {
                    if (nameSelected) {
                        nameSearch = nameSearch.replaceAll(".$", "");
                    } else if (loreSelected) {
                        loreSearch = loreSearch.replaceAll(".$", "");
                    } else {
                        ahSearch = ahSearch.replaceAll(".$", "");
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
                } else if (loreSelected) {
                    if (keyCode == 57) {
                        loreSearch = loreSearch + " ";
                    }
                    loreSearch = loreSearch + String.valueOf(typedChar).replaceAll("[^/da-zA-Z]", "");
                } else {
                    if (keyCode == 57) {
                        ahSearch = ahSearch + " ";
                    }
                    ahSearch = ahSearch + String.valueOf(typedChar).replaceAll("[^/da-zA-Z]", "");
                }
            } else {
                if (String.valueOf(typedChar).equals("v")) {
                    try {
                        String clipboardData = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                        if (nameSelected) {
                            nameSearch = nameSearch + String.valueOf(clipboardData).replaceAll("[^/da-zA-Z]", "");
                        } else if (loreSelected) {
                            loreSearch = loreSearch + String.valueOf(clipboardData).replaceAll("[^/da-zA-Z]", "");
                        } else {
                            ahSearch = ahSearch + String.valueOf(clipboardData).replaceAll("[^/da-zA-Z]", "");
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
