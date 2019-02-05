package com.blayzer.atomcore.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blayzer.atomcore.AtomCore;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class ChunkLoadUtils implements LoadingCallback {
	
	private static Map<String, Ticket> ticketsList = new HashMap<String, Ticket>();

    public static void loadChunk(World world, BlockPos pos) {
    	Ticket chunkTicket = getTicket(world, pos);
    	chunkTicket.getModData().setInteger("blockX", pos.getX());
    	chunkTicket.getModData().setInteger("blockY", pos.getY());
    	chunkTicket.getModData().setInteger("blockZ", pos.getZ());

    	ForgeChunkManager.forceChunk(chunkTicket, new ChunkPos(pos.getX() >> 4,  pos.getZ() >> 4));
    }
    
    public static void unloadChunk(World world, BlockPos pos) {
    	Ticket chunkTicket = getTicket(world, pos);
    	chunkTicket.getModData().setInteger("blockX", pos.getX());
    	chunkTicket.getModData().setInteger("blockY", pos.getY());
    	chunkTicket.getModData().setInteger("blockZ", pos.getZ());
    	
    	ForgeChunkManager.unforceChunk(chunkTicket, new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4));
    }
    
    public static void loadTicket(Ticket ticket, BlockPos pos) {
    	ChunkPos loadChunk = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
    	ForgeChunkManager.forceChunk(ticket, loadChunk);
    }
    
    private static Ticket getTicket(World world, BlockPos pos) {
    	String key = world.getWorldInfo().getWorldName() + "_" + pos;
    	if(ticketsList.containsKey(key))
    		return ticketsList.get(key);
    	else {
    		ticketsList.put(key, ForgeChunkManager.requestTicket(AtomCore.instance, world, Type.NORMAL));
    		return ticketsList.get(key);
    	}
    		
    }

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for(Ticket ticket: tickets){
			int blockX = ticket.getModData().getInteger("blockX");
			int blockY = ticket.getModData().getInteger("blockY");
			int blockZ = ticket.getModData().getInteger("blockZ");
			BlockPos pos = new BlockPos(blockX, blockY, blockZ);
			this.loadTicket(ticket, pos);
		}
	}

//	@Override
//	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
//		List<Ticket> validTickets = Lists.newArrayList();
//		for(Ticket ticket: tickets){
//			int blockX = ticket.getModData().getInteger("blockX");
//			int blockY = ticket.getModData().getInteger("blockY");
//			int blockZ = ticket.getModData().getInteger("blockZ");
//			Block block = world.getBlock(blockX, blockY, blockZ);
//			if(block == MFBlocks.WorldAnchor){
//				validTickets.add(ticket);
//			}
//		}
//		return validTickets;
//	}
}
