package com.blayzer.atomcore.items;

import com.blayzer.atomcore.block.Blocks;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Items {

    public static ItemRainBlock blockRain_item;
    public static ItemBlockBase blockgenerator_item;

    @SubscribeEvent
    public static void init(RegistryEvent.Register<Item> event) {
        blockRain_item = new ItemRainBlock(Blocks.blockRain);
        blockgenerator_item = new ItemBlockBase(Blocks.blockGenerator);

        event.getRegistry().registerAll(
                blockRain_item,
                blockgenerator_item
        );
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        registerItemBlockModel(blockRain_item);
        registerItemBlockModel(blockgenerator_item);
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemBlockModel(ItemBlockBase item) {
        ModelResourceLocation location = new ModelResourceLocation(item.getModelName());
        ModelLoader.setCustomModelResourceLocation(item, 0, location);
    }
}
