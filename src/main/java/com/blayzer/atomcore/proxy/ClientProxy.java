package com.blayzer.atomcore.proxy;

import com.blayzer.atomcore.block.Blocks;
import com.blayzer.atomcore.gui.ControllerContainerGui;
import com.blayzer.atomcore.handlers.GuardRenderEvent;
import com.blayzer.atomcore.items.Items;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
	
	public static KeyBinding keyActivatedRender = new KeyBinding(I18n.translateToLocalFormatted("settings.rgvisual"), 47, "AtomCore");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.registerKeyBinding(keyActivatedRender);
        MinecraftForge.EVENT_BUS.register(new GuardRenderEvent());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event){
        Blocks.initModels();
        Items.initModels();
    }

}
