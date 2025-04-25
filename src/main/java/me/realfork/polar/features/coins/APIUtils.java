package me.jacktym.aiomacro.features.coins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jacktym.aiomacro.util.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class APIUtils {

    public static Map<String, String> auctionItems = new HashMap<>();
    public static long lastUpdated = 0;

    @SubscribeEvent
    public static void updateAuctionsAPI(TickEvent.ClientTickEvent event) {
        if (lastUpdated == 0 || System.currentTimeMillis() - lastUpdated >= 60000) {
            try {
                URL auctionUrl = new URL("https://api.hypixel.net/skyblock/auctions");
                HttpURLConnection connection = (HttpURLConnection) auctionUrl.openConnection();
                InputStream response = connection.getInputStream();
                if (response != null) {
                    try (Scanner scanner = new Scanner(response)) {
                        String responseBody = scanner.useDelimiter("\\A").next();

                        JsonElement jsonElement = new JsonParser().parse(responseBody);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        lastUpdated = jsonObject.get("lastUpdated").getAsLong();

                        int numPages = jsonObject.get("totalPages").getAsInt();
                        new Thread(() -> {
                            for (int page = 0; page < numPages; page++) {
                                try {
                                    URL pageUrl = new URL("https://api.hypixel.net/skyblock/auctions?page=" + page);
                                    HttpURLConnection connection1 = (HttpURLConnection) pageUrl.openConnection();
                                    InputStream response1 = connection1.getInputStream();
                                    if (response1 != null) {
                                        try (Scanner scanner1 = new Scanner(response1)) {
                                            String responseBody1 = scanner1.useDelimiter("\\A").next();

                                            JsonElement jsonElement1 = new JsonParser().parse(responseBody1);
                                            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
                                            JsonArray auctions = jsonObject1.getAsJsonArray("auctions");

                                            for (JsonElement item : auctions) {
                                                boolean isBin = item.getAsJsonObject().get("bin").getAsBoolean();
                                                if (isBin) {
                                                    JsonElement itemLore = item.getAsJsonObject().get("item_lore");
                                                    JsonElement itemName = item.getAsJsonObject().get("item_name");
                                                    JsonElement itemPrice = item.getAsJsonObject().get("starting_bid");
                                                    JsonElement itemSeller = item.getAsJsonObject().get("auctioneer");

                                                    String lore = Utils.stripColor(itemLore.getAsString());

                                                    String itemNameString = itemName.getAsString();

                                                    //black/whitelist code here


                                                    //add item to list of auctions

                                                    auctionItems.put(itemNameString, lore + " ;" + "Price: " + itemPrice + " , Seller: " + itemSeller);
                                                }
                                            }
                                            page++;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }
}

