package com.blayzer.atomcore.proxy;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.handlers.GuiHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

//    public static Configuration config;

    public void preInit(FMLPreInitializationEvent event){
//        config = new Configuration(event.getSuggestedConfigurationFile());
//        ConfigHandler.readConfig();
    }

    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(AtomCore.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event){
//        if (config.hasChanged())
//            config.save();
    }

}
