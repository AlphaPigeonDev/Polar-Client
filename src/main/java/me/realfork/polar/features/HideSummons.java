package me.jacktym.aiomacro.features;

import me.jacktym.aiomacro.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class HideSummons {
    public static boolean isEntityASummon(Entity entity) {
        if(entity instanceof EntityPlayer) {
            return entity.getName().equals("Lost Adventurer");
        } else if(entity instanceof EntityZombie || entity instanceof EntitySkeleton) {
            for(int i = 0; i < 5; i++) {
                ItemStack item = ((EntityMob) entity).getEquipmentInSlot(i);
                if(Utils.getSkyBlockID(item).equals("HEAVY_HELMET") || Utils.getSkyBlockID(item).equals("ZOMBIE_KNIGHT_HELMET") || Utils.getSkyBlockID(item).equals("SKELETOR_HELMET")) {
                    return true;
                }
            }
        }
        return false;
    }
}
