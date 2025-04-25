package me.jacktym.aiomacro.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    public static KeyBinding[] keyBindings;

    public void preInit(FMLPreInitializationEvent preEvent) {

        super.preInit(preEvent);

    }

    public void init(FMLInitializationEvent event) {

        super.init(event);


    }

    public void postInit(FMLPostInitializationEvent postEvent) {

        super.postInit(postEvent);

    }

}