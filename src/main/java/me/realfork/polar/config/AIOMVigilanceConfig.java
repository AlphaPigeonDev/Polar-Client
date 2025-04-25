package me.jacktym.aiomacro.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.features.*;
import me.jacktym.aiomacro.gui.HUDLocationGui;
import me.jacktym.aiomacro.gui.TPSViewerGui;
import me.jacktym.aiomacro.rendering.LineRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AIOMVigilanceConfig extends Vigilant {

    public static final File modDir = new File(Minecraft.getMinecraft().mcDataDir, "config/AIO-Macro");
    public static final File configFile = new File(modDir + "/AIO-Macro.toml");

    //Macro Settings
    public static AIOMVigilanceConfig INSTANCE;
    public static boolean awaitShowColourWindow = false;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Macro Type",
            description = "Choose the macro you would like to use.",
            category = "Macro Settings",
            options = {"Netherwart/S-Shaped", "Sugarcane", "Nuker", "Bazaar Flipper", "Fairy Soul Aura", "Shiny Pig ESP", "Minion Aura", "Scatha Macro", "Carpentry Macro", "Auction Flipper", "Cocoa Bean", "One Row"}
    )
    public static int macroType;
    @Property(
            type = PropertyType.SLIDER,
            name = "Pitch",
            description = "Sets the value you are looking up and down (Check f3).",
            category = "Macro Settings",
            min = -90,
            max = 90
    )
    public static int pitch;
    @Property(
            type = PropertyType.SLIDER,
            name = "Yaw",
            description = "Sets the value you are looking left to right (Check f3).",
            category = "Macro Settings",
            min = -180,
            max = 180
    )
    public static int yaw;
    @Property(
            type = PropertyType.SWITCH,
            name = "Auto God Potion",
            description = "Automatically buys and consumes a God Potion. (Requires gold in purse).",
            category = "Macro Settings"
    )
    public static boolean autoGodPotion;
    @Property(
            type = PropertyType.SWITCH,
            name = "Auto Booster Cookie",
            description = "Automatically buys and consumes a Booster Cookie. (Requires gold in purse).",
            category = "Macro Settings"
    )
    public static boolean autoCookie;
    @Property(
            type = PropertyType.SWITCH,
            name = "Fast Break",
            description = "Breaks crops at 40bps to allow for faster rancher boot speeds",
            category = "Macro Settings"
    )
    public static boolean fastBreak;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Fast Break BPS",
            description = "How many blocks per second (20 Ticks) Fast Break breaks.",
            category = "Macro Settings",
            options = {"20bps", "40bps", "60bps", "80bps"}
    )
    public static int fastBreakBPS;
    @Property(
            type = PropertyType.SLIDER,
            name = "Auto Sell Minimum",
            description = "The minimum amount of stacks to automatically sell (Cookie Only, 0 To Disable)",
            category = "Macro Settings",
            max = 10
    )
    public static int autoSellMinimum;
    @Property(
            type = PropertyType.SWITCH,
            name = "Drop 180",
            description = "Does a 180 turn after dropping during a farming macro",
            category = "Macro Settings"
    )
    public static boolean drop180;
    @Property(
            type = PropertyType.SWITCH,
            name = "Ungrab",
            description = "Ungrabs your mouse when using a farming macro",
            category = "Macro Settings"
    )
    public static boolean ungrab;
    //Failsafes
    @Property(
            type = PropertyType.SWITCH,
            name = "Webhook Alerts",
            description = "Sends webhook on failsafe, message, party invite, island visit, etc.",
            category = "Failsafes",
            subcategory = "Webhooks"
    )
    public static boolean webhookAlerts;
    @Property(
            type = PropertyType.SLIDER,
            name = "Auto Webhook",
            description = "How often to automatically send a webhook (0 to disable)",
            category = "Failsafes",
            max = 360
    )
    public static int autoWebhook = 0;
    @Property(
            type = PropertyType.TEXT,
            name = "Webhook Link",
            description = "Link to send webhook alerts to.",
            category = "Failsafes",
            subcategory = "Webhooks"
    )
    public static String webhookLink;
    @Property(
            type = PropertyType.SLIDER,
            name = "Random Delay Minimum",
            description = "The Minimum Random Delay (Milliseconds).",
            category = "Failsafes",
            min = 100,
            max = 5000
    )
    public static int randomDelayMin = 1000;
    @Property(
            type = PropertyType.SLIDER,
            name = "Random Delay Maximum",
            description = "The Maximum Random Delay (Milliseconds).",
            category = "Failsafes",
            min = 100,
            max = 5000
    )
    public static int randomDelayMax = 2000;
    @Property(
            type = PropertyType.SLIDER,
            name = "Auto Disable",
            description = "How long to farm until disabling (0 to disable)",
            category = "Failsafes",
            max = 360
    )
    public static int autoDisable = 0;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Island Failsafe",
            description = "Triggers when not on island.",
            category = "Failsafes",
            options = {"Stop Farming", "Teleport To Island", "Disable"}
    )
    public static int islandfailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "Jacob Failsafe",
            description = "Stops farming during Jacob's contests.",
            category = "Failsafes"
    )
    public static boolean jacobfailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "Bedrock Failsafe",
            description = "Stops farming when looking at bedrock.",
            category = "Failsafes"
    )
    public static boolean bedrockfailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "Set Spawn",
            description = "Sets your island spawn after each row (Used to infinite farm when using teleport to island failsafe).",
            category = "Failsafes"
    )
    public static boolean setSpawnFailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "Play Sound",
            description = "Plays a sound to alert you when a failsafe is triggered.",
            category = "Failsafes"
    )
    public static boolean soundfailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "AntiStuck",
            description = "Detects when a player is stuck and attempts to unstick them.",
            category = "Failsafes"
    )
    public static boolean antiStuckFailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "AntiStuck Jump",
            description = "Includes jumping in AntiStuck motion (helpful in some farms, unhelpful in others).",
            category = "Failsafes"
    )
    public static boolean antiStuckJump;
    @Property(
            type = PropertyType.SWITCH,
            name = "Desync Failsafe",
            description = "Detects when a player desyncs from farming and resyncs.",
            category = "Failsafes"
    )
    public static boolean desyncFailsafe;
    @Property(
            type = PropertyType.SWITCH,
            name = "Banwave Failsafe",
            description = "Detects when a banwave is in place, stops macroing, and leaves SkyBlock.",
            category = "Failsafes"
    )
    public static boolean banwaveFailsafe;
    @Property(
            type = PropertyType.NUMBER,
            name = "Minimum Bans",
            description = "The minimum bans to trigger the banwave failsafe.",
            category = "Failsafes",
            min = 1,
            max = 5
    )
    public static int banWavePlayers = 3;

    //Farming HUD
    @Property(
            type = PropertyType.SWITCH,
            name = "Toggle Farming HUD",
            description = "Toggles showing of the Farming HUD.",
            category = "Farming HUD"
    )
    public static boolean farmingHUDOn;
    @Property(
            type = PropertyType.SLIDER,
            name = "Farming HUD X",
            description = "Sets the X value of farming hud.",
            category = "Farming HUD"
    )
    public static int farmingHUDX = 0;
    @Property(
            type = PropertyType.SLIDER,
            name = "Farming HUD Y",
            description = "Sets the Y value of farming hud.",
            category = "Farming HUD"
    )
    public static int farmingHUDY = 0;
    @Property(
            type = PropertyType.SWITCH,
            name = "Total Profit",
            description = "Displays how much profit you have made farming so far.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean totalProfitHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Profit Per Hour",
            description = "Displays how much profit you make every hour.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean profitPerHourHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Profit Per 12 Hours",
            description = "Displays how much profit you make every 12 hours.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean profitPer12HoursHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Profit Per 24 Hours",
            description = "Displays how much profit you make every 24 hours.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean profitPer24HoursHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Crops Per Hour",
            description = "Displays how many crops you yield in an hour.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean cropsPerHourHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Total Final Crops Yielded",
            description = "Displays how many final crop upgrades you yielded.",
            category = "Farming HUD",
            subcategory = "Statistics"
    )
    public static boolean totalFinalCropHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Mathematical Hoe Counter",
            description = "Displays the counter on your mathematical hoe.",
            category = "Farming HUD",
            subcategory = "Statistics"
    )
    public static boolean hoeCounterHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "God Potion Time",
            description = "Displays time remaining on God Potion.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean godPotionTimeHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Booster Cookie Time",
            description = "Displays time remaining on Booster Cookie.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean boosterCookieTimeHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Jacobs Farming Event Timers",
            description = "Displays time until Jacob's Farming Event Starts/Ends.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean jacobsEventHUD;
    @Property(
            type = PropertyType.SWITCH,
            name = "Total Farming Time",
            description = "Displays total time spent macroing.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean farmingTime;
    @Property(
            type = PropertyType.COLOR,
            name = "HUD Text Color",
            description = "Sets the color for the HUD Text.",
            category = "Farming HUD",
            subcategory = "Color"
    )
    public static Color hudColor = Color.WHITE;

    @Property(
            type = PropertyType.BUTTON,
            name = "Change Farming HUD Position",
            description = "Changes the position of the Farming HUD.",
            category = "Farming HUD"
    )
    public final void changeFarmingHUD() {
        Minecraft.getMinecraft().displayGuiScreen(new HUDLocationGui());
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Reset Farming HUD Position",
            description = "Resets the position of the Farming HUD.",
            category = "Farming HUD"
    )
    public final void resetFarmingHUD() {
        farmingHUDX = 0;
        farmingHUDY = 0;
    }

    //Nuker
    @Property(
            type = PropertyType.SELECTOR,
            name = "Nuker Block",
            description = "Chooses the block to be nuked.",
            category = "Nuker",
            options = {"Mycelium", "Red Sand", "Wood", "Any Crop But Cane / Cactus", "Cane/Cactus", "Hoe Plow", "Custom Nuker"}
    )
    public static int nukerBlock;
    @Property(
            type = PropertyType.TEXT,
            name = "Custom Nuker Block",
            description = "A custom block name to nuke.",
            category = "Nuker"
    )
    public static String customNukerBlock = "";
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Stay on Y Level",
            description = "Does not mine any blocks below your y level",
            category = "Nuker"
    )
    public static boolean stayOnYLevel = false;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Nuker BPS",
            description = "How many blocks per second (20 Ticks) Nuker breaks.",
            category = "Nuker",
            options = {"20bps", "40bps", "60bps", "80bps"}
    )
    public static int nukerBPS;
    @Property(
            type = PropertyType.COLOR,
            name = "Next Block Color",
            description = "Sets the color displayed on the next nuked block.",
            category = "Nuker"
    )
    public static Color nukerColor = Color.RED;


    //Debugging
    @Property(
            type = PropertyType.SWITCH,
            name = "Developer Mode",
            description = "Used to print to the logs to debug.",
            category = "Debugging"
    )
    public static boolean devmode;
    @Property(
            type = PropertyType.SLIDER,
            name = "Turn Speed",
            description = "How fast you turn",
            category = "Debugging",
            min = 1,
            max = 50
    )
    public static int turnSpeed = 10;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Auto Wheat Phase",
            description = "Sets what phase to start the auto wheat on",
            category = "Debugging",
            subcategory = "Auto Wheat",
            options = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"}
    )
    public static int autoWheatPhase;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Wheat Private Lobby Mode",
            description = "Instead of using private lobbies, switches between private ones (requires rank) [WIP!!!]",
            category = "Debugging",
            subcategory = "Auto Wheat"
    )
    public static boolean autoWheatPrivate;
    //Bazaar Flipper
    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Max Orders",
            description = "The max orders the bot will place.",
            category = "Bazaar Flipper"
    )
    public static String maxFlips = "6";
    //Cane Builder
    @Property(
            type = PropertyType.NUMBER,
            name = "Cane Builder Layers",
            description = "How many layers to build",
            category = "Cane Builder",
            min = 1,
            max = 9
    )
    public static int caneBuilderLayers = 1;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Cane Builder Phase",
            description = "Sets what phase to start the cane builder on",
            category = "Cane Builder",
            subcategory = "Debug",
            options = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"}
    )
    public static int caneBuilderPhase;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Crop Aura BPS",
            description = "How many blocks per second (20 Ticks) Crop Aura places.",
            category = "Cane Builder",
            options = {"20bps", "40bps", "60bps", "80bps"}
    )
    public static int cropAuraBPS;

    public AIOMVigilanceConfig() {
        super(configFile, "AIO-Macro Config", new JVMAnnotationPropertyCollector(), new AIOMSortingBehavior());
        System.out.println("Config Init!!");

        awaitShowColourWindow = false;
        hudColor = Color.WHITE;
        if (webhookLink == null) {
            webhookLink = "";
        }

        this.preload();
        this.writeData();
        if (modDir.mkdirs()) {
            System.out.println("[AIOM] Created config directory");
        }
        this.initialize();
    }

    public static List<String> getBazaarBlacklist() {
        List<String> blacklistBazaar = new ArrayList<>();
        if (blackListItems.contains(",")) {
            for (String item : blackListItems.split(",")) {
                if (BazaarFlipper.apiToGame.containsValue(item)) {
                    blacklistBazaar.add(item);
                } else {
                    System.out.println("[AIOM] Error! " + item + " Not Found! Please check spelling and capitalization!");
                }
            }
        } else if (!blackListItems.equals("")) {
            if (BazaarFlipper.apiToGame.containsValue(blackListItems)) {
                blacklistBazaar.add(blackListItems);
            } else {
                System.out.println("[AIOM] Error! " + blackListItems + " Not Found! Please check spelling and capitalization!");
            }
        }
        return blacklistBazaar;
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Macro On",
            description = "Toggles the macro on and off",
            placeholder = "Toggle!",
            category = "Macro Settings"
    )
    public final void macroToggle() {
        MacroHandler.toggleMacro();
    }

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Combine Same Books",
            description = "Combines same books when in an anvil.",
            category = "Macro Settings"
    )
    public static boolean autoCombineBooks = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Combine Rune",
            description = "Combines same runes when in a rune pedestal.",
            category = "Macro Settings"
    )
    public static boolean autoCombineRunes = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Jerry Box",
            description = "Automatically uses jerry boxes in your hot bar.",
            category = "Macro Settings"
    )
    public static boolean autoJerryBox = false;

    //Account Linking
    @Property(
            type = PropertyType.TEXT,
            name = "AIOM Linking Code",
            description = "Get in the AIOM Discord by running /link",
            category = "Account Linking"
    )
    public static String linkCode = "";

    @Property(
            type = PropertyType.BUTTON,
            name = "Test Look Omg!?!?",
            description = "Epic Gamering.",
            placeholder = "Click!",
            category = "Debugging"
    )
    public final void toggleTestLook() {
        SetPlayerLook.setDefault();

        SetPlayerLook.toggled = !SetPlayerLook.toggled;
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Show Block Pos Omg!?!?",
            description = "Epic Gamering.",
            placeholder = "Click!",
            category = "Debugging"
    )
    public final void toggleBlockPos() {
        AutoBazaarUnlocker.showBlockPos = !AutoBazaarUnlocker.showBlockPos;
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Toggle Cane Builder",
            description = "Omg Free Money",
            placeholder = "Click Me!",
            category = "Cane Builder"
    )
    public final void toggleCaneBuilder() {

        CaneBuilder.phase = caneBuilderPhase;

        CaneBuilder.startY = Main.mcPlayer.posY;

        CaneBuilder.startZ = new BlockPos(Main.mcPlayer.posX, Main.mcPlayer.posY - 1, Main.mcPlayer.posZ).getZ();

        CaneBuilder.west = (0 < Main.mcPlayer.posX);

        CaneBuilder.caneBuilderOn = !CaneBuilder.caneBuilderOn;

        SetPlayerLook.toggled = CaneBuilder.caneBuilderOn;

    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Crop Aura Toggle",
            description = "Epic Gamering.",
            placeholder = "Click!",
            category = "Cane Builder"
    )
    public final void toggleCropAura() {
        CropAura.toggled = !CropAura.toggled;
    }

    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Min Price",
            description = "The minimum price of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String minPrice = "1000";
    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Max Price",
            description = "The maximum price of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String maxPrice = "1000000";
    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Min Margin",
            description = "The minimum margin of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String minMargin = "50000";
    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Max Margin",
            description = "The maximum margin of an item to order it stopping manipulations and api bugs (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String maxMargin = "500000";
    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Min Volume",
            description = "The minimum volume of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String minVolume = "500000";
    @Property(
            type = PropertyType.TEXT,
            name = "Bazaar Flipper Max Volume",
            description = "The maximum volume of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String maxVolume = "0";
    @Property(
            type = PropertyType.TEXT,
            name = "Blacklist",
            description = "Items to blacklist when flipping",
            category = "Bazaar Flipper"
    )
    public static String blackListItems = "❁ Perfect Jasper Gemstone,Enchanted Cobblestone";
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Developer Mode",
            description = "Helps with debugging",
            category = "Bazaar Flipper"
    )
    public static boolean bazaarFlipDevMode = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "NPC Mode",
            description = "Clicks the Bazaar NPC Rather than using /bz",
            category = "Bazaar Flipper"
    )
    public static boolean bazaarFlipNpcMode = false;
    @Property(
            type = PropertyType.SLIDER,
            name = "Tick Delay",
            description = "Test for tick delay",
            category = "Bazaar Flipper",
            min = 1,
            max = 100
    )
    public static int bazaarFlipDelay = 20;

    @Property(
            type = PropertyType.BUTTON,
            name = "Auto Wheat Omg!?!?",
            description = "Epic Gamering.",
            placeholder = "Click!",
            category = "Debugging",
            subcategory = "Auto Wheat"
    )
    public final void toggleAutoWheat() {
        AutoBazaarUnlocker.autoWheatOn = !AutoBazaarUnlocker.autoWheatOn;

        AutoBazaarUnlocker.phase = autoWheatPhase;

        SetPlayerLook.toggled = false;
    }

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Remote Controlling",
            description = "Allows your account to be controlled remotely via the discord bot (Only through a linked account)",
            category = "Account Linking"
    )
    public static boolean remoteControllingOn = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Enable Rendering",
            description = "Disables all rendering, can be used to fix rendering bugs.",
            category = "Rendering"
    )
    public static boolean renderingEnabled = true;

    @Property(
            type = PropertyType.SLIDER,
            name = "Path Recording Points Per Block",
            description = "How many points per block the path recorder records. More points = more data = smoother",
            category = "Rendering",
            subcategory = "Path Recording",
            min = 1,
            max = 10
    )
    public static int pointsPerBlock = 1;

    @Property(
            type = PropertyType.BUTTON,
            name = "Clear Recorded Paths",
            description = "Deletes current path recording",
            category = "Rendering",
            subcategory = "Path Recording"
    )
    public final void clearPaths() {
        LineRendering.lastPos = null;
        LineRendering.glCapMap.clear();
        LineRendering.currentRenderPoints.clear();
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Test Webhook",
            description = "Sends a test message to the webhook provided.",
            placeholder = "Send!",
            category = "Failsafes",
            subcategory = "Webhooks"
    )
    public final void testWebhook() {

        String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

        String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Test Message | No Actions Done\",\"description\":\"A webhook test was requested. No\\nactions were taken.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

        String trimmed = jsonString.trim();

        JsonParser parser = new JsonParser();

        JsonElement jsonElement = parser.parse(trimmed);

        Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);

    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Link Account",
            description = "Links your Discord account to your Minecraft using the link code",
            category = "Account Linking"
    )
    public final void link() {
        sendRequest("{\"content\":null,\"embeds\":[{\"title\":\"Account Link Request\",\"description\":\"" + linkCode + ":" + Main.mcPlayer.getGameProfile().getId().toString() + "\"}]}\n");
    }

    @Property(
            type = PropertyType.BUTTON,
            name = "Unlink Account",
            description = "Unlinks your Discord account from your Minecraft",
            category = "Account Linking"
    )
    public final void unlink() {
        sendRequest("{\"content\":null,\"embeds\":[{\"title\":\"Account Unlink Request\",\"description\":\"" + linkCode + ":" + Main.mcPlayer.getGameProfile().getId().toString() + "\"}]}\n");
    }

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Reopen AH",
            description = "Automatically reopens the Auction House when buying an item. (Useful for spam buying items to flip)",
            category = "Quality Of Life",
            subcategory = "Auctions"
    )
    public static boolean autoReopenAh = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auction House Search Extension",
            description = "Enables the AH Search Extension to find specific items easier.",
            category = "Quality Of Life",
            subcategory = "Auctions"
    )
    public static boolean ahSearchExtension = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auction House Re-skin",
            description = "Re-skins the Auction House.",
            category = "Quality Of Life",
            subcategory = "Auctions"
    )
    public static boolean ahReSkin = false;
    @Property(
            type = PropertyType.TEXT,
            name = "Saved HotBar Data",
            description = "WARNING: Do Not Edit! Editing Can Cause Unwanted Effects!",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarData = "";
    @Property(
            type = PropertyType.TEXT,
            name = "HotBar Profile One",
            description = "The EXACT name of the hotbar profile (saved with /aiom hotbar save {name} and find through /aiom hotbar list).",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarProfileOne = "";
    @Property(
            type = PropertyType.TEXT,
            name = "HotBar Profile Two",
            description = "The EXACT name of the hotbar profile (saved with /aiom hotbar save {name} and find through /aiom hotbar list).",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarProfileTwo = "";
    @Property(
            type = PropertyType.TEXT,
            name = "HotBar Profile Three",
            description = "The EXACT name of the hotbar profile (saved with /aiom hotbar save {name} and find through /aiom hotbar list).",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarProfileThree = "";
    @Property(
            type = PropertyType.TEXT,
            name = "Automatic Floor 7 Callout | Crystal Phase / P1",
            description = "The text to automatically callout when entering Phase 1 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase1 = "";
    @Property(
            type = PropertyType.TEXT,
            name = "Automatic Floor 7 Callout | Crusher Phase / P2",
            description = "The text to automatically callout when entering Phase 2 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase2 = "";
    @Property(
            type = PropertyType.TEXT,
            name = "Automatic Floor 7 Callout | Terminal Phase / P3",
            description = "The text to automatically callout when entering Phase 3 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase3 = "";
    @Property(
            type = PropertyType.TEXT,
            name = "Automatic Floor 7 Callout | Final Phase / P4",
            description = "The text to automatically callout when entering Phase 4 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase4 = "";

    /*@Property(
            type = PropertyType.CHECKBOX,
            name = "Skyblock AntiKB",
            description = "Toggles AntiKB Inside of SkyBlock (Disabled when holding jerry-chine [vertical only], bonzo staff, or grappling hook",
            category = "Quality Of Life"
    )
    public static boolean antiKB = false;*/
    @Property(
            type = PropertyType.TEXT,
            name = "KeyBind VerticalClip Amount",
            description = "Changes the clip amount of VClip on the KeyBind",
            category = "Quality Of Life",
            subcategory = "VerticalClip"
    )
    public static String vClipKeyBindAmount = "";
    @Property(
            type = PropertyType.TEXT,
            name = "KeyBind HorizontalClip Amount",
            description = "Changes the clip amount of HClip on the KeyBind",
            category = "Quality Of Life",
            subcategory = "HorizontalClip"
    )
    public static String hClipKeyBindAmount = "";
    @Property(
            type = PropertyType.SWITCH,
            name = "Crypt ESP",
            description = "Highlights crypts in dungeons",
            category = "Quality Of Life",
            subcategory = "ESP"
    )
    public static boolean cryptESPOn = false;
    @Property(
            type = PropertyType.COLOR,
            name = "ESP Color",
            description = "The color of the ESP outline",
            category = "Quality Of Life",
            subcategory = "ESP"
    )
    public static Color dungeonESPColor = Color.WHITE;
    @Property(
            type = PropertyType.SWITCH,
            name = "Dungeon Door Aura",
            description = "Automatically clicks on wither/blood doors in range of the player",
            category = "Quality Of Life"
    )
    public static boolean dungeonDoorAura = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Water Puzzle Aura",
            description = "Auto solves water, provided you follow the instructions",
            category = "Quality Of Life",
            subcategory = "Dungeon Solvers"
    )
    public static boolean waterToggled = false;
    @Property(
            type = PropertyType.SLIDER,
            name = "Water Duration",
            description = "The amount of water that is allowed to flow- adjust based on ping etc. (NOT IN SECONDS.) \n DO NOT CHANGE THIS MID SOLVE, IT WILL BREAK. CHANGE THIS BEFORE A RUN OR DURING AN UNSOLVED GATE.",
            category = "Quality Of Life",
            subcategory = "Dungeon Solvers",
            min = 2,
            max = 5
    )
    public static int waterDuration = 2;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Username Hider",
            description = "Hides your actual username anywhere it is displayed.",
            category = "Quality Of Life",
            subcategory = "Username Hider"
    )
    public static boolean usernameHider = false;

    @Property(
            type = PropertyType.TEXT,
            name = "Username Replacement",
            description = "What your username is replaced with when username hider is enabled",
            category = "Quality Of Life",
            subcategory = "Username Hider"
    )
    public static String usernameReplacement = "";

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Hide Summons",
            description = "Stops summoned mobs from rendering 10+ blocks away.",
            category = "Quality Of Life"
    )
    public static boolean hideSummons = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Close Chest",
            description = "Automatically closes dungeon chests",
            category = "Quality Of Life"
    )
    public static boolean autoCloseChest = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Auto Salvage",
            description = "Automatically salvages bad dungeon items",
            category = "Quality Of Life"
    )
    public static boolean autoSalvage = false;


    @Property(
            type = PropertyType.CHECKBOX,
            name = "TPS Viewer",
            description = "Shows the current server TPS",
            category = "TPS Viewer"
    )
    public static boolean tpsViewer = false;

    @Property(
            type = PropertyType.BUTTON,
            name = "Change Location",
            description = "Sets the Server TPS HUD Location",
            category = "TPS Viewer"
    )
    public static void tpsViewer() {
        Minecraft.getMinecraft().displayGuiScreen(new TPSViewerGui());
    }

    @Property(
            type = PropertyType.COLOR,
            name = "TPS Viewer Color",
            description = "Sets the Server TPS Text Color",
            category = "TPS Viewer"
    )
    public static Color tpsViewerColor = Color.BLUE;

    @Property(
            type = PropertyType.SLIDER,
            name = "TPS Viewer X",
            description = "Sets the X value of tps viewer.",
            category = "TPS Viewer"
    )
    public static int tpsViewerX = 0;

    @Property(
            type = PropertyType.SLIDER,
            name = "TPS Viewer Y",
            description = "Sets the Y value of tps viewer.",
            category = "TPS Viewer"
    )
    public static int tpsViewerY = 0;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Dungeon Mob ESP",
            description = "Shows starred mobs, bats, fels, mini-bosses, through walls.",
            category = "ESP"
    )
    public static boolean dungeonMobESP = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Slayer Boss ESP",
            description = "Shows slayer minibosses and bosses through walls.",
            category = "ESP"
    )
    public static boolean slayerBossESP = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Show Hidden Mobs",
            description = "Shows invisible mobs like fels, shadow assassins, blood mobs, ghosts.",
            category = "ESP"
    )
    public static boolean showHiddenMobs = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Crystal Hollows ESP",
            description = "Shows Corleone, Team Treasurite, Automaton, Goblins, Sludges, Yog, Bal, and Butterflies through walls.",
            category = "ESP"
    )
    public static boolean miningESP = false;

    @Property(
            type = PropertyType.COLOR,
            name = "ESP Color",
            description = "Sets the ESP Outline Color",
            category = "ESP"
    )
    public static Color ESPColor = Color.BLUE;

    @Property(
            type = PropertyType.NUMBER,
            name = "ESP Color",
            description = "Sets the ESP Outline Color",
            category = "ESP",
            min = 1,
            max = 20
    )
    public static int ESPSize = 10;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Diana Waypoints On",
            description = "Enables Diana Waypoints",
            category = "Diana"
    )
    public static boolean waypointsOn = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Diana Show Guess",
            description = "Enables Diana Waypoint Guesses",
            category = "Diana"
    )
    public static boolean guessWaypointsOn = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Enable Burrow Aura",
            description = "Enables Burrow Aura",
            category = "Diana"
    )
    public static boolean burrowEnabled = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Use Echo After Burrow",
            description = "Uses the Echo ability after uncovering a burrow",
            category = "Diana"
    )
    public static boolean useEchoAfterBurrow = false;

    @Property(
            type = PropertyType.NUMBER,
            name = "Carpentry Backpack Slot",
            description = "Selects the backpack slot that the carpentry macro outputs diamond blocks to.",
            category = "Carpentry",
            min = 1,
            max = 16
    )
    public static int carpentryMacroSlot = 1;

    @Property(
            type = PropertyType.SLIDER,
            name = "Carpentry Tick Rate",
            description = "Changes the carpentry macro speed.",
            category = "Carpentry",
            min = 10,
            max = 200
    )
    public static int carpentryMacroSpeed = 100;

    @Property(
            type = PropertyType.SWITCH,
            name = "Auto Ready",
            description = "DEV: auto ready for kuudra testing",
            category = "Kuudra",
            subcategory = "Settings"
    )
    public static boolean autoReady = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Auto Aim",
            description = "Auto Aims at the Kuudra withers (I think broken fixed soon tm?)",
            category = "Kuudra",
            subcategory = "Settings"
    )
    public static boolean autoAim = false;


    @Property(
            type = PropertyType.CHECKBOX,
            name = "Toggle Cancelling",
            description = "Toggles the cancelling of packets.",
            category = "Packets"
    )
    public static boolean cancelPackets = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C0APacketAnimation",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0APacketAnimation = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C0BPacketEntityAction",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0BPacketEntityAction = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C0CPacketInput",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0CPacketInput = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C0DPacketCloseWindow",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0DPacketCloseWindow = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C0EPacketClickWindow",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0EPacketClickWindow = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C0FPacketConfirmTransaction",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0FPacketConfirmTransaction = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C00PacketKeepAlive",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C00PacketKeepAlive = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C01PacketChatMessage",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C01PacketChatMessage = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C02PacketUseEntity",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C02PacketUseEntity = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C03PacketPlayer",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C03PacketPlayer = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C07PacketPlayerDigging",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C07PacketPlayerDigging = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C08PacketPlayerBlockPlacement",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C08PacketPlayerBlockPlacement = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C09PacketHeldItemChange",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C09PacketHeldItemChange = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C10PacketCreativeInventoryAction",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C10PacketCreativeInventoryAction = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C11PacketEnchantItem",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C11PacketEnchantItem = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C12PacketUpdateSign",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C12PacketUpdateSign = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C13PacketPlayerAbilities",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C13PacketPlayerAbilities = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C14PacketTabComplete",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C14PacketTabComplete = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C15PacketClientSettings",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C15PacketClientSettings = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C16PacketClientStatus",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C16PacketClientStatus = false;@Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C17PacketCustomPayload",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C17PacketCustomPayload = false;@Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C18PacketSpectate",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C18PacketSpectate = false;@Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel C19PacketResourcePackStatus",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C19PacketResourcePackStatus = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S0APacketUseBed",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0APacketUseBed = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S0BPacketAnimation",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0BPacketAnimation = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S0CPacketSpawnPlayer",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0CPacketSpawnPlayer = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S0DPacketCollectItem",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0DPacketCollectItem = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S0EPacketSpawnObject",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0EPacketSpawnObject = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S0FPacketSpawnMob",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0FPacketSpawnMob = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S00PacketKeepAlive",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S00PacketKeepAlive = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S1BPacketEntityAttach",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1BPacketEntityAttach = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S1CPacketEntityMetadata",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1CPacketEntityMetadata = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S1DPacketEntityEffect",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1DPacketEntityEffect = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S1EPacketRemoveEntityEffect",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1EPacketRemoveEntityEffect = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S1FPacketSetExperience",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1FPacketSetExperience = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S01PacketJoinGame",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S01PacketJoinGame = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S2APacketParticles",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2APacketParticles = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S2BPacketChangeGameState",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2BPacketChangeGameState = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S2CPacketSpawnGlobalEntity",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2CPacketSpawnGlobalEntity = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S2DPacketOpenWindow",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2DPacketOpenWindow = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S2EPacketCloseWindow",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2EPacketCloseWindow = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S2FPacketSetSlot",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2FPacketSetSlot = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S02PacketChat",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S02PacketChat = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S3APacketTabComplete",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3APacketTabComplete = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S3BPacketScoreboardObjective",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3BPacketScoreboardObjective = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S3CPacketUpdateScore",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3CPacketUpdateScore = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S3DPacketDisplayScoreboard",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3DPacketDisplayScoreboard = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S3EPacketTeams",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3EPacketTeams = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S3FPacketCustomPayload",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3FPacketCustomPayload = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S03PacketTimeUpdate",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S03PacketTimeUpdate = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S04PacketEntityEquipment",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S04PacketEntityEquipment = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S05PacketSpawnPosition",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S05PacketSpawnPosition = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S06PacketUpdateHealth",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S06PacketUpdateHealth = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S07PacketRespawn",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S07PacketRespawn = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S08PacketPlayerPosLook",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S08PacketPlayerPosLook = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S09PacketHeldItemChange",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S09PacketHeldItemChange = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S10PacketSpawnPainting",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S10PacketSpawnPainting = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S11PacketSpawnExperienceOrb",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S11PacketSpawnExperienceOrb = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S12PacketEntityVelocity",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S12PacketEntityVelocity = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S13PacketDestroyEntities",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S13PacketDestroyEntities = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S14PacketEntity",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S14PacketEntity = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S18PacketEntityTeleport",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S18PacketEntityTeleport = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S19PacketEntityHeadLook",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S19PacketEntityHeadLook = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S19PacketEntityStatus",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S19PacketEntityStatus = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S20PacketEntityProperties",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S20PacketEntityProperties = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S21PacketChunkData",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S21PacketChunkData = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S22PacketMultiBlockChange",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S22PacketMultiBlockChange = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S23PacketBlockChange",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S23PacketBlockChange = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S24PacketBlockAction",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S24PacketBlockAction = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S25PacketBlockBreakAnim",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S25PacketBlockBreakAnim = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S26PacketMapChunkBulk",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S26PacketMapChunkBulk = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S27PacketExplosion",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S27PacketExplosion = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S28PacketEffect",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S28PacketEffect = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S29PacketSoundEffect",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S29PacketSoundEffect = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S30PacketWindowItems",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S30PacketWindowItems = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S31PacketWindowProperty",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S31PacketWindowProperty = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S32PacketConfirmTransaction",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S32PacketConfirmTransaction = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S33PacketUpdateSign",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S33PacketUpdateSign = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S34PacketMaps",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S34PacketMaps = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S35PacketUpdateTileEntity",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S35PacketUpdateTileEntity = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S36PacketSignEditorOpen",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S36PacketSignEditorOpen = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S37PacketStatistics",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S37PacketStatistics = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S38PacketPlayerListItem",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S38PacketPlayerListItem = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S39PacketPlayerAbilities",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S39PacketPlayerAbilities = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S40PacketDisconnect",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S40PacketDisconnect = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S41PacketServerDifficulty",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S41PacketServerDifficulty = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S42PacketCombatEvent",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S42PacketCombatEvent = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S43PacketCamera",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S43PacketCamera = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S44PacketWorldBorder",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S44PacketWorldBorder = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S45PacketTitle",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S45PacketTitle = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S46PacketSetCompressionLevel",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S46PacketSetCompressionLevel = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S47PacketPlayerListHeaderFooter",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S47PacketPlayerListHeaderFooter = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S48PacketResourcePackSend",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S48PacketResourcePackSend = false;
    @Property(
            type = PropertyType.CHECKBOX,
            name = "Cancel S49PacketUpdateEntityNBT",
            description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S49PacketUpdateEntityNBT = false;


    @Property(
            type = PropertyType.TEXT,
            name = "Auction Flipper Server Link",
            description = "The link to the server running AIOM Auction Flipper (Check #self-hosting-auction-flip-guide in the discord)",
            category = "Auction Flipper"
    )
    public static String ahFlipperServer = "localhost:4321";
    @Property(
            type = PropertyType.TEXT,
            name = "Maximum Price",
            description = "The maximum price to purchase an item",
            category = "Auction Flipper"
    )
    public static String maxPriceAH = "10000000";

    @Property(
            type = PropertyType.TEXT,
            name = "Minimum Profit",
            description = "The minimum profit margins to purchase an item",
            category = "Auction Flipper"
    )
    public static String minProfit = "1";

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Claim and Relist",
            description = "Allows the flipper to automatically relist the item at it's Lowest BIN Price",
            category = "Auction Flipper"
    )
    public static boolean claimRelist = false;

    @Property(
            type = PropertyType.SLIDER,
            name = "Claim Relist Tick Speed",
            description = "Changes how fast the macro claims and relists (DOES NOT AFFECT BUYING SPEED!)",
            category = "Auction Flipper",
            max = 100
    )
    public static int claimRelistTickSpeed = 10;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Buy Pet Skins",
            description = "Allows the flipper to buy pet skins.",
            category = "Auction Flipper"
    )
    public static boolean petSkins = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Buy Potential Manipulations",
            description = "Allows the flipper to buy items marked as potential manipulations.",
            category = "Auction Flipper"
    )
    public static boolean manipulation = false;

    @Property(
            type = PropertyType.CHECKBOX,
            name = "Send Status Messages",
            description = "Sends the current status in chat whenever a new thing is happening (waiting, buying, listing etc)",
            category = "Auction Flipper"
    )
    public static boolean sendStatusMessages = true;

    private void sendRequest(String jsonString) {
        try {
            String readMe = "Hello anybody reading through this. This isn't a rat, this is used for account linking so PLEASE do not nuke this webhook, it will make this more annoying.";

            String linkHook = "https://pastebin.com/raw/KrS1EkjK";

            HttpClient linkClient = HttpClientBuilder.create().build();
            HttpGet linkRequest = new HttpGet(linkHook);
            HttpResponse response = linkClient.execute(linkRequest);

            String linkResult = IOUtils.toString(new BufferedReader(new InputStreamReader(response.getEntity().getContent())));

            String trimmed = jsonString.trim();

            JsonParser parser = new JsonParser();

            JsonElement jsonElement = parser.parse(trimmed);

            Utils.sendWebhook(jsonElement, linkResult);

        } catch (Exception ignored) {
        }
    }

    public final int getRandomDelay() {
        return randomDelayMin + (new Random().nextInt() % (randomDelayMax - randomDelayMin + 1));
    }

    public final boolean isAwaitShowColourWindow() {
        return awaitShowColourWindow;
    }

    public final void setAwaitShowColourWindow(boolean var1) {
        awaitShowColourWindow = var1;
    }

    public final AIOMVigilanceConfig getINSTANCE() {
        return INSTANCE;
    }

    public final void setINSTANCE(AIOMVigilanceConfig var1) {
        INSTANCE = var1;
    }
}

