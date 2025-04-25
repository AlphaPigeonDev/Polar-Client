package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;

public class FarmingHUD {

    public static int getInventoryPrice() {
        ItemStack[] inventory = Main.mcPlayer.inventory.mainInventory;

        int inventoryPrice = 0;

        for (ItemStack itemStack : inventory) {
            if (itemStack != null) {
                String displayName = Utils.stripColor(itemStack.getDisplayName());

                switch (displayName) {
                    case "Wheat":
                    case "Cactus":
                    case "Potato":
                    case "Carrot":
                        inventoryPrice += itemStack.stackSize;
                        break;
                    case "Hay Bale":
                        inventoryPrice += 9 * itemStack.stackSize;
                        break;
                    case "Enchanted Hay Bale":
                        inventoryPrice += 1300 * itemStack.stackSize;
                        break;
                    case "Tightly-Tied Hay Bale":
                        inventoryPrice += 187200 * itemStack.stackSize;
                        break;
                    case "Pumpkin":
                    case "Brown Mushroom Block":
                    case "Brown Mushroom":
                    case "Red Mushroom Block":
                    case "Red Mushroom":
                        inventoryPrice += 4 * itemStack.stackSize;
                        break;
                    case "Enchanted Pumpkin":
                        inventoryPrice += 640 * itemStack.stackSize;
                        break;
                    case "Polished Pumpkin":
                        inventoryPrice += 102400 * itemStack.stackSize;
                        break;
                    case "Melon":
                        inventoryPrice += 0.5 * itemStack.stackSize;
                        break;
                    case "Enchanted Melon":
                    case "Enchanted Cactus Green":
                    case "Enchanted Potato":
                    case "Enchanted Carrot":
                        inventoryPrice += 160 * itemStack.stackSize;
                        break;
                    case "Enchanted Melon Block":
                    case "Enchanted Cactus":
                    case "Enchanted Baked Potato":
                        inventoryPrice += 25600 * itemStack.stackSize;
                        break;
                    case "Enchanted Golden Carrot":
                        inventoryPrice += 20608 * itemStack.stackSize;
                        break;
                    case "Sugar Cane":
                    case "Nether Wart":
                        inventoryPrice += 2 * itemStack.stackSize;
                        break;
                    case "Enchanted Sugar":
                    case "Enchanted Nether Wart":
                        inventoryPrice += 320 * itemStack.stackSize;
                        break;
                    case "Enchanted Sugar Cane":
                    case "Mutant Nether Wart":
                        inventoryPrice += 51200 * itemStack.stackSize;
                        break;
                    case "Enchanted Red Mushroom Block":
                    case "Enchanted Brown Mushroom Block":
                        inventoryPrice += 20736 * itemStack.stackSize;
                        break;
                    case "Cocoa Beans":
                        inventoryPrice += 3 * itemStack.stackSize;
                        break;
                    case "Enchanted Cocoa Bean":
                        inventoryPrice += 480 * itemStack.stackSize;
                        break;
                    case "Enchanted Cookie":
                        inventoryPrice += 61500 * itemStack.stackSize;
                        break;
                }
            }
        }
        return inventoryPrice;
    }

