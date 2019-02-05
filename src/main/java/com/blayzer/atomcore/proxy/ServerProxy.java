package com.blayzer.atomcore.proxy;

import java.sql.SQLException;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.handlers.EntityHandler;
import com.blayzer.atomcore.util.ChunkLoadUtils;
import com.blayzer.atomcore.util.DBUtils;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        
        MinecraftForge.EVENT_BUS.register(new EntityHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        
        ForgeChunkManager.setForcedChunkLoadingCallback(AtomCore.instance, new ChunkLoadUtils());
        try {
			DBUtils.createDBConnection(FMLServerHandler.instance().getSavesDirectory().getAbsolutePath());
			DBUtils.createDBTable();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }


}
