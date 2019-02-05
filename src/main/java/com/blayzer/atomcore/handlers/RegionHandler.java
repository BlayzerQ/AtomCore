package com.blayzer.atomcore.handlers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;

import com.blayzer.atomcore.AtomCore;
import com.blayzer.atomcore.config.ConfigHandler;
import com.blayzer.atomcore.network.MessageListRegions;
import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;
import com.blayzer.atomcore.util.ChunkLoadUtils;
import com.blayzer.atomcore.util.DBUtils;
import com.blayzer.atomcore.util.Economy;
import com.blayzer.atomcore.util.RedProtectAPI;
import com.blayzer.atomcore.util.RegionData;
import com.blayzer.atomcore.util.TextHelper;
import com.blayzer.atomcore.util.RegionData.Area;
import com.blayzer.atomcore.util.RegionData.Area.Vec3;

import br.net.fabiozumbi12.RedProtect.Sponge.RPUtil;
import br.net.fabiozumbi12.RedProtect.Sponge.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RegionHandler {
	//Получаем тайл, получаем энергию в нем, затем если ее достаточно для оплаты привата - отнимаем от энергии в блоке нужное количество материи/энергии
	//Шифт-клик по блоку - досрочный вызов проверки, но только для данного блока, если игрок пополнил энергией блок - подумать над нужностью
	//public static Map<String, BlockPos> minersCache = new HashMap<String, BlockPos>();
	
	public static void setFlags(Region r, String[] flags, String value) {
		for(String f : flags) {
			Object objflag = RPUtil.parseObject(value);
			//r.setFlag(null, f, objflag);
			RedProtectAPI.setRegionFlag(r, f, objflag);
		}
	}
	
	public static void notifyPlayers(Region region, String message) {
		List<String> members = new ArrayList<String>(); 
		members.addAll(region.getAdmins());
		members.addAll(region.getLeaders());
		members.addAll(region.getMembers());
		for(String s : members) {
			EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList().getPlayerByUsername(s);
			if(player != null) {
				TextHelper.chatMessageServer(player, message);
				player.playSound(SoundEvents.BLOCK_NOTE_BASS, 1.0F, 1.0F);
			}
		}
	}
	
	public static int getRegionFee(Region region) {
		//System.out.println(region.getArea() + " " + region.getMaxY() + " " + region.getMinY());
		//System.out.println(region.getArea() * (region.getMaxY() - region.getMinY()));
		//System.out.println((region.getArea() * (region.getMaxY() - region.getMinY())) * 25);
		int ysize = (region.getMaxY()-region.getMinY()) + 1;
		int size = (region.getArea() * ysize)*ConfigHandler.minerBlock.DMPerBlock;
		return size;
	}
	
	public static class RegionHandlerTask implements Consumer<Task> {
	    @Override
	    public void accept(Task task) {
			Set<Region> regions = RedProtectAPI.getAllRegions();
			String[] flags = {"build", "chest", "lever", "button", "door", "passives", "allow-mod", "fire"}; //Возможно нужно добавить и другие флаги //Глобально включить пвп?
			for (Region r : regions) {
				
				if(r.getLeaders().contains("#server#")) return;
				
				String key = r.getName() + "_" + r.getWorld(); //Назваие_Мир
				try {
					//if(RegionHandler.minersCache.containsKey(key)) {
					if(DBUtils.hasDBValue(key)) {
						int price = getRegionFee(r);
					
						for(World w : DimensionManager.getWorlds()) {
							if(w.getWorldInfo().getWorldName() == r.getWorld()) {
								//BlockPos pos = RegionHandler.minersCache.get(key);
								BlockPos pos = DBUtils.getDBValues(key);
								//Прогружаем чанк
								ChunkLoadUtils.loadChunk(w, pos);
							
								TileEntityMinerBlock tile = (TileEntityMinerBlock) w.getTileEntity(pos);
								//tile будет null, если из мира блок пропал, а из базы не был удален
								if(tile == null) {
									System.out.println("AtomCore: Tile in DB is missing in the world: " + tile + ", this will removed from database");
									DBUtils.deleteFromDB(key);
									return;
								}
								int curEnergy = tile.getEnergyStored();
								if(curEnergy >= price) {
									int newEnergy = curEnergy-price;
									tile.setEnergy(newEnergy, true);
									
									if(RedProtectAPI.getBoolFlag(r, flags[0]))
										RegionHandler.notifyPlayers(r, I18n.translateToLocalFormatted("message.enableprotect"));
									
									RegionHandler.setFlags(r, flags, "false");
							
								} else {
									RegionHandler.setFlags(r, flags, "true");
									RegionHandler.notifyPlayers(r, I18n.translateToLocalFormatted("message.disableprotect"));
								}
								//Отгружаем чанк
								ChunkLoadUtils.unloadChunk(w, pos);
							}
						}
					} else {
						RegionHandler.setFlags(r, flags, "true");
						RegionHandler.notifyPlayers(r, I18n.translateToLocalFormatted("message.disableprotect"));
					}	
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Region check task complete");
	    }
	}
	
	public static class RegionVisualTask implements Consumer<Task> {
	    @Override
	    public void accept(Task task) {
	    	List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getPlayerList().getPlayers();
	    	for(EntityPlayerMP player : players) {
	    		sendRegionsToPlayer(player);
	    	}
	    }
	}
	
	public static void sendRegionsToPlayer(EntityPlayer player) {
		//Формирует список приватов, преобразовываем в RegionData и отправлям
		Set<Region> regions = RedProtectAPI.getRegionsNear((Player) player, ConfigHandler.minerBlock.radiusGlobal);
		LinkedList<RegionData> regions_client = new LinkedList<RegionData>();
		

    	if (regions.size() > 0) {
    		Iterator<Region> i = regions.iterator();
    		while (i.hasNext()) {
    			Region r = i.next();
    			
            	Vec3 min = new Vec3(r.getMinMbrX(), r.getMinY(), r.getMinMbrZ());
            	Vec3 max = new Vec3(r.getMaxMbrX(), r.getMaxY(), r.getMaxMbrZ());
            	Area area = new Area(min, max, r.getWorld());
            	RegionData rg = new RegionData("T", area);
            	regions_client.add(rg);
    		}
    	}
    	AtomCore.network.sendTo(new MessageListRegions(0, SerializationUtils.serialize((Serializable) regions_client)), (EntityPlayerMP) player);
	}

}