    public static int getCropsPerHour() {
        ItemStack[] inventory = Main.mcPlayer.inventory.mainInventory;

        int crops = 0;

        for (ItemStack itemStack : inventory) {
            if (itemStack != null) {

                String displayName = Utils.stripColor(itemStack.getDisplayName());

                switch (displayName) {
                    case "Wheat":
                    case "Cactus":
                    case "Potato":
                    case "Carrot":
                    case "Pumpkin":
                    case "Brown Mushroom":
                    case "Red Mushroom":
                    case "Melon":
                    case "Sugar Cane":
                    case "Nether Wart":
                    case "Cocoa Beans":
                        crops += itemStack.stackSize;
                        break;
                    case "Enchanted Cactus Green":
                    case "Enchanted Potato":
                    case "Enchanted Carrot":
                    case "Enchanted Pumpkin":
                    case "Enchanted Brown Mushroom":
                    case "Enchanted Red Mushroom":
                    case "Enchanted Melon":
                    case "Enchanted Sugar":
                    case "Enchanted Nether Wart":
                    case "Enchanted Cocoa Beans":
                        crops += 32 * 5 * itemStack.stackSize;
                        break;
                    case "Hay Bale":
                        crops += 9 * itemStack.stackSize;
                        break;
                    case "Tightly-Tied Hay Bale":
                    case "Enchanted Cookie":
                    case "Enchanted Red Mushroom Block":
                    case "Enchanted Brown Mushroom Block":
                    case "Enchanted Sugar Cane":
                    case "Enchanted Golden Carrot":
                    case "Enchanted Melon Block":
                    case "Enchanted Baked Potato":
                    case "Enchanted Cactus":
                    case "Polished Pumpkin":
                    case "Mutant Nether Wart":
                        crops += 32 * 5 * 32 * 5 * itemStack.stackSize;
                        break;
                }
            }
        }
        long hours = 1 + ((Utils.currentTimeMillis() - MacroHandler.macroStartMillis) / 60000);
        return (int) (crops / hours);
    }

    public static int getFinalCrops() {
        ItemStack[] inventory = Main.mcPlayer.inventory.mainInventory;

        int finalCrops = 0;

        for (ItemStack itemStack : inventory) {

            if (itemStack != null) {
                String displayName = Utils.stripColor(itemStack.getDisplayName());
                switch (displayName) {
                    case "Tightly-Tied Hay Bale":
                    case "Enchanted Cookie":
                    case "Enchanted Red Mushroom Block":
                    case "Enchanted Brown Mushroom Block":
                    case "Enchanted Sugar Cane":
                    case "Enchanted Golden Carrot":
                    case "Enchanted Melon Block":
                    case "Enchanted Baked Potato":
                    case "Enchanted Cactus":
                    case "Polished Pumpkin":
                    case "Mutant Nether Wart":
                        finalCrops += itemStack.stackSize;
                        break;
                }
            }
        }
        return finalCrops;
    }

    public static int getProfitPerHour() {
        int profit = getInventoryPrice();

        int profitPerHour = 0;
        if (MacroHandler.isMacroOn) {

            long millisecondsMacroing = Utils.timeSinceMillis(MacroHandler.macroStartMillis);

            int secondsMacroing = (int) (millisecondsMacroing / 1000);

            if (secondsMacroing != 0) {
                profitPerHour = (3600 / secondsMacroing) * profit;
            }

        }
        return profitPerHour;
    }

    public static int getTimeFarming() {
        long millisecondsMacroing = Utils.timeSinceMillis(MacroHandler.macroStartMillis);

        return (int) (millisecondsMacroing / 1000);
    }

    public static String getHoeCounter() {
        Utils.getCounter();
        return String.valueOf(Utils.getCounter());
    }

    public static String getBoosterCookieTime() {
        String cookieTime = "";
        int cookieTimeIndex = 0;
        int currentIndex = 0;
        for (String footer : Utils.getTabFooters()) {
            if (footer.contains("Cookie Buff")) {
                cookieTimeIndex = currentIndex + 2;
            }
            currentIndex++;
        }

        if (cookieTimeIndex != 0) {
            if (Utils.getTabFooters().get(cookieTimeIndex) != null) {
                cookieTime = Utils.getTabFooters().get(cookieTimeIndex);
            }
        }

        return cookieTime;
    }

    public static String getGodPotionTime() {
        for (String tab : Utils.getTabFooters()) {
            tab = Utils.stripColor(tab);
            if (tab.startsWith("You have a God Potion active!")) {
                return tab.split("You have a God Potion active! ")[1];
            }
        }
        return "";
    }

