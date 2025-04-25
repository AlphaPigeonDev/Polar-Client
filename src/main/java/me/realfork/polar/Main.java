package me.jacktym.aiomacro;

import gg.essential.elementa.effects.StencilEffect;
import gg.essential.universal.UMinecraft;
import gg.essential.universal.UScreen;
import kotlin.jvm.internal.Intrinsics;
import me.jacktym.aiomacro.commands.AIOC;
import me.jacktym.aiomacro.commands.AIOM;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.features.*;
import me.jacktym.aiomacro.features.coins.Trades;
import me.jacktym.aiomacro.keybinds.ModInputHandler;
import me.jacktym.aiomacro.proxy.ClientProxy;
import me.jacktym.aiomacro.proxy.CommonProxy;
import me.jacktym.aiomacro.proxy.PacketHandler;
import me.jacktym.aiomacro.rendering.BeaconRendering;
import me.jacktym.aiomacro.rendering.BlockRendering;
import me.jacktym.aiomacro.rendering.CosmeticRendering;
import me.jacktym.aiomacro.rendering.Renderer;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Mod(modid = NGGlobal.MOD_ID, name = NGGlobal.MOD_NAME, version = NGGlobal.VERSION)
public class Main {

    @Instance(NGGlobal.MOD_ID)
    public static Main instance;

    @SidedProxy(clientSide = NGGlobal.NG_CLIENT_PROXY, serverSide = NGGlobal.NG_COMMON_PROXY)
    public static CommonProxy proxy;
    

    public static UScreen gui = null;

    public static Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayerSP mcPlayer = mc.thePlayer;

    public static WorldClient mcWorld = mc.theWorld;

    public static boolean notNull = false;


    public static boolean remindToUpdate = false;

    public static boolean sentUpdateReminder = false;
    

