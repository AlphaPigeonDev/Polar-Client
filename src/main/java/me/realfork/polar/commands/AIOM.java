package me.jacktym.aiomacro.commands;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.features.AutoHotBar;
import me.jacktym.aiomacro.features.Nuker;
import me.jacktym.aiomacro.features.PathFind;
import me.jacktym.aiomacro.features.SetPlayerLook;
import me.jacktym.aiomacro.rendering.BeaconRendering;
import me.jacktym.aiomacro.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.awt.*;
import java.util.List;

public class AIOM extends CommandBase {

    public String getCommandName() {
        return "aiom";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/aiom";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public static String fragBotNameCache = "";

    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Main.gui = AIOMVigilanceConfig.INSTANCE.gui();
        } else {
            switch (args[0]) {
                case "fragbot":
                case "fb":
                    fragBotNameCache = Utils.getFromServer("https://gist.githubusercontent.com/JackTYM/7879a5546f9e897c82ecfd438192129b/raw/84e07031215689c583a8cbf11760968d13d05a83/AIOMFragbot.txt");
                    Main.mcPlayer.sendChatMessage("/p " + fragBotNameCache);
                    break;
                case "hotbar":
                    if (args.length == 1) {
                        Main.sendMarkedChatMessage("Proper Usage: /aiom hotbar (save/load/delete) {name} | /aiom hotbar list");
                    } else {
                        switch (args[1]) {
                            case "save":
                                AutoHotBar.saveHotBar(args[2]);
                                Main.sendMarkedChatMessage(args[2] + " has been saved!");
                                break;
                            case "load":
                                AutoHotBar.hotBarSolution = AutoHotBar.findHotBarSolution(AutoHotBar.hotBars.get(args[2]));
                                AutoHotBar.swapHotBar = true;
                                break;
                            case "list":
                                Main.sendMarkedChatMessage("HotBar List: " + AutoHotBar.hotBars.keySet());
                                break;
                            case "delete":
                                AutoHotBar.hotBars.remove(args[2]);
                                Main.sendMarkedChatMessage(args[2] + " has been removed!");
                                break;
                            default:
                                Main.sendMarkedChatMessage("Proper Usage: /aiom hotbar (save/load/delete) {name} | /aiom hotbar list");
                                break;
                        }
                    }
                    break;
                case "vclip":
                    if (args.length == 1) {
                        Main.sendMarkedChatMessage("/aiom vclip (num)");
                    } else {
                        Main.mcPlayer.setPosition(Main.mcPlayer.posX, Main.mcPlayer.posY + Double.parseDouble(args[1]), Main.mcPlayer.posZ);
                    }
                    break;
                case "hclip":
                    if (args.length == 1) {
                        Main.sendMarkedChatMessage("/aiom hclip (num)");
                    } else {
                        if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= -135 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= -45) {
                            Main.mcPlayer.setPosition(Main.mcPlayer.posX + Double.parseDouble(args[1]), Main.mcPlayer.posY, Main.mcPlayer.posZ);
                        } else if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= -45 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= 45) {
                            Main.mcPlayer.setPosition(Main.mcPlayer.posX, Main.mcPlayer.posY, Main.mcPlayer.posZ + Double.parseDouble(args[1]));
                        } else if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= 45 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= 135) {
                            Main.mcPlayer.setPosition(Main.mcPlayer.posX - Double.parseDouble(args[1]), Main.mcPlayer.posY, Main.mcPlayer.posZ);
                        } else if (SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) <= 135 && SetPlayerLook.fixStaticYaw(Main.mcPlayer.rotationYaw) >= -135) {
                            Main.mcPlayer.setPosition(Main.mcPlayer.posX, Main.mcPlayer.posY, Main.mcPlayer.posZ - Double.parseDouble(args[1]));
                        }
                    }
                    break;
                case "customclip":
                    if (args.length <= 3) {
                        Main.sendMarkedChatMessage("/aiom customclip (x) (y) (z))");
                    } else {
                        Main.mcPlayer.setPosition(Main.mcPlayer.posX + Double.parseDouble(args[1]), Main.mcPlayer.posY + Double.parseDouble(args[2]), Main.mcPlayer.posZ + Double.parseDouble(args[3]));
                    }
                    break;
                case "drawbeacon":
                    if (args.length == 1) {
                        Main.sendMarkedChatMessage("/aiom drawbeacon [add/remove/list]");
                    } else {
                        if (args[1].equals("add")) {
                            if (args.length < 7) {
                                Main.sendMarkedChatMessage("/aiom drawbeacon add (x) (y) (z) (rgb color) (beacon name)");
                            } else {
                                Utils.renderBeacon(new BlockPos(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])), new Color(Integer.parseInt(args[4])), Utils.generateBeaconName(args[5]));
                            }
                        } else if (args[1].equals("remove")) {
                            if (args.length < 3) {
                                Main.sendMarkedChatMessage("/aiom drawbeacon remove (beacon name)");
                            } else {
                                BeaconRendering.beaconData.remove(args[2]);
                            }
                        } else if (args[1].equals("list")) {
                            Main.sendMarkedChatMessage(BeaconRendering.beaconData.keySet().toString());
                        }
                    }
                    break;
                case "pathfind":
                    if (args.length == 1) {
                        Main.sendMarkedChatMessage("/aiom pathfind [clear/block/entity] OR /aiom pathfind (x) (y) (z)");
                    } else {
                        switch (args[1]) {
                            case "clear":
                                PathFind.clear();
                                break;
                            case "block":
                                if (args.length == 2) {
                                    Main.sendMarkedChatMessage("/aiom pathfind block (block name)");
                                } else {
                                    PathFind.clear();

                                    new Thread(() -> {
                                        List<Vec3> blockList = Nuker.pickBlocks(new Vec3i(100, 100, 100));

                                        blockList.removeIf(block -> Main.mcWorld.getBlockState(new BlockPos(block)).getBlock() != Block.getBlockFromName(args[2]));

                                        Vec3 closestBlock = null;

                                        for (Vec3 vec3 : blockList) {
                                            if (closestBlock == null || Utils.distanceBetweenPoints(vec3, Main.mcPlayer.getPositionVector()) < Utils.distanceBetweenPoints(closestBlock, Main.mcPlayer.getPositionVector())) {
                                                closestBlock = new Vec3(new BlockPos(vec3));
                                            }
                                        }

                                        if (closestBlock != null) {
                                            if (args.length == 3) {
                                                PathFind.pathFind(closestBlock.addVector(0, 1, 0), true);
                                            } else {
                                                PathFind.pathFind(closestBlock.addVector(0, 1, 0), Boolean.parseBoolean(args[3]));
                                            }
                                        }
                                    }).start();
                                }
                                break;
                            case "entity":
                                if (args.length == 2) {
                                    Main.sendMarkedChatMessage("/aiom pathfind entity (entity name)");
                                } else {
                                    PathFind.clear();

                                    new Thread(() -> {
                                        List<Entity> entityList = Main.mcWorld.loadedEntityList;

                                        entityList.removeIf(entity -> !entity.getDisplayName().getUnformattedText().equals(args[2]));

                                        Entity closestEntity = null;

                                        for (Entity entity : entityList) {
                                            if (closestEntity == null || Utils.distanceBetweenPoints(entity.getPositionVector(), Main.mcPlayer.getPositionVector()) < Utils.distanceBetweenPoints(closestEntity.getPositionVector(), Main.mcPlayer.getPositionVector())) {
                                                closestEntity = entity;
                                            }
                                        }

                                        if (closestEntity != null) {
                                            if (args.length == 3) {
                                                PathFind.pathFind(new Vec3(new BlockPos(closestEntity.getPositionVector())), true);
                                            } else {
                                                PathFind.pathFind(new Vec3(new BlockPos(closestEntity.getPositionVector())), Boolean.parseBoolean(args[3]));
                                            }
                                        }
                                    }).start();
                                }
                                break;
                            default:
                                if (args.length <= 3) {
                                    Main.sendMarkedChatMessage("/aiom pathfind (x) (y) (z)");
                                }
                                PathFind.clear();
                                if (args.length == 4) {
                                    PathFind.pathFind(new Vec3(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])), true);
                                } else {
                                    PathFind.pathFind(new Vec3(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])), Boolean.valueOf(args[4]));
                                }
                        }
                        break;
                    }
            }
        }
    }
}
