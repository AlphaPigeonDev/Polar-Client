package me.jacktym.aiomacro.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.io.File;
import java.io.PrintWriter;
import java.util.Base64;

public class AIOC extends CommandBase {

    public String getCommandName() {
        return "aioc";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/aioc";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public void processCommand(ICommandSender sender, String[] args) {
        File passwordFile = (new File(Main.mc.mcDataDir, "/AIOM-DONOTSHARE"));
        if (args.length == 0) {
            if (passwordFile.exists()) {
                Main.sendMarkedChatMessage("/aioc (message)");
            } else {
                Main.sendMarkedChatMessage("/aioc link [display_name] [password]");
            }
        } else if (args[0].equals("link")) {
            if (args.length == 3) {
                try {
                    if (passwordFile.createNewFile()) {
                        System.out.println("File Created.");
                    } else {
                        System.out.println("File Exists.");
                    }
                    new PrintWriter(passwordFile).close();
                    Utils.writeToFile(passwordFile, Base64.getEncoder().encodeToString(args[2].getBytes()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.sendMarkedChatMessage("Failed To Create/Write To File! Please Report This With Your latest.log!");
                    return;
                }
                JsonObject json = new JsonParser().parse("{\"UUID\":\"" + Main.mcPlayer.getGameProfile().getId().toString() + "\",\"display_name\":\"" + args[1] + "\",\"password\":\"" + args[2] + "\"}\n").getAsJsonObject();
                Utils.sendToServer("http://localhost", json);
                //Utils.sendToServer("http://93.181.131.50:4976", json);
            } else {
                Main.sendMarkedChatMessage("/aioc link [display_name] [password]");
            }
        } else {
            if (passwordFile.exists()) {
                JsonObject json = new JsonParser().parse("{\"message\": args,\"UUID\":" + Main.mcPlayer.getGameProfile().getId().toString() + ",\"password\":" + new String(Base64.getDecoder().decode(Utils.readFromFile(passwordFile).getBytes())) + "}").getAsJsonObject();
                Utils.sendToServer("http://localhost", json);
                //Utils.sendToServer("http://93.181.131.50:4976", json);
            } else {
                Main.sendMarkedChatMessage("Password File Does Not Exist! Please Use /aioc link!");
            }
        }
    }
}
