package com.blayzer.atomcore.config;

import net.minecraftforge.common.config.ConfigManager;

import com.blayzer.atomcore.util.ModUtil;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModUtil.MOD_ID)
public class ConfigHandler {

    public static MinerBlock minerBlock = new MinerBlock();
    public static Generator generator = new Generator();

    public static class MinerBlock {
    	MinerBlock() {
        }
        
        @Config.Comment("[FE] % for convert FE to dark-metter (energy*pecent/100)")
        @Config.RangeInt(min = 0, max = 100)
        public int percent = 30;
        
        @Config.Comment("[FE] cost RF for 1 block of region")
        @Config.RangeInt(min = 0, max = 1000)
        public int DMPerBlock = 25;
        
        @Config.Comment("Time in minutes for region checker")
        @Config.RangeInt(min = 1, max = 1440)
        public int checkTime = 60;
        
        @Config.Comment("Time in seconds for RGVisual data send")
        @Config.RangeInt(min = 1, max = 1440)
        public int rgVisualTime = 20;
        
        @Config.Comment("Cooldown for usage block in secs")
        @Config.RangeInt(min = 1, max = 1440)
        public int cooldownLength = 60;

//        @Config.Comment("[FE] FE needed to activate the block")
//        @Config.RangeInt(min = 0)
//        public int FE_activation = 500000;
        
        @Config.Comment("Range around players for send regions to visual (global task)")
        @Config.RangeInt(min = 1, max = 1000)
        public int radiusGlobal = 100;
        
        @Config.Comment("Range around players for send regions to visual (on private region)")
        @Config.RangeInt(min = 1, max = 1000)
        public int radiusOnPrivate = 40;

        @Config.Comment("[FE] max FE input")
        @Config.RangeInt(min = 0)
        public int FE_maxInput = 4000;

        @Config.Comment("[FE] inner FE capacity")
        @Config.RangeInt(min = 0)
        public int FE_capacity = 100000000;

    }

    public static class Generator {
        Generator() {
        }

        @Config.Comment("FE generation")
        @Config.RangeInt(min = 0)
        public int generation = 40;

        @Config.Comment("maximum FE output per side")
        @Config.RangeInt(min = 0)
        public int maxOutput = 2000;

        @Config.Comment("inner FE capacity")
        @Config.RangeInt(min = 0)
        public int capacity = 200000;
    }

    @Mod.EventBusSubscriber(modid = ModUtil.MOD_ID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            event.getConfigID();
            if (event.getModID().equals(ModUtil.MOD_ID)) {
                ConfigManager.sync(ModUtil.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
