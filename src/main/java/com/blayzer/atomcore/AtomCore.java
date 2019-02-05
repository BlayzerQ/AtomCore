package com.blayzer.atomcore;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.scheduler.Task;

import com.blayzer.atomcore.config.ConfigHandler;
import com.blayzer.atomcore.handlers.RegionHandler;
import com.blayzer.atomcore.network.MessageListRegions;
import com.blayzer.atomcore.network.MessageRebootButton;
import com.blayzer.atomcore.network.MessageUpdateInfo;
import com.blayzer.atomcore.proxy.CommonProxy;
import com.blayzer.atomcore.util.ChunkLoadUtils;
import com.blayzer.atomcore.util.DBUtils;
import com.blayzer.atomcore.util.ModUtil;
import com.blayzer.atomcore.util.RedProtectAPI;
import com.blayzer.atomcore.util.RegionData;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

@Mod(modid = ModUtil.MOD_ID,
        name = ModUtil.NAME,
        version = ModUtil.VERSION,
        acceptedMinecraftVersions = "[1.12, 1.13)",
        dependencies = "required-after:redstoneflux;" +
                "required-after:tesla;"
)
@Mod.EventBusSubscriber
public class AtomCore {
	
	public static SimpleNetworkWrapper network;
	
	public static boolean isRenderer = true;
	public static List<RegionData> regions_client = new ArrayList<RegionData>();

	@Mod.Instance(ModUtil.MOD_ID)
    public static AtomCore instance;

    @SidedProxy(clientSide = ModUtil.ClIENT_PROXY, serverSide = ModUtil.SERVER_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        network = NetworkRegistry.INSTANCE.newSimpleChannel("atomcore");
		network.registerMessage(MessageListRegions.Handler.class, MessageListRegions.class, 0, Side.CLIENT);
		network.registerMessage(MessageUpdateInfo.Handler.class, MessageUpdateInfo.class, 1, Side.CLIENT);
		network.registerMessage(MessageRebootButton.Handler.class, MessageRebootButton.class, 2, Side.SERVER);
        proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
    
    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        Task task = Task.builder().execute(new RegionHandler.RegionHandlerTask())
        		.interval(ConfigHandler.minerBlock.checkTime, TimeUnit.MINUTES)
                .name("Region Check Handler").submit(this);
        
        Task task2 = Task.builder().execute(new RegionHandler.RegionVisualTask())
        		.interval(ConfigHandler.minerBlock.rgVisualTime, TimeUnit.SECONDS)
                .name("RGVisual Handler").submit(this);
    }
}
