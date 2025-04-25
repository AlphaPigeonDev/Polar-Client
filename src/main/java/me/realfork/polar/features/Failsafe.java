package me.jacktym.aiomacro.features;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import kotlin.jvm.internal.Intrinsics;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Failsafe {

    public static boolean goToIsland = false;

    private static boolean goLobby = true;
    private static boolean goSkyblock = false;
    private static boolean goIsland = false;
    private static boolean startStopFly = false;
    private static boolean endStopFly = false;

    public static boolean checkForJacob = false;

    private static int desync = 0;

    private static int recentBans = 0;

    private int tick = 0;
    long lastCounter = 0;
    private int desyncTick = 0;
    private int jacobTick = 0;

    public static boolean bedrockFailsafe() {
        MovingObjectPosition mop = Main.mc.getRenderViewEntity().rayTrace(200, 1.0F);
        if (mop != null) {
            return Main.mcWorld.getBlockState(mop.getBlockPos()).getBlock() == Block.getBlockFromName("bedrock");
        } else {
            return false;
        }
    }

    private int banWaveTick = 0;
    private int remoteControlTick = 0;
    private boolean gotMacroStatus = false;
    private int macroStatusAttempts = 0;

    public static boolean islandFailsafe() {
        if (Main.mcWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {

            boolean onIsland = false;

            List<String> scoreboardLines = Utils.getScoreboard();
            for (String scoreboardLine : scoreboardLines) {
                if ((Utils.stripColor(scoreboardLine.replaceAll("[^\\x00-\\x7F]", "")).contains("Your Island"))) {
                    onIsland = true;
                }
            }
            if (AIOMVigilanceConfig.devmode) {
                for (String scoreboardLine : scoreboardLines) {
                    System.out.println(Utils.stripColor(scoreboardLine));
                }
            }
            return !onIsland;
        } else {
            return false;
        }
    }

    public static boolean jacobFailsafe() {
        boolean jacobsEvent = false;

        for (String scoreboard : Utils.getScoreboard()) {
            scoreboard = Utils.stripColor(scoreboard);

            if (scoreboard.contains("Jacob's Contes")) {
                jacobsEvent = true;
            }
        }
        return jacobsEvent;
    }

    @SubscribeEvent
    public void playerTick(@NotNull TickEvent.ClientTickEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MacroHandler.isMacroOn && Main.mcPlayer != null && Main.mcWorld != null && (AIOMVigilanceConfig.macroType == 0 || AIOMVigilanceConfig.macroType == 1)) {
            if (AIOMVigilanceConfig.bedrockfailsafe) {
                if (bedrockFailsafe()) {
                    Main.sendMarkedChatMessage("Macro Disabled! | " + EnumChatFormatting.RED + "FAILSAFE Bedrock Detected!");

                    MacroHandler.toggleMacro();

                    if (AIOMVigilanceConfig.soundfailsafe) {
                        for (int i = 0; i < 5; i++) {
                            Main.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("note.pling"), (float) Main.mcPlayer.posX, (float) Main.mcPlayer.posY, (float) Main.mcPlayer.posZ));
                        }
                    }
                    if (AIOMVigilanceConfig.webhookAlerts) {

                        String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                        String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Stopped\",\"description\":\"The macro has stopped due to bedrock\\nbeing detected in front of the player.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                        String trimmed = jsonString.trim();

                        JsonParser parser = new JsonParser();

                        JsonElement jsonElement = parser.parse(trimmed);

                        Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                    }
                }
            }
            if (AIOMVigilanceConfig.jacobfailsafe) {
                if (jacobFailsafe()) {
                    Main.sendMarkedChatMessage("Macro Paused! | " + EnumChatFormatting.RED + "FAILSAFE! Jacobs Event Started!");

                    MacroHandler.toggleMacro();

                    checkForJacob = true;

                    if (AIOMVigilanceConfig.soundfailsafe) {
                        for (int i = 0; i < 5; i++) {
                            if (Utils.millisPassed(Utils.currentTimeMillis() + 3)) {
                                Main.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("note.pling"), (float) Main.mcPlayer.posX, (float) Main.mcPlayer.posY, (float) Main.mcPlayer.posZ));
                            }
                        }
                    }
                    if (AIOMVigilanceConfig.webhookAlerts) {

                        String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                        String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Paused\",\"description\":\"The macro has paused due to a jacob's\\nevent starting.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                        String trimmed = jsonString.trim();

                        JsonParser parser = new JsonParser();

                        JsonElement jsonElement = parser.parse(trimmed);
                        Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                    }
                }
            }
            if (AIOMVigilanceConfig.islandfailsafe != 2) {
                if (islandFailsafe()) {
                    if (AIOMVigilanceConfig.soundfailsafe) {
                        for (int i = 0; i < 5; i++) {
                            Main.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("note.pling"), (float) Main.mcPlayer.posX, (float) Main.mcPlayer.posY, (float) Main.mcPlayer.posZ));
                        }
                    }
                    if (AIOMVigilanceConfig.islandfailsafe == 0) {
                        Main.sendMarkedChatMessage("Macro Disabled! | " + EnumChatFormatting.RED + "FAILSAFE You are not on your island!");

                        MacroHandler.toggleMacro();

                        if (AIOMVigilanceConfig.webhookAlerts) {

                            String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                            String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Stopped\",\"description\":\"The macro has stopped due to not\\nbeing on your island.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                            String trimmed = jsonString.trim();

                            JsonParser parser = new JsonParser();

                            JsonElement jsonElement = parser.parse(trimmed);

                            Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                        }
                    }
                    if (AIOMVigilanceConfig.islandfailsafe == 1) {
                        Main.sendMarkedChatMessage("Macro Paused! | " + EnumChatFormatting.RED + "FAILSAFE You are not on your island!");

                        MacroHandler.toggleMacro();
                        if (!goToIsland) {
                            goToIsland = true;
                        }

                        if (AIOMVigilanceConfig.webhookAlerts) {

                            String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                            String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Paused\",\"description\":\"The macro has paused due to not\\nbeing on your island.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                            String trimmed = jsonString.trim();

                            JsonParser parser = new JsonParser();

                            JsonElement jsonElement = parser.parse(trimmed);

                            Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                        }
                    }
                }
            }
            if (AIOMVigilanceConfig.desyncFailsafe && desync >= 5) {
                desync = 0;
                Main.sendMarkedChatMessage("Macro Paused! | " + EnumChatFormatting.RED + "FAILSAFE You have been De-synced! Re-syncing.");

                MacroHandler.toggleMacro();
                if (!goToIsland) {
                    goToIsland = true;
                }

                if (AIOMVigilanceConfig.webhookAlerts) {

                    String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                    String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Paused\",\"description\":\"The macro has paused due to the\\nplayer being de-synced.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                    String trimmed = jsonString.trim();

                    JsonParser parser = new JsonParser();

                    JsonElement jsonElement = parser.parse(trimmed);

                    Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                }
            }
            if (AIOMVigilanceConfig.banwaveFailsafe && recentBans >= AIOMVigilanceConfig.banWavePlayers) {
                Main.sendMarkedChatMessage("Macro Disabled! | " + EnumChatFormatting.RED + "FAILSAFE A ban wave is occurring! Leaving SkyBlock.");

                MacroHandler.toggleMacro();

                Main.mcPlayer.sendChatMessage("/l");

                if (AIOMVigilanceConfig.webhookAlerts) {

                    String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                    String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Paused\",\"description\":\"The macro has paused due to a\\nban wave being activated.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                    String trimmed = jsonString.trim();

                    JsonParser parser = new JsonParser();

                    JsonElement jsonElement = parser.parse(trimmed);

                    Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                }
            }
            if (AIOMVigilanceConfig.autoWebhook != 0 && ((Utils.currentTimeMillis() - MacroHandler.macroStartMillis) / 60000 % AIOMVigilanceConfig.autoWebhook) == 0 && AIOMVigilanceConfig.webhookAlerts && (Utils.currentTimeMillis() - MacroHandler.macroStartMillis) >= 60000) {

                String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Automatic Webhook\",\"description\":\"The macro has not been stopped\\nthe automatic timer has passed.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                String trimmed = jsonString.trim();

                JsonParser parser = new JsonParser();

                JsonElement jsonElement = parser.parse(trimmed);

                Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);

            }
            if (AIOMVigilanceConfig.autoDisable != 0 && ((Utils.currentTimeMillis() - MacroHandler.macroStartMillis) / 60000 % AIOMVigilanceConfig.autoDisable) == 0 && (Utils.currentTimeMillis() - MacroHandler.macroStartMillis) >= 60000) {
                MacroHandler.toggleMacro();
                if (AIOMVigilanceConfig.webhookAlerts) {

                    String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                    String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Stopped\",\"description\":\"The macro has stopped due to the\\nautomatic timer ending.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                    String trimmed = jsonString.trim();

                    JsonParser parser = new JsonParser();

                    JsonElement jsonElement = parser.parse(trimmed);

                    Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                }
            }
        }
    }

    @SubscribeEvent
    public void checkTick(TickEvent.ClientTickEvent event) {
        if (AIOMVigilanceConfig.desyncFailsafe && Main.mcPlayer != null && Main.mcWorld != null) {
            if (MacroHandler.isMacroOn) {
                //5 Seconds
                if (desyncTick >= 100) {
                    desyncTick = 0;

                    if (lastCounter == Utils.getCounter() && Utils.getCounter() != 0L) {
                        desync++;
                    }
                    lastCounter = Utils.getCounter();
                }
                //30 Seconds
                if (banWaveTick >= 600) {
                    banWaveTick = 0;
                    try {
                        URL banwaveUrl = new URL("https://snipes.rip/banstats");
                        HttpURLConnection banwaveConnection = (HttpURLConnection) banwaveUrl.openConnection();
                        InputStream response = banwaveConnection.getInputStream();
                        try (Scanner scanner = new Scanner(response)) {
                            String responseBody = scanner.useDelimiter("\\A").next();

                            JsonElement jsonElement = new JsonParser().parse(responseBody);

                            recentBans = jsonElement.getAsJsonObject().get("record").getAsJsonObject().get("staff").getAsJsonObject().get("staff_latest_15m").getAsInt();
                        }
                    } catch (Exception ignored) {
                    }
                }

                //10 Seconds
                if (jacobTick >= 200) {
                    if (!jacobFailsafe() && checkForJacob) {
                        Main.sendMarkedChatMessage("Macro Unpaused! | " + EnumChatFormatting.RED + "FAILSAFE! Jacobs Event Ended!");

                        MacroHandler.toggleMacro();

                        checkForJacob = false;
                        if (AIOMVigilanceConfig.soundfailsafe) {
                            for (int i = 0; i < 5; i++) {
                                if (Utils.millisPassed(Utils.currentTimeMillis() + 3)) {
                                    Main.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("note.pling"), (float) Main.mcPlayer.posX, (float) Main.mcPlayer.posY, (float) Main.mcPlayer.posZ));
                                }
                            }
                        }
                        if (AIOMVigilanceConfig.webhookAlerts) {

                            String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                            String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Failsafe Alert | Macro Paused\",\"description\":\"The macro has unpaused due to a jacob's\\nevent ending.\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                            String trimmed = jsonString.trim();

                            JsonParser parser = new JsonParser();

                            JsonElement jsonElement = parser.parse(trimmed);
                            Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                        }
                    }
                }
                banWaveTick++;
                desyncTick++;
                jacobTick++;
            }
            //10 Seconds
            if (remoteControlTick >= 200) {
                if (AIOMVigilanceConfig.remoteControllingOn) {
                    remoteControlTick = 0;
                    if (macroStatusAttempts >= 6) {
                        macroStatusAttempts = 0;
                        gotMacroStatus = false;
                    }
                    macroStatusAttempts++;
                    if (!gotMacroStatus) {
                        try {
                            URL versionUrl = new URL("https://gist.githubusercontent.com/JackTYM/b20a15ad8926123ee2e49ae5d7e03b0d/raw/");
                            HttpURLConnection versionConnection = (HttpURLConnection) versionUrl.openConnection();
                            InputStream response = versionConnection.getInputStream();
                            try (Scanner scanner = new Scanner(response)) {
                                String responseBody = scanner.useDelimiter("\\A").next();

                                String[] controls = responseBody.split("\n");

                                if (controls.length != 0) {
                                    for (String control : controls) {
                                        if (Main.mcPlayer.getGameProfile().getId().toString().equals(control.split(":")[0])) {
                                            switch (control.split(":")[1]) {
                                                case ("stop"):
                                                    if (MacroHandler.isMacroOn) {
                                                        MacroHandler.isMacroOn = false;
                                                        Main.sendMarkedChatMessage("Macro Disabled! | Remote Controlling");
                                                    }
                                                    gotMacroStatus = true;
                                                    break;
                                                case ("start"):
                                                    if (!MacroHandler.isMacroOn) {
                                                        MacroHandler.isMacroOn = true;
                                                        Main.sendMarkedChatMessage("Macro Enabled! | Remote Controlling");
                                                    }
                                                    gotMacroStatus = true;
                                                    break;
                                                case ("status"):
                                                    Main.sendMarkedChatMessage("Macro Status Sent! | Remote Controlling");
                                                    if (!AIOMVigilanceConfig.webhookLink.equals("")) {
                                                        String screenshotLink = Objects.requireNonNull(Utils.takeScreenshot()).replace("\"", "");

                                                        String jsonString = "{\"content\":null,\"embeds\":[{\"title\":\"Macro Status Report\",\"description\":\"A macro status report was requested.\\nMacro Enabled: " + MacroHandler.isMacroOn + "\\n\\nAccount: " + Main.mcPlayer.getName() + "\",\"color\":5814783,\"author\":{\"name\":\"AIO-Macro\"},\"image\":{\"url\":\"" + screenshotLink + "\"}}]}\n";

                                                        String trimmed = jsonString.trim();

                                                        JsonParser parser = new JsonParser();

                                                        JsonElement jsonElement = parser.parse(trimmed);

                                                        Utils.sendWebhook(jsonElement, AIOMVigilanceConfig.webhookLink);
                                                    } else {
                                                        Main.sendMarkedChatMessage("Failed to send status alert. Invalid webhook.");
                                                    }
                                                    gotMacroStatus = true;
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            remoteControlTick++;
        }
    }

    @SubscribeEvent
    public void goToIsland(@NotNull TickEvent.ClientTickEvent event) {
        if (goToIsland) {
            if (tick >= 200 && Main.mcWorld != null && Main.mcPlayer != null) {
                if (goLobby) {
                    Main.mcPlayer.sendChatMessage("/l");
                    goLobby = false;
                    goSkyblock = true;
                } else if (goSkyblock) {
                    Main.mcPlayer.sendChatMessage("/skyblock");
                    goSkyblock = false;
                    goIsland = true;
                } else if (goIsland) {
                    Main.mcPlayer.sendChatMessage("/is");
                    goIsland = false;
                    startStopFly = true;
                } else if (startStopFly) {
                    startStopFly = false;
                    endStopFly = true;

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                } else if (endStopFly) {
                    endStopFly = false;
                    goLobby = true;

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);

                    Main.sendMarkedChatMessage("Macro Unpaused!");
                    goToIsland = false;
                    MacroHandler.isMacroOn = true;
                }
                tick = 0;
            }

            tick++;
        }
    }
}