    public static HashMap<String, Integer> assMap = new HashMap<>();
    public static HashMap<String, Integer> boobMap = new HashMap<>();
    public static HashMap<String, Integer> testicleMap = new HashMap<>();
    public static HashMap<String, Integer> haloMap = new HashMap<>();
    public static void sendChatMessage(String message) {
        mcPlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void sendMarkedChatMessage(String message) {
        mcPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "[" + EnumChatFormatting.LIGHT_PURPLE + "AIOM" + EnumChatFormatting.DARK_PURPLE + "] " + EnumChatFormatting.RESET + message));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent preEvent) {
        proxy.preInit(preEvent);

        AIOMVigilanceConfig.INSTANCE = new AIOMVigilanceConfig();

        try {
            URL versionUrl = new URL("https://raw.githubusercontent.com/JackTYM/AIOMacro/master/currentVersion.txt");
            HttpURLConnection versionConnection = (HttpURLConnection) versionUrl.openConnection();
            InputStream response = versionConnection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();

                int versionInt = Integer.parseInt(responseBody.replace(".", ""));

                int clientVersion = Integer.parseInt(NGGlobal.VERSION.split("v")[1].replace(".", ""));

                if (clientVersion < versionInt) {
                    remindToUpdate = true;
                }
                if (clientVersion > versionInt) {
                    System.out.println("You are in the future!");
                }
            }
        } catch (Exception ignored) {
        }

        try {
            URL sizeUrl = new URL("https://gist.githubusercontent.com/JackTYM/c88536ecbe88f3a6aad98f7aeeaaf951/raw/AIOMSizes.txt");
            HttpURLConnection sizeConnection = (HttpURLConnection) sizeUrl.openConnection();
            InputStream response = sizeConnection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();

                System.out.println(responseBody);

                for (String line : responseBody.split("\n")) {
                    if (line.split(":")[1].startsWith("ass")) {
                        assMap.put(line.split(":")[0], Integer.parseInt(line.split(":ass_")[1]));
                    }
                    if (line.split(":")[1].startsWith("boob")) {
                        boobMap.put(line.split(":")[0], Integer.parseInt(line.split(":boob_")[1]));
                    }
                    if (line.split(":")[1].startsWith("dick")) {
                        testicleMap.put(line.split(":")[0], Integer.parseInt(line.split(":dick_")[1]));
                    }
                    if (line.split(":")[1].startsWith("halo")) {
                        haloMap.put(line.split(":")[0], Integer.parseInt(line.split(":halo_")[1]));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        ClientCommandHandler.instance.registerCommand(new AIOM());
        ClientCommandHandler.instance.registerCommand(new AIOC());

        List<Object> registry = new ArrayList<>();
        registry.add(new ModInputHandler());
        registry.add(new FastBreak());
        registry.add(new Failsafe());
        registry.add(new FarmingHUD());
        registry.add(new NetherWart());
        registry.add(new SugarCane());
        registry.add(new OneRow());
        registry.add(new AntiStuck());
        registry.add(new Nuker());
        registry.add(new SetPlayerLook());
        registry.add(new CropAura());
        registry.add(new BazaarFlipper());
        registry.add(new Main());
        registry.add(new CaneBuilder());
        registry.add(new AutoBazaarUnlocker());
        registry.add(new PathFind());
        registry.add(new FairySoulAura());
        registry.add(new ShinyPigESP());
        registry.add(new BlockRendering());
        registry.add(new MinionAura());
        registry.add(new AutoGodPot());
        registry.add(new AutoCookie());
        registry.add(new AutoHotBar());
        registry.add(new AutoF7Callouts());
        registry.add(new Scatha());
        registry.add(new CryptESP());
        registry.add(new DungeonDoorAura());
        registry.add(new WaterSolver());
        registry.add(new DianaWaypoints());
        registry.add(new BeaconRendering());
        registry.add(new BurrowAura());
        registry.add(new AutoReopenAH());
        registry.add(new CarpentryMacro());
        registry.add(new Kuudra());
        registry.add(new Trades());
        registry.add(new AuctionFlipper());
        registry.add(new DungeonRoomDetection());
        registry.add(new me.jacktym.aiomacro.rendering.LineRendering());
        registry.add(new AHSearchExtension());
        registry.add(new AHReSkin());
        registry.add(new Renderer());
        registry.add(new BlockRendering());
        registry.add(new PacketHandler());
        registry.add(new TPS());
        registry.add(new ModInputHandler());
        registry.add(new BookCombine());
        registry.add(new RuneCombine());
        registry.add(new JerryBoxMacro());

        registry.forEach(MinecraftForge.EVENT_BUS::register);

        StencilEffect.Companion.enableStencil();

        ClientProxy.keyBindings = new KeyBinding[10];

        ClientProxy.keyBindings[0] = new KeyBinding("Toggle Macro", Keyboard.KEY_DELETE, "All-In-One Macro");
        ClientProxy.keyBindings[1] = new KeyBinding("Open Gui", Keyboard.KEY_BACKSLASH, "All-In-One Macro");
        ClientProxy.keyBindings[2] = new KeyBinding("HotBar Profile One", Keyboard.KEY_F6, "All-In-One Macro");
        ClientProxy.keyBindings[3] = new KeyBinding("HotBar Profile Two", Keyboard.KEY_F7, "All-In-One Macro");
        ClientProxy.keyBindings[4] = new KeyBinding("HotBar Profile Three", Keyboard.KEY_F8, "All-In-One Macro");
        ClientProxy.keyBindings[5] = new KeyBinding("VerticalClip", Keyboard.KEY_GRAVE, "All-In-One Macro");
        ClientProxy.keyBindings[6] = new KeyBinding("HorizontalClip", Keyboard.KEY_ADD, "All-In-One Macro");
        ClientProxy.keyBindings[7] = new KeyBinding("Refresh Diana Waypoints", Keyboard.KEY_PAUSE, "All-In-One Macro");
        ClientProxy.keyBindings[8] = new KeyBinding("Toggle Packet Cancelling", Keyboard.KEY_END, "All-In-One Macro");
        ClientProxy.keyBindings[9] = new KeyBinding("Toggle Path Recording", Keyboard.KEY_F8, "All-In-One Macro");

        for (int i = 0; i < ClientProxy.keyBindings.length; i++) {
            ClientRegistry.registerKeyBinding(ClientProxy.keyBindings[i]);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent postEvent) {
        proxy.postInit(postEvent);

        Utils.updateBazaarApi();

        RenderPlayer slim_render = Main.mc.getRenderManager().getSkinMap().get("slim");
        slim_render.addLayer(new CosmeticRendering(slim_render));

        RenderPlayer default_render = Main.mc.getRenderManager().getSkinMap().get("default");
        default_render.addLayer(new CosmeticRendering(default_render));

        AutoHotBar.loadHotBars();

        Locale.setDefault(new Locale("en", "US"));
    }

    @SubscribeEvent
    public final void onJoinWorld(@NotNull EntityJoinWorldEvent e) {
        if (mcWorld != null && mcPlayer != null && !sentUpdateReminder && remindToUpdate) {

            ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/JackTYM/AIOMacro/releases/latest"));
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click To Open GitHub!"));
            ChatComponentText linkMessage = new ChatComponentText("https://github.com/JackTYM/AIOMacro/releases/latest");

            linkMessage.setChatStyle(style.setChatHoverEvent(hoverEvent));

            mcPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "[" + EnumChatFormatting.LIGHT_PURPLE + "AIOM" + EnumChatFormatting.DARK_PURPLE + "] " + EnumChatFormatting.RESET + "A New Update Is Available! You could be missing out on new features or bugfixes! "));
            mcPlayer.addChatMessage(linkMessage);
            sentUpdateReminder = true;

        }
    }

    @SubscribeEvent
    public final void tick(@NotNull TickEvent.ClientTickEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (gui != null) {
            try {
                UMinecraft.getMinecraft().displayGuiScreen(gui);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            gui = null;
        }

        mcPlayer = mc.thePlayer;

        mcWorld = mc.theWorld;

        notNull = mcPlayer != null && mcWorld != null;
    }
}
