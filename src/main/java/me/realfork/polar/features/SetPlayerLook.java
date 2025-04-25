package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.Main;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import me.jacktym.aiomacro.util.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SetPlayerLook {

    public static int yaw = AIOMVigilanceConfig.yaw;

    public static int pitch = AIOMVigilanceConfig.pitch;

    public static boolean toggled = false;

    public static void setDefault() {
        yaw = AIOMVigilanceConfig.yaw;

        pitch = AIOMVigilanceConfig.pitch;
    }

    public static float fixStaticYaw(float yaw) {
        for (int i = 0; i < 10000; i++) {
            if (yaw >= 0) {
                if (yaw - i * 360 <= 180) {
                    yaw = Float.parseFloat(String.format("%.1f", yaw - i * 360));
                    if (yaw == -180) {
                        yaw = 180;
                    }
                    return yaw;
                }
            } else {
                if (yaw + i * 360 >= -180) {
                    yaw = Float.parseFloat(String.format("%.1f", yaw + i * 360));
                    if (yaw == -180) {
                        yaw = 180;
                    }
                    return yaw;
                }
            }
        }
        Main.sendMarkedChatMessage("Error With Fixing Yaw. yaw: " + yaw + ". yaw: " + Float.parseFloat(String.format("%.1f", yaw)) + ".");
        return 0;
    }

    public static boolean isYawLookCorrect() {
        return fixStaticYaw(Main.mcPlayer.rotationYaw) - yaw == 0;
    }

    public static boolean isPitchLookCorrect() {
        return Float.parseFloat(String.format("%.1f", Main.mcPlayer.rotationPitch)) - pitch == 0;
    }

    public static boolean isLookCorrect() {
        return isYawLookCorrect() && isPitchLookCorrect();
    }

    public float fixYaw(float yaw) {
        for (int i = 0; i < 10000; i++) {
            if (yaw >= 0) {
                if (yaw - i * 360 <= 180) {
                    yaw = yaw - i * 360;
                    if (yaw == -180) {
                        yaw = 180;
                    }
                    return Float.parseFloat(String.format("%.1f", yaw));
                }
            } else {
                if (yaw + i * 360 >= -180) {
                    yaw = yaw + i * 360;
                    if (yaw == -180) {
                        yaw = 180;
                    }
                    return Float.parseFloat(String.format("%.1f", yaw));
                }
            }
        }
        Main.sendMarkedChatMessage("Error With Fixing Yaw. yaw: " + yaw + ". yaw: " + Float.parseFloat(String.format("%.1f", yaw)) + ".");
        return 0;
    }

    @SubscribeEvent
    public void goToYaw(TickEvent.ClientTickEvent event) {
        if (toggled) {
            for (int i = 0; i < AIOMVigilanceConfig.turnSpeed && !isYawLookCorrect(); i++) {
                float fixedYaw = fixYaw(Main.mcPlayer.rotationYaw);

                int direction = getDirection(fixedYaw, yaw);

                if (direction == 1) {
                    Main.mcPlayer.rotationYaw -= 0.1;
                } else if (direction == 2) {
                    Main.mcPlayer.rotationYaw += 0.1;
                }
            }
        }
    }

    public int getDirection(float yaw, float yawGoal) {
        double tempYaw1 = Math.floor(yaw);
        double tempYaw2 = Math.floor(yaw);
        if (tempYaw1 == yawGoal) {
            return 1;
        } else if (tempYaw2 == yawGoal) {
            return 2;
        } else {
            for (int i = 0; i < 180; i++) {
                tempYaw1--;
                tempYaw2++;

                if (tempYaw1 == -181) {
                    tempYaw1 = 180;
                }
                if (tempYaw2 == 181) {
                    tempYaw2 = -180;
                }
                if (tempYaw1 == yawGoal) {
                    return 1;
                }
                if (tempYaw2 == yawGoal) {
                    return 2;
                }
            }
        }
        Main.sendMarkedChatMessage("Error With Setting Yaw. tempYaw1: " + tempYaw1 + ". tempYaw2: " + tempYaw2 + ". yawGoal: " + yawGoal);
        return 0;
    }


    @SubscribeEvent
    public void goToPitch(TickEvent.ClientTickEvent event) {
        if (toggled) {
            for (int i = 0; i < AIOMVigilanceConfig.turnSpeed && !isPitchLookCorrect(); i++) {
                if (pitch > Float.parseFloat(String.format("%.1f", Main.mcPlayer.rotationPitch))) {
                    Main.mcPlayer.rotationPitch += 0.1;
                } else {
                    Main.mcPlayer.rotationPitch -= 0.1;
                }
            }
        }
    }

    private static long currentTimeMillis180 = Utils.currentTimeMillis();

    public static void flip180Check() {
        if (Main.mcPlayer.posY != 0 && Main.mcPlayer.motionY <= -0.3 && AIOMVigilanceConfig.drop180 && Utils.currentTimeMillis() - currentTimeMillis180 >= 15000) {
            currentTimeMillis180 = Utils.currentTimeMillis();
            if (SetPlayerLook.yaw >= 0) {
                SetPlayerLook.yaw = SetPlayerLook.yaw - 180;
            } else {
                SetPlayerLook.yaw = SetPlayerLook.yaw + 180;
            }
        }
    }
}
