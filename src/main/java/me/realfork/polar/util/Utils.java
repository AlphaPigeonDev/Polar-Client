package me.jacktym.aiomacro.util;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.essential.universal.ChatColor;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.features.SetPlayerLook;
import me.jacktym.aiomacro.rendering.BeaconRendering;
import me.jacktym.aiomacro.rendering.BlockRendering;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.*;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static JsonObject bazaarApi;

    //ScreenshotUtils ong
    public static String takeScreenshot() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);

            String filename = String.valueOf(System.currentTimeMillis()).substring(String.valueOf(System.currentTimeMillis()).length() - 3);

            File temp = File.createTempFile(filename, ".png");

            ImageIO.write(capture, "png", temp);

            temp.deleteOnExit();

            return uploadScreenshot(temp);
        } catch (Exception e) {
            return null;
        }
    }

    public static String uploadScreenshot(File file) {
        String link = "";

        try {
            URL url;
            url = new URL("https://api.imgur.com/3/image");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedImage image;
            image = ImageIO.read(file);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArray);
            byte[] byteImage = byteArray.toByteArray();
            String dataImage = Arrays.toString(Base64.getEncoder().encode(byteImage));
            String data = URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(dataImage, "UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID 9a47f5fc66bc069");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            conn.connect();
            StringBuilder stb = new StringBuilder();
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                stb.append(line).append("\n");
            }
            wr.close();
            rd.close();

            JsonElement jsonElement = new JsonParser().parse(stb.toString());

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            link = jsonObject.get("data").getAsJsonObject().get("link").toString();
        } catch (Exception ignored) {
        }
        return link;
    }

    //Time Utils
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long timeSinceMillis(long oldMS) {
        return System.currentTimeMillis() - oldMS;
    }

    public static boolean millisPassed(long waitTime) {
        return currentTimeMillis() >= waitTime;
    }


    //Webhook Utils
    public static void sendWebhook(JsonElement jsonElement, String urlString) {

        JsonObject json = new JsonObject();
        json.addProperty("content", "");
        json = jsonElement.getAsJsonObject();

        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "User-Agent");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream stream = connection.getOutputStream();
            stream.write(json.toString().getBytes());
            stream.flush();
            stream.close();

            connection.getInputStream().close();
            connection.disconnect();
        } catch (Exception ignored) {
        }
    }

    public static void updateBazaarApi() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            try {
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(
                        "https://api.hypixel.net/skyblock/bazaar");
                HttpResponse response = client.execute(request);

                String result = IOUtils.toString(new BufferedReader
                        (new InputStreamReader(
                                response.getEntity().getContent())));

                JsonElement jelement = new JsonParser().parse(result);
                bazaarApi = jelement.getAsJsonObject().getAsJsonObject("products");

            } catch (IOException ignored) {
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    //Color Utils
    public static String stripColor(String toStrip) {
        for (ChatColor c : ChatColor.values()) {
            toStrip = toStrip.replaceAll(c.toString(), "");
        }
        return toStrip;
    }

    //Misc Utils
    public static void openNpc(String npcName) {
        List<Entity> loadedEntityList = Main.mcWorld.getLoadedEntityList();

        for (Entity e : loadedEntityList) {
            String entityName = Utils.stripColor(e.getDisplayName().toString().split("text='")[1].split("', siblings")[0]);

            if (entityName.equals(npcName)) {
                Main.mcPlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.INTERACT));
            }
        }
    }

    public static void useEntity(Entity entity) {
        MovingObjectPosition movingObject = Main.mc.objectMouseOver;
        Vec3 vec3 = new Vec3(movingObject.hitVec.xCoord - entity.posX, movingObject.hitVec.yCoord - entity.posY, movingObject.hitVec.zCoord - entity.posZ);
        Main.mcPlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, vec3));
    }

    public static List<String> getScoreboard() {
        List<String> scoreboardLines = new ArrayList<>();
        try {
            ScoreObjective sidebarObjective = Main.mcWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            Collection<Score> scores = Main.mcWorld.getScoreboard().getSortedScores(sidebarObjective);
            for (Score line : scores) {
                ScorePlayerTeam team = Main.mcWorld.getScoreboard().getPlayersTeam(line.getPlayerName());
                String scoreboardLine = ScorePlayerTeam.formatPlayerName(team, line.getPlayerName()).trim();
                String strippedCleansedScoreboardLine = Utils.stripColor(scoreboardLine);
                scoreboardLines.add(strippedCleansedScoreboardLine);
            }

        } catch (NullPointerException ignored) {
        }
        return scoreboardLines;
    }

    public static List<String> getTabFooters() {
        List<String> footerList = new ArrayList<>();
        try {
            final Field myField = ReflectionHelper.findField(Main.mc.ingameGUI.getTabList().getClass(), "field_175255_h", "footer");
            for (IChatComponent sibling : ((IChatComponent) myField.get(Main.mc.ingameGUI.getTabList())).getSiblings()) {
                footerList.add(sibling.getFormattedText());
            }
        } catch (Exception ignored) {
        }
        return footerList;
    }

    public static Long getCounter() {
        try {
            if (Main.mcPlayer.getHeldItem() != null && Main.mcPlayer.getHeldItem().getTagCompound().getCompoundTag("display").getTagList("Lore", 8) != null) {
                NBTTagList loreArray = Main.mcPlayer.getHeldItem().getTagCompound().getCompoundTag("display").getTagList("Lore", 8);

                String lore = Utils.stripColor(loreArray.toString());

                if (lore.contains("Counter")) {

                    String counterWithCrop = lore.split("Counter: ")[1].split("\"")[0];

                    String counterWithoutCrop = counterWithCrop.replace(" Sugar Canes", "").replace(" Nether Warts", "").replace(" Carrots", "").replace(" Wheat", "").replace(" Potatoes", "");

                    return Long.parseLong(counterWithoutCrop.replace(",", ""));
                }
            }
        } catch (NullPointerException ignored) {
        }
        return 0L;
    }

    public static List<String> getTabList() {
        List<String> tabListLines = new ArrayList<>();
        try {
            Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new PlayerComparator());

            NetHandlerPlayClient netHandler = Main.mcPlayer.sendQueue;
            List<NetworkPlayerInfo> fullList = field_175252_a.sortedCopy(netHandler.getPlayerInfoMap());

            for (NetworkPlayerInfo playerInfo : fullList) {
                List<IChatComponent> siblings = playerInfo.getDisplayName().getSiblings();
                for (IChatComponent chatComponent : siblings) {
                    tabListLines.add(chatComponent.getFormattedText());
                }
            }
        } catch (NullPointerException ignored) {
        }

        return tabListLines;
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
        }
    }

    public static String getWorld() {
        String worldName = "";
        List<String> tabList = Utils.getTabList();

        int currentIndex = 0;
        int worldIndex = 0;

        for (String tab : tabList) {
            tab = Utils.stripColor(tab);
            if (tab.contains("Area:")) {
                worldIndex = currentIndex + 1;
            }
            currentIndex++;

        }

        if (worldIndex != 0) {
            worldName = stripColor(tabList.get(worldIndex));
        }
        return worldName;
    }

    public static Double distanceBetweenPoints(Vec3 vec1, Vec3 vec2) {
        if (vec1 != null && vec2 != null) {
            return Math.sqrt((vec1.xCoord - vec2.xCoord) * (vec1.xCoord - vec2.xCoord) +
                    (vec1.yCoord - vec2.yCoord) * (vec1.yCoord - vec2.yCoord) +
                    (vec1.zCoord - vec2.zCoord) * (vec1.zCoord - vec2.zCoord));
        }
        return null;
    }

    public static boolean isMouseGrabbed = true;
    private static MouseHelper oldHelper = Main.mc.mouseHelper;

    public static void unGrab() {
        isMouseGrabbed = false;
        Main.mc.gameSettings.pauseOnLostFocus = false;
        Main.mc.mouseHelper.ungrabMouseCursor();
        oldHelper = Main.mc.mouseHelper;
        Main.mc.mouseHelper = new MouseHelper() {
            @Override
            public void mouseXYChange() {
            }
        };
    }

    public static void reGrab() {
        if (!isMouseGrabbed) {
            if (oldHelper != null) {
                Main.mc.mouseHelper = oldHelper;
            }
            Main.mc.mouseHelper.grabMouseCursor();
            isMouseGrabbed = true;
        }
    }

    public static boolean vec3Equals(Vec3 vec1, Vec3 vec2) {
        if (vec1 != null && vec2 != null) {
            return vec1.subtract(vec2).toString().equals("(0.0, 0.0, 0.0)");
        }
        return false;
    }

    public static boolean vec3NotContains(List<Vec3> vecArray, Vec3 vecSearch) {
        try {
            for (Vec3 vec3 : vecArray) {
                if (vec3.subtract(vecSearch).toString().equals("(0.0, 0.0, 0.0)")) {
                    return false;
                }
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    public static void renderBeacon(BlockPos bp, Color color, String beaconName) {

        ArrayList<Integer> beaconData = new ArrayList<>();


        beaconData.add(bp.getX());
        beaconData.add(bp.getY());
        beaconData.add(bp.getZ());
        beaconData.add(color.getRGB());

        BeaconRendering.beaconData.put(beaconName, beaconData);
    }

    public static String generateBeaconName(String baseName) {
        String returnString;

        int i = 0;
        while (true) {
            if (!BeaconRendering.beaconData.containsKey(baseName + i)) {
                returnString = baseName + i;
                break;
            } else {
                i++;
            }
        }

        return returnString;
    }

    public static void removeSimilarBeaconNames(String baseName) {
        BeaconRendering.beaconData.keySet().removeIf(name -> name.contains(baseName));
    }

    public static void autoAim(Vec3 vec3) {
        Vec3 vec1 = Main.mcPlayer.getPositionVector().addVector(0, 1, 0);
        Vec3 vec2 = vec3.addVector(0, 2, 0);

        BlockRendering.renderMap.clear();
        BlockRendering.renderMap.put(new BlockPos(vec2), Color.GREEN);

        double dirx = vec1.xCoord - vec2.xCoord;
        double diry = vec1.yCoord - vec2.yCoord;
        double dirz = vec1.zCoord - vec2.zCoord;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;

        yaw += 90f;
        SetPlayerLook.pitch = (int) pitch;
        SetPlayerLook.yaw = (int) yaw;
        SetPlayerLook.toggled = true;
    }

    public static void sendToServer(String server, JsonObject jsonData) {
        try {
            URL url = new URL(server);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream stream = connection.getOutputStream();
            stream.write(jsonData.toString().getBytes());
            stream.flush();
            stream.close();

            connection.getInputStream().close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFromServer(String server) {
        try {
            URL url = new URL(server);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                return scanner.useDelimiter("\\A").next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean inDungeons() {
        for (String s : getTabList()) {
            if (s.contains("Catacombs")) {
                return true;
            }
        }
        return false;
    }

    public static String numToRoman(String num) {
        switch (num) {
            case "1":
                return "I";
            case "2":
                return "II";
            case "3":
                return "III";
            case "4":
                return "IV";
            case "5":
                return "V";
            case "6":
                return "VI";
            case "7":
                return "VII";
            case "8":
                return "VIII";
            case "9":
                return "IX";
            case "10":
                return "X";
            default:
                return "";
        }
    }

    public static void writeToFile(File file, String data) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(File file) {
        StringBuilder contents = new StringBuilder();
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                contents.append(reader.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents.toString();
    }

    public static String getSkyBlockID(ItemStack item) {
        if (item != null) {
            NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            if (extraAttributes != null && extraAttributes.hasKey("id")) {
                return extraAttributes.getString("id");
            }
        }
        return "";
    }

    public static ArrayList<Vec3> pickBlocks(Vec3i vec3i, Block blockType) {
        BlockPos playerPos = Main.mcPlayer.getPosition();
        ArrayList<Vec3> blocks = new ArrayList<>();
        for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
            IBlockState blockState = Main.mcWorld.getBlockState(blockPos);
            if (blockState.getBlock() == blockType) {
                blocks.add(new Vec3((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5));
            }
        }
        return blocks;
    }

    public static HashMap<BlockPos, Double>
    sortByValue(HashMap<BlockPos, Double> hm) {
        List<Map.Entry<BlockPos, Double>> list
                = new LinkedList<Map.Entry<BlockPos, Double>>(
                hm.entrySet());

        list.sort(Map.Entry.comparingByValue());

        HashMap<BlockPos, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<BlockPos, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static BlockPos getNearest(HashMap<BlockPos, Double> blocksList) {
        Map.Entry<BlockPos, Double> returnVal = null;
        for (Map.Entry<BlockPos, Double> entry : blocksList.entrySet()) {
            if (entry.getValue() != null) {
                if (returnVal == null || entry.getValue() < returnVal.getValue()) {
                    returnVal = entry;
                }
            }
        }
        if (returnVal != null) {
            return returnVal.getKey();
        } else {
            return new BlockPos(0, 0, 0);
        }
    }
}
