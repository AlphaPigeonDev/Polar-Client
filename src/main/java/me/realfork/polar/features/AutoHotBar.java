package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class AutoHotBar {

    public static HashMap<String, List<String>> hotBars = new HashMap<>();
    public static boolean swapHotBar = false;
    public static HashMap<Integer, Integer> hotBarSolution;
    public static int hotBarTicks = 0;

    public static void loadHotBars() {
        hotBars.clear();
        if (!AIOMVigilanceConfig.hotBarData.equals("")) {
            String hotBarData = AIOMVigilanceConfig.hotBarData.replace("{", "").replace("}", "");
            List<String> hotBarList = new ArrayList<>();
            if (hotBarData.contains("],")) {
                Collections.addAll(hotBarList, hotBarData.split("], "));
            } else {
                hotBarList.add(hotBarData.replace("]", ""));
            }

            for (String hotBar : hotBarList) {
                List<String> hotBarNames = new ArrayList<>(Arrays.asList(hotBar.split("=\\[")[1].split(", ")));
                hotBars.put(hotBar.split("=")[0], hotBarNames);
            }
        }
    }

    public static void saveHotBar(String hotBarName) {
        if (!hotBarName.equals("")) {
            List<String> hotBarNames = new ArrayList<>();
            List<ItemStack> hotBarItems = new ArrayList<>();
            Collections.addAll(hotBarItems, Main.mcPlayer.inventory.mainInventory);
            hotBarItems = hotBarItems.subList(0, 9);
            for (ItemStack stack : hotBarItems) {
                try {
                    hotBarNames.add(stack.getDisplayName());
                } catch (NullPointerException e) {
                    hotBarNames.add("Nothing");
                }
            }
            hotBars.put(hotBarName, hotBarNames);
            AIOMVigilanceConfig.hotBarData = hotBars.toString();
        } else {
            Main.sendMarkedChatMessage("Failed to save your hotBar. Please enter a name.");
        }
    }

    public static HashMap<Integer, Integer> findHotBarSolution(List<String> hotBarItems) {
        HashMap<Integer, Integer> solution = new HashMap<>();
        boolean foundItem;
        for (int x = 0; x <= hotBarItems.size() - 1; x++) {
            foundItem = false;
            for (int i = 0; i <= Main.mcPlayer.inventory.mainInventory.length - 1; i++) {
                try {
                    if (hotBarItems.get(x).equals("Nothing")) {
                        foundItem = true;
                        break;
                    }
                    if (!solution.containsKey(i)
                            && Main.mcPlayer.inventory.mainInventory[i].getDisplayName().equals(hotBarItems.get(x))) {
                        if (i != x) {
                            solution.put(i, x);
                        }
                        foundItem = true;
                        break;
                    }
                } catch (NullPointerException ignored) {
                }
            }
            if (!foundItem) {
                Main.sendMarkedChatMessage("Error getting HotBar item: " + hotBarItems.get(x));
            }
        }
        return solution;
    }

    @SubscribeEvent
    public void hotBarTick(TickEvent.ClientTickEvent event) {
        if (swapHotBar) {
            if (hotBarTicks >= 10) {
                hotBarTicks = 0;
                if (hotBarSolution != null) {
                    if (Main.mc.currentScreen != null && Main.mc.currentScreen.toString().contains("client.gui.inventory")) {
                        for (Map.Entry<Integer, Integer> entry : hotBarSolution.entrySet()) {
                            if (entry.getKey() >= 9) {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, entry.getKey(), entry.getValue(), 2, Main.mcPlayer);
                            } else {
                                Main.mc.playerController.windowClick(Main.mcPlayer.openContainer.windowId, entry.getKey() + 36, entry.getValue(), 2, Main.mcPlayer);
                            }
                            hotBarSolution.remove(entry.getKey());
                            return;
                        }
                        Main.mcPlayer.closeScreen();
                        swapHotBar = false;
                        Main.sendMarkedChatMessage("HotBar Loaded!");
                    } else {
                        Main.mc.displayGuiScreen(new GuiInventory(Main.mcPlayer));
                    }
                } else {
                    Main.sendMarkedChatMessage("Error swapping HotBar, no solution found");
                    swapHotBar = false;
                }
            }
            hotBarTicks++;
        }
    }
}