    public static String getJacobsEventTime() {
        String jacobTime = "";
        int jacobTimeIndex = 0;
        int currentIndex = 0;

        for (String scoreboard : Utils.getScoreboard()) {
            scoreboard = Utils.stripColor(scoreboard);

            if (scoreboard.contains("Jacob's Contes")) {
                jacobTimeIndex = currentIndex - 1;
            }
            currentIndex++;
        }

        if (jacobTimeIndex != 0) {
            jacobTime = Utils.getScoreboard().get(jacobTimeIndex).replaceAll("[^\\x00-\\x7F]", "").substring(1);
        }

        return jacobTime;
    }

    @SubscribeEvent
    public void onRender(@NotNull RenderGameOverlayEvent.Text event) {
        if (AIOMVigilanceConfig.farmingHUDOn) {
            FontRenderer fontRendererObj = Main.mc.fontRendererObj;

            for (int i = 0; i < getHUDList().size(); i++) {
                String draw = getHUDList().get(i);
                fontRendererObj.drawString(draw, AIOMVigilanceConfig.farmingHUDX, AIOMVigilanceConfig.farmingHUDY + (10 * i), AIOMVigilanceConfig.hudColor.getRGB());
            }
        }
    }

    public ArrayList<String> getHUDList() {
        ArrayList<String> hudList = new ArrayList<>();
        if (Main.mcPlayer != null && Main.mcWorld != null) {

            if (AIOMVigilanceConfig.totalProfitHUD) {
                hudList.add("Total Profit: " + getInventoryPrice() + " Coins");
            }
            if (AIOMVigilanceConfig.profitPerHourHUD) {
                hudList.add("Profit Per Hour: " + getProfitPerHour() + " Coins");
            }
            if (AIOMVigilanceConfig.profitPer12HoursHUD) {
                hudList.add("Profit Per 12 Hours: " + getProfitPerHour() * 12 + " Coins");
            }
            if (AIOMVigilanceConfig.profitPer24HoursHUD) {
                hudList.add("Profit Per 24 Hours: " + getProfitPerHour() * 24 + " Coins");
            }
            if (AIOMVigilanceConfig.cropsPerHourHUD) {
                hudList.add("Crops Per Hour: " + getCropsPerHour() + " Crops");
            }
            if (AIOMVigilanceConfig.totalFinalCropHUD) {
                hudList.add("Final Upgraded Crops: " + getFinalCrops() + " Crops");
            }
            if (AIOMVigilanceConfig.hoeCounterHUD) {
                hudList.add("Hoes: " + getHoeCounter());
            }
            if (AIOMVigilanceConfig.godPotionTimeHUD) {
                hudList.add("God Potion Time: " + getGodPotionTime());
            }
            if (AIOMVigilanceConfig.boosterCookieTimeHUD) {
                hudList.add("Booster Cookie Time: " + getBoosterCookieTime());
            }
            if (AIOMVigilanceConfig.jacobsEventHUD) {
                hudList.add("Jacobs Event Time: " + getJacobsEventTime());
            }
            if (AIOMVigilanceConfig.farmingTime) {
                Duration duration = Duration.ofSeconds(getTimeFarming());
                long hours = duration.toHours();
                long minutes = duration.toMinutes() - hours * 60;
                long seconds = (duration.getSeconds() - minutes * 60 - hours * 3600);

                if (getTimeFarming() == 0 || !MacroHandler.isMacroOn) {
                    hudList.add("Total Time Farming: None.");
                } else if (hours == 0 && minutes == 0 && seconds >= 1) {
                    hudList.add("Total Time Farming: " + seconds + "s");
                } else if (hours == 0 && minutes >= 1) {
                    hudList.add("Total Time Farming: " + minutes + "m " + seconds + "s");
                } else if (hours >= 1) {
                    hudList.add("Total Time Farming: " + hours + "h " + minutes + "m " + seconds + "s");
                }
            }
        }
        return hudList;
    }


}
