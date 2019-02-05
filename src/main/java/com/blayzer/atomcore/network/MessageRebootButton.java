package com.blayzer.atomcore.network;

import java.sql.SQLException;

import com.blayzer.atomcore.config.ConfigHandler;
import com.blayzer.atomcore.gui.ControllerContainerGui;
import com.blayzer.atomcore.handlers.RegionHandler;
import com.blayzer.atomcore.tileentities.TileEntityMinerBlock;
import com.blayzer.atomcore.util.DBUtils;
import com.blayzer.atomcore.util.RedProtectAPI;
import com.blayzer.atomcore.util.TextHelper;

import br.net.fabiozumbi12.RedProtect.Sponge.Region;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageRebootButton implements IMessage
{
	static int cooldown = 0;
	static int x;
	static int y;
	static int z;
	
    public MessageRebootButton() {
    }
    
    public MessageRebootButton(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void toBytes(final ByteBuf buffer) {
    	buffer.writeInt(x);
    	buffer.writeInt(y);
    	buffer.writeInt(z);
    }
    
    public void fromBytes(final ByteBuf buffer) {
    	this.x = buffer.readInt();
    	this.y = buffer.readInt();
    	this.z = buffer.readInt();
    }
    
    public static class Handler implements IMessageHandler<MessageRebootButton, IMessage>
    {
        public IMessage onMessage(final MessageRebootButton packet, final MessageContext message) {
        	
        	EntityPlayer player = message.getServerHandler().player;
        	World world = player.world;
        	BlockPos pos = new BlockPos(x, y, z);
        	TileEntityMinerBlock tile = (TileEntityMinerBlock) world.getTileEntity(pos);
        	try {
        	if (cooldown == 0) {
                if (tile.getEnergyStored() >= tile.getActivation()) {
                	if(tile.isProtectionActive()) {
                		TextHelper.chatMessageServer(player, I18n.translateToLocalFormatted("message.protectisactive"));
                		return null;
                	}
                		
                	//tile.changeEnergy(-tile.getActivation(), true);
                    //cooldown = ConfigHandler.minerBlock.cooldownLength;
                   
        			String[] flags = {"build", "chest", "lever", "button", "door", "passives", "allow-mod", "fire"}; //Возможно нужно добавить и другие флаги
        			Region region = tile.getRegion();
    				String key = region.getName() + "_" + region.getWorld(); //Назваие_Мир
    				
    				if(DBUtils.hasDBValue(key)) {
    					int price = tile.getActivation() + RegionHandler.getRegionFee(region);
    					
    					for(World w : DimensionManager.getWorlds()) {
    						if(w.getWorldInfo().getWorldName() == region.getWorld()) {
    							//BlockPos pos = DBUtils.getDBValues(key);
    							//Прогружаем чанк
    							//ChunkLoadUtils.loadChunk(w, pos);
    							
    							//TileEntityMinerBlock tile = (TileEntityMinerBlock) w.getTileEntity(pos);
    							int curEnergy = tile.getEnergyStored();
    							if(curEnergy >= price) {
    								int newEnergy = curEnergy-price;
    								tile.setEnergy(newEnergy, true);
    								
    								if(RedProtectAPI.getBoolFlag(region, flags[0]))
    									RegionHandler.notifyPlayers(region, I18n.translateToLocalFormatted("message.enableprotect"));
    								
    								RegionHandler.setFlags(region, flags, "false");
    							
    							} else {
    								RegionHandler.setFlags(region, flags, "true");
    								RegionHandler.notifyPlayers(region, I18n.translateToLocalFormatted("message.disableprotect"));
    							}
    							//Отгружаем чанк
    							//ChunkLoadUtils.unloadChunk(w, pos);
    						}
    					}
    				} else {
    					RegionHandler.setFlags(region, flags, "true");
    					RegionHandler.notifyPlayers(region, I18n.translateToLocalFormatted("message.disableprotect"));
    				}
                    
                } else {
                    TextHelper.chatMessageServer(player, I18n.translateToLocalFormatted("message.noenergy", TextHelper.getEnergyText(tile.getEnergyStored())));
                }
            } else {
            	TextHelper.chatMessageServer(player, I18n.translateToLocalFormatted("message.cooldown", cooldown));
            }
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
            return null;
        }
    }
}
